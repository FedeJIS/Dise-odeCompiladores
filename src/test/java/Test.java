import compilador.Compilador;

public class Test {
    public static void main(String[] args) {
        String lineasCFuente
                =   "IF (a < b) THEN\n" +
                "       OUT(a)\n" +
                "    ELSE\n" +
                "       OUT(a);\n" +
                "    END_IF;";

        Compilador.compilar(lineasCFuente);
    }
}
