package casos_prueba_catedra;

import compilador.CompiladorFijo;

import java.util.ArrayList;
import java.util.List;

public class Cadena_BienDef {
    public static void main(String[] args) {
        List<String> lineasCFuente = new ArrayList<>();

        lineasCFuente.add("OUT(");
        lineasCFuente.add("\"LINEA1-\n");
        lineasCFuente.add("LINEA2-\n");
        lineasCFuente.add("LINEA3\"");
        lineasCFuente.add(");");


        CompiladorFijo.compilar(lineasCFuente);
    }
}
