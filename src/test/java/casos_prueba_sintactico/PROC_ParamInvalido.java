package casos_prueba_sintactico;

import compilador.Compilador;

public class PROC_ParamInvalido {
    public static void main(String[] args) {
        String lineasCFuente
                = "PROC lala(DOUBLE , VAR a, VAR UINT) NI = 1_ui {\n" +
                "   OUT(a);\n" +
                "};";

        Compilador.compilar(lineasCFuente);
    }
}
