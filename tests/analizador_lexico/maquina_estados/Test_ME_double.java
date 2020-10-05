package analizador_lexico.maquina_estados;

import analizador_lexico.AnalizadorLexico;
import util.tabla_simbolos.TablaSimbolos;

import static analizador_lexico.maquina_estados.Test_ME_descartables.testGenerico;

public class Test_ME_double {
    public static void main(String[] args) {
        TablaSimbolos tablaS;

        tablaS = testGenerico("1.", AnalizadorLexico.T_CTE_DOUBLE);
        tablaS.printAll();
        System.out.println();

        tablaS = testGenerico(".6",AnalizadorLexico.T_CTE_DOUBLE);
        tablaS.printAll();
        System.out.println();

        tablaS = testGenerico("1.2",AnalizadorLexico.T_CTE_DOUBLE);
        tablaS.printAll();
        System.out.println();

        tablaS = testGenerico("3.d+5",AnalizadorLexico.T_CTE_DOUBLE);
        tablaS.printAll();
        System.out.println();

        tablaS = testGenerico("2.5d2",AnalizadorLexico.T_CTE_DOUBLE); //Le falta el '+'. Genera un double sin exponente.
        tablaS.printAll();
        System.out.println();

        tablaS = testGenerico("0.",AnalizadorLexico.T_CTE_DOUBLE);
        tablaS.printAll();
        System.out.println();

        tablaS = testGenerico(".0",AnalizadorLexico.T_CTE_DOUBLE);
        tablaS.printAll();
        System.out.println();

        tablaS = testGenerico("5.0d-1",AnalizadorLexico.T_CTE_DOUBLE);
        tablaS.printAll();
        System.out.println();

        tablaS = testGenerico("5.0d-",AnalizadorLexico.T_CTE_DOUBLE);
        tablaS.printAll();
        System.out.println();

        tablaS = testGenerico("5.0d+",AnalizadorLexico.T_CTE_DOUBLE);
        tablaS.printAll();
        System.out.println();
    }
}
