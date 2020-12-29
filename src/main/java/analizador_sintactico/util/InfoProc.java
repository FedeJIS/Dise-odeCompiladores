package analizador_sintactico.util;

import util.tabla_simbolos.TablaSimbolos;

import java.util.ArrayList;
import java.util.List;

public class InfoProc {
    private final String lexema;
    private int numInvoc;
    private final List<String> nombreParams = new ArrayList<>();
    private final List<String> tipoParams = new ArrayList<>();
    private boolean infoValida = true;

    public InfoProc(String lexema) {
        this.lexema = lexema;
    }

    public String getLexema() {
        return lexema;
    }

    public int getNumInvoc() {
        return numInvoc;
    }

    public void setNumInvoc(int numInvoc) {
        this.numInvoc = numInvoc;
    }

    public void addParam(String proc, String nombreParam, String tipo, TablaSimbolos tablaS){
        if (nombreParams.size() < 3){ //Solo agrego si tengo menos de 3 parametros.
            this.nombreParams.add(nombreParam);
            tablaS.addParamProc(proc, nombreParam);
            tablaS.addTipoParamProc(proc, tipo);
        }
    }

    public boolean isInfoValida() {
        return infoValida;
    }

    public void setInfoValida(boolean infoValida) {
        this.infoValida = infoValida;
    }
}
