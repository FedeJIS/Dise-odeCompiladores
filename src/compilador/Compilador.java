package compilador;

import analizador_lexico.AnalizadorLexico;
import analizador_sintactico.Parser;
import util.CodigoFuente;
import util.FileProcessor;
import util.tabla_simbolos.TablaSimbolos;

public class Compilador {
    private static final TablaSimbolos tablaS = new TablaSimbolos();

    public static void main(String[] args) {
        String pathCFuente = "codigo_fuente.txt"; //args[0];

        Parser parser = init(pathCFuente);
        parser.run();

        tablaS.printAll();
    }

    private static Parser init(String pathCFuente) {
        CodigoFuente cFuente = new CodigoFuente(FileProcessor.getLineas(pathCFuente));
        AnalizadorLexico aLexico = new AnalizadorLexico(cFuente, tablaS);
        return new Parser(false, aLexico, tablaS);
    }
}
