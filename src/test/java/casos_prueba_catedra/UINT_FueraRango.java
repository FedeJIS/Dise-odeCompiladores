package casos_prueba_catedra;

import compilador.Compilador;

public class UINT_FueraRango {
    public static void main(String[] args) {
        int max_pasado = (int)Math.pow(2,16);

        String lineasCFuente =
                "y = "+max_pasado+"_ui;\n" +
                "x = "+(max_pasado+1)+"_ui;";

        Compilador.compilar(lineasCFuente);
    }
}
