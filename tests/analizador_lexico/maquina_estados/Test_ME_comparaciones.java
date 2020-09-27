package analizador_lexico.maquina_estados;

import analizador_lexico.AnalizadorLexico;

import static analizador_lexico.maquina_estados.Test_ME_descartables.testGenerico;

public class Test_ME_comparaciones {
    public static void main(String[] args) {
        /*
         * Se debe generar el token 3.
         */
        testGenerico("<=", AnalizadorLexico.T_COMP_MENOR_IGUAL);
        System.out.println();

        /*
         * Se debe generar el token 4.
         */
        testGenerico(">=",AnalizadorLexico.T_COMP_MAYOR_IGUAL);
        System.out.println();

        /*
         * Se debe generar el token 5.
         */
        testGenerico("!=",AnalizadorLexico.T_COMP_DISTINTO);
        System.out.println();

        /*
         * Se debe generar el token 6.
         */
        testGenerico("==",AnalizadorLexico.T_COMP_IGUAL);
        System.out.println();
    }
}
