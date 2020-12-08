package generacion_asm.generadores;

import util.tabla_simbolos.TablaSimbolos;


import static generacion_asm.util.UtilsRegistro.getPrefijo;

public class GeneradorOut {
    public static String generaInstrOut(TablaSimbolos tablaS, String tipoOut, String op){
        if (tipoOut.equals("OUT_UINT"))
            return "invoke printf, cfm$(\"%i\\n\"), "+getPrefijo(tablaS, op)+op+'\n';
        if (tipoOut.equals("OUT_DOUBLE"))
            return "invoke printf, cfm$(\"%f\\n\"), "+getPrefijo(tablaS, op)+TablaSimbolos.formatDouble(op)+'\n';

        op = "_CAD_"+op.substring(1, op.length()-1).replace(' ', '_')+" "; //Agrega esp al final.
        return "invoke printf, cfm$(\"%s\\n\"), OFFSET "+op + '\n';
    }
}
