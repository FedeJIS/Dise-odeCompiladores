package casos_prueba_catedra;

import compilador.Compilador;

public class UINT_EnRango {
    public static void main(String[] args) {
        int min = 0;
        int max = (int)Math.pow(2,16)-1;

        String lineasCFuente =
                "y = "+min+"_ui;\n" +
                "x = "+max+"_ui;";

        Compilador.compilar(lineasCFuente);
    }
}
