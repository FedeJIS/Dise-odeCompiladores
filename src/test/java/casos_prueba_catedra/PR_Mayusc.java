package casos_prueba_catedra;

import compilador.Compilador;

public class PR_Mayusc {
    public static void main(String[] args) {
        String lineasCFuente
                =  "IF (x < y) THEN "
                +       "OUT(y);"
                +   "END_IF;";

        Compilador.compilar(lineasCFuente,false,true);
    }
}
