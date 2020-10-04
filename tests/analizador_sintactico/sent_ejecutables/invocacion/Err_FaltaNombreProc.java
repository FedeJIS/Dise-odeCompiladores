package analizador_sintactico.sent_ejecutables.invocacion;

import util.testing.RunSintactico;

public class Err_FaltaNombreProc {
    public static void main(String[] args) {
        String linea =
                "PROC () NI = 1\n" +
                "{\n" +
                    "x = 5;\n" +
                "};";
        RunSintactico.run(false,linea);
    }
}
