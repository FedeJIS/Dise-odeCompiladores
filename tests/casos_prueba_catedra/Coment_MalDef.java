package casos_prueba_catedra;

import compilador.CompiladorFijo;

import java.util.ArrayList;
import java.util.List;

public class Coment_MalDef {
    public static void main(String[] args) {
        List<String> lineasCFuente = new ArrayList<>();

        lineasCFuente.add("%%NO RECONOCE EL COMENTARIO, TOMA EL CONTENIDO COMO UN IDENTIFICADOR");
        lineasCFuente.add("%mal_coment = 5_ui;");
        lineasCFuente.add("x = 5_ui;");


        CompiladorFijo.compilar(lineasCFuente);
    }
}
