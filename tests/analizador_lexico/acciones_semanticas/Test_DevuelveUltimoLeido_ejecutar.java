package analizador_lexico.acciones_semanticas;

import analizador_lexico.AccionSemantica;
import util.CodigoFuente;

import java.util.ArrayList;
import java.util.List;

public class Test_DevuelveUltimoLeido_ejecutar {
    public static void main(String[] args) {
        List<String> lineas = new ArrayList<>();
        String l1 = "a", l2 = "b";
        lineas.add(l1+l2);

        CodigoFuente codigoFuente = new CodigoFuente(lineas);
        AccionSemantica.DevuelveUltimoLeido devuelveUltimoLeido = new AccionSemantica.DevuelveUltimoLeido(codigoFuente);

        System.out.println("1. Caracter leido:"+codigoFuente.simboloActual()); //Leo 'a'.
        codigoFuente.avanzar();
        System.out.println("2. Caracter leido:"+codigoFuente.simboloActual()); //Leo 'b'.

        devuelveUltimoLeido.ejecutar(); //Retrocedo una posicion.

        System.out.println("3. Caracter leido:"+codigoFuente.simboloActual()); //Leo 'a'.
    }
}
