package analizador_sintactico.sent_ejecutables.loop;

import util.RunSintactico;

public class Err_CuerpoVacio {
    public static void main(String[] args) {
        RunSintactico.run(false,"LOOP UNTIL (5 < 10);");
        System.out.println("$$$$$$$$");
    }
}
