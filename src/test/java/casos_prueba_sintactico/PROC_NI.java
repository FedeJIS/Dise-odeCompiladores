package casos_prueba_sintactico;

import compilador.Compilador;

public class PROC_NI {
    public static void main(String[] args) {
        String lineasCFuente =
                "PROC lala() NI = {\n" +
                "   OUT(a);\n" +
                "};\n" +

                "PROC lala() = 1_ui {\n" +
                "   OUT(a);\n" +
                "};\n" +

                "PROC lala() NI 1_ui {\n" +
                "   OUT(a);\n" +
                "};\n" +

                "PROC lala() 1_ui {\n" +
                "   OUT(a);\n" +
                "};\n"
                ;

        Compilador.compilar(lineasCFuente,false,true);
    }
}
