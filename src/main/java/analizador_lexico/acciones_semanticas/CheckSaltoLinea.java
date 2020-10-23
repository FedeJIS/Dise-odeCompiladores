package analizador_lexico.acciones_semanticas;

import analizador_lexico.maquina_estados.MaquinaEstados;
import util.CodigoFuente;
import util.TablaNotificaciones;

public class CheckSaltoLinea extends AccionSemantica {
    private final CodigoFuente codigoFuente;
    private final MaquinaEstados maquinaEstados;

    public CheckSaltoLinea(CodigoFuente codigoFuente, MaquinaEstados maquinaEstados) {
        this.codigoFuente = codigoFuente;
        this.maquinaEstados = maquinaEstados;
    }

    @Override
    public void ejecutar() {
        if (codigoFuente.simboloAnterior() != '-') //Se lee un salto de linea, pero falta el '-' que lo antecede.
            TablaNotificaciones.agregarWarning("Linea "+maquinaEstados.getLineaActual()+": Falta un '-' antes del salto de linea.");
        else //En caso de que este el '-', hay que sacarlo. RESTRICCION DE LA CATEDRA.
            quitaChar();
    }
}
