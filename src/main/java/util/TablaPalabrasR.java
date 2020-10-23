package util;

import java.util.HashMap;
import java.util.Map;

public class TablaPalabrasR {
    private static final Map<String, Integer> palabrasReserv = new HashMap<>();

    public static boolean esReservada(String palabra) {
        return palabrasReserv.containsKey(palabra);
    }

    public static int getToken(String palabra) {
        return palabrasReserv.get(palabra);
    }

    public static void agregar(String palabra, int token) {
        palabrasReserv.putIfAbsent(palabra,token);
    }

    public static void printPRs(){
        for (String pR : palabrasReserv.keySet())
            System.out.print(pR+",");
    }

    public static void clear() {
        palabrasReserv.clear();
    }
}
