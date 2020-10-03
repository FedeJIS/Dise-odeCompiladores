package analizador_sintactico.sent_declarativas.lista_variables;

import util.RunSintactico;

public class Err_FaltaPuntoComa {
    public static void main(String[] args) {
        String linea = "UINT x,y,z,w";
        RunSintactico.run(false,linea);
        System.out.println("$$$$$$$$");
    }
}
