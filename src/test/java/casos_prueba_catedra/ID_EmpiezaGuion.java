package casos_prueba_catedra;

import compilador.Compilador;

public class ID_EmpiezaGuion {
    public static void main(String[] args) { //TODO VER SI SE PUEDE MOSTRAR MEJOR EL ERROR CUANDO FALTA ID DEL LADO DER.
        String lineasCFuente
            = "_1 = 1_ui;" + '\n'
            + "_ = 1_ui;" + '\n'
            + "_a = 1_ui;" + '\n';

        Compilador.compilar(lineasCFuente);
    }
}
