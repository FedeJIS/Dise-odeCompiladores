package analizador_sintactico.sent_ejecutables.invocacion;

import util.testing.RunSintactico;

public class Err_NIIncompleto {
    public static void main(String[] args) {
        String linea =
                "PROC x() NI = {\n" +
                        "x = 5;\n" +
                        "};";
        RunSintactico.run(false,linea);

    }
}
