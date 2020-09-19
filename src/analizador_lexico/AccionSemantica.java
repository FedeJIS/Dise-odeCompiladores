package analizador_lexico;

public class AccionSemantica {
    private final static int LIMITE_STRING=20;
    private final static int LIMITE_INT=(int)(Math.pow(2,16)-1);
    private final static double LIMIT_DNEG_INF=-1.7976931348623157;
    private final static double LIMIT_DNEG_SUP=-2.2250738585072014;
    private final static double LIMIT_DPOS_INF=2.2250738585072014;
    private final static double LIMIT_DPOS_SUP=.7976931348623157;
    private final static int LIMIT_DEXP=308;
    private FileProcessor fileProcessor;
    private TablaDeSimbolos tablaDeSimbolos;
    private Reservado reservado;
    private CodigoFuente codigoFuente;
    private String sTemporal;
    private double numeroIntD=0; // Utilizado para AS-double (parte numerica y parte exp)

    public AccionSemantica (Reservado reservado, String pathFuente){
        fileProcessor= new FileProcessor();
        tablaDeSimbolos= new TablaDeSimbolos();
        this.reservado=reservado;
        this.codigoFuente= new CodigoFuente(fileProcessor.getLineas(pathFuente));
        this.numeroIntD=Double.NEGATIVE_INFINITY;
    }

    public void ejecutar(){}

    public class AS0{
        /**
         * Inicializa un string en vacio
         * @return
         */
        public void ejecutar (){
            sTemporal="";
        }
    }

    public class AS1{
        /**
         * Concatena un caracter al final de un string
         * @return
         */
        public void ejecutar (){
            StringBuilder cadena= new StringBuilder(sTemporal);
            cadena.append(codigoFuente.simboloActual());
            sTemporal=cadena.toString();
        }
    }

    public class AS2{
        /**
         * Si se excede el limite de un string, se trunca y genera un WARNING
         * @return
         */
        public void ejecutar (){
            if (LIMITE_STRING < sTemporal.length()){
                sTemporal=sTemporal.substring(0,LIMITE_STRING);
                fileProcessor.escribirArchivo("./warning.txt","WARNING: String truncado a: "+sTemporal,fileProcessor.existeArchivo("./warning.txt"));
            }
        }
    }

    /**
     * Devuelve un objeto con la celda de la tabla de simbolos segun el lexema
     */
    public class AS3{
        public Celda ejecutar(int token) {
            AS6 ultimo = new AS6();
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

    public class AS5{
        /**
         * Consumir caracter
         */
        public void ejecutar(){
            System.out.println("caracter consumido" + codigoFuente.simboloActual());
        }
    }
    public class AS6{
        /**
         * Devuelve el último caracter de un string
         * @return
         */
        public char ejecutar (){
           return sTemporal.charAt(sTemporal.length()-1);
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


    public class AS12{
        /**
         * Incrementa en uno la cantidad de lineas de un archivo.
         */
        private int cant_lineas=0;
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
