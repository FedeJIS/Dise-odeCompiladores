package analizador_sintactico;//### This file created by BYACC 1.8(/Java extension  1.15)
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


import analizador_lexico.AnalizadorLexico;
import util.CodigoFuente;
import util.TablaNotificaciones;
import util.tabla_simbolos.Celda;
import util.tabla_simbolos.TablaSimbolos;

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
    0,    2,    2,    1,    1,    3,    3,    4,    4,    5,
    5,    7,    7,    8,    8,    8,    8,   12,   12,   14,
   14,   13,   13,    9,   10,   10,   11,   11,    6,    6,
    6,    6,    6,   15,   15,   20,   20,   20,   20,   16,
   21,   21,   21,   22,   22,   22,   23,   23,   23,   23,
   17,   24,   26,   26,   26,   27,   27,   25,   28,   29,
   29,   29,   29,   29,   29,   18,   18,   30,   31,   32,
   19,   33,   33,   33,   33,
};
final static short yylen[] = {                            2,
    1,    1,    1,    2,    3,    1,    1,    0,    1,    4,
    2,    2,    1,    3,    2,    2,    1,    1,    3,    0,
    1,    3,    2,    3,    3,    2,    1,    3,    1,    1,
    1,    1,    1,    3,    4,    1,    3,    5,    7,    3,
    3,    3,    1,    3,    3,    1,    1,    1,    1,    2,
    2,    2,    2,    3,    2,    2,    3,    2,    5,    1,
    1,    1,    1,    1,    1,    4,    3,    2,    2,    2,
    4,    1,    1,    1,    1,
};
final static short yydefred[] = {                         0,
    0,    2,    3,    0,    0,    0,    0,    0,    1,    0,
    0,    6,    7,    0,   29,   30,   31,   32,   33,    0,
    0,    0,    0,    0,   68,    0,    0,   52,    0,   12,
    0,   11,    9,    0,    0,    0,    0,   51,    0,    0,
    0,   34,    0,   47,   48,   49,    0,    0,    0,   46,
    0,   55,    0,    0,   53,   75,   72,   73,   74,    0,
    0,    5,    0,   15,    0,    0,    0,    0,    0,   58,
   69,    0,   67,    0,   21,    0,   35,   50,    0,    0,
    0,    0,   61,   60,   65,   64,   62,   63,    0,    0,
   54,   71,   28,    0,   23,   14,    0,    0,    0,   10,
   70,   66,    0,    0,    0,   44,   45,    0,   57,   22,
   19,   24,   26,    0,    0,   59,   25,    0,    0,   39,
};
final static short yydgoto[] = {                          8,
    9,   10,   11,   34,   12,   13,   14,   36,   69,  100,
   32,   66,   67,   76,   15,   16,   17,   18,   19,   43,
   48,   49,   50,   20,   38,   28,   54,   25,   89,   21,
   40,   74,   60,
};
final static short yysindex[] = {                      -167,
  -29,    0,    0,  -12, -117,   25, -219,    0,    0, -209,
  -35,    0,    0,   30,    0,    0,    0,    0,    0, -192,
 -173,  -38,  -40,  -40,    0, -118,  -35,    0, -217,    0,
   53,    0,    0, -167,  -39, -175,  -12,    0, -117, -233,
   64,    0,   68,    0,    0,    0,  -40,   32,  -11,    0,
  -33,    0,  -35,  -15,    0,    0,    0,    0,    0,   70,
 -209,    0, -171,    0, -145,   72,   64,   54,   -6,    0,
    0, -117,    0, -154,    0, -139,    0,    0,  -40,  -40,
  -40,  -40,    0,    0,    0,    0,    0,    0,  -40, -232,
    0,    0,    0, -138,    0,    0, -194, -155, -107,    0,
    0,    0,   64,  -11,  -11,    0,    0,   10,    0,    0,
    0,    0,    0,   -4, -135,    0,    0,   64, -134,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,   84,    0,    0,    0,
   89,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0, -196,    0,    0,    0,
   73,    0,    0,    8, -149,    0,    0,    0,    0,    0,
  -37,    0,    0,    0,    0,    0,    0,   57,    1,    0,
    0,    0, -108,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0, -147,  -41,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    3,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  -28,   21,   41,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  -27,    0,    0,
};
final static short yygindex[] = {                         0,
  -14,   -9,    0,   -8,    0,    4,    0,    0,    0,    0,
   69,   34,    0,  -51,    0,    0,    0,    0,    0,   15,
   -2,   27,  -32,    0,    0,  -16,   39,   98,    0,    0,
    0,    0,    0,
};
final static int YYTABLESIZE=361;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         18,
   43,   64,   42,   36,   47,   26,   52,    4,   27,   79,
   22,   80,   37,   38,   78,   97,    8,  113,   55,   62,
   41,   51,   71,   33,    1,   65,   87,   24,   88,   53,
   81,   23,    4,   72,   73,   82,    5,   30,    6,   56,
   42,   43,   27,   43,   90,   43,   57,   31,  106,  107,
  116,  115,   79,   94,   80,  101,   40,   58,   59,   43,
   43,   41,   43,   41,   29,   41,  119,    2,    3,   35,
    8,    8,   27,    8,   79,   27,   80,   37,   63,   41,
   41,   42,   41,   42,  114,   42,  108,   65,    8,    1,
    2,    3,   39,   53,    2,    3,   61,    4,   68,   42,
   42,    5,   42,    6,    7,  104,  105,   75,   77,   91,
   92,   95,   96,  102,   98,   40,   99,  103,  110,  112,
  117,  118,   41,   13,   17,   43,   16,   56,  109,   93,
  111,   27,    4,  120,   70,    0,    0,    0,    1,    1,
    0,    0,    0,    0,    0,   41,    4,    4,    8,    1,
    5,    5,    6,    6,    2,    3,    8,    4,    0,    0,
    8,    5,    8,    6,    7,   42,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   40,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   27,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    8,    0,    0,   44,    0,   41,   20,
   20,   20,    2,    3,   83,   84,   85,   86,   20,   20,
    0,   20,   18,   63,   45,   46,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   43,   43,   43,
   43,   43,   43,   43,    0,   43,    0,   43,   43,   43,
   43,   43,   43,    0,    0,    0,    0,   41,   41,   41,
   41,   41,   41,   41,    0,   41,    0,   41,   41,   41,
   41,   41,   41,    0,    0,    0,    0,   42,   42,   42,
   42,   42,   42,   42,    0,   42,    0,   42,   42,   42,
   42,   42,   42,   40,    0,    0,    0,    0,   40,   40,
    0,   40,    0,   40,   40,   40,   40,   40,   40,   27,
    0,    0,    0,    0,   27,   27,    0,   27,    0,    0,
    0,   27,    0,   27,   27,    8,    0,    0,    0,    0,
    8,    8,    0,    8,    0,    0,    0,    8,    0,    8,
    8,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
    0,   41,   41,   41,   45,  123,  125,    0,    5,   43,
   40,   45,   41,   41,   47,   67,  125,  125,   27,   34,
    0,   24,   39,   59,  257,   35,   60,   40,   62,   26,
   42,   61,  265,  267,  268,   47,  269,  257,  271,  257,
    0,   41,   39,   43,   53,   45,  264,  257,   81,   82,
   41,  103,   43,   63,   45,   72,    0,  275,  276,   59,
   60,   41,   62,   43,   40,   45,  118,  262,  263,   40,
  267,  268,    0,  270,   43,   72,   45,  270,  273,   59,
   60,   41,   62,   43,   99,   45,   89,   97,    0,  257,
  262,  263,  266,   90,  262,  263,   44,  265,  274,   59,
   60,  269,   62,  271,  272,   79,   80,   44,   41,  125,
   41,  257,   41,  268,   61,   59,  123,  257,  257,  275,
  125,  257,  257,   40,  274,  125,  274,  125,   90,   61,
   97,   59,  125,  119,   37,   -1,   -1,   -1,  257,  257,
   -1,   -1,   -1,   -1,   -1,  125,  265,  265,  257,  257,
  269,  269,  271,  271,  262,  263,  265,  265,   -1,   -1,
  269,  269,  271,  271,  272,  125,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  125,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  125,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  125,   -1,   -1,  257,   -1,  257,  257,
  262,  263,  262,  263,  258,  259,  260,  261,  257,  257,
   -1,  273,  274,  273,  275,  276,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  257,  258,  259,
  260,  261,  262,  263,   -1,  265,   -1,  267,  268,  269,
  270,  271,  272,   -1,   -1,   -1,   -1,  257,  258,  259,
  260,  261,  262,  263,   -1,  265,   -1,  267,  268,  269,
  270,  271,  272,   -1,   -1,   -1,   -1,  257,  258,  259,
  260,  261,  262,  263,   -1,  265,   -1,  267,  268,  269,
  270,  271,  272,  257,   -1,   -1,   -1,   -1,  262,  263,
   -1,  265,   -1,  267,  268,  269,  270,  271,  272,  257,
   -1,   -1,   -1,   -1,  262,  263,   -1,  265,   -1,   -1,
   -1,  269,   -1,  271,  272,  257,   -1,   -1,   -1,   -1,
  262,  263,   -1,  265,   -1,   -1,   -1,  269,   -1,  271,
  272,
};
}
final static short YYFINAL=8;
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
"tipo_id : UINT",
"tipo_id : DOUBLE",
"bloque_sentencias : tipo_sentencia fin_sentencia",
"bloque_sentencias : tipo_sentencia fin_sentencia bloque_sentencias",
"tipo_sentencia : sentencia_decl",
"tipo_sentencia : sentencia_ejec",
"fin_sentencia :",
"fin_sentencia : ';'",
"sentencia_decl : nombre_proc params_proc ni_proc cuerpo_proc",
"sentencia_decl : tipo_id lista_variables",
"nombre_proc : PROC ID",
"nombre_proc : PROC",
"params_proc : '(' lista_params_decl ')'",
"params_proc : '(' ')'",
"params_proc : '(' lista_params_decl",
"params_proc : '('",
"lista_params_decl : param",
"lista_params_decl : param separador_variables lista_params_decl",
"separador_variables :",
"separador_variables : ','",
"param : VAR tipo_id ID",
"param : tipo_id ID",
"ni_proc : NI '=' CTE_UINT",
"cuerpo_proc : '{' bloque_sentencias '}'",
"cuerpo_proc : '{' '}'",
"lista_variables : ID",
"lista_variables : ID ',' lista_variables",
"sentencia_ejec : invocacion",
"sentencia_ejec : asignacion",
"sentencia_ejec : loop",
"sentencia_ejec : if",
"sentencia_ejec : print",
"invocacion : ID '(' ')'",
"invocacion : ID '(' lista_params_inv ')'",
"lista_params_inv : ID",
"lista_params_inv : ID separador_variables ID",
"lista_params_inv : ID separador_variables ID separador_variables ID",
"lista_params_inv : ID separador_variables ID separador_variables ID separador_variables lista_params_inv",
"asignacion : ID '=' expresion",
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
"loop : cuerpo_loop cuerpo_until",
"cuerpo_loop : LOOP bloque_estruct_ctrl",
"bloque_estruct_ctrl : sentencia_ejec fin_sentencia",
"bloque_estruct_ctrl : '{' bloque_sentencias_ejec '}'",
"bloque_estruct_ctrl : '{' '}'",
"bloque_sentencias_ejec : sentencia_ejec fin_sentencia",
"bloque_sentencias_ejec : sentencia_ejec fin_sentencia bloque_sentencias_ejec",
"cuerpo_until : UNTIL condicion",
"condicion : '(' expresion comparador expresion ')'",
"comparador : COMP_MAYOR_IGUAL",
"comparador : COMP_MENOR_IGUAL",
"comparador : '<'",
"comparador : '>'",
"comparador : COMP_IGUAL",
"comparador : COMP_DISTINTO",
"if : encabezado_if rama_then rama_else END_IF",
"if : encabezado_if rama_then END_IF",
"encabezado_if : IF condicion",
"rama_then : THEN bloque_estruct_ctrl",
"rama_else : ELSE bloque_estruct_ctrl",
"print : OUT '(' imprimible ')'",
"imprimible : CADENA",
"imprimible : CTE_UINT",
"imprimible : CTE_DOUBLE",
"imprimible : ID",
};

