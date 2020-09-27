package analizador_lexico.maquina_estados;

import analizador_lexico.AnalizadorLexico;

import static analizador_lexico.maquina_estados.Test_ME_descartables.testGenerico;

public class Test_ME_uint {
    public static void main(String[] args) {
        /*
         * Se genera el uint normal.
         */
        testGenerico("55_ui", AnalizadorLexico.T_CTE_UINT);
        System.out.println();

        /*
         * Se genera el uint normal y se notifica un warning porque el sufijo no esta.
         */
        testGenerico("55",AnalizadorLexico.T_CTE_UINT);
        System.out.println();

        /*
         * Se genera el uint normal y se notifica un warning porque el sufijo esta incompleto.
         */
        testGenerico("55_",AnalizadorLexico.T_CTE_UINT);
        System.out.println();

        testGenerico("55_u",AnalizadorLexico.T_CTE_UINT);
        System.out.println();

        /*
         * UINT mas grande aceptado.
         */
        testGenerico(String.valueOf((int)(Math.pow(2,16)-1)),AnalizadorLexico.T_CTE_UINT);
        System.out.println();

        testGenerico(String.valueOf(65535),AnalizadorLexico.T_CTE_UINT);
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
