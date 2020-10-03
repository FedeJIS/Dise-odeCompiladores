package analizador_sintactico.sent_ejecutables.loop;

import util.RunSintactico;

public class Err_FaltaCond {
    public static void main(String[] args) {
        RunSintactico.run(false,"LOOP UINT x; UNTIL");
        System.out.println("$$$$$$$$");

    }
}
