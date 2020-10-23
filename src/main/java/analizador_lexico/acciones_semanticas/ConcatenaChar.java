package analizador_lexico.acciones_semanticas;

import util.CodigoFuente;

/**
 * Concatena un caracter al final de un string.
 */
public class ConcatenaChar extends AccionSemantica {
    private final CodigoFuente codigoFuente;

    public ConcatenaChar(CodigoFuente codigoFuente) {
        this.codigoFuente = codigoFuente;
    }

    @Override
    public void ejecutar() {
        concatenaChar(codigoFuente.simboloActual());
    }
}
