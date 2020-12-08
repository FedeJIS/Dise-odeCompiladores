package generacion_asm.generadores;

import generacion_asm.GeneradorAssembler;
import generacion_asm.util.InfoReg;
import util.tabla_simbolos.TablaSimbolos;

import java.util.ArrayList;
import java.util.List;

import static generacion_asm.util.UtilsRegistro.*;

public class GeneradorDiv {
    /**
     * Fuente: A / B
     * Polaca: A,B,/
     * divs = B, divd = A
     */
    public static List<String> genInstrAritmDiv(TablaSimbolos tablaS, List<InfoReg> registros, String divs, String divd) {
        tiposOperandosValidos(tablaS, registros, divd, esRegistro(divd), divs, esRegistro(divs));

        List<String> asm = new ArrayList<>();

        //Variable & Variable
        if (!esRegistro(divd) && !esRegistro(divs))
            if (tablaS.getTipoEntrada(divd).equals("DOUBLE"))
                return GeneradorAritmDouble.genInstrAritmDouble(tablaS, "/", divd, divs);
            else {
                asm.addAll(liberaRegistro(registros, AX)); //Libera AX, y guarda su contenido previo si corresponde.
                asm.add("MOV AX, " + getPrefijo(tablaS, divd) + divd); //Muevo divd a AX.
                actualizaReg(registros, AX, true, -1); //Ocupo AX.

                asm.addAll(liberaRegistro(registros, DX)); //Libera DX, y guarda su contenido previo si corresponde.
                asm.add("MOV DX, 0"); //Seteo DX en 0.
                actualizaReg(registros, DX, true, -1); //Ocupo DX.

                if (tablaS.esEntradaCte(divs)) {
                    //Muevo divs a memoria pq el divs no puede ser un inmediato.
                    asm.add("MOV @aux" + GeneradorAssembler.getVariableAux() + ", " + divs);
                    asm.add("DIV @aux" + GeneradorAssembler.getVariableAux()); //Hago la division.
                    GeneradorAssembler.incrementaVarAux();
                } else asm.add("DIV " + getPrefijo(tablaS, divs) + divs); //Hago la division.
            }

        //Reg & Variable. Reg destino tiene que ser AX.
        if (esRegistro(divd) && !esRegistro(divs)) {
            if (!divd.equals("AX")) { //Tengo que mover el divd a AX.
                asm.addAll(liberaRegistro(registros, AX)); //Libera AX, y guarda su contenido previo si corresponde.
                asm.add("MOV AX, " + divd); //Muevo divd a AX.
                actualizaReg(registros, AX, true, -1); //Ocupo AX.
            }
            asm.addAll(liberaRegistro(registros, DX));
            asm.add("MOV DX, 0"); //Seteo DX en 0.
            actualizaReg(registros, DX, true, -1); //Ocupo DX.

            if (tablaS.esEntradaCte(divs)) {
                asm.add("MOV @aux" + GeneradorAssembler.getVariableAux() + ", " + divs); //Muevo divs a memoria pq el divs no puede ser un inmediato.
                asm.add("DIV @aux" + GeneradorAssembler.getVariableAux()); //Hago la division.
                GeneradorAssembler.incrementaVarAux();
            } else asm.add("DIV _" + divs); //Hago la division.
        }

        //Reg & Reg. Reg destino tiene que ser AX.
        if (esRegistro(divd) && esRegistro(divs)) {
            if (!divd.equals("AX")) { //Tengo que mover el divd a AX.
                asm.addAll(liberaRegistro(registros, AX));
                asm.add("MOV AX, " + divd); //Muevo divd a AX.
                actualizaReg(registros, AX, true, -1); //Ocupo AX.
            }

            asm.addAll(liberaRegistro(registros, DX));
            asm.add("MOV DX, 0"); //Seteo DX en 0.
            actualizaReg(registros, DX, true, -1); //Ocupo DX.

            asm.add("DIV " + divs); //Hago la division.
        }

        //Variable & Reg. Reg destino tiene que ser AX.
        if (!esRegistro(divd) && esRegistro(divs))
            if (!divd.equals("AX")) { //Tengo que mover el divd a AX.
                asm.addAll(liberaRegistro(registros, AX));
                asm.add("MOV AX, " + getPrefijo(tablaS, divd) + divd); //Muevo divd a AX.
                actualizaReg(registros, AX, true, -1); //Ocupo AX.
            } else {
                asm.addAll(liberaRegistro(registros, DX));
                asm.add("MOV DX, 0"); //Seteo DX en 0.
                actualizaReg(registros, DX, true, -1); //Ocupo DX.

                asm.add("DIV " + divs); //Hago la division.
            }

        GeneradorAssembler.agregaElementoPila("AX"); //El cociente queda en AX.
        actualizaReg(registros, AX, true, GeneradorAssembler.getLongitudPila()); //Actualizo ref del reg AX.
        actualizaReg(registros, DX, false, -1); //Libero DX.

        return asm;
    }
}
