package compilador;

import analizador_lexico.AnalizadorLexico;
import analizador_sintactico.Parser;
import util.CodigoFuente;
import util.FileProcessor;
import util.TablaNotificaciones;
import util.tabla_simbolos.TablaSimbolos;

import java.io.FileNotFoundException;

public class Compilador {
    private static final TablaSimbolos tablaS = new TablaSimbolos();

    public static void main(String[] args) {
        String pathCFuente = args[0];

        Parser parser = init(pathCFuente);
        System.out.println("TOKENS GENERADOS POR EL LEXICO:");
        parser.run();
        System.out.println();
        System.out.println("###############################################");

        saveOrPrint("/tabla_simbolos.txt",tablaS.toString(),"Tabla de simbolos generada en: '/tabla_simbolos.txt'.","Tabla de simbolos:");
        System.out.println("###############################################");

        saveOrPrint("/errores.txt",TablaNotificaciones.getErrores(),"Errores generados en: '/errores.txt'.","Errores:");
        System.out.println("###############################################");

        saveOrPrint("/warning.txt",TablaNotificaciones.getWarnings(),"Warnings generados en: '/warnings.txt'.","Warnings:");
        System.out.println("###############################################");
    }

    private static Parser init(String pathCFuente) {
        CodigoFuente cFuente = new CodigoFuente(FileProcessor.getLineas(pathCFuente));
        AnalizadorLexico aLexico = new AnalizadorLexico(cFuente, tablaS);
        return new Parser(false, aLexico, tablaS);
    }

    private static void saveOrPrint(String path, String toSave, String mensajeExito, String tituloFallo){
        try {
            FileProcessor.escribirArchivo(path, toSave,false);
            System.out.println(mensajeExito);
        } catch (FileNotFoundException fileNotFoundEx){
            System.out.println("Fallo de escritura: "+fileNotFoundEx.getMessage());
            System.out.println(tituloFallo);
            System.out.println(toSave);
        }
    }
}
