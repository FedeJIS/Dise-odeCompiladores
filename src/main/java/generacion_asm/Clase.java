package generacion_asm;

import generacion_c_intermedio.MultiPolaca;
import generacion_c_intermedio.Polaca;

import java.util.ArrayList;
import java.util.List;

public class Clase {
    String ambitoActual;

    List<Boolean> registros = new ArrayList<>();

    List<String> asmProcs = new ArrayList<>();
    List<String> asm = new ArrayList<>();

    List<String> pila = new ArrayList<>();

    public Clase(){
        ambitoActual = "PROGRAM";
        generaAsmDeclProc(null); //TODO CAMBIAR
        registros.add(false);
        registros.add(false);
        registros.add(false);
        registros.add(false);
    }

    private void generaAsmDeclProc(MultiPolaca multiPolaca){
        for (String proc : multiPolaca.getNombreProcs()) {
            asmProcs.add("_" + proc + " PROC");
            asmProcs.addAll(generarAsm(multiPolaca.getPolaca(proc))); //Obtiene polaca del proc y la pasa a asm.
            asmProcs.add("RET");
            asmProcs.add("_" + proc + " ENDP");
        }

    }

    public List<String> generarAsm(Polaca polaca){
        List<String> asm = new ArrayList<>();
        String pasoAnt = "";
        for (String paso : polaca.getListaPasos()){
            switch (paso){
                case "INVOC": generaInstrInvoc(pasoAnt);
//                case "*": generaInstrConmutat(paso);
//                case "+": generaInstrConmutat(paso);
//                case "/": generaInstrNoConmutat(paso);
//                case "-": generaInstrNoConmutat(paso);
                default:
                    pila.add(paso);
            }
            pasoAnt = paso;
        }
        return asm;
    }

    private void generaInstrInvoc(String proc) {
        if (!ambitoActual.equals(proc)) {
            ambitoActual = proc; //Actualiza ambito actual
            asm.add("CALL _" + proc);
        }

    }

    /**
     * Genera instrucciones para op aritmeticas en siguientes casos:
     *  + Variable & Variable
     *  + Reg & Variable
     *  + Reg & Reg
     */
//    private void generaInstrucAritm(String paso, String op1, String op2){
//        //Variable & Variable
//        if (!esRegistro(op1.charAt(0))){
//            int reg = getRegistroLibre(); //Obtengo reg libre.
//            registros.set(reg,true); //Ocupo reg.
//            asm.add("MOV "+ getNombreRegistro(reg)+", "+op1);
//            asm.add(getInstruccionOp(paso)+" "+ getNombreRegistro(reg)+", "+op2);
//        }
//
//        //Reg & Variable
//        if (esRegistro(op1.charAt(0)) && !esRegistro(op2.charAt(0)))
//            asm.add(getInstruccionOp(paso) + " " + op1 + ", " + op2); //op1 es el registro.
//
//        //Reg & Reg
//        if (esRegistro(op1.charAt(0)) && esRegistro(op2.charAt(0))){
//            asm.add(getInstruccionOp(paso) + " " + op1 + ", " + op2);
//            registros.set(getIdRegistro(op2),false); //Desocupo reg.
//        }
//    }
//
//    private void generaInstrConmutat(String paso){
//        String op2 = pila.remove(pila.size() - 1);
//        String op1 = pila.remove(pila.size() - 1);
//
//        generaInstrucAritm(paso, op1, op2);
//
//        //Variable & Reg
//        asm.add(getInstruccionOp(paso) + " " + op2 + ", " + op1); //op2 es el registro.
//    }
//
//    private void generaInstrNoConmutat(String paso){
//        String op2 = pila.remove(pila.size() - 1);
//        String op1 = pila.remove(pila.size() - 1);
//
//        generaInstrucAritm(paso, op1, op2);
//
//        //Variable & Reg
//        int reg = getRegistroLibre(); //Obtengo reg libre.
//        registros.set(reg,true); //Ocupo reg.
//        asm.add("MOV "+getNombreRegistro(reg)+", "+op1); //op1 es la variable.
//        asm.add(getInstruccionOp(paso) + " " + getNombreRegistro(reg) + ", " + op1);
//        registros.set(getIdRegistro(op1),false); //Libero registro divisor.
//    }
//
//    private boolean esRegistro(char primerChar) {
//        return primerChar == 'A'
//                || primerChar == 'B'
//                || primerChar == 'C'
//                || primerChar == 'D';
//    }
//
//    private int getIdRegistro(String nombre){
//        switch (nombre){
//            case "AX": return 0;
//            case "BX": return 1;
//            case "CX": return 2;
//            case "DX": return 3;
//        }
//        return -1;
//    }
//
//    private String getNombreRegistro(int id){
//        switch (id){
//            case 0: return "AX";
//            case 1: return "BX";
//            case 2: return "CX";
//            case 3: return "DX";
//        }
//        return null;
//    }
//
//    private String getInstruccionOp(String operador){
//        switch (operador){
//            case "*": return "MUL";
//            case "+": return "ADD";
//            case "/": return "DIV";
//            case "-": return "SUB";
//        }
//        return null;
//    }
//
//    private int getRegistroLibre(String op){
//        int reg = 0;
//
//        if (op.equals("*") || op.equals("/")) { //Los registros utilizables para las operaciones son AX y DX.
//            int reg1 = getIdRegistro("AX");
//            int reg2 = getIdRegistro("DX");
//
//            if (registros.get(reg1))
//        }
//            if () //A D}
//
//        while (reg < registros.size() && !registros.get(reg))
//            reg++;
//        return reg;
//    }
}
