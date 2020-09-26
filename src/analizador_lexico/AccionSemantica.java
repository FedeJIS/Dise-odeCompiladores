package analizador_lexico;

import analizador_lexico.maquina_estados.MaquinaEstados;
import util.CodigoFuente;
import util.FileProcessor;
import util.Reservado;
import util.tabla_simbolos.Celda;
import util.tabla_simbolos.TablaDeSimbolos;

public class AccionSemantica {
    private final static int LIMITE_STRING = 20;
    private final static int LIMITE_INT=(int)(Math.pow(2,16)-1);
    private final static double LIM_INF_DOUBLE_NEG = -1.7976931348623157;
    private final static double LIM_SUP_DOUBLE_NEG = -2.2250738585072014;
    private final static double LIM_INF_DOUBLE_POS = 2.2250738585072014;
    private final static double LIM_SUP_DOUBLE_POS = 1.7976931348623157;
    private final static int  MIN_DOUBLE_EXP = -308, MAX_DOUBLE_EXP = 308;
    private static String sTemporal;
    private static double baseNumDouble = Double.NEGATIVE_INFINITY; // Utilizado para AS-double (parte numerica y parte exp)

    public AccionSemantica(){}

    /**
     * Metodo hook que se sobre escribe segun la accion particular.
     */
    public void ejecutar(){}

    /* ---Implementaciones--- */

    public static class InicStringVacio extends AccionSemantica{
        /**
         * Inicializa un string en vacio.
         */
        @Override
        public void ejecutar (){
            sTemporal = "";
        }

        /**
         * Usado solo para testeo.
         *
         * @return true si el string temporal esta vacio, false en cualquier otro caso.
         */
        public boolean isStringVacio(){
            return sTemporal.equals("");
        }
    }

    public static class ConcatenaChar extends AccionSemantica{
        private final CodigoFuente codigoFuente;

        public ConcatenaChar(CodigoFuente codigoFuente) {
            this.codigoFuente = codigoFuente;
        }

        /**
         * Concatena un caracter al final de un string.
         */
        @Override
        public void ejecutar(){
            sTemporal = sTemporal + codigoFuente.simboloActual();
        }

        /**
         * Usado solo para testeos.
         *
         * @return el ultimo char concatenado al string temporal.
         */
        public char getUltimoChar(){
            return sTemporal.charAt(sTemporal.length()-1);
        }
    }

    public static class TruncaId extends AccionSemantica{
        private final FileProcessor fileProcessor;

        public TruncaId(FileProcessor fileProcessor) {
            this.fileProcessor = fileProcessor;
        }

        /**
         * Si se excede el limite de un string, se trunca y genera un WARNING.
         */
        @Override
        public void ejecutar (){
            if (LIMITE_STRING < sTemporal.length()){
                sTemporal=sTemporal.substring(0,LIMITE_STRING);
                fileProcessor.escribirArchivo("./warning.txt","WARNING: String truncado a: "+sTemporal,fileProcessor.existeArchivo("./warning.txt"));
            }
        }

        /**
         * Usado solo para testeos.
         *
         * @return el string temporal almacenado.
         */
        public String getSTemporal(){
            return sTemporal;
        }
    }

    public static class DevuelveUltimoLeido extends AccionSemantica{
        private final CodigoFuente codigoFuente;

        public DevuelveUltimoLeido(CodigoFuente codigoFuente) {
            this.codigoFuente = codigoFuente;
        }

        /**
         * Retrocede una posicion en el codigo fuente, para que se vuelva a leer el ultimo caracter leido.
         */
        @Override
        public void ejecutar (){
            codigoFuente.retroceder();
        }
    }

    public static class GeneraTokenTS extends AccionSemantica{
        private final MaquinaEstados maquinaEstados;

        private final TablaDeSimbolos tablaS;

        private final int token;

