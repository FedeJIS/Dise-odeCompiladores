package analizador_sintactico.sent_declarativas.procedimientos;

import util.testing.RunSintactico;

public class Err_FaltaPuntoComa {
    public static void main(String[] args) {
        String linea =
                "PROC procedimiento (UINT x) NI = 1{\n" +
                    "OUT(x);\n" +
                "}";
        RunSintactico.run(false,linea);
        System.out.println("$$$$$$$$");
    }
}
