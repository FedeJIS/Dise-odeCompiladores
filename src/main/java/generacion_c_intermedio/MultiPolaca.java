package generacion_c_intermedio;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MultiPolaca {
    private final Map<String, Polaca> polacaProcedimientos = new HashMap<>();

    @Override
    public String toString() {
        StringBuilder multiPolacaBuilder = new StringBuilder();
        for (String ambito : polacaProcedimientos.keySet())
            multiPolacaBuilder.append(ambito).append('\n').append(polacaProcedimientos.get(ambito).toString()).append('\n');
        return multiPolacaBuilder.toString();
    }

    public void print() {
        System.out.println("###POLACA PROCS###");
        for (String ambito : polacaProcedimientos.keySet())
            System.out.println(ambito + polacaProcedimientos.get(ambito).toString());
    }

    public Polaca inicPolacaVacia(String ambito){
        if (polacaProcedimientos.containsKey(ambito))
            throw new IllegalStateException("Ya existe una polaca para el ambito '"+ambito+"'.");

        Polaca nuevaPolaca = new Polaca();
        polacaProcedimientos.put(ambito,nuevaPolaca);
        return nuevaPolaca;
    }

    public void quitarUltimoPaso(String proc) {
        Polaca polacaProc = polacaProcedimientos.get(proc);
        if (polacaProc == null) throw new IllegalStateException("No existe polaca para el procedimiento '"+proc+"'.");

        polacaProc.quitarUltimoPaso();
    }

    public void agregarPasos(String proc, String... pasos){
        Polaca polacaProc = polacaProcedimientos.getOrDefault(proc,new Polaca());

        polacaProc.agregarPasos(pasos);
        polacaProcedimientos.put(proc,polacaProc);
    }

    public Collection<String> getNombreProcs(){
        return polacaProcedimientos.keySet();
    }

    public Polaca getPolaca(String proc){
        return polacaProcedimientos.get(proc);
    }

    public void ejecutarPuntoControl(String ambitoActual, int puntoControl) {
        Polaca polacaProc = polacaProcedimientos.get(ambitoActual);
        if (polacaProc == null) {
            if (puntoControl != Polaca.PC_LOOP) //El unico caso en el que esta bien que no exista el ambito es con un LOOP.
                throw new IllegalStateException("No se encontro una polaca para el ambito '"+ambitoActual+"'.");
            polacaProc = inicPolacaVacia(ambitoActual); //Inicializo la polaca del loop vacia.
        }

        switch (puntoControl) {
            case Polaca.PC_THEN:
                polacaProc.puntoControlThen();
                break;
            case Polaca.PC_ELSE:
                polacaProc.puntoControlElse();
                break;
            case Polaca.PC_FIN_COND:
                polacaProc.puntoControlFinCondicional();
                break;
            case Polaca.PC_LOOP:
                polacaProc.puntoControlLoop();
                break;
            case Polaca.PC_UNTIL:
                polacaProc.puntoControlUntil();
                break;
        }
    }
}
