package analizador_lexico;

import analizador_lexico.maquina_estados.MaquinaEstados;

import java.util.ArrayList;
import java.util.List;

public class AnalizadorLexico {
    public List<Object> generaListaToken(){
        FileProcessor fileProcessor = new FileProcessor();

        CodigoFuente cFuente = new CodigoFuente(fileProcessor.getLineas("archivos/codigo_fuente.txt"));

        MaquinaEstados maquinaEstados = new MaquinaEstados(fileProcessor,cFuente,null,null);

        while (!cFuente.eof()) {
            maquinaEstados.transicionar(cFuente.simboloActual());
            cFuente.avanzar();
        }

        return maquinaEstados.getListaToken();
    }
}
