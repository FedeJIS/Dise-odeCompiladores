package analizador_lexico.acciones_semanticas;

import analizador_lexico.AccionSemantica;
import analizador_lexico.CodigoFuente;

import java.util.ArrayList;
import java.util.List;

public class Test_TruncaId_ejecutar {
    public static void main(String[] args) {

        AccionSemantica.TruncaId truncaId = new AccionSemantica.TruncaId(null);

        //Test no truncar 1.
        testGenerico("1234567891234567891", false, truncaId); //19 caracteres.

        //Test no truncar 2.
        testGenerico("12345678912345678912", false, truncaId); //20 caracteres.

        //Test trucar.
        testGenerico("123456789123456789123", true, truncaId); //21 caracteres.
    }

    private static void testGenerico(String linea, boolean resultadoEsperado, AccionSemantica.TruncaId truncaId) {
        AccionSemantica.InicStringVacio inicStringVacio = new AccionSemantica.InicStringVacio();
        inicStringVacio.ejecutar();

        List<String> lineas = new ArrayList<>();
        lineas.add(linea);

        CodigoFuente codigoFuente = new CodigoFuente(lineas);
        AccionSemantica.ConcatenaChar concatenaChar = new AccionSemantica.ConcatenaChar(codigoFuente);

        while (!codigoFuente.eof()) {
            concatenaChar.ejecutar();
            codigoFuente.avanzar();
        }

        System.out.println("Esperado:"+resultadoEsperado+". Resultado:"+ truncaId.truncadoNecesario());
    }
}
