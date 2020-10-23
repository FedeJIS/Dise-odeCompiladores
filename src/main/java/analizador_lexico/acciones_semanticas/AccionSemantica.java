package analizador_lexico.acciones_semanticas;

public abstract class AccionSemantica {
    private static final StringBuilder sTemporal = new StringBuilder();
    private static double baseNumDouble = Double.NEGATIVE_INFINITY; // Utilizado para AS-double (parte numerica y parte exp)

    public void inicString(){
        sTemporal.delete(0,sTemporal.length());
    }

    public void quitaChar(){
        if (sTemporal.length() == 0) throw new IllegalStateException("String temporal vacio. No hay elementos para eliminar.");
        sTemporal.delete(sTemporal.length()-1,sTemporal.length());
    }

    public void concatenaChar(char nuevoChar){
        sTemporal.append(nuevoChar);
    }

    public int longString(){
        return sTemporal.length();
    }

    public void truncaString(int limite){
        sTemporal.delete(limite,sTemporal.length()-1); //Elimina desde la posicion pasada como limite hasta el ultimo char almacenado.
    }

    public String getString(){
        return sTemporal.toString();
    }

    /**
     * Si baseNumDouble es distinto de infinito significa que ya se inicializo.
     */
    public boolean baseNumDoubleInicializado(){
        return baseNumDouble != Double.NEGATIVE_INFINITY;
    }

    public double getBaseNumDouble() {
        return baseNumDouble;
    }

    public void setBaseNumDouble(double valorBase){
        baseNumDouble = valorBase;
    }

    public abstract void ejecutar();
}
