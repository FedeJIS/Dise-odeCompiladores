package analizador_sintactico.sent_ejecutables.condicional;

import util.testing.RunSintactico;

public class Err_FaltaIf {
    public static void main(String[] args) {
        String linea =
                "(x==y) THEN\n" +
                    "UINT x\n;" +
                "END_IF;";
        RunSintactico.run(false,linea);
        System.out.println("$$$$$$$$");

        linea =
                "(x==y) THEN\n" +
                    "UINT x\n;" +
                "ELSE\n" +
                    "UINT y\n;" +
                "END_IF;";
        RunSintactico.run(false,linea);
        System.out.println("$$$$$$$$");
    }
}
