package analizador_lexico.maquina_estados;

import analizador_lexico.*;

import java.util.ArrayList;
import java.util.List;

public class MaquinaEstados {
    private final TransicionEstado[][] maquinaEstados = new TransicionEstado[Estado.TOTAL_ESTADOS][Input.TOTAL_INPUTS]; //[filas][columnas].

    private int estadoActual = Estado.INICIAL;

    private final List<Object> listaToken = new ArrayList<>();

    private char ultimoCaracterLeido;

    /**
     * Acciones semanticas.
     */

    private final AccionSemantica accionGeneral, inicStringVacio, concatenaChar, checkLongString, devuelveUltimoLeido,
        generaTokenId;
    private final AccionSemantica consumeChar;
    private final AccionSemantica cuentaSaltoLinea;



    /**
     * Constructor.
     *
     * @param codigoFuente ESTA VARIABLE HAY QUE PASARSELA A LAS ASs QUE DEVUELVEN EL ULTIMO CARACTER.
     */
    public MaquinaEstados(CodigoFuente codigoFuente, FileProcessor fileProcessor){
        Reservado tablaPR = new Reservado();
        TablaDeSimbolos tablaS = new TablaDeSimbolos();

        /* Inicializacion de acciones semanticas */
        accionGeneral = new AccionSemantica(tablaPR);
        inicStringVacio = new AccionSemantica.InicStringVacio(tablaPR); //0
        concatenaChar = new AccionSemantica.ConcatenaChar(tablaPR,codigoFuente); //1
        checkLongString = new AccionSemantica.CheckLongString(tablaPR,fileProcessor); //2
        devuelveUltimoLeido = new AccionSemantica.DevuelveUltimoLeido(tablaPR,codigoFuente); //3
        generaTokenId = new AccionSemantica.GeneraTokenId(tablaPR,tablaS); //4

        consumeChar = new AccionSemantica.ConsumeChar(tablaPR,codigoFuente);

        cuentaSaltoLinea = new AccionSemantica.CuentaSaltoLinea(tablaPR); //12

        /* Inicializacion estados */
        inicTransicionesInicial(inicStringVacio,concatenaChar,cuentaSaltoLinea);
        inicDeteccionId();
        inicDeteccionPR();
        inicInicioComent();
        inicCuerpoComent();
        inicComparacion();
        inicDeteccionCtes();
        inicCadena();
    }

    /**
     * Ejecuta la accion semantica del estado y avanza al siguiente.
     *
     * @param charInput caracter leido.
     */
    public void transicionar(char charInput){
        ultimoCaracterLeido = charInput;

        int codigoInput = Input.charToInt(charInput); //Obtiene el codigo asociado al caracter leido.

        TransicionEstado transicionEstado = maquinaEstados[estadoActual][codigoInput];

        transicionEstado.ejecutarAccionSemantica();
        estadoActual = transicionEstado.siguienteEstado();
    }

    /**
     * Agrega un token a la lista de token. Solo es usado por aquellas AS a las que le corresponda generar tokens.
     *
     * @param token token a agregar.
     */
    public void agregarToken(Object token){
        listaToken.add(token);
    }

    /**
     * Obtiene la lista de tokens generada al transicionar en la maquina.
     *
     * @return la lista de tokens generada.
     */
    public List<Object> getListaToken(){
        return listaToken;
    }

    /**
     * Obtiene el ultimo caracter que fue leido por la maquina. Solo es usado por aquellas AS que deban almacenar el
     * ultimo char que se leyo.
     *
     * @return el ultimo caracter leido.
     */
    public char getUltimoCaracterLeido() {
        return ultimoCaracterLeido;
    }

