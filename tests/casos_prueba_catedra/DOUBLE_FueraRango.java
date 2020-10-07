package casos_prueba_catedra;

import compilador.CompiladorFijo;

import java.util.ArrayList;
import java.util.List;

public class DOUBLE_FueraRango {
    public static void main(String[] args) {
        List<String> lineasCFuente = new ArrayList<>();

        lineasCFuente.add("min_posit = 2.2250738585072011d-308;");
        lineasCFuente.add("max_posit = 1.7976931348623159d308;");
        lineasCFuente.add("min_negat = -2.2250738585072011d-308;");
        lineasCFuente.add("max_negat = -1.7976931348623159d308;");

        CompiladorFijo.compilar(lineasCFuente);

    }
}
