package analizador_sintactico.sent_declarativas.procedimientos;

import util.testing.RunSintactico;

public class Err_4Params {
    public static void main(String[] args) {
        RunSintactico.run(false,"PROC procedimiento (UINT x, DOUBLE y, UINT z, DOUBLE w) NI = 1 {" +
                "x = 5;};"); //4 parametros. Error porque hay mas de 3 params.

        System.out.println();

        RunSintactico.run(false,"PROC procedimiento (UINT x, DOUBLE y, UINT z, DOUBLE w, UINT m) NI = 1 {" +
                "x = 5;};"); //4 parametros. Error porque hay mas de 3 params.
    }
}
