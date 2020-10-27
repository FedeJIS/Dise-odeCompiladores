package casos_prueba_semantica;

import compilador.Compilador;

public class ProcsRedeclarados {
    public static void main(String[] args) {
        String lineasFuente =
                "UINT x;\n" +
                "PROC x(VAR UINT a, DOUBLE b, UINT c) NI = 5_ui {\n" +
                    "UINT z;\n" +
                    "PROC x() NI = 1_ui{\n" +
                        "UINT fgh;\n" +
                        "UINT abc;\n" +
                    "};\n" +
                    "PROC x() NI = 1_ui{\n" +
                        "UINT abcd;\n" +
                    "};\n" +
                "};";

        Compilador.compilar(lineasFuente,false,true);
    }
}
