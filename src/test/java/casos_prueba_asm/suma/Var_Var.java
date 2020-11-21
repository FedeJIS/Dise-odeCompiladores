package casos_prueba_asm.suma;

import compilador.Compilador;

public class Var_Var {
    public static void main(String[] args) {
        String lineasCFuente =
                "UINT a, b, c;\n" +
                "a = 5_ui;\n" +
                "b = 6_ui;\n" +
                "c = a + b;" +
                "IF (c == a + b) THEN \n" +
                    "OUT(\"BIEN\");\n" +
                "ELSE OUT(\"MAL\");\n" +
                "END_IF;";

        Compilador.compilar(lineasCFuente,true,false);
    }
}
