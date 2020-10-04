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






//#line 2 "gramatica.y"
package analizador_sintactico;

import analizador_lexico.AnalizadorLexico;
import util.tabla_simbolos.Celda;
import util.tabla_simbolos.TablaDeSimbolos;
//#line 23 "Parser.java"




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
   11,   11,   13,   13,   14,   14,   14,   15,   15,   15,
    9,    9,   10,   10,   10,    4,    4,    4,    4,    4,
   16,   21,   21,   21,   21,   17,   17,   22,   22,   22,
   23,   23,   23,   24,   24,   24,   24,   18,   25,   25,
   26,   19,   19,   19,   19,   28,   28,   29,   29,   29,
   30,   30,   12,   12,   27,   27,   31,   31,   31,   31,
   31,   31,   20,   32,   32,   32,   32,
};
final static short yylen[] = {                            2,
    1,    1,    2,    3,    1,    1,    1,    4,    2,    2,
    1,    3,    2,    3,    1,    3,    0,    1,    3,    5,
    7,    9,    1,    1,    3,    2,    2,    2,    1,    1,
    1,    1,    1,    3,    1,    1,    1,    1,    1,    1,
    2,    2,    3,    1,    2,    3,    2,    3,    3,    1,
    3,    3,    1,    1,    1,    1,    2,    2,    2,    1,
    2,    4,    3,    3,    2,    2,    1,    2,    1,    1,
    2,    1,    2,    3,    2,    5,    1,    1,    1,    1,
    1,    1,    4,    1,    1,    1,    1,
};
final static short yydefred[] = {                         0,
    7,    0,   31,   32,    0,    0,    0,    0,    0,    0,
    1,    0,    5,    6,    0,    0,   36,   37,   38,   39,
   40,    0,   67,    0,    0,    0,   41,   66,    0,    0,
   59,    0,   10,   54,   55,   56,   75,    0,    0,    0,
   53,    0,    0,    0,   35,    0,    9,    0,   58,    0,
   70,    0,   42,    0,    0,    0,   73,   87,   84,   85,
   86,    0,   57,   78,   77,   82,   81,    0,    0,   79,
   80,    0,    0,    0,    4,   29,    0,    0,    0,    0,
   23,   24,   15,    0,    0,    0,   61,   68,    0,   63,
    0,   43,   74,   83,    0,    0,    0,   51,   52,   26,
    0,   28,   12,    0,    0,    0,    8,   34,   71,   62,
   76,   25,    0,   14,    0,    0,   16,    0,    0,    0,
    0,   22,
};
final static short yydgoto[] = {                         10,
   11,   30,   13,   14,   15,   44,   85,  107,   16,   47,
   79,   31,   80,   81,   82,   17,   18,   19,   20,   21,
   27,   39,   40,   41,   22,   49,   23,   24,   52,   91,
   72,   62,
};
final static short yysindex[] = {                        51,
    0,  -14,    0,    0,    9,   40,   31, -179,  -41,    0,
    0,   34,    0,    0,   77, -216,    0,    0,    0,    0,
    0, -235,    0,   29,  -24,  -26,    0,    0,   51,   66,
    0, -225,    0,    0,    0,    0,    0,  -26,   -7,   -5,
    0,   51, -165, -229,    0,   85,    0,    9,    0,   40,
    0, -152,    0,   89,   58,    6,    0,    0,    0,    0,
    0,   91,    0,    0,    0,    0,    0,  -26,  -26,    0,
    0,  -26,  -26,  -26,    0,    0, -143, -124,   93,   92,
    0,    0,    0,   74,   14, -216,    0,    0,   40,    0,
 -129,    0,    0,    0,   -5,   -5,   59,    0,    0,    0,
 -115,    0,    0, -165, -131,   40,    0,    0,    0,    0,
    0,    0,  105,    0,   26, -165,    0,  110, -165,  112,
 -165,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0, -113,    0,  118,    0,    0,
    0,   16,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   15,   20,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    3,
    0,   21,  -34,    0,    0,   18,    0,    0,    0,    1,
    0,   22,    0,   25,   28,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  -36, -222,  -31,
    0,    0,    0,    0,    0,    0,    0,    0,    2,    0,
   30,    0,    0,    0,   13,   23,    0,    0,    0,    0,
  -35,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  -30,    0,    0,    0,    0,  -29,    0,  -27,
  -34,    0,
};
final static short yygindex[] = {                         0,
   70,   76,    0,    0,    0,    0,    0,    0,  -10,    4,
   39,    7,   -9,    0,    0,    0,    0,    0,    0,    0,
    0,   -2,   53,   50,    0,    0,   38,    0,    0,    0,
    0,    0,
};
final static int YYTABLESIZE=323;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         37,
   69,   72,   50,   38,   30,   27,   17,   30,   27,   18,
   19,   20,   48,   21,   44,    2,   53,   33,   38,   47,
    3,   65,   49,   55,   45,   25,   83,   46,   54,   64,
   51,   58,   78,   13,   48,   68,   73,   69,   59,   45,
   46,   74,   28,   50,   84,   50,   26,   50,    9,   60,
   61,   13,   70,   48,   71,   48,   88,   48,   33,   69,
   72,   50,   50,   49,   50,   49,  101,   49,    9,   97,
   32,   48,   48,   44,   48,   12,   33,   33,   47,    9,
   65,   49,   49,   45,   49,   87,   46,   63,   64,  108,
    9,   76,   42,   78,  113,  109,    3,    4,   56,  111,
   68,   68,   69,   69,   12,   78,  118,   77,   78,  120,
   78,   75,  115,  100,   89,   90,   43,   12,    3,    4,
   95,   96,   98,   99,   57,   69,   72,   50,   86,   92,
   93,   94,  102,  103,  105,  104,  106,   48,  110,   44,
    2,  112,   33,  114,   47,    3,   65,   49,  116,   45,
  117,   29,   46,  119,   64,  121,   60,   11,    0,  122,
    0,    0,   29,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   34,    0,    0,    0,   30,
   27,   17,    0,    0,   18,   19,   20,    0,   21,    0,
   34,   45,   46,   35,   36,    0,    0,   30,   27,   17,
    0,    0,   18,   19,   20,    0,   21,    0,   35,   36,
   64,   65,   66,   67,    0,    0,    0,    0,    0,    0,
   50,   50,   50,   50,    0,    0,    0,   69,   69,   72,
   48,   48,   48,   48,    0,    0,    0,    0,    0,    0,
   49,   49,   49,   49,    1,    2,    0,    0,    0,    0,
    3,    4,    0,    5,   50,    1,    2,    6,    0,    7,
    8,    3,    4,    0,    5,    0,    1,    2,    6,    0,
    7,    8,    3,    4,    0,    5,    0,    0,    0,    6,
    0,    7,    8,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
    0,    0,    0,   45,   41,   41,   41,   44,   44,   41,
   41,   41,    0,   41,    0,    0,   41,    0,   45,    0,
    0,    0,    0,   26,    0,   40,  256,    0,   25,    0,
   24,  257,   43,  256,  270,   43,   42,   45,  264,  256,
  257,   47,    5,   41,  274,   43,   61,   45,   40,  275,
  276,  274,   60,   41,   62,   43,   50,   45,   41,   59,
   59,   59,   60,   41,   62,   43,   77,   45,   40,   72,
   40,   59,   60,   59,   62,    0,   59,  257,   59,   40,
   59,   59,   60,   59,   62,   48,   59,   38,   59,   86,
   40,  257,   59,  104,  104,   89,  262,  263,   29,   41,
   43,   43,   45,   45,   29,  116,  116,  273,  119,  119,
  121,   42,  106,  257,  267,  268,   40,   42,  262,  263,
   68,   69,   73,   74,   59,  125,  125,  125,   44,   41,
  125,   41,  257,   41,   61,   44,  123,  125,  268,  125,
  125,  257,  125,  275,  125,  125,  125,  125,   44,  125,
  125,  123,  125,   44,  125,   44,  270,   40,   -1,  121,
   -1,   -1,  123,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  257,   -1,   -1,   -1,  256,
  256,  256,   -1,   -1,  256,  256,  256,   -1,  256,   -1,
  257,  256,  257,  275,  276,   -1,   -1,  274,  274,  274,
   -1,   -1,  274,  274,  274,   -1,  274,   -1,  275,  276,
  258,  259,  260,  261,   -1,   -1,   -1,   -1,   -1,   -1,
  258,  259,  260,  261,   -1,   -1,   -1,  267,  268,  268,
  258,  259,  260,  261,   -1,   -1,   -1,   -1,   -1,   -1,
  258,  259,  260,  261,  256,  257,   -1,   -1,   -1,   -1,
  262,  263,   -1,  265,  266,  256,  257,  269,   -1,  271,
  272,  262,  263,   -1,  265,   -1,  256,  257,  269,   -1,
  271,  272,  262,  263,   -1,  265,   -1,   -1,   -1,  269,
   -1,  271,  272,
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
"cuerpo_proc : '{' bloque_estruct_ctrl '}'",
"lista_params :",
"lista_params : param",
"lista_params : param ',' param",
"lista_params : param ',' param ',' param",
"lista_params : param ',' param ',' param ',' param",
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
"bloque_estruct_ctrl : sentencia ';'",
"bloque_estruct_ctrl : '{' bloque_sentencias '}'",
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

