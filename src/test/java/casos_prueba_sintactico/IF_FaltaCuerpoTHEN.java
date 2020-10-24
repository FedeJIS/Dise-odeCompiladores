package casos_prueba_sintactico;

import compilador.Compilador;

public class IF_FaltaCuerpoTHEN {
    public static void main(String[] args) {
        String lineasCFuente =
                "IF (a==a) THEN\n" +
                "END_IF;\n"
                ;

        Compilador.compilar(lineasCFuente);
    }
}
