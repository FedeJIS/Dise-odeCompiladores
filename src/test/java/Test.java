import compilador.Compilador;

public class Test {

    public static void main(String[] args) {
        Compilador.compilar(
                "PROC x(DOUBLE f, DOUBLE g, DOUBLE h) NI = 1_ui{" + '\n' +
                                "OUT(5_ui);" + '\n' +
                            "};"
                ,true,true);
    }
}
