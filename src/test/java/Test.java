import compilador.Compilador;

public class Test {

    public static void main(String[] args) {
        String lineasCFuente =
                "OUT(x);\n" +
                "OUT(y);\n" +
                "PROC p() NI = 1_ui {\n" +
                    "PROC q() NI = 1_ui {\n" +
                        "OUT(x);\n" +
                    "};\n" +
                    "IF (x<y) THEN\n" +
                        "OUT(x);\n" +
                    "ELSE OUT(y);\n" +
                    "END_IF;\n" +
                "};\n" +
                "OUT(z);"
                ;

        Compilador.compilar(lineasCFuente,true,false);

    }
}
