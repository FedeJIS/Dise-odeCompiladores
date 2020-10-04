package analizador_sintactico.sent_ejecutables.asignacion;

import util.RunSintactico;
import util.tabla_simbolos.TablaDeSimbolos;

public class OK_CambioSignoFactor {
    public static void main(String[] args) {
        String linea =
                "IF (55.0 < 55.0) THEN\n" +
                    "OUT(55.0);\n" +
                "END_IF;\n";
        execute(linea);

    }

    private static void execute(String linea) {
        RunSintactico.run(false, linea);

        TablaDeSimbolos tablaS = RunSintactico.getTablaS();

        tablaS.printAll();

        System.out.println("$$$$$$$$");
    }
}