//#line 169 "gramatica.y"

    private AnalizadorLexico aLexico;
    private TablaDeSimbolos tablaS;

    /**
     * Create a parser, setting the debug to true or false.
     *
     * @param debugMe true for debugging, false for no debug.
     */
    public Parser(boolean debugMe, AnalizadorLexico aLexico, TablaDeSimbolos tablaS) {
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
        System.err.println("Error en la linea " + aLexico.getLineaActual() + ": " + mensajeError);
    }

    private void checkCambioSigno() {
        String lexemaSignoNoC = yylval.sval; //Obtengo el lexema del factor.
        Celda celdaOriginal = tablaS.getValor(lexemaSignoNoC); //La sentencia va aca si o si, porque mas adelante ya no existe la entrada en la TS.

        if (celdaOriginal.getTipo().equals("DOUBLE")) {
            tablaS.quitarReferencia(lexemaSignoNoC); //El lexema esta en la TS si o si. refs--.
            if (tablaS.entradaSinReferencias(lexemaSignoNoC)) tablaS.eliminarEntrada(lexemaSignoNoC);

            String lexemaSignoC = String.valueOf(Double.parseDouble(lexemaSignoNoC) * -1); //Cambio el signo del factor.
            if (!tablaS.contieneLexema(lexemaSignoC)) {
                tablaS.agregarEntrada(celdaOriginal.getToken(), lexemaSignoC, celdaOriginal.getTipo());
            }
        }
    }
	
	
	
	
	
	
