package analizador_lexico.maquina_estados;

import analizador_lexico.*;
import util.CodigoFuente;
import util.FileProcessor;
import util.Reservado;
import util.tabla_simbolos.TablaDeSimbolos;

public class MaquinaEstados {
    private final TransicionEstado[][] maquinaEstados = new TransicionEstado[Estado.TOTAL_ESTADOS][Input.TOTAL_INPUTS]; //[filas][columnas].

    private int estadoActual = Estado.INICIAL;

    private final AnalizadorLexico aLexico; //Permite agregar tokens a medida que se generan.

    /**
     * Constructor.
     */
    public MaquinaEstados(AnalizadorLexico aLexico, FileProcessor fileProcessor, CodigoFuente codigoFuente,
                          TablaDeSimbolos tablaS, Reservado tablaPR){
        this.aLexico = aLexico;

        /* Inicializacion de acciones semanticas */
        AccionSemantica inicStringVacio = new AccionSemantica.InicStringVacio(); //0
        AccionSemantica concatenaChar = new AccionSemantica.ConcatenaChar(codigoFuente); //1
        AccionSemantica truncaId = new AccionSemantica.TruncaId(fileProcessor); //2
        AccionSemantica devuelveUltimoLeido = new AccionSemantica.DevuelveUltimoLeido(codigoFuente); //3
        AccionSemantica generTokenId = new AccionSemantica.GeneraTokenTS(this, tablaS, aLexico.T_ID); //4
        AccionSemantica generaTokenCadena = new AccionSemantica.GeneraTokenTS(this,tablaS,aLexico.T_CADENA); //4
        AccionSemantica generaTokenPR = new AccionSemantica.GeneraTokenPR(this, tablaPR); //5
        AccionSemantica generaTokenLiteral = new AccionSemantica.GeneraTokenLiteral(this,codigoFuente); //6
        AccionSemantica consumeChar = new AccionSemantica.ConsumeChar(codigoFuente); //7
        AccionSemantica generaTokenUINT = new AccionSemantica.GeneraTokenUINT(this, tablaS, aLexico.T_CTE_UINT); //8
        AccionSemantica generaTokenDouble = new AccionSemantica.GeneraTokenDouble(this,tablaS,aLexico.T_CTE_DOUBLE); //10
        AccionSemantica cuentaSaltoLinea = new AccionSemantica.CuentaSaltoLinea(); //12

        /* Inicializacion estados */
        inicTransicionesInicial(inicStringVacio, concatenaChar, cuentaSaltoLinea, generaTokenLiteral);
        inicDeteccionId(concatenaChar, truncaId, generTokenId, devuelveUltimoLeido, cuentaSaltoLinea);
        inicDeteccionPR(concatenaChar, devuelveUltimoLeido, generaTokenPR, cuentaSaltoLinea);
        inicInicioComent(cuentaSaltoLinea, devuelveUltimoLeido);
        inicCuerpoComent(cuentaSaltoLinea);
        inicComparacion(devuelveUltimoLeido, consumeChar, cuentaSaltoLinea,generaTokenLiteral);
        inicDeteccionCtes(concatenaChar, devuelveUltimoLeido, cuentaSaltoLinea, generaTokenUINT, generaTokenDouble, consumeChar);
        inicCadena(concatenaChar, cuentaSaltoLinea, generaTokenCadena);
    }

    /**
     * @return true si la maquina esta en el estado final, false si no lo esta.
     */
    public boolean estadoFinalAlcanzado(){
        return estadoActual == Estado.FINAL;
    }

    public void reiniciar(){
        estadoActual = 0;
    }

    public void setVariablesSintactico(int token, String lexema){
        aLexico.setVariablesSintactico(token,lexema);
    }

