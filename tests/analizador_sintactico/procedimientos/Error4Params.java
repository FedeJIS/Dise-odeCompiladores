package analizador_sintactico.procedimientos;

import util.RunSintactico;

public class Error4Params {
    public static void main(String[] args) {
        RunSintactico.run(false,"PROC procedimiento (UINT x, DOUBLE y, UINT z, DOUBLE w) NI = 1 {" +
                "x = 5;};"); //4 parametros. Error porque hay mas de 3 params.
    }
}