package casos_prueba_catedra;

import compilador.CompiladorFijo;

import java.util.ArrayList;
import java.util.List;

public class UINT_EnRango {
    public static void main(String[] args) {
        List<String> lineasCFuente = new ArrayList<>();

        int min = 0;
        int max = (int)Math.pow(2,16)-1;
        lineasCFuente.add("y = "+min+"_ui;");
        lineasCFuente.add("x = "+max+"_ui;");
        CompiladorFijo.compilar(lineasCFuente);
    }
}
