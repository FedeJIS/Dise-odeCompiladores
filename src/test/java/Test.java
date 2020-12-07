import compilador.Compilador;

public class Test {

    public static void main(String[] args) {
        Compilador.compilar(
                "DOUBLE b; UINT x;" + '\n' +
                            "b = -5.0d1 * -b;"
                ,true,true);
    }
}
