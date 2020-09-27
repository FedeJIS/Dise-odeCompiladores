package analizador_lexico.maquina_estados;

import analizador_lexico.AnalizadorLexico;
import util.tabla_simbolos.TablaDeSimbolos;

import static analizador_lexico.maquina_estados.Test_ME_descartables.testGenerico;

public class Test_ME_ids {
    public static void main(String[] args) {
        /*
         * Identificadores: se debe generar el token 1.
         */
        String lexema = "a";
        test(lexema,lexema);
        System.out.println();

        lexema = "a_";
        test(lexema,lexema);
        System.out.println();

        lexema = "a_9";
        test(lexema,lexema);
        System.out.println();

        lexema = "a9";
        test(lexema,lexema);
        System.out.println();

        lexema = "a9_";
        test(lexema,lexema);
        System.out.println();

        /* Se debe descartar lo que no sea parte del id */
        lexema = "a9_";
        test(lexema,lexema+"W");
        System.out.println();

        lexema = "a9_";
        test(lexema,lexema+";;;;");
        System.out.println();
    }

    private static void test(String lexema, String linea) {
        TablaDeSimbolos tablaS;
        tablaS = testGenerico(linea, AnalizadorLexico.T_ID);
        try{
            tablaS.getValor(lexema);
            System.out.println("Lexema '"+ lexema +"' encontrado en la TS.");
        } catch (IllegalStateException illegalStateException){
            System.out.println("Lexema '"+ lexema +"' NO encontrado en la TS.");
        }
    }
}
