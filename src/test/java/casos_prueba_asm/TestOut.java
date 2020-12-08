package casos_prueba_asm;

import generacion_asm.generadores.GeneradorComp;
import generacion_asm.generadores.GeneradorOut;
import generacion_asm.util.UtilsRegistro;
import util.tabla_simbolos.Celda;
import util.tabla_simbolos.TablaSimbolos;

public class TestOut {
    public static void main(String[] args) {
        TablaSimbolos tablaS = new TablaSimbolos();
        tablaS.agregarEntrada(new Celda(0, "var1", "DOUBLE", Celda.USO_VAR, true));
        tablaS.agregarEntrada(new Celda(0, "@aux1", "DOUBLE", Celda.USO_VAR, true));
        tablaS.agregarEntrada(new Celda(0, "-7.2E1", "DOUBLE", Celda.USO_CTE, true));
        tablaS.agregarEntrada(new Celda(0, "uint1", "UINT", Celda.USO_VAR, true));
        tablaS.agregarEntrada(new Celda(0, "5", "UINT", Celda.USO_CTE, true));
        tablaS.agregarEntrada(new Celda(0, "\"CADENA DE TEXTO\"", "", "", true));

        System.out.println(tablaS.toAsm());

        System.out.println(GeneradorOut.generaInstrOut(tablaS, "OUT_DOUBLE", "var1"));
        System.out.println(GeneradorOut.generaInstrOut(tablaS, "OUT_DOUBLE", "@aux1"));
        System.out.println(GeneradorOut.generaInstrOut(tablaS, "OUT_DOUBLE", "-7.2E1"));
        System.out.println(GeneradorOut.generaInstrOut(tablaS, "OUT_UINT", "uint1"));
        System.out.println(GeneradorOut.generaInstrOut(tablaS, "OUT_UINT", "5"));
        System.out.println(GeneradorOut.generaInstrOut(tablaS, "OUT_CAD", "\"CADENA DE TEXTO\""));
    }
}