    /**
     * Ejecuta la accion semantica del estado y avanza al siguiente.
     *
     * @param charInput caracter leido.
     */
    public void transicionar(char charInput){
        int codigoInput = Input.charToInt(charInput); //Obtiene el codigo asociado al caracter leido.

        TransicionEstado transicionEstado = maquinaEstados[estadoActual][codigoInput];

        estadoActual = transicionEstado.siguienteEstado();

        transicionEstado.ejecutarAccionSemantica();
    }

    public void transicionarEOF(){
        TransicionEstado transicionEstado = maquinaEstados[estadoActual][Input.EOF];

        transicionEstado.ejecutarAccionSemantica();

        estadoActual = Estado.FINAL; //Finalizo ejecucion
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
                                         AccionSemantica cuentaSaltoLinea, AccionSemantica generaTokenLiteral) {
        /*
         * Inputs no validos.
         * Si no transiciono al estado final, no detengo la ejecucion del lexico. Por eso me quedo en el estado inicial,
         * pero notifico el error.
         * TODO: Notificar error.
         */
        inicTransiciones(Estado.INICIAL,Estado.INICIAL);

        /* Descartables. */
        maquinaEstados[Estado.INICIAL][Input.DESCARTABLE] = new TransicionEstado(Estado.INICIAL);
        maquinaEstados[Estado.INICIAL][Input.SALTO_LINEA] = new TransicionEstado(Estado.INICIAL,cuentaSaltoLinea);

        /* Ids. */
        maquinaEstados[Estado.INICIAL][Input.D_MINUSC] = new TransicionEstado(Estado.DETECCION_ID,inicStringVacio,concatenaChar);
        maquinaEstados[Estado.INICIAL][Input.U_MINUSC] = new TransicionEstado(Estado.DETECCION_ID,inicStringVacio,concatenaChar);
        maquinaEstados[Estado.INICIAL][Input.I_MINUSC] = new TransicionEstado(Estado.DETECCION_ID,inicStringVacio,concatenaChar);
        maquinaEstados[Estado.INICIAL][Input.LETRA_MINUSC] = new TransicionEstado(Estado.DETECCION_ID,inicStringVacio,concatenaChar);

        /* PRs. */
        maquinaEstados[Estado.INICIAL][Input.LETRA_MAYUS] = new TransicionEstado(Estado.DETECCION_PR,inicStringVacio,concatenaChar);

        /* Ctes. */
        maquinaEstados[Estado.INICIAL][Input.DIGITO] = new TransicionEstado(Estado.CTE_PARTE_ENTERA,inicStringVacio,concatenaChar);
        maquinaEstados[Estado.INICIAL][Input.PUNTO] = new TransicionEstado(Estado.CTE_PARTE_DECIM,inicStringVacio,concatenaChar);

        /* Comentarios. */
        maquinaEstados[Estado.INICIAL][Input.PORCENTAJE] = new TransicionEstado(Estado.INICIO_COMENT);

        /* Comparaciones y asignacion. */
        maquinaEstados[Estado.INICIAL][Input.MENOR] = new TransicionEstado(Estado.COMP_MENOR);
        maquinaEstados[Estado.INICIAL][Input.MAYOR] = new TransicionEstado(Estado.COMP_MAYOR);
        maquinaEstados[Estado.INICIAL][Input.ADMIRACION] = new TransicionEstado(Estado.COMP_DISTINTO);
        maquinaEstados[Estado.INICIAL][Input.IGUAL] = new TransicionEstado(Estado.SIGNO_IGUAL);

        /* Tokens literales. */
        maquinaEstados[Estado.INICIAL][Input.SUMA] = new TransicionEstado(Estado.FINAL,generaTokenLiteral);
        maquinaEstados[Estado.INICIAL][Input.RESTA] = new TransicionEstado(Estado.FINAL,generaTokenLiteral);
        maquinaEstados[Estado.INICIAL][Input.MULTIPL] = new TransicionEstado(Estado.FINAL,generaTokenLiteral);
        maquinaEstados[Estado.INICIAL][Input.DIV] = new TransicionEstado(Estado.FINAL,generaTokenLiteral);
        maquinaEstados[Estado.INICIAL][Input.CORCHETE_A] = new TransicionEstado(Estado.FINAL,generaTokenLiteral);
        maquinaEstados[Estado.INICIAL][Input.CORCHETE_C] = new TransicionEstado(Estado.FINAL,generaTokenLiteral);
        maquinaEstados[Estado.INICIAL][Input.PARENT_A] = new TransicionEstado(Estado.FINAL,generaTokenLiteral);
        maquinaEstados[Estado.INICIAL][Input.PARENT_C] = new TransicionEstado(Estado.FINAL,generaTokenLiteral);
        maquinaEstados[Estado.INICIAL][Input.COMA] = new TransicionEstado(Estado.FINAL,generaTokenLiteral);
        maquinaEstados[Estado.INICIAL][Input.PUNTO_COMA] = new TransicionEstado(Estado.FINAL,generaTokenLiteral);

        /* Cadena multilinea. */
        maquinaEstados[Estado.INICIAL][Input.COMILLA] = new TransicionEstado(Estado.CADENA,inicStringVacio,concatenaChar);

        /* EOF. */
        AccionSemantica generaTokenEOF = new AccionSemantica.GeneraTokenParticular(this,aLexico.T_EOF);
        maquinaEstados[Estado.INICIAL][Input.EOF] = new TransicionEstado(Estado.FINAL,generaTokenEOF);
    }

