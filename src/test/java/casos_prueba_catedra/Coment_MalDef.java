package casos_prueba_catedra;

import compilador.Compilador;

public class Coment_MalDef { //TODO ARREGLAR GRAMATICA PARA COMENT MAL DEF EN MEDIO DEL FUENTE.
    public static void main(String[] args) {
        String lineasCFuente =
                "%%NO RECONOCE EL SIGUIENTE COMENTARIO PORQUE FALTA UN '%'. TOMA EL CONTENIDO COMO UN IDENTIFICADOR" + '\n' +
                "%mal_coment" + '\n' +
                "x = 5_ui;";
        Compilador.compilar(lineasCFuente);
    }
}
