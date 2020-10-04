package analizador_sintactico.sent_ejecutables.loop;

import util.testing.RunSintactico;

public class OK {
    public static void main(String[] args) {
        RunSintactico.run(false,"LOOP UINT x; UNTIL (5 < 10);");
        System.out.println("$$$$$$$$");
        RunSintactico.run(false,"LOOP {UINT x;} UNTIL (5 < 10);");
        System.out.println("$$$$$$$$");
        RunSintactico.run(false,"LOOP {UINT x; x = 5;} UNTIL (5 < 10);");
        System.out.println("$$$$$$$$");
    }
}
