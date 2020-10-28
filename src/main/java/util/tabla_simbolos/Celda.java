package util.tabla_simbolos;

import java.util.ArrayList;
import java.util.List;

public class Celda {
    public static final String USO_PROC = "Proc";
    private static final String USO_PARAM_CVR = "ParamCVR";

    /**
     * Atributos comunes.
     */
    private final int token;
    private String lexema;
    private String tipo;
    private String uso = "-";
    private boolean declarada;
    private int referencias;

    /**
     * Atributos de procedimientos.
     */
    private int nInvoc, maxInvoc;
    private List<String> paramsDecl;

    public Celda(int token, String lexema, String tipo) {
        this.token = token;
        this.lexema = lexema;
        this.tipo = tipo;
        this.referencias = 0;
    }

    @Override
    public String toString() {
        String baseCelda =
                "{" +
                "lex='" + lexema + '\'' +
                ", tipo='" + tipo + '\'' +
                ", uso='" + uso + '\'' +
                ", decl='" + declarada + '\'' +
                ", nRefs=" + referencias;
        if (uso.equals(USO_PROC) && paramsDecl != null)
            return baseCelda + ", nInvoc=" + nInvoc + ", params=" + paramsDecl.toString() + '}';
        return baseCelda + '}';
    }

    public void setAmbito(String ambito){
        lexema = ambito+":"+lexema;
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

    public boolean isProc(){
        return uso.equals(USO_PROC);
    }

    public boolean isParamCVR() {return uso.equals(USO_PARAM_CVR);}

    public void setUso(String uso) {
        this.uso = uso;
    }

    public boolean isDeclarada(){
        return declarada;
    }

    public void setDeclarada(boolean declarada) {
        this.declarada = declarada;
    }

    public void actualizarReferencias(int i) {
        referencias += i;
    }

    public boolean sinReferencias() {
        return referencias == 0;
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

    public void setParamsDecl(List<String> paramsDecl){
        this.paramsDecl = new ArrayList<>(paramsDecl);
    }

    public int getNParams(){
        return paramsDecl.size();
    }

    public String getParam(int i){
        return paramsDecl.get(i);
    }
}
