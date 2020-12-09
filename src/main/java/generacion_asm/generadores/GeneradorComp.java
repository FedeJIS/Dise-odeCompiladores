package generacion_asm.generadores;

import generacion_asm.util.InfoReg;
import util.tabla_simbolos.TablaSimbolos;

import java.util.ArrayList;
import java.util.List;

import static generacion_asm.util.UtilsRegistro.*;

public class GeneradorComp {
    private static List<String> genInstrCompDouble(TablaSimbolos tablaS, List<InfoReg> registros, String op1, String op2) {
        List<String> asm = new ArrayList<>();
        asm.add("FINIT");

        //Si el op1 es un valor inmediato primero lo cargo desde memoria.
        if (!esRegistro(op1) && tablaS.esEntradaCte(op1)) op1 = "_" + TablaSimbolos.formatDouble(op1);
        else op1 = getPrefijo(tablaS, op1) + op1;

        //Si el op2 es un valor inmediato primero lo cargo desde memoria.
        if (!esRegistro(op2) && tablaS.esEntradaCte(op2)) op2 = "_" + TablaSimbolos.formatDouble(op2);
        else op2 = getPrefijo(tablaS, op2) + op2;

        asm.add("FLD " + op1); //Pongo op1 en la pila del coproc.
        asm.add("FLD " + op2); //Pongo op2 en la pila del coproc.
        asm.add("FCOMP"); //Comparacion.

        asm.addAll(liberaRegistro(registros, AX));
        asm.add("FSTSW AX"); //Almacena el resultado en memoria.

        asm.add("SAHF"); //Almacena en los ocho bits menos significativos del registro de indicadores el valor del registro AH.

        return asm;
    }

    public static List<String> genInstrComp(TablaSimbolos tablaS, List<InfoReg> registros, String opIzq, String opDer) {
        List<String> asm = new ArrayList<>();

        tiposOperandosValidos(tablaS, registros, opIzq, esRegistro(opIzq), opDer, esRegistro(opDer));

        //Primer operando es un valor inmediato.
        if (!esRegistro(opIzq) && tablaS.esEntradaCte(opIzq))
            if (tablaS.getTipoEntrada(opIzq).equals("DOUBLE")) { //Valor inmediato DOUBLE
                asm.addAll(genInstrCompDouble(tablaS, registros, opDer, opIzq));
                return asm;
            } else { //Valor inmediato UINT
                String nuevoOp = getNombreRegistro(getRegistroLibre(registros));
                asm.add("MOV " + nuevoOp + ", " + opIzq); //No tiene prefijo pq es un valor inmediato UINT.
                asm.add("CMP " + nuevoOp + ", " + getPrefijo(tablaS, opDer) + opDer);
                return asm;
            }

        //Var COMP Var. Ambos operandos no pueden estar en memoria, tengo que traer uno a un reg.
        if (!esRegistro(opDer) && !esRegistro(opIzq))
            if (tablaS.getTipoEntrada(opDer).equals("DOUBLE")) {
                asm.addAll(genInstrCompDouble(tablaS, registros, opDer, opIzq));
                return asm;
            } else {
                String nuevoOp = getNombreRegistro(getRegistroLibre(registros));
                asm.add("MOV " + nuevoOp + ", " + getPrefijo(tablaS, opIzq) + opIzq);
                asm.add("CMP " + nuevoOp + ", " + getPrefijo(tablaS, opDer) + opDer);
                return asm;
            }

        //Reg COMP Var.
        if (!esRegistro(opDer) && esRegistro(opIzq)
                && tiposOperandosValidos(tablaS, registros, opDer,false,opIzq,true)){
            asm.add("CMP " + opIzq + ", " + getPrefijo(tablaS, opDer) + opDer);
            return asm;
        }

        //Var COMP Reg.
        if (esRegistro(opDer) && !esRegistro(opIzq)
                && tiposOperandosValidos(tablaS, registros, opDer,true,opIzq,false)){
            String nuevoOp = getNombreRegistro(getRegistroLibre(registros));
            asm.add("MOV " + nuevoOp + ", " + getPrefijo(tablaS, opIzq) + opIzq);
            asm.add("CMP " + nuevoOp + ", " + getPrefijo(tablaS, opDer) + opDer);
            return asm;
        }

        //Reg COMP Reg.
        if (esRegistro(opDer) && esRegistro(opIzq)
                && tiposOperandosValidos(tablaS, registros, opDer,true,opIzq,true)){
            asm.add("CMP " + opIzq + ", " + opDer);
            return asm;
        }

        return asm;
    }

    public static List<String> genInstrSalto(String tipoJump, String labelJump, String tipoComp) {
        List<String> asm = new ArrayList<>();

        if (tipoJump.equals("BF"))
            switch (tipoComp) {
                case "<":
                    asm.add("JAE L" + labelJump); //Opuesto de '<' = '>='.
                    break;
                case "<=":
                    asm.add("JA L" + labelJump); //Opuesto de '<=' = '>'.
                    break;
                case ">":
                    asm.add("JBE L" + labelJump); //Opuesto de '>' = '<='.
                    break;
                case ">=":
                    asm.add("JB L" + labelJump); //Opuesto de '>=' = '<'.
                    break;
                case "==":
                    asm.add("JNE L" + labelJump); //Opuesto de '==' = '!='.
                    break;
                case "!=":
                    asm.add("JE L" + labelJump); //Opuesto de '!=' = '=='.
                    break;
                default:
                    break;
            }

        return asm;
    }
}
