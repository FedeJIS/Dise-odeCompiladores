package generacion_asm.generadores;

import generacion_asm.GeneradorAssembler;
import generacion_asm.util.InfoReg;
import util.tabla_simbolos.TablaSimbolos;

import java.util.ArrayList;
import java.util.List;

import static generacion_asm.util.UtilsRegistro.*;

public class GeneradorSuma {
    /**
     * Fuente: A + B
     * Polaca: A,B,+
     * opDer = B, opIzq = A
     */
    public static List<String> genInstrAritmSuma(TablaSimbolos tablaS, List<InfoReg> registros,
                                                 String opDer, String opIzq) {
        tiposOperandosValidos(tablaS, registros, opIzq, esRegistro(opIzq), opDer, esRegistro(opDer));

        List<String> asm = new ArrayList<>();

        //Variable & Variable
        if (!esRegistro(opIzq) && !esRegistro(opDer))
            if (tablaS.getTipoEntrada(opDer).equals("DOUBLE"))
                return GeneradorAritmDouble.genInstrAritmDouble(tablaS, "+", opIzq, opDer);
            else {
                int reg = getRegistroLibre(registros); //Obtengo reg libre.
                asm.add("MOV " + getNombreRegistro(reg) + ", " + getPrefijo(tablaS, opIzq) + opIzq);
                asm.add("ADD " + getNombreRegistro(reg) + ", " + getPrefijo(tablaS, opDer) + opDer);
                GeneradorAssembler.agregaElementoPila(getNombreRegistro(reg));
                actualizaReg(registros, reg, true, GeneradorAssembler.getLongitudPila());
            }

        //Reg & Variable
        if (esRegistro(opIzq) && !esRegistro(opDer)) {
            asm.add("ADD " + opIzq + ", " + getPrefijo(tablaS, opDer) + opDer); //opIzq es el registro.
            GeneradorAssembler.agregaElementoPila(opIzq);
        }

        //Reg & Reg
        if (esRegistro(opIzq) && esRegistro(opDer)) {
            asm.add("ADD " + opIzq + ", " + opDer);
            GeneradorAssembler.agregaElementoPila(opIzq);
            marcaRegLiberado(registros, opDer);
        }

        //Variable & Reg
        if (!esRegistro(opIzq) && esRegistro(opDer)) {
            //opDer es el registro. Puedo sumar sobre opDer porque la op es conmutativa.
            asm.add("ADD " + opDer + ", " + getPrefijo(tablaS, opIzq) + opIzq);
            GeneradorAssembler.agregaElementoPila(opDer);
        }
        return asm;
    }
}
