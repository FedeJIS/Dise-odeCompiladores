package generacion_asm.generadores;

import generacion_asm.util.InfoReg;
import util.tabla_simbolos.TablaSimbolos;

import java.util.ArrayList;
import java.util.List;

import static generacion_asm.util.UtilsRegistro.*;

public class GeneradorAsign {
    public static List<String> genInstrAsign(TablaSimbolos tablaS, List<InfoReg> registros, String dest, String src){
        List<String> asm = new ArrayList<>();

        tiposOperandosValidos(tablaS, registros, dest, esRegistro(dest), src, esRegistro(src));

        //dest = Variable & src = Reg. Asigna directamente sobre la variable.
        if (!esRegistro(dest) && esRegistro(src)) {
            asm.add("MOV " + getPrefijo(tablaS, dest) + dest + ", " + src);
            marcaRegLiberado(registros, src);
        }

        //Variable & Variable
        if (!esRegistro(dest) && !esRegistro(src)) {
            if (tablaS.getTipoEntrada(dest).equals("DOUBLE")) { //Es un double
                if (tablaS.esEntradaCte(src)) src = "_" + TablaSimbolos.formatDouble(src);
                else src = getPrefijo(tablaS, src) + src;

                asm.add("FLD " + src);
                asm.add("FSTP " + getPrefijo(tablaS, dest) + dest);

                return asm;
            }
            int idReg = getRegistroLibre(registros);
            asm.add("MOV " + getNombreRegistro(idReg) + ", " + getPrefijo(tablaS, src) + src);
            asm.add("MOV _" + dest + ", " + getNombreRegistro(idReg));
        }

        return asm;
    }
}
