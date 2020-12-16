package analizador_lexico.acciones_semanticas;

import analizador_lexico.maquina_estados.MaquinaEstados;
import util.TablaNotificaciones;
import util.tabla_simbolos.TablaSimbolos;

/**
 * Dada la parte numerica de un double (entera y decimal), se verifica si el exponente es correcto.
 * Luego revisa si el double baseNumDouble elevado al exponente se encuentra en el rango dado.
 */
public class GeneraTokenDouble extends AccionSemantica {
    private final static double LIM_INF_DOUBLE_POS = 2.2250738585072014;
    private final static double LIM_SUP_DOUBLE_POS = 1.7976931348623157;
    private final static int MAX_DOUBLE_EXP = 308;

    private final MaquinaEstados maquinaEstados;

    private final TablaSimbolos tablaS;

    private final short token;

    public GeneraTokenDouble(MaquinaEstados maquinaEstados, TablaSimbolos tablaS, short token) {
        this.maquinaEstados = maquinaEstados;
        this.tablaS = tablaS;
        this.token = token;
    }

    public void ejecutar() {
        if (baseNumDoubleInicializado()) {
            int exp = 0; //Vale 0 por defecto (Util para los casos donde no se tiene exponente).
            String expString = getString();

            if (expString.isEmpty() //No se cargo el exponente.
                    || expString.equals("-") //Se cargo solo un '-' para el exponente.
                    || expString.equals("+")) //Se cargo solo un '+' para el exponente.
                TablaNotificaciones.agregarWarning(maquinaEstados.getLineaActual(), "Falto el exponente del numero DOUBLE. El exponente es 0 por defecto");
            else exp = Integer.parseInt(expString);

            double baseNumDouble = getBaseNumDouble();
            if (doubleValido(baseNumDouble, exp)) {
                double doubleNormalizado = baseNumDouble * Math.pow(10, exp);

                tablaS.agregarEntrada(token, String.valueOf(doubleNormalizado), "DOUBLE");
                tablaS.setDeclaracionEntrada(String.valueOf(doubleNormalizado), true);
                tablaS.setUsoEntrada(String.valueOf(doubleNormalizado), "CTE");
                maquinaEstados.setVariablesSintactico(token, String.valueOf(doubleNormalizado));
            }
        }
    }

    private boolean doubleValido(double baseNumDouble, double expNumDouble) {
        boolean doubleValido = true;
        if (expFueraRango(expNumDouble)) {
            maquinaEstados.reiniciar(); //Evita que la maquina quede en el estado final, para que el lexico no genere un token.
            doubleValido = false;
            TablaNotificaciones.agregarError(maquinaEstados.getLineaActual(),"El exponente '" + expNumDouble + "' esta fuera de rango.");
        }

        if (doubleFueraRango(baseNumDouble, expNumDouble)) {
            maquinaEstados.reiniciar(); //Evita que la maquina quede en el estado final, para que el lexico no genere un token.
            doubleValido = false;
            TablaNotificaciones.agregarError(maquinaEstados.getLineaActual(),"El numero DOUBLE '" + baseNumDouble * Math.pow(10, expNumDouble) + "' esta fuera de rango.");
        }

        return doubleValido;
    }

    private boolean expFueraRango(double expNumDouble) {
        return expNumDouble < -MAX_DOUBLE_EXP || expNumDouble > MAX_DOUBLE_EXP;
    }

    private boolean doubleFueraRango(double baseNumDouble, double expNumDouble) {
        double min = LIM_INF_DOUBLE_POS * Math.pow(10, -MAX_DOUBLE_EXP);
        double max = LIM_SUP_DOUBLE_POS * Math.pow(10, MAX_DOUBLE_EXP);

        double doubleNormalizado = baseNumDouble * Math.pow(10, expNumDouble);

        return doubleNormalizado != 0.0 && (doubleNormalizado < min || doubleNormalizado > max);
    }
}
