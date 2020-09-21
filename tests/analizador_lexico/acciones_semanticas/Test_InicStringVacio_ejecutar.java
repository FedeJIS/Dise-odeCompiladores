package analizador_lexico.acciones_semanticas;

import analizador_lexico.AccionSemantica;

public class Test_InicStringVacio_ejecutar {
    public static void main(String[] args) {
        AccionSemantica.InicStringVacio inicStringVacio = new AccionSemantica.InicStringVacio();

        inicStringVacio.ejecutar();
        boolean resultado = inicStringVacio.isStringVacio();
        System.out.println("Esperado:true. Resultado:"+resultado);
    }
}
