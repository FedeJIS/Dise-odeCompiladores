package generacion_asm;

import analizador_sintactico.Parser;
import generacion_asm.generadores.*;
import generacion_asm.util.InfoReg;
import generacion_c_intermedio.MultiPolaca;
import generacion_c_intermedio.Polaca;
import util.TablaNotificaciones;
import util.tabla_simbolos.Celda;
import util.tabla_simbolos.TablaSimbolos;

import java.util.ArrayList;
import java.util.List;

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
        if (pilaOps.size() > 0) {
            System.out.println("Antes:");
            System.out.println(pilaOps.toString());
            pilaOps.remove(pilaOps.size() - 1);
            System.out.println();
            System.out.println("Dsp:");
            System.out.println(pilaOps.toString());
        }
    }

    public static int getLongitudPila(){
        return pilaOps.size() - 1;
    }

    public static List<String> generaAsmDeclProc(MultiPolaca multiPolaca) {
        List<String> asmProcs = new ArrayList<>();
        for (String proc : multiPolaca.getNombreProcs()) {
            asmProcs.add("_" + proc + " PROC");
            asmProcs.addAll(generaAsm(multiPolaca.getPolaca(proc))); //Obtiene polaca del proc y la pasa a asm.
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

    private static List<String> generaInstrAritmDouble(String operador, String op1, String op2) {
        return new ArrayList<>();
    }

    //---GENERACION INSTRUCCIONES ARITMETICAS---

    /**
     * Fuente: A - B
     * Polaca: A,B,b
     * src = B, dest = A
     */
    private static List<String> genInstrAritmResta(String src, String dest) {
        List<String> asm = new ArrayList<>();

        //Variable & Variable
        if (!esRegistro(dest) && !esRegistro(src) && tiposOperandosValidos(dest, false, src, false))
            if (tablaS.getTipoEntrada(src).equals("DOUBLE")) return generaInstrAritmDouble("-", dest, src);
            else {
                int reg = getRegistroLibre(); //Obtengo reg libre.
                asm.add("MOV " + getNombreRegistro(reg) + ", " + getPrefijo(dest) + dest);
                asm.add("SUB " + getNombreRegistro(reg) + ", " + getPrefijo(src) + src);

                asm.add("CMP " + getNombreRegistro(reg) + ", 0");
                asm.add("JE L_resta_neg");

                pilaOps.add(getNombreRegistro(reg));
                actualizaReg(reg, true, pilaOps.size() - 1);
            }

        //Reg & Variable
        if (esRegistro(dest) && !esRegistro(src) && tiposOperandosValidos(dest, true, src, false)) {
            asm.add("SUB " + dest + ", " + getPrefijo(src) + src); //dest es el registro.
            pilaOps.add(dest);
        }

        //Reg & Reg
        if (esRegistro(dest) && esRegistro(src) && tiposOperandosValidos(dest, true, src, true)) {
            asm.add("SUB " + dest + ", " + src);
            pilaOps.add(dest);
            marcaRegLiberado(src);
        }

        //Variable & Reg. No puedo restar sobre src porque la op es conmut.
        if (!esRegistro(dest) && esRegistro(src) && tiposOperandosValidos(dest, false, src, true)) {
            int reg = getRegistroLibre();
            asm.add("MOV " + getNombreRegistro(reg) + ", " + getPrefijo(dest) + dest); //Muevo dest a registro libre.
            asm.add("SUB " + getNombreRegistro(reg) + ", " + src); //Resto sobre el nuevo reg.

            pilaOps.add(getNombreRegistro(reg));
            actualizaReg(reg, true, pilaOps.size() - 1);
            actualizaReg(getIdRegistro(src), false, -1);
        }

        return asm;
    }

    /**
     * Fuente: A / B
     * Polaca: A,B,/
     * divs = B, divd = A
     */
    private static List<String> genInstrAritmDiv(String divs, String divd) {
        List<String> asm = new ArrayList<>();

        //Variable & Variable
        if (!esRegistro(divd) && !esRegistro(divs) && tiposOperandosValidos(divd, false, divs, false))
            if (tablaS.getTipoEntrada(divd).equals("DOUBLE")) return generaInstrAritmDouble("/", divd, divs);
            else {
                asm.addAll(liberaRegistro(AX)); //Se encarga de liberar AX, y guardar su contenido previo si corresponde.
                asm.add("MOV AX, " + getPrefijo(divd) + divd); //Muevo divd a AX.
                actualizaReg(AX, true, -1); //Ocupo AX.

                asm.addAll(liberaRegistro(DX)); //Se encarga de liberar DX, y guardar su contenido previo si corresponde.
                asm.add("MOV DX, 0"); //Seteo DX en 0.
                actualizaReg(DX, true, -1); //Ocupo DX.

                if (tablaS.esEntradaCte(divs)) {
                    asm.add("MOV @aux" + variableAux + ", " + divs); //Muevo divs a memoria pq el divs no puede ser un inmediato.
                    asm.add("DIV @aux" + variableAux); //Hago la division.
                    variableAux++;
                } else asm.add("DIV _" + divs); //Hago la division.
            }

        //Reg & Variable. Reg destino tiene que ser AX.
        if (esRegistro(divd) && !esRegistro(divs) && tiposOperandosValidos(divd, true, divs, false)) {
            if (!divd.equals("AX")) { //Tengo que mover el divd a AX.
                asm.addAll(liberaRegistro(AX)); //Se encarga de liberar AX, y guardar su contenido previo si corresponde.
                asm.add("MOV AX, " + divd); //Muevo divd a AX.
                actualizaReg(AX, true, -1); //Ocupo AX.
            }
            asm.addAll(liberaRegistro(DX));
            asm.add("MOV DX, 0"); //Seteo DX en 0.
            actualizaReg(DX, true, -1); //Ocupo DX.

            if (tablaS.esEntradaCte(divs)) {
                asm.add("MOV @aux" + variableAux + ", " + divs); //Muevo divs a memoria pq el divs no puede ser un inmediato.
                asm.add("DIV @aux" + variableAux); //Hago la division.
                variableAux++;
            } else asm.add("DIV _" + divs); //Hago la division.
        }

        //Reg & Reg. Reg destino tiene que ser AX.
        if (esRegistro(divd) && esRegistro(divs) && tiposOperandosValidos(divd, true, divs, true)) {
            if (!divd.equals("AX")) { //Tengo que mover el divd a AX.
                asm.addAll(liberaRegistro(AX)); //Se encarga de liberar AX, y guardar su contenido previo si corresponde.
                asm.add("MOV AX, " + divd); //Muevo divd a AX.
                actualizaReg(AX, true, -1); //Ocupo AX.
            }

            asm.addAll(liberaRegistro(DX));
            asm.add("MOV DX, 0"); //Seteo DX en 0.
            actualizaReg(DX, true, -1); //Ocupo DX.

            asm.add("DIV " + divs); //Hago la division.
        }

        //Variable & Reg. Reg destino tiene que ser AX.
        if (!esRegistro(divd) && esRegistro(divs) && tiposOperandosValidos(divd, false, divs, true))
            if (!divd.equals("AX")) { //Tengo que mover el divd a AX.
                asm.addAll(liberaRegistro(AX)); //Se encarga de liberar AX, y guardar su contenido previo si corresponde.
                asm.add("MOV AX, " + getPrefijo(divd) + divd); //Muevo divd a AX.
                actualizaReg(AX, true, -1); //Ocupo AX.
            } else {
                asm.addAll(liberaRegistro(DX));
                asm.add("MOV DX, 0"); //Seteo DX en 0.
                actualizaReg(DX, true, -1); //Ocupo DX.

                asm.add("DIV " + divs); //Hago la division.
            }

        pilaOps.add("AX"); //El cociente queda en AX.
        actualizaReg(AX, true, pilaOps.size() - 1); //Actualizo ref del reg AX.
        actualizaReg(DX, false, -1); //Libero DX.

        return asm;
    }

    private static void actualizaReg(int nReg, boolean ocupado, int ref) {
        InfoReg reg = registros.get(nReg);
        reg.setOcupado(ocupado);
        reg.setRef(ref);
    }

    //---UTILIDADES ARITMETICA---

    private static String getInstrAritmDouble(String operador) {
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

    private static List<String> liberaRegistro(int idReg) {
        List<String> asm = new ArrayList<>();

        if (registros.get(idReg).isNotOcupado()) return asm;
        else {
            int regLibre = 0;
            while (regLibre < registros.size() && !registros.get(regLibre).isNotOcupado())
                regLibre++; //Encuentra 1 reg libre.

            int refAnterior = registros.get(AX).getRef();
            String valorNuevaRef;
            if (regLibre == registros.size()) { //No hay regs libres.
                valorNuevaRef = "@aux" + variableAux;
                variableAux++;
            } else {
                valorNuevaRef = getNombreRegistro(regLibre);
                registros.get(regLibre).setOcupado(true); //Marco el registro al cual movi como ocupado.
            }
            pilaOps.set(refAnterior, valorNuevaRef); //Actualizo el operando que estaba en la pila.
            asm.add("MOV " + valorNuevaRef + ", AX"); //Copio lo que estaba en AX en su nuevo lugar.
        }
        return asm;
    }

    //---UTILIDADES REGISTROS---

    private static int getRegistroLibre() {
        //Compruebo primero que esten libres los registros que no se usan en MUL y DIV.
        if (registros.get(BX).isNotOcupado()) return BX;
        if (registros.get(CX).isNotOcupado()) return CX;
        if (registros.get(AX).isNotOcupado()) return AX;
        if (registros.get(DX).isNotOcupado()) return DX;
        return -1;
    }

    private static boolean esRegistro(String operando) {
        return operando.charAt(0) == 'A'
                || operando.charAt(0) == 'B'
                || operando.charAt(0) == 'C'
                || operando.charAt(0) == 'D';
    }

    private static int getIdRegistro(String nombre) {
        switch (nombre) {
            case "AX":
                return 0;
            case "BX":
                return 1;
            case "CX":
                return 2;
            case "DX":
                return 3;
        }
        return -1;
    }

    private static String getNombreRegistro(int id) {
        switch (id) {
            case 0:
                return "AX";
            case 1:
                return "BX";
            case 2:
                return "CX";
            case 3:
                return "DX";
        }
        return "";
    }

    private static void marcaRegLiberado(String reg) {
        registros.get(getIdRegistro(reg)).setOcupado(false);
        registros.get(getIdRegistro(reg)).setRef(-1);
    }

    private static String getPrefijo(String op) {
        if (esRegistro(op)) return ""; //Regs
        if (op.startsWith("@")) return ""; //Auxs
        if (tablaS.getTipoEntrada(op).equals(Celda.TIPO_UINT)) return ""; //Ctes uint.

        return "_"; //Ctes double o vars.
    }

    //---OTRAS UTILIDADES---

    private static boolean tiposOperandosValidos(String op1, boolean esRegOp1, String op2, boolean esRegOp2) {
        String tipoOp1, tipoOp2;

        if (esRegOp1) tipoOp1 = registros.get(getIdRegistro(op1)).getTipo();
        else tipoOp1 = tablaS.getTipoEntrada(op1);

        if (esRegOp2) tipoOp2 = registros.get(getIdRegistro(op2)).getTipo();
        else tipoOp2 = tablaS.getTipoEntrada(op2);

        //Tipos validos.
        if (tipoOp1.equals(tipoOp2)) return true;

        //Tipos invalidos.
        TablaNotificaciones.agregarError(0, "Los operandos '" + op1 + "' y '" + op2 + "' no tienen tipos compatibles.");
        throw new IllegalStateException("El codigo tiene errores, cortando generacion asm.");
    }
}
