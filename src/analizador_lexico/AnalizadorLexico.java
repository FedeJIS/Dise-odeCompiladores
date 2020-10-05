package analizador_lexico;

import analizador_lexico.maquina_estados.MaquinaEstados;
import analizador_sintactico.Parser;
import util.CodigoFuente;
import util.Reservado;
import util.tabla_simbolos.TablaSimbolos;

public class AnalizadorLexico {
    private final CodigoFuente cFuente;
    private final MaquinaEstados maquinaEstados;

    /**
     * Valores para tokens.
     */
    public static final int T_EOF = 0;
    public static final int T_ID = Parser.ID;
    public static final int T_COMP_MENOR_IGUAL = Parser.COMP_MENOR_IGUAL;
    public static final int T_COMP_MAYOR_IGUAL = Parser.COMP_MAYOR_IGUAL;
    public static final int T_COMP_DISTINTO = Parser.COMP_DISTINTO;
    public static final int T_COMP_IGUAL = Parser.COMP_IGUAL;
    public static final int T_UINT = Parser.UINT;
    public static final int T_DOUBLE = Parser.DOUBLE;
    public static final int T_CADENA = Parser.CADENA;
    public static final int T_IF = Parser.IF;
    public static final int T_THEN = Parser.THEN;
    public static final int T_ELSE = Parser.ELSE;
    public static final int T_END_IF = Parser.END_IF;
    public static final int T_LOOP = Parser.LOOP;
    public static final int T_UNTIL = Parser.UNTIL;
    public static final int T_OUT = Parser.OUT;
    public static final int T_PROC = Parser.PROC;
    public static final int T_VAR = Parser.VAR;
    public static final int T_NI = Parser.NI;
    public static final int T_CTE_UINT = Parser.CTE_UINT;
    public static final int T_CTE_DOUBLE = Parser.CTE_DOUBLE;

    public int ultimoTokenGenerado = -1;
    public String ultimoLexemaGenerado;

    public AnalizadorLexico(CodigoFuente cFuente, TablaSimbolos tablaS){
        this.cFuente = cFuente;
        this.maquinaEstados = new MaquinaEstados(this, cFuente,tablaS, inicTPR());
    }

    public void setVariablesSintactico(int token, String lexema){
        this.ultimoTokenGenerado = token;
        this.ultimoLexemaGenerado = lexema;
    }

    public int getLineaActual(){
        return maquinaEstados.getLineaActual();
    }

    private Reservado inicTPR(){
        Reservado tPR = new Reservado();
        tPR.agregar("UINT", T_UINT);
        tPR.agregar("DOUBLE", T_DOUBLE);
        tPR.agregar("IF", T_IF);
        tPR.agregar("THEN", T_THEN);
        tPR.agregar("ELSE", T_ELSE);
        tPR.agregar("END_IF", T_END_IF);
        tPR.agregar("LOOP", T_LOOP);
        tPR.agregar("UNTIL", T_UNTIL);
        tPR.agregar("OUT", T_OUT);
        tPR.agregar("PROC", T_PROC);
        tPR.agregar("VAR", T_VAR);
        tPR.agregar("NI", T_NI);

        return tPR;
    }

    public int produceToken(){
        while (!maquinaEstados.estadoFinalAlcanzado()){
            if (cFuente.eof()) {
                maquinaEstados.transicionarEOF();
            }
            else {
                maquinaEstados.transicionar(cFuente.simboloActual());
                cFuente.avanzar();
            }
        }
        maquinaEstados.reiniciar();
        return ultimoTokenGenerado;
    }
}
