package analizador_sintactico.sentencias.error_falta_punto_coma.declarativas;

import util.RunSintactico;

public class Procedimientos {
    public static void main(String[] args) {
        String linea =
                "PROC procedimiento (UINT x) NI = 1{\n" +
                    "OUT(x);\n" +
                "}";
        RunSintactico.run(false,linea);
        System.out.println("$$$$$$$$");
    }
}
