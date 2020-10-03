package analizador_sintactico.sent_ejecutables.invocacion;

import util.RunSintactico;

public class OK {
    public static void main(String[] args) {
        RunSintactico.run(false,"w();");
        System.out.println("$$$$$$$$");
        RunSintactico.run(false,"w(x);");
        System.out.println("$$$$$$$$");
        RunSintactico.run(true,"w(x,y);");
        System.out.println("$$$$$$$$");
        RunSintactico.run(true,"w(x,y,z,t,w,o);");
    }
}
