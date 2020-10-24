package casos_prueba_sintactico;

import compilador.Compilador;

public class OUT_PrintInvalido {
    public static void main(String[] args) {
        String lineasCFuente =
                "OUT(OUT(x));";

        Compilador.compilar(lineasCFuente);

    }
}