    /**
     * Establece una transicion predeterminada para un estado en especifico.
     *
     * @param estadoOrigen estado desde donde partira la transicion.
     * @param estadoDestino estado al cual se llega luego de la transicion.
     * @param accionesSemanticas acciones semanticas a ejecutar al transicionar.
     */
    private void inicTransiciones(int estadoOrigen, int estadoDestino, AccionSemantica... accionesSemanticas){
        for (int input = 0; input < Input.TOTAL_INPUTS; input++)
            maquinaEstados[estadoOrigen][input] = new TransicionEstado(estadoDestino,accionesSemanticas);
    }

    /**
     * Inicializacion estado 0.
     */
    private void inicTransicionesInicial(AccionSemantica inicStringVacio, AccionSemantica concatenaChar,
                                         AccionSemantica cuentaSaltoLinea) {
        inicTransiciones(Estado.INICIAL,Estado.ERR_SIMBOLO_INV,null);

        //Descartables.
        maquinaEstados[Estado.INICIAL][Input.DESCARTABLE] = new TransicionEstado(Estado.INICIAL,null);
        maquinaEstados[Estado.INICIAL][Input.SALTO_LINEA] = new TransicionEstado(Estado.INICIAL,cuentaSaltoLinea);

        //Ids.
        maquinaEstados[Estado.INICIAL][Input.D_MINUSC] = new TransicionEstado(Estado.DETECCION_ID,inicStringVacio,concatenaChar);
        maquinaEstados[Estado.INICIAL][Input.U_MINUSC] = new TransicionEstado(Estado.DETECCION_ID,inicStringVacio,concatenaChar);
        maquinaEstados[Estado.INICIAL][Input.I_MINUSC] = new TransicionEstado(Estado.DETECCION_ID,inicStringVacio,concatenaChar);
        maquinaEstados[Estado.INICIAL][Input.LETRA_MINUSC] = new TransicionEstado(Estado.DETECCION_ID,inicStringVacio,concatenaChar);

        //PRs.
        maquinaEstados[Estado.INICIAL][Input.LETRA_MAYUS] = new TransicionEstado(Estado.DETECCION_PR,inicStringVacio,concatenaChar);

        //Ctes.
        maquinaEstados[Estado.INICIAL][Input.DIGITO] = new TransicionEstado(Estado.CTE_PARTE_ENTERA,inicStringVacio,concatenaChar);
        maquinaEstados[Estado.INICIAL][Input.PUNTO] = new TransicionEstado(Estado.CTE_PARTE_DECIM,inicStringVacio,concatenaChar);

        //Comentarios.
        maquinaEstados[Estado.INICIAL][Input.PORCENTAJE] = new TransicionEstado(Estado.INICIO_COMENT,null);

        //Comparaciones y asignacion.
        maquinaEstados[Estado.INICIAL][Input.MENOR] = new TransicionEstado(Estado.COMP_MENOR,null);
        maquinaEstados[Estado.INICIAL][Input.MAYOR] = new TransicionEstado(Estado.COMP_MAYOR,null);
        maquinaEstados[Estado.INICIAL][Input.ADMIRACION] = new TransicionEstado(Estado.COMP_DISTINTO,null);
        maquinaEstados[Estado.INICIAL][Input.IGUAL] = new TransicionEstado(Estado.SIGNO_IGUAL,null);

        //Tokens unitarios.
        //TODO: Las siguientes transiciones tienen que tener una AS que retornen el token especifico asociado al caracter.
        maquinaEstados[Estado.INICIAL][Input.SUMA] = new TransicionEstado(Estado.FINAL,null);
        maquinaEstados[Estado.INICIAL][Input.RESTA] = new TransicionEstado(Estado.FINAL,null);
        maquinaEstados[Estado.INICIAL][Input.MULTIPL] = new TransicionEstado(Estado.FINAL,null);
        maquinaEstados[Estado.INICIAL][Input.DIV] = new TransicionEstado(Estado.FINAL,null);
        maquinaEstados[Estado.INICIAL][Input.CORCHETE_A] = new TransicionEstado(Estado.FINAL,null);
        maquinaEstados[Estado.INICIAL][Input.CORCHETE_C] = new TransicionEstado(Estado.FINAL,null);
        maquinaEstados[Estado.INICIAL][Input.PARENT_A] = new TransicionEstado(Estado.FINAL,null);
        maquinaEstados[Estado.INICIAL][Input.PARENT_C] = new TransicionEstado(Estado.FINAL,null);
        maquinaEstados[Estado.INICIAL][Input.COMA] = new TransicionEstado(Estado.FINAL,null);
        maquinaEstados[Estado.INICIAL][Input.PUNTO_COMA] = new TransicionEstado(Estado.FINAL,null);

        //Cadena multilinea.
        maquinaEstados[Estado.INICIAL][Input.COMILLA] = new TransicionEstado(Estado.CADENA,inicStringVacio,concatenaChar);
    }

