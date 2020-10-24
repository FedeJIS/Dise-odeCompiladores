import compilador.Compilador;

public class Test {

    public static void main(String[] args) {
        String lineasCFuente =
                "a = 99_ui;";

        Compilador.compilar(lineasCFuente,false,true);

    }
}
