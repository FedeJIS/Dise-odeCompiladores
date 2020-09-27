package analizador_lexico.maquina_estados;

import static analizador_lexico.maquina_estados.Test_ME_descartables.testGenerico;

public class Test_ME_tokensLiterales {
    public static void main(String[] args) {
        /*
         * El token del "{" es el 123.
         */
        testGenerico("{",123);
        System.out.println();

        /*
         * El token del ";" es el 59.
         */
        testGenerico(";",59);
        System.out.println();

        /*
         * El token del "<" es el 60.
         */
        testGenerico("<",60);
        System.out.println();

        /*
         * El token del "=" es el 61.
         */
        testGenerico("=",61);
        System.out.println();

        /*
         * El token del ">" es el 62.
         */
        testGenerico(">",62);
        System.out.println();
    }
}
