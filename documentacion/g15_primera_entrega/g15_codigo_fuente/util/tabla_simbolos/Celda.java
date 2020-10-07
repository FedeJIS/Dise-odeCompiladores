package util.tabla_simbolos;

public class Celda {
    private final int token;
    private final String lexema;
    private final String tipo;
    private int referencias;

    public Celda(int token, String lexema, String tipo) {
        this.token = token;
        this.lexema = lexema;
        this.tipo = tipo;
        this.referencias = 0;
    }

    public void actualizarReferencias(int i) {
        referencias += i;
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

    public boolean sinReferencias() {
        return referencias == 0;
    }

    @Override
    public String toString() {
        return "{" +
                "token=" + token +
                ", lexema='" + lexema + '\'' +
                ", tipo='" + tipo + '\'' +
                ", referencias=" + referencias +
                '}';
    }
}
