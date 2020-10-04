package analizador_sintactico.sent_ejecutables.invocacion;

import util.testing.RunSintactico;

public class Err_FaltaParentCierre {
    public static void main(String[] args) {
        RunSintactico.run(false,"w(;");
        System.out.println("$$$$$$$$");
        RunSintactico.run(false,"w(x;");
        System.out.println("$$$$$$$$");
        RunSintactico.run(false,"w(x,y;");
        System.out.println("$$$$$$$$");
        RunSintactico.run(false,"w(x,y,z,t,w,o;");
    }
}
