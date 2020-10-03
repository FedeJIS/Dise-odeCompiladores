package analizador_sintactico.sent_ejecutables.loop;

import util.RunSintactico;

public class Err_FaltaCond {
    public static void main(String[] args) {
        String linea =
                "LOOP\n" +
                    "x = 1;\n" +
                "UNTIL ();";
        RunSintactico.run(false,linea);
        System.out.println("$$$$$$$$");
    }
}
