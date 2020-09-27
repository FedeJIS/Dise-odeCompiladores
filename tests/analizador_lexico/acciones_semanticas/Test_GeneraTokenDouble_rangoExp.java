package analizador_lexico.acciones_semanticas;

import analizador_lexico.AccionSemantica;

public class Test_GeneraTokenDouble_rangoExp {
    public static void main(String[] args) {
        AccionSemantica.GeneraTokenDouble generaTokenDouble =
                new AccionSemantica.GeneraTokenDouble(null,null,0);

        //-308 es el minimo.
        System.out.println("Esperado: false. Resultado:"+generaTokenDouble.expFueraRango(-308));

        //-309 esta fuera de rango.
        System.out.println("Esperado: true. Resultado:"+generaTokenDouble.expFueraRango(-309));

        //308 es el maximo.
        System.out.println("Esperado: false. Resultado:"+generaTokenDouble.expFueraRango(308));

        //309 esta fuera de rango.
        System.out.println("Esperado: true. Resultado:"+generaTokenDouble.expFueraRango(309));
    }
}
