package analizador_lexico.acciones_semanticas;

import util.CodigoFuente;

/**
 * Retrocede una posicion en el codigo fuente, para que se vuelva a leer el ultimo caracter leido.
 */
public class RetrocedeCFuente extends AccionSemantica {
    private final CodigoFuente codigoFuente;

    public RetrocedeCFuente(CodigoFuente codigoFuente) {
        this.codigoFuente = codigoFuente;
    }

    @Override
    public void ejecutar() {
        codigoFuente.retroceder();
    }
}
