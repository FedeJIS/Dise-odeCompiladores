package casos_prueba_catedra;

import compilador.CompiladorFijo;

import java.util.ArrayList;
import java.util.List;

public class UINT_FueraRango {
    public static void main(String[] args) {
        List<String> lineasCFuente = new ArrayList<>();

        int min = -1;
        int max = (int)Math.pow(2,16);
        lineasCFuente.add("y = "+min+"_ui;");
        lineasCFuente.add("x = "+max+"_ui;");
        CompiladorFijo.compilar(lineasCFuente);
    }
}
