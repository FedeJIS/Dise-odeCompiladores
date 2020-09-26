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
//public class Test_ME_pReservadas {
//    public static void main(String[] args) {
//        String pReservada = "WHILE";
//
//        String fuente = "WHILE";
//        System.out.println("Fuente:'"+fuente+"'");
//        testGenerico(fuente,pReservada);
//
//        System.out.println("###############################");
//
//        fuente = "WHILE{";
//        System.out.println("Fuente:'"+fuente+"'");
//        testGenerico(fuente,pReservada);
//
//        System.out.println("###############################");
//
//        pReservada = "WHILE_DO";
//        fuente = "WHILE_DO";
//        System.out.println("Fuente:'"+fuente+"'");
//        testGenerico(fuente,pReservada);
//    }
//
//    private static void testGenerico(String fuente, String pReservada){
//        List<String> lineas = new ArrayList<>();
//        lineas.add(fuente);
//        CodigoFuente cFuente = new CodigoFuente(lineas);
//
//        TablaDeSimbolos tS = new TablaDeSimbolos();
//        Reservado tPR = new Reservado();
//        tPR.agregar(pReservada);
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
//        System.out.println("PR buscada:"+pReservada+". Esta en la TPR?"+tPR.esReservada(pReservada));
//
//        System.out.print("Tokens generados: ");
//        for (Celda celda : analizadorLexico.getListaToken())
//            System.out.print(celda.getToken()+" ");
//        System.out.println();
//    }
//}
