package casos_prueba_catedra;

import compilador.Compilador;
import compilador.CompiladorFijo;

import java.util.ArrayList;
import java.util.List;

public class PR_MalDef {
    public static void main(String[] args) {
        String lineasCFuente
                = "out(5_ui); \n"
                + "OUt(5_ui); \n"
                + "oUT(5_ui);";

        Compilador.compilar(lineasCFuente);
    }
}
