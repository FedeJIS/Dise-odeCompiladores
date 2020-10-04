package tabla_simbolos;

import util.tabla_simbolos.Celda;
import util.tabla_simbolos.TablaDeSimbolos;

public class Test_getValor {
    public static void main(String[] args) {
        TablaDeSimbolos tS = new TablaDeSimbolos();

        int token = 1;
        String lexema = "5";

        tS.agregarEntrada(token,lexema,"");

        int resultadoToken = tS.getValor(lexema).getToken();

        System.out.println("Token esperado: "+token+". Token conseguido: "+resultadoToken);

        try {
            tS.getValor(lexema);
            tS.printAll();
        } catch (IllegalStateException illegalStateException){
            System.out.println(illegalStateException.getMessage());
        }
    }
}
