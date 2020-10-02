package analizador_sintactico.lista_variables;

import util.RunSintactico;

public class Test_Error {
    public static void main(String[] args) {
        RunSintactico.run(false,"UINT W;"); //Error porque falta un id.
    }
}
