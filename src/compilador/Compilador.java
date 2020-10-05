package compilador;

import analizador_lexico.AnalizadorLexico;
import analizador_sintactico.Parser;
import util.CodigoFuente;
import util.FileProcessor;
import util.TablaNotificaciones;
import util.tabla_simbolos.TablaSimbolos;

public class Compilador {
    private static final TablaSimbolos tablaS = new TablaSimbolos();

    public static void main(String[] args) {
        String pathCFuente = "codigo_fuente.txt";//args[0];

        Parser parser = init(pathCFuente);
        System.out.println("TOKENS GENERADOS POR EL LEXICO:");
        parser.run();
        System.out.println("################################");

        System.out.println();
        System.out.println("TABLA DE SIMBOLOS FINAL:");
        tablaS.printAll();
        System.out.println("################################");

        System.out.println();
        System.out.println("ERRORES:");
        System.out.println(TablaNotificaciones.getErrores());
//        FileProcessor.escribirArchivo("/errores.txt", TablaNotificaciones.getErrores(),false);
        System.out.println("################################");

        System.out.println();
        System.out.println("WARNINGS:");
        System.out.println(TablaNotificaciones.getWarnings());
//        FileProcessor.escribirArchivo("/warning.txt",TablaNotificaciones.getWarnings(),false);
    }

    private static Parser init(String pathCFuente) {
        CodigoFuente cFuente = new CodigoFuente(FileProcessor.getLineas(pathCFuente));
        AnalizadorLexico aLexico = new AnalizadorLexico(cFuente, tablaS);
        return new Parser(false, aLexico, tablaS);
    }
}
