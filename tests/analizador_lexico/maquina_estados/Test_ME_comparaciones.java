package analizador_lexico.maquina_estados;

import analizador_lexico.AnalizadorLexico;
import util.CodigoFuente;
import util.FileProcessor;
import util.Reservado;
import util.tabla_simbolos.Celda;
import util.tabla_simbolos.TablaDeSimbolos;

import java.util.ArrayList;
import java.util.List;

public class Test_ME_comparaciones {
    public static void main(String[] args) {
        String fuente = "<";
        System.out.println("Fuente:'"+fuente+"'");
        testGenerico(fuente);

        System.out.println("###############################");

        fuente = "<=";
        System.out.println("Fuente:'"+fuente+"'");
        testGenerico(fuente);

        System.out.println("###############################");

        fuente = ">";
        System.out.println("Fuente:'"+fuente+"'");
        testGenerico(fuente);

        System.out.println("###############################");

        fuente = ">=";
        System.out.println("Fuente:'"+fuente+"'");
        testGenerico(fuente);

        System.out.println("###############################");

        fuente = "!";
        System.out.println("Fuente:'"+fuente+"'");
        testGenerico(fuente);

        System.out.println("###############################");

        fuente = "!=";
        System.out.println("Fuente:'"+fuente+"'");
        testGenerico(fuente);

        System.out.println("###############################");

        fuente = "=";
        System.out.println("Fuente:'"+fuente+"'");
        testGenerico(fuente);

        System.out.println("###############################");

        fuente = "==";
        System.out.println("Fuente:'"+fuente+"'");
        testGenerico(fuente);
    }

    private static void testGenerico(String fuente){
        List<String> lineas = new ArrayList<>();
        lineas.add(fuente);
        CodigoFuente cFuente = new CodigoFuente(lineas);

        TablaDeSimbolos tS = new TablaDeSimbolos();
        Reservado tPR = new Reservado();

        AnalizadorLexico analizadorLexico = new AnalizadorLexico();
        MaquinaEstados maquinaEstados = new MaquinaEstados(analizadorLexico,new FileProcessor(),cFuente,tS,tPR);

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

        System.out.print("Tokens generados: ");
        for (Celda celda : analizadorLexico.getListaToken())
            System.out.print(celda.getToken()+" ");
        System.out.println();
    }
}
