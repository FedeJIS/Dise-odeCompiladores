package casos_prueba_sintactico;

import compilador.Compilador;

public class IF_FaltaCond {
    public static void main(String[] args) {
        String lineasCFuente =
                "IF THEN\n" +
                " OUT(a);\n" +
                "END_IF;\n"
                ;

        Compilador.compilar(lineasCFuente);
    }
}
