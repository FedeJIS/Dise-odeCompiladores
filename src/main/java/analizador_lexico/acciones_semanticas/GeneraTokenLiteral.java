package analizador_lexico.acciones_semanticas;

import analizador_lexico.maquina_estados.MaquinaEstados;
import util.CodigoFuente;

public class GeneraTokenLiteral extends AccionSemantica {
    private final MaquinaEstados maquinaEstados;

    private final CodigoFuente cFuente;

    public GeneraTokenLiteral(MaquinaEstados maquinaEstados, CodigoFuente cFuente) {
        this.maquinaEstados = maquinaEstados;
        this.cFuente = cFuente;
    }

    @Override
    public void ejecutar() {
        int token = cFuente.simboloActual(); //Conversion implicita de char a ASCII.
        maquinaEstados.setVariablesSintactico((short) token, ""); //No tiene lexema.
    }
}