    /**
     * Inicializacion estado 1.
     */
    private void inicDeteccionId(AccionSemantica concatenaChar, AccionSemantica checkLongString,
                                 AccionSemantica generaTokenId, AccionSemantica devuelveUltimoLeido) {
        inicTransiciones(Estado.DETECCION_ID,Estado.FINAL,checkLongString,generaTokenId,devuelveUltimoLeido);

        maquinaEstados[Estado.DETECCION_ID][Input.D_MINUSC] = new TransicionEstado(Estado.DETECCION_ID,concatenaChar);
        maquinaEstados[Estado.DETECCION_ID][Input.U_MINUSC] = new TransicionEstado(Estado.DETECCION_ID,concatenaChar);
        maquinaEstados[Estado.DETECCION_ID][Input.I_MINUSC] = new TransicionEstado(Estado.DETECCION_ID,concatenaChar);
        maquinaEstados[Estado.DETECCION_ID][Input.LETRA_MINUSC] = new TransicionEstado(Estado.DETECCION_ID,concatenaChar);
        maquinaEstados[Estado.DETECCION_ID][Input.DIGITO] = new TransicionEstado(Estado.DETECCION_ID,concatenaChar);
        maquinaEstados[Estado.DETECCION_ID][Input.GUION_B] = new TransicionEstado(Estado.DETECCION_ID,concatenaChar);
    }

    /**
     * Inicializacion estado 2.
     */
    private void inicDeteccionPR(){
        inicTransiciones(Estado.DETECCION_PR,Estado.FINAL,"4");

        maquinaEstados[Estado.DETECCION_PR][Input.LETRA_MAYUS] = new TransicionEstado(Estado.DETECCION_PR,"");
        maquinaEstados[Estado.DETECCION_PR][Input.GUION_B] = new TransicionEstado(Estado.DETECCION_PR,"");
    }

    /**
     * Inicializacion estado 3.
     */
    private void inicInicioComent(){
        inicTransiciones(Estado.INICIO_COMENT,Estado.ERR_SIMBOLO_INV,"");

        maquinaEstados[Estado.INICIO_COMENT][Input.PORCENTAJE] = new TransicionEstado(Estado.CUERPO_COMENT,"");
    }

    /**
     * Inicializacion estado 4.
     */
    private void inicCuerpoComent(){
        inicTransiciones(Estado.CUERPO_COMENT,Estado.CUERPO_COMENT,"");

        maquinaEstados[Estado.CUERPO_COMENT][Input.SALTO_LINEA] = new TransicionEstado(Estado.FINAL,"12");
    }

