package casos_prueba_catedra;

import compilador.Compilador;

public class DOUBLE_FueraRango {
    public static void main(String[] args) {
        String lineasCFuente =
                "min_posit = 2.2250738585072011d-308;\n"
                + "max_posit = 1.7976931348623159d308;\n"
                + "min_negat = -2.2250738585072011d-308;\n" //TODO AGREGAR ERROR CUANDO NO HAY NADA DEL LADO DER.
                + "max_negat = -1.7976931348623159d308;"
                ;

        Compilador.compilar(lineasCFuente,false,true);
    }
}
