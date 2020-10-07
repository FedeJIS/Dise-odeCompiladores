package casos_prueba_catedra;

import compilador.CompiladorFijo;

import java.util.ArrayList;
import java.util.List;

public class PR_Mayusc {
    public static void main(String[] args) {
        List<String> lineasCFuente = new ArrayList<>();

        lineasCFuente.add("IF (x < y) THEN ");
        lineasCFuente.add("OUT(y);");
        lineasCFuente.add("END_IF;");


        CompiladorFijo.compilar(lineasCFuente);



    }
}
