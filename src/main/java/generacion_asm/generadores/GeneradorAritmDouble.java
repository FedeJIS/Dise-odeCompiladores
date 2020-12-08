package generacion_asm.generadores;

import analizador_sintactico.Parser;
import generacion_asm.GeneradorAssembler;
import util.tabla_simbolos.TablaSimbolos;

import java.util.ArrayList;
import java.util.List;

import static generacion_asm.util.UtilsRegistro.*;

public class GeneradorAritmDouble {
    private static String getInstruccion(String operador) {
        switch (operador) {
            case "*":
                return "FMUL";
            case "+":
                return "FADD";
            case "/":
                return "FDIV";
            case "-":
                return "FSUB";
        }
        return null;
    }

    public static List<String> genInstrAritmDouble(TablaSimbolos tablaS, String operador, String op1, String op2) {
        List<String> asm = new ArrayList<>();

        //Si el op1 es un valor inmediato primero lo cargo desde memoria.
        if (!esRegistro(op1) && tablaS.esEntradaCte(op1)) op1 = "_" + TablaSimbolos.formatDouble(op1);
        else op1 = getPrefijo(tablaS, op1) + op1;

        //Si el op2 es un valor inmediato primero lo cargo desde memoria.
        if (!esRegistro(op2) && tablaS.esEntradaCte(op2)) op2 = "_" + TablaSimbolos.formatDouble(op2);
        else op2 = getPrefijo(tablaS, op2) + op2;

        asm.add("FLD " + op1); //Pongo op1 en la pila del coproc.
        asm.add("FLD " + op2); //Pongo op2 en la pila del coproc.
        asm.add(getInstruccion(operador)); //Hago op en la pila del coproc.
        asm.add("FSTP @aux" + GeneradorAssembler.getVariableAux()); //Muevo resultado a mem.
        GeneradorAssembler.agregaElementoPila("@aux" + GeneradorAssembler.getVariableAux()); //Agrego operando a pila.
        tablaS.agregarEntrada(Parser.ID, "@aux" + GeneradorAssembler.getVariableAux(), "DOUBLE"); //Agrego vaux a TS.

        GeneradorAssembler.incrementaVarAux();

        return asm;
    }
}
