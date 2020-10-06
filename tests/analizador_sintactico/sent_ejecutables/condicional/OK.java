package analizador_sintactico.sent_ejecutables.condicional;

import util.testing.RunSintactico;

public class OK {
    public static void main(String[] args) {
        RunSintactico.run(false,"IF (x==y) THEN OUT(x); END_IF;");
//        System.out.println("$$$$$$$$");
        RunSintactico.run(false,"IF (x==y) THEN {OUT(x);x=5;} END_IF;");
//        System.out.println("$$$$$$$$");
        RunSintactico.run(false,"IF (x==y) THEN OUT(x); ELSE OUT(y); END_IF;");
//        System.out.println("$$$$$$$$");
        RunSintactico.run(false,"IF (x==y) THEN {OUT(x);PROC procedimiento () NI = 1 {x = 5;};} ELSE OUT(y); END_IF;");
//        System.out.println("$$$$$$$$");

        String linea =
                "IF (x==y) THEN {\n" +
                    "x = 5;\n" +
                    "y = 10;\n" +
                "} END_IF;";

        RunSintactico.run(false,linea);
        System.out.println("$$$$$$$$");
    }
}
