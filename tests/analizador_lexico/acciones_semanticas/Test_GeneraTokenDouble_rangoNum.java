package analizador_lexico.acciones_semanticas;

import analizador_lexico.AccionSemantica;

public class Test_GeneraTokenDouble_rangoNum {
    public static void main(String[] args) {
        double LIM_INF_DOUBLE_POS = 2.2250738585072014;
        double LIM_SUP_DOUBLE_POS = 1.7976931348623157;

        AccionSemantica.GeneraTokenDouble generaTokenDouble =
                new AccionSemantica.GeneraTokenDouble(null,null,0);

        System.out.println("Esperado: false. Resultado:"+generaTokenDouble.doubleFueraRango(LIM_INF_DOUBLE_POS,-308));

        System.out.println("Esperado: false. Resultado:"+generaTokenDouble.doubleFueraRango(LIM_SUP_DOUBLE_POS,308));

        System.out.println("Esperado: true. Resultado:"+generaTokenDouble.doubleFueraRango(LIM_INF_DOUBLE_POS-1,-308));

        System.out.println("Esperado: true. Resultado:"+generaTokenDouble.doubleFueraRango(LIM_SUP_DOUBLE_POS+0.0000000000000001,308));
    }
}
