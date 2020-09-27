package analizador_lexico.maquina_estados;

import static analizador_lexico.maquina_estados.Test_ME_descartables.testGenerico;

public class Test_ME_pReservadas {
    public static void main(String[] args) {
        /*
         * Se debe generar el token 2
         */
        testGenerico("IF",2);
        testGenerico("THEN",2);
        testGenerico("ELSE",2);
        testGenerico("END_IF",2);
        testGenerico("OUT",2);
        testGenerico("FUNC",2);
        testGenerico("RETURN",2);
        testGenerico("LOOP",2);
        testGenerico("UNTIL",2);
        testGenerico("UINT",2);
        testGenerico("DOUBLE",2);

        /*
         * No se debe generar ningun token. (-1)
         */
        testGenerico("ELS",-1);
        testGenerico("THEn",-1);
        testGenerico("DOUBLEE",-1);
    }
}
