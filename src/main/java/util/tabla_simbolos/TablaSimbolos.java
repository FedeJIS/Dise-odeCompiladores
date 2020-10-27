package util.tabla_simbolos;

import java.util.Hashtable;
import java.util.Map;

public class TablaSimbolos {
    private final Map<String, Celda> tablaSimb;

    public static final String USO_ENTRADA_PROC = "Proc";

    public TablaSimbolos() {
        tablaSimb = new Hashtable<>();
    }

    private String generaLexemaAmbito(String lexema, String ambito){
        return ambito+"::"+lexema; //Name mangling.
    }

    public boolean contieneLexema(String lexema, String ambito) {
        return tablaSimb.containsKey(generaLexemaAmbito(lexema,ambito));
    }

    public boolean isEntradaProc(String lexema, String ambito){
        return tablaSimb.get(generaLexemaAmbito(lexema,ambito)).getUso().equals(USO_ENTRADA_PROC);
    }

    public void setTipoEntrada(String lexema, String tipo){
        Celda entrada = tablaSimb.get(lexema);
        entrada.setTipo(tipo);
    }

    public void setUsoEntrada(String lexema, String uso){
        Celda entrada = tablaSimb.get(lexema);
        entrada.setUso(uso);
    }

    public void setAmbitoEntrada(String lexema, String ambito){
        Celda entrada = tablaSimb.remove(lexema);
        entrada.setAmbito(ambito);
        tablaSimb.put(entrada.getLexema(),entrada);
    }

    public void setDeclaracionEntrada(String lexema, boolean declarada){
        Celda entrada = tablaSimb.get(lexema);
        entrada.setDeclarada(declarada);
    }

    public boolean isEntradaDeclarada(String lexema, String ambito){
        return tablaSimb.get(generaLexemaAmbito(lexema,ambito)) != null
            && tablaSimb.get(generaLexemaAmbito(lexema, ambito)).isDeclarada();
    }

    public void setMaxInvoc(String lexema, int nMax){
        Celda entrada = tablaSimb.get(lexema);
        entrada.setMaxInvoc(nMax);
    }

    public boolean maxInvocAlcanzadas(String lexema, String ambito){
        Celda entrada = tablaSimb.get(generaLexemaAmbito(lexema,ambito));
        return entrada.maxInvocAlcanzadas();
    }

    public void incrementaNInvoc(String lexema, String ambito){
        Celda entrada = tablaSimb.get(generaLexemaAmbito(lexema,ambito));
        entrada.incrementaNInvoc();
    }

    /**
     * Agrega una celda (token,lexema,tipo). En caso de existir previamente, incrementa en uno las referencias a la
     * celda.
     */
    public void agregarEntrada(int token, String lexema, String tipo) {
        Celda celda;

        if (tablaSimb.containsKey(lexema)) celda = getEntrada(lexema); //Si el lexema existe extraigo la celda para actualizar las referencias
        else {//Si no existe creo una nueva y la inserto.
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
    public Celda getEntrada(String lexema) {
        Celda celda = tablaSimb.get(lexema);

        if (celda == null) //Agrege la excepcion por si llega a fallar el get, que no ande el null dando vueltas.
            throw new IllegalStateException("El lexema '" + lexema + "' no se encontro en la tabla de simbolos.");
        return celda;
    }

    public String toString() {
        if (tablaSimb.isEmpty()) return "Tabla de simbolos vacia.";
        StringBuilder builder = new StringBuilder();
        for (Celda c : tablaSimb.values())
            builder.append(c.toString()).append('\n');
        return builder.toString();
    }

    public boolean entradaSinReferencias(String lexema) {
        return getEntrada(lexema).sinReferencias();
    }

    public void quitarReferencia(String lexema) {
        tablaSimb.get(lexema).actualizarReferencias(-1); //refs--
    }
}
