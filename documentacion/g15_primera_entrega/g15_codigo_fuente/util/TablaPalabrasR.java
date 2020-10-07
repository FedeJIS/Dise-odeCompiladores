package util;

import java.util.HashMap;
import java.util.Map;

public class TablaPalabrasR {
    private final Map<String, Integer> palabrasReserv = new HashMap<>();

    public boolean esReservada(String palabra) {
        return palabrasReserv.containsKey(palabra);
    }

    public int getToken(String palabra) {
        return palabrasReserv.get(palabra);
    }

    public void agregar(String palabra, int token) {
        if (!esReservada(palabra)) palabrasReserv.put(palabra, token);
    }
}
