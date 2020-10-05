package analizador_lexico.maquina_estados;

import analizador_lexico.AnalizadorLexico;
import util.CodigoFuente;
import util.FileProcessor;
import util.tabla_simbolos.TablaSimbolos;

import java.util.ArrayList;
import java.util.List;

public class Test_ME_descartables {
    public static void main(String[] args){

        /*
         * Se deben descartar todos los espacios.
         * No debe haber tokens generados (-1).
         */
        testGenerico("     ",-1);
        System.out.println();

        /*
         * Se deben descartar todas las tabulaciones.
         * No debe haber tokens generados (-1).
         */
        testGenerico("\t\t\t\t\t\t",-1);

        /*
         * Se deben descartar todos los saltos de linea.
         * No debe haber tokens generados (-1).
         */
        testGenerico("\n\n\n\n\n\n",-1);
    }

    public static TablaSimbolos testGenerico(String lineaFuente, int tokenEsperado) {
        TablaSimbolos tablaS = new TablaSimbolos();

        AnalizadorLexico aLexico;
        aLexico = new AnalizadorLexico(inicCodigoFuente(lineaFuente), tablaS);
        int tokenOriginal = aLexico.produceToken();

        System.out.println("Token generado:" + tokenOriginal + ". Esperado:" + tokenEsperado + ". Exito:" + (tokenOriginal == tokenEsperado));

        return tablaS;
    }

    private static CodigoFuente inicCodigoFuente(String fuente) {
        List<String> lineas = new ArrayList<>();
        lineas.add(fuente);
        return new CodigoFuente(lineas);
    }
}
