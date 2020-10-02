package analizador_sintactico.condicional;

import util.RunSintactico;

public class ErrorCuerpoElseVacio {
    public static void main(String[] args) {
        RunSintactico.run(false,"IF (x==y) THEN ELSE END_IF;");
        System.out.println("$$$$$$$$");
    }
}
