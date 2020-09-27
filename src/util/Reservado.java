package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class Reservado {
    private final Map<String, PalabraReservada> palabrasReserv = new HashMap<>();

    public boolean esReservada(String palabra){
        return palabrasReserv.containsKey(palabra);
    }

    public int getToken(String palabra){
        return palabrasReserv.get(palabra).getToken();
    }

    public boolean agregar(String palabra, int token){
        if (!esReservada(palabra)) {
            palabrasReserv.put(palabra,new PalabraReservada(palabra,token));
            return true;
        }
        return false;
    }

    private class PalabraReservada{
        private final String palabra;
        private final int token;

        public PalabraReservada(String palabra, int token) {
            this.palabra = palabra;
            this.token = token;
        }

        public int getToken() {
            return token;
        }
    }

}
