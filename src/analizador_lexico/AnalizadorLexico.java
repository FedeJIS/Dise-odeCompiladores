package analizador_lexico;

import analizador_lexico.maquina_estados.MaquinaEstados;
import util.CodigoFuente;
import util.FileProcessor;
import util.Reservado;
import util.tabla_simbolos.TablaDeSimbolos;

public class AnalizadorLexico {
    private final CodigoFuente cFuente;
    private final MaquinaEstados maquinaEstados;

    /**
     * Valores para tokens (TODO REEMPLAZAR POR LOS VALORES QUE DA YACC).
     */
    public static final int T_EOF = 0, T_ID = 1, T_COMP_MENOR_IGUAL = 2, T_COMP_MAYOR_IGUAL = 3, T_COM_DISTINTO = 4,
            T_COMP_IGUAL = 5, T_UINT = 6, T_DOUBLE = 7, T_CADENA = 8, T_IF = 9, T_THEN = 10, T_ELSE = 11, T_END_IF = 12,
            T_LOOP = 13, T_UNTIL = 14, T_OUT = 15, T_PROC = 16, T_VAR = 17, T_NI = 18, T_CTE_UINT = 19, T_CTE_DOUBLE = 20;

    public int ultimoTokenGenerado = -1; //TODO Reemplazar por nombre requerido por el parser.
    public String ultimoLexemaGenerado; //TODO Reemplazar por nombre requerido por el parser.

    public AnalizadorLexico(FileProcessor fileProcessor, CodigoFuente cFuente, TablaDeSimbolos tablaS){
        this.cFuente = cFuente;
        this.maquinaEstados = new MaquinaEstados(this,fileProcessor,cFuente,tablaS, inicTPR());
    }

    public void setVariablesSintactico(int token, String lexema){
        this.ultimoTokenGenerado = token;
        this.ultimoLexemaGenerado = lexema;
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
