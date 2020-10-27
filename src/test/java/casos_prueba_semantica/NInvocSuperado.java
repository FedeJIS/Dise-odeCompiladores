package casos_prueba_semantica;

import compilador.Compilador;

public class NInvocSuperado {
    public static void main(String[] args) {
        String lineasFuente =
                "PROC y() NI = 3_ui {\n" +
                    "OUT(5_ui);\n" +
                "};\n" +
                "y();\n" +
                "y();\n" +
                "y();\n" +
                "y();\n"
                ;

        Compilador.compilar(lineasFuente,false,true);
    }
}
