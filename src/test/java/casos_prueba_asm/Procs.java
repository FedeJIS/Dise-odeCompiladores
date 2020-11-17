package casos_prueba_asm;

import compilador.Compilador;

public class Procs {
    public static void main(String[] args) {
        String lineasCFuente =
                "PROC x(VAR UINT a) NI = 1_ui{\n" +
                    "OUT(5_ui);\n" +
                    "PROC y() NI = 2_ui {\n" +
                        "OUT(5_ui);\n" +
                        "DOUBLE www;\n" +
                    "};" +
                    "PROC z() NI = 4_ui {\n" +
                        "OUT(6_ui);\n" +
//                        "z(); \n" +
                    "};\n" +
                    "z();\n" +
                "};\n" +
                "UINT aa;\n" +
                "aa = 5_ui;" +
                "x(aa);";

        Compilador.compilar(lineasCFuente,true,true);
    }
}