    /**
     * Inicializacion estado 1.
     */
    private void inicDeteccionId(AccionSemantica concatenaChar, AccionSemantica truncaId,
                                 AccionSemantica generaTokenId, AccionSemantica devuelveUltimoLeido,
                                 AccionSemantica cuentaSaltoLinea) {
        /* Inputs no validos. */
        inicTransiciones(Estado.DETECCION_ID,Estado.FINAL,truncaId,generaTokenId,devuelveUltimoLeido);
        maquinaEstados[Estado.DETECCION_ID][Input.SALTO_LINEA] = new TransicionEstado(Estado.FINAL,truncaId,generaTokenId,
                cuentaSaltoLinea); //Permite contar un salto de linea (No devuelve el ultimo leido pq se descartaria de todas formas).

        /* Camino normal. */
        maquinaEstados[Estado.DETECCION_ID][Input.D_MINUSC] = new TransicionEstado(Estado.DETECCION_ID,concatenaChar);
        maquinaEstados[Estado.DETECCION_ID][Input.U_MINUSC] = new TransicionEstado(Estado.DETECCION_ID,concatenaChar);
        maquinaEstados[Estado.DETECCION_ID][Input.I_MINUSC] = new TransicionEstado(Estado.DETECCION_ID,concatenaChar);
        maquinaEstados[Estado.DETECCION_ID][Input.LETRA_MINUSC] = new TransicionEstado(Estado.DETECCION_ID,concatenaChar);
        maquinaEstados[Estado.DETECCION_ID][Input.DIGITO] = new TransicionEstado(Estado.DETECCION_ID,concatenaChar);
        maquinaEstados[Estado.DETECCION_ID][Input.GUION_B] = new TransicionEstado(Estado.DETECCION_ID,concatenaChar);

        /* EOF. Voy directo al estado final. No hace falta devolver ultimo leido. */
        maquinaEstados[Estado.DETECCION_ID][Input.EOF] = new TransicionEstado(Estado.FINAL,truncaId,generaTokenId);
    }

