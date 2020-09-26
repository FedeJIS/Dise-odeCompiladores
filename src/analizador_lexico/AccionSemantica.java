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
    private final static double LIMIT_DNEG_INF=-1.7976931348623157;
    private final static double LIMIT_DNEG_SUP=-2.2250738585072014;
    private final static double LIMIT_DPOS_INF=2.2250738585072014;
    private final static double LIMIT_DPOS_SUP=.7976931348623157;
    private final static int LIMIT_DEXP=308;
    private static String sTemporal;
    private double numeroIntD = Double.NEGATIVE_INFINITY; // Utilizado para AS-double (parte numerica y parte exp)

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
         * Retrocede una vez en el codigo fuente, para que se vuelva a leer el ultimo caracter que se leyo.
         */
        @Override
        public void ejecutar (){
            codigoFuente.retroceder();
        }
    }

    public static class GeneraTokenTS extends AccionSemantica{
        private final MaquinaEstados maquinaEstados;

        private final TablaDeSimbolos tablaDeSimbolos;

        private final int token;

        public GeneraTokenTS(MaquinaEstados maquinaEstados, TablaDeSimbolos tablaDeSimbolos, int token) {
            this.maquinaEstados = maquinaEstados;
            this.tablaDeSimbolos = tablaDeSimbolos;
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
            Celda celda = tablaDeSimbolos.agregar(new Celda(token,sTemporal,""));
            maquinaEstados.agregarToken(celda); //Agrega el token a una lista para que sea accedida por el sintactico mas adelante.
            //TODO: Ver como pasarle el lexema al sintactico.

            maquinaEstados.reiniciar();
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
            if (tablaPR.esReservada(sTemporal)) maquinaEstados.agregarToken(new Celda(token,"",""));
            else System.out.println("Notificar error"); //TODO: Hacer bien esto.

            maquinaEstados.reiniciar();
        }
    }

    public static class GeneraTokenLiteral extends AccionSemantica{
        private final MaquinaEstados maquinaEstados;

        private final int token;

        public GeneraTokenLiteral(MaquinaEstados maquinaEstados, CodigoFuente codigoFuente) {
            this.maquinaEstados = maquinaEstados;
            this.token = codigoFuente.simboloActual(); //Conversion implicita de char a ASCII.
        }

        @Override
        public void ejecutar() {
            maquinaEstados.agregarToken(new Celda(token,"",""));

            maquinaEstados.reiniciar();
        }
    }

    public static class ConsumeChar extends AccionSemantica{
        private final CodigoFuente codigoFuente;

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

        private final int token;

        public GeneraTokenUINT(MaquinaEstados maquinaEstados, int token) {
            this.maquinaEstados = maquinaEstados;
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
                    maquinaEstados.agregarToken(new Celda(token,"","UINT")); //TODO: Ver como pasarle el lexema al sintactico.
                    //TODO: Se agrega a la TS?

                    maquinaEstados.reiniciar();
                }
            }
            catch (NumberFormatException numberFormatException){
                System.out.println(); //No tendria que llegar nunca a este punto, es imposible que el string tenga algo que no sea un entero.
            }
        }
    }

    /**
     * Dada la parte numerica de un double entero,decimal (numeroIntD), se verifica si el exponente es correcto
     * Luego revisa si el double numeroIntD elevado a exponente Math.Pow(numeroIntD,exponente) es válido en el rango.
     */
    public class GeneraTokenDouble{
        public boolean ejecutar(){
            // Si la parte numerica es un double (obtenida de AS11_partnum)
            if (numeroIntD != Double.NEGATIVE_INFINITY) {
                try {
                    // Si el exponente se encuentra dentro del rango
                    double exponente = Double.parseDouble(sTemporal);
                    if ((exponente >= 0) && (exponente <= LIMIT_DEXP)) {

                        //Si el numero es un double valido dentro del rango
                        double numDouble=Math.pow(numeroIntD,exponente);
                        System.out.println(numDouble);
                        if (((numDouble >= Math.pow(LIMIT_DNEG_INF, LIMIT_DEXP)) && (numDouble <= Math.pow(LIMIT_DNEG_SUP, -LIMIT_DEXP))) ||
                                ((numDouble >= Math.pow(LIMIT_DPOS_INF, -LIMIT_DEXP)) && (numDouble <= Math.pow(LIMIT_DPOS_SUP, LIMIT_DEXP))))
                            return true;
                        else
                            return false;
                    } else
                        return false;

                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
            return false;
        }
    }

    public class GeneraTokenDoubleNum extends AccionSemantica {
        /**
         * Verifica si la parte numerica es un numero double y lo asigna a numeroIntD
         * Si es invalido, numeroIntD se vuelve Double.NEGATIVEINFINITY
         */
        public void ejecutar(){
            try{
                numeroIntD = Double.parseDouble(sTemporal);


            }
            catch(NumberFormatException numberFormatException){ //No tendria que llegar nunca a este punto, es imposible que el string tenga algo que no sea un double.
                numberFormatException.printStackTrace();
                numeroIntD=Double.NEGATIVE_INFINITY;
            }
        }
        public double getNumero(){
            return numeroIntD;
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
