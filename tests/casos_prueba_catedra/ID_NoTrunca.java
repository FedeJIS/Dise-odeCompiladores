package casos_prueba_catedra;

import compilador.CompiladorFijo;

import java.util.ArrayList;
import java.util.List;

public class ID_NoTrunca {
    public static void main(String[] args) {
        List<String> lineasCFuente = new ArrayList<>();

        lineasCFuente.add("a_123456789123456789 = 1_ui; %%20 simbolos");
        lineasCFuente.add("a___________________ = 1_ui; %%20 simbolos");
        lineasCFuente.add("abcdefghijklmnopqrst = 1_ui; %%20 simbolos");
        lineasCFuente.add("a_12345678912345678 = 1_ui; %%19 simbolos");

        CompiladorFijo.compilar(lineasCFuente);
    }
}
