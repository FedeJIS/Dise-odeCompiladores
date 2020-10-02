package util;

import analizador_lexico.AnalizadorLexico;
import analizador_sintactico.Parser;
import util.tabla_simbolos.TablaDeSimbolos;

import java.util.ArrayList;
import java.util.List;

public class RunSintactico {
    public static void run(boolean debug, String linea) {
        List<String> lineas = new ArrayList<>();
        lineas.add(linea);

        CodigoFuente cFuente = new CodigoFuente(lineas);

        AnalizadorLexico aLexico = new AnalizadorLexico(new FileProcessor(),cFuente,new TablaDeSimbolos());
        Parser parser = new Parser(debug,aLexico);

        parser.run();
    }
}
