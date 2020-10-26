package util.tabla_simbolos;

import util.TablaNotificaciones;

public class Celda {
    private final int token;
    private String lexema;
    private String tipo;
    private String uso;
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

    public void setUso(String uso) {
        this.uso = uso;
    }

    public boolean isDeclarada(){
        return declarada;
    }

    public void setDeclarada(boolean declarada) {
//        if (this.declarada){ //Hay una redeclaracion.
//            int finAmbito = lexema.lastIndexOf("::");
//            String ambito = lexema.substring(0,finAmbito);
//            String id = lexema.substring(finAmbito+1);
//            TablaNotificaciones.agregarError("El identificador '" + id+"' ya se encuentra declarado en el ambito '"+ambito+"'.");
//        }
        this.declarada = declarada;
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
                ", referencias=" + referencias +
                '}';
    }
}
