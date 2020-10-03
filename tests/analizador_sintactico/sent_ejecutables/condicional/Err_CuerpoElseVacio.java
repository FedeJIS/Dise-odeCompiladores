package analizador_sintactico.sent_ejecutables.condicional;

import util.RunSintactico;

public class Err_CuerpoElseVacio {
    public static void main(String[] args) {
        String linea =
                "IF (x==y) THEN\n" +
                    "OUT(x);\n" +
                "ELSE\n" +
                "END_IF;";
        RunSintactico.run(false,linea);
        System.out.println("$$$$$$$$");
    }
}
