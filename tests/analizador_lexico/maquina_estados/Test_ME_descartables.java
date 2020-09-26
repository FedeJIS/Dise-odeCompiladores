package analizador_lexico.maquina_estados;

import analizador_lexico.AnalizadorLexico;
import util.CodigoFuente;
import util.FileProcessor;
import util.tabla_simbolos.TablaDeSimbolos;

import java.util.ArrayList;
import java.util.List;

public class Test_ME_descartables {
    public static void main(String[] args){
        FileProcessor fileProcessor = new FileProcessor();
        TablaDeSimbolos tablaS = new TablaDeSimbolos();

        /*
         * Se deben descartar todos los espacios.
         * No debe haber tokens generados (-1).
         */
        AnalizadorLexico aLexico = new AnalizadorLexico(fileProcessor,inicCodigoFuente("     "),tablaS);
        int token = aLexico.produceToken();
        System.out.println("Token generado:"+token);
        System.out.println("#######################################");

        /*
         * Se deben descartar todas las tabulaciones.
         * No debe haber tokens generados (-1).
         */
        aLexico = new AnalizadorLexico(fileProcessor,inicCodigoFuente("\t\t\t\t\t\t"),tablaS);
        token = aLexico.produceToken();
        System.out.println("Token generado:"+token);
        System.out.println("#######################################");

        /*
         * Se deben descartar todos los saltos de linea.
         * No debe haber tokens generados (-1).
         */
        aLexico = new AnalizadorLexico(fileProcessor,inicCodigoFuente("\n\n\n\n\n\n"),tablaS);
        token = aLexico.produceToken();
        System.out.println("Token generado:"+token);
        System.out.println("#######################################");
    }

    public static CodigoFuente inicCodigoFuente(String fuente) {
        List<String> lineas = new ArrayList<>();
        lineas.add(fuente);
        return new CodigoFuente(lineas);
    }
}
