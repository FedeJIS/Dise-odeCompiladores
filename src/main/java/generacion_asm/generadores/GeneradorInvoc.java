package generacion_asm.generadores;

import generacion_asm.GeneradorAssembler;
import generacion_asm.util.InfoReg;
import util.tabla_simbolos.TablaSimbolos;

import java.util.ArrayList;
import java.util.List;

public class GeneradorInvoc {
    public static List<String> genInstrInvoc(TablaSimbolos tablaS, List<InfoReg> registros, String proc){
        GeneradorAssembler.quitaElementoPila(); //Saco el paso del proc.
        List<String> asm = new ArrayList<>();

        if (!tablaS.maxInvocAlcanzadas(proc)){
            //Asignacion de valores a params.
            List<String> paramsReales = tablaS.getParamReales(proc);
            for (int i = 0; i < tablaS.getNParams(proc); i++) {
                GeneradorAssembler.quitaElementoPila(); //Saco los parametros.
                String paramDecl = tablaS.getParam(proc, i);
                String paramReal = paramsReales.get(i);
                asm.addAll(GeneradorAsign.genInstrAsign(tablaS, registros, paramDecl, paramReal));
            }

            asm.add("CALL _"+proc);
        }
        return asm;
    }
}
