package analizador_lexico.maquina_estados;

public class Estado {
    public static final int INICIAL = 0, DETECCION_ID = 1, DETECCION_PR = 2, INICIO_COMENT = 3, CUERPO_COMENT = 4,
            COMP_MENOR = 5, COMP_MAYOR = 6, COMP_DISTINTO = 7, SIGNO_IGUAL = 8, CTE_PARTE_ENTERA = 9, CTE_UI_SUF1 = 10,
            CTE_UI_SUF2 = 11, CTE_UI_SUF3 = 12, CTE_PARTE_DECIM = 13, CTE_PARTE_EXP = 14, CADENA = 15,
            ERR_SIMBOLO_INV = 16, ERR_CADENA_ABIERTA = 17, FINAL = 18;

    public static final int TOTAL_ESTADOS = 19;
}
