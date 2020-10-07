package casos_prueba_catedra;

import compilador.CompiladorFijo;

import java.util.ArrayList;
import java.util.List;

public class DOUBLE_ValoresValidos {
    public static void main(String[] args) {
        List<String> lineasCFuente = new ArrayList<>();

        lineasCFuente.add("a = 1.;");
        lineasCFuente.add("a = .6;");
        lineasCFuente.add("a = -1.2;");
        lineasCFuente.add("a = 3.d-5;");
        lineasCFuente.add("a = 2.d34;");
        lineasCFuente.add("a = 2.5d1;");
        lineasCFuente.add("a = 13.;");
        lineasCFuente.add("a = 0.;");
        lineasCFuente.add("a = .d4;");

        CompiladorFijo.compilar(lineasCFuente);

    }
}
