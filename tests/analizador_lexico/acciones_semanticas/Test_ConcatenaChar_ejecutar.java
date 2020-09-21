package analizador_lexico.acciones_semanticas;

import analizador_lexico.AccionSemantica;
import analizador_lexico.CodigoFuente;

import java.util.ArrayList;
import java.util.List;

public class Test_ConcatenaChar_ejecutar {
    public static void main(String[] args) {
        List<String> lineas = new ArrayList<>();
        String l1 = "a", l2 = "b", l3 = "\n";
        lineas.add(l1+l2+l3);

        CodigoFuente codigoFuente = new CodigoFuente(lineas);
        AccionSemantica.ConcatenaChar concatenaChar = new AccionSemantica.ConcatenaChar(codigoFuente);

        //Test letra comun 1.
        testGenerico(l1,concatenaChar);

        //Test letra comun 2.
        codigoFuente.avanzar();
        testGenerico(l2,concatenaChar);

        //Test salto linea.
        codigoFuente.avanzar();
        testGenerico(l3,concatenaChar);
    }

    private static void testGenerico(String letra, AccionSemantica.ConcatenaChar concatenaChar){
        concatenaChar.ejecutar();
        char ultimoChar = concatenaChar.getUltimoChar();
        boolean resultado = letra.charAt(0) == ultimoChar;
        System.out.println("Esperado:"+letra+". Resultado:"+ultimoChar+"("+resultado+")");
    }
}
