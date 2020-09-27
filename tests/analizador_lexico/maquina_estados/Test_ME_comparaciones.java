package analizador_lexico.maquina_estados;

import static analizador_lexico.maquina_estados.Test_ME_descartables.testGenerico;

public class Test_ME_comparaciones {
    public static void main(String[] args) {
        /*
         * Se debe generar el token 3.
         */
        testGenerico("<=",3);
        System.out.println();

        /*
         * Se debe generar el token 4.
         */
        testGenerico(">=",4);
        System.out.println();

        /*
         * Se debe generar el token 5.
         */
        testGenerico("!=",5);
        System.out.println();

        /*
         * Se debe generar el token 6.
         */
        testGenerico("==",6);
        System.out.println();
    }
}
