package analizador_lexico;

import util.CodigoFuente;
import util.FileProcessor;
import util.tabla_simbolos.TablaDeSimbolos;

import java.util.ArrayList;
import java.util.List;

public class Test_Lexico_consumoTokens {
    public static void main(String[] args) {
        System.out.print("[x<y]:");
        testGenerico("x<y");
        System.out.println();

        System.out.print("[x = 5]:");
        testGenerico("x = 5");
        System.out.println();

        System.out.print("[IF (x != y) THEN]:");
        testGenerico("IF (x != y) THEN");
        System.out.println();
    }

    private static void testGenerico(String lineaFuente) {
        FileProcessor fileProcessor = new FileProcessor();
        TablaDeSimbolos tablaS = new TablaDeSimbolos();

        AnalizadorLexico aLexico;
        aLexico = new AnalizadorLexico(fileProcessor, inicCodigoFuente(lineaFuente), tablaS);

        int tokenOriginal = -1;
        while (tokenOriginal != 0){
            tokenOriginal = aLexico.yylex();
            System.out.print(tokenOriginal + " ");
        }

    }

    private static CodigoFuente inicCodigoFuente(String fuente) {
        List<String> lineas = new ArrayList<>();
        lineas.add(fuente);
        return new CodigoFuente(lineas);
    }
}
