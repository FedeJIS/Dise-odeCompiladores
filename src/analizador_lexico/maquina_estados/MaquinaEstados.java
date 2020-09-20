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
     * Valores para tokens (REEMPLAZAR POR LOS VALORES QUE DA YACC).
     */
    private final int tokenId = 0, tokenPR = 1, tokenMenor = 2, tokenMenorIgual = 3, tokenMayor = 4, tokenMayorIgual = 5,
        tokenDistinto = 6, tokenAsignacion = 7, tokenIgual = 8, tokenSuma = 9, tokenResta = 10, tokenMultipl = 11,
        tokenDiv = 12, tokenCorcheteA = 13, tokenCorcheteC = 14, tokenParentA = 15, tokenParentC = 16, tokenPunto = 17,
        tokenPuntoComa = 18, tokenUINT = 19;


    /**
     * Constructor.
     *
     * @param codigoFuente ESTA VARIABLE HAY QUE PASARSELA A LAS ASs QUE DEVUELVEN EL ULTIMO CARACTER.
     */
    public MaquinaEstados(CodigoFuente codigoFuente, FileProcessor fileProcessor){
        Reservado tablaPR = new Reservado();
        TablaDeSimbolos tablaS = new TablaDeSimbolos();

        /* Inicializacion de acciones semanticas */
        AccionSemantica inicStringVacio = new AccionSemantica.InicStringVacio(); //0
        AccionSemantica concatenaChar = new AccionSemantica.ConcatenaChar(codigoFuente); //1
        AccionSemantica truncaId = new AccionSemantica.TruncaId(fileProcessor); //2
        AccionSemantica devuelveUltimoLeido = new AccionSemantica.DevuelveUltimoLeido(codigoFuente); //3
        AccionSemantica generaTokenId = new AccionSemantica.GeneraTokenId(this, tablaS, tokenId); //4
        AccionSemantica generaTokenPR = new AccionSemantica.GeneraTokenPR(this, tablaPR, tokenPR); //5
        AccionSemantica consumeChar = new AccionSemantica.ConsumeChar(codigoFuente); //7

        AccionSemantica cuentaSaltoLinea = new AccionSemantica.CuentaSaltoLinea(tablaPR); //12

        /* Inicializacion estados */
        inicTransicionesInicial(inicStringVacio, concatenaChar, cuentaSaltoLinea);
        inicDeteccionId(concatenaChar, truncaId, generaTokenId, devuelveUltimoLeido, cuentaSaltoLinea);
        inicDeteccionPR(concatenaChar, devuelveUltimoLeido, generaTokenPR, cuentaSaltoLinea);
        inicInicioComent(cuentaSaltoLinea);
        inicCuerpoComent(cuentaSaltoLinea);
        inicComparacion(devuelveUltimoLeido, consumeChar, cuentaSaltoLinea);
//        inicDeteccionCtes();
        inicCadena(concatenaChar, cuentaSaltoLinea);
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
            maquinaEstados[estadoOrigen][input] = new TransicionEstado(estadoDestino, accionesSemanticas);
    }

    /**
     * Inicializacion estado 0.
     */
    private void inicTransicionesInicial(AccionSemantica inicStringVacio, AccionSemantica concatenaChar,
                                         AccionSemantica cuentaSaltoLinea) {
        inicTransiciones(Estado.INICIAL,Estado.ERR_SIMBOLO_INV,null); //TODO Agregar AS para errores.

        //Descartables.
        maquinaEstados[Estado.INICIAL][Input.DESCARTABLE] = new TransicionEstado(Estado.INICIAL);
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
        maquinaEstados[Estado.INICIAL][Input.PORCENTAJE] = new TransicionEstado(Estado.INICIO_COMENT);

        //Comparaciones y asignacion.
        maquinaEstados[Estado.INICIAL][Input.MENOR] = new TransicionEstado(Estado.COMP_MENOR);
        maquinaEstados[Estado.INICIAL][Input.MAYOR] = new TransicionEstado(Estado.COMP_MAYOR);
        maquinaEstados[Estado.INICIAL][Input.ADMIRACION] = new TransicionEstado(Estado.COMP_DISTINTO);
        maquinaEstados[Estado.INICIAL][Input.IGUAL] = new TransicionEstado(Estado.SIGNO_IGUAL);

        AccionSemantica generaToken;

        //Token suma.
        generaToken = new AccionSemantica.GeneraTokenUnitario(this,tokenSuma);
        maquinaEstados[Estado.INICIAL][Input.SUMA] = new TransicionEstado(Estado.FINAL,generaToken);

        //Token resta.
        generaToken = new AccionSemantica.GeneraTokenUnitario(this,tokenResta);
        maquinaEstados[Estado.INICIAL][Input.RESTA] = new TransicionEstado(Estado.FINAL,generaToken);

        //Token multiplicacion.
        generaToken = new AccionSemantica.GeneraTokenUnitario(this,tokenMultipl);
        maquinaEstados[Estado.INICIAL][Input.MULTIPL] = new TransicionEstado(Estado.FINAL,generaToken);

        //Token division.
        generaToken = new AccionSemantica.GeneraTokenUnitario(this,tokenDiv);
        maquinaEstados[Estado.INICIAL][Input.DIV] = new TransicionEstado(Estado.FINAL,generaToken);

        //Token corchete abierto.
        generaToken = new AccionSemantica.GeneraTokenUnitario(this,tokenCorcheteA);
        maquinaEstados[Estado.INICIAL][Input.CORCHETE_A] = new TransicionEstado(Estado.FINAL,generaToken);

        //Token corchete cerrado.
        generaToken = new AccionSemantica.GeneraTokenUnitario(this,tokenCorcheteC);
        maquinaEstados[Estado.INICIAL][Input.CORCHETE_C] = new TransicionEstado(Estado.FINAL,generaToken);

        //Token parentesis abierto.
        generaToken = new AccionSemantica.GeneraTokenUnitario(this,tokenParentA);
        maquinaEstados[Estado.INICIAL][Input.PARENT_A] = new TransicionEstado(Estado.FINAL,generaToken);

        //Token parentesis cerrado.
        generaToken = new AccionSemantica.GeneraTokenUnitario(this,tokenParentC);
        maquinaEstados[Estado.INICIAL][Input.PARENT_C] = new TransicionEstado(Estado.FINAL,generaToken);

        //Token punto.
        generaToken = new AccionSemantica.GeneraTokenUnitario(this, tokenPunto);
        maquinaEstados[Estado.INICIAL][Input.COMA] = new TransicionEstado(Estado.FINAL,generaToken);

        //Token punto y coma.
        generaToken = new AccionSemantica.GeneraTokenUnitario(this,tokenPuntoComa);
        maquinaEstados[Estado.INICIAL][Input.PUNTO_COMA] = new TransicionEstado(Estado.FINAL,generaToken);

        //Cadena multilinea.
        maquinaEstados[Estado.INICIAL][Input.COMILLA] = new TransicionEstado(Estado.CADENA,inicStringVacio,concatenaChar);
    }

    /**
     * Inicializacion estado 1.
     */
    private void inicDeteccionId(AccionSemantica concatenaChar, AccionSemantica truncaId,
                                 AccionSemantica generaTokenId, AccionSemantica devuelveUltimoLeido,
                                 AccionSemantica cuentaSaltoLinea) {
        inicTransiciones(Estado.DETECCION_ID,Estado.FINAL,truncaId,generaTokenId,devuelveUltimoLeido);

        maquinaEstados[Estado.DETECCION_ID][Input.SALTO_LINEA] = new TransicionEstado(Estado.FINAL,truncaId,generaTokenId,
                devuelveUltimoLeido, cuentaSaltoLinea); //Permite contar un salto de linea (No devuelve el ultimo leido pq se descartaria de todas formas).
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
    private void inicDeteccionPR(AccionSemantica concatenaChar, AccionSemantica devuelveUltimoLeido,
                                 AccionSemantica generaTokenPR, AccionSemantica cuentaSaltoLinea){
        inicTransiciones(Estado.DETECCION_PR,Estado.FINAL,generaTokenPR,devuelveUltimoLeido);

        maquinaEstados[Estado.DETECCION_PR][Input.SALTO_LINEA] = new TransicionEstado(Estado.FINAL,generaTokenPR,
                cuentaSaltoLinea); //Permite contar un salto de linea (No devuelve el ultimo leido pq se descartaria de todas formas).
        maquinaEstados[Estado.DETECCION_PR][Input.LETRA_MAYUS] = new TransicionEstado(Estado.DETECCION_PR,concatenaChar);
        maquinaEstados[Estado.DETECCION_PR][Input.GUION_B] = new TransicionEstado(Estado.DETECCION_PR,concatenaChar);
    }

    /**
     * Inicializacion estado 3.
     */
    private void inicInicioComent(AccionSemantica cuentaSaltoLinea){
        inicTransiciones(Estado.INICIO_COMENT,Estado.ERR_SIMBOLO_INV,null); //TODO Agregar AS para errores.

        maquinaEstados[Estado.INICIO_COMENT][Input.SALTO_LINEA] = new TransicionEstado(Estado.ERR_SIMBOLO_INV,
                cuentaSaltoLinea); //Permite contar un salto de linea.
        maquinaEstados[Estado.INICIO_COMENT][Input.PORCENTAJE] = new TransicionEstado(Estado.CUERPO_COMENT);
    }

    /**
     * Inicializacion estado 4.
     */
    private void inicCuerpoComent(AccionSemantica cuentaSaltoLinea){
        inicTransiciones(Estado.CUERPO_COMENT,Estado.CUERPO_COMENT);

        maquinaEstados[Estado.CUERPO_COMENT][Input.SALTO_LINEA] = new TransicionEstado(Estado.FINAL,cuentaSaltoLinea);
    }

    /**
     * Inicializacion estados 5, 6, 7 y 8.
     */
    private void inicComparacion(AccionSemantica devuelveUltimoLeido, AccionSemantica consumeChar, AccionSemantica cuentaSaltoLinea){
        AccionSemantica generaToken;

        //Comparacion por menor estricto (5).
        generaToken = new AccionSemantica.GeneraTokenUnitario(this,tokenMenor);
        inicTransiciones(Estado.COMP_MENOR,Estado.FINAL,devuelveUltimoLeido,generaToken);
        maquinaEstados[Estado.COMP_MENOR][Input.SALTO_LINEA] = new TransicionEstado(Estado.FINAL,generaToken,
                cuentaSaltoLinea); //Permite contar un salto de linea (No devuelve el ultimo leido pq se descartaria de todas formas).

        //Comparacion por menor igual (5).
        generaToken = new AccionSemantica.GeneraTokenUnitario(this,tokenMenorIgual);
        maquinaEstados[Estado.COMP_MENOR][Input.IGUAL] = new TransicionEstado(Estado.FINAL,consumeChar,generaToken);

        //Comparacion por mayor estricto (6).
        generaToken = new AccionSemantica.GeneraTokenUnitario(this,tokenMayor);
        inicTransiciones(Estado.COMP_MAYOR,Estado.FINAL,devuelveUltimoLeido,generaToken);
        maquinaEstados[Estado.COMP_MAYOR][Input.SALTO_LINEA] = new TransicionEstado(Estado.FINAL,generaToken,
                cuentaSaltoLinea); //Permite contar un salto de linea (No devuelve el ultimo leido pq se descartaria de todas formas).

        //Comparacion por mayor igual (6).
        generaToken = new AccionSemantica.GeneraTokenUnitario(this,tokenMayorIgual);
        maquinaEstados[Estado.COMP_MAYOR][Input.IGUAL] = new TransicionEstado(Estado.FINAL,consumeChar,generaToken);

        //'!' solo (7).
        inicTransiciones(Estado.COMP_DISTINTO,Estado.ERR_SIMBOLO_INV,devuelveUltimoLeido); //'!' por si solo no es valido.
        maquinaEstados[Estado.COMP_DISTINTO][Input.SALTO_LINEA] = new TransicionEstado(Estado.ERR_SIMBOLO_INV,
                cuentaSaltoLinea); //Permite contar un salto de linea (No devuelve el ultimo leido pq se descartaria de todas formas).

        //Comparacion por distincion (7).
        generaToken = new AccionSemantica.GeneraTokenUnitario(this,tokenDistinto);
        maquinaEstados[Estado.COMP_DISTINTO][Input.IGUAL] = new TransicionEstado(Estado.FINAL,consumeChar,generaToken);

        //Asignacion (8).
        generaToken = new AccionSemantica.GeneraTokenUnitario(this,tokenAsignacion);
        inicTransiciones(Estado.SIGNO_IGUAL,Estado.FINAL,devuelveUltimoLeido, generaToken);
        maquinaEstados[Estado.SIGNO_IGUAL][Input.SALTO_LINEA] = new TransicionEstado(Estado.FINAL, generaToken,
                cuentaSaltoLinea); //Permite contar un salto de linea (No devuelve el ultimo leido pq se descartaria de todas formas).

        //Comparacion por igualdad (8).
        generaToken = new AccionSemantica.GeneraTokenUnitario(this,tokenIgual);
        maquinaEstados[Estado.SIGNO_IGUAL][Input.IGUAL] = new TransicionEstado(Estado.FINAL,consumeChar, generaToken);
    }

    /**
     * Inicializacion estados 9, 10, 11, 12, 13, 14.
     */
    private void inicDeteccionCtes(AccionSemantica concatenaChar, AccionSemantica devuelveUltimoLeido,
                                   AccionSemantica cuentaSaltoLinea){
        //Parte entera (9).
        inicTransiciones(Estado.CTE_PARTE_ENTERA, Estado.ERR_SIMBOLO_INV,devuelveUltimoLeido); //TODO: Notificar error.

        maquinaEstados[Estado.CTE_PARTE_ENTERA][Input.DIGITO] = new TransicionEstado(Estado.CTE_PARTE_ENTERA,concatenaChar);
        maquinaEstados[Estado.CTE_PARTE_ENTERA][Input.GUION_B] = new TransicionEstado(Estado.CTE_UI_SUF1); //Salto a deteccion de sufijo para UIs.
        maquinaEstados[Estado.CTE_PARTE_ENTERA][Input.PUNTO] = new TransicionEstado(Estado.CTE_PARTE_DECIM,concatenaChar); //Salto a parte decimal de doubles.
//        maquinaEstados[Estado.CTE_PARTE_ENTERA][Input.D_MINUSC] = new TransicionEstado(Estado.CTE_PARTE_EXP,"1"); //Salto a parte exponencial de doubles.

        //Sufijo1 (10).
        inicTransiciones(Estado.CTE_UI_SUF1, Estado.ERR_SIMBOLO_INV,devuelveUltimoLeido); //TODO: Notificar error.
        maquinaEstados[Estado.CTE_UI_SUF1][Input.SALTO_LINEA] = new TransicionEstado(Estado.ERR_SIMBOLO_INV,
                cuentaSaltoLinea); //Permite contar un salto de linea (No devuelve el ultimo leido pq se descartaria de todas formas).
        maquinaEstados[Estado.CTE_UI_SUF1][Input.U_MINUSC] = new TransicionEstado(Estado.CTE_UI_SUF2);

        //Sufijo2 (11).
        inicTransiciones(Estado.CTE_UI_SUF2, Estado.ERR_SIMBOLO_INV,devuelveUltimoLeido); //TODO: Notificar error.
        maquinaEstados[Estado.CTE_UI_SUF2][Input.SALTO_LINEA] = new TransicionEstado(Estado.ERR_SIMBOLO_INV,
                cuentaSaltoLinea); //Permite contar un salto de linea (No devuelve el ultimo leido pq se descartaria de todas formas).
        maquinaEstados[Estado.CTE_UI_SUF2][Input.I_MINUSC] = new TransicionEstado(Estado.CTE_UI_SUF3);

        //Sufijo3 (12).
        AccionSemantica generaTokenUINT = new AccionSemantica.GeneraTokenUINT(this,tokenUINT);
        inicTransiciones(Estado.CTE_UI_SUF3, Estado.FINAL,devuelveUltimoLeido, generaTokenUINT);
        maquinaEstados[Estado.CTE_UI_SUF3][Input.SALTO_LINEA] = new TransicionEstado(Estado.ERR_SIMBOLO_INV,
                cuentaSaltoLinea); //Permite contar un salto de linea (No devuelve el ultimo leido pq se descartaria de todas formas).

        //Parte decimal (13).
//        inicTransiciones(Estado.CTE_PARTE_DECIM, Estado.FINAL, devuelveUltimoLeido, );
//        maquinaEstados[Estado.CTE_PARTE_DECIM][Input.D_MINUSC] = new TransicionEstado(Estado.CTE_PARTE_EXP,"1"); //Salto a parte exponencial de doubles.
//        maquinaEstados[Estado.CTE_PARTE_DECIM][Input.DIGITO] = new TransicionEstado(Estado.CTE_PARTE_DECIM,"1");

        //Parte exponencial (14).
//        inicTransiciones(Estado.CTE_PARTE_EXP, Estado.FINAL,"6,11");
//        maquinaEstados[Estado.CTE_PARTE_EXP][Input.DIGITO] = new TransicionEstado(Estado.CTE_PARTE_EXP,"1");
    }

    /**
     * Inicializacion estado 15.
     */
    private void inicCadena(AccionSemantica concatenaChar, AccionSemantica cuentaSaltoLinea){
        inicTransiciones(Estado.CADENA,Estado.CADENA,concatenaChar);
        maquinaEstados[Estado.CADENA][Input.SALTO_LINEA] = new TransicionEstado(Estado.CADENA,concatenaChar, cuentaSaltoLinea);
        maquinaEstados[Estado.CADENA][Input.COMILLA] = new TransicionEstado(Estado.FINAL,concatenaChar);
    }
}