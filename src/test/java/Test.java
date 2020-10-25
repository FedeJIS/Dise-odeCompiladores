import compilador.Compilador;

public class Test {

    public static void main(String[] args) {
        Compilador.compilar(testDeclarativaDentroEjecutable(),true,true);
    }

    /**
     * FUNCIONA
     */
    private static String testGeneracionPolacaProcs(){
        return
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
    }

    /**
     * FUNCIONA
     */
    private static String testDeclarativaDentroEjecutable(){
        return
            "IF (x==y) THEN\n" + //1
                "PROC p() NI = 1_ui{\n" + //2
                    "OUT(x);\n" + //3
                "};\n" + //4
            "END_IF;\n" + //5

            "IF (x==y) THEN{\n" + //6
                "PROC p() NI = 1_ui{\n" + //7
                    "OUT(x);\n" + //8
                "};\n" + //9
            "} END_IF;\n" +//10

            "IF (x==y) THEN{\n" + //11
                "OUT(x);\n" + //12
                "PROC p() NI = 1_ui{\n" + //13
                    "OUT(x);\n" + //14
                "};\n" + //15
            "} END_IF;\n" //16

            ;
    }
}
