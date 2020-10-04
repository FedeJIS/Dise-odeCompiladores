package analizador_sintactico.sent_ejecutables.asignacion;

import static util.testing.RunSintactico.execute;

public class Err_Generico {
    public static void main(String[] args) {
//        execute("x = -55.0*;");

//        execute("= -55.0*;");

        execute("= -55.0*");

//        execute("= ;");
    }
}
