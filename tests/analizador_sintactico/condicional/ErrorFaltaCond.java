package analizador_sintactico.condicional;

import util.RunSintactico;

public class ErrorFaltaCond {
    public static void main(String[] args) {
        RunSintactico.run(false,"IF THEN OUT(x); END_IF;");
        System.out.println("$$$$$$$$");

        RunSintactico.run(false,"IF THEN OUT(x); ELSE OUT(y); END_IF;");
        System.out.println("$$$$$$$$");
    }
}
