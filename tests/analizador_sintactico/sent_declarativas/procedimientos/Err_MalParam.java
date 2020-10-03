package analizador_sintactico.sent_declarativas.procedimientos;

import util.RunSintactico;

public class Err_MalParam {
    public static void main(String[] args) {
        RunSintactico.run(false,"PROC procedimiento (UINT VAR x) NI = 1 {x = 5;};"); //Mal declarado param.
        System.out.println();
        RunSintactico.run(false,"PROC procedimiento (VAR x) NI = 1 {x = 5;};"); //Mal declarado param.
    }
}
