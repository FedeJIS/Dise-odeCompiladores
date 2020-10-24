package casos_prueba_sintactico;

import compilador.Compilador;

public class PROC_FaltaSeparacionParams {

    public static void main(String[] args) {
        String lineasCFuente
                = "PROC lala(DOUBLE a DOUBLE b, DOUBLE c) NI = 1_ui {\n" +
                "   OUT(a);\n" +
                "};\n" +

                "PROC lala(DOUBLE a, DOUBLE b DOUBLE c) NI = 1_ui {\n" +
                "   OUT(a);\n" +
                "};\n" +

                "lala(a b, c);\n" +
                "lala(a, b c);"

                ;

        Compilador.compilar(lineasCFuente);
    }
}
