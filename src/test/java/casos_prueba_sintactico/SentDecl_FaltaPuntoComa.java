package casos_prueba_sintactico;

import compilador.Compilador;

public class SentDecl_FaltaPuntoComa {
    public static void main(String[] args) {
        String lineasCFuente
                =
                "PROC lala(DOUBLE a) NI = 5_ui {\n" +
                " OUT(a) %%FALTA ';' ACA\n  " +
                " OUT(a);\n  " +
                "} %%FALTA ';' ACA\n" +
                "OUT(5_ui) %%FALTA ';' ACA\n" +
                "OUT(3.0d1); \n" +
                "UINT x,y,z %%FALTA ';' ACA\n";

        Compilador.compilar(lineasCFuente);
    }
}
