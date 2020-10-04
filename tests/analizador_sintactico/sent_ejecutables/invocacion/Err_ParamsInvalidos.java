package analizador_sintactico.sent_ejecutables.invocacion;

import util.testing.RunSintactico;

public class Err_ParamsInvalidos {
    public static void main(String[] args) {
        String linea = "mi_funcion\n(UINT,x,DOUBLE);";
        RunSintactico.run(false,linea);
        System.out.println("$$$$$$$$");
    }
}
