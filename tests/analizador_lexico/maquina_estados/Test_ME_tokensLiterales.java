package analizador_lexico.maquina_estados;

import static analizador_lexico.maquina_estados.Test_ME_descartables.testGenerico;

public class Test_ME_tokensLiterales {
    public static void main(String[] args) {
        /*
         * El token del "{" es el 123.
         */
        testGenerico("{",'{');
        System.out.println();

        /*
         * El token del ";" es el 59.
         */
        testGenerico(";",';');
        System.out.println();

        /*
         * El token del "<" es el 60.
         */
        testGenerico("<",'<');
        System.out.println();

        /*
         * El token del "=" es el 61.
         */
        testGenerico("=",'=');
        System.out.println();

        /*
         * El token del ">" es el 62.
         */
        testGenerico(">",'>');
        System.out.println();
    }
}
