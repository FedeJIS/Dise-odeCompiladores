package compilador;

import analizador_lexico.AnalizadorLexico;
import analizador_sintactico.Parser;
import util.CodigoFuente;
import util.ReprTokens;
import util.TablaNotificaciones;
import util.TablaPalabrasR;
import util.tabla_simbolos.TablaSimbolos;

public class Compilador {
    private static final TablaSimbolos tablaS = new TablaSimbolos();

    public static void compilar(String lineasCFuente, boolean imprimirPolaca, boolean imprimirOtros){
        inicTablaPR();
        inicValorStringTokens();

        CodigoFuente cFuente = new CodigoFuente(lineasCFuente);
        AnalizadorLexico aLexico = new AnalizadorLexico(cFuente, tablaS);
        Parser parser = new Parser(false,aLexico,tablaS);

        parser.run();

        if (imprimirPolaca) {
            parser.printPolaca();
            parser.printPolacaProcs();
        }
        if (imprimirOtros) finCompilacion();
    }

    private static void inicTablaPR() {
        TablaPalabrasR.clear();
        TablaPalabrasR.agregar("UINT", Parser.UINT);
        TablaPalabrasR.agregar("DOUBLE", Parser.DOUBLE);
        TablaPalabrasR.agregar("IF", Parser.IF);
        TablaPalabrasR.agregar("THEN", Parser.THEN);
        TablaPalabrasR.agregar("ELSE", Parser.ELSE);
        TablaPalabrasR.agregar("END_IF", Parser.END_IF);
        TablaPalabrasR.agregar("LOOP", Parser.LOOP);
        TablaPalabrasR.agregar("UNTIL", Parser.UNTIL);
        TablaPalabrasR.agregar("OUT", Parser.OUT);
        TablaPalabrasR.agregar("PROC", Parser.PROC);
        TablaPalabrasR.agregar("VAR", Parser.VAR);
        TablaPalabrasR.agregar("NI", Parser.NI);
    }

    private static void inicValorStringTokens(){
        ReprTokens.clear();
        ReprTokens.add(AnalizadorLexico.T_EOF,"EOF");
        ReprTokens.add((short) '<',"<");
        ReprTokens.add((short) '>',">");
        ReprTokens.add((short) '=',"=");
        ReprTokens.add(Parser.COMP_MENOR_IGUAL,"<=");
        ReprTokens.add(Parser.COMP_MAYOR_IGUAL,">=");
        ReprTokens.add(Parser.COMP_DISTINTO,"!=");
        ReprTokens.add(Parser.COMP_IGUAL,"==");
    }

    private static void finCompilacion() {
        System.out.println();
        System.out.println("###############################################");

        print(tablaS.toString(), "Tabla de simbolos:");
        System.out.println("###############################################");

        print(TablaNotificaciones.getErrores(), "Errores:");
        System.out.println("###############################################");

        print(TablaNotificaciones.getWarnings(), "Warnings:");
        System.out.println("###############################################");
    }

    private static void print(String toPrint, String titulo){
        System.out.println(titulo);
        System.out.println(toPrint);
    }
}
