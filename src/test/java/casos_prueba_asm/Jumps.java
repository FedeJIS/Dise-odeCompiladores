package casos_prueba_asm;

import compilador.Compilador;

public class Jumps {
    public static void main(String[] args) {
        String lineasCFuente =
            "UINT a, b;\n" +
            "a = 5_ui;\n" +
            "b = 5_ui;" +
            "IF (a < b) THEN\n" +
                "OUT(5_ui);\n" +
            "END_IF;";

        Compilador.compilar(lineasCFuente,true,true);
    }
}
