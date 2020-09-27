package analizador_lexico.maquina_estados;

import analizador_lexico.AnalizadorLexico;

import static analizador_lexico.maquina_estados.Test_ME_descartables.testGenerico;

public class Test_ME_pReservadas {
    public static void main(String[] args) {
        testGenerico("UINT", AnalizadorLexico.T_UINT);
        testGenerico("DOUBLE",AnalizadorLexico.T_DOUBLE);
        testGenerico("IF",AnalizadorLexico.T_IF);
        testGenerico("THEN",AnalizadorLexico.T_THEN);
        testGenerico("ELSE",AnalizadorLexico.T_ELSE);
        testGenerico("END_IF",AnalizadorLexico.T_END_IF);
        testGenerico("LOOP",AnalizadorLexico.T_LOOP);
        testGenerico("UNTIL",AnalizadorLexico.T_UNTIL);
        testGenerico("OUT",AnalizadorLexico.T_OUT);
        testGenerico("PROC",AnalizadorLexico.T_PROC);
        testGenerico("VAR",AnalizadorLexico.T_VAR);
        testGenerico("NI",AnalizadorLexico.T_NI);

        /*
         * No se debe generar ningun token. (-1)
         */
        testGenerico("ELS",-1);
        testGenerico("THEn",-1);
        testGenerico("DOUBLEE",-1);
    }
}
