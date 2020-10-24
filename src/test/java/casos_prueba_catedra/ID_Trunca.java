package casos_prueba_catedra;

import compilador.Compilador;

public class ID_Trunca {
    public static void main(String[] args) {
        String lineasCFuente =
                "a_1234567891234567891 = 1_ui; %%21 simbolos" + '\n' +
                "a____________________ = 1_ui; %%21 simbolos" + '\n' +
                "abcdefghijklmnopqrstu = 1_ui; %%21 simbolos" + '\n';
        Compilador.compilar(lineasCFuente,false,true);
    }
}
