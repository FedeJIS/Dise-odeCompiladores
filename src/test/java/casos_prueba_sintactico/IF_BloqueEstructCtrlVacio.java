package casos_prueba_sintactico;

import compilador.Compilador;

public class IF_BloqueEstructCtrlVacio {
    public static void main(String[] args) {
        String lineasCFuente =
                "LOOP{\n" +
                "\n  " +
                "} UNTIL (a == 1_ui);\n" +
                "" +
                "IF (a==1_ui) THEN\n" +
                "{" +
                "\n" +
                "}END_IF;\n"
                ;

        Compilador.compilar(lineasCFuente);
    }
}
