package generacion_asm;

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
        private String tipo;

        public Dupla() {
            this.ref = -1;
            this.ocupado = false;
            this.tipo = "";
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

        public void setTipo(String tipo) {
            this.tipo = tipo;
        }
    }

    private final int AX = 0, BX = 1, CX = 2, DX = 3;

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
                    generaInstrComp(paso);
                    tipoComp = paso;
                }
                case "jump": asm.addAll(generaInstrSalto(paso,pasoAnt,tipoComp));

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

    private void generaInstrComp(String operador) {
        String op1 = pila.remove(pila.size() - 1);
        String op2 = pila.remove(pila.size() - 1);

        if (!esRegistro(op1) && !esRegistro(op2)){ //Ambos operandos no pueden estar en memoria, tengo que traer uno a un reg.
            asm.add("MOV ");
        }

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
            int reg = getRegistroLibre("=");
            asm.add("MOV " + reg + ", " + src);
            asm.add("MOV " + dest + ", " + reg);
        }

        return asm;
    }

    //---GENERACION INSTRUCCIONES ARITMETICAS---

    /**
     * Genera instrucciones para op aritmeticas en siguientes casos:
     *  + Variable & Variable
     *  + Reg & Variable
     *  + Reg & Reg
     */
    private List<String> generaInstrucAritm(String operador, String op1, String op2){
        List<String> asm = new ArrayList<>();

        //Variable & Variable
        if (!esRegistro(op1) && !esRegistro(op2))
            if (!tiposOperandosValidos(op1,false,op2,false)) //Tipos incompatibles.
                TablaNotificaciones.agregarError(0, "Los operadores '" + op1 + "' y '" + op2 + "' no tienen tipos compatibles.");
            else {
                int reg = getRegistroLibre(operador); //Obtengo reg libre.
                registros.get(reg).setOcupado(true); //Seteo como ocupado el reg.
                registros.get(reg).setTipo(tablaS.getTipo(op1)); //Seteo el tipo del valor almacenado para usarlo mas adelante.
                asm.add("MOV " + getNombreRegistro(reg) + ", " + op1);
                asm.add(getInstruccionOp(operador) + " " + getNombreRegistro(reg) + ", " + op2);
            }

        //Reg & Variable
        if (esRegistro(op1) && !esRegistro(op2))
            if (!tiposOperandosValidos(op1,true,op2,false)) //Tipos incompatibles.
                TablaNotificaciones.agregarError(0, "Los operadores '" + op1 + "' y '" + op2 + "' no tienen tipos compatibles.");
            else asm.add(getInstruccionOp(operador) + " " + op1 + ", " + op2); //op1 es el registro.

        //Reg & Reg
        if (esRegistro(op1) && esRegistro(op2))
            if (!tiposOperandosValidos(op1,true,op2,true)) //Tipos incompatibles.
                TablaNotificaciones.agregarError(0, "Los operadores '" + op1 + "' y '" + op2 + "' no tienen tipos compatibles.");
            else {
                asm.add(getInstruccionOp(operador) + " " + op1 + ", " + op2);
                liberarReg(op2);
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
        if (!cortarEjec && tiposOperandosValidos(op1,false,op2,true))
            asm.add(getInstruccionOp(paso) + " " + op2 + ", " + op1); //op2 es el registro.

        return asm; //El hecho de que sea vacia debido a un error de tipos no causa ningun problema.
    }

    private List<String> generaInstrNoConmutat(String paso){
        String op2 = pila.remove(pila.size() - 1);
        String op1 = pila.remove(pila.size() - 1);

        List<String> asm = generaInstrucAritm(paso, op1, op2);
        boolean cortarEjec = !asm.isEmpty(); //Si la lista tiene algun elemento hay que cortar ejec, pq el caso actual
                                            // fue contemplado por el metodo anterior.

        //Variable & Reg
        if (!cortarEjec && tiposOperandosValidos(op1,false,op2,true)){
            int reg = getRegistroLibre(paso); //Obtengo reg libre.
            registros.get(reg).setOcupado(true); //Ocupo reg.
            asm.add("MOV " + getNombreRegistro(reg) + ", " + op1); //Muevo variable a registro. op1 es la variable.
            asm.add(getInstruccionOp(paso) + " " + getNombreRegistro(reg) + ", " + op2);
            liberarReg(op2);
        }

        return asm; //El hecho de que sea vacia debido a un error de tipos no causa ningun problema.
    }

    //---UTILIDADES REGISTROS---

    private int getRegistroLibre(String op){
        //Multiplicacion
        if (op.equals("*")){
            if (registros.get(AX).isNotOcupado()) registros.get(AX).setRef(pila.size());
            else {
                int i = 0;
                while (i < registros.size() && registros.get(i).isNotOcupado()) i++; //Encontrar 1 reg libre.
                pila.set(registros.get(AX).getRef(),getNombreRegistro(i)); //Actualizo referencia a reg en la pila.
                registros.get(i).setOcupado(true); //Marco el registro al cual movi como ocupado.
                asm.add("MOV "+getNombreRegistro(i)+", "+getNombreRegistro(AX)); //MOV para actualizar registro.
            }
            return AX;
        }

        //Division.


        //Suma y resta. Compruebo primero que esten libres los registros que no se usan en MUL y DIV.
        if (registros.get(BX).isNotOcupado()) return BX;
        if (registros.get(CX).isNotOcupado()) return CX;
        if (registros.get(AX).isNotOcupado()) return AX;
        return DX;
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
        return null;
    }

    private String getInstruccionOp(String operador){
        switch (operador){
            case "*": return "MUL";
            case "+": return "ADD";
            case "/": return "DIV";
            case "-": return "SUB";
        }
        return null;
    }

    private void liberarReg(String reg) {
        registros.get(getIdRegistro(reg)).setTipo("");
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
