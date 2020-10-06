package analizador_lexico.maquina_estados;

import analizador_lexico.AnalizadorLexico;
import util.tabla_simbolos.TablaSimbolos;

import static analizador_lexico.maquina_estados.Test_ME_descartables.testGenerico;

public class Test_ME_uint {
    public static void main(String[] args) {
        TablaSimbolos tablaS;
        /*
         * Se genera el uint normal.
         */
        tablaS = testGenerico("55_ui", AnalizadorLexico.T_CTE_UINT);
        tablaS.toString();
        System.out.println("Lexema encontrado:"+tablaS.getValor("55").getLexema());
        System.out.println();

        /*
         * Se genera el uint normal y se notifica un warning porque el sufijo no esta.
         */
        tablaS = testGenerico("56",AnalizadorLexico.T_CTE_UINT);
        tablaS.toString();
        System.out.println("Lexema encontrado:"+tablaS.getValor("56").getLexema());
        System.out.println();

        /*
         * Se genera el uint normal y se notifica un warning porque el sufijo esta incompleto.
         */
        tablaS = testGenerico("57_",AnalizadorLexico.T_CTE_UINT);
        tablaS.toString();
        System.out.println("Lexema encontrado:"+tablaS.getValor("57").getLexema());
        System.out.println();

        tablaS = testGenerico("58_u",AnalizadorLexico.T_CTE_UINT);
        tablaS.toString();
        System.out.println("Lexema encontrado:"+tablaS.getValor("58").getLexema());
        System.out.println();

        /*
         * Maximo UINT aceptado.
         */
        int maximo = (int)(Math.pow(2,16)-1);

        tablaS = testGenerico(maximo+"_ui", AnalizadorLexico.T_CTE_UINT);
        tablaS.toString();
        System.out.println("Lexema encontrado:"+tablaS.getValor(String.valueOf(maximo)).getLexema());
        System.out.println();

        tablaS = testGenerico(String.valueOf(maximo),AnalizadorLexico.T_CTE_UINT);
        tablaS.toString();
        System.out.println("Lexema encontrado:"+tablaS.getValor(String.valueOf(maximo)).getLexema());
        System.out.println();

        /*
         * Se supera el limite para el UINT, no se genera el token.
         */
        tablaS = testGenerico(String.valueOf(maximo+1),-1);
        tablaS.toString();
        System.out.println();
    }
}
