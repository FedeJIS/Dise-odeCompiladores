package analizador_sintactico.sent_ejecutables.asignacion;

import util.RunSintactico;

public class Err_FaltaExpr {
    public static void main(String[] args) {
        String linea = "x = ;";
        RunSintactico.run(false,linea);
        System.out.println("$$$$$$$$");

        linea = "x = ";
        RunSintactico.run(false,linea);
        System.out.println("$$$$$$$$");
    }
}
