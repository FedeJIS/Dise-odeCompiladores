package casos_prueba_sintactico;

import compilador.Compilador;

public class IF_FaltaCuerpoELSE {
    public static void main(String[] args) {
        String lineasCFuente =
                "IF (a==a) THEN\n" +
                    "OUT(a);\n" +
                "ELSE\n" +
                "END_IF;\n"
                ;

        Compilador.compilar(lineasCFuente,false,true);
    }
}