        public GeneraTokenTS(MaquinaEstados maquinaEstados, TablaDeSimbolos tablaS, int token) {
            this.maquinaEstados = maquinaEstados;
            this.tablaS = tablaS;
            this.token = token;
        }

        /**
         * Busca el lexema acumulado hasta el momento en la TS. Si lo encuentra, devuelve la celda asociada. En caso de
         * que no lo encuentre, crea una nueva celda, la agrega y la retorna.
         * Luego de acceder a la TS, se agrega el token generado a la maquina de estados, para que luego pueda ser
         * accedido por el analizador sintactico.
         *
         * Ya no es necesario sacar el ultimo caracter leido, de eso se encarga la maquina de estados. (Bruno)
         */
        @Override
        public void ejecutar() {
            tablaS.agregar(new Celda(token,sTemporal,""));
            maquinaEstados.setVariablesSintactico(token,sTemporal);
        }
    }

    public static class GeneraTokenPR extends AccionSemantica{
        private final MaquinaEstados maquinaEstados;

        private final Reservado tablaPR;

        private final int token;

        public GeneraTokenPR(MaquinaEstados maquinaEstados, Reservado tablaPR, int token) {
            this.maquinaEstados = maquinaEstados;
            this.tablaPR = tablaPR;
            this.token = token;
        }

        /**
         * Chequea que la palabra almacenada sea una palabra reservada valida y agrega el token a la lista de tokens.
         */
        @Override
        public void ejecutar(){
            if (tablaPR.esReservada(sTemporal)) {
                maquinaEstados.setVariablesSintactico(token,""); //No tiene lexema.
            }
            else{
                //TODO Notificar error.
            }
        }
    }

    public static class GeneraTokenLiteral extends AccionSemantica{
        private final MaquinaEstados maquinaEstados;

        private final CodigoFuente cFuente;

        public GeneraTokenLiteral(MaquinaEstados maquinaEstados, CodigoFuente cFuente) {
            this.maquinaEstados = maquinaEstados;
            this.cFuente = cFuente;
        }

        @Override
        public void ejecutar() {
            int token = cFuente.simboloActual(); //Conversion implicita de char a ASCII.
            maquinaEstados.setVariablesSintactico(token,""); //No tiene lexema.
        }
    }

    public static class GeneraTokenParticular extends AccionSemantica{
        private final MaquinaEstados maquinaEstados;

        private final int token;

        public GeneraTokenParticular(MaquinaEstados maquinaEstados, int token) {
            this.maquinaEstados = maquinaEstados;
            this.token = token;
        }

        @Override
        public void ejecutar() {
            maquinaEstados.setVariablesSintactico(token,""); //No tiene lexema.
        }
    }

    public static class ConsumeChar extends AccionSemantica{
        private final CodigoFuente codigoFuente; //TODO Remove?

        public ConsumeChar(CodigoFuente codigoFuente) {
            this.codigoFuente = codigoFuente;
        }

        /**
         * Consumir caracter.
         */
        public void ejecutar(){
        }
    }

    public static class GeneraTokenUINT extends AccionSemantica {
        private final MaquinaEstados maquinaEstados;

        private final TablaDeSimbolos tablaS;

        private final int token;

        public GeneraTokenUINT(MaquinaEstados maquinaEstados, TablaDeSimbolos tablaS, int token) {
            this.maquinaEstados = maquinaEstados;
            this.tablaS = tablaS;
            this.token = token;
        }

        /**
         * Verifica los limites de un numero entero.
         */
        @Override
        public void ejecutar(){
            try {
                int numero = Integer.parseInt(sTemporal);
                if (numero >= 0 && numero <= LIMITE_INT) { //La cte esta en el rango valido.
                    tablaS.agregar(new Celda(token,sTemporal,"UINT"));
                    maquinaEstados.setVariablesSintactico(token,sTemporal);
                }
                else{
                    //TODO Notificar error.
                    maquinaEstados.reiniciar(); //Evita que la maquina quede en el estado final, para que el lexico no genere un token. //TODO Verificar.
                }
            }
            catch (NumberFormatException numberFormatException){
                System.out.println(); //No tendria que llegar nunca a este punto, es imposible que el string tenga algo que no sea un entero.
            }
        }
    }

