package casos_prueba_semantica;

import compilador.Compilador;

public class IdsNoDeclarados {
    public static void main(String[] args) {
        String lineasFuente =
                "x = 5_ui;\n" +
                "y();"
                ;

        Compilador.compilar(lineasFuente,false,true);
    }
}
