package analizador_sintactico.sentencias.error_falta_punto_coma.ejecutables;

import util.RunSintactico;

public class Otros {
    public static void main(String[] args) {
        String linea = "mi_funcion\n()";
        RunSintactico.run(false,linea);
        System.out.println("$$$$$$$$");

        linea = "mi_funcion(x,y,z)";
        RunSintactico.run(false,linea);
        System.out.println("$$$$$$$$");

        linea = "x = 2721";
        RunSintactico.run(false,linea);
        System.out.println("$$$$$$$$");

        linea = "OUT(xxx)";
        RunSintactico.run(false,linea);
        System.out.println("$$$$$$$$");
    }
}
