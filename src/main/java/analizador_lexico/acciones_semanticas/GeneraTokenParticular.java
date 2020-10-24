package analizador_lexico.acciones_semanticas;

import analizador_lexico.maquina_estados.MaquinaEstados;
import util.ReprTokens;

public class GeneraTokenParticular extends AccionSemantica {
    private final MaquinaEstados maquinaEstados;

    private final short token;

    public GeneraTokenParticular(MaquinaEstados maquinaEstados, short token) {
        this.maquinaEstados = maquinaEstados;
        this.token = token;
    }

    @Override
    public void ejecutar() {
        maquinaEstados.setVariablesSintactico(token,
                ReprTokens.getRepresentacion(token)); //No tiene lexema, pero setea el string para guardarlo en la polaca.
    }
}
