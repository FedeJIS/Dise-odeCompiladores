package analizador_sintactico.condicional;

import util.RunSintactico;

public class ErrorFaltaIf {
    public static void main(String[] args) {
        RunSintactico.run(false,"(x==y) THEN UINT x; END_IF;");
        System.out.println("$$$$$$$$");
        RunSintactico.run(false,"(x==y) THEN UINT x; ELSE UINT y; END_IF;");
        System.out.println("$$$$$$$$");
    }
}
