package casos_prueba_catedra;

import compilador.CompiladorFijo;

import java.util.ArrayList;
import java.util.List;

public class ID_EmpiezaGuion {
    public static void main(String[] args) {
        List<String> lineasCFuente = new ArrayList<>();

        lineasCFuente.add("_1 = 1_ui;");
        lineasCFuente.add("_ = 1_ui;");
        lineasCFuente.add("_a = 1_ui;");

        CompiladorFijo.compilar(lineasCFuente);
    }
}
