//package analizador_lexico.acciones_semanticas;
//
//import analizador_lexico.AccionSemantica;
//import analizador_lexico.AnalizadorLexico;
//import util.CodigoFuente;
//import util.tabla_simbolos.TablaDeSimbolos;
//import analizador_lexico.maquina_estados.MaquinaEstados;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class Test_GeneraTokenId_ejecutar {
//    public static void main(String[] args) {
//        String lexema = "abcd";
//        CodigoFuente codigoFuente = inic(lexema);
//
//        TablaDeSimbolos tS = new TablaDeSimbolos();
//
//        AnalizadorLexico analizadorLexico = new AnalizadorLexico();
//        AccionSemantica.GeneraTokenTS generaTokenTS =
//                new AccionSemantica.GeneraTokenTS(new MaquinaEstados(analizadorLexico,null,codigoFuente,tS,null),tS,1);
//
//        generaTokenTS.ejecutar();
//
//        System.out.println("Esperado:"+lexema+". Obtenido:"+tS.getValor(lexema).getLexema());
//    }
//
//    private static CodigoFuente inic(String lexema){
//        List<String> lineas = new ArrayList<>();
//        lineas.add(lexema);
//
//        CodigoFuente codigoFuente = new CodigoFuente(lineas);
//
//        AccionSemantica.InicStringVacio inicStringVacio = new AccionSemantica.InicStringVacio();
//        inicStringVacio.ejecutar();
//
//        AccionSemantica.ConcatenaChar concatenaChar = new AccionSemantica.ConcatenaChar(codigoFuente);
//
//
//        while (!codigoFuente.eof()){
//            concatenaChar.ejecutar();
//            codigoFuente.avanzar();
//        }
//        return codigoFuente;
//    }
//}
