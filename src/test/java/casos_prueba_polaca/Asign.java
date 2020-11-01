package casos_prueba_polaca;

import compilador.Compilador;

public class Asign {
    public static void main(String[] args) {
        String lineasCFuente =
                "DOUBLE a;\n" +
                "a = 99.0 * -55.0 + -4.0;";

        Compilador.compilar(lineasCFuente,true,true);
    }
}
