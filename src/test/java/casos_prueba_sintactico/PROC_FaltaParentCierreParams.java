package casos_prueba_sintactico;

import compilador.Compilador;

public class PROC_FaltaParentCierreParams {

    public static void main(String[] args) {
        String lineasCFuente
                = "PROC lala(DOUBLE a NI = 1_ui {\n" +
                "   OUT(a);\n" +
                "};";

        Compilador.compilar(lineasCFuente);
    }
}
