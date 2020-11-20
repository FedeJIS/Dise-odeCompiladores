package casos_prueba_asm;

import compilador.Compilador;

public class Jumps {
    public static void main(String[] args) {
//        String lineasCFuente = "OUT(\"CADENA\");";
//        String lineasCFuente =
//                "OUT(5.0d1);\n" +
//                "DOUBLE a;\n" +
//                "a = -5.0d2;\n" +
//                "OUT(a);";
        String lineasCFuente =
                "IF (5.0d1 < 10.0d1) THEN\n" +
                "OUT(5_ui);\n" +
                "END_IF;";

        Compilador.compilar(lineasCFuente,true,true);
    }
}
