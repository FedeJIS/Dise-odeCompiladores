package analizador_sintactico.sent_ejecutables.condicional;

import util.testing.RunSintactico;

public class Err_FaltaThen {
    public static void main(String[] args) {
        String linea =
                "IF (x==y)\n" +
                    "OUT(x);\n" +
                "END_IF;";
        RunSintactico.run(false,linea);
        System.out.println("$$$$$$$$");
    }
}
