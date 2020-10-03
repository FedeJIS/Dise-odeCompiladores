package analizador_sintactico.sent_ejecutables.loop;

import util.RunSintactico;

public class Err_CuerpoVacio {
    public static void main(String[] args) {
        String linea =
                "LOOP\n" +
                    "" +
                "UNTIL\n" +
                    "(x==y)" +
                ";";
        RunSintactico.run(false,linea);
        System.out.println("$$$$$$$$");
    }
}
