package casos_prueba_catedra;

import compilador.CompiladorFijo;

import java.util.ArrayList;
import java.util.List;

public class PR_Minusc {
    public static void main(String[] args) {
        List<String> lineasCFuente = new ArrayList<>();

        lineasCFuente.add("if (5 < 5) then " +
                            "out(5);" +
                        "end_if;");


        CompiladorFijo.compilar(lineasCFuente);
    }
}
