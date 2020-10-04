package analizador_sintactico.sent_declarativas.lista_variables;

import util.testing.RunSintactico;

public class Err_FaltaId {
    public static void main(String[] args) {
        RunSintactico.run(false,"UINT;"); //Error porque falta un id.

        RunSintactico.run(false,"UINT x,;");
    }
}
