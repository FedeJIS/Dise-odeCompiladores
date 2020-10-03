package analizador_sintactico.sent_ejecutables.condicional;

import util.RunSintactico;

public class Err_FaltaPuntoComa {
    public static void main(String[] args) {
        String linea =
                "IF (x==y) THEN\n" +
                    "OUT(x);\n" +
                "END_IF";
        RunSintactico.run(false,linea);
        System.out.println("$$$$$$$$");

        linea =
                "IF (x==y) THEN\n" +
                    "OUT(x);\n" +
                "ELSE\n" +
                    "OUT(y);\n" +
                "END_IF";

        RunSintactico.run(false,linea);
        System.out.println("$$$$$$$$");
    }
}
