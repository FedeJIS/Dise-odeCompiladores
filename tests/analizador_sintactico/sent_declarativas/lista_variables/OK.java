package analizador_sintactico.sent_declarativas.lista_variables;

import util.testing.RunSintactico;

public class OK {
    public static void main(String[] args) {
        RunSintactico.run(false,"UINT x, y, z,w;");
        System.out.println();
    }
}
