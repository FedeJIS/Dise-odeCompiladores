package util.tabla_simbolos;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class TablaSimbolos {
    private final Map<String, Celda> tablaSimb;

    public TablaSimbolos() {
        tablaSimb = new Hashtable<>();
    }

    //---METODOS GENERALES---

    public String toString() {
        if (tablaSimb.isEmpty()) return "Tabla de simbolos vacia.";
        StringBuilder builder = new StringBuilder();
        for (Celda c : tablaSimb.values())
            builder.append(c.toString()).append('\n');
        return builder.toString();
    }

    public String toAsm(){
        StringBuilder asmBuilder = new StringBuilder();

        for (String lexema : tablaSimb.keySet()) {
            Celda celda = tablaSimb.get(lexema);

            if (lexema.startsWith("PROGRAM")) asmBuilder.append('_');
            //Variable no auxiliar entera.
            if ((lexema.startsWith("PROGRAM") || lexema.startsWith("@"))
                    && celda.getTipo().equals("UINT")){
                asmBuilder.append(lexema) //Nombre variable.
                        .append(" DW ") //Tipo.
                        .append(0) //Valor.
                        .append('\n');
            }
            //Variable no auxiliar double.
            else if ((lexema.startsWith("PROGRAM") || lexema.startsWith("@"))
                    && celda.getTipo().equals("DOUBLE")){
                asmBuilder.append(lexema) //Nombre variable.
                        .append(" DQ ") //Tipo.
                        .append(0) //Valor.
                        .append('\n');
            }
            //Constante double
            else if (celda.esCte() && celda.getTipo().equals("DOUBLE")){
                asmBuilder.append('_').append(formatDouble(lexema))
                        .append(" DQ ") //Tipo.
                        .append(lexema) //Valor.
                        .append('\n');
            }
            //Cadena caracteres
            else if (lexema.startsWith("\"") && lexema.endsWith("\"")){
                asmBuilder.append("_CAD_")
                        .append(lexema, 1, lexema.length() - 1)
                        .append(" DB ")
                        .append("'").append(lexema, 1, lexema.length() - 1).append("'") //Cadena.
                        .append(", 0")
                        .append('\n');
            }
        }

        return asmBuilder.toString();
    }

    public static String formatDouble(String doubleS){
        String nDouble = doubleS.replace('-','n');
        return nDouble.replace('.','p');
    }

    public void clear() {
        tablaSimb.clear();
    }

    //---INSERCION, ELIMINACION Y OBTENCION DE ENTRADAS---

    public boolean contieneLexema(String lexema) {
        return tablaSimb.containsKey(lexema);
    }

    /**
     * Agrega una nueva entrada solo si no existe en la tabla.
     */
    public void agregarEntrada(Celda entrada){
        if (tablaSimb.containsKey(entrada.getLexema()))
            throw new IllegalStateException("Ya hay una entrada con el lexema '"+entrada.getLexema()+"'.");
        tablaSimb.put(entrada.getLexema(), entrada);
        entrada.actualizarReferencias(1);
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

    public Celda getEntrada(String lexema) {
        Celda celda = tablaSimb.get(lexema);

        if (celda == null) //Agrege la excepcion por si llega a fallar el get, que no ande el null dando vueltas.
            throw new IllegalStateException("El lexema '" + lexema + "' no se encontro en la tabla de simbolos.");
        return celda;
    }

    //---CONSULTAS DATOS ENTRADAS---

    public boolean entradaSinReferencias(String lexema) {
        return getEntrada(lexema).sinReferencias();
    }

    public void quitarReferencia(String lexema) {
        Celda celda = tablaSimb.get(lexema);
        celda.actualizarReferencias(-1); //refs--
        if (celda.sinReferencias()) tablaSimb.remove(celda.getLexema());
    }

    public boolean esEntradaCte(String lexema) {
        Celda entrada = tablaSimb.get(lexema);
        if (entrada == null) throw new IllegalStateException("Lexema del parametro no encontrado en la TS");
        return entrada.esCte();
    }

    public String getTipoEntrada(String lexema){
        Celda entrada = tablaSimb.get(lexema);
        return entrada.getTipo();
    }

    public void setTipoEntrada(String lexema, String tipo){
        Celda entrada = tablaSimb.get(lexema);
        if (entrada == null) throw new IllegalStateException("Lexema no encontrado en la TS");
        entrada.setTipo(tipo);
    }

    public String getUsoEntrada(String lexema) {
        Celda entrada = tablaSimb.get(lexema);
        if (entrada == null) throw new IllegalStateException("Lexema no encontrado en la TS");
        return entrada.getUso();
    }

    public void setUsoEntrada(String lexema, String uso){
        Celda entrada = tablaSimb.get(lexema);
        if (entrada == null) throw new IllegalStateException("Lexema no encontrado en la TS");
        entrada.setUso(uso);
    }

    public void setAmbitoEntrada(String lexema, String ambito){
        Celda entrada = tablaSimb.remove(lexema);
        if (entrada == null) throw new IllegalStateException("Lexema no encontrado en la TS");
        entrada.setAmbito(ambito);
        tablaSimb.put(entrada.getLexema(),entrada);
    }

    public boolean isEntradaDeclarada(String lexema){
        return tablaSimb.get(lexema) != null
                && tablaSimb.get(lexema).isDeclarada();
    }

    public void setDeclaracionEntrada(String lexema, boolean declarada){
        Celda entrada = tablaSimb.get(lexema);
        entrada.setDeclarada(declarada);
    }

    //---METODOS PROCEDIMIENTOS---

    public boolean isEntradaProc(String lexema){
        Celda entrada = tablaSimb.get(lexema);
        if (entrada == null) throw new IllegalStateException("Lexema no encontrado en la TS");
        return entrada.isProc();
    }

    public boolean isEntradaParamCVR(String lexema){
        Celda entrada = tablaSimb.get(lexema);
        if (entrada == null) throw new IllegalStateException("Lexema no encontrado en la TS");
        return entrada.isParamCVR();
    }

    public void setMaxInvoc(String lexema, int nMax){
        Celda entrada = tablaSimb.get(lexema);
        if (entrada == null) throw new IllegalStateException("Lexema '"+lexema+"' no encontrado en la TS");
        entrada.setMaxInvoc(nMax);
    }

    public boolean maxInvocAlcanzadas(String lexema){
        Celda entrada = tablaSimb.get(lexema);
        if (entrada == null) throw new IllegalStateException("Lexema no encontrado en la TS");
        return entrada.maxInvocAlcanzadas();
    }

    public void incrementaNInvoc(String lexema){
        Celda entrada = tablaSimb.get(lexema);
        if (entrada == null) throw new IllegalStateException("Lexema no encontrado en la TS");
        entrada.incrementaNInvoc();
    }

    public void setParamsProc(String lexema, List<String> paramsProc){
        Celda entrada = tablaSimb.get(lexema);
        if (entrada == null) throw new IllegalStateException("Lexema no encontrado en la TS");
        entrada.setParamsDecl(paramsProc);
    }

    public int getNParams(String lexema){
        Celda entrada = tablaSimb.get(lexema);
        if (entrada == null) throw new IllegalStateException("Lexema no encontrado en la TS");
        return entrada.getNParams();
    }

    public String getParam(String lexema, int i){
        Celda entrada = tablaSimb.get(lexema);
        if (entrada == null) throw new IllegalStateException("Lexema no encontrado en la TS");

        Celda entradaParam = tablaSimb.get(entrada.getParam(i));
        if (entradaParam == null)
            throw new IllegalStateException("Lexema del parametro '"+entrada.getParam(i)+"'no encontrado en la TS");

        return entradaParam.getLexema();
    }

    public String getTipoParam(String lexema, int i){
        Celda entrada = tablaSimb.get(lexema);
        if (entrada == null) throw new IllegalStateException("Lexema no encontrado en la TS");

        Celda entradaParam = tablaSimb.get(entrada.getParam(i));
        if (entradaParam == null)
            throw new IllegalStateException("Lexema del parametro '"+entrada.getParam(i)+"'no encontrado en la TS");

        return entradaParam.getTipo();
    }
}
