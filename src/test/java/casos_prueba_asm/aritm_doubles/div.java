package casos_prueba_asm.aritm_doubles;

import compilador.Compilador;

public class div {
    public static void main(String[] args) {
        String lineasCFuente =
                "DOUBLE a, b, c;\n" +
                "a = 6.0d1;\n" +
                "b = 7.0d1;\n" +
                "c = 8.0d1;\n" +
                "a = a / b / c;";

        Compilador.compilar(lineasCFuente,true,false);

    }
}