    /**
     * Inicializacion estado 2.
     */
    private void inicDeteccionPR(AccionSemantica concatenaChar, AccionSemantica devuelveUltimoLeido,
                                 AccionSemantica generaTokenPR, AccionSemantica cuentaSaltoLinea){
        /* Inputs no validos. */
        inicTransiciones(Estado.DETECCION_PR,Estado.FINAL,generaTokenPR,devuelveUltimoLeido);
        maquinaEstados[Estado.DETECCION_PR][Input.SALTO_LINEA] = new TransicionEstado(Estado.FINAL,generaTokenPR,
                cuentaSaltoLinea); //Permite contar un salto de linea (No devuelve el ultimo leido pq se descartaria de todas formas).

        /* Camino normal. */
        maquinaEstados[Estado.DETECCION_PR][Input.LETRA_MAYUS] = new TransicionEstado(Estado.DETECCION_PR,concatenaChar);
        maquinaEstados[Estado.DETECCION_PR][Input.GUION_B] = new TransicionEstado(Estado.DETECCION_PR,concatenaChar);

        /* EOF. Voy directo al estado final. No hace falta devolver ultimo leido. */
        maquinaEstados[Estado.DETECCION_PR][Input.EOF] = new TransicionEstado(Estado.FINAL,generaTokenPR);
    }

    /**
     * Inicializacion estado 3.
     */
    private void inicInicioComent(AccionSemantica cuentaSaltoLinea, AccionSemantica devuelveUltimoLeido){
        /*
         * Inputs no validos.
         * Si no transiciono al estado final, no detengo la ejecucion del lexico. Por eso me quedo en el estado inicial,
         * pero notifico el error.
         * TODO: Notificar error.
         */
        inicTransiciones(Estado.INICIO_COMENT,Estado.INICIAL,devuelveUltimoLeido);
        maquinaEstados[Estado.INICIO_COMENT][Input.SALTO_LINEA] = new TransicionEstado(Estado.INICIAL,
                cuentaSaltoLinea); //Permite contar un salto de linea. No devuele ultimo leido porque se descartaria.

        /* Camino normal. */
        maquinaEstados[Estado.INICIO_COMENT][Input.PORCENTAJE] = new TransicionEstado(Estado.CUERPO_COMENT);

        /* EOF. Voy directo al estado final. No hace falta devolver ultimo leido. TODO: Notificar error. */
        maquinaEstados[Estado.INICIO_COMENT][Input.EOF] = new TransicionEstado(Estado.FINAL);
    }

    /**
     * Inicializacion estado 4.
     */
    private void inicCuerpoComent(AccionSemantica cuentaSaltoLinea){
        /* Inputs validos. */
        inicTransiciones(Estado.CUERPO_COMENT,Estado.CUERPO_COMENT);

        /* Fin comentario (salto linea). Vuelvo al inicio para no detener la ejecucion del lexico. */
        maquinaEstados[Estado.CUERPO_COMENT][Input.SALTO_LINEA] = new TransicionEstado(Estado.INICIAL,cuentaSaltoLinea);

        /* EOF. No hace falta devolver ultimo leido. */
        maquinaEstados[Estado.CUERPO_COMENT][Input.EOF] = new TransicionEstado(Estado.FINAL);
    }

