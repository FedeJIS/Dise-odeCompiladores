package casos_prueba_catedra;

import compilador.Compilador;
import compilador.CompiladorFijo;

import java.util.ArrayList;
import java.util.List;

public class PR_Mayusc {
    public static void main(String[] args) {
        String lineasCFuente
                =  "IF (x < y) THEN "
                +       "OUT(y);"
                +   "END_IF;";

        Compilador.compilar(lineasCFuente);
    }
}
