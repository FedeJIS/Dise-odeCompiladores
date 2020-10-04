package analizador_sintactico.sent_ejecutables.asignacion.factor;

import static util.testing.RunSintactico.execute;

public class CambioSigno_Menos_x3Menos {
    public static void main(String[] args) {
        /*
         * En la TS quedan la entrada '55.0' y '-55.0' con 1 referencia c/u. TODO No anda.
         */
        String linea =
                "x = -55.0;" +
                "y = ---55.0;";
        execute(linea);

    }
}
