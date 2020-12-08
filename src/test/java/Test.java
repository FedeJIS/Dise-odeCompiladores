import compilador.Compilador;

public class Test {

    public static void main(String[] args) {
        Main.main(new String[]{("/home/brunog/Desktop/fuente.txt")});

//        Compilador.compilar(
//                "PROC x(VAR DOUBLE f, DOUBLE g, DOUBLE h) NI = 1_ui{" + '\n' +
//                        "OUT(5_ui);" + '\n' +
//                        "};" + '\n' +
//                        "DOUBLE w, y, z;" + '\n' +
//                        "x(y,7.2d11, z);" + '\n' +
//                        "x(w,7.2d11, w);"
//                ,true,true);
    }
}
