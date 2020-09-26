//package analizador_lexico.maquina_estados;
//
//import analizador_lexico.AnalizadorLexico;
//import util.CodigoFuente;
//import util.FileProcessor;
//import util.Reservado;
//import util.tabla_simbolos.Celda;
//import util.tabla_simbolos.TablaDeSimbolos;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class Test_ME_ids {
//    public static void main(String[] args) {
//        String lexema = "est";
//
//        String fuente = "est";
//        System.out.println("Fuente:'"+fuente+"'");
//        testGenerico(fuente,lexema);
//
//        System.out.println("###############################");
//
//        fuente = "est/";
//        System.out.println("Fuente:'"+fuente+"'");
//        testGenerico(fuente,lexema);
//
//        System.out.println("###############################");
//
//        lexema = "est_";
//        fuente = "est_";
//        System.out.println("Fuente:'"+fuente+"'");
//        testGenerico(fuente,lexema);
//
//        System.out.println("###############################");
//
//        lexema = "e_st";
//        fuente = "e_st";
//        System.out.println("Fuente:'"+fuente+"'");
//        testGenerico(fuente,lexema);
//    }
//
//    private static void testGenerico(String fuente, String lexema){
//        List<String> lineas = new ArrayList<>();
//        lineas.add(fuente);
//        CodigoFuente cFuente = new CodigoFuente(lineas);
//
//        TablaDeSimbolos tS = new TablaDeSimbolos();
//        Reservado tPR = new Reservado();
//
//        AnalizadorLexico analizadorLexico = new AnalizadorLexico();
//        MaquinaEstados maquinaEstados = new MaquinaEstados(analizadorLexico,new FileProcessor(),cFuente,tS,tPR);
//
//        /* Inic lexico */
//        while (!cFuente.eof()){
//            System.out.print(cFuente.simboloActual());
//            maquinaEstados.transicionar(cFuente.simboloActual());
//            System.out.println(". Estado actual:"+maquinaEstados.getEstadoActual());
//            cFuente.avanzar();
//        }
//        maquinaEstados.transicionarEOF();
//        System.out.println("[EOF]. Estado actual:"+maquinaEstados.getEstadoActual());
//        /* Fin lexico */
//
//        int estadoActual = maquinaEstados.getEstadoActual();
//
//        System.out.println("Estado esperado:"+ Estado.INICIAL +". Conseguido:"+estadoActual);
//
//        System.out.println("Lexema buscado:"+lexema+". Lexema encontrado:"+tS.getValor(lexema).getLexema());
//
//        System.out.print("Tokens generados: ");
//        for (Celda celda : analizadorLexico.getListaToken())
//            System.out.print(celda.getToken()+" ");
//        System.out.println();
//    }
//}
