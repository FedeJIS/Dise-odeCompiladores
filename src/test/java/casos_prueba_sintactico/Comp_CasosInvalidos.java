package casos_prueba_sintactico;

import compilador.Compilador;

public class Comp_CasosInvalidos {
    public static void main(String[] args) {
        String lineasCFuente =
                "IF (a=a) THEN\n" +
                    "OUT(a);\n" +
                "END_IF;\n" +

                "IF (a==) THEN\n" +
                    "OUT(a);\n" +
                "END_IF;\n" +

                "IF (==a) THEN\n" +
                    "OUT(a);\n" +
                "END_IF;\n" +

                "IF (a==a THEN\n" +
                    "OUT(a);\n" +
                "END_IF;\n"
                ;

        Compilador.compilar(lineasCFuente,false,true);
    }
}
