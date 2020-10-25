import compilador.Compilador;

public class Test {

    public static void main(String[] args) {
        String lineasCFuente =
                "PROC p() NI = 1_ui {\n" +
                    "OUT(x);" +
                    "PROC q() NI = 1_ui {\n" +
                        "IF (x==y) THEN OUT(x);END_IF;\n" +
                        "LOOP OUT(y);\n" +
                        "UNTIL (y==10_ui);\n" +
                    "};\n" +
                    "IF (x<y) THEN\n" +
                        "OUT(x);\n" +
                    "ELSE OUT(y);\n" +
                    "END_IF;\n" +
                "};\n" +
                "OUT(z);";

        Compilador.compilar(lineasCFuente,true,false);
    }
}
