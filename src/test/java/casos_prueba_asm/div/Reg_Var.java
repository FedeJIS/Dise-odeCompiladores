package casos_prueba_asm.div;

import compilador.Compilador;

public class Reg_Var {
    public static void main(String[] args) {
        String lineasCFuente =
                "UINT a, b;\n" +
                "a = 30_ui;\n" +
                "b = 6_ui;\n" +
                "a = a / b / a;";

        Compilador.compilar(lineasCFuente,true,false);
    }
}
