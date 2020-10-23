package casos_prueba_catedra;

import compilador.Compilador;
import compilador.CompiladorFijo;

import java.util.ArrayList;
import java.util.List;

public class Coment_BienDef {
    public static void main(String[] args) {
        String lineasCFuente =
                "%%COMENTARIO_A" + '\n' +
                "%%COMENTARIO_B" + '\n' +
                "%%125445?¡{-'¿" + '\n' +
                "x = 5_ui;";

        Compilador.compilar(lineasCFuente);
    }
}
