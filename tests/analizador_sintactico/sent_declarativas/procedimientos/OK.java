package analizador_sintactico.sent_declarativas.procedimientos;

import util.RunSintactico;

public class OK {
    public static void main(String[] args) {
        RunSintactico.run(false,"PROC procedimiento () NI = 1 {x = 5;};"); //Sin parametros.
        System.out.println();
        RunSintactico.run(false,"PROC procedimiento (VAR UINT x) NI = 1 {x = 5;};"); //1 parametro.
        System.out.println();
        RunSintactico.run(false,"PROC procedimiento (UINT x, DOUBLE y) NI = 1 {x = 5;};"); //2 parametros.
        System.out.println();
        RunSintactico.run(false,"PROC procedimiento (UINT x, DOUBLE y, UINT z) NI = 1 {x = 5;};"); //3 parametros.
    }
}
