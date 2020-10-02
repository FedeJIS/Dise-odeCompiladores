package analizador_sintactico.loop;

import util.RunSintactico;

public class ErrorCuerpoVacio {
    public static void main(String[] args) {
        RunSintactico.run(false,"LOOP UNTIL (5 < 10);");
        System.out.println("$$$$$$$$");
    }
}
