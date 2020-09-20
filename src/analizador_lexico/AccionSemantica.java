package analizador_lexico;

public class AccionSemantica {
    private final static int LIMITE_STRING=20;
    private final static int LIMITE_INT=(int)(Math.pow(2,16)-1);
    private final static double LIMIT_DNEG_INF=-1.7976931348623157;
    private final static double LIMIT_DNEG_SUP=-2.2250738585072014;
    private final static double LIMIT_DPOS_INF=2.2250738585072014;
    private final static double LIMIT_DPOS_SUP=.7976931348623157;
    private final static int LIMIT_DEXP=308;
//    private static FileProcessor fileProcessor;
    private TablaDeSimbolos tablaDeSimbolos;
    private Reservado reservado;
//    private CodigoFuente codigoFuente;
    private static String sTemporal;
    private double numeroIntD = 0; // Utilizado para AS-double (parte numerica y parte exp)

    public AccionSemantica (Reservado reservado){
//        fileProcessor= new FileProcessor();
        tablaDeSimbolos= new TablaDeSimbolos();
        this.reservado=reservado;
//        this.codigoFuente = new CodigoFuente(fileProcessor.getLineas(pathFuente));
        this.numeroIntD=Double.NEGATIVE_INFINITY;
    }

    /**
     * Metodo hook que se sobre escribe segun la accion particular.
     */
    public void ejecutar(){}

    /* ---Implementaciones--- */

    public static class InicStringVacio extends AccionSemantica{
        public InicStringVacio(Reservado reservado) {
            super(reservado);
        }

        /**
         * Inicializa un string en vacio.
         */
        @Override
        public void ejecutar (){
            sTemporal="";
        }
    }

    public static class ConcatenaChar extends AccionSemantica{
        private final CodigoFuente codigoFuente;

        public ConcatenaChar(Reservado reservado, CodigoFuente codigoFuente) {
            super(reservado);
            this.codigoFuente = codigoFuente;
        }

        /**
         * Concatena un caracter al final de un string.
         */
        @Override
        public void ejecutar(){
            sTemporal= sTemporal + codigoFuente.simboloActual();
        }
    }

    public static class CheckLongString extends AccionSemantica{
        private final FileProcessor fileProcessor;
        public CheckLongString(Reservado reservado, FileProcessor fileProcessor) {
            super(reservado);
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
    }

    public static class DevuelveUltimoLeido extends AccionSemantica{
        private final CodigoFuente codigoFuente;

        public DevuelveUltimoLeido(Reservado reservado, CodigoFuente codigoFuente) {
            super(reservado);
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

    /**
     * Devuelve un objeto con la celda de la tabla de simbolos segun el lexema
     */
    public static class GeneraTokenId extends AccionSemantica{
        private final TablaDeSimbolos tablaDeSimbolos;

        public GeneraTokenId(Reservado reservado, TablaDeSimbolos tablaDeSimbolos) {
            super(reservado);
            this.tablaDeSimbolos = tablaDeSimbolos;
        }

        public Celda ejecutar(int token) {
            String lexema = sTemporal.substring(0, sTemporal.length() - 1); // quita el ultimo caracter
            return tablaDeSimbolos.agregar(new Celda(token,lexema,""));
        }
    }

    public class AS4{
        /**
         * Es una palabra reservada valida
         * @return
         */
        public boolean ejecutar(){
            sTemporal=sTemporal.substring(0, sTemporal.length() - 1);
            return reservado.esReservada(sTemporal);
        }
    }

    public static class ConsumeChar extends AccionSemantica{
        private final CodigoFuente codigoFuente;

        public ConsumeChar(Reservado reservado, CodigoFuente codigoFuente) {
            super(reservado);
            this.codigoFuente = codigoFuente;
        }

        /**
         * Consumir caracter.
         */
        public void ejecutar(){
            System.out.println("caracter consumido" + codigoFuente.simboloActual());
        }
    }


    public class AS10{
        /**
         * Verifica los limites de un numero entero
         * @return
         */
        public boolean ejecutar(){
            int numero;
            try {
                numero = Integer.parseInt(sTemporal);
            }
            catch (Exception e){
                return false;
            }
            if ((numero>=0)&&(numero <= LIMITE_INT))
                return true;
            else
                    return false;
        }
    }

    /**
     * Dada la parte numerica de un double entero,decimal (numeroIntD), se verifica si el exponente es correcto
     * Luego revisa si el double numeroIntD elevado a exponente Math.Pow(numeroIntD,exponente) es válido en el rango.
     */
    public class AS11_partExp{
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

    public class AS11_partenum{
        /**
         * Verifica si la parte numerica es un numero double y lo asigna a numeroIntD
         * Si es invalido, numeroIntD se vuelve Double.NEGATIVEINFINITY
         */
        public void ejecutar(){
            try{
                numeroIntD=Double.parseDouble(sTemporal);
            }
            catch(Exception e){
                e.printStackTrace();
                numeroIntD=Double.NEGATIVE_INFINITY;
            }
        }
        public double getNumero(){
            return numeroIntD;
        }
    }


    public static class CuentaSaltoLinea extends AccionSemantica{
        public CuentaSaltoLinea(Reservado reservado) {
            super(reservado);
        }

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
