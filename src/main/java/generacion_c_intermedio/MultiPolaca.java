package generacion_c_intermedio;

import java.util.HashMap;
import java.util.Map;

public class MultiPolaca {
    private final Map<String, Polaca> polacaProcedimientos = new HashMap<>();

    public void agregarPasos(String ambito, String... pasos){
        Polaca polacaProc = polacaProcedimientos.getOrDefault(ambito,new Polaca());

        polacaProc.agregarPasos(pasos);
        polacaProcedimientos.put(ambito,polacaProc);
    }

    public void print() {
        for (String ambito : polacaProcedimientos.keySet())
            System.out.print(ambito + polacaProcedimientos.get(ambito).toString());
        System.out.println();
    }

    public void ejecutarPuntoControl(String ambitoActual, int puntoControl) {
        Polaca polacaProc = polacaProcedimientos.get(ambitoActual);
        if (polacaProc == null) throw new IllegalStateException("No se encontro una polaca para el ambito actual.");

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
        }

    }
}
