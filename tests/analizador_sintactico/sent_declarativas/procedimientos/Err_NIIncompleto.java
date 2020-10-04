package analizador_sintactico.sent_declarativas.procedimientos;

import util.testing.RunSintactico;

public class Err_NIIncompleto {
    public static void main(String[] args) {
        String linea =
                "PROC x() NI = {\n" +
                        "x = 5;\n" +
                        "};";
        RunSintactico.run(false,linea);

        linea =
                "PROC x() NI {\n" +
                        "x = 5;\n" +
                    "};";
        RunSintactico.run(false,linea);

        linea =
                "PROC x() = {\n" +
                        "x = 5;\n" +
                        "};";
        RunSintactico.run(false,linea);
    }
}
