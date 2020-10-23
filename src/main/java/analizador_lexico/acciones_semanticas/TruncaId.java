package analizador_lexico.acciones_semanticas;

import analizador_lexico.AnalizadorLexico;
import util.TablaNotificaciones;

/**
 * Si se excede el limite de un string, se trunca y genera un WARNING.
 */
public class TruncaId extends AccionSemantica {
    private final static int LIMITE_STRING = 20;

    private final AnalizadorLexico aLexico;

    public TruncaId(AnalizadorLexico aLexico) {
        this.aLexico = aLexico;
    }

    @Override
    public void ejecutar() {
        if (LIMITE_STRING < longString()) {
            truncaString(LIMITE_STRING);
            TablaNotificaciones.agregarWarning("Warning en la linea " + aLexico.getLineaActual() + ": Identificador truncado por superar limite de caracteres.");
        }
    }
}
