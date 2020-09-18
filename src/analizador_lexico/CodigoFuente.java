package analizador_lexico;

import java.util.List;

public class CodigoFuente {
    private final String simbolos;

    private int posicionActual = 0;

    public CodigoFuente(List<String> lineas) {
        StringBuilder builder = new StringBuilder();
        for (String linea : lineas)
            builder.append(linea).append("\n"); //Agrega el salto de linea para que pueda ser leido por la maquina.
        simbolos = builder.toString();
    }

    public boolean avanzar(){
        if (eof()) return false; //Evita seguir avanzando cuando se esta en el eof.
        posicionActual++;
        return true;
    }

    public boolean retroceder(){
        if (posicionActual == 0) return false; //Evita seguir retrocediendo cuando se esta en el principio del archivo.
        posicionActual--;
        return true;
    }

    public boolean eof(){
        return posicionActual == simbolos.length();
    }

    public char simboloActual(){
        return simbolos.charAt(posicionActual);
    }
}
