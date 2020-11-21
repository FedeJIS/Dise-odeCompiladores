package casos_prueba_asm.aritm_doubles;

import compilador.Compilador;

public class suma {
    public static void main(String[] args) {
        String lineasCFuente =
                "DOUBLE a, b, c;\n" +
                "a = 6.0d1;\n" +
                "b = 7.0d1;\n" +
                "c = a + b;\n" +
                "OUT(c);\n" +
                "IF (c == a + b) THEN \n" +
                    "OUT(\"BIEN\");\n" +
                "ELSE OUT(\"MAL\");\n" +
                "END_IF;";

        Compilador.compilar(lineasCFuente,true,false);

    }
}
