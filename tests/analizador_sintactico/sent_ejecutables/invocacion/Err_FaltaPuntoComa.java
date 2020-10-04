package analizador_sintactico.sent_ejecutables.invocacion;

import util.testing.RunSintactico;

public class Err_FaltaPuntoComa {
    public static void main(String[] args) {
        String linea = "mi_funcion\n()";
        RunSintactico.run(false,linea);
        System.out.println("$$$$$$$$");

        linea = "mi_funcion(x,y,z)";
        RunSintactico.run(false,linea);
        System.out.println("$$$$$$$$");
    }
}
