package generacion_c_intermedio;

import util.TablaNotificaciones;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Polaca {
    public static final int PC_THEN = 0;
    public static final int PC_ELSE = 1;
    public static final int PC_FIN_COND = 2;
    public static final int PC_LOOP = 3;
    public static final int PC_UNTIL = 4;

    public static final String PASO_BI = "BI", PASO_BF = "BF";
    public static final String PASO_INVOC = "INVOC";

    private final List<String> listaPasos = new ArrayList<>();
    private final List<Integer> pilaIncompletos = new ArrayList<>();

    private final String simboloPasoIncompleto = "@";

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < listaPasos.size(); i++)
            builder.append('(').append(i).append(';').append(listaPasos.get(i)).append(')').append('\n');

        return builder.toString();
    }

    public int longitud(){
        return listaPasos.size();
    }

    public void quitarUltimoPaso() {
        if (listaPasos.size() != 0)
            listaPasos.remove(listaPasos.size()-1);
    }

    public void agregarPasos(String... pasos){
        if (!TablaNotificaciones.hayErrores()) listaPasos.addAll(Arrays.asList(pasos));
    }

    public void ajustaPaso(int paso, int offset){
        int valorSalto = Integer.parseInt(listaPasos.get(paso-1)); //El paso a ajustar es el anterior al que me encuentro.
        listaPasos.set(paso-1, String.valueOf(valorSalto + offset));
    }

    public List<String> getListaPasos(){
        return new ArrayList<>(listaPasos);
    }

    /**NUEVA ENTREGA: Retorna los 2 ultimos pasos de la polaca **/
    public String[] getUltimosPasos(){
        if(listaPasos.size() >= 1) {
            String ultimoPaso = listaPasos.get(listaPasos.size()-1);
            String anteultimoPaso = listaPasos.get(listaPasos.size()-2);
            String[] pasos = {anteultimoPaso,ultimoPaso};
            return pasos;
        }
        return null;
    }

    public void puntoControlThen(){
        if (!TablaNotificaciones.hayErrores()) {
            listaPasos.add(simboloPasoIncompleto); //Agrego el BF con destino incompleto.
            int pasoActual = listaPasos.size() - 1; //La posicion del paso incompleto es la actual.
            pilaIncompletos.add(pasoActual); //Apilo el numero para accederlo posteriormente.

            listaPasos.add("BF"); //Agrego el operador unario BF = Bifurcacion por falso.
        }
    }

    public void puntoControlElse() {
        if (!TablaNotificaciones.hayErrores()) {
            int posPasoIncompleto = pilaIncompletos.remove(pilaIncompletos.size()-1); //Desapilo el incompleto en el tope de la pila.
            listaPasos.add(simboloPasoIncompleto); //Agrego el paso BI con destino incompleto.
            pilaIncompletos.add(listaPasos.size() - 1); //Apilo el paso actual, ya que esta incompleto.
            listaPasos.add("BI"); //Agrego el operador unario BI = Bifurcacion incondicional.

            int pasoActual = listaPasos.size() - 1; //Actualizo el paso actual.
            listaPasos.set(posPasoIncompleto, String.valueOf(pasoActual + 1)); //La BF tiene que caer en el siguiente a la pos actual.
            listaPasos.add("L"+listaPasos.size()); //Agrego label para ser usado en generacion assembler.
        }
    }

    public void puntoControlFinCondicional() {
        if (!TablaNotificaciones.hayErrores()) {
            int posPasoIncompleto = pilaIncompletos.remove(pilaIncompletos.size() - 1); //Desapilo el incompleto en el tope de la pila.
            int posActual = listaPasos.size() - 1;
            listaPasos.set(posPasoIncompleto, String.valueOf(posActual + 1)); //La BI tiene que caer en el siguiente a la pos actual.
            listaPasos.add("L"+listaPasos.size()); //Agrego label para ser usado en generacion assembler.
        }
    }

    public void puntoControlLoop() {
        if (!TablaNotificaciones.hayErrores()) {
            listaPasos.add("L"+(listaPasos.size()));
            pilaIncompletos.add(listaPasos.size()-1); //Apilo el inicio del LOOP (posicion actual).  No hay que agregarle
                                                    // un offset porque no se agrego ningun paso a la polaca.
        }
    }

    public void puntoControlUntil() {
        if (!TablaNotificaciones.hayErrores()) {
            int posPasoIncompleto = pilaIncompletos.remove(pilaIncompletos.size() - 1); //Desapilo el incompleto en el tope de la pila.
            listaPasos.add(String.valueOf(posPasoIncompleto));
            listaPasos.add("BF"); //Se va al inicio del LOOP en caso de que la condicion sea falsa.
        }
    }
}
