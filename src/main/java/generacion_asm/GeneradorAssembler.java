package generacion_asm;

import generacion_asm.generadores.*;
import generacion_asm.util.InfoReg;
import generacion_c_intermedio.MultiPolaca;
import generacion_c_intermedio.Polaca;
import util.TablaNotificaciones;
import util.tabla_simbolos.TablaSimbolos;

import java.util.ArrayList;
import java.util.List;

import static generacion_asm.util.UtilsRegistro.*;

public class GeneradorAssembler {
    private static final int AX = 0, BX = 1, CX = 2, DX = 3;
    private static final List<InfoReg> registros = new ArrayList<>();
    private static final List<String> pilaOps = new ArrayList<>();
    private static int variableAux = 0;
    private static TablaSimbolos tablaS;
    private static String procAnterior = "";

    public static void reset(TablaSimbolos tablaS) {
        GeneradorAssembler.tablaS = tablaS;
        variableAux = 0;

        pilaOps.clear();
        registros.clear();
        registros.add(new InfoReg());
        registros.add(new InfoReg());
        registros.add(new InfoReg());
        registros.add(new InfoReg());
    }

    public static int getVariableAux() {
        return variableAux;
    }

    public static void incrementaVarAux() {
        GeneradorAssembler.variableAux++;
    }

    public static void cambiaElementoPila(int refAnterior, String valorNuevaRef){
        pilaOps.set(refAnterior, valorNuevaRef);
    }

    public static void agregaElementoPila(String elem){
        pilaOps.add(elem);
    }

    public static  void quitaElementoPila(){
        if (pilaOps.size() > 0) pilaOps.remove(pilaOps.size() - 1);
    }
    public static  String getTopePila(){
        if (pilaOps.size() > 0) return pilaOps.remove(pilaOps.size() - 1);
        return null;
    }

    public static int getLongitudPila(){
        return pilaOps.size() - 1;
    }

    public static List<String> generaAsmDeclProc(MultiPolaca multiPolaca) {
        List<String> asmProcs = new ArrayList<>();
        for (String proc : multiPolaca.getNombreProcs()) {
            asmProcs.add("_" + proc + " PROC");

            //Control recursion.
            int regLibre = getRegistroLibre(registros);
            asmProcs.add("MOV "+getNombreRegistro(regLibre)+", _INVOCADO_"+proc);
            asmProcs.add("CMP "+getNombreRegistro(regLibre)+", 1"); //esta en 1 si la invocacion es recursiva.
            asmProcs.add("JE L_recursion"); //salta a label en caso de que la comparacion de verdadero.
            asmProcs.add("MOV _INVOCADO_"+proc+", 1"); //Pone variable auxiliar en 1.

            //Control NI.
            regLibre = getRegistroLibre(registros);
            asmProcs.add("MOV "+getNombreRegistro(regLibre)+", _NI_ACT_"+proc);
            asmProcs.add("CMP "+getNombreRegistro(regLibre)+", _NI_MAX_"+proc); //comparar ni.
            asmProcs.add("JAE L_ni_superado"); //salta a label en caso de que se supere el limite.
            asmProcs.add("ADD _NI_ACT_"+proc+", 1"); //si es menor hacer call y sumar 1.

            asmProcs.addAll(generaAsm(multiPolaca.getPolaca(proc))); //Obtiene instrucciones del proc y la pasa a asm.

            //El proc termino de ejecutarse, por lo que la variable asociada a la recursion tiene que estar en 0.
            asmProcs.add("MOV _INVOCADO_"+proc+", 0");

            asmProcs.add("RET");
            asmProcs.add("_" + proc + " ENDP");
            pilaOps.clear();
        }
        return asmProcs;
    }

    public static List<String> generaAsm(Polaca polaca) {
        if (TablaNotificaciones.hayErrores())
            throw new IllegalStateException("El codigo tiene errores, cortando generacion asm.");
        List<String> asm = new ArrayList<>();
        String tipoComp = "";
        for (String paso : polaca.getListaPasos()) {
            if (paso.charAt(0) == 'L') asm.add(paso + ":"); //Agrego el label.
            else switch (paso) {
                case "INVOC": asm.addAll(GeneradorInvoc.genInstrInvoc(tablaS, registros, procAnterior));
                    break;
                case "*": asm.addAll(GeneradorMult.genInstrAritmMult(tablaS, registros,
                        pilaOps.remove(pilaOps.size() - 1), pilaOps.remove(pilaOps.size() - 1)));
                    break;
                case "+": asm.addAll(GeneradorSuma.genInstrAritmSuma(tablaS, registros,
                        pilaOps.remove(pilaOps.size() - 1), pilaOps.remove(pilaOps.size() - 1)));
                    break;
                case "/": asm.addAll(GeneradorDiv.genInstrAritmDiv(tablaS, registros,
                        pilaOps.remove(pilaOps.size() - 1), pilaOps.remove(pilaOps.size() - 1)));
                    break;
                case "-": asm.addAll(GeneradorResta.genInstrAritmResta(tablaS, registros,
                        pilaOps.remove(pilaOps.size() - 1), pilaOps.remove(pilaOps.size() - 1)));
                    break;
                case "=": asm.addAll(GeneradorAsign.genInstrAsign(tablaS, registros,
                        pilaOps.remove(pilaOps.size() - 1), pilaOps.remove(pilaOps.size() - 1)));
                    break;
                case "-=": asm.addAll(GeneradorAsign.genInstrAsignResta(tablaS, registros,
                        pilaOps.remove(pilaOps.size() - 1), pilaOps.remove(pilaOps.size() - 1)));
                    break;
                case "<":
                case "<=":
                case ">":
                case ">=":
                case "==":
                case "!=": {
                    String opDer = pilaOps.remove(pilaOps.size()-1);
                    String opIzq = pilaOps.remove(pilaOps.size()-1);
                    asm.addAll(GeneradorComp.genInstrComp(tablaS, registros, opIzq, opDer));
                    tipoComp = paso;
                    break;
                }
                case "BI": asm.add("JMP L" + pilaOps.remove(pilaOps.size() - 1));
                    break;
                case "BF": asm.addAll(GeneradorComp.genInstrSalto(paso, pilaOps.remove(pilaOps.size() - 1), tipoComp));
                    break;
                case "OUT_UINT":
                case "OUT_DOUBLE":
                case "OUT_CAD": asm.add(GeneradorOut.generaInstrOut(tablaS, paso, pilaOps.remove(pilaOps.size()-1)));
                    break;
                default:
                    if (esRegistro(paso)) registros.get(getIdRegistro(paso)).setRef(pilaOps.size() - 1);
                    procAnterior = paso;
                    pilaOps.add(paso);
                    break;
            }
        }
        return asm;
    }
}
