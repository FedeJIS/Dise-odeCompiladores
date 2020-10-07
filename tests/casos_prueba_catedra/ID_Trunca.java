package casos_prueba_catedra;

import compilador.CompiladorFijo;

import java.util.ArrayList;
import java.util.List;

public class ID_Trunca {
    public static void main(String[] args) {
        List<String> lineasCFuente = new ArrayList<>();

        lineasCFuente.add("a_1234567891234567891 = 1_ui; %%21 simbolos");
        lineasCFuente.add("a____________________ = 1_ui; %%21 simbolos");
        lineasCFuente.add("abcdefghijklmnopqrstu = 1_ui; %%21 simbolos");

        CompiladorFijo.compilar(lineasCFuente);
    }
}
