package casos_prueba_asm.suma;

import compilador.Compilador;

public class Reg_Reg {
    public static void main(String[] args) {
        String lineasCFuente =
                "UINT a, b, c;\n" +
                "DOUBLE zz;\n" +
                "zz = -5.0d1;\n" +
                "a = 5_ui;\n" +
                "b = 6_ui;\n" +
                "c = 1_ui;\n" +
                "a = a*b + c*5_ui;";

        Compilador.compilar(lineasCFuente,true,true);
    }
}
