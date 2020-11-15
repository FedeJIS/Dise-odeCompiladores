package casos_prueba_asm.jumps;

import compilador.Compilador;

public class Jumps {
    public static void main(String[] args) {
        String lineasCFuente =
            "IF (5_ui < 1_ui) THEN\n" +
                "OUT(5_ui);\n" +
            "END_IF;";

        Compilador.compilar(lineasCFuente,true,true);
    }
}
