package analizador_sintactico.condicional;

import util.RunSintactico;

public class ErrorFaltaEndIf {
    public static void main(String[] args) {
        RunSintactico.run(false,"IF (x==y) THEN OUT(x);");
        System.out.println("$$$$$$$$");

        RunSintactico.run(false,"IF (x==y) THEN OUT(x); ELSE OUT(y);");
        System.out.println("$$$$$$$$");
    }
}
