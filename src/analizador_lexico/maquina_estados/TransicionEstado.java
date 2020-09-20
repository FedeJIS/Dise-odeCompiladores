package analizador_lexico.maquina_estados;

import analizador_lexico.AccionSemantica;

import java.util.Arrays;
import java.util.List;

public class TransicionEstado {
    private final int siguienteEstado;
    private final List<AccionSemantica> accionesSemanticas;

    public TransicionEstado(int siguienteEstado, AccionSemantica... accionesSemanticas) {
        this.siguienteEstado = siguienteEstado;
        this.accionesSemanticas = Arrays.asList(accionesSemanticas);
    }

    public int siguienteEstado() {
        return siguienteEstado;
    }

    public void ejecutarAccionSemantica() {
        for (AccionSemantica accionSemantica : accionesSemanticas)
            accionSemantica.ejecutar();
    }
}
