package generacion_asm.generadores;

import generacion_asm.util.InfoReg;
import util.tabla_simbolos.Celda;
import util.tabla_simbolos.TablaSimbolos;

import java.util.ArrayList;
import java.util.List;

public class GeneradorInvoc {
    public static List<String> genInstrInvoc(TablaSimbolos tablaS, List<InfoReg> registros, String proc){
        List<String> asm = new ArrayList<>();

        if (!tablaS.maxInvocAlcanzadas(proc)){
            //Asignacion de valores a params.
            List<String> paramsReales = tablaS.getParamReales(proc);
            for (int i = 0; i < tablaS.getNParams(proc); i++) {
                String paramDecl = tablaS.getParam(proc, i);
                String paramReal = paramsReales.get(i);

                asm.addAll(GeneradorAsign.genInstrAsign(tablaS, registros, paramDecl, paramReal));
            }
            asm.add("CMP _NI_MAX_"+proc+", _NI_ACT_"+proc); //comparar ni.
            asm.add("JAE L_ni_superado"); //salta a label en caso de que se supere el limite.


            asm.add("ADD _NI_ACT_"+proc+", 1"); //si es menor hacer call y sumar 1.
            asm.add("CALL _"+proc);
        }
        return asm;
    }
}
