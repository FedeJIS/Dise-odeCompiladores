package casos_prueba_polaca;

import compilador.Compilador;

public class LOOP {
    public static void main(String[] args) {
        String lineasCFuente =
                "UINT a;\n" +
                "a = 5_ui;\n" +
                "LOOP{\n" +
                    "IF (a == a) THEN\n" +
                        "LOOP\n" +
                            "OUT(a);\n" +
                        "UNTIL (a == 5_ui);\n" +
                    "END_IF;\n" +
                    "a = a + 1_ui;\n" +
                "}UNTIL (a > 10_ui);";

        Compilador.compilar(lineasCFuente,true,false);
    }
}
