//### This file created by BYACC 1.8(/Java extension  1.15)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";






//#line 2 "archivos/gramatica.y"
package analizador_sintactico;

import analizador_lexico.AnalizadorLexico;
import util.TablaNotificaciones;
import util.tabla_simbolos.Celda;
import util.tabla_simbolos.TablaSimbolos;
//#line 24 "Parser.java"




@SuppressWarnings("ALL")
public class Parser
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//public class ParserVal is defined in ParserVal.java


String   yytext;//user variable to return contextual strings
ParserVal yyval; //used to return semantic vals from action routines
ParserVal yylval;//the 'lval' (result) I got from yylex()
ParserVal valstk[];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
void val_init()
{
  valstk=new ParserVal[YYSTACKSIZE];
  yyval=new ParserVal();
  yylval=new ParserVal();
  valptr=-1;
}
void val_push(ParserVal val)
{
  if (valptr>=YYSTACKSIZE)
    return;
  valstk[++valptr]=val;
}
ParserVal val_pop()
{
  if (valptr<0)
    return new ParserVal();
  return valstk[valptr--];
}
void val_drop(int cnt)
{
int ptr;
  ptr=valptr-cnt;
  if (ptr<0)
    return;
  valptr = ptr;
}
ParserVal val_peek(int relative)
{
int ptr;
  ptr=valptr-relative;
  if (ptr<0)
    return new ParserVal();
  return valstk[ptr];
}
final ParserVal dup_yyval(ParserVal val)
{
  ParserVal dup = new ParserVal();
  dup.ival = val.ival;
  dup.dval = val.dval;
  dup.sval = val.sval;
  dup.obj = val.obj;
  return dup;
}
//#### end semantic value section ####
public final static short ID=257;
public final static short COMP_MENOR_IGUAL=258;
public final static short COMP_MAYOR_IGUAL=259;
public final static short COMP_DISTINTO=260;
public final static short COMP_IGUAL=261;
public final static short UINT=262;
public final static short DOUBLE=263;
public final static short CADENA=264;
public final static short IF=265;
public final static short THEN=266;
public final static short ELSE=267;
public final static short END_IF=268;
public final static short LOOP=269;
public final static short UNTIL=270;
public final static short OUT=271;
public final static short PROC=272;
public final static short VAR=273;
public final static short NI=274;
public final static short CTE_UINT=275;
public final static short CTE_DOUBLE=276;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    1,    1,    1,    2,    2,    2,    3,    3,    5,
    5,    6,    6,    7,    7,    8,   11,   11,   11,   11,
   11,   12,   12,   13,   13,   13,   14,   14,   14,    9,
    9,   10,   10,   10,    4,    4,    4,    4,    4,   20,
   20,   15,   21,   21,   21,   21,   16,   16,   22,   22,
   22,   23,   23,   23,   24,   24,   24,   24,   17,   25,
   25,   26,   18,   18,   18,   18,   29,   29,   30,   30,
   30,   31,   31,   27,   27,   28,   28,   32,   32,   32,
   32,   32,   32,   19,   33,   33,   33,   33,
};
final static short yylen[] = {                            2,
    1,    1,    2,    3,    1,    1,    1,    4,    2,    2,
    1,    3,    2,    3,    1,    3,    0,    1,    3,    5,
    9,    1,    1,    3,    2,    2,    2,    1,    1,    1,
    1,    1,    3,    1,    1,    1,    1,    1,    1,    2,
    3,    2,    2,    3,    1,    2,    3,    2,    3,    3,
    1,    3,    3,    1,    1,    1,    1,    2,    2,    2,
    1,    2,    4,    3,    3,    2,    2,    1,    2,    1,
    1,    2,    1,    2,    3,    2,    5,    1,    1,    1,
    1,    1,    1,    4,    1,    1,    1,    1,
};
final static short yydefred[] = {                         0,
    7,    0,   30,   31,    0,    0,    0,    0,    0,    0,
    1,    0,    5,    6,    0,    0,   35,   36,   37,   38,
   39,    0,   68,    0,    0,    0,   42,   67,    0,    0,
   60,    0,   10,   55,   56,   57,   76,    0,    0,    0,
   54,    0,    0,    0,   34,    0,    9,    0,   59,    0,
   71,    0,   43,    0,    0,    0,    0,   74,   88,   85,
   86,   87,    0,   58,   79,   78,   83,   82,    0,    0,
   80,   81,    0,    0,    0,    4,   28,    0,    0,    0,
    0,   22,   23,   15,    0,    0,    0,   62,   69,    0,
   64,    0,   44,    0,   75,   84,    0,    0,    0,   52,
   53,   25,    0,   27,   12,    0,    0,    0,    8,   33,
   72,   63,   41,   77,   24,    0,   14,    0,    0,   16,
    0,    0,    0,    0,   21,
};
final static short yydgoto[] = {                         10,
   11,   12,   13,   30,   15,   44,   86,  109,   79,   47,
   80,   81,   82,   83,   17,   18,   19,   20,   21,   57,
   27,   39,   40,   41,   22,   49,   31,   23,   24,   52,
   92,   73,   63,
};
final static short yysindex[] = {                       -15,
    0,  -14,    0,    0,  -13,   34,   -3, -223,  -41,    0,
    0,  -16,    0,    0,   52, -207,    0,    0,    0,    0,
    0, -173,    0,   29,  -27,  -24,    0,    0,   39,   41,
    0, -224,    0,    0,    0,    0,    0,  -24,   53,   -6,
    0,  -15, -154, -221,    0,   58,    0,  -13,    0,   34,
    0, -157,    0,   71,   48,   57,   -7,    0,    0,    0,
    0,    0,   81,    0,    0,    0,    0,    0,  -24,  -24,
    0,    0,  -24,  -24,  -24,    0,    0, -158, -133,   90,
   89,    0,    0,    0,   73,   12, -207,    0,    0,   34,
    0, -132,    0,   39,    0,    0,   -6,   -6,   80,    0,
    0,    0, -120,    0,    0, -154, -136,  -15,    0,    0,
    0,    0,    0,    0,    0,  101,    0,   24, -154,    0,
  107, -154,  110, -154,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0, -112,    0,  119,    0,    0,
    0,   15,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   17,   19,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    3,
    0,   16,  -34,    0,    0,   18,    0,    0,    0,    1,
    0,   22,    0,   28,   30,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  -36, -217,
  -31,    0,    0,    0,    0,    0,    0,    0,    0,    2,
    0,   31,    0,   35,    0,    0,   13,   23,    0,    0,
    0,    0,  -35,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  -30,    0,    0,    0,    0,
  -29,    0,    0,  -34,    0,
};
final static short yygindex[] = {                         0,
  -22,    0,    0,   38,    0,    0,    0,    0,   42,    7,
   37,   -5,    0,    0,    0,    0,    0,    0,    0,   68,
    0,   -2,   60,   32,    0,    0,    5,   40,    0,    0,
    0,    0,    0,
};
final static int YYTABLESIZE=314;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         37,
   70,   73,   51,   38,   29,   26,   17,   29,   26,   18,
   19,   20,   49,   53,    2,    3,   45,   32,   48,   76,
   38,   66,   50,   55,    9,   25,    9,   46,   51,   47,
   65,   54,   59,   33,   84,   74,   32,   14,   13,   60,
   75,   16,   42,   51,   28,   51,   26,   51,   45,   46,
   61,   62,   85,   49,   89,   49,   13,   49,   32,   70,
   73,   51,   51,   50,   51,   50,   56,   50,    9,   64,
   99,   49,   49,    9,   49,   45,   32,   48,    9,   14,
   66,   50,   50,   16,   50,  118,   46,   88,   47,   65,
   69,   43,   70,  110,  111,   69,   48,   70,  102,   58,
  116,   87,   77,    3,    4,  100,  101,    3,    4,   90,
   91,   93,   71,  121,   72,   94,  123,   95,   78,  103,
  114,   96,   69,  104,   70,   70,   73,   51,   97,   98,
  105,   56,  106,  107,  108,  112,  115,   49,  117,    2,
    3,   45,   32,   48,  119,   14,   66,   50,  120,   16,
  122,   29,   46,  124,   47,   65,   29,   61,   11,   40,
  125,  113,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   34,    0,    0,    0,   29,
   26,   17,    0,    0,   18,   19,   20,    0,   45,   46,
    0,    0,   34,   35,   36,    0,    0,   29,   26,   17,
    1,    2,   18,   19,   20,    0,    3,    4,    0,    5,
   35,   36,    0,    6,    0,    7,    8,    0,    0,    0,
   51,   51,   51,   51,    0,    0,    0,   70,   70,   73,
   49,   49,   49,   49,    0,    0,    0,    0,    0,    0,
   50,   50,   50,   50,    0,    2,    0,    0,    0,    0,
    2,    0,    0,    5,   50,    2,    0,    6,    5,    7,
    0,    0,    6,    5,    7,    0,    0,    6,    0,    7,
   65,   66,   67,   68,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
    0,    0,    0,   45,   41,   41,   41,   44,   44,   41,
   41,   41,    0,   41,    0,    0,    0,    0,    0,   42,
   45,    0,    0,   26,   40,   40,   40,    0,   24,    0,
    0,   25,  257,  257,  256,   42,   40,    0,  256,  264,
   47,    0,   59,   41,    5,   43,   61,   45,  256,  257,
  275,  276,  274,   41,   50,   43,  274,   45,   41,   59,
   59,   59,   60,   41,   62,   43,   29,   45,   40,   38,
   73,   59,   60,   40,   62,   59,   59,   59,   40,   42,
   59,   59,   60,   42,   62,  108,   59,   48,   59,   59,
   43,   40,   45,   87,   90,   43,  270,   45,  257,   59,
  106,   44,  257,  262,  263,   74,   75,  262,  263,  267,
  268,   41,   60,  119,   62,   59,  122,  125,  273,   78,
   41,   41,   43,  257,   45,  125,  125,  125,   69,   70,
   41,   94,   44,   61,  123,  268,  257,  125,  275,  125,
  125,  125,  125,  125,   44,  108,  125,  125,  125,  108,
   44,  123,  125,   44,  125,  125,  123,  270,   40,  125,
  124,   94,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  257,   -1,   -1,   -1,  256,
  256,  256,   -1,   -1,  256,  256,  256,   -1,  256,  257,
   -1,   -1,  257,  275,  276,   -1,   -1,  274,  274,  274,
  256,  257,  274,  274,  274,   -1,  262,  263,   -1,  265,
  275,  276,   -1,  269,   -1,  271,  272,   -1,   -1,   -1,
  258,  259,  260,  261,   -1,   -1,   -1,  267,  268,  268,
  258,  259,  260,  261,   -1,   -1,   -1,   -1,   -1,   -1,
  258,  259,  260,  261,   -1,  257,   -1,   -1,   -1,   -1,
  257,   -1,   -1,  265,  266,  257,   -1,  269,  265,  271,
   -1,   -1,  269,  265,  271,   -1,   -1,  269,   -1,  271,
  258,  259,  260,  261,
};
}
final static short YYFINAL=10;
final static short YYMAXTOKEN=276;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,"'('","')'","'*'","'+'","','",
"'-'",null,"'/'",null,null,null,null,null,null,null,null,null,null,null,"';'",
"'<'","'='","'>'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
"'{'",null,"'}'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,"ID","COMP_MENOR_IGUAL","COMP_MAYOR_IGUAL",
"COMP_DISTINTO","COMP_IGUAL","UINT","DOUBLE","CADENA","IF","THEN","ELSE",
"END_IF","LOOP","UNTIL","OUT","PROC","VAR","NI","CTE_UINT","CTE_DOUBLE",
};
final static String yyrule[] = {
"$accept : programa",
"programa : bloque_sentencias",
"bloque_sentencias : sentencia",
"bloque_sentencias : sentencia ';'",
"bloque_sentencias : sentencia ';' bloque_sentencias",
"sentencia : sentencia_declarativa",
"sentencia : sentencia_ejecutable",
"sentencia : error",
"sentencia_declarativa : nombre_proc params_proc ni_proc cuerpo_proc",
"sentencia_declarativa : tipo lista_variables",
"nombre_proc : PROC ID",
"nombre_proc : PROC",
"params_proc : '(' lista_params ')'",
"params_proc : '(' lista_params",
"ni_proc : NI '=' CTE_UINT",
"ni_proc : error",
"cuerpo_proc : '{' bloque_sentencias '}'",
"lista_params :",
"lista_params : param",
"lista_params : param ',' param",
"lista_params : param ',' param ',' param",
"lista_params : param ',' param ',' param ',' param ',' lista_params",
"param : param_var",
"param : param_comun",
"param_var : VAR tipo ID",
"param_var : VAR ID",
"param_var : VAR tipo",
"param_comun : tipo ID",
"param_comun : ID",
"param_comun : tipo",
"tipo : UINT",
"tipo : DOUBLE",
"lista_variables : ID",
"lista_variables : ID ',' lista_variables",
"lista_variables : error",
"sentencia_ejecutable : invocacion",
"sentencia_ejecutable : asignacion",
"sentencia_ejecutable : sentencia_loop",
"sentencia_ejecutable : sentencia_if",
"sentencia_ejecutable : print",
"bloque_sentencias_ejec : sentencia_ejecutable ';'",
"bloque_sentencias_ejec : sentencia_ejecutable ';' bloque_sentencias_ejec",
"invocacion : ID params_invocacion",
"params_invocacion : '(' ')'",
"params_invocacion : '(' lista_variables ')'",
"params_invocacion : '('",
"params_invocacion : '(' lista_variables",
"asignacion : ID '=' expresion",
"asignacion : ID '='",
"expresion : expresion '+' termino",
"expresion : expresion '-' termino",
"expresion : termino",
"termino : termino '*' factor",
"termino : termino '/' factor",
"termino : factor",
"factor : ID",
"factor : CTE_UINT",
"factor : CTE_DOUBLE",
"factor : '-' factor",
"sentencia_loop : cuerpo_loop cuerpo_until",
"cuerpo_loop : LOOP bloque_estruct_ctrl",
"cuerpo_loop : LOOP",
"cuerpo_until : UNTIL condicion",
"sentencia_if : encabezado_if rama_then rama_else END_IF",
"sentencia_if : encabezado_if rama_then END_IF",
"sentencia_if : encabezado_if rama_then rama_else",
"sentencia_if : encabezado_if rama_then",
"encabezado_if : IF condicion",
"encabezado_if : condicion",
"rama_then : THEN bloque_estruct_ctrl",
"rama_then : THEN",
"rama_then : bloque_estruct_ctrl",
"rama_else : ELSE bloque_estruct_ctrl",
"rama_else : ELSE",
"bloque_estruct_ctrl : sentencia_ejecutable ';'",
"bloque_estruct_ctrl : '{' bloque_sentencias_ejec '}'",
"condicion : '(' ')'",
"condicion : '(' expresion comparador expresion ')'",
"comparador : COMP_MAYOR_IGUAL",
"comparador : COMP_MENOR_IGUAL",
"comparador : '<'",
"comparador : '>'",
"comparador : COMP_IGUAL",
"comparador : COMP_DISTINTO",
"print : OUT '(' imprimible ')'",
"imprimible : CADENA",
"imprimible : CTE_UINT",
"imprimible : CTE_DOUBLE",
"imprimible : ID",
};

