package analizador_lexico;

import analizador_lexico.maquina_estados.MaquinaEstados;
import util.CodigoFuente;
import util.FileProcessor;
import util.Reservado;
import util.tabla_simbolos.Celda;
import util.tabla_simbolos.TablaDeSimbolos;

import java.util.ArrayList;
import java.util.List;

public class AnalizadorLexico {
    private final FileProcessor fileProcessor;
    private final CodigoFuente cFuente;
    private final TablaDeSimbolos tablaS;
    private final Reservado tablaPR;
    private final MaquinaEstados maquinaEstados;

    public int token = -1; //TODO Reemplazar por nombre requerido por el parser.
    public String lexema; //TODO Reemplazar por nombre requerido por el parser.

    public AnalizadorLexico(FileProcessor fileProcessor, CodigoFuente cFuente, TablaDeSimbolos tablaS){
        this.fileProcessor = fileProcessor;
        this.cFuente = cFuente;
        this.tablaS = tablaS;
        this.tablaPR = inicTPR();
        this.maquinaEstados = new MaquinaEstados(this,fileProcessor,cFuente,tablaS,tablaPR);
    }

    public void setVariablesSintactico(int token, String lexema){
        this.token = token;
        this.lexema = lexema;
    }

    private Reservado inicTPR(){
        Reservado tPR = new Reservado();
        tPR.agregar("IF");
        tPR.agregar("THEN");
        tPR.agregar("ELSE");
        tPR.agregar("END_IF");
        tPR.agregar("OUT");
        tPR.agregar("FUNC");
        tPR.agregar("RETURN");
        tPR.agregar("LOOP");
        tPR.agregar("UNTIL");
        tPR.agregar("UINT");
        tPR.agregar("DOUBLE");

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
        return token;
    }
}
