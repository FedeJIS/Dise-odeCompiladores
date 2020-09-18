package analizador_lexico;

import analizador_lexico.maquina_estados.Input;
import analizador_lexico.maquina_estados.MaquinaEstados;

import java.util.ArrayList;
import java.util.List;

public class AnalizadorLexico {
    public List<Object> generaListaToken(){
        List<Object> listaToken = new ArrayList<>();

        MaquinaEstados maquinaEstados = new MaquinaEstados();
        FileProcessor fileProcessor = new FileProcessor();

        ArrayList<String> lineasCodigoFuente = fileProcessor.getLineas("archivos/codigo_fuente.txt");
        StringBuilder codigoFuente = new StringBuilder();
        for (String linea : lineasCodigoFuente) codigoFuente.append(linea);

        for (int i = 0; i < codigoFuente.length(); i++)
            maquinaEstados.transicionar(Input.charToInt(codigoFuente.charAt(i)));

        return listaToken;
    }


}
