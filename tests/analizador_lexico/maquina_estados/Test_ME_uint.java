package analizador_lexico.maquina_estados;

import static analizador_lexico.maquina_estados.Test_ME_descartables.testGenerico;

public class Test_ME_uint {
    public static void main(String[] args) {
        /*
         * Se genera el uint normal.
         */
        testGenerico("55_ui",7);
        System.out.println();

        /*
         * Se genera el uint normal y se notifica un warning porque el sufijo no esta.
         */
        testGenerico("55",7);
        System.out.println();

        /*
         * Se genera el uint normal y se notifica un warning porque el sufijo esta incompleto.
         */
        testGenerico("55_",7);
        System.out.println();

        testGenerico("55_u",7);
        System.out.println();

        /*
         * UINT mas grande aceptado.
         */
        testGenerico(String.valueOf((int)(Math.pow(2,16)-1)),7);
        System.out.println();

        testGenerico(String.valueOf(65535),7);
        System.out.println();

        /*
         * Se supera el limite para el UINT, no se genera el token.
         */
        testGenerico(String.valueOf((int)(Math.pow(2,16))),-1);
        System.out.println();

        testGenerico(String.valueOf(65536),-1);
        System.out.println();


    }
}
