package compilador;

import analizador_lexico.AnalizadorLexico;
import analizador_sintactico.Parser;
import generacion_asm.GeneradorAssembler;
import generacion_c_intermedio.MultiPolaca;
import generacion_c_intermedio.Polaca;
import util.*;
import util.tabla_simbolos.TablaSimbolos;

public class Compilador {
    private static final TablaSimbolos tablaS = new TablaSimbolos();
    private static Polaca polacaProgram;
    private static MultiPolaca polacaProcs;

    public static void compilar(String lineasCFuente, boolean imprimirPolaca, boolean imprimirOtros){
        inicTablaPR();
        inicValorStringTokens();
        GeneradorAssembler.reset(tablaS);

        CodigoFuente cFuente = new CodigoFuente(lineasCFuente);
        AnalizadorLexico aLexico = new AnalizadorLexico(cFuente, tablaS);
        Parser parser = new Parser(aLexico,tablaS);

        parser.run();

        polacaProgram = parser.getPolacaProgram();
        polacaProcs = parser.getPolacaProcs();

        if (imprimirPolaca) {
            System.out.println(tablaS.toAsm());
            System.out.println("###POLACA PROGRAM###\n"+polacaProgram.toString());
            System.out.println("###POLACA PROCEDIMIENTOS###\n"+polacaProcs.toString());
        }

        if (imprimirOtros) finCompilacion();
    }

    public static void compilar(String pathSrc, String basePathDest){
        //Compilacion
        if (!FileProcessor.existeArchivo(pathSrc)) {
            System.out.println("No existe el archivo: '" + pathSrc + "'.");
            return;
        }

        compilar(FileProcessor.getLineasFuente(pathSrc),false,false);

        //Generacion asm
        try {
            FileProcessor.escribirArchivo(basePathDest+"_asm.asm",getAsm());
            System.out.println("Assembler generado exitosamente.");
        } catch (IllegalStateException illStEx){
            System.out.println(illStEx.getMessage());
            System.out.println("Assembler no generado.");
        }

        //Guardado de resultados
        String resultados =
                "###TABLA SIMBOLOS###\n" + tablaS.toString() +
                "--------------------------------------------\n" +
                TablaNotificaciones.getResultados() + '\n' +
                "--------------------------------------------\n" +
                "###POLACA PROGRAM###\n" + polacaProgram.toString() +
                "--------------------------------------------\n" +
                "###POLACA PROCEDIMENTOS###\n" + polacaProcs.toString()
                ;
        FileProcessor.escribirArchivo(basePathDest+"_salidas.txt",resultados);



        //Clear de estructuras estaticas
        tablaS.clear();
    }

    private static String getAsm() {
        StringBuilder asmProcsBuilder = new StringBuilder();
        for (String instrAsm : GeneradorAssembler.generaAsmDeclProc(polacaProcs))
            asmProcsBuilder.append(instrAsm).append('\n');

        StringBuilder asmProgramBuilder = new StringBuilder();
        for (String instrAsm : GeneradorAssembler.generaAsm(polacaProgram))
            asmProgramBuilder.append(instrAsm).append('\n');

        return ".386\n" +
                ".model flat, stdcall\n" +
                "option casemap :none\n" +
                "include \\masm32\\include\\windows.inc\n" +
                "include \\masm32\\include\\kernel32.inc\n" +
                "include \\masm32\\include\\user32.inc\n" +
                "includelib \\masm32\\lib\\kernel32.lib\n" +
                "includelib \\masm32\\lib\\user32.lib\n" +
                "include \\masm32\\include\\masm32rt.inc\n" +
                "dll_dllcrt0 PROTO C\n" +
                "printf PROTO C :VARARG\n\n" +
                ".DATA\n" +
                tablaS.toAsm() + "\n" +
                "@resta_neg DB 'Error: Resultado de resta menor a cero.', 0" + "\n" +
                "@recursion DB 'Error: Recursiones en procedimientos no permitidas.', 0" + "\n" +
                "@ni_superado DB 'Error: NI para el procedimiento superado.', 0" + "\n" +
                ".CODE\n" +
                asmProcsBuilder.toString() + "\n\n" +
                "START:\n" +
                asmProgramBuilder.toString() +
                "JMP L_final" + "\n\n" +
                "L_resta_neg:" + '\n' +
                "invoke MessageBox, NULL, addr @resta_neg, addr @resta_neg , MB_OK" + '\n' +
                "JMP L_final" + "\n\n" +
                "L_recursion:" + '\n' +
                "invoke MessageBox, NULL, addr @recursion, addr @recursion , MB_OK" + '\n' +
                "JMP L_final" + "\n\n" +
                "L_ni_superado:" + '\n' +
                "invoke MessageBox, NULL, addr @ni_superado, addr @ni_superado , MB_OK" + '\n' +
                "JMP L_final" + "\n\n" +
                "L_final:" + '\n' +
                "invoke ExitProcess, 0" + '\n' +
                "END START";
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
