package generacion_c_intermedio;

import java.util.ArrayList;
import java.util.List;

public class PilaAmbitos {
    private final List<String> ambitos = new ArrayList<>();

    private String ambitoActual;

    public PilaAmbitos(){
        ambitoActual = "PROGRAM";
        ambitos.add(ambitoActual);
    }

    public String getAmbitoActual(){
        return ambitoActual;
    }

    public void agregarAmbito(String nuevoAmbito){
        ambitos.add(nuevoAmbito);
        ambitoActual = nuevoAmbito;
    }

    public void eliminarUltimo(){
        ambitos.remove(ambitos.size()-1); //Saco el ultimo ambito almacenado.
        ambitoActual = ambitos.get(ambitos.size()-1); //Actualizo el ambito actual.
    }

    public String getAmbitosConcatenados(){
        StringBuilder builder = new StringBuilder();
        for (String ambito : ambitos){
            builder.append(ambito).append(".");
        }
        return builder.substring(0,builder.length()-1); //El '-1' elimina el ultimo '.' concatenado.
    }
}
