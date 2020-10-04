package analizador_sintactico.sent_declarativas.procedimientos;

import util.testing.RunSintactico;

public class Err_FaltaParentCierreParams {
    public static void main(String[] args) {
        String linea =
                "PROC procedimiento ( NI = 1\n" +
                "{\n" +
                    "x = 5;\n" +
                "};";
        RunSintactico.run(false,linea);
    }
}
