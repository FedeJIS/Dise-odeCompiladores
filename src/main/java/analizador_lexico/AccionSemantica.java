package analizador_lexico;

import analizador_lexico.maquina_estados.MaquinaEstados;
import util.CodigoFuente;
import util.TablaNotificaciones;
import util.TablaPalabrasR;
import util.tabla_simbolos.TablaSimbolos;

public class AccionSemantica {
    private static String sTemporal;

    private static double baseNumDouble = Double.NEGATIVE_INFINITY; // Utilizado para AS-double (parte numerica y parte exp)

    public AccionSemantica() {
    }

    /**
     * Metodo hook que se sobre escribe segun la accion particular.
     */
    public void ejecutar() {
    }

    /* ---Implementaciones--- */

//    public static class InicStringVacio extends AccionSemantica {
//        /**
//         * Inicializa un string en vacio.
//         */
//        @Override
//        public void ejecutar() {
//            sTemporal = "";
//        }
//    }

//    public static class ConcatenaChar extends AccionSemantica {
//        private final CodigoFuente codigoFuente;
//
//        public ConcatenaChar(CodigoFuente codigoFuente) {
//            this.codigoFuente = codigoFuente;
//        }
//
//        /**
//         * Concatena un caracter al final de un string.
//         */
//        @Override
//        public void ejecutar() {
//            sTemporal = sTemporal + codigoFuente.simboloActual();
//        }
//    }

//    public static class TruncaId extends AccionSemantica {
//        private final static int LIMITE_STRING = 20;
//
//        private final AnalizadorLexico aLexico;
//
//        public TruncaId(AnalizadorLexico aLexico) {
//            this.aLexico = aLexico;
//        }
//
//        /**
//         * Si se excede el limite de un string, se trunca y genera un WARNING.
//         */
//        @Override
//        public void ejecutar() {
//            if (LIMITE_STRING < sTemporal.length()) {
//                sTemporal = sTemporal.substring(0, LIMITE_STRING);
//                TablaNotificaciones.agregarWarning("Warning en la linea " + aLexico.getLineaActual() + ": Identificador truncado por superar limite de caracteres.");
//            }
//        }
//    }

//    public static class RetrocedeFuente extends AccionSemantica {
//        private final CodigoFuente codigoFuente;
//
//        public RetrocedeFuente(CodigoFuente codigoFuente) {
//            this.codigoFuente = codigoFuente;
//        }
//
//        /**
//         * Retrocede una posicion en el codigo fuente, para que se vuelva a leer el ultimo caracter leido.
//         */
//        @Override
//        public void ejecutar() {
//            codigoFuente.retroceder();
//        }
//    }

//    public static class GeneraTokenTS extends AccionSemantica {
//        private final MaquinaEstados maquinaEstados;
//
//        private final TablaSimbolos tablaS;
//
//        private final int token;
//
//        public GeneraTokenTS(MaquinaEstados maquinaEstados, TablaSimbolos tablaS, int token) {
//            this.maquinaEstados = maquinaEstados;
//            this.tablaS = tablaS;
//            this.token = token;
//        }
//
//        /**
//         * Si la TS contiene una entrada con el lexema actual, incrementa su numero de referencias en uno. Si no la
//         * contiene, la crea y agrega.
//         */
//        @Override
//        public void ejecutar() {
//            tablaS.agregarEntrada(token, sTemporal, "");
//            maquinaEstados.setVariablesSintactico(token, sTemporal);
//        }
//    }

//    public static class GeneraTokenPR extends AccionSemantica {
//        private final MaquinaEstados maquinaEstados;
//
//        private final TablaPalabrasR tablaPR;
//
//        public GeneraTokenPR(MaquinaEstados maquinaEstados, TablaPalabrasR tablaPR) {
//            this.maquinaEstados = maquinaEstados;
//            this.tablaPR = tablaPR;
//        }
//
//        /**
//         * Chequea que la palabra almacenada sea una palabra reservada valida y agrega el token a la lista de tokens.
//         */
//        @Override
//        public void ejecutar() {
//            if (tablaPR.esReservada(sTemporal)) {
//                maquinaEstados.setVariablesSintactico(tablaPR.getToken(sTemporal), ""); //No tiene lexema.
//            } else {
//                TablaNotificaciones.agregarError("Linea: "+maquinaEstados.getLineaActual()+": La palabra '" + sTemporal + "' no es una palabra reservada valida.");
//                maquinaEstados.reiniciar(); //Evita que la maquina quede en el estado final, para que el lexico no genere un token.
//            }
//        }
//    }

//    public static class GeneraTokenLiteral extends AccionSemantica {
//        private final MaquinaEstados maquinaEstados;
//
//        private final CodigoFuente cFuente;
//
//        public GeneraTokenLiteral(MaquinaEstados maquinaEstados, CodigoFuente cFuente) {
//            this.maquinaEstados = maquinaEstados;
//            this.cFuente = cFuente;
//        }
//
//        @Override
//        public void ejecutar() {
//            int token = cFuente.simboloActual(); //Conversion implicita de char a ASCII.
//            maquinaEstados.setVariablesSintactico(token, ""); //No tiene lexema.
//        }
//    }

//    public static class GeneraTokenParticular extends AccionSemantica {
//        private final MaquinaEstados maquinaEstados;
//
//        private final int token;
//
//        public GeneraTokenParticular(MaquinaEstados maquinaEstados, int token) {
//            this.maquinaEstados = maquinaEstados;
//            this.token = token;
//        }
//
//        @Override
//        public void ejecutar() {
//            maquinaEstados.setVariablesSintactico(token, ""); //No tiene lexema.
//        }
//    }

//    /**
//     * Consumir caracter. Esta AS esta solo para que quede claro que se consume un caracter.
//     * No tiene ninguna otra utilidad.
//     */
//    public static class ConsumeChar extends AccionSemantica {
//    }

//    public static class GeneraTokenUINT extends AccionSemantica {
//        private final static int LIMITE_INT = (int) (Math.pow(2, 16) - 1);
//
//        private final MaquinaEstados maquinaEstados;
//
//        private final TablaSimbolos tablaS;
//
//        private final int token;
//
//        public GeneraTokenUINT(MaquinaEstados maquinaEstados, TablaSimbolos tablaS, int token) {
//            this.maquinaEstados = maquinaEstados;
//            this.tablaS = tablaS;
//            this.token = token;
//        }
//
//        /**
//         * Verifica los limites de un numero entero.
//         */
//        @Override
//        public void ejecutar() {
//            int numero = Integer.parseInt(sTemporal); //No genera NumberFormatEx porque solo se cargan digitos al string.
//            if (numeroEnRango(numero)) {
//                tablaS.agregarEntrada(token, sTemporal, "UINT");
//                maquinaEstados.setVariablesSintactico(token, sTemporal);
//            } else {
//                TablaNotificaciones.agregarError("Linea "+maquinaEstados.getLineaActual()+": El numero UINT '" + sTemporal + "' esta fuera de rango.");
//                maquinaEstados.reiniciar(); //Evita que la maquina quede en el estado final, para que el lexico no genere un token.
//            }
//        }
//
//        public boolean numeroEnRango(int numero) {
//            return numero >= 0 && numero <= LIMITE_INT;
//        }
//    }

//    public static class ParseBaseDouble extends AccionSemantica {
//        /**
//         * Verifica si la parte numerica es un numero double y lo asigna a numeroIntD
//         * Si es invalido, numeroIntD se vuelve Double.NEGATIVEINFINITY
//         */
//        public void ejecutar() {
//            if (sTemporal.equals(".")) baseNumDouble = 0;
//            else baseNumDouble = Double.parseDouble(sTemporal); //No genera NumberFormatEx porque solo se cargan digitos al string.
//            sTemporal = ""; //Reinicia el string temporal.
//        }
//    }

//    public static class GeneraTokenDouble extends AccionSemantica {
//        private final static double LIM_INF_DOUBLE_POS = 2.2250738585072014;
//        private final static double LIM_SUP_DOUBLE_POS = 1.7976931348623157;
//        private final static int MAX_DOUBLE_EXP = 308;
//
//        private final MaquinaEstados maquinaEstados;
//
//        private final TablaSimbolos tablaS;
//
//        private final int token;
//
//        public GeneraTokenDouble(MaquinaEstados maquinaEstados, TablaSimbolos tablaS, int token) {
//            this.maquinaEstados = maquinaEstados;
//            this.tablaS = tablaS;
//            this.token = token;
//        }
//
//        /**
//         * Dada la parte numerica de un double (entera y decimal), se verifica si el exponente es correcto.
//         * Luego revisa si el double baseNumDouble elevado al exponente se encuentra en el rango dado.
//         */
//        public void ejecutar() {
//            if (baseNumDouble != Double.NEGATIVE_INFINITY) {
//                int expNumDouble = 0; //Vale 0 por defecto (Util para los casos donde no se tiene exponente).
//                if (!sTemporal.isEmpty() && !sTemporal.equals("-") && !sTemporal.equals("+"))
//                    expNumDouble = Integer.parseInt(sTemporal);
//                else
//                    TablaNotificaciones.agregarWarning("Linea "+maquinaEstados.getLineaActual()+": Falto el exponente del numero DOUBLE. El exponente es 0 por defecto");
//
//                if (doubleValido(baseNumDouble, expNumDouble)) {
//                    double doubleNormalizado = baseNumDouble * Math.pow(10, expNumDouble);
//
//                    tablaS.agregarEntrada(token, String.valueOf(doubleNormalizado), "DOUBLE");
//                    maquinaEstados.setVariablesSintactico(token, String.valueOf(doubleNormalizado));
//                }
//            }
//        }
//
//        private boolean doubleValido(double baseNumDouble, double expNumDouble) {
//            boolean doubleValido = true;
//            if (expFueraRango(expNumDouble)) {
//                maquinaEstados.reiniciar(); //Evita que la maquina quede en el estado final, para que el lexico no genere un token.
//                doubleValido = false;
//                TablaNotificaciones.agregarError("Linea "+maquinaEstados.getLineaActual()+": El exponente '" + expNumDouble + "' esta fuera de rango.");
//            }
//
//            if (doubleFueraRango(baseNumDouble, expNumDouble)) {
//                maquinaEstados.reiniciar(); //Evita que la maquina quede en el estado final, para que el lexico no genere un token.
//                doubleValido = false;
//                TablaNotificaciones.agregarError("Linea "+maquinaEstados.getLineaActual()+": El numero DOUBLE '" + baseNumDouble * Math.pow(10, expNumDouble) + "' esta fuera de rango.");
//            }
//
//            return doubleValido;
//        }
//
//        public boolean expFueraRango(double expNumDouble) {
//            return expNumDouble < -MAX_DOUBLE_EXP || expNumDouble > MAX_DOUBLE_EXP;
//        }
//
//        public boolean doubleFueraRango(double baseNumDouble, double expNumDouble) {
//            double min = LIM_INF_DOUBLE_POS * Math.pow(10, -MAX_DOUBLE_EXP);
//            double max = LIM_SUP_DOUBLE_POS * Math.pow(10, MAX_DOUBLE_EXP);
//
//            double doubleNormalizado = baseNumDouble * Math.pow(10, expNumDouble);
//
//            return doubleNormalizado != 0.0 && (doubleNormalizado < min || doubleNormalizado > max);
//        }
//    }

//    public static class CuentaSaltoLinea extends AccionSemantica {
//        /**
//         * Incrementa en uno la cantidad de lineas de un archivo.
//         */
//        private int cantLineas = 1;
//
//        @Override
//        public void ejecutar() {
//            cantLineas++;
//        }
//
//        public int getCantLineas() {
//            return cantLineas;
//        }
//    }

//    public static class NotificaError extends AccionSemantica {
//        private final String mensaje;
//        private final AnalizadorLexico aLexico;
//        private final CodigoFuente cFuente;
//        private final boolean simboloNoReconocido;
//
//        public NotificaError(String mensaje, AnalizadorLexico aLexico, CodigoFuente cFuente, boolean simboloNoReconocido) {
//            this.mensaje = mensaje;
//            this.aLexico = aLexico;
//            this.cFuente = cFuente;
//            this.simboloNoReconocido = simboloNoReconocido;
//        }
//
//        /**
//         * Accion semantica auxiliar para notificar errores.
//         */
//        @Override
//        public void ejecutar() {
//            String error = "Linea "+aLexico.getLineaActual()+": ";
//            if (simboloNoReconocido)
//                error += "Simbolo '" + cFuente.simboloActual() + "' no reconocido.";
//            else error += mensaje;
//
//            TablaNotificaciones.agregarError(error);
//        }
//    }

//    public static class NotificaWarning extends AccionSemantica {
//        private final String mensaje;
//        private final AnalizadorLexico aLexico;
//
//        public NotificaWarning(String mensaje, AnalizadorLexico aLexico) {
//            this.mensaje = mensaje;
//            this.aLexico = aLexico;
//        }
//
//        /**
//         * Accion semantica auxiliar para notificar warnings.
//         */
//        @Override
//        public void ejecutar() {
//            String warning = "Linea "+aLexico.getLineaActual()+": " + mensaje;
//            TablaNotificaciones.agregarWarning(warning);
//        }
//    }

}
