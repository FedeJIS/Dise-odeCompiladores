package tabla_simbolos;

import util.tabla_simbolos.Celda;
import util.tabla_simbolos.TablaDeSimbolos;

public class Test_eliminarEntrada {
    public static void main(String[] args) {
        TablaDeSimbolos tS = new TablaDeSimbolos();

        int token = 1;
        String lexema = "5";

        tS.agregarEntrada(token,lexema,"");

        System.out.println("Original:");
        tS.printAll();

        Celda celda = tS.getValor(lexema);
        celda.actualizarReferencias(-1);

        System.out.println("Quito referencia:");
        tS.printAll();
        if (celda.sinReferencias()) tS.eliminarEntrada(lexema);
        System.out.println("Final:");
        tS.printAll();


    }
}
