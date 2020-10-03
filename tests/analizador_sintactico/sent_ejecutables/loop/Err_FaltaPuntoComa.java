package analizador_sintactico.sent_ejecutables.loop;

import util.RunSintactico;

public class Err_FaltaPuntoComa {
    public static void main(String[] args) {
        String linea =
                "LOOP\n" +
                    "OUT(x);\n" +
                "UNTIL (x >= y)";
        RunSintactico.run(false,linea);
        System.out.println("$$$$$$$$");
    }
}
