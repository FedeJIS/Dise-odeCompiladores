package compilador;

import analizador_lexico.AnalizadorLexico;
import analizador_sintactico.Parser;
import util.CodigoFuente;
import util.TablaNotificaciones;
import util.tabla_simbolos.TablaSimbolos;

public class CompiladorFijo {
    private static final TablaSimbolos tablaS = new TablaSimbolos();

    public static void compilar(String lineasCFuente) {
        Parser parser = init(lineasCFuente);
        System.out.println("TOKENS GENERADOS POR EL LEXICO:");
        parser.run();
        System.out.println();
        System.out.println("###############################################");

        print(tablaS.toString(), "Tabla de simbolos:");
        System.out.println("###############################################");

        print(TablaNotificaciones.getErrores(), "Errores:");
        System.out.println("###############################################");

        print(TablaNotificaciones.getWarnings(), "Warnings:");
        System.out.println("###############################################");
    }

    private static Parser init(String lineasCFuente) {
        CodigoFuente cFuente = new CodigoFuente(lineasCFuente);
        AnalizadorLexico aLexico = new AnalizadorLexico(cFuente, tablaS);
        return new Parser(false, aLexico, tablaS);
    }

    private static void print(String toPrint, String titulo){
        System.out.println(titulo);
        System.out.println(toPrint);
    }
}
