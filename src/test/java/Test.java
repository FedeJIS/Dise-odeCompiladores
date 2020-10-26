import compilador.Compilador;

public class Test {

    public static void main(String[] args) {
        Compilador.compilar(testVariableNoDecl(),false,true);
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

    /**
     * FUNCIONA.
     */
    private static String testCargaDatosTS(){
        return
                "PROC x(VAR UINT v1, DOUBLE v2, UINT v3) NI = 5_ui {\n" +
                    "UINT b;\n" +
                    "b = 5_ui;\n" +
                "};\n" +
                "y = 5_ui;"
                ;
    }

    /**
     * FUNCIONA
     */
    private static String testVariableRedecl(){
        return
                "UINT a; \n" +
                "PROC x(VAR UINT v4, DOUBLE v2, UINT v3) NI = 5_ui {\n" +
                    "UINT v4;\n" +
                    "UINT a;\n" +
                "};\n" +
                "y = 5_ui;"
                ;
    }

    private static String testVariableNoDecl(){
        return
//                "UINT a ; \n" +
//                "PROC x(VAR UINT b, DOUBLE c, UINT d) NI = 5_ui {\n" +
////                    "x = 3_ui;\n" +
//                    "a = 5.0d1;\n" +
//                "};"
                "PROC x() NI = 1_ui{\n" +
                    "OUT(5_ui);\n" +
                "};" +
                "PROC y() NI = 1_ui{\n" +
                    "x();\n" +
                "};"
                ;
    }
}
