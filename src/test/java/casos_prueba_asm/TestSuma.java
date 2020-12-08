package casos_prueba_asm;

import generacion_asm.generadores.GeneradorSuma;
import generacion_asm.util.UtilsRegistro;
import util.tabla_simbolos.Celda;
import util.tabla_simbolos.TablaSimbolos;

public class TestSuma {
    public static void main(String[] args) {
        TablaSimbolos tablaS = new TablaSimbolos();
        tablaS.agregarEntrada(new Celda(0, "var1", "DOUBLE", Celda.USO_VAR, true));
        tablaS.agregarEntrada(new Celda(0, "@aux1", "DOUBLE", Celda.USO_VAR, true));
        tablaS.agregarEntrada(new Celda(0, "var2", "DOUBLE", Celda.USO_VAR, true));
        tablaS.agregarEntrada(new Celda(0, "-7.2E1", "DOUBLE", Celda.USO_CTE, true));
        tablaS.agregarEntrada(new Celda(0, "5.1E1", "DOUBLE", Celda.USO_CTE, true));
        tablaS.agregarEntrada(new Celda(0, "uint1", "UINT", Celda.USO_VAR, true));
        tablaS.agregarEntrada(new Celda(0, "uint2", "UINT", Celda.USO_VAR, true));
        tablaS.agregarEntrada(new Celda(0, "5", "UINT", Celda.USO_CTE, true));

        for (String instr : GeneradorSuma.genInstrAritmSuma(tablaS, UtilsRegistro.init(), "-7.2E1", "@aux1"))
            System.out.println(instr);
    }
}
