package analizador_lexico.acciones_semanticas;

import analizador_lexico.maquina_estados.MaquinaEstados;
import util.TablaNotificaciones;
import util.tabla_simbolos.TablaSimbolos;

/**
 * Verifica los limites de un numero entero y genera el token asociado en caso de que corresponda.
 */
public class GeneraTokenUINT extends AccionSemantica {
    private final static int LIMITE_INT = (int) (Math.pow(2, 16) - 1);

    private final MaquinaEstados maquinaEstados;

    private final TablaSimbolos tablaS;

    private final short token;

    public GeneraTokenUINT(MaquinaEstados maquinaEstados, TablaSimbolos tablaS, short token) {
        this.maquinaEstados = maquinaEstados;
        this.tablaS = tablaS;
        this.token = token;
    }

    @Override
    public void ejecutar() {
        String numeroString = getString();
        int numero = Integer.parseInt(numeroString);
        if (numeroEnRango(numero)) {
            tablaS.agregarEntrada(token, numeroString, "UINT");
            tablaS.setDeclaracionEntrada(numeroString,true);
            tablaS.setUsoEntrada(numeroString,"CTE");
            maquinaEstados.setVariablesSintactico(token, numeroString);
        } else {
            TablaNotificaciones.agregarError(maquinaEstados.getLineaActual(),"El numero UINT '" + numeroString + "' esta fuera de rango.");
            maquinaEstados.reiniciar(); //Evita que la maquina quede en el estado final, para que el lexico no genere un token.
        }
    }

    private boolean numeroEnRango(int numero) {
        return numero >= 0 && numero <= LIMITE_INT;
    }
}
