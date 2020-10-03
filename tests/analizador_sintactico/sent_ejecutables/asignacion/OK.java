package analizador_sintactico.sent_ejecutables.asignacion;

import util.RunSintactico;

public class OK {
    public static void main(String[] args) {
        RunSintactico.run(false,"x = 55;");
        System.out.println("$$$$$$$$");
        RunSintactico.run(false,"x = 55 * 5;");
        System.out.println("$$$$$$$$");
        RunSintactico.run(false,"x = 55 / 5;");
        System.out.println("$$$$$$$$");
        RunSintactico.run(false,"x = 55 + 5;");
        System.out.println("$$$$$$$$");
        RunSintactico.run(false,"x = 55 - 5;");
        System.out.println("$$$$$$$$");
        RunSintactico.run(false,"x = -55    ;");
        System.out.println("$$$$$$$$");
    }
}
