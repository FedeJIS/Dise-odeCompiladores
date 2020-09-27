package analizador_lexico.maquina_estados;

import analizador_lexico.AnalizadorLexico;

import static analizador_lexico.maquina_estados.Test_ME_descartables.testGenerico;

public class Test_ME_double {
    public static void main(String[] args) {
        testGenerico("1.", AnalizadorLexico.T_CTE_DOUBLE);
        System.out.println();

        testGenerico(".6",AnalizadorLexico.T_CTE_DOUBLE);
        System.out.println();

        testGenerico("1.2",AnalizadorLexico.T_CTE_DOUBLE);
        System.out.println();

        testGenerico("3.d5",AnalizadorLexico.T_CTE_DOUBLE);
        System.out.println();

        testGenerico("2.5d2",AnalizadorLexico.T_CTE_DOUBLE);
        System.out.println();

        testGenerico("0.",AnalizadorLexico.T_CTE_DOUBLE);
        System.out.println();

        testGenerico(".0",AnalizadorLexico.T_CTE_DOUBLE);
        System.out.println();
    }
}
