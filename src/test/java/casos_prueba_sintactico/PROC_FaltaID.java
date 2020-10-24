package casos_prueba_sintactico;

import compilador.Compilador;

public class PROC_FaltaID {
    public static void main(String[] args) {
        String lineasCFuente
                = "PROC (DOUBLE a) NI = 1_ui {\n" +
                "   OUT(a);\n" +
                "};";

        Compilador.compilar(lineasCFuente);
    }
}