    /**
     * Inicializacion estados 5, 6, 7 y 8.
     */
    private void inicComparacion(AccionSemantica devuelveUltimoLeido, AccionSemantica consumeChar,
                                 AccionSemantica cuentaSaltoLinea, AccionSemantica generaTokenLiteral){
        AccionSemantica generaTokenParticular;

        /* Comparacion por menor estricto (5). */
        generaTokenParticular = new AccionSemantica.GeneraTokenParticular(this,'<');

        inicTransiciones(Estado.COMP_MENOR,Estado.FINAL,devuelveUltimoLeido,generaTokenParticular);
        maquinaEstados[Estado.COMP_MENOR][Input.SALTO_LINEA] = new TransicionEstado(Estado.INICIAL,generaTokenParticular,
                cuentaSaltoLinea); //Permite contar un salto de linea (No devuelve el ultimo leido pq se descartaria de todas formas).
        maquinaEstados[Estado.COMP_MENOR][Input.EOF] = new TransicionEstado(Estado.FINAL, generaTokenParticular); //No devuelve ultimo leido dsp de un EOF.

        /* Comparacion por menor igual (5). */
        generaTokenParticular = new AccionSemantica.GeneraTokenParticular(this,aLexico.T_COMP_MENOR_IGUAL);

        maquinaEstados[Estado.COMP_MENOR][Input.IGUAL] = new TransicionEstado(Estado.FINAL,consumeChar, generaTokenParticular);

        /* Comparacion por mayor estricto (6). */
        generaTokenParticular = new AccionSemantica.GeneraTokenParticular(this,'>');

        inicTransiciones(Estado.COMP_MAYOR,Estado.FINAL,devuelveUltimoLeido,generaTokenParticular);
        maquinaEstados[Estado.COMP_MAYOR][Input.SALTO_LINEA] = new TransicionEstado(Estado.INICIAL,generaTokenParticular,
            cuentaSaltoLinea); //Permite contar un salto de linea (No devuelve el ultimo leido pq se descartaria de todas formas).
        maquinaEstados[Estado.COMP_MAYOR][Input.EOF] = new TransicionEstado(Estado.FINAL, generaTokenParticular); //No devuelve ultimo leido dsp de un EOF.

        /* Comparacion por mayor igual (6). */
        generaTokenParticular = new AccionSemantica.GeneraTokenParticular(this,aLexico.T_COMP_MAYOR_IGUAL);

        maquinaEstados[Estado.COMP_MAYOR][Input.IGUAL] = new TransicionEstado(Estado.FINAL,consumeChar, generaTokenParticular);

        /* '!' solo (7). El simbolo por si solo no es valido. TODO: Notificar error. */
        inicTransiciones(Estado.COMP_DISTINTO,Estado.INICIAL,devuelveUltimoLeido);
        maquinaEstados[Estado.COMP_DISTINTO][Input.SALTO_LINEA] = new TransicionEstado(Estado.INICIAL,
                cuentaSaltoLinea); //Permite contar un salto de linea (No devuelve el ultimo leido pq se descartaria de todas formas).
        maquinaEstados[Estado.COMP_DISTINTO][Input.EOF] = new TransicionEstado(Estado.INICIAL); //No devuelve ultimo leido dsp de un EOF.

        /* Comparacion por distincion (7). */
        generaTokenParticular = new AccionSemantica.GeneraTokenParticular(this,aLexico.T_COM_DISTINTO);

        maquinaEstados[Estado.COMP_DISTINTO][Input.IGUAL] = new TransicionEstado(Estado.FINAL,consumeChar, generaTokenParticular);

        /* Asignacion (8). */
        generaTokenParticular = new AccionSemantica.GeneraTokenParticular(this,'=');

        inicTransiciones(Estado.SIGNO_IGUAL,Estado.FINAL,devuelveUltimoLeido, generaTokenParticular);
        maquinaEstados[Estado.SIGNO_IGUAL][Input.SALTO_LINEA] = new TransicionEstado(Estado.FINAL, generaTokenParticular,
                cuentaSaltoLinea); //Permite contar un salto de linea (No devuelve el ultimo leido pq se descartaria de todas formas).
        maquinaEstados[Estado.SIGNO_IGUAL][Input.EOF] = new TransicionEstado(Estado.FINAL, generaTokenParticular); //No devuelve ultimo leido dsp de un EOF.

        /* Comparacion por igualdad (8). */
        generaTokenParticular = new AccionSemantica.GeneraTokenParticular(this,aLexico.T_COMP_IGUAL);
        maquinaEstados[Estado.SIGNO_IGUAL][Input.IGUAL] = new TransicionEstado(Estado.FINAL,consumeChar,generaTokenParticular);

    }

