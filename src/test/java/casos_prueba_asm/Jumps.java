package casos_prueba_asm;

import compilador.Compilador;

public class Jumps {
    public static void main(String[] args) {
        String lineasCFuente = "OUT(\"CADENA\");";
//            "IF (5.0d1 < 10.0d1) THEN\n" +
//                "OUT(5_ui);\n" +
//            "END_IF;";

        Compilador.compilar(lineasCFuente,true,true);
    }
}
