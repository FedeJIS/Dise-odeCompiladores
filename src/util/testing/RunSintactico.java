package util.testing;

import analizador_lexico.AnalizadorLexico;
import analizador_sintactico.Parser;
import util.CodigoFuente;
import util.FileProcessor;
import util.tabla_simbolos.TablaDeSimbolos;

import java.util.ArrayList;
import java.util.List;

public class RunSintactico {
    private static TablaDeSimbolos tablaS = new TablaDeSimbolos();

    public static void run(boolean debug, String linea) {
        List<String> lineas = new ArrayList<>();
        lineas.add(linea);

        CodigoFuente cFuente = new CodigoFuente(lineas);

        AnalizadorLexico aLexico = new AnalizadorLexico(new FileProcessor(),cFuente,tablaS);
        Parser parser = new Parser(debug,aLexico,tablaS);

        parser.run();
    }

    public static TablaDeSimbolos getTablaS(){
        return tablaS;
    }

    public static void clearTablaS() {
        tablaS = new TablaDeSimbolos();
    }

    public static void execute(String linea) {
        RunSintactico.clearTablaS();
        RunSintactico.run(false, linea);

        TablaDeSimbolos tablaS = RunSintactico.getTablaS();

        tablaS.printAll();

        System.out.println("$$$$$$$$");
    }
}
