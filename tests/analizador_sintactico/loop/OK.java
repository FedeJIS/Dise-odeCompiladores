package analizador_sintactico.loop;

import util.RunSintactico;

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