//#line 149 "archivos/gramatica.y"


    private AnalizadorLexico aLexico;
    private TablaSimbolos tablaS;
    private CodigoFuente cFuente;

    /**
     * Create a parser, setting the debug to true or false.
     *
     * @param debugMe true for debugging, false for no debug.
     */
    public Parser(boolean debugMe, AnalizadorLexico aLexico, TablaSimbolos tablaS, CodigoFuente cFuente) {
        yydebug = debugMe;
        this.aLexico = aLexico;
        this.tablaS = tablaS;
        this.cFuente = cFuente;
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
        TablaNotificaciones.agregarError("Error en la linea " + aLexico.getLineaActual() + ": " + mensajeError);
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
        } else
            TablaNotificaciones.agregarError("Error en la linea " + aLexico.getLineaActual() + ": No se permiten UINT negativos");

    }



























			

//#line 445 "Parser.java"
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
case 8:
//#line 22 "archivos/gramatica.y"
{yyerror("Falta ';' al final de la sentencia.");}
break;
case 13:
//#line 31 "archivos/gramatica.y"
{yyerror("Falta el identificador del procedimiento.");}
break;
case 16:
//#line 36 "archivos/gramatica.y"
{yyerror("Falta el parentesis de cierre para los parametros.");}
break;
case 17:
//#line 37 "archivos/gramatica.y"
{yyerror("Falta el parentesis de cierre para los parametros.");}
break;
case 20:
//#line 44 "archivos/gramatica.y"
{yyerror("Falta una ',' para separar dos parametros.");}
break;
case 26:
//#line 56 "archivos/gramatica.y"
{yyerror("Cuerpo del procedimiento vacio.");}
break;
case 39:
//#line 77 "archivos/gramatica.y"
{yyerror("Un procedimiento no puede tener mas de 3 parametros.");}
break;
case 50:
//#line 96 "archivos/gramatica.y"
{}
break;
case 55:
//#line 107 "archivos/gramatica.y"
{yyerror("Bloque de sentencias vacio.");}
break;
//#line 630 "Parser.java"
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
