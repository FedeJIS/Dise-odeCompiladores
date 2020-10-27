package casos_prueba_semantica;

import compilador.Compilador;

public class NInvocInvalido {
    public static void main(String[] args) {
        String lineasFuente =
                "PROC y() NI = 0_ui {\n" +
                    "OUT(5_ui);\n" +
                "};\n" +
                "PROC z() NI = 5_ui {\n" +
                    "OUT(5_ui);\n" +
                "};"
                ;

        Compilador.compilar(lineasFuente,false,true);
    }
}
