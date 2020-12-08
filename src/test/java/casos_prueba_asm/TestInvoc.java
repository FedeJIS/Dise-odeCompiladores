package casos_prueba_asm;

import generacion_asm.GeneradorAssembler;
import generacion_asm.generadores.GeneradorInvoc;
import generacion_asm.generadores.GeneradorMult;
import generacion_asm.util.InfoReg;
import generacion_asm.util.UtilsRegistro;
import util.tabla_simbolos.Celda;
import util.tabla_simbolos.TablaSimbolos;

import java.util.List;

import static generacion_asm.util.UtilsRegistro.AX;

public class TestInvoc {
    public static void main(String[] args) {
        TablaSimbolos tablaS = new TablaSimbolos();
        tablaS.agregarEntrada(new Celda(0, "proc", "-", Celda.USO_PROC, true));
        tablaS.setMaxInvoc("proc", 3);

        for (String instr : GeneradorInvoc.genInstrInvoc(tablaS, UtilsRegistro.init(), "proc"))
            System.out.println(instr);
    }
}
