package analizador_sintactico.condicional;

import util.RunSintactico;

public class OK {
    public static void main(String[] args) {
        RunSintactico.run(false,"IF (x==y) THEN OUT(x); END_IF;");
        System.out.println("$$$$$$$$");
        RunSintactico.run(false,"IF (x==y) THEN {OUT(x);x=5;} END_IF;");
        System.out.println("$$$$$$$$");
        RunSintactico.run(false,"IF (x==y) THEN OUT(x); ELSE OUT(y); END_IF;");
        System.out.println("$$$$$$$$");
        RunSintactico.run(false,"IF (x==y) THEN {OUT(x);x=5;} ELSE OUT(y); END_IF;");
        System.out.println("$$$$$$$$");
    }
}
