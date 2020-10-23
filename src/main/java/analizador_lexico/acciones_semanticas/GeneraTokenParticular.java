package analizador_lexico.acciones_semanticas;

import analizador_lexico.maquina_estados.MaquinaEstados;

public class GeneraTokenParticular extends AccionSemantica {
    private final MaquinaEstados maquinaEstados;

    private final int token;

    public GeneraTokenParticular(MaquinaEstados maquinaEstados, int token) {
        this.maquinaEstados = maquinaEstados;
        this.token = token;
    }

    @Override
    public void ejecutar() {
        maquinaEstados.setVariablesSintactico(token, ""); //No tiene lexema.
    }
}
