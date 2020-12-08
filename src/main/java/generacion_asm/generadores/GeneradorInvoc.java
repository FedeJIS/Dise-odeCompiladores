package generacion_asm.generadores;

import java.util.ArrayList;
import java.util.List;

public class GeneradorInvoc {
    public static List<String> genInstrInvoc(String proc){
        List<String> asm = new ArrayList<>();

        asm.add("CMP _NI_MAX_"+proc+", _NI_ACT_"+proc); //comparar ni.
        asm.add("JAE L_ni_superado"); //salta a label en caso de que se supere el limite.
        asm.add("ADD _NI_ACT_"+proc+" 1"); //si es menor hacer call y sumar 1.
        asm.add("CALL _"+proc);
        return asm;
    }
}
