package casos_prueba_polaca;

import compilador.Compilador;

public class Asign {
    public static void main(String[] args) {
        String lineasCFuente =
                "DOUBLE a;\n" +
                "a = b * c;";

        Compilador.compilar(lineasCFuente,true,true);
    }
}
