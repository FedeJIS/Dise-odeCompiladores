package analizador_lexico.maquina_estados;

import static analizador_lexico.maquina_estados.Test_ME_descartables.testGenerico;

public class Test_ME_comentarios {
    public static void main(String[] args) {
        /*
         * Se deben descartar todos los caracteres del comentario.
         * No debe haber tokens generados (-1).
         */
        testGenerico("%%ESTO SE DESCARTA !#asd67s1´+{ñ´´1;",-1);
        System.out.println();

        /*
         * Se descarta hasta el salto de linea, luego se genera un token id.
         */
        testGenerico("%%ESTO SE DESCARTA !#asd67s1´+{ñ´´1;\ncorta",1);
        System.out.println();
    }
}
