package generacion_asm.generadores;

import generacion_asm.GeneradorAssembler;
import generacion_asm.util.InfoReg;
import util.tabla_simbolos.TablaSimbolos;

import java.util.ArrayList;
import java.util.List;

import static generacion_asm.util.UtilsRegistro.*;

public class GeneradorMult {
    /**
     * Fuente: A * B
     * Polaca: A,B,*
     * opDer = B, opIzq = A
     */
    public static List<String> genInstrAritmMult(TablaSimbolos tablaS, List<InfoReg> registros,
                                                  String opDer, String opIzq) {
        tiposOperandosValidos(tablaS, registros, opIzq, esRegistro(opIzq), opDer, esRegistro(opDer));

        List<String> asm = new ArrayList<>();

        //Variable & Variable
        if (!esRegistro(opIzq) && !esRegistro(opDer))
            if (tablaS.getTipoEntrada(opIzq).equals("DOUBLE"))
                return GeneradorAritmDouble.genInstrAritmDouble(tablaS, "*", opIzq, opDer);
            else {
                //Se encarga de liberar AX, y guardar su contenido previo si corresponde.
                asm.addAll(liberaRegistro(registros, AX));
                asm.add("MOV AX, " + getPrefijo(tablaS, opIzq) + opIzq);
                asm.add("MUL " + getPrefijo(tablaS, opDer) + opDer);
            }

        //Reg & Variable. Reg izq tiene que ser AX.
        if (esRegistro(opIzq) && !esRegistro(opDer))
            if (opIzq.equals("AX")) asm.add("MUL " + getPrefijo(tablaS, opDer) + opDer);
            else { //Tengo que mover el opIzq a AX.
                asm.addAll(liberaRegistro(registros, AX));
                asm.add("MOV AX, " + opIzq);
                asm.add("MUL " + getPrefijo(tablaS, opDer) + opDer);

                actualizaReg(registros, getIdRegistro(opIzq), false, -1);
            }

        //Reg & Reg. Reg izq tiene que ser AX.
        if (esRegistro(opIzq) && esRegistro(opDer)) {
            if (opIzq.equals("AX")) asm.add("MUL " + opDer);
            else {
                asm.addAll(liberaRegistro(registros, AX));
                asm.add("MOV AX, " + opIzq);
                asm.add("MUL " + opDer);

                actualizaReg(registros, getIdRegistro(opIzq), false, -1);
            }
            actualizaReg(registros, getIdRegistro(opDer), false, -1);
        }

        //Variable & Reg. Reg der tiene que ser AX.
        if (!esRegistro(opIzq) && esRegistro(opDer)) {
            if (opDer.equals("AX"))
                asm.add("MUL " + getPrefijo(tablaS, opIzq) + opIzq); //Puedo invertir ops por prop conmut.
            else {
                asm.addAll(liberaRegistro(registros, AX));
                asm.add("MOV AX, " + opDer);
                asm.add("MUL " + getPrefijo(tablaS, opIzq) + opIzq);
            }
            actualizaReg(registros, getIdRegistro(opDer), false, -1);
        }

        GeneradorAssembler.agregaElementoPila("AX");
        actualizaReg(registros, AX, true, GeneradorAssembler.getLongitudPila());
        return asm;
    }
}
