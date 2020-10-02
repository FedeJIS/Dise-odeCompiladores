package analizador_sintactico.condicional;

import util.RunSintactico;

public class ErrorCuerpoThenVacio {
    public static void main(String[] args) {
        RunSintactico.run(false,"IF (x==y) THEN END_IF;");
        System.out.println("$$$$$$$$");
        RunSintactico.run(false,"IF (x==y) THEN ELSE OUT(y); END_IF;");
        System.out.println("$$$$$$$$");
    }
}
