package analizador_sintactico.util;

import analizador_lexico.AnalizadorLexico;
import analizador_sintactico.Parser;
import generacion_c_intermedio.MultiPolaca;
import generacion_c_intermedio.PilaAmbitos;
import generacion_c_intermedio.Polaca;
import util.TablaNotificaciones;
import util.tabla_simbolos.Celda;
import util.tabla_simbolos.TablaSimbolos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ParserHelper {
    private final AnalizadorLexico aLexico;
    private final TablaSimbolos tablaS;
    private final PilaAmbitos pilaAmbitos;
    private final Polaca polacaProgram;
    private final MultiPolaca polacaProcedimientos;

    public ParserHelper(AnalizadorLexico aLexico, TablaSimbolos tablaS, PilaAmbitos pilaAmbitos, Polaca polacaProgram,
                        MultiPolaca polacaProcedimientos) {
        this.aLexico = aLexico;
        this.tablaS = tablaS;
        this.pilaAmbitos = pilaAmbitos;
        this.polacaProgram = polacaProgram;
        this.polacaProcedimientos = polacaProcedimientos;
    }

    //---USO GENERAL---

    public void eliminarUltimoAmbito(){
        if (!pilaAmbitos.inAmbitoGlobal()) pilaAmbitos.eliminarUltimo();
    }

    private String getAmbitoId(String lexema) {
        StringBuilder builderAmbito = new StringBuilder(pilaAmbitos.getAmbitoActual());

        while (!builderAmbito.toString().isEmpty()) {
            //Busca el id en el ambito actual.
            if (tablaS.contieneLexema(PilaAmbitos.aplicaNameManglin(builderAmbito.toString(), lexema)))
                return builderAmbito.toString();

            //"Baja" un nivel en la pila de ambitos.
            if (!builderAmbito.toString().equals("PROGRAM")) //Chequea no estar en el ambito global.
                builderAmbito.delete(builderAmbito.lastIndexOf("@"), builderAmbito.length());
            else builderAmbito.delete(0, builderAmbito.length());
        }
        return ""; //La variable no esta declarada.
    }

    private boolean isIdDecl(String lexema, String ambito) {
        if (ambito.isEmpty()) return false; //La TS no contiene el lexema en el ambito recibido.
        return tablaS.isEntradaDeclarada(PilaAmbitos.aplicaNameManglin(ambito, lexema));
    }

    private boolean isIdNoRedecl(String lexema, String ambito) {
        if (isIdDecl(lexema, ambito)) {
            String msgError = "El identificador '" + lexema + "' ya se encuentra declarado.";
            TablaNotificaciones.agregarError(aLexico.getLineaActual(), msgError);
            return false;
        }
        return true;
    }

    //---DECLARACION VARIABLES---

    private String ultimoTipoLeido; //Almacena temporalmente el ultimo tipo leido.

    public void setUltimoTipoLeido(String tipo){
        ultimoTipoLeido = tipo;
    }

    public void declaracionVar(String lexema){
        String ambito = getAmbitoId(lexema);

        if (isIdNoRedecl(lexema, ambito)){
            tablaS.quitarReferencia(lexema);

            String nLexema = PilaAmbitos.aplicaNameManglin(pilaAmbitos.getAmbitoActual(), lexema);
            tablaS.agregarEntrada(new Celda(Parser.ID, nLexema, ultimoTipoLeido, Celda.USO_VAR, true));
        }
    }

    //---DECLARACION PROCS---

    /**
     * Guarda informacion de los procedimientos. Facilita el anidamiento de los mismos.
     */
    private final List<InfoProc> pilaInfoProc = new ArrayList<>();

    /**
     * Invocado cuando se lee el identificador de un procedimiento.
     */
    public void lecturaIdProc(String lexema) {
        pilaInfoProc.add(new InfoProc(lexema));

        if (isIdNoRedecl(lexema, pilaAmbitos.getAmbitoActual())) { //Solo actuo si el id no se declaro para otra cosa.
            tablaS.quitarReferencia(lexema);

            String nLexema = PilaAmbitos.aplicaNameManglin(pilaAmbitos.getAmbitoActual(), lexema);
            tablaS.agregarEntrada(new Celda(Parser.ID, nLexema, "-", Celda.USO_PROC, true));

            pilaAmbitos.agregarAmbito(lexema);
        } else pilaInfoProc.get(pilaInfoProc.size() - 1).setInfoValida(false); //Marco proc como invalido.
    }

    /**
     * Invocado cuando se lee un parametro formal de un procedimiento.
     */
    public void lecturaParamFormal(String lexema, String tipoPasaje) {
        InfoProc infoProc = pilaInfoProc.get(pilaInfoProc.size() - 1); //El param formal es del ultimo proc leido.
        String ambito = getAmbitoId(lexema);

        if (isIdNoRedecl(lexema, ambito)) { //Solo actuo si el id no se declaro para otra cosa.
            tablaS.quitarReferencia(lexema);

            String nLexema = PilaAmbitos.aplicaNameManglin(pilaAmbitos.getAmbitoActual(), lexema);
            infoProc.addParam(nLexema, tipoPasaje);
            tablaS.agregarEntrada(new Celda(Parser.ID, nLexema, ultimoTipoLeido, tipoPasaje, true));
        } else {
            infoProc.setInfoValida(false); //Marco proc como invalido.
            tablaS.quitarReferencia(lexema);
        }

    }

    /**
     * Invocado cuando se lee el NI de un procedimiento.
     */
    public void lecturaNumInvoc(int numInvoc, boolean errorGram, String msgErrorGram) {
        InfoProc infoProc = pilaInfoProc.get(pilaInfoProc.size() - 1);
        if (errorGram) {
            infoProc.setInfoValida(false);
            TablaNotificaciones.agregarError(aLexico.getLineaActual(), msgErrorGram);
        }

        if (numInvoc < 1 || numInvoc > 4) {
            String msgError = "El numero de invocaciones de un procedimiento debe estar en el rango [1,4].";
            TablaNotificaciones.agregarError(aLexico.getLineaActual(), msgError);
            infoProc.setInfoValida(false);
        } else infoProc.setNumInvoc(numInvoc);

        if (!errorGram) //Ya tengo todos los datos que necesito para declarar el proc.
            declaracionProc();
    }

    /**
     * Invocado cuando se termina de leer el cuerpo de un procedimiento.
     */
    public void declaracionProc() {
        //Es un remove pq ya no se necesita almacenar mas la info.
        InfoProc infoProc = pilaInfoProc.remove(pilaInfoProc.size() - 1);

        String ambito = getAmbitoId(infoProc.getLexema());

        if (infoProc.isInfoValida()) {
            tablaS.setMaxInvoc(PilaAmbitos.aplicaNameManglin(ambito, infoProc.getLexema()), infoProc.getNumInvoc());
            tablaS.setParamsProc(PilaAmbitos.aplicaNameManglin(ambito, infoProc.getLexema()), infoProc.getParams());
        }
    }

    //---INVOCACION PROCS---

    /**
     * Almacena hasta tres parametros de una invocacion a un procedimiento.
     */
    private final List<String> listaParamsInvoc = new ArrayList<>();

    /**
     * Guarda los lexemas de los parametros recibidos en una invocacion a un procedimiento.
     */
    public void guardaParamsInvoc(String... lexemaParams) {
        if (listaParamsInvoc.size() < 3) for (String paramReal : lexemaParams)
            if (tablaS.esEntradaCte(paramReal)) listaParamsInvoc.add(paramReal);
            else listaParamsInvoc.add(PilaAmbitos.aplicaNameManglin(pilaAmbitos.getAmbitoActual(), paramReal));
    }

    /**
     * Chequea que el identificador este declarado y se corresponda con el de un procedimiento.
     */
    private boolean isIdInvocValido(String lexema, String ambito) {
        if (!isIdDecl(lexema, ambito)) { //Proc no declarado.
            TablaNotificaciones.agregarError(aLexico.getLineaActual(),
                    "El procedimiento '" + lexema + "' no esta declarado.");
            return false;
        }
        if (!tablaS.isEntradaProc(PilaAmbitos.aplicaNameManglin(ambito, lexema))) { //Id no es de un proc.
            TablaNotificaciones.agregarError(aLexico.getLineaActual(),
                    "El identificador '" + lexema + "' no se corresponde con ningun procedimiento.");
            return false;
        }
        return true;
    }

    private boolean comparaParams(int nParam, String paramReal, String paramFormal){
        boolean validos = true;
        //Chequea que el param formal no tenga pasaje CVR y el param real sea una cte.
        if (tablaS.isEntradaParamCVR(paramFormal) && tablaS.esEntradaCte(paramReal)){
            validos = false;
            TablaNotificaciones.agregarError(aLexico.getLineaActual(),
                    String.format("En la posicion %d se esperaba una variable, pero se encontro una constante.", nParam));
        }

        //Compara tipos de parametros.
        if (!tablaS.getTipoEntrada(paramReal).equals(tablaS.getTipoEntrada(paramFormal))){
            validos = false;
            TablaNotificaciones.agregarError(aLexico.getLineaActual(),
                    String.format("En la posicion %d se esperaba un %s, pero se encontro un %s.",
                            nParam, tablaS.getTipoEntrada(paramReal), tablaS.getTipoEntrada(paramFormal)));
        }

        return validos;
    }

    /**
     * Chequea que los tipos de los params formales y reales coincidan.
     */
    private boolean areParamsRealesValidos(String lexemaProc, List<String> paramsReales) {
        boolean validos = true;
        //Check numero params reales coincide con numero params formales.
        if (paramsReales.size() != tablaS.getNParams(lexemaProc)) {
            TablaNotificaciones.agregarError(aLexico.getLineaActual(),
                    "Se esperaban " + tablaS.getNParams(lexemaProc) + " parametros, "
                            + "pero se encontraron " + listaParamsInvoc.size() + ".");
            validos = false;
        }

        //Check tipos parametros. Compara tantos parametros como sea posible.
        for (int i = 0; i < tablaS.getNParams(lexemaProc) && i < paramsReales.size(); i++) {
            validos = comparaParams(i+1, listaParamsInvoc.get(i), tablaS.getParam(lexemaProc, i));
        }

        return validos;
    }

    public void invocacionProc(String lexema) {
        String ambito = getAmbitoId(lexema);

        boolean invocValida = isIdInvocValido(lexema, ambito);

        String nLexema = PilaAmbitos.aplicaNameManglin(ambito, lexema);

        //Hago el check de NI y params solo si es un id valido para la invocacion (esta declarado y es un proc).
        if (invocValida) {
            if (tablaS.maxInvocAlcanzadas(nLexema)) { //Check NI no superado.
                invocValida = false;
                TablaNotificaciones.agregarError(aLexico.getLineaActual(),
                        "El procedimiento '" + lexema + "' ya alcanzo su numero maximo de invocaciones.");
            }
            invocValida = invocValida && areParamsRealesValidos(nLexema, listaParamsInvoc);
        }

        //Si ningun control fallo, genero las instrucciones para la invocacion.
        if (invocValida) {
            agregarPasosRepr(nLexema, Polaca.PASO_INVOC);
            tablaS.incrementaNInvoc(nLexema);

            //Pasa el valor de los param formales a los reales (En caso de param CVR).
            for (int i = 0; i < tablaS.getNParams(nLexema); i++) {
                String paramDecl = tablaS.getParam(nLexema, i);
                if (tablaS.isEntradaParamCVR(paramDecl)) {
                    String paramInvoc = listaParamsInvoc.get(i);
                    agregarPasosRepr(paramDecl, paramInvoc, "="); //paramInvoc = paramDecl.
                }
            }
        }
        tablaS.quitarReferencia(lexema);
        listaParamsInvoc.clear();
    }


    //---POLACA---

    private void quitarUltimoPasoRepr() {
        if (pilaAmbitos.inAmbitoGlobal())
            polacaProgram.quitarUltimoPaso();
        else polacaProcedimientos.quitarUltimoPaso(pilaAmbitos.getAmbitoActual());
    }

    private void agregarPasosRepr(String... pasos) {
        if (pilaAmbitos.inAmbitoGlobal())
            polacaProgram.agregarPasos(pasos);
        else polacaProcedimientos.agregarPasos(pilaAmbitos.getAmbitoActual(), pasos);
    }

    private void puntoControlThen() {
        if (pilaAmbitos.inAmbitoGlobal())
            polacaProgram.puntoControlThen();
        else polacaProcedimientos.ejecutarPuntoControl(pilaAmbitos.getAmbitoActual(), Polaca.PC_THEN);
    }

    private void puntoControlElse() {
        if (pilaAmbitos.inAmbitoGlobal())
            polacaProgram.puntoControlElse();
        else polacaProcedimientos.ejecutarPuntoControl(pilaAmbitos.getAmbitoActual(), Polaca.PC_ELSE);
    }

    private void puntoControlFinCondicional() {
        if (pilaAmbitos.inAmbitoGlobal())
            polacaProgram.puntoControlFinCondicional();
        else polacaProcedimientos.ejecutarPuntoControl(pilaAmbitos.getAmbitoActual(), Polaca.PC_FIN_COND);
    }

    private void puntoControlLoop() {
        if (pilaAmbitos.inAmbitoGlobal())
            polacaProgram.puntoControlLoop();
        else polacaProcedimientos.ejecutarPuntoControl(pilaAmbitos.getAmbitoActual(), Polaca.PC_LOOP);
    }

    private void puntoControlUntil() {
        if (pilaAmbitos.inAmbitoGlobal())
            polacaProgram.puntoControlUntil();
        else polacaProcedimientos.ejecutarPuntoControl(pilaAmbitos.getAmbitoActual(), Polaca.PC_UNTIL);
    }

    public Polaca getPolacaProgram() {
        return polacaProgram;
    }

    public MultiPolaca getPolacaProcs() {
        return polacaProcedimientos;
    }
}