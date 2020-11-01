package casos_prueba_polaca;

import compilador.Compilador;

public class LOOP {
    public static void main(String[] args) {
        String lineasCFuente =
                "UINT a;\n" +
                "LOOP\n" +
                    "a = a + 1;\n" +
                "UNTIL (a > 10_ui);";

        Compilador.compilar(lineasCFuente,true,false);

    }
}
