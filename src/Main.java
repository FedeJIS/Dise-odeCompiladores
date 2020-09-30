import analizador_lexico.AnalizadorLexico;
import analizador_sintactico.Parser;
import util.CodigoFuente;
import util.FileProcessor;
import util.tabla_simbolos.TablaDeSimbolos;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

//        run("PROC procedimiento (UINT x) NI = 1 {x = 5;}"); //Anda
//        System.out.println("####################");

        run("x = 177;"); //Anda
        System.out.println("####################");

        run("x = 2 + 5;"); //Anda
        System.out.println("####################");

        run("x = 2 * 5;"); //Anda
        System.out.println("####################");

//        run("IF (5 < 9) THEN x = 19; END_IF"); //Anda
//        System.out.println("####################");

//        run("LOOP\n{x = 5;\ny=10;\n\n\n\n} UNTIL (5 == 5)");
//        System.out.println("####################");


    }

    private static void run(String linea) {
        List<String> lineas = new ArrayList<>();
        lineas.add(linea);

        CodigoFuente cFuente = new CodigoFuente(lineas);

        AnalizadorLexico aLexico = new AnalizadorLexico(new FileProcessor(),cFuente,new TablaDeSimbolos());
        Parser parser = new Parser(true,aLexico);

        parser.run();
    }
}
