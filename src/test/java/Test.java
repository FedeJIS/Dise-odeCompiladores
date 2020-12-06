import compilador.Compilador;

public class Test {

    public static void main(String[] args) {
        Compilador.compilar("DOUBLE w;" + '\n' +
                "PROC x(VAR DOUBLE w, VAR DOUBLE g, DOUBLE h) NI = 1_ui{" + '\n' +
                        "OUT(5_ui);" + '\n' +
                        "};" + '\n' +
                        "DOUBLE y, z;" + '\n' +
                        "x();"
                ,true,true);
    }
}
