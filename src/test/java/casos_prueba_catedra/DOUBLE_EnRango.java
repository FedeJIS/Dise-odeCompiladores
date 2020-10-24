package casos_prueba_catedra;

import compilador.Compilador;

public class DOUBLE_EnRango {
    public static void main(String[] args) {
        String lineasCFuente
            = "min_posit = 2.2250738585072014d-308;\n"
            + "max_posit = 1.7976931348623157d308;\n"
            + "min_negat = -2.2250738585072014d-308;\n"
            + "max_negat = -1.7976931348623157d308;";

        Compilador.compilar(lineasCFuente);
    }
}
