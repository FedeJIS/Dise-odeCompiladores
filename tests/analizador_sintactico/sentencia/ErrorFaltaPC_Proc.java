package analizador_sintactico.sentencia;

import util.RunSintactico;

public class ErrorFaltaPC_Proc {
    public static void main(String[] args) {
        String linea =
                "PROC procedimiento (UINT x) NI = 1{\n" +
                    "OUT(x);\n" +
                "}";
        RunSintactico.run(false,linea);
        System.out.println("$$$$$$$$");
    }
}
