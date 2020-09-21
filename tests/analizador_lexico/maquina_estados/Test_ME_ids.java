package analizador_lexico.maquina_estados;

import analizador_lexico.CodigoFuente;
import analizador_lexico.FileProcessor;
import analizador_lexico.Reservado;
import analizador_lexico.TablaDeSimbolos;

import java.util.ArrayList;
import java.util.List;

public class Test_ME_ids {
    public static void main(String[] args) {
        String lexema = "est";

        String fuente = "est";
        System.out.println("Fuente:'"+fuente+"'");
        testGenerico(fuente,lexema);

        System.out.println("###############################");

        fuente = "est/";
        System.out.println("Fuente:'"+fuente+"'");
        testGenerico(fuente,lexema);
    }

    private static void testGenerico(String fuente, String lexema){
        List<String> lineas = new ArrayList<>();
        lineas.add(fuente);
        CodigoFuente cFuente = new CodigoFuente(lineas);

        TablaDeSimbolos tS = new TablaDeSimbolos();
        Reservado tPR = new Reservado();

        MaquinaEstados maquinaEstados = new MaquinaEstados(new FileProcessor(),cFuente,tS,tPR);

        /* Inic lexico */
        while (!cFuente.eof()){
            System.out.print(cFuente.simboloActual());
            maquinaEstados.transicionar(cFuente.simboloActual());
            System.out.println(". Estado actual:"+maquinaEstados.getEstadoActual());
            cFuente.avanzar();
        }
        maquinaEstados.transicionarEOF();
        System.out.println("[EOF]. Estado actual:"+maquinaEstados.getEstadoActual());
        /* Fin lexico */

        int estadoActual = maquinaEstados.getEstadoActual();

        System.out.println("Estado esperado:"+ Estado.INICIAL +". Conseguido:"+estadoActual);

        System.out.println("Lexema buscado:"+lexema+". Lexema encontrado:"+tS.getValor(lexema).getLexema());
    }
}
