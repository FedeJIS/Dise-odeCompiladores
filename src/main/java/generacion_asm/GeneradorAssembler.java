package generacion_asm;

import analizador_sintactico.Parser;
import generacion_c_intermedio.MultiPolaca;
import generacion_c_intermedio.Polaca;
import util.TablaNotificaciones;
import util.tabla_simbolos.TablaSimbolos;

import java.util.ArrayList;
import java.util.List;

public class GeneradorAssembler {
    private static class InfoReg {
        private int ref;
        private boolean ocupado;
        private final String tipo;

        public InfoReg() {
            this.ref = -1;
            this.ocupado = false;
            this.tipo = "UINT";
        }

        public int getRef() {
            return ref;
        }

        public void setRef(int ref) {
            this.ref = ref;
        }

        public boolean isNotOcupado() {
            return !ocupado;
        }

        public void setOcupado(boolean ocupado) {
            this.ocupado = ocupado;
        }

        public String getTipo() {
            return tipo;
        }
    }

    private static final int AX = 0, BX = 1, CX = 2, DX = 3;

    private static int variableAux = 0;

    private static final List<InfoReg> registros = new ArrayList<>();

    private static final List<String> pilaOps = new ArrayList<>();

    private static TablaSimbolos tablaS;

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

    public static List<String> generaAsmDeclProc(MultiPolaca multiPolaca){
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

    private static String procAnterior = "";

    public static List<String> generaAsm(Polaca polaca){
        if (TablaNotificaciones.hayErrores())
            throw new IllegalStateException("El codigo tiene errores, cortando generacion asm.");
        List<String> asm = new ArrayList<>();
        String tipoComp = "";
        for (String paso : polaca.getListaPasos()){
            if (paso.charAt(0) == 'L') asm.add(paso+":"); //Agrego el label.
            else switch (paso){
                case "INVOC":
                    int aux2 = 0;
                    String procInvocado = pilaOps.remove(pilaOps.size()-1);
                    if (procAnterior.equals(procInvocado)) aux2 = 1;

                    //Si aux1 y aux2 son iguales, significa que se esta realizando una invocacion recursiva.
                    String reg = getNombreRegistro(getRegistroLibre());
                    asm.add("MOV "+reg+", 1");
                    asm.add("CMP "+reg+", "+aux2);
                    asm.add("JE L_recursion");
                    asm.add("CALL _" + procInvocado);

                    procAnterior = procInvocado;
                    break;
                case "*":
                    asm.addAll(genInstrAritmMult(pilaOps.remove(pilaOps.size()-1), pilaOps.remove(pilaOps.size()-1)));
                    break;
                case "+":
                    asm.addAll(genInstrAritmSuma(pilaOps.remove(pilaOps.size()-1), pilaOps.remove(pilaOps.size()-1)));
                    break;
                case "/":
                    asm.addAll(genInstrAritmDiv(pilaOps.remove(pilaOps.size()-1), pilaOps.remove(pilaOps.size()-1)));
                    break;
                case "-":
                    asm.addAll(genInstrAritmResta(pilaOps.remove(pilaOps.size()-1), pilaOps.remove(pilaOps.size()-1)));
                    break;
                case "=":
                    asm.addAll(genInstrAsign());
                    break;
                case "<":
                case "<=":
                case ">":
                case ">=":
                case "==":
                case "!=": {
                    asm.addAll(genInstrComp());
                    tipoComp = paso;
                    break;
                }
                case "BI":
                    asm.add("JMP L"+pilaOps.remove(pilaOps.size()-1));
                    break;
                case "BF":
                    asm.addAll(genInstrSalto(paso,pilaOps.remove(pilaOps.size()-1),tipoComp));
                    break;
                case "OUT_UINT":
                    String op = pilaOps.remove(pilaOps.size()-1);
                    asm.add("invoke MessageBox, NULL, addr "+getPrefijo(op)+op+", addr "+getPrefijo(op)+op+", MB_OK" + '\n');
                    break;
                case "OUT_DOUBLE":
                    op = pilaOps.remove(pilaOps.size()-1);
                    asm.add("invoke MessageBox, NULL, addr "+getPrefijo(op)+op+", addr "+getPrefijo(op)+op+", MB_OK" + '\n');
                    break;
                case "OUT_CAD":
                    asm.add("OUT_CAD"+pilaOps.remove(pilaOps.size()-1));
                    break;

                default:
                    if (esRegistro(paso))
                        registros.get(getIdRegistro(paso)).setRef(pilaOps.size() - 1);
                    pilaOps.add(paso.replace(":","@"));
                    break;
            }
        }
        return asm;
    }

    //---GENERACION INSTRUCCIONES JUMP---

    private static List<String> genInstrSalto(String tipoJump, String labelJump, String tipoComp) {
        List<String> asm = new ArrayList<>();

        if (tipoJump.equals("BF"))
            switch (tipoComp){
                case "<":
                    asm.add("JAE L"+labelJump); //Opuesto de '<' = '>='.
                    break;
                case "<=":
                    asm.add("JA L"+labelJump); //Opuesto de '<=' = '>'.
                    break;
                case ">":
                    asm.add("JBE L"+labelJump); //Opuesto de '>' = '<='.
                    break;
                case ">=":
                    asm.add("JB L"+labelJump); //Opuesto de '>=' = '<'.
                    break;
                case "==":
                    asm.add("JNE L"+labelJump); //Opuesto de '==' = '!='.
                    break;
                case "!=":
                    asm.add("JE L"+labelJump); //Opuesto de '!=' = '=='.
                    break;
                default:
                    throw new IllegalStateException("Simbolo "+tipoComp+" no reconocido como comparador");
            }

        return asm;
    }

    //---GENERACION INSTRUCCIONES COMPARACION---

    private static List<String> genInstrComp() {
        List<String> asm = new ArrayList<>();

        String op1 = pilaOps.remove(pilaOps.size() - 1); //Op mas a la derecha.
        String op2 = pilaOps.remove(pilaOps.size() - 1);

        //Primer operando es un valor inmediato.
        if (!esRegistro(op2) && tablaS.esCte(op2) && tiposOperandosValidos(op1,false,op2,false))
            if (tablaS.getTipo(op2).equals("DOUBLE")) {
                asm.addAll(genInstrCompDouble(op1, op2));
                return asm;
            }
            else {
                String nuevoOp = getNombreRegistro(getRegistroLibre());
                asm.add("MOV " + nuevoOp + ", " + op2);
                asm.add("CMP " + nuevoOp + ", " + getPrefijo(op1) + op1);
            }

        //Var & Var. Ambos operandos no pueden estar en memoria, tengo que traer uno a un reg.
        if (!esRegistro(op1) && !esRegistro(op2) && tiposOperandosValidos(op1,false,op2,false))
            if (tablaS.getTipo(op1).equals("DOUBLE")) {
                asm.addAll(genInstrCompDouble(op1, op2));
                return asm;
            }
            else {
                String nuevoOp = getNombreRegistro(getRegistroLibre());
                asm.add("MOV " + nuevoOp + ", " + getPrefijo(op2) + op2);
                asm.add("CMP " + nuevoOp + ", " + getPrefijo(op1) + op1);
            }
        return asm;
    }

    private static List<String> genInstrCompDouble(String op1, String op2){
        List<String> asm = new ArrayList<>();

        //Si el op1 es un valor inmediato primero lo cargo desde memoria.
        if (!esRegistro(op1) && tablaS.esCte(op1)) op1 = TablaSimbolos.formatDouble(op1);

        //Si el op2 es un valor inmediato primero lo cargo desde memoria.
        if (!esRegistro(op2) && tablaS.esCte(op2)) op2 = TablaSimbolos.formatDouble(op2);

        asm.add("FLD "+op1); //Pongo op1 en la pila del coproc.
        asm.add("FLD "+op2); //Pongo op2 en la pila del coproc.
        asm.add("FCOM"); //Comparacion.

        asm.add("FSTSW @aux"+variableAux); //Almacena el resultado en memoria.
        asm.addAll(liberaRegistro(AX));
        asm.add("MOV AX, @aux"+variableAux); //Muevo a AX el resultado de la comparacion.
        tablaS.agregarEntrada(Parser.ID,"@aux"+variableAux,"DOUBLE"); //Agreo vaux a TS.
        variableAux++;

        asm.add("SAHF"); //Almacena en los ocho bits menos significativos del registro de indicadores el valor del registro AH.

        return asm;
    }

    //---GENERACION INSTRUCCIONES ASIGNACION---

    private static List<String> genInstrAsign(){
        List<String> asm = new ArrayList<>();

        String dest = pilaOps.remove(pilaOps.size() - 1);
        String src = pilaOps.remove(pilaOps.size() - 1);

        //dest = Variable & src = Reg
        if (!esRegistro(dest) && esRegistro(src) && tiposOperandosValidos(dest, false, src, true)){
            asm.add("MOV "+getPrefijo(dest)+dest+", "+src);
            marcaRegLiberado(src);
        }

        //Variable & Variable
        if (!esRegistro(dest) && !esRegistro(src) && tiposOperandosValidos(dest,false,src,false)) {
            if (tablaS.getTipo(dest).equals("DOUBLE")){ //Es un double
                if (tablaS.esCte(src)) src = TablaSimbolos.formatDouble(src);
                else src = getPrefijo(src)+src;

                asm.add("FLD "+src);
                asm.add("FSTP"+getPrefijo(dest)+dest);

                return asm;
            }
            int idReg = getRegistroLibre();
            asm.add("MOV " + getNombreRegistro(idReg) + ", " + getPrefijo(src) + src);
            asm.add("MOV _" + dest + ", " + getNombreRegistro(idReg));
        }

        return asm;
    }

    //---GENERACION INSTRUCCIONES ARITMETICAS---

    private static List<String> generaInstrAritmDouble(String operador, String op1, String op2){
        List<String> asm = new ArrayList<>();

        //Si el op1 es un valor inmediato primero lo cargo desde memoria.
        if (!esRegistro(op1) && tablaS.esCte(op1)) op1 = TablaSimbolos.formatDouble(op1);
        else op1 = getPrefijo(op1)+op1;

        //Si el op2 es un valor inmediato primero lo cargo desde memoria.
        if (!esRegistro(op2) && tablaS.esCte(op2)) op2 = TablaSimbolos.formatDouble(op2);
        else op2 = getPrefijo(op2)+op2;

        asm.add("FLD "+op1); //Pongo op1 en la pila del coproc.
        asm.add("FLD "+op2); //Pongo op2 en la pila del coproc.
        asm.add(getInstrAritmDouble(operador)); //Hago op en la pila del coproc.
        asm.add("FSTP @aux"+variableAux); //Muevo resultado a mem.
        pilaOps.add("@aux"+variableAux); //Agrego operando a pila.
        tablaS.agregarEntrada(Parser.ID,"@aux"+variableAux,"DOUBLE"); //Agreo vaux a TS.

        variableAux++;

        return asm;
    }

    /**
     * Fuente: A + B
     * Polaca: A,B,+
     * src = B, dest = A
     */
    private static List<String> genInstrAritmSuma(String src, String dest){
        List<String> asm = new ArrayList<>();

        //Variable & Variable
        if (!esRegistro(dest) && !esRegistro(src) && tiposOperandosValidos(dest,false,src,false))
            if (tablaS.getTipo(src).equals("DOUBLE")) return generaInstrAritmDouble("+", dest, src);
            else {
                int reg = getRegistroLibre(); //Obtengo reg libre.
                asm.add("MOV " + getNombreRegistro(reg) + ", " + getPrefijo(dest) + dest);
                asm.add("ADD " + getNombreRegistro(reg) + ", " + getPrefijo(src) + src);
                pilaOps.add(getNombreRegistro(reg));
                actualizaReg(reg, true, pilaOps.size()-1);
            }

        //Reg & Variable
        if (esRegistro(dest) && !esRegistro(src) && tiposOperandosValidos(dest,true,src,false)) {
            asm.add("ADD " + dest + ", " + getPrefijo(src) + src); //dest es el registro.
            pilaOps.add(dest);
        }

        //Reg & Reg
        if (esRegistro(dest) && esRegistro(src) && tiposOperandosValidos(dest,true,src,true)) {
            asm.add("ADD " + dest + ", " + src);
            pilaOps.add(dest);
            marcaRegLiberado(src);
        }

        //Variable & Reg
        if (!esRegistro(dest) && esRegistro(src) && tiposOperandosValidos(dest,false,src,true)) {
            asm.add("ADD " + src + ", " + getPrefijo(dest) + dest); //src es el registro. Puedo sumar sobre src porque la op es conmutativa.
            pilaOps.add(src);
        }
        return asm;
    }

    /**
     * Fuente: A * B
     * Polaca: A,B,*
     * src = B, dest = A
     */
    private static List<String> genInstrAritmMult(String src, String dest){
        List<String> asm = new ArrayList<>();

        //Variable & Variable
        if (!esRegistro(dest) && !esRegistro(src) && tiposOperandosValidos(dest,false,src,false))
            if (tablaS.getTipo(dest).equals("DOUBLE")) return generaInstrAritmDouble("*", dest, src);
            else {
                asm.addAll(liberaRegistro(AX)); //Se encarga de liberar AX, y guardar su contenido previo si corresponde.
                asm.add("MOV AX, "+getPrefijo(dest)+dest);
                asm.add("MUL AX, "+getPrefijo(src)+src);
            }

        //Reg & Variable. Reg destino tiene que ser AX.
        if (esRegistro(dest) && !esRegistro(src) && tiposOperandosValidos(dest,true,src,false))
            if (dest.equals("AX")) asm.add("MUL AX, "+getPrefijo(src)+src);
            else { //Tengo que mover el dest a AX.
                asm.addAll(liberaRegistro(AX));
                asm.add("MOV AX, " + dest);
                asm.add("MUL AX, "+getPrefijo(src)+src);

                actualizaReg(getIdRegistro(dest),false,-1);
            }

        //Reg & Reg. Reg destino tiene que ser AX.
        if (esRegistro(dest) && esRegistro(src) && tiposOperandosValidos(dest,true,src,true))
            if (dest.equals("AX")) asm.add("MUL AX, " + src);
            else {
                asm.addAll(liberaRegistro(AX));
                asm.add("MOV AX, " + dest);
                asm.add("MUL AX, " + src);

                actualizaReg(getIdRegistro(dest),false,-1);
                actualizaReg(getIdRegistro(src),false,-1);
            }

        //Variable & Reg. Reg destino tiene que ser AX.
        if (!esRegistro(dest) && esRegistro(src) && tiposOperandosValidos(dest,false,src,true))
            if (src.equals("AX")) asm.add("MUL "+ src + ", "+getPrefijo(dest)+dest); //Puedo invertir los operandos por prop conmut.
            else {
                asm.addAll(liberaRegistro(AX));
                asm.add("MOV AX, " + src);
                asm.add("MUL AX, "+getPrefijo(dest)+dest);

                actualizaReg(getIdRegistro(src),false,-1);
            }

        pilaOps.add("AX");
        actualizaReg(AX,true, pilaOps.size()-1);
        return asm;
    }

    /**
     * Fuente: A - B
     * Polaca: A,B,b
     * src = B, dest = A
     */
    private static List<String> genInstrAritmResta(String src, String dest){
        List<String> asm = new ArrayList<>();

        //Variable & Variable
        if (!esRegistro(dest) && !esRegistro(src) && tiposOperandosValidos(dest,false,src,false))
            if (tablaS.getTipo(src).equals("DOUBLE")) return generaInstrAritmDouble("-", dest, src);
            else {
                int reg = getRegistroLibre(); //Obtengo reg libre.
                asm.add("MOV " + getNombreRegistro(reg) + ", " + getPrefijo(dest) + dest);
                asm.add("SUB " + getNombreRegistro(reg) + ", " + getPrefijo(src) + src);

                asm.add("CMP "+getNombreRegistro(reg)+", 0");
                asm.add("JB L_resta_neg");

                pilaOps.add(getNombreRegistro(reg));
                actualizaReg(reg, true, pilaOps.size()-1);
            }

        //Reg & Variable
        if (esRegistro(dest) && !esRegistro(src) && tiposOperandosValidos(dest,true,src,false)) {
            asm.add("SUB " + dest + ", " + getPrefijo(src) + src); //dest es el registro.
            pilaOps.add(dest);
        }

        //Reg & Reg
        if (esRegistro(dest) && esRegistro(src) && tiposOperandosValidos(dest,true,src,true)) {
            asm.add("SUB " + dest + ", " + src);
            pilaOps.add(dest);
            marcaRegLiberado(src);
        }

        //Variable & Reg. No puedo restar sobre src porque la op es conmut.
        if (!esRegistro(dest) && esRegistro(src) && tiposOperandosValidos(dest,false,src,true)){
            int reg = getRegistroLibre();
            asm.add("MOV "+getNombreRegistro(reg)+", " + getPrefijo(dest) + dest); //Muevo dest a registro libre.
            asm.add("SUB "+getNombreRegistro(reg)+", "+src); //Resto sobre el nuevo reg.

            pilaOps.add(getNombreRegistro(reg));
            actualizaReg(reg, true, pilaOps.size()-1);
            actualizaReg(getIdRegistro(src),false,-1);
        }

        return asm;
    }

    /**
     * Fuente: A / B
     * Polaca: A,B,/
     * divs = B, divd = A
     */
    private static List<String> genInstrAritmDiv(String divs, String divd){
        List<String> asm = new ArrayList<>();

        //Variable & Variable
        if (!esRegistro(divd) && !esRegistro(divs) && tiposOperandosValidos(divd,false,divs,false))
            if (tablaS.getTipo(divd).equals("DOUBLE")) return generaInstrAritmDouble("/", divd, divs);
            else {
                asm.addAll(liberaRegistro(AX)); //Se encarga de liberar AX, y guardar su contenido previo si corresponde.
                asm.add("MOV AX, " + getPrefijo(divd) + divd); //Muevo divd a AX.
                actualizaReg(AX,true, -1); //Ocupo AX.

                asm.addAll(liberaRegistro(DX)); //Se encarga de liberar DX, y guardar su contenido previo si corresponde.
                asm.add("MOV DX, 0"); //Seteo DX en 0.
                actualizaReg(DX,true, -1); //Ocupo DX.

                if (tablaS.esCte(divs)) {
                    asm.add("MOV @aux" + variableAux + ", " + divs); //Muevo divs a memoria pq el divs no puede ser un inmediato.
                    asm.add("DIV @aux" + variableAux); //Hago la division.
                    variableAux++;
                } else asm.add("DIV _"+divs); //Hago la division.
            }

        //Reg & Variable. Reg destino tiene que ser AX.
        if (esRegistro(divd) && !esRegistro(divs) && tiposOperandosValidos(divd,true,divs,false)) {
            if (!divd.equals("AX")) { //Tengo que mover el divd a AX.
                asm.addAll(liberaRegistro(AX)); //Se encarga de liberar AX, y guardar su contenido previo si corresponde.
                asm.add("MOV AX, " + divd); //Muevo divd a AX.
                actualizaReg(AX, true, -1); //Ocupo AX.
            }
            asm.addAll(liberaRegistro(DX));
            asm.add("MOV DX, 0"); //Seteo DX en 0.
            actualizaReg(DX, true, -1); //Ocupo DX.

            if (tablaS.esCte(divs)) {
                asm.add("MOV @aux" + variableAux + ", " + divs); //Muevo divs a memoria pq el divs no puede ser un inmediato.
                asm.add("DIV @aux" + variableAux); //Hago la division.
                variableAux++;
            } else asm.add("DIV _"+divs); //Hago la division.
        }

        //Reg & Reg. Reg destino tiene que ser AX.
        if (esRegistro(divd) && esRegistro(divs) && tiposOperandosValidos(divd,true,divs,true)){
            if (!divd.equals("AX")) { //Tengo que mover el divd a AX.
                asm.addAll(liberaRegistro(AX)); //Se encarga de liberar AX, y guardar su contenido previo si corresponde.
                asm.add("MOV AX, " + divd); //Muevo divd a AX.
                actualizaReg(AX, true, -1); //Ocupo AX.
            }

            asm.addAll(liberaRegistro(DX));
            asm.add("MOV DX, 0"); //Seteo DX en 0.
            actualizaReg(DX, true, -1); //Ocupo DX.

            asm.add("DIV "+divs); //Hago la division.
        }

        //Variable & Reg. Reg destino tiene que ser AX.
        if (!esRegistro(divd) && esRegistro(divs) && tiposOperandosValidos(divd,false,divs,true))
            if (!divd.equals("AX")) { //Tengo que mover el divd a AX.
                asm.addAll(liberaRegistro(AX)); //Se encarga de liberar AX, y guardar su contenido previo si corresponde.
                asm.add("MOV AX, " + getPrefijo(divd) + divd); //Muevo divd a AX.
                actualizaReg(AX, true, -1); //Ocupo AX.
            }
            else {
                asm.addAll(liberaRegistro(DX));
                asm.add("MOV DX, 0"); //Seteo DX en 0.
                actualizaReg(DX, true, -1); //Ocupo DX.

                asm.add("DIV "+divs); //Hago la division.
            }

        pilaOps.add("AX"); //El cociente queda en AX.
        actualizaReg(AX, true, pilaOps.size() - 1); //Actualizo ref del reg AX.
        actualizaReg(DX, false, -1); //Libero DX.

        return asm;
    }

    //---UTILIDADES ARITMETICA---

    private static void actualizaReg(int nReg, boolean ocupado, int ref){
        InfoReg reg = registros.get(nReg);
        reg.setOcupado(ocupado);
        reg.setRef(ref);
    }

    private static String getInstrAritmDouble(String operador){
        switch (operador){
            case "*": return "FMUL";
            case "+": return "FADD";
            case "/": return "FDIV";
            case "-": return "FSUB";
        }
        return null;
    }

    //---UTILIDADES REGISTROS---

    private static List<String> liberaRegistro(int idReg){
        List<String> asm = new ArrayList<>();

        if (registros.get(idReg).isNotOcupado()) return asm;
        else {
            int regLibre = 0;
            while (regLibre < registros.size() && !registros.get(regLibre).isNotOcupado()) regLibre++; //Encuentra 1 reg libre.

            int refAnterior = registros.get(AX).getRef();
            String valorNuevaRef;
            if (regLibre == registros.size()) { //No hay regs libres.
                valorNuevaRef = "@aux" + variableAux;
                variableAux++;
            }
            else {
                valorNuevaRef = getNombreRegistro(regLibre);
                registros.get(regLibre).setOcupado(true); //Marco el registro al cual movi como ocupado.
            }
            pilaOps.set(refAnterior,valorNuevaRef); //Actualizo el operando que estaba en la pila.
            asm.add("MOV "+valorNuevaRef+", AX"); //Copio lo que estaba en AX en su nuevo lugar.
        }
        return asm;
    }

    private static int getRegistroLibre(){
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

    private static int getIdRegistro(String nombre){
        switch (nombre){
            case "AX": return 0;
            case "BX": return 1;
            case "CX": return 2;
            case "DX": return 3;
        }
        return -1;
    }

    private static String getNombreRegistro(int id){
        switch (id){
            case 0: return "AX";
            case 1: return "BX";
            case 2: return "CX";
            case 3: return "DX";
        }
        return "";
    }

    private static void marcaRegLiberado(String reg) {
        registros.get(getIdRegistro(reg)).setOcupado(false);
        registros.get(getIdRegistro(reg)).setRef(-1);
    }

    //---OTRAS UTILIDADES---

    private static String getPrefijoDouble(String op, boolean isCteDouble){
        if (isCteDouble) return "_";
        return getPrefijo(op);
    }

    private static String getPrefijo(String op){
        if ((tablaS.getUso(op).equals("Variable") || tablaS.getUso(op).equals("CTE"))
                && !op.startsWith("@")) return "_";

        return "";
    }

    private static boolean tiposOperandosValidos(String op1, boolean esRegOp1, String op2, boolean esRegOp2){
        String tipoOp1, tipoOp2;

        if (esRegOp1) tipoOp1 = registros.get(getIdRegistro(op1)).getTipo();
        else tipoOp1 = tablaS.getTipo(op1);

        if (esRegOp2) tipoOp2 = registros.get(getIdRegistro(op2)).getTipo();
        else tipoOp2 = tablaS.getTipo(op2);

        //Tipos validos.
        if (tipoOp1.equals(tipoOp2)) return true;

        //Tipos invalidos.
        TablaNotificaciones.agregarError(0, "Los operandos '" + op1 + "' y '" + op2 + "' no tienen tipos compatibles.");
        throw new IllegalStateException("El codigo tiene errores, cortando generacion asm.");
    }
}
