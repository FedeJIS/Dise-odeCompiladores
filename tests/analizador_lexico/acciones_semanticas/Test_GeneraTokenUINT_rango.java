package analizador_lexico.acciones_semanticas;

import analizador_lexico.AccionSemantica;

public class Test_GeneraTokenUINT_rango {
    public static void main(String[] args) {
        int LIMITE_INT=(int)(Math.pow(2,16)-1);

        AccionSemantica.GeneraTokenUINT generaTokenUINT =
                new AccionSemantica.GeneraTokenUINT(null,null,0);

        System.out.println("Esperado: true. Resultado:"+generaTokenUINT.numeroEnRango(LIMITE_INT));

        System.out.println("Esperado: true. Resultado:"+generaTokenUINT.numeroEnRango(0));

        System.out.println("Esperado: false. Resultado:"+generaTokenUINT.numeroEnRango(LIMITE_INT+1));

        System.out.println("Esperado: false. Resultado:"+generaTokenUINT.numeroEnRango(-1));
    }
}
