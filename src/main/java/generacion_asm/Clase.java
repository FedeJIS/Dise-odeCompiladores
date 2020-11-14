package generacion_asm;

import analizador_sintactico.Parser;
import generacion_c_intermedio.MultiPolaca;
import generacion_c_intermedio.Polaca;
import util.TablaNotificaciones;
import util.tabla_simbolos.TablaSimbolos;

import java.util.ArrayList;
import java.util.List;

public class Clase {
    private class Dupla{
        private int ref;
        private boolean ocupado;
        private final String tipo;

        public Dupla() {
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

    private final String OP_SUMA = "+", OP_RESTA = "-", OP_MULT = "*", OP_DIV = "/";

    private final int AX = 0, BX = 1, CX = 2, DX = 3;

    private int variableAux = 0;

    private List<Dupla> registros = new ArrayList<>();

    private List<String> asmProcs = new ArrayList<>();
    private List<String> asm = new ArrayList<>();

    private List<String> pila = new ArrayList<>();

    private final TablaSimbolos tablaS;

    private void generaAsmDeclProc(MultiPolaca multiPolaca){
        for (String proc : multiPolaca.getNombreProcs()) {
            asmProcs.add("_" + proc + " PROC");
            asmProcs.addAll(generarAsm(multiPolaca.getPolaca(proc))); //Obtiene polaca del proc y la pasa a asm.
            asmProcs.add("RET");
            asmProcs.add("_" + proc + " ENDP");
        }
    }

    public Clase(TablaSimbolos tablaS, MultiPolaca multiPolaca){
        this.tablaS = tablaS;
        generaAsmDeclProc(multiPolaca); //TODO CAMBIAR
        registros.add(new Dupla());
        registros.add(new Dupla());
        registros.add(new Dupla());
        registros.add(new Dupla());
    }

    public List<String> generarAsm(Polaca polaca){
        List<String> asm = new ArrayList<>();
        String pasoAnt = "";
        String tipoComp = "";
        for (String paso : polaca.getListaPasos()){
            switch (paso){
                case "INVOC": asm.add("CALL _" + pasoAnt); //pasoAnt es el label del proc invocado.
                case "*":
                case "+": asm.addAll(generaInstrConmutat(paso));
                case "/":
                case "-": asm.addAll(generaInstrNoConmutat(paso));
                case "=": asm.addAll(generaInstrAsign());
                case "<":
                case ">":
                case "==":
                case "!=": {
                    asm.addAll(generaInstrComp(paso));
                    tipoComp = paso;
                }
                case "BI":
                case "BF": asm.addAll(generaInstrSalto(paso,pasoAnt,tipoComp));

                default: { //TODO Los labels caerian aca, hay que corregir eso.
                    pila.add(paso);
                    if (esRegistro(paso))
                        registros.get(getIdRegistro(paso)).setRef(pila.size() - 1);
                }
            }
            pasoAnt = paso;
        }
        return asm;
    }

    //---GENERACION INSTRUCCIONES JUMP---

    private List<String> generaInstrSalto(String tipoJump, String labelJump, String tipoComp) {
        List<String> asm = new ArrayList<>();

        if (tipoJump.equals("BI")) asm.add("JMP L"+labelJump);

        if (tipoJump.equals("BF"))
            switch (tipoComp){
                case "<": asm.add("JAE L"+labelJump); //Opuesto de '<' = '>='.
                case "<=": asm.add("JA L"+labelJump); //Opuesto de '<=' = '>'.
                case ">": asm.add("JBE L"+labelJump); //Opuesto de '>' = '<='.
                case ">=": asm.add("JB L"+labelJump); //Opuesto de '>=' = '<'.
                case "==": asm.add("JNE L"+labelJump); //Opuesto de '==' = '!='.
                case "!=": asm.add("JE L"+labelJump); //Opuesto de '!=' = '=='.
                default: throw new IllegalStateException("Simbolo "+tipoComp+" no reconocido como comparador");
            }

        return asm;
    }

    //---GENERACION INSTRUCCIONES COMPARACION---

    private List<String> generaInstrComp(String operador) {
        List<String> asm = new ArrayList<>();

        String op1 = pila.remove(pila.size() - 1); //Op mas a la derecha.
        String op2 = pila.remove(pila.size() - 1);

        //Primer operando es un valor inmediato.
        if (!esRegistro(op2) && tablaS.esCte(op2) && tiposOperandosValidos(op1,false,op2,false)) {
            String nuevoOp = getNombreRegistro(getRegistroLibre(operador,null));
            asm.add("MOV " + nuevoOp + ", " + op2);
            op2 = nuevoOp;
        }

        //Var & Var. Ambos operandos no pueden estar en memoria, tengo que traer uno a un reg.
        if (!esRegistro(op1) && !esRegistro(op2) && tiposOperandosValidos(op1,false,op2,false)){
            String nuevoOp = getNombreRegistro(getRegistroLibre(operador, null));
            asm.add("MOV "+nuevoOp+", "+op2);
            op2 = nuevoOp;
        }

        asm.add("CMP "+op2+", "+op1);
        return asm;
    }

    //---GENERACION INSTRUCCIONES ASIGNACION---

    private List<String> generaInstrAsign(){
        List<String> asm = new ArrayList<>();
        String dest = pila.remove(pila.size() - 1);
        String src = pila.remove(pila.size() - 1);

        //dest = Variable & src = Reg
        if (!esRegistro(dest) && esRegistro(src) && tiposOperandosValidos(dest, false, src, true)){
            asm.add("MOV _"+dest+", "+src);
            liberarReg(src);
        }

        //Variable & Variable
        if (!esRegistro(dest) && !esRegistro(src) && tiposOperandosValidos(dest,false,src,false)) {
            int reg = getRegistroLibre("=", null);
            asm.add("MOV " + reg + ", " + src);
            asm.add("MOV " + dest + ", " + reg);
        }

        return asm;
    }

    //---GENERACION INSTRUCCIONES ARITMETICAS---

    private List<String> generaInstrAritmDouble(String operador, String op1, String op2){
        List<String> asm = new ArrayList<>();

        if (!esRegistro(op1) && tablaS.esCte(op1)){ //Si el op1 es un valor inmediato primero lo cargo en memoria.
            asm.add("MOV @aux"+variableAux+", "+op1);
            op1 = "@aux"+variableAux;
            variableAux++;
        }

        if (!esRegistro(op2) && tablaS.esCte(op2)){ //Si el op2 es un valor inmediato primero lo cargo en memoria.
            asm.add("MOV @aux"+variableAux+", "+op2);
            op2 = "@aux"+variableAux;
            variableAux++;
        }

        asm.add("FLD "+op1); //Pongo op1 en la pila del coproc.
        asm.add("FLD "+op2); //Pongo op2 en la pila del coproc.
        asm.add(getInstruccionOp(operador,"DOUBLE")); //Hago op en la pila del coproc.
        asm.add("FSTP @aux"+variableAux); //Muevo resultado a mem.
        pila.add("@aux"+variableAux); //Agrego operando a pila.
        tablaS.agregarEntrada(Parser.ID,"@aux"+variableAux,"DOUBLE"); //Agreo vaux a TS.

        variableAux++;

        return asm;
    }

    private List<String> generaInstrAritmSuma(String op1, String op2){
        List<String> asm = new ArrayList<>();

        //Variable & Variable
        if (!esRegistro(op1) && !esRegistro(op2) && tiposOperandosValidos(op1,false,op2,false))
            if (tablaS.getTipo(op1).equals("DOUBLE")) return generaInstrAritmDouble(OP_SUMA, op1, op2);
            else {
                int reg = getRegistroLibre(OP_SUMA, asm); //Obtengo reg libre.
                asm.add("MOV " + getNombreRegistro(reg) + ", _" + op1);
                asm.add("ADD " + getNombreRegistro(reg) + ", " + op2);
                pila.add(getNombreRegistro(reg));
                actualizaReg(reg, true, pila.size()-1);
            }

        //Reg & Variable
        if (esRegistro(op1) && !esRegistro(op2) && tiposOperandosValidos(op1,true,op2,false))
            asm.add("ADD "+ op1 + ", _" + op2); //op1 es el registro.

        //Reg & Reg
        if (esRegistro(op1) && esRegistro(op2) && tiposOperandosValidos(op1,true,op2,true)) {
            asm.add("ADD " + op1 + ", " + op2);
            liberarReg(op2);
        }

        //Variable & Reg
        if (!esRegistro(op1) && esRegistro(op2) && tiposOperandosValidos(op1,false,op2,true))
            asm.add("ADD "+op2 + ", _" + op1); //op2 es el registro.

        return asm;
    }

    private List<String> generaInstrAritmMult(String dest, String src){
        List<String> asm = new ArrayList<>();

        //Variable & Variable
        if (!esRegistro(dest) && !esRegistro(src) && tiposOperandosValidos(dest,false,src,false))
            if (tablaS.getTipo(dest).equals("DOUBLE")) return generaInstrAritmDouble("*", dest, src);
            else {
                liberaRegAX(); //Se encarga de liberar AX, y guardar su contenido previo si corresponde.
                asm.add("MOV AX, _" + dest);
                asm.add("MUL AX, _" + src);

                pila.add("AX");
                actualizaReg(AX,true,pila.size()-1);
            }

        //Reg & Variable. Reg destino tiene que ser AX.
        if (esRegistro(dest) && !esRegistro(src) && tiposOperandosValidos(dest,true,src,false))
            if (dest.equals("AX")) asm.add("MUL AX, _" + src);
            else { //Tengo que mover el dest a AX.
                liberaRegAX();
                asm.add("MOV AX, _" + dest);
                asm.add("MUL AX, _"+src);

                pila.add("AX");
                actualizaReg(AX,true,pila.size()-1);
                actualizaReg(getIdRegistro(dest),false,-1);
            }

        //Reg & Reg. Reg destino tiene que ser AX.
        if (esRegistro(dest) && esRegistro(src) && tiposOperandosValidos(dest,true,src,true))
            if (dest.equals("AX")) asm.add("MUL "+ dest + ", _" + src);
            else {
                liberaRegAX();
                asm.add("MOV AX, _" + dest);
                asm.add("MUL AX, _"+src);

                pila.add("AX");
                actualizaReg(AX,true,pila.size()-1);
                actualizaReg(getIdRegistro(dest),false,-1);
                actualizaReg(getIdRegistro(src),false,-1);
            }

        //Variable & Reg. Reg destino tiene que ser AX.
        if (!esRegistro(dest) && esRegistro(src) && tiposOperandosValidos(dest,false,src,true))
            if (src.equals("AX")) asm.add("MUL "+ src + ", _" + dest); //Puedo invertir los operandos por prop conmut.
            else {
                liberaRegAX();
                asm.add("MOV AX, _" + src);
                asm.add("MUL AX, _"+dest);

                pila.add("AX");
                actualizaReg(AX,true,pila.size()-1);
                actualizaReg(getIdRegistro(src),false,-1);
            }

        return asm;
    }

    /**
     * Genera instrucciones para op aritmeticas en siguientes casos:
     *  + Variable & Variable
     *  + Reg & Variable
     *  + Reg & Reg
     */
    private List<String> generaInstrucAritm(String operador, String op1, String op2){
        List<String> asm = new ArrayList<>();

        //Variable & Variable
        if (!esRegistro(op1) && !esRegistro(op2) && tiposOperandosValidos(op1,false,op2,false)){
            if (tablaS.getTipo(op1).equals("DOUBLE")) asm.addAll(generaInstrAritmDouble(operador,op1,op2));
            else {
                int reg = getRegistroLibre(operador, asm); //Obtengo reg libre.
                registros.get(reg).setOcupado(true); //Seteo como ocupado el reg.
                registros.get(reg).setTipo(tablaS.getTipo(op1)); //Seteo el tipo del valor almacenado para usarlo mas adelante.
                asm.add("MOV " + getNombreRegistro(reg) + ", " + op1);
                asm.add(getInstruccionOp(operador, "UINT") + " " + getNombreRegistro(reg) + ", " + op2);
                pila.add(getNombreRegistro(reg));
            }
        }

        //Reg & Variable
        if (esRegistro(op1) && !esRegistro(op2) && tiposOperandosValidos(op1,true,op2,false))
            if (tablaS.getTipo(op2).equals("DOUBLE")) asm.addAll(generaInstrAritmDouble(operador,op1,op2));
            else asm.add(getInstruccionOp(operador,"UINT") + " " + op1 + ", " + op2); //op1 es el registro.

        //Reg & Reg
        if (esRegistro(op1) && esRegistro(op2) && tiposOperandosValidos(op1,true,op2,true)){
            if (registros.get(getIdRegistro(op1)).getTipo().equals("DOUBLE")) asm.addAll(generaInstrAritmDouble(operador,op1,op2));
            else {
                asm.add(getInstruccionOp(operador, "UINT") + " " + op1 + ", " + op2);
                liberarReg(op2);
            }
        }

        return asm; //Si la lista esta vacia, ocurrio una incompatibilidad de tipos. Util para mas adelante.
    }

    private List<String> generaInstrConmutat(String paso){
        String op2 = pila.remove(pila.size() - 1);
        String op1 = pila.remove(pila.size() - 1);

        asm = generaInstrucAritm(paso, op1, op2);
        boolean cortarEjec = !asm.isEmpty(); //Si la lista tiene algun elemento hay que cortar ejec, pq el caso actual
                                            // fue contemplado por el metodo anterior.

        //Variable & Reg
        if (!cortarEjec && tiposOperandosValidos(op1,false,op2,true)) {
            if (tablaS.getTipo(op1).equals("UINT"))
                asm.add(getInstruccionOp(paso,"UINT") + " " + op2 + ", " + op1); //op2 es el registro.
        }

        return asm; //El hecho de que sea vacia debido a un error de tipos no causa ningun problema.
    }

    private List<String> generaInstrNoConmutat(String paso){
        String op2 = pila.remove(pila.size() - 1); //Operando mas a la derecha.
        String op1 = pila.remove(pila.size() - 1);

        List<String> asm = generaInstrucAritm(paso, op1, op2);
        boolean cortarEjec = !asm.isEmpty(); //Si la lista tiene algun elemento hay que cortar ejec, pq el caso actual
                                            // fue contemplado por el metodo anterior.

        //Variable & Reg
        if (paso.equals("-"))
            if (!cortarEjec && tiposOperandosValidos(op1,false,op2,true)){
                int reg = getRegistroLibre(paso, asm); //Obtengo reg libre.
                registros.get(reg).setOcupado(true); //Ocupo reg.
                asm.add("MOV " + getNombreRegistro(reg) + ", " + op1); //Muevo variable a registro. op1 es la variable.
                asm.add(getInstruccionOp(paso,"UINT") + " " + getNombreRegistro(reg) + ", " + op2);
                liberarReg(op2);
            }
        if (paso.equals("/"))
            if (!cortarEjec && tiposOperandosValidos(op1, false, op2, true)) {
                asm.add("MOV AX, _"+op1); //Dividendo.
                asm.add("MOV DX, 0"); //Resto.
                asm.add("DIV _"+op2); //Divisor.
                pila.add("AX");
            }

        return asm; //El hecho de que sea vacia debido a un error de tipos no causa ningun problema.
    }

    //---UTILIDADES ARITMETICA---

    private void actualizaReg(int nReg, boolean ocupado, int ref){
        Dupla reg = registros.get(nReg);
        reg.setOcupado(ocupado);
        reg.setRef(ref);
    }

    private String getInstruccionOp(String operador, String tipoOps){
        switch (operador){
            case "*": return tipoOps.equals("UINT") ? "MUL" : "FMUL";
            case "+": return tipoOps.equals("UINT") ? "ADD" : "FADD";
            case "/": return tipoOps.equals("UINT") ? "DIV" : "FDIV";
            case "-": return tipoOps.equals("UINT") ? "SUB" : "FSUB";
        }
        return null;
    }

    //---UTILIDADES REGISTROS---

    private boolean isRegDouble(String op1) {
        return registros.get(getIdRegistro(op1)).getTipo().equals("DOUBLE");
    }

    private int liberaRegAX(){
        if (registros.get(AX).isNotOcupado()) registros.get(AX).setRef(pila.size());
        else {
            int i = 0;
            while (i < registros.size() && registros.get(i).isNotOcupado()) i++; //Encontrar 1 reg libre.

            int refAnterior = registros.get(AX).getRef();
            String valorNuevaRef;
            if (i == registros.size()) { //No hay regs libres.
                valorNuevaRef = "@aux" + variableAux;
                variableAux++;
            }
            else {
                valorNuevaRef = getNombreRegistro(i);
                registros.get(i).setOcupado(true); //Marco el registro al cual movi como ocupado.
            }

            pila.set(refAnterior,valorNuevaRef); //Actualizo el operando que estaba en la pila.
            asm.add("MOV "+valorNuevaRef+", AX"); //Copio lo que estaba en AX en su nuevo lugar.
        }
        return AX;
    }

    private int getRegistroLibre(String operador, List<String> asm){
        //Multiplicacion. Tengo que devolver AX si o si.
        if (operador.equals("*")){
            if (registros.get(AX).isNotOcupado()) registros.get(AX).setRef(pila.size());
            else {
                int i = 0;
                while (i < registros.size() && registros.get(i).isNotOcupado()) i++; //Encontrar 1 reg libre.

                int refAnterior = registros.get(AX).getRef();
                String valorNuevaRef;
                if (i == registros.size()) { //No hay regs libres.
                    valorNuevaRef = "@aux" + variableAux;
                    variableAux++;
                }
                else {
                    valorNuevaRef = getNombreRegistro(i);
                    registros.get(i).setOcupado(true); //Marco el registro al cual movi como ocupado.
                }

                pila.set(refAnterior,valorNuevaRef); //Actualizo el operando que estaba en la pila.
                asm.add("MOV "+valorNuevaRef+", AX"); //Copio lo que estaba en AX en su nuevo lugar.
            }
            return AX;
        }

        //Division.
        if (operador.equals("/")){
            //Registro DX ocupado. Tengo que mover el contenido.
            Dupla regDX = registros.get(DX);
            if (!regDX.isNotOcupado()){
                int regLibre = getRegistroLibre("",asm);
                if (regLibre == -1) { //No hay registros libres. Muevo a variable aux.
                    pila.set(regDX.getRef(),"@aux"+variableAux);
                    asm.add("MOV "+"@aux"+variableAux+", "+getNombreRegistro(DX));
                    variableAux++;
                } else { //Hay un registro libre.
                    pila.set(regDX.getRef(),getNombreRegistro(regLibre));
                    asm.add("MOV "+getNombreRegistro(regLibre)+", "+getNombreRegistro(DX));
                }
            }

            //AX
            Dupla regAx = registros.get(AX);
            if (!regAx.isNotOcupado()){
                int regLibre = getRegistroLibre("",asm);
                if (regLibre == -1) { //No hay registros libres. Muevo a variable aux.
                    pila.set(regAx.getRef(),"@aux"+variableAux);
                    asm.add("MOV "+"@aux"+variableAux+", "+getNombreRegistro(AX));
                    variableAux++;
                } else { //Hay un registro libre.
                    pila.set(regAx.getRef(),getNombreRegistro(regLibre));
                    asm.add("MOV "+getNombreRegistro(regLibre)+", "+getNombreRegistro(AX));
                }
            }
        }

        //Suma y resta. Compruebo primero que esten libres los registros que no se usan en MUL y DIV.
        if (registros.get(BX).isNotOcupado()) return BX;
        if (registros.get(CX).isNotOcupado()) return CX;
        if (registros.get(AX).isNotOcupado()) return AX;
        if (registros.get(DX).isNotOcupado()) return DX;
        return -1;
    }

    private boolean esRegistro(String operando) {
        return operando.charAt(0) == 'A'
                || operando.charAt(0) == 'B'
                || operando.charAt(0) == 'C'
                || operando.charAt(0) == 'D';
    }

    private int getIdRegistro(String nombre){
        switch (nombre){
            case "AX": return 0;
            case "BX": return 1;
            case "CX": return 2;
            case "DX": return 3;
        }
        return -1;
    }

    private String getNombreRegistro(int id){
        switch (id){
            case 0: return "AX";
            case 1: return "BX";
            case 2: return "CX";
            case 3: return "DX";
        }
        return "";
    }

    private void liberarReg(String reg) {
        registros.get(getIdRegistro(reg)).setOcupado(false);
        registros.get(getIdRegistro(reg)).setRef(-1);
    }

    //---OTRAS UTILIDADES---

    private boolean tiposOperandosValidos(String op1, boolean esRegOp1, String op2, boolean esRegOp2){
        String tipoOp1, tipoOp2;

        if (esRegOp1) tipoOp1 = registros.get(getIdRegistro(op1)).getTipo();
        else tipoOp1 = tablaS.getTipo(op1);

        if (esRegOp2) tipoOp2 = registros.get(getIdRegistro(op2)).getTipo();
        else tipoOp2 = tablaS.getTipo(op2);

        //Tipos validos.
        if (tipoOp1.equals(tipoOp2)) return true;

        //Tipos invalidos.
        TablaNotificaciones.agregarError(0, "Los operandos '" + op1 + "' y '" + op2 + "' no tienen tipos compatibles.");
        return false;
    }
}
