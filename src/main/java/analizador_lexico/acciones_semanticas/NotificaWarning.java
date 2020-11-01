package analizador_lexico.acciones_semanticas;

import analizador_lexico.AnalizadorLexico;
import util.TablaNotificaciones;

public class NotificaWarning extends AccionSemantica {
    private final String mensaje;
    private final AnalizadorLexico aLexico;

    public NotificaWarning(String mensaje, AnalizadorLexico aLexico) {
        this.mensaje = mensaje;
        this.aLexico = aLexico;
    }

    /**
     * Accion semantica auxiliar para notificar warnings.
     */
    @Override
    public void ejecutar() {
        TablaNotificaciones.agregarWarning(aLexico.getLineaActual(),mensaje);
    }
}
