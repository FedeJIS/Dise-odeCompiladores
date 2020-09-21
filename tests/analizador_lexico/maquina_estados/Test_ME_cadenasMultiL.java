package analizador_lexico.maquina_estados;

import analizador_lexico.*;

import java.util.ArrayList;
import java.util.List;

public class Test_ME_cadenasMultiL {
    public static void main(String[] args) {
        String cadena = "\"A\nB\nC\""; //Cadena cerrada.

        testGenerico(cadena);

        System.out.println("###############################");

        cadena = "\"AAAAA"; //Cadena abierta.

        testGenerico(cadena);

        System.out.println("###############################");
    }

    private static void testGenerico(String cadena){
        List<String> lineas = new ArrayList<>();
        lineas.add(cadena);
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

        System.out.println("Lexema buscado:"+cadena+". Lexema encontrado:"+tS.getValor(cadena).getLexema());

        System.out.print("Tokens generados: ");
        for (Celda celda : maquinaEstados.getListaToken())
            System.out.print(celda.getToken()+" ");
        System.out.println();
    }
}
