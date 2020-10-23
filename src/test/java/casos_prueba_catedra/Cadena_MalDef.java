package casos_prueba_catedra;

import compilador.Compilador;

public class Cadena_MalDef {
    public static void main(String[] args) {
        String lineasCFuente =
                "OUT("+
                    "\"LINEA1-\n" +
                    "LINEA2\n" +
                    "LIN-----EA3\"" +
                ");";

        Compilador.compilar(lineasCFuente);
    }
}
