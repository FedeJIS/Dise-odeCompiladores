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
    private final List<Celda> listaToken = new ArrayList<>();

    public void agregaToken(Celda token){
        listaToken.add(token);
    }

    private Reservado inicTPR(){
        Reservado tPR = new Reservado();
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

    public void ejecutar(){
        FileProcessor fileProcessor = new FileProcessor();

        CodigoFuente cFuente = new CodigoFuente(fileProcessor.getLineas("archivos/codigo_fuente.txt"));

        TablaDeSimbolos tS = new TablaDeSimbolos();

        MaquinaEstados maquinaEstados = new MaquinaEstados(this, fileProcessor,cFuente,tS,inicTPR());

        while (!cFuente.eof()) {
            maquinaEstados.transicionar(cFuente.simboloActual());
            cFuente.avanzar();
        }
    }

    public List<Celda> getListaToken() {
        return listaToken;
    }
}
