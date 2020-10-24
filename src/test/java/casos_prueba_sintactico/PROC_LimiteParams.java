package casos_prueba_sintactico;

import compilador.Compilador;

public class PROC_LimiteParams {
    public static void main(String[] args) {
        String lineasCFuente
                = "PROC lala(DOUBLE a, DOUBLE b, DOUBLE c, DOUBLE d, DOUBLE d) NI = 1_ui {\n" +
                "   OUT(a);\n" +
                "};\n" +

                "lala(a,b,c,d);";

        Compilador.compilar(lineasCFuente,false,true);
    }
}
