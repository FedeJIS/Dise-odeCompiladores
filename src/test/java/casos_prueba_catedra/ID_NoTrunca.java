package casos_prueba_catedra;

import compilador.Compilador;

public class ID_NoTrunca {
    public static void main(String[] args) {
        String lineasCFuente

                = "a_123456789123456789 = 1_ui; %%20 simbolos" + '\n'
                + "a___________________ = 1_ui; %%20 simbolos" + '\n'
                + "abcdefghijklmnopqrst = 1_ui; %%20 simbolos" + '\n'
                + "a_12345678912345678 = 1_ui; %%19 simbolos";

        Compilador.compilar(lineasCFuente,false,true);
    }
}
