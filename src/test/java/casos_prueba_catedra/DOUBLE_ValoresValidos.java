package casos_prueba_catedra;

import compilador.Compilador;
import compilador.CompiladorFijo;

import java.util.ArrayList;
import java.util.List;

public class DOUBLE_ValoresValidos {
    public static void main(String[] args) {
        String lineasCFuente

        = "a = 1.;" +'\n'
        + "a = .6;" +'\n'
        + "a = -1.2;" +'\n'
        + "a = 3.d-5;" +'\n'
        + "a = 2.d34;" +'\n'
        + "a = 2.5d1;" +'\n'
        + "a = 13.;" +'\n'
        + "a = 0.;" +'\n'
        + "a = .d4;" +'\n';

        Compilador.compilar(lineasCFuente);

    }
}
