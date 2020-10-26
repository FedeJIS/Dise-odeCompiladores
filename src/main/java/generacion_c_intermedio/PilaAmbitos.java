package generacion_c_intermedio;

import java.util.ArrayList;
import java.util.List;

public class PilaAmbitos {
    private final List<String> ambitos = new ArrayList<>();

    private final String ambitoGlobal = "PROGRAM";

    public PilaAmbitos(){
        ambitos.add(ambitoGlobal);
    }

    public boolean inAmbitoGlobal(){
        return getAmbitosConcatenados().equals(ambitoGlobal);
    }

    public void agregarAmbito(String nuevoAmbito){
        ambitos.add(nuevoAmbito);
    }

    public void eliminarUltimo(){
        ambitos.remove(ambitos.size()-1); //Saco el ultimo ambito almacenado.
    }

    public String getUltimoAmbito(){
        return ambitos.get(ambitos.size()-1);
    }

    public String getAmbitosConcatenados(){
        StringBuilder builder = new StringBuilder();
        for (String ambito : ambitos){
            builder.append(ambito).append(":");
        }
        return builder.substring(0,builder.length()-1); //El '-1' elimina el ultimo '.' concatenado.
    }
}
