package analizador_sintactico.sent_declarativas.lista_variables;

import util.RunSintactico;

public class Err_FaltaId {
    public static void main(String[] args) {
        RunSintactico.run(false,"UINT W;"); //Error porque falta un id.
    }
}
