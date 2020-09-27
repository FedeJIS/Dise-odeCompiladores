package analizador_lexico.maquina_estados;

import analizador_lexico.AnalizadorLexico;

import static analizador_lexico.maquina_estados.Test_ME_descartables.testGenerico;

public class Test_ME_cadenasMultiL {
    public static void main(String[] args) {
        testGenerico("\"CADENA\nMULTI\nLINEA\"", AnalizadorLexico.T_CADENA);
        System.out.println();
    }
}
