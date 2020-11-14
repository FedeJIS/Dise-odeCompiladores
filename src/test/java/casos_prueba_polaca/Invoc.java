package casos_prueba_polaca;

import compilador.Compilador;

public class Invoc {
    public static void main(String[] args) {
        String lineasCFuente =
                "PROC x() NI = 2_ui{\n" +
                    "OUT(5_ui);\n" +
                    "PROC y() NI = 3_ui{\n" +
                        "OUT(5_ui);\n" +
                    "};\n" +
                    "OUT(5_ui);" +
                "};\n" +
                "x();\n" +
                "OUT(\"a\");"
                ;

        Compilador.compilar(lineasCFuente,true,true);
    }
}
