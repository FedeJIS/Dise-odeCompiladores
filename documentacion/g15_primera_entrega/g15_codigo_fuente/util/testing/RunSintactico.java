package util.testing;

import analizador_lexico.AnalizadorLexico;
import analizador_sintactico.Parser;
import util.CodigoFuente;
import util.tabla_simbolos.TablaSimbolos;

import java.util.ArrayList;
import java.util.List;

public class RunSintactico {
    private static TablaSimbolos tablaS = new TablaSimbolos();

    public static void run(boolean debug, String linea) {
        List<String> lineas = new ArrayList<>();
        lineas.add(linea);

        CodigoFuente cFuente = new CodigoFuente(lineas);

        AnalizadorLexico aLexico = new AnalizadorLexico(cFuente, tablaS);
        Parser parser = new Parser(debug,aLexico,tablaS);

        parser.run();
    }

    public static TablaSimbolos getTablaS(){
        return tablaS;
    }

    public static void clearTablaS() {
        tablaS = new TablaSimbolos();
    }

    public static void execute(String linea) {
        RunSintactico.clearTablaS();
        RunSintactico.run(false, linea);

        TablaSimbolos tablaS = RunSintactico.getTablaS();

//        tablaS.printAll();

        System.out.println("$$$$$$$$");
    }
}
