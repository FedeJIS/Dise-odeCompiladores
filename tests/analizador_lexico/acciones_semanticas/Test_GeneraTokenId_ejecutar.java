package analizador_lexico.acciones_semanticas;

import analizador_lexico.AccionSemantica;
import analizador_lexico.CodigoFuente;
import analizador_lexico.TablaDeSimbolos;
import analizador_lexico.maquina_estados.MaquinaEstados;

import java.util.ArrayList;
import java.util.List;

public class Test_GeneraTokenId_ejecutar {
    public static void main(String[] args) {
        String lexema = "abcd";
        CodigoFuente codigoFuente = inic(lexema);

        TablaDeSimbolos tS = new TablaDeSimbolos();

        AccionSemantica.GeneraTokenId generaTokenId =
                new AccionSemantica.GeneraTokenId(new MaquinaEstados(null,codigoFuente,tS,null),tS,1);

        generaTokenId.ejecutar();

        System.out.println("Esperado:"+lexema+". Obtenido:"+tS.getValor(lexema).getLexema());
    }

    private static CodigoFuente inic(String lexema){
        List<String> lineas = new ArrayList<>();
        lineas.add(lexema);

        CodigoFuente codigoFuente = new CodigoFuente(lineas);

        AccionSemantica.InicStringVacio inicStringVacio = new AccionSemantica.InicStringVacio();
        inicStringVacio.ejecutar();

        AccionSemantica.ConcatenaChar concatenaChar = new AccionSemantica.ConcatenaChar(codigoFuente);


        while (!codigoFuente.eof()){
            concatenaChar.ejecutar();
            codigoFuente.avanzar();
        }
        return codigoFuente;
    }
}
