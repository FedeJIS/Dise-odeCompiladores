package casos_prueba_asm.suma;

import compilador.Compilador;

public class Var_Reg {
    public static void main(String[] args) {
        String lineasCFuente =
                "UINT a, b;\n" +
                "a = 5_ui;\n" +
                "b = 6_ui;\n" +
                "a = a + b*5_ui;";

        Compilador.compilar(lineasCFuente,true,false);
    }
}
