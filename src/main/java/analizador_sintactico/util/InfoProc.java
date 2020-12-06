package analizador_sintactico.util;

import java.util.ArrayList;
import java.util.List;

public class InfoProc {
    private final String lexema;
    private int numInvoc;
    private final List<String> nombreParams = new ArrayList<>();
    private final List<String> tipoPasajeParams = new ArrayList<>();
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

    public List<String> getParams() {
        return nombreParams;
    }

    public void addParam(String nombreParam, String tipoPasaje){
        if (nombreParams.size() < 3){ //Solo agrego si tengo menos de 3 parametros.
            this.nombreParams.add(nombreParam);
            this.tipoPasajeParams.add(tipoPasaje);
        }
    }

    public boolean isInfoValida() {
        return infoValida;
    }

    public void setInfoValida(boolean infoValida) {
        this.infoValida = infoValida;
    }
}
