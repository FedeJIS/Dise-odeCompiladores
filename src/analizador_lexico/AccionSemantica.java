package analizador_lexico;

import analizador_lexico.maquina_estados.MaquinaEstados;
import util.CodigoFuente;
import util.FileProcessor;
import util.Reservado;
import util.tabla_simbolos.Celda;
import util.tabla_simbolos.TablaDeSimbolos;

public class AccionSemantica {
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
        private final static int LIMITE_STRING = 20;

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

        public GeneraTokenPR(MaquinaEstados maquinaEstados, Reservado tablaPR) {
            this.maquinaEstados = maquinaEstados;
            this.tablaPR = tablaPR;
        }

        /**
         * Chequea que la palabra almacenada sea una palabra reservada valida y agrega el token a la lista de tokens.
         */
        @Override
        public void ejecutar(){
            if (tablaPR.esReservada(sTemporal)) {
                maquinaEstados.setVariablesSintactico(tablaPR.getToken(sTemporal),""); //No tiene lexema.
            }
            else{
                maquinaEstados.reiniciar(); //Evita que la maquina quede en el estado final, para que el lexico no genere un token.
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

    /**
     * Consumir caracter. Esta AS esta solo para que quede claro que se consume un caracter.
     * No tiene ninguna otra utilidad.
     */
    public static class ConsumeChar extends AccionSemantica{
    }

    public static class GeneraTokenUINT extends AccionSemantica {
        private final static int LIMITE_INT=(int)(Math.pow(2,16)-1);

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
            int numero = Integer.parseInt(sTemporal); //No genera NumberFormatEx porque solo se cargan digitos al string.
            if (numeroEnRango(numero)) {
                tablaS.agregar(new Celda(token,sTemporal,"UINT"));
                maquinaEstados.setVariablesSintactico(token,sTemporal);
            }
            else{
                //TODO Notificar error.
                maquinaEstados.reiniciar(); //Evita que la maquina quede en el estado final, para que el lexico no genere un token.
            }
        }

        public boolean numeroEnRango(int numero) {
            return numero >= 0 && numero <= LIMITE_INT;
        }
    }

    public static class ParseBaseDouble extends AccionSemantica {
        /**
         * Verifica si la parte numerica es un numero double y lo asigna a numeroIntD
         * Si es invalido, numeroIntD se vuelve Double.NEGATIVEINFINITY
         */
        public void ejecutar(){
            baseNumDouble = Double.parseDouble(sTemporal); //No genera NumberFormatEx porque solo se cargan digitos al string.
            sTemporal = ""; //Reinicia el string temporal.
        }
    }

    public static class GeneraTokenDouble extends AccionSemantica{
        private final static double LIM_INF_DOUBLE_NEG = -1.7976931348623157;
        private final static double LIM_SUP_DOUBLE_NEG = -2.2250738585072014;
        private final static double LIM_INF_DOUBLE_POS = 2.2250738585072014;
        private final static double LIM_SUP_DOUBLE_POS = 1.7976931348623157;
        private final static int MAX_DOUBLE_EXP = 308;

        private final MaquinaEstados maquinaEstados;

        private final TablaDeSimbolos tablaS;

        private final int token;

        public GeneraTokenDouble(MaquinaEstados maquinaEstados, TablaDeSimbolos tablaS, int token) {
            this.maquinaEstados = maquinaEstados;
            this.tablaS = tablaS;
            this.token = token;
        }

        /**
         * Dada la parte numerica de un double (entera y decimal), se verifica si el exponente es correcto.
         * Luego revisa si el double baseNumDouble elevado al exponente se encuentra en el rango dado.
         */
        public void ejecutar() {
            if (baseNumDouble != Double.NEGATIVE_INFINITY) {
                int expNumDouble = 0; //Vale 0 por defecto (Util para los casos donde no se tiene exponente).
                if (!sTemporal.isEmpty() && !sTemporal.equals("-") && !sTemporal.equals("+")) expNumDouble = Integer.parseInt(sTemporal);

                if (doubleValido(baseNumDouble,expNumDouble)) {
                    double doubleNormalizado = baseNumDouble * Math.pow(10, expNumDouble);
                    tablaS.agregar(new Celda(token, String.valueOf(doubleNormalizado), "DOUBLE"));
                    maquinaEstados.setVariablesSintactico(token, String.valueOf(doubleNormalizado));
                }
            }
        }

        private boolean doubleValido(double baseNumDouble, double expNumDouble){
            boolean doubleValido = true;
            if (expFueraRango(expNumDouble)) {
                maquinaEstados.reiniciar(); //Evita que la maquina quede en el estado final, para que el lexico no genere un token.
                doubleValido = false;
                //TODO Notificar error.
            }

            if (doubleFueraRango(baseNumDouble,expNumDouble)) {
                maquinaEstados.reiniciar(); //Evita que la maquina quede en el estado final, para que el lexico no genere un token.
                doubleValido = false;
                //TODO Notificar error.
            }

            return doubleValido;
        }

        public boolean expFueraRango(double expNumDouble) {
            return expNumDouble < -MAX_DOUBLE_EXP || expNumDouble > MAX_DOUBLE_EXP;
        }

        public boolean doubleFueraRango(double baseNumDouble, double expNumDouble) {
            double min = LIM_INF_DOUBLE_POS * Math.pow(10,-MAX_DOUBLE_EXP);
            double max = LIM_SUP_DOUBLE_POS * Math.pow(10, MAX_DOUBLE_EXP);

            double doubleNormalizado = baseNumDouble * Math.pow(10,expNumDouble);

            return doubleNormalizado != 0.0 && (doubleNormalizado < min || doubleNormalizado > max);
        }
    }

    public static class CuentaSaltoLinea extends AccionSemantica{
        /**
         * Incrementa en uno la cantidad de lineas de un archivo.
         */
        private int cantLineas = 1;

        public void ejecutar(){
                cantLineas++;
        }

        public int getCantLineas() {
            return cantLineas;
        }
    }

}
