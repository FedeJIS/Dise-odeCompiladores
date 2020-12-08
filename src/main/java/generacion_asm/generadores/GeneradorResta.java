package generacion_asm.generadores;

import generacion_asm.GeneradorAssembler;
import generacion_asm.util.InfoReg;
import util.tabla_simbolos.TablaSimbolos;

import java.util.ArrayList;
import java.util.List;

import static generacion_asm.util.UtilsRegistro.*;

public class GeneradorResta {
    /**
     * Fuente: A - B
     * Polaca: A,B,b
     * opDer = B, opIzq = A
     */
    public static List<String> genInstrAritmResta(TablaSimbolos tablaS, List<InfoReg> registros,
                                                  String opDer, String opIzq) {
        tiposOperandosValidos(tablaS, registros, opIzq, esRegistro(opIzq), opDer, esRegistro(opDer));

        List<String> asm = new ArrayList<>();

        //Variable & Variable
        if (!esRegistro(opIzq) && !esRegistro(opDer))
            if (tablaS.getTipoEntrada(opDer).equals("DOUBLE"))
                return GeneradorAritmDouble.genInstrAritmDouble(tablaS, "-", opIzq, opDer);
            else {
                int reg = getRegistroLibre(registros); //Obtengo reg libre.
                asm.add("MOV " + getNombreRegistro(reg) + ", " + getPrefijo(tablaS, opIzq) + opIzq);
                asm.add("SUB " + getNombreRegistro(reg) + ", " + getPrefijo(tablaS, opDer) + opDer);

                //Check resta negativa.
                asm.add("CMP " + getNombreRegistro(reg) + ", 0");
                asm.add("JLE L_resta_neg");

                GeneradorAssembler.agregaElementoPila(getNombreRegistro(reg));
                actualizaReg(registros, reg, true, GeneradorAssembler.getLongitudPila());
            }

        //Reg & Variable
        if (esRegistro(opIzq) && !esRegistro(opDer)) {
            asm.add("SUB " + opIzq + ", " + getPrefijo(tablaS, opDer) + opDer); //opIzq es el registro.

            //Check resta negativa.
            asm.add("CMP " + opIzq + ", 0");
            asm.add("JLE L_resta_neg");

            GeneradorAssembler.agregaElementoPila(opIzq);
        }

        //Reg & Reg
        if (esRegistro(opIzq) && esRegistro(opDer)) {
            asm.add("SUB " + opIzq + ", " + opDer);

            //Check resta negativa.
            asm.add("CMP " + opIzq + ", 0");
            asm.add("JLE L_resta_neg");

            GeneradorAssembler.agregaElementoPila(opIzq);
            marcaRegLiberado(registros, opDer);
        }

        //Variable & Reg. No puedo restar sobre opDer porque la op es conmut.
        if (!esRegistro(opIzq) && esRegistro(opDer)) {
            int reg = getRegistroLibre(registros);
            asm.add("MOV " + getNombreRegistro(reg) + ", " + getPrefijo(tablaS, opIzq) + opIzq); //Muevo opIzq a registro libre.
            asm.add("SUB " + getNombreRegistro(reg) + ", " + opDer); //Resto sobre el nuevo reg.

            //Check resta negativa.
            asm.add("CMP " + getNombreRegistro(reg) + ", 0");
            asm.add("JLE L_resta_neg");

            GeneradorAssembler.agregaElementoPila(getNombreRegistro(reg));
            actualizaReg(registros, reg, true, GeneradorAssembler.getLongitudPila());
            actualizaReg(registros, getIdRegistro(opDer), false, -1);
        }

        return asm;
    }
}
