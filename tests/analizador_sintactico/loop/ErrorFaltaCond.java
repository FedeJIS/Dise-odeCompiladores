package analizador_sintactico.loop;

import util.RunSintactico;

public class ErrorFaltaCond {
    public static void main(String[] args) {
        RunSintactico.run(false,"LOOP UINT x; UNTIL");
        System.out.println("$$$$$$$$");

    }
}
