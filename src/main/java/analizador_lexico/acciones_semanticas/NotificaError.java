package analizador_lexico.acciones_semanticas;

import analizador_lexico.AnalizadorLexico;
import util.CodigoFuente;
import util.TablaNotificaciones;

public class NotificaError extends AccionSemantica {
    private final String mensaje;
    private final AnalizadorLexico aLexico;
    private final CodigoFuente cFuente;
    private final boolean simboloNoReconocido;

    public NotificaError(String mensaje, AnalizadorLexico aLexico, CodigoFuente cFuente, boolean simboloNoReconocido) {
        this.mensaje = mensaje;
        this.aLexico = aLexico;
        this.cFuente = cFuente;
        this.simboloNoReconocido = simboloNoReconocido;
    }

    /**
     * Accion semantica auxiliar para notificar errores.
     */
    @Override
    public void ejecutar() {
        String error = "Linea "+aLexico.getLineaActual()+": ";
        if (simboloNoReconocido)
            error += "Simbolo '" + cFuente.simboloActual() + "' no reconocido.";
        else error += mensaje;

        TablaNotificaciones.agregarError(error);
    }
}