    /**
     * Inicializacion estados 9, 10, 11, 12, 13, 14.
     */
    private void inicDeteccionCtes(AccionSemantica concatenaChar, AccionSemantica devuelveUltimoLeido,
                                   AccionSemantica cuentaSaltoLinea, AccionSemantica generaTokenUINT,
                                   AccionSemantica generaTokenDouble, AccionSemantica consumeChar){
        /* Acciones semanticas usadas */
        AccionSemantica parseBaseDouble = new AccionSemantica.ParseBaseDouble();

        /* Parte entera (9). El lexico "da por hecho" que es un UINT, asi se evita dar problemas al sintactico. */
        inicTransiciones(Estado.CTE_PARTE_ENTERA, Estado.FINAL,generaTokenUINT, devuelveUltimoLeido, generaTokenUINT); //TODO: Mostrar warning porque falto "_ui".
        maquinaEstados[Estado.CTE_PARTE_ENTERA][Input.SALTO_LINEA] = new TransicionEstado(Estado.FINAL, generaTokenUINT,
                cuentaSaltoLinea); //Permite contar un salto de linea (No devuelve el ultimo leido pq se descartaria de todas formas).
        maquinaEstados[Estado.CTE_PARTE_ENTERA][Input.EOF] = new TransicionEstado(Estado.FINAL, generaTokenUINT); //No devuelve ultimo leido dsp de un EOF.

        maquinaEstados[Estado.CTE_PARTE_ENTERA][Input.DIGITO] = new TransicionEstado(Estado.CTE_PARTE_ENTERA,concatenaChar);
        maquinaEstados[Estado.CTE_PARTE_ENTERA][Input.GUION_B] = new TransicionEstado(Estado.CTE_UI_SUF1); //Salto a deteccion de sufijo para UIs.
        maquinaEstados[Estado.CTE_PARTE_ENTERA][Input.PUNTO] = new TransicionEstado(Estado.CTE_PARTE_DECIM,concatenaChar); //Salto a parte decimal de doubles.
        maquinaEstados[Estado.CTE_PARTE_ENTERA][Input.D_MINUSC] = new TransicionEstado(Estado.CTE_PARTE_EXP,parseBaseDouble,consumeChar); //Salto a parte exponencial de doubles.

        /* Deteccion sufijo (10,11,12). */
        inicDeteccionSufijo(devuelveUltimoLeido, cuentaSaltoLinea, generaTokenUINT);

        /* Parte decimal (13). */
        inicTransiciones(Estado.CTE_PARTE_DECIM, Estado.FINAL, devuelveUltimoLeido, parseBaseDouble, generaTokenDouble);
        maquinaEstados[Estado.CTE_PARTE_DECIM][Input.SALTO_LINEA] = new TransicionEstado(Estado.FINAL, parseBaseDouble, generaTokenDouble,
                cuentaSaltoLinea); //Permite contar un salto de linea (No devuelve el ultimo leido pq se descartaria de todas formas).
        maquinaEstados[Estado.CTE_PARTE_DECIM][Input.EOF] = new TransicionEstado(Estado.FINAL, parseBaseDouble, generaTokenDouble); //No devuelve ultimo leido dsp de un EOF.

        maquinaEstados[Estado.CTE_PARTE_DECIM][Input.DIGITO] = new TransicionEstado(Estado.CTE_PARTE_DECIM, concatenaChar);
        maquinaEstados[Estado.CTE_PARTE_DECIM][Input.D_MINUSC] = new TransicionEstado(Estado.CTE_PARTE_EXP,parseBaseDouble,consumeChar); //Salto a parte exponencial de doubles.

        /* Parte exponencial (14). */
        inicTransiciones(Estado.CTE_PARTE_EXP, Estado.FINAL, devuelveUltimoLeido, parseBaseDouble, generaTokenDouble);
        maquinaEstados[Estado.CTE_PARTE_EXP][Input.SALTO_LINEA] = new TransicionEstado(Estado.FINAL, parseBaseDouble, generaTokenDouble,
                cuentaSaltoLinea); //Permite contar un salto de linea (No devuelve el ultimo leido pq se descartaria de todas formas).
        maquinaEstados[Estado.CTE_PARTE_EXP][Input.EOF] = new TransicionEstado(Estado.FINAL, parseBaseDouble, generaTokenDouble); //No devuelve ultimo leido dsp de un EOF.

        maquinaEstados[Estado.CTE_PARTE_EXP][Input.DIGITO] = new TransicionEstado(Estado.CTE_PARTE_EXP,concatenaChar);
    }

