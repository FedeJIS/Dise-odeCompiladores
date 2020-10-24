package casos_prueba_sintactico;

import compilador.Compilador;

public class SentEjec_FaltaPuntoComa {
    public static void main(String[] args) {
        String lineasCFuente =
                "LOOP{\n" +
                " OUT(a);\n  " +
                "} UNTIL (a == 1_ui) %%FALTA ';' ACA\n" +
                "" +
                "IF (a==1_ui) THEN\n" +
                " OUT(a);\n" +
                "END_IF %%FALTA ';' ACA\n"
                ;

        Compilador.compilar(lineasCFuente);
    }
}
