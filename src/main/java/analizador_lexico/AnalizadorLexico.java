package analizador_lexico;

import analizador_lexico.maquina_estados.MaquinaEstados;
import analizador_sintactico.Parser;
import util.CodigoFuente;
import util.TablaPalabrasR;
import util.tabla_simbolos.TablaSimbolos;

import java.util.HashMap;
import java.util.Map;

public class AnalizadorLexico {
    private final CodigoFuente cFuente;
    private final MaquinaEstados maquinaEstados;

    /**
     * Valores para tokens.
     */
    public static final int T_EOF = 0;
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

    private TablaPalabrasR inicTPR(){

        return null;
    }

    public int produceToken(){
        while (!maquinaEstados.estadoFinalAlcanzado()){
            if (cFuente.eof()) maquinaEstados.transicionarEOF();
            else {
                maquinaEstados.transicionar(cFuente.simboloActual());
                cFuente.avanzar();
            }
        }
        maquinaEstados.reiniciar();
        return ultimoTokenGenerado;
    }
}