//#line 173 "archivos/gramatica.y"

    private AnalizadorLexico aLexico;
    private TablaSimbolos tablaS;

    /**
     * Create a parser, setting the debug to true or false.
     *
     * @param debugMe true for debugging, false for no debug.
     */
    public Parser(boolean debugMe, AnalizadorLexico aLexico, TablaSimbolos tablaS) {
        yydebug = debugMe;
        this.aLexico = aLexico;
        this.tablaS = tablaS;
    }

    private int yylex() {
        int token = aLexico.produceToken();
        yylval = new ParserVal(aLexico.ultimoLexemaGenerado);
        return token;
    }

    private void yyout(String mensaje) {
        System.out.println(mensaje);
    }

    private void yyerror(String mensajeError) {
        TablaNotificaciones.agregarError("Linea " + aLexico.getLineaActual() + ": " + mensajeError);
    }

    private void checkCambioSigno() {
        String lexemaSignoNoC = yylval.sval; //Obtengo el lexema del factor.
        Celda celdaOriginal = tablaS.getValor(lexemaSignoNoC); //La sentencia va aca si o si, porque mas adelante ya no existe la entrada en la TS.

        if (celdaOriginal.getTipo().equals("DOUBLE")) {
            tablaS.quitarReferencia(lexemaSignoNoC); //El lexema esta en la TS si o si. refs--.
            if (tablaS.entradaSinReferencias(lexemaSignoNoC)) tablaS.eliminarEntrada(lexemaSignoNoC);

            String lexemaSignoC = String.valueOf(Double.parseDouble(lexemaSignoNoC) * -1); //Cambio el signo del factor.
            tablaS.agregarEntrada(celdaOriginal.getToken(), lexemaSignoC, celdaOriginal.getTipo());
        } else
            TablaNotificaciones.agregarError("Linea " + aLexico.getLineaActual() + ": No se permiten UINT negativos");

    }
	
	
	
	
	
	
