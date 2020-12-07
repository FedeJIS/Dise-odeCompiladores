import compilador.Compilador;

public class Test {

    public static void main(String[] args) {
        Compilador.compilar(
                "DOUBLE a;" + '\n' +
                "PROC x(DOUBLE e) NI = 1_ui{" + '\n' +
                    "IF(e < 10.) THEN" + '\n' +
                        "x(e);" + '\n' +
                    "END_IF;" + '\n' +
                    "OUT(\"en x\");" + '\n' +
                "};" + '\n' +
                "a = 15.;" + '\n' +
                "x(a);"
                ,true,true);
    }
}
