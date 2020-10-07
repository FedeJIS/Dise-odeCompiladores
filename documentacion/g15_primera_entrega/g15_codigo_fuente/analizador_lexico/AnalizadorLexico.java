package analizador_lexico;

import analizador_lexico.maquina_estados.MaquinaEstados;
import analizador_sintactico.Parser;
import util.CodigoFuente;
import util.TablaPalabrasR;
import util.tabla_simbolos.TablaSimbolos;

import java.util.HashMap;
import java.util.Map;

public class AnalizadorLexico {
    private final CodigoFuente cFuente;
    private final MaquinaEstados maquinaEstados;

    /**
     * Valores para tokens.
     */
    public static final int T_EOF = 0;
    public static final int T_ID = Parser.ID;
    public static final int T_COMP_MENOR_IGUAL = Parser.COMP_MENOR_IGUAL;
    public static final int T_COMP_MAYOR_IGUAL = Parser.COMP_MAYOR_IGUAL;
    public static final int T_COMP_DISTINTO = Parser.COMP_DISTINTO;
    public static final int T_COMP_IGUAL = Parser.COMP_IGUAL;
    public static final int T_UINT = Parser.UINT;
    public static final int T_DOUBLE = Parser.DOUBLE;
    public static final int T_CADENA = Parser.CADENA;
    public static final int T_IF = Parser.IF;
    public static final int T_THEN = Parser.THEN;
    public static final int T_ELSE = Parser.ELSE;
    public static final int T_END_IF = Parser.END_IF;
    public static final int T_LOOP = Parser.LOOP;
    public static final int T_UNTIL = Parser.UNTIL;
    public static final int T_OUT = Parser.OUT;
    public static final int T_PROC = Parser.PROC;
    public static final int T_VAR = Parser.VAR;
    public static final int T_NI = Parser.NI;
    public static final int T_CTE_UINT = Parser.CTE_UINT;
    public static final int T_CTE_DOUBLE = Parser.CTE_DOUBLE;

    public int ultimoTokenGenerado = -1;
    public String ultimoLexemaGenerado;
    private final Map<Integer, String> nombreTokens = new HashMap<>();

    public AnalizadorLexico(CodigoFuente cFuente, TablaSimbolos tablaS){
        this.cFuente = cFuente;
        this.maquinaEstados = new MaquinaEstados(this, cFuente,tablaS, inicTPR());
        inicMapaToken();
    }

    private void inicMapaToken() {
        nombreTokens.put(T_EOF,"EOF");
        nombreTokens.put(T_ID,"ID");
        nombreTokens.put((int)'<',"<");
        nombreTokens.put(T_COMP_MENOR_IGUAL,"<=");
        nombreTokens.put((int)'>',">");
        nombreTokens.put(T_COMP_MAYOR_IGUAL,">=");
        nombreTokens.put(T_COMP_DISTINTO,"!=");
        nombreTokens.put((int)'=',"=");
        nombreTokens.put(T_COMP_IGUAL,"==");
        nombreTokens.put(T_UINT,"UINT");
        nombreTokens.put(T_DOUBLE,"DOUBLE");
        nombreTokens.put(T_CADENA,"CADENA");
        nombreTokens.put(T_IF,"IF");
        nombreTokens.put(T_THEN,"THEN");
        nombreTokens.put(T_ELSE,"ELSE");
        nombreTokens.put(T_END_IF,"END_IF");
        nombreTokens.put(T_LOOP,"LOOP");
        nombreTokens.put(T_UNTIL,"UNTIL");
        nombreTokens.put(T_OUT,"OUT");
        nombreTokens.put(T_PROC,"PROC");
        nombreTokens.put(T_VAR,"VAR");
        nombreTokens.put(T_NI,"NI");
        nombreTokens.put(T_CTE_UINT,"CTE_UINT");
        nombreTokens.put(T_CTE_DOUBLE,"CTE_DOUBLE");
        nombreTokens.put((int)'+',"+");
        nombreTokens.put((int)'-',"-");
        nombreTokens.put((int)'*',"*");
        nombreTokens.put((int)'/',"/");
        nombreTokens.put((int)'{',"{");
        nombreTokens.put((int)'}',"}");
        nombreTokens.put((int)'(',"(");
        nombreTokens.put((int)')',")");
        nombreTokens.put((int)',',",");
        nombreTokens.put((int)';',";");
        nombreTokens.put((int)'"',"\"");
    }

    public void setVariablesSintactico(int token, String lexema){
        this.ultimoTokenGenerado = token;
        this.ultimoLexemaGenerado = lexema;
    }

    public int getLineaActual(){
        return maquinaEstados.getLineaActual();
    }

    private TablaPalabrasR inicTPR(){
        TablaPalabrasR tPR = new TablaPalabrasR();
        tPR.agregar("UINT", T_UINT);
        tPR.agregar("DOUBLE", T_DOUBLE);
        tPR.agregar("IF", T_IF);
        tPR.agregar("THEN", T_THEN);
        tPR.agregar("ELSE", T_ELSE);
        tPR.agregar("END_IF", T_END_IF);
        tPR.agregar("LOOP", T_LOOP);
        tPR.agregar("UNTIL", T_UNTIL);
        tPR.agregar("OUT", T_OUT);
        tPR.agregar("PROC", T_PROC);
        tPR.agregar("VAR", T_VAR);
        tPR.agregar("NI", T_NI);

        return tPR;
    }

    public int produceToken(){
        while (!maquinaEstados.estadoFinalAlcanzado()){
            if (cFuente.eof()) {
                maquinaEstados.transicionarEOF();
                System.out.println();
            }
            else {
                if (cFuente.simboloActual() == '\n') System.out.println();
                if (cFuente.simboloActual() == '\t' || cFuente.simboloActual() == ' ') System.out.print(" ");
                maquinaEstados.transicionar(cFuente.simboloActual());
                cFuente.avanzar();
            }
        }
        maquinaEstados.reiniciar();
        System.out.print(nombreTokens.get(ultimoTokenGenerado)); //Imprime el token que se leyo.
        return ultimoTokenGenerado;
    }
}
