package casos_prueba_catedra;

import compilador.CompiladorFijo;

import java.util.ArrayList;
import java.util.List;

public class Coment_BienDef {
    public static void main(String[] args) {
        List<String> lineasCFuente = new ArrayList<>();

        lineasCFuente.add("%%COMENTARIO_A");
        lineasCFuente.add("%%COMENTARIO_B");
        lineasCFuente.add("%%125445?¡{-'¿");
        lineasCFuente.add("x = 5_ui;");


        CompiladorFijo.compilar(lineasCFuente);
    }
}
