package analizador_sintactico.sent_ejecutables.asignacion.factor;

import static util.testing.RunSintactico.execute;

public class NReferencias_Mas_Mas {
    public static void main(String[] args) {
        /*
         * En la TS queda la entrada '55.0' con 2 referencia.
         */
        String linea =
                "x = 55.0;" +
                "y = 55.0;";
        execute(linea);
    }
}
