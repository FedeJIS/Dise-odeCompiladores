package generacion_c_intermedio;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Polaca {
    public static final int PC_THEN = 0;
    public static final int PC_ELSE = 1;
    public static final int PC_FIN_COND = 2;

    private final List<String> listaPasos = new ArrayList<>();
    private final List<Integer> pilaIncompletos = new ArrayList<>();

    private final String simboloPasoIncompleto = "@";

    @Override
    public String toString() {
        return "{" +
                listaPasos.toString() +
                '}';
    }

    public void print() {
        System.out.println("###POLACA###");
        for (int i = 0; i < listaPasos.size(); i++)
            System.out.print("("+i+","+ listaPasos.get(i)+");");
        System.out.println();
    }

    public void agregarPasos(String... pasos){
        listaPasos.addAll(Arrays.asList(pasos));
    }

    public void puntoControlThen(){
        listaPasos.add(simboloPasoIncompleto); //Agrego el BF con destino incompleto.
        int pasoActual = listaPasos.size() - 1; //La posicion del paso incompleto es la actual.
        pilaIncompletos.add(pasoActual); //Apilo el numero para accederlo posteriormente.

        listaPasos.add("BF"); //Agrego el operador unario BF = Bifurcacion por falso.
    }

    public void puntoControlElse() {
        int posPasoIncompleto = pilaIncompletos.get(pilaIncompletos.size()-1); //Desapilo el incompleto en el tope de la pila.

        listaPasos.add(simboloPasoIncompleto); //Agrego el paso BI con destino incompleto.
        pilaIncompletos.add(listaPasos.size()-1); //Apilo el paso actual, ya que esta incompleto.
        listaPasos.add("BI"); //Agrego el operador unario BI = Bifurcacion incondicional.

        int pasoActual = listaPasos.size() - 1; //Actualizo el paso actual.
        listaPasos.set(posPasoIncompleto, String.valueOf(pasoActual + 1)); //La BF tiene que caer en el siguiente a la pos actual.
    }

    public void puntoControlFinCondicional() {
        int posPasoIncompleto = pilaIncompletos.get(pilaIncompletos.size()-1); //Desapilo el incompleto en el tope de la pila.
        int posActual = listaPasos.size()-1;
        listaPasos.set(posPasoIncompleto,String.valueOf(posActual + 1)); //La BI tiene que caer en el siguiente a la pos actual.
    }

    public void puntoControlLoop() {
        pilaIncompletos.add(listaPasos.size()); //Apilo el inicio del LOOP (posicion actual).
                                                // No hay que agregarle un offset porque no se agrego ningun paso a la polaca.
    }

    public void puntoControlUntil() {
        int posPasoIncompleto = pilaIncompletos.get(pilaIncompletos.size()-1); //Desapilo el incompleto en el tope de la pila.
        listaPasos.add(String.valueOf(posPasoIncompleto));
        listaPasos.add("BF"); //Se va al inicio del LOOP en caso de que la condicion sea falsa.
    }
}
