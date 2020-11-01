import compilador.Compilador;
import util.FileProcessor;

import java.io.File;

public class Main {

    public static void main(String[] args) {
        if (args.length == 0) throw new IllegalStateException("Se esperaba 1 argumento, pero se recibieron 0.");
        Compilador.compilar(args[0],getPathBase(args[0]));
    }

    public static String getPathBase(String pathArchivo){
        int comienzoExt = pathArchivo.lastIndexOf('.');

        if (comienzoExt > 0) return pathArchivo.substring(0,comienzoExt); //Si tiene extension la quito.
        return pathArchivo;
    }
}
