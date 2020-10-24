package casos_prueba_catedra;

import compilador.Compilador;

public class ID_EmpiezaGuion {
    public static void main(String[] args) {
        String lineasCFuente
            = "_1 = 1_ui;" + '\n'
            + "_ = 1_ui;" + '\n'
            + "_a = 1_ui;" + '\n';

        Compilador.compilar(lineasCFuente,false,true);
    }
}
