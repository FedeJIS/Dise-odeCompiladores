package casos_prueba_sintactico;

import compilador.Compilador;

public class Asign_FaltaExpr {
    public static void main(String[] args) {
        String lineasCFuente ="x = ;";

        Compilador.compilar(lineasCFuente,false,true);
    }
}