//#line 437 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  val_push(yylval);     //save empty value
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        if (yychar < 0)    //it it didn't work/error
          {
          yychar = 0;      //change it to default string (no -1!)
          if (yydebug)
            yylexdebug(yystate,yychar);
          }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        if (yydebug)
          debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0)   //check for under & overflow here
            {
            yyerror("stack underflow. aborting...");  //note lower case 's'
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            if (yydebug)
              debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            if (yydebug)
              debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0)   //check for under & overflow here
              {
              yyerror("Stack underflow. aborting...");  //capital 'S'
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        if (yydebug)
          {
          yys = null;
          if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          if (yys == null) yys = "illegal-symbol";
          debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          }
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    if (yydebug)
      debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    yyval = dup_yyval(yyval); //duplicate yyval if ParserVal is used as semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 2:
//#line 19 "archivos/gramatica.y"
{yyerror("Falta ';' al final de la sentencia");}
break;
case 7:
//#line 26 "archivos/gramatica.y"
{yyerror("Sentencia mal definida");}
break;
case 11:
//#line 34 "archivos/gramatica.y"
{yyerror("Procedimiento sin nombre");}
break;
case 13:
//#line 38 "archivos/gramatica.y"
{yyerror("Falta parentesis de cierre de parametros");}
break;
case 15:
//#line 42 "archivos/gramatica.y"
{yyerror("Formato incorrecto de NI. El formato correcto es: 'NI = CTE_UINT'");}
break;
case 21:
//#line 52 "archivos/gramatica.y"
{yyerror("El procedimiento no puede tener mas de 3 parametros");}
break;
case 25:
//#line 60 "archivos/gramatica.y"
{yyerror("Falta el tipo de un parametro");}
break;
case 26:
//#line 61 "archivos/gramatica.y"
{yyerror("Falta el nombre de un parametro");}
break;
case 28:
//#line 65 "archivos/gramatica.y"
{yyerror("Falta el tipo de un parametro");}
break;
case 29:
//#line 66 "archivos/gramatica.y"
{yyerror("Falta el nombre de un parametro");}
break;
case 34:
//#line 75 "archivos/gramatica.y"
{yyerror("Lista de variables mal definida");}
break;
case 45:
//#line 94 "archivos/gramatica.y"
{yyerror("Falta parentesis de cierre");}
break;
case 46:
//#line 95 "archivos/gramatica.y"
{yyerror("Falta parentesis de cierre");}
break;
case 48:
//#line 99 "archivos/gramatica.y"
{yyerror("Falta expresion para la asignacion");}
break;
case 58:
//#line 115 "archivos/gramatica.y"
{checkCambioSigno();}
break;
case 61:
//#line 122 "archivos/gramatica.y"
{yyerror("Cuerpo LOOP vacio");}
break;
case 65:
//#line 130 "archivos/gramatica.y"
{yyerror("Falta palabra clave END_IF");}
break;
case 66:
//#line 131 "archivos/gramatica.y"
{yyerror("Falta palabra clave END_IF");}
break;
case 68:
//#line 135 "archivos/gramatica.y"
{yyerror("Falta palabra clave IF");}
break;
case 70:
//#line 139 "archivos/gramatica.y"
{yyerror("Cuerpo THEN vacio");}
break;
case 71:
//#line 140 "archivos/gramatica.y"
{yyerror("Falta palabra clave THEN");}
break;
case 73:
//#line 144 "archivos/gramatica.y"
{yyerror("Cuerpo ELSE vacio");}
break;
case 76:
//#line 151 "archivos/gramatica.y"
{yyerror("Condicion vacia");}
break;
//#line 678 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        if (yychar<0) yychar=0;  //clean, if necessary
        if (yydebug)
          yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
/**
 * A default run method, used for operating this parser
 * object in the background.  It is intended for extending Thread
 * or implementing Runnable.  Turn off with -Jnorun .
 */
public void run()
{
  yyparse();
}
//## end of method run() ########################################



//## Constructors ###############################################
//## The -Jnoconstruct option was used ##
//###############################################################



}
//################### END OF CLASS ##############################
