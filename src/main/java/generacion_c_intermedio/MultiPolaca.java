package generacion_c_intermedio;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MultiPolaca {
    private final Map<String, Polaca> polacaProcedimientos = new HashMap<>();

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

    public void agregarPasos(String proc, String... pasos){
        Polaca polacaProc = polacaProcedimientos.getOrDefault(proc,new Polaca());

        polacaProc.agregarPasos(pasos);
        polacaProcedimientos.put(proc,polacaProc);
    }

    public List<String> getListaPasos(String proc){
        return polacaProcedimientos.get(proc).getListaPasos();
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