    private void inicDeteccionSufijo(AccionSemantica devuelveUltimoLeido, AccionSemantica cuentaSaltoLinea,
                                     AccionSemantica generaTokenUINT) {
        /* Sufijo1 (10). */
        inicTransiciones(Estado.CTE_UI_SUF1, Estado.FINAL, devuelveUltimoLeido, generaTokenUINT); //TODO: Mostrar warning porque falto "ui".
        maquinaEstados[Estado.CTE_UI_SUF1][Input.SALTO_LINEA] = new TransicionEstado(Estado.FINAL, generaTokenUINT,
                cuentaSaltoLinea); //Permite contar un salto de linea (No devuelve el ultimo leido pq se descartaria de todas formas).
        maquinaEstados[Estado.CTE_UI_SUF1][Input.EOF] = new TransicionEstado(Estado.FINAL, generaTokenUINT); //No devuelve ultimo leido dsp de un EOF.

        maquinaEstados[Estado.CTE_UI_SUF1][Input.U_MINUSC] = new TransicionEstado(Estado.CTE_UI_SUF2);

        /* Sufijo2 (11). */
        inicTransiciones(Estado.CTE_UI_SUF2, Estado.FINAL, devuelveUltimoLeido, generaTokenUINT); //TODO: Mostrar warning porque falto "i".
        maquinaEstados[Estado.CTE_UI_SUF2][Input.SALTO_LINEA] = new TransicionEstado(Estado.FINAL, generaTokenUINT,
                cuentaSaltoLinea); //Permite contar un salto de linea (No devuelve el ultimo leido pq se descartaria de todas formas).
        maquinaEstados[Estado.CTE_UI_SUF2][Input.EOF] = new TransicionEstado(Estado.FINAL, generaTokenUINT); //No devuelve ultimo leido dsp de un EOF.

        maquinaEstados[Estado.CTE_UI_SUF2][Input.I_MINUSC] = new TransicionEstado(Estado.CTE_UI_SUF3);

        /* Sufijo3 (12). */
        inicTransiciones(Estado.CTE_UI_SUF3, Estado.FINAL, devuelveUltimoLeido, generaTokenUINT);
        maquinaEstados[Estado.CTE_UI_SUF3][Input.SALTO_LINEA] = new TransicionEstado(Estado.FINAL, generaTokenUINT,
                cuentaSaltoLinea); //Permite contar un salto de linea (No devuelve el ultimo leido pq se descartaria de todas formas).
        maquinaEstados[Estado.CTE_UI_SUF3][Input.EOF] = new TransicionEstado(Estado.FINAL, generaTokenUINT); //No devuelve ultimo leido dsp de un EOF.
    }

    /**
     * Inicializacion estado 15.
     */
    private void inicCadena(AccionSemantica concatenaChar, AccionSemantica cuentaSaltoLinea,
                            AccionSemantica generaTokenCadena){
        /* Inputs validos. */
        inicTransiciones(Estado.CADENA,Estado.CADENA,concatenaChar);
        maquinaEstados[Estado.CADENA][Input.SALTO_LINEA] = new TransicionEstado(Estado.CADENA,concatenaChar,
                cuentaSaltoLinea); //Permite contar saltos de linea.

        /* Fin cadena. */
        maquinaEstados[Estado.CADENA][Input.COMILLA] = new TransicionEstado(Estado.FINAL,concatenaChar,generaTokenCadena);

        /* EOF. Quedo la cadena abierta, hay que notificar error. TODO: Notificar error. */
        maquinaEstados[Estado.CADENA][Input.EOF] = new TransicionEstado(Estado.INICIAL);
    }
}
