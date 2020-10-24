package casos_prueba_polaca;

import compilador.Compilador;

public class Asign {
    public static void main(String[] args) {
        String lineasCFuente =
                "a = 99.0 * 5.0 + -4.0;";

        Compilador.compilar(lineasCFuente,true,false);
    }
}
