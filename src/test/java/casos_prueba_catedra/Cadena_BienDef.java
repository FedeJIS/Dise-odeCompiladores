package casos_prueba_catedra;

import compilador.Compilador;

public class Cadena_BienDef {
    public static void main(String[] args) {
        String lineasCFuente =
                "OUT("+
                    "\"LINEA1-\n" +
                    "LINEA2-\n" +
                    "LINEA3\"" +
                ");";

        Compilador.compilar(lineasCFuente,false,true);
    }
}
