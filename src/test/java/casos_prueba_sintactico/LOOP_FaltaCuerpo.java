package casos_prueba_sintactico;

import compilador.Compilador;

public class LOOP_FaltaCuerpo {
    public static void main(String[] args) {
        String lineasCFuente =
                "LOOP\n" +
                " \n" +
                "UNTIL (a == 1_ui);";

        Compilador.compilar(lineasCFuente);
    }
}
