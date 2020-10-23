package casos_prueba_catedra;

import compilador.Compilador;

public class PROC_CuerpoVacio {
    public static void main(String[] args) {
        String lineasCFuente
                = "PROC lala(DOUBLE a) NI = 5_ui {\n" +
                "};";

        Compilador.compilar(lineasCFuente);
    }
}