//#line 435 "Parser.java"
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
//#line 18 "gramatica.y"
{yyerror("Falta ';' al final de la sentencia");}
break;
case 7:
//#line 25 "gramatica.y"
{yyerror("Sentencia mal definida");}
break;
case 11:
//#line 33 "gramatica.y"
{yyerror("Procedimiento sin nombre");}
break;
case 13:
//#line 37 "gramatica.y"
{yyerror("Falta parentesis de cierre de parametros");}
break;
case 15:
//#line 41 "gramatica.y"
{yyerror("Formato incorrecto de NI. El formato correcto es: 'NI = CTE_UINT'");}
break;
case 21:
//#line 51 "gramatica.y"
{yyerror("El procedimiento no puede tener mas de 3 parametros");}
break;
case 22:
//#line 52 "gramatica.y"
{yyerror("El procedimiento no puede tener mas de 3 parametros");}
break;
case 26:
//#line 60 "gramatica.y"
{yyerror("Falta el tipo de un parametro");}
break;
case 27:
//#line 61 "gramatica.y"
{yyerror("Falta el nombre de un parametro");}
break;
case 29:
//#line 65 "gramatica.y"
{yyerror("Falta el tipo de un parametro");}
break;
case 30:
//#line 66 "gramatica.y"
{yyerror("Falta el nombre de un parametro");}
break;
case 35:
//#line 75 "gramatica.y"
{yyerror("Lista de variables mal definida");}
break;
case 44:
//#line 90 "gramatica.y"
{yyerror("Falta parentesis de cierre");}
break;
case 45:
//#line 91 "gramatica.y"
{yyerror("Falta parentesis de cierre");}
break;
case 47:
//#line 95 "gramatica.y"
{yyerror("Falta expresion para la asignacion");}
break;
case 57:
//#line 111 "gramatica.y"
{checkCambioSigno();}
break;
case 60:
//#line 118 "gramatica.y"
{yyerror("Cuerpo LOOP vacio");}
break;
case 64:
//#line 126 "gramatica.y"
{yyerror("Falta palabra clave END_IF");}
break;
case 65:
//#line 127 "gramatica.y"
{yyerror("Falta palabra clave END_IF");}
break;
case 67:
//#line 131 "gramatica.y"
{yyerror("Falta palabra clave IF");}
break;
case 69:
//#line 135 "gramatica.y"
{yyerror("Cuerpo THEN vacio");}
break;
case 70:
//#line 136 "gramatica.y"
{yyerror("Falta palabra clave THEN");}
break;
case 72:
//#line 140 "gramatica.y"
{yyerror("Cuerpo ELSE vacio");}
break;
case 75:
//#line 147 "gramatica.y"
{yyerror("Condicion vacia");}
break;
//#line 680 "Parser.java"
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
