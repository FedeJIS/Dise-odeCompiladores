package casos_prueba_asm.resta;

import compilador.Compilador;

public class Var_Var {
    public static void main(String[] args) {
        String lineasCFuente =
                "UINT a, b;\n" +
                "a = 7_ui;\n" +
                "b = 6_ui;\n" +
                "a = a - b;";

        Compilador.compilar(lineasCFuente,true,false);
    }
}
