package analizador_lexico.acciones_semanticas;

/**
 * Incrementa en uno el contador de lineas del codigo fuente.
 */
public class CuentaSaltoLinea extends AccionSemantica {
    private int cantLineas = 1;

    @Override
    public void ejecutar() {
        cantLineas++;
    }

    public int getCantLineas() {
        return cantLineas;
    }
}
