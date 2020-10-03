package analizador_sintactico.sent_ejecutables.asignacion;

import util.RunSintactico;

public class Err_FaltaPuntoComa {
    public static void main(String[] args) {
    String linea = "x = 2721";
    RunSintactico.run(false,linea);
    System.out.println("$$$$$$$$");
    }
}
