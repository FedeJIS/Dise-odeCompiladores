package casos_prueba_semantica;

import compilador.Compilador;

public class CheckAsignaciones {
    public static void main(String[] args) {
        String lineasFuente =
                "x = 5_ui;\n" +
                "PROC y() NI = 5_ui {\n" +
                    "OUT(5_ui);\n" +
                "};\n" +
                "UINT y;\n" +
                "y = 5_ui;"
                ;

        Compilador.compilar(lineasFuente,false,true);
    }
}
