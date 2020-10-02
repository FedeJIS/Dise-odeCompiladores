package analizador_sintactico.condicional;

import util.RunSintactico;

public class ErrorFaltaThen {
    public static void main(String[] args) {
        RunSintactico.run(false,"IF (x==y) OUT(x); END_IF;");
        System.out.println("$$$$$$$$");

        RunSintactico.run(false,"IF (x==y) OUT(x); ELSE OUT(y); END_IF;");
        System.out.println("$$$$$$$$");
    }
}
