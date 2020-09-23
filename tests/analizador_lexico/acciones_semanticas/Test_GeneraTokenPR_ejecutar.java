package analizador_lexico.acciones_semanticas;

import analizador_lexico.AccionSemantica;
import analizador_lexico.AnalizadorLexico;
import util.CodigoFuente;
import util.Reservado;
import analizador_lexico.maquina_estados.MaquinaEstados;

import java.util.ArrayList;
import java.util.List;

public class Test_GeneraTokenPR_ejecutar {
    public static void main(String[] args) {
        String pR = "WHILE";
        CodigoFuente codigoFuente = inic(pR);

        Reservado tPR = new Reservado();
        tPR.agregar(pR);

        AnalizadorLexico analizadorLexico = new AnalizadorLexico();
        AccionSemantica.GeneraTokenPR generaTokenPR =
                new AccionSemantica.GeneraTokenPR(new MaquinaEstados(analizadorLexico,null,codigoFuente,null,tPR),tPR,1);

        generaTokenPR.ejecutar();

        System.out.println("Esperado:"+true+". Obtenido:"+tPR.esReservada(pR));
    }

    private static CodigoFuente inic(String pR){
        List<String> lineas = new ArrayList<>();
        lineas.add(pR);

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
