package analizador_lexico.maquina_estados;

import analizador_lexico.AnalizadorLexico;
import util.tabla_simbolos.TablaDeSimbolos;

import static analizador_lexico.maquina_estados.Test_ME_descartables.testGenerico;

public class Test_ME_double {
    public static void main(String[] args) {
        TablaDeSimbolos tablaS;

        tablaS = testGenerico("1.", AnalizadorLexico.T_CTE_DOUBLE);
        tablaS.printAll();
        System.out.println();

        tablaS = testGenerico(".6",AnalizadorLexico.T_CTE_DOUBLE);
        tablaS.printAll();
        System.out.println();

        tablaS = testGenerico("1.2",AnalizadorLexico.T_CTE_DOUBLE);
        tablaS.printAll();
        System.out.println();

        tablaS = testGenerico("3.d5",AnalizadorLexico.T_CTE_DOUBLE);
        tablaS.printAll();
        System.out.println();
//        System.out.println("Lexema encontrado:"+tablaS.getValor(String.valueOf(3.0*Math.pow(10,5))).getLexema());

        tablaS = testGenerico("2.5d2",AnalizadorLexico.T_CTE_DOUBLE);
        tablaS.printAll();
        System.out.println();
//        System.out.println("Lexema encontrado:"+tablaS.getValor("55").getLexema());

        tablaS = testGenerico("0.",AnalizadorLexico.T_CTE_DOUBLE);
        tablaS.printAll();
        System.out.println();
//        System.out.println("Lexema encontrado:"+tablaS.getValor("55").getLexema());

        tablaS = testGenerico(".0",AnalizadorLexico.T_CTE_DOUBLE);
        tablaS.printAll();
        System.out.println();
//        System.out.println("Lexema encontrado:"+tablaS.getValor("55").getLexema());
    }
}
