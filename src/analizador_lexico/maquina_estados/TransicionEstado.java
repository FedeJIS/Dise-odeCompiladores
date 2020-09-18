package analizador_lexico.maquina_estados;

public class TransicionEstado {
    private final int siguienteEstado;
    private final Object accionSemantica;

    public TransicionEstado(int siguienteEstado, Object accionSemantica) {
        this.siguienteEstado = siguienteEstado;
        this.accionSemantica = accionSemantica;
    }

    public int siguienteEstado() {
        return siguienteEstado;
    }

    public void ejecutarAccionSemantica() {
        System.out.println("Se ejecuto la accion semantica "+accionSemantica.toString()); //TODO Reemplazar por AccionSemantica.ejecutar().
    }
}
