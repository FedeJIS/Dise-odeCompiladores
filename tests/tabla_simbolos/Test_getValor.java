package tabla_simbolos;

import analizador_lexico.Celda;
import analizador_lexico.TablaDeSimbolos;

public class Test_getValor {
    public static void main(String[] args) {
        TablaDeSimbolos tS = new TablaDeSimbolos();

        int token = 1;
        String lexema = "5";
        tS.agregar(new Celda(token,lexema,""));

        int resultadoToken = tS.getValor(lexema).getToken();
        System.out.println("Token esperado: "+token+". Token conseguido: "+resultadoToken);

        try {
            tS.getValor("lexema");
        } catch (IllegalStateException illegalStateException){
            System.out.println(illegalStateException.getMessage());
        }
    }
}