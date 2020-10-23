package analizador_lexico.acciones_semanticas;

import analizador_lexico.maquina_estados.MaquinaEstados;
import util.TablaNotificaciones;
import util.TablaPalabrasR;

/**
 * Chequea que la palabra almacenada sea una palabra reservada valida y agrega el token a la lista de tokens.
 */
public class GeneraTokenPR extends AccionSemantica {
    private final MaquinaEstados maquinaEstados;

    public GeneraTokenPR(MaquinaEstados maquinaEstados) {
        this.maquinaEstados = maquinaEstados;
    }
    @Override
    public void ejecutar() {
        TablaPalabrasR.printPRs();
        String palabraR = getString();

        if (TablaPalabrasR.esReservada(palabraR)) {
            maquinaEstados.setVariablesSintactico(TablaPalabrasR.getToken(palabraR), ""); //No tiene lexema.
        } else {
            TablaNotificaciones.agregarError("Linea: "+maquinaEstados.getLineaActual()+": La palabra '" + palabraR + "' no es una palabra reservada valida.");
            maquinaEstados.reiniciar(); //Evita que la maquina quede en el estado final, para que el lexico no genere un token.
        }
    }
}