    public static class ParseBaseDouble extends AccionSemantica {
        /**
         * Verifica si la parte numerica es un numero double y lo asigna a numeroIntD
         * Si es invalido, numeroIntD se vuelve Double.NEGATIVEINFINITY
         */
        public void ejecutar(){
            try{
                baseNumDouble = Double.parseDouble(sTemporal);
                sTemporal = ""; //Reinicia el string temporal.
            }
            catch(NumberFormatException numberFormatException){ //No tendria que llegar nunca a este punto, es imposible que el string tenga algo que no sea un double.
                numberFormatException.printStackTrace();
                baseNumDouble = Double.NEGATIVE_INFINITY;
            }
        }
    }

    public static class GeneraTokenDouble extends AccionSemantica{
        private final MaquinaEstados maquinaEstados;

        private final TablaDeSimbolos tablaS;

        private final int token;

        public GeneraTokenDouble(MaquinaEstados maquinaEstados, TablaDeSimbolos tablaS, int token) {
            this.maquinaEstados = maquinaEstados;
            this.tablaS = tablaS;
            this.token = token;
        }

        /**
         * Dada la parte numerica de un double entero,decimal (numeroIntD), se verifica si el exponente es correcto
         * Luego revisa si el double numeroIntD elevado a exponente Math.Pow(numeroIntD,exponente) es válido en el rango.
         */
        public void ejecutar(){
            if (baseNumDouble != Double.NEGATIVE_INFINITY)
                try {
                    if (!isBaseFueraRango(baseNumDouble)){
                        double expNumDouble = 0; //Vale 0 por defecto.
                        if (!sTemporal.isEmpty()) expNumDouble = Double.parseDouble(sTemporal);

                        if (expNumDouble >= -MAX_DOUBLE_EXP && expNumDouble <= MAX_DOUBLE_EXP){ //Exponente con rango valido.
                            double doubleNormalizado = Math.pow(baseNumDouble,expNumDouble);
                            tablaS.agregar(new Celda(token,String.valueOf(doubleNormalizado),"DOUBLE"));
                            maquinaEstados.setVariablesSintactico(token,String.valueOf(doubleNormalizado));
                        }
                        else {
                            maquinaEstados.reiniciar(); //Evita que la maquina quede en el estado final, para que el lexico no genere un token. //TODO Verificar.
                            //TODO Notificar error.
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }

        private boolean isBaseFueraRango(double baseNumDouble) {
            boolean baseFueraRango = false;
            if (baseNumDouble <= LIM_INF_DOUBLE_POS || baseNumDouble >= LIM_SUP_DOUBLE_POS){
                maquinaEstados.reiniciar(); //Evita que la maquina quede en el estado final, para que el lexico no genere un token. //TODO Verificar.
                baseFueraRango = true;
                //TODO Notificar error.
            }

            if (baseNumDouble <= LIM_INF_DOUBLE_NEG || baseNumDouble >= LIM_SUP_DOUBLE_NEG){
                maquinaEstados.reiniciar(); //Evita que la maquina quede en el estado final, para que el lexico no genere un token. //TODO Verificar.
                baseFueraRango = true;
                //TODO Notificar error.
            }
            return baseFueraRango;
        }
    }

    public static class CuentaSaltoLinea extends AccionSemantica{
        /**
         * Incrementa en uno la cantidad de lineas de un archivo.
         */
        private int cant_lineas = 0;

        public void ejecutar(){
                cant_lineas++;
        }
    }

    public void set_sTemporal (String s){
        sTemporal=s;
    }

    public String getString(){
        return sTemporal;
    }
}
