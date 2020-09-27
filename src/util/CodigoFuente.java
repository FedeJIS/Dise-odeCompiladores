package util;

import java.util.List;

public class CodigoFuente {
    private final String simbolos;

    private int posicionActual = 0;

    public CodigoFuente(List<String> lineas) {
        StringBuilder builder = new StringBuilder();
        for (String linea : lineas)
            builder.append(linea);
        simbolos = builder.toString();
    }

    public void avanzar(){
        if (eof()) return; //Evita seguir avanzando cuando se esta en el eof.
        posicionActual++;
    }

    public void retroceder(){
        if (posicionActual == 0) return; //Evita seguir retrocediendo cuando se esta en el principio del archivo.
        posicionActual--;
    }

    public boolean eof(){
        return posicionActual == simbolos.length();
    }

    public char simboloActual(){
        return simbolos.charAt(posicionActual);
    }
}
