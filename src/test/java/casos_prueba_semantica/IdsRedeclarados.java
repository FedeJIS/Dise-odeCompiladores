package casos_prueba_semantica;

import compilador.Compilador;

public class IdsRedeclarados {
    public static void main(String[] args) {
        String lineasFuente =
                "UINT z; \n" +
                "PROC x(VAR UINT a, DOUBLE a, UINT c) NI = 5_ui {\n" +
                    "UINT z;\n" +
                    "PROC ww() NI = 1_ui{\n" +
                        "UINT c;\n" +
                        "UINT abc;\n" +
                    "};\n" +
                    "PROC pp() NI = 1_ui{\n" +
                        "UINT abc;\n" +
                    "};\n" +
                "};";

        Compilador.compilar(lineasFuente,false,true);
    }
}