    /**
     * Inicializacion estados 5, 6, 7 y 8.
     */
    private void inicComparacion(){
        //Comparacion por menor (5).
        inicTransiciones(Estado.COMP_MENOR,Estado.FINAL,"6");
        maquinaEstados[Estado.COMP_MENOR][Input.IGUAL] = new TransicionEstado(Estado.FINAL,"5");

        //Comparacion por mayor (6).
        inicTransiciones(Estado.COMP_MAYOR,Estado.FINAL,"6");
        maquinaEstados[Estado.COMP_MAYOR][Input.IGUAL] = new TransicionEstado(Estado.FINAL,"5");

        //Comparacion por distincion (7).
        inicTransiciones(Estado.COMP_DISTINTO,Estado.ERR_SIMBOLO_INV,"6"); //'!' por si solo no es valido.
        maquinaEstados[Estado.COMP_DISTINTO][Input.IGUAL] = new TransicionEstado(Estado.FINAL,"5");

        //Asignacion (8a).
        inicTransiciones(Estado.SIGNO_IGUAL,Estado.FINAL,"6");

        //Comparacion por igualdad (8b).
        maquinaEstados[Estado.SIGNO_IGUAL][Input.IGUAL] = new TransicionEstado(Estado.FINAL,"5");
    }

    /**
     * Inicializacion estados 9, 10, 11, 12, 13, 14.
     */
    private void inicDeteccionCtes(){
        //Parte entera (9).
        inicTransiciones(Estado.CTE_PARTE_ENTERA, Estado.ERR_SIMBOLO_INV,"6");

        maquinaEstados[Estado.CTE_PARTE_ENTERA][Input.DIGITO] = new TransicionEstado(Estado.CTE_PARTE_ENTERA,"1");
        maquinaEstados[Estado.CTE_PARTE_ENTERA][Input.GUION_B] =
                new TransicionEstado(Estado.CTE_UI_SUF1,""); //Salto a deteccion de sufijo para UIs.
        maquinaEstados[Estado.CTE_PARTE_ENTERA][Input.PUNTO] =
                new TransicionEstado(Estado.CTE_PARTE_DECIM,"1"); //Salto a parte decimal de doubles.
        maquinaEstados[Estado.CTE_PARTE_ENTERA][Input.D_MINUSC] =
                new TransicionEstado(Estado.CTE_PARTE_EXP,"1"); //Salto a parte exponencial de doubles.

        //Sufijo1 (10).
        inicTransiciones(Estado.CTE_UI_SUF1, Estado.ERR_SIMBOLO_INV,"6");
        maquinaEstados[Estado.CTE_UI_SUF1][Input.U_MINUSC] = new TransicionEstado(Estado.CTE_UI_SUF2,"");

        //Sufijo2 (11).
        inicTransiciones(Estado.CTE_UI_SUF2, Estado.ERR_SIMBOLO_INV,"6");
        maquinaEstados[Estado.CTE_UI_SUF2][Input.I_MINUSC] = new TransicionEstado(Estado.CTE_UI_SUF3,"");

        //Sufijo3 (12).
        inicTransiciones(Estado.CTE_UI_SUF3, Estado.FINAL,"6, 10");

        //Parte decimal (13).
        inicTransiciones(Estado.CTE_PARTE_DECIM, Estado.FINAL,"6,11");
        maquinaEstados[Estado.CTE_PARTE_DECIM][Input.D_MINUSC] =
                new TransicionEstado(Estado.CTE_PARTE_EXP,"1"); //Salto a parte exponencial de doubles.
        maquinaEstados[Estado.CTE_PARTE_DECIM][Input.DIGITO] = new TransicionEstado(Estado.CTE_PARTE_DECIM,"1");

        //Parte exponencial (14).
        inicTransiciones(Estado.CTE_PARTE_EXP, Estado.FINAL,"6,11");
        maquinaEstados[Estado.CTE_PARTE_EXP][Input.DIGITO] = new TransicionEstado(Estado.CTE_PARTE_EXP,"1");
    }

    /**
     * Inicializacion estado 15.
     */
    private void inicCadena(){
        inicTransiciones(Estado.CADENA,Estado.CADENA,"6");
        maquinaEstados[Estado.CADENA][Input.SALTO_LINEA] = new TransicionEstado(Estado.CADENA,"12");
        maquinaEstados[Estado.CADENA][Input.COMILLA] = new TransicionEstado(Estado.FINAL,"");
    }
}
