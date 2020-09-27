package util.tabla_simbolos;

public class Celda {
    private final int token;
    private final String lexema;
    private final String tipo;

    public Celda(int token, String lexema, String tipo) {
        this.token = token;
        this.lexema = lexema;
        this.tipo = tipo;
    }

    public int getToken() {
        return token;
    }

    public String getLexema() {
        return lexema;
    }

    public String getTipo() {
        return tipo;
    }
}
