package analizador_sintactico.sent_declarativas.procedimientos;

import util.testing.RunSintactico;

public class Err_MalParamVAR {
    public static void main(String[] args) {
        String linea =
                "PROC procedimiento (VAR x) NI = 1{\n" +
                    "OUT(x);\n" +
                "};";
        RunSintactico.run(false,linea);
        System.out.println("$$$$$$$$");

         linea =
                "PROC procedimiento (VAR UINT) NI = 1{\n" +
                    "OUT(x);\n" +
                "};";
        RunSintactico.run(false,linea);
        System.out.println("$$$$$$$$");
    }
}
