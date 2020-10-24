package casos_prueba_sintactico;

import compilador.Compilador;

public class LOOP_FaltaCond {
    public static void main(String[] args) {
        String lineasCFuente =
                "LOOP{\n" +
                " OUT(a);\n" +
                "} UNTIL;";

        Compilador.compilar(lineasCFuente,false,true);
    }
}
