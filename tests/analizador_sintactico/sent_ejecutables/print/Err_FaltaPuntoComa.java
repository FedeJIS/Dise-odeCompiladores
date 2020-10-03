package analizador_sintactico.sent_ejecutables.print;

import util.RunSintactico;

public class Err_FaltaPuntoComa {
    public static void main(String[] args) {
        String linea = "OUT(xxx)";
        RunSintactico.run(false,linea);
        System.out.println("$$$$$$$$");
    }
}
