package analizador_lexico.acciones_semanticas;

import analizador_lexico.maquina_estados.MaquinaEstados;
import util.tabla_simbolos.TablaSimbolos;

/**
 * Si la TS contiene una entrada con el lexema actual, incrementa su numero de referencias en uno. Si no la
 * contiene, la crea y agrega.
 */
public class GeneraTokenTS extends AccionSemantica {
    private final MaquinaEstados maquinaEstados;

    private final TablaSimbolos tablaS;

    private final int token;

    public GeneraTokenTS(MaquinaEstados maquinaEstados, TablaSimbolos tablaS, int token) {
        this.maquinaEstados = maquinaEstados;
        this.tablaS = tablaS;
        this.token = token;
    }

    @Override
    public void ejecutar() {
        String lexema = getString();
        tablaS.agregarEntrada(token, lexema, "");
        maquinaEstados.setVariablesSintactico(token, lexema);
    }
}
