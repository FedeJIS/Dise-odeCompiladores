package analizador_sintactico.sentencias.error_falta_punto_coma.ejecutables;

import util.RunSintactico;

public class Loop {
    public static void main(String[] args) {
        String linea =
                "LOOP\n" +
                    "OUT(x);\n" +
                "UNTIL (x >= y)";
        RunSintactico.run(false,linea);
        System.out.println("$$$$$$$$");
    }
}
