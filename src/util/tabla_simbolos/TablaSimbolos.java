package util.tabla_simbolos;

import java.util.Hashtable;

public class TablaSimbolos {
    private final Hashtable<String, Celda> tablaSimb;

    public TablaSimbolos() {
        tablaSimb = new Hashtable<>();
    }

    public boolean contieneLexema(String lexema) {
        return tablaSimb.containsKey(lexema);
    }

    /**
     * Agrega una celda (token,lexema,tipo). En caso de existir previamente, incrementa en uno las referencias a la
     * celda.
     */
    public void agregarEntrada(int token, String lexema, String tipo) {
        Celda celda;

        if (tablaSimb.containsKey(lexema)) {
            celda = getValor(lexema);
        } else {
            celda = new Celda(token, lexema, tipo);
            tablaSimb.put(lexema, celda);
        }
        celda.actualizarReferencias(1);
    }

    public void eliminarEntrada(String lexema) {
        Celda celda = tablaSimb.get(lexema);
        if (celda == null)
            throw new IllegalStateException("El lexema '" + lexema + "' no se encontro en la tabla de simbolos.");
        if (!celda.sinReferencias()) //No deja eliminar celdas con referencias.
            throw new IllegalStateException("La entrada asociada al lexema '" + lexema + "' no puede ser eliminada porque" +
                    "aun esta siendo referenciada.");
        tablaSimb.remove(lexema);
    }

    /**
     * Dado un lexema, devuelve la celda en la tabla de simbolos.
     *
     * @return Celda asociada al lexema.
     */
    public Celda getValor(String lexema) {
        Celda celda = tablaSimb.get(lexema);

        if (celda == null) //Agrege la excepcion por si llega a fallar el get, que no ande el null dando vueltas.
            throw new IllegalStateException("El lexema '" + lexema + "' no se encontro en la tabla de simbolos.");
        return celda;
    }

    public void printAll() {
        if (tablaSimb.isEmpty()) System.out.println("Tabla de simbolos vacia.");
        for (Celda c : tablaSimb.values())
            System.out.println(c.toString());
    }

    public boolean entradaSinReferencias(String lexema) {
        return getValor(lexema).sinReferencias();
    }

    public void quitarReferencia(String lexema) {
        tablaSimb.get(lexema).actualizarReferencias(-1); //refs--
    }
}
