package analizador_lexico;

public class AccionSemantica {
    private FileProcessor fileProcessor= new FileProcessor();
    private TablaDeSimbolos tablaDeSimbolos= new TablaDeSimbolos();
    private Reservado reservado= new Reservado();
    private String sTemporal;

    public AccionSemantica (Reservado reservado){
        fileProcessor= new FileProcessor();
        tablaDeSimbolos= new TablaDeSimbolos();
        this.reservado=reservado;
    }
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
         * @param caracter
         * @return
         */
        public void ejecutar (char caracter){
            StringBuilder cadena= new StringBuilder(sTemporal);
            cadena.append(caracter);
            sTemporal=cadena.toString();
        }
    }

    public class AS2{
        /**
         * Si se excede el limite de un string, se trunca y genera un WARNING
         * @param cantCaracteres
         * @return
         */
        public void ejecutar (int cantCaracteres){
            if ((cantCaracteres > 0)&&(cantCaracteres < sTemporal.length())){
                sTemporal=sTemporal.substring(0,cantCaracteres);
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
         * @param c
         */
        public void ejecutar(char c){
            System.out.println("caracter consumido");
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




    public String getString(){
        return sTemporal;
    }
}
