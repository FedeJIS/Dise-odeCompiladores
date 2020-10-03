package analizador_sintactico.sentencias.error_falta_punto_coma.declarativas;

import util.RunSintactico;

public class ListaVar {
    public static void main(String[] args) {
        String linea = "UINT x,y,z,w";
        RunSintactico.run(false,linea);
        System.out.println("$$$$$$$$");
    }
}
