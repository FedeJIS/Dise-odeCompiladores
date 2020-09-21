package analizador_lexico.maquina_estados;

import analizador_lexico.CodigoFuente;
import analizador_lexico.FileProcessor;
import analizador_lexico.Reservado;
import analizador_lexico.TablaDeSimbolos;

import java.util.ArrayList;
import java.util.List;

public class Test_ME_descartables {
    public static void main(String[] args){
        System.out.println("Espacio en blanco");
        testGenerico(" ",Estado.INICIAL);

        System.out.println("Tab");
        testGenerico("  ",Estado.INICIAL);

        System.out.println("Salto linea");
        testGenerico("\n",Estado.INICIAL);
    }

    public static void testGenerico(String linea, int estadoEsperado){
        List<String> lineas = new ArrayList<>();
        lineas.add(linea);
        CodigoFuente cFuente = new CodigoFuente(lineas);

        TablaDeSimbolos tS = new TablaDeSimbolos();
        Reservado tPR = new Reservado();

        MaquinaEstados maquinaEstados = new MaquinaEstados(new FileProcessor(),cFuente,tS,tPR);

        while (!cFuente.eof()){
            System.out.println(cFuente.simboloActual());
            maquinaEstados.transicionar(cFuente.simboloActual());
            cFuente.avanzar();
        }
        maquinaEstados.transicionarEOF();
        System.out.println();

        int estadoActual = maquinaEstados.getEstadoActual();

        System.out.println("Estado esperado:"+estadoEsperado+". Conseguido:"+estadoActual);
    }
}
