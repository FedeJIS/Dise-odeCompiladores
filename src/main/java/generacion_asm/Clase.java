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

        public Dupla(int ref, boolean ocupado) {
            this.ref = ref;
            this.ocupado = ocupado;
        }

        public int getRef() {
            return ref;
        }

        public void setRef(int ref) {
            this.ref = ref;
        }

        public boolean isOcupado() {
            return ocupado;
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

    List<Dupla> registros = new ArrayList<>();

    List<String> asmProcs = new ArrayList<>();
    List<String> asm = new ArrayList<>();

    List<String> pila = new ArrayList<>();

    private final TablaSimbolos tablaS;

    public Clase(TablaSimbolos tablaS){
        this.tablaS = tablaS;
        generaAsmDeclProc(null); //TODO CAMBIAR
        registros.add(new Dupla(0,false));
        registros.add(new Dupla(0,false));
        registros.add(new Dupla(0,false));
        registros.add(new Dupla(0,false));
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
        String pasoAntAnt = "";
        for (String paso : polaca.getListaPasos()){
            switch (paso){
                case "INVOC": generaInstrInvoc(pasoAnt);
                case "*":
                    generaInstrConmutat(paso);
                case "+": generaInstrConmutat(paso);
                case "/": generaInstrNoConmutat(paso);
                case "-": generaInstrNoConmutat(paso);
                default: {
                    pila.add(paso);
                    if (esRegistro(paso.charAt(0)))
                        registros.get(getIdRegistro(paso)).setRef(pila.size() - 1);
                }
            }
            pasoAnt = paso;
        }
        return asm;
    }

    private void generaInstrInvoc(String proc) {
        asm.add("CALL _" + proc);
    }

    private int getRegistroLibre(String op){
        //Multiplicacion
        if (op.equals("*")){
            if (!registros.get(AX).isOcupado()) registros.get(AX).setRef(pila.size());
            else {
                int i = 0;
                while (i < registros.size() && !registros.get(i).isOcupado()) i++; //Encontrar 1 reg libre.
                pila.set(registros.get(AX).getRef(),getNombreRegistro(i)); //Actualizo referencia a reg en la pila.
                registros.get(i).setOcupado(true); //Marco el registro al cual movi como ocupado.
                asm.add("MOV "+getNombreRegistro(i)+", "+getNombreRegistro(AX)); //MOV para actualizar registro.
            }
            return AX;
        }

        //Division.


        //Suma y resta. Compruebo primero que esten libres los registros que no se usan en MUL y DIV.
        if (!registros.get(BX).isOcupado()) return BX;
        if (!registros.get(CX).isOcupado()) return CX;
        if (!registros.get(AX).isOcupado()) return AX;
        return DX;
    }

    /**
     * Genera instrucciones para op aritmeticas en siguientes casos:
     *  + Variable & Variable
     *  + Reg & Variable
     *  + Reg & Reg
     */
    private void generaInstrucAritm(String operador, String op1, String op2){
        //Variable & Variable
        if (!esRegistro(op1.charAt(0)) && !esRegistro(op2.charAt(0))){
            if (!tablaS.getTipo(op1).equals(tablaS.getTipo(op2))){ //Tipos incompatibles.
                TablaNotificaciones.agregarError(0,"Los operadores '"+op1+"' y '"+op2+"' no tienen tipos compatibles.");
                return; //Corto ejecucion.
            }

            int reg = getRegistroLibre(operador); //Obtengo reg libre.
            registros.get(reg).setOcupado(true); //Seteo como ocupado el reg.
            registros.get(reg).setTipo(tablaS.getTipo(op1)); //Seteo el tipo del valor almacenado para usarlo mas adelante.
            asm.add("MOV "+ getNombreRegistro(reg)+", "+op1);
            asm.add(getInstruccionOp(operador)+" "+ getNombreRegistro(reg)+", "+op2);
        }

        //Reg & Variable
        if (esRegistro(op1.charAt(0)) && !esRegistro(op2.charAt(0))) {
            Dupla r = registros.get(getIdRegistro(op1));
            if (!r.getTipo().equals(tablaS.getTipo(op2))) { //Tipos incompatibles.
                TablaNotificaciones.agregarError(0, "Los operadores '" + op1 + "' y '" + op2 + "' no tienen tipos compatibles.");
                return; //Corto ejecucion.
            }
            asm.add(getInstruccionOp(operador) + " " + op1 + ", " + op2); //op1 es el registro.
        }

        //Reg & Reg
        if (esRegistro(op1.charAt(0)) && esRegistro(op2.charAt(0))){
            Dupla r1 = registros.get(getIdRegistro(op1));
            Dupla r2 = registros.get(getIdRegistro(op2));
            if (!r1.getTipo().equals(r2.getTipo())){ //Tipos incompatibles.
                TablaNotificaciones.agregarError(0,"Los operadores '"+op1+"' y '"+op2+"' no tienen tipos compatibles.");
                return; //Corto ejecucion.
            }
            asm.add(getInstruccionOp(operador) + " " + op1 + ", " + op2);
            registros.get(getIdRegistro(op2)).setOcupado(false); //Desocupo reg.
        }
    }

    private void generaInstrConmutat(String paso){
        String op2 = pila.remove(pila.size() - 1);
        String op1 = pila.remove(pila.size() - 1);

        generaInstrucAritm(paso, op1, op2);

        //Variable & Reg
        Dupla r = registros.get(getIdRegistro(op2));
        if (tablaS.getTipo(op1).equals(r.getTipo())){ //Tipos incompatibles.
            TablaNotificaciones.agregarError(0,"Los operadores '"+op1+"' y '"+op2+"' no tienen tipos compatibles.");
            return; //Corto ejecucion.
        }
        asm.add(getInstruccionOp(paso) + " " + op2 + ", " + op1); //op2 es el registro.
    }

    private void generaInstrNoConmutat(String paso){
        String op2 = pila.remove(pila.size() - 1);
        String op1 = pila.remove(pila.size() - 1);

        generaInstrucAritm(paso, op1, op2);

        //Variable & Reg
        Dupla r = registros.get(getIdRegistro(op2));
        if (tablaS.getTipo(op1).equals(r.getTipo())){ //Tipos incompatibles.
            TablaNotificaciones.agregarError(0,"Los operadores '"+op1+"' y '"+op2+"' no tienen tipos compatibles.");
            return; //Corto ejecucion.
        }

        int reg = getRegistroLibre(paso); //Obtengo reg libre.
        registros.get(reg).setOcupado(true); //Ocupo reg.
        asm.add("MOV "+getNombreRegistro(reg)+", "+op1); //Muevo variable a registro. op1 es la variable.
        asm.add(getInstruccionOp(paso) + " " + getNombreRegistro(reg) + ", " + op2);
        registros.get(getIdRegistro(op2)).setOcupado(false); //Libero segundo registro.
    }

    private boolean esRegistro(char primerChar) {
        return primerChar == 'A'
                || primerChar == 'B'
                || primerChar == 'C'
                || primerChar == 'D';
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
}
