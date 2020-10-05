package analizador_lexico;

import analizador_sintactico.Parser;
import util.CodigoFuente;
import util.FileProcessor;
import util.tabla_simbolos.TablaSimbolos;

import java.util.ArrayList;
import java.util.List;

public class Test_Lexico_consumoTokens {
    public static void main(String[] args) {
//        System.out.print("[x=55;y=55;z=55;]:");
        testGenerico("x=55;y=55;z=55;");
        System.out.println();
//
//        System.out.print("[x = 5]:");
//        testGenerico("x = 5");
//        System.out.println();
//
//        System.out.print("[IF (x != y) THEN]:");
//        testGenerico("IF (x != y) THEN");
//        System.out.println();
//
//        System.out.print("[\"CADENA-MULTI\"]:");
//        testGenerico("\"CADENA-multi\"");
//        System.out.println();
    }

    private static void testGenerico(String lineaFuente) {
        FileProcessor fileProcessor = new FileProcessor();
        TablaSimbolos tablaS = new TablaSimbolos();

        AnalizadorLexico aLexico;
        aLexico = new AnalizadorLexico(inicCodigoFuente(lineaFuente), tablaS);

        Parser parser = new Parser(false,aLexico,tablaS);
        parser.run();
        tablaS.printAll();

    }

    private static CodigoFuente inicCodigoFuente(String fuente) {
        List<String> lineas = new ArrayList<>();
        lineas.add(fuente);
        return new CodigoFuente(lineas);
    }
}
