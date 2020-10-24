package util;

import java.util.HashMap;
import java.util.Map;

public class ReprTokens {
    /**
     * Almacena la representacion en String de ciertos tokens.
     */
    private static final Map<Short, String> representaciones = new HashMap<>();
    
    public static void clear(){
        representaciones.clear();
    }
    
    public static void add(short token, String representacion){
        representaciones.put(token,representacion);
    }
    
    public static String getRepresentacion(short token){
        String repr = representaciones.get(token);
        if (repr == null) throw new IllegalStateException("No existe el token solicitado.");
        return repr;
    }
}
