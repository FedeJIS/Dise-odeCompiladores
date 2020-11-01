package casos_prueba_polaca;

import compilador.Compilador;

public class Condicional {
    public static void main(String[] args) {
        String lineasCFuente =
                "UINT a,b,c;\n" +
//                "a = 5_ui; b = 4_ui; c = 3_ui;" +
                "IF (a - b > c + 1_ui) THEN\n" +
                    "a = b + c;\n" +
                "ELSE a = b - c;" +
                "END_IF;";

        Compilador.compilar(lineasCFuente,true,false);
    }
}
