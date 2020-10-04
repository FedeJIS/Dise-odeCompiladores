package analizador_sintactico.sent_ejecutables.condicional;

import util.testing.RunSintactico;

public class Err_FaltaCond {
    public static void main(String[] args) {
        String linea =
                "IF () THEN\n" +
                    "UINT x\n;" +
                "END_IF;";
        RunSintactico.run(false,linea);
        System.out.println("$$$$$$$$");

        linea =
                "IF (THEN\n" +
                    "UINT x\n;" +
                "ELSE\n" +
                    "UINT y\n;" +
                "END_IF;";
        RunSintactico.run(false,linea);
        System.out.println("$$$$$$$$");
    }
}
