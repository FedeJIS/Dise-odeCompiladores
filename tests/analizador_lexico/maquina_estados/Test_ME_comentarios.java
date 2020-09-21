package analizador_lexico.maquina_estados;

import analizador_lexico.*;

import java.util.ArrayList;
import java.util.List;

public class Test_ME_comentarios {
    public static void main(String[] args) {
        String fuente = "%%ESTO ES UN COMENTARIO TODO SE IGNORA ''¿'&/())(!7823'";
        System.out.println("Fuente:'"+fuente+"'");
        testGenerico(fuente);


    }

    private static void testGenerico(String fuente){
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

        System.out.println("Estado esperado:"+ Estado.FINAL +". Conseguido:"+estadoActual);

        System.out.print("Tokens generados: ");
        for (Celda celda : maquinaEstados.getListaToken())
            System.out.print(celda.getToken()+" ");
        System.out.println();
    }
}
