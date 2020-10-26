package util.tabla_simbolos;

public class Celda {
    private final int token;
    private String lexema;
    private String tipo;
    private String uso;
    private int nInvoc, maxInvoc;
    private boolean declarada;
    private int referencias;

    public Celda(int token, String lexema, String tipo) {
        this.token = token;
        this.lexema = lexema;
        this.tipo = tipo;
        this.referencias = 0;
    }

    public void setAmbito(String ambito){
        lexema = ambito+"::"+lexema;
    }

    public String getLexema() {
        return lexema;
    }

    public int getToken() {
        return token;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getUso() {
        return uso;
    }

    public void setUso(String uso) {
        this.uso = uso;
    }

    public boolean isDeclarada(){
        return declarada;
    }

    public void setDeclarada(boolean declarada) {
        this.declarada = declarada;
    }

    public void incrementaNInvoc(){
        nInvoc++;
    }

    public void setMaxInvoc(int maxInvoc) {
        this.maxInvoc = maxInvoc;
    }

    public boolean maxInvocAlcanzadas(){
        return nInvoc == maxInvoc;
    }

    public void actualizarReferencias(int i) {
        referencias += i;
    }

    public boolean sinReferencias() {
        return referencias == 0;
    }

    @Override
    public String toString() {
        return "{" +
                "token=" + token +
                ", lexema='" + lexema + '\'' +
                ", tipo='" + tipo + '\'' +
                ", uso='" + uso + '\'' +
                ", declarada='" + declarada + '\'' +
                ", nInvoc=" + nInvoc +
                ", referencias=" + referencias +
                '}';
    }
}
