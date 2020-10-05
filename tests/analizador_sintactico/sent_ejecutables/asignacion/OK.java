package analizador_sintactico.sent_ejecutables.asignacion;

import util.testing.RunSintactico;
import util.tabla_simbolos.TablaSimbolos;

public class OK {
    public static void main(String[] args) {
        execute("x = 55;");

        execute("x = -55.0;");

        execute("x = -55.0 * -1.0;");

        execute("x = 55 * 5;");

        execute("x = 55 / 5;");

        execute("x = 55 + 5;");

        execute("x = 55 - 5;");
    }

    private static void execute(String linea) {
        RunSintactico.clearTablaS();
        RunSintactico.run(false, linea);

        TablaSimbolos tablaS = RunSintactico.getTablaS();

        tablaS.printAll();

        System.out.println("$$$$$$$$");
    }
}
