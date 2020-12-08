package generacion_asm.util;

import generacion_asm.GeneradorAssembler;
import util.TablaNotificaciones;
import util.tabla_simbolos.Celda;
import util.tabla_simbolos.TablaSimbolos;

import java.util.ArrayList;
import java.util.List;

public class UtilsRegistro {
    public static int AX = 0;
    public static int BX = 1;
    public static int CX = 2;
    public static int DX = 3;

    public static List<InfoReg> init(){
        List<InfoReg> registros = new ArrayList<>();
        registros.add(new InfoReg());
        registros.add(new InfoReg());
        registros.add(new InfoReg());
        registros.add(new InfoReg());

        return registros;
    }

    public static int getRegistroLibre(List<InfoReg> registros) {
        //Compruebo primero que esten libres los registros que no se usan en MUL y DIV.
        if (registros.get(BX).isNotOcupado()) return BX;
        if (registros.get(CX).isNotOcupado()) return CX;
        if (registros.get(AX).isNotOcupado()) return AX;
        if (registros.get(DX).isNotOcupado()) return DX;
        return -1;
    }

    public static List<String> liberaRegistro(List<InfoReg> registros, int idReg) {
        List<String> asm = new ArrayList<>();

        if (registros.get(idReg).isNotOcupado()) return asm;
        else {
            int regLibre = 0;
            while (regLibre < registros.size() && !registros.get(regLibre).isNotOcupado())
                regLibre++; //Encuentra 1 reg libre.

            int refAnterior = registros.get(AX).getRef();
            String valorNuevaRef;
            if (regLibre == registros.size()) { //No hay regs libres.
                valorNuevaRef = "@aux" + GeneradorAssembler.getVariableAux();
                GeneradorAssembler.incrementaVarAux();
            } else {
                valorNuevaRef = getNombreRegistro(regLibre);
                registros.get(regLibre).setOcupado(true); //Marco el registro al cual movi como ocupado.
            }
            GeneradorAssembler.cambiaElementoPila(refAnterior, valorNuevaRef); //Actualizo el op que estaba en la pila.
            asm.add("MOV " + valorNuevaRef + ", AX"); //Copio lo que estaba en AX en su nuevo lugar.
        }
        return asm;
    }

    public static boolean esRegistro(String operando) {
        return operando.charAt(0) == 'A'
                || operando.charAt(0) == 'B'
                || operando.charAt(0) == 'C'
                || operando.charAt(0) == 'D';
    }

    public static String getNombreRegistro(int id) {
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

    //OTROS

    public static String getPrefijo(TablaSimbolos tablaS, String op) {
        if (esRegistro(op)) return ""; //Regs
        if (op.startsWith("@")) return ""; //Auxs
        if (tablaS.esEntradaCte(op) && tablaS.getTipoEntrada(op).equals(Celda.TIPO_UINT)) return ""; //Ctes uint.

        return "_"; //Ctes double o vars.
    }

    public static boolean tiposOperandosValidos(TablaSimbolos tablaS, List<InfoReg> registros,
                                                String op1, boolean esRegOp1, String op2, boolean esRegOp2) {

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
