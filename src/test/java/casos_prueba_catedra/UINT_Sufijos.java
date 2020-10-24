package casos_prueba_catedra;

import compilador.Compilador;

public class UINT_Sufijos {
    public static void main(String[] args) {
        String lineasCFuente

                = "a = 1; %%Completa el sufijo" + '\n'
                + "a = 1_; %%Error sufijo incompleto" + '\n'
                + "a = 1_u; %%Error sufijo incompleto" + '\n'
                + "a = 1_ui; %%UINT bien definido";

        Compilador.compilar(lineasCFuente,false,true);
    }
}
