import compilador.Compilador;

public class Test {

    public static void main(String[] args) {
        Compilador.compilar(
                //Declaracion proc valida.
                "PROC x(VAR DOUBLE f, VAR DOUBLE g, DOUBLE h, DOUBLE mm) NI = 1_ui{" + '\n' +
                        "OUT(5_ui);" + '\n' +
                        "};" + '\n' +
                        "DOUBLE w, y, z;" + '\n' +
                        "x(w,y,7.2d11, z);"
                ,true,true);
    }
}
