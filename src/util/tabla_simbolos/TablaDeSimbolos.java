package util.tabla_simbolos;

import java.util.Hashtable;

public class TablaDeSimbolos {
    private Hashtable<String, Celda> tablaSimb;

    public TablaDeSimbolos(){
        tablaSimb = new Hashtable<>();
    }

    /**
     * Agrega una celda (token,lexema,tipo). En caso de existir previamente, retorna el value anterior.
     * @param celda
     * @return
     */
    public Celda agregar(Celda celda){
        if (!tablaSimb.containsKey(celda.getLexema()))
            tablaSimb.put(celda.getLexema(), celda);
        return getValor(celda.getLexema());
    }

    /**
     * Elimina una celda dado el token
     * @param lexema
     * @return
     */
    public boolean eliminar(String lexema){
        if (tablaSimb.containsKey(lexema)){
            tablaSimb.remove(lexema);
            return true;
        }
        return false;
    }

    /**
     * Dado un lexema, devuelve la celda en la tabla de simbolos.
     * @param lexema
     * @return Celda o null en caso de no existir.
     */
    public Celda getValor(String lexema){
        Celda celda = tablaSimb.get(lexema);

        if (celda == null) //Agrege la excepcion por si llega a fallar el get, que no ande el null dando vueltas. (Bruno)
            throw new IllegalStateException("El lexema '"+lexema+"' no se encontro en la tabla de simbolos.");
        return celda;
    }
}