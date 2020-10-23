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
    0,    1,    1,    2,    2,    3,    3,    3,    3,    5,
    5,    6,    6,    7,    7,    8,    8,   11,   11,   11,
   11,   11,   12,   12,   13,   13,   13,   14,   14,   14,
    9,    9,   10,   10,   10,    4,    4,    4,    4,    4,
   20,   20,   20,   20,   21,   21,   21,   15,   22,   22,
   22,   22,   16,   16,   23,   23,   23,   24,   24,   24,
   25,   25,   25,   25,   17,   26,   26,   27,   18,   18,
   18,   18,   29,   29,   30,   30,   30,   31,   31,   28,
   28,   32,   32,   32,   32,   32,   32,   19,   33,   33,
   33,   33,
};
final static short yylen[] = {                            2,
    1,    1,    2,    1,    2,    5,    4,    3,    2,    2,
    1,    3,    2,    3,    1,    3,    2,    0,    1,    3,
    5,    9,    1,    1,    3,    2,    2,    2,    1,    1,
    1,    1,    1,    3,    1,    1,    1,    1,    1,    1,
    1,    2,    2,    3,    1,    2,    3,    2,    2,    3,
    1,    2,    3,    2,    3,    3,    1,    3,    3,    1,
    1,    1,    1,    2,    2,    2,    1,    2,    4,    3,
    3,    2,    2,    1,    2,    1,    1,    2,    1,    2,
    5,    1,    1,    1,    1,    1,    1,    4,    1,    1,
    1,    1,
};
final static short yydefred[] = {                         0,
    0,   31,   32,    0,    0,    0,    0,    0,    0,    1,
    0,    4,    0,    0,    0,   36,   37,   38,   39,   40,
    0,   74,    0,    0,    0,   48,   73,    0,    0,   66,
    0,   10,   61,   62,   63,   80,    0,    0,    0,   60,
    3,    5,    0,    0,   35,    0,    0,    0,   65,    0,
   77,    0,   49,    0,    0,    0,    0,   46,   92,   89,
   90,   91,    0,   64,   83,   82,   87,   86,    0,    0,
   84,   85,    0,    0,    0,   29,    0,    0,    0,    0,
   23,   24,   15,    0,    0,    0,    8,   68,   75,    0,
   70,    0,   50,    0,   43,   47,   88,    0,    0,    0,
   58,   59,   26,    0,   28,   12,    0,    0,    0,    0,
   34,   78,   69,   44,   81,   25,    0,   14,   17,    0,
    6,    0,   16,    0,    0,    0,    0,   22,
};
final static short yydgoto[] = {                          9,
   10,   11,   12,   29,   14,   44,   85,  110,   78,   47,
   79,   80,   81,   82,   16,   17,   18,   19,   20,   57,
   30,   26,   38,   39,   40,   21,   49,   22,   23,   52,
   92,   73,   63,
};
final static short yysindex[] = {                        39,
  -18,    0,    0,   -8,   38,   -6, -217,  -41,    0,    0,
   39,    0,  -15,   27, -218,    0,    0,    0,    0,    0,
 -215,    0,  171,  -27,  -43,    0,    0,  170,   -1,    0,
 -227,    0,    0,    0,    0,    0,  -43,  205,  -11,    0,
    0,    0, -198, -229,    0,   26,   14,   -8,    0,   38,
    0, -179,    0,   42,    9,  143,  -45,    0,    0,    0,
    0,    0,   44,    0,    0,    0,    0,    0,  -43,  -43,
    0,    0,  -43,  -43,  -43,    0, -191, -167,   50,   49,
    0,    0,    0,   33,  -26, -218,    0,    0,    0,   38,
    0, -169,    0,  170,    0,    0,    0,  -11,  -11,   41,
    0,    0,    0, -156,    0,    0, -198, -173,  -16,   45,
    0,    0,    0,    0,    0,    0,   61,    0,    0,  -19,
    0, -198,    0,   64, -198,   69, -198,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0, -163,    0,   76,    0,    0,    0,
   16,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   63,   88,    0,    0,    0,  134,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   55,    0,
    0,    0,  -34,    0,    0,    1,   17,    0,    0,   51,
    0,  153,    0,  112,  127,    4,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  -38, -223,  -32,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   74,
    0,  150,    0,    5,    0,    0,    0,   78,  103,    0,
    0,    0,    0,  -36,    0,    0,    0,    0,    0,   28,
    0,    0,    0,    0,    0,    0,  -31,    0,    0,    0,
    0,    0,    0,  -30,    0,    0,  -34,    0,
};
final static short yygindex[] = {                         0,
    2,    0,    0,   18,    0,    0,    0,    0,   15,   -5,
   -7,  -72,    0,    0,    0,    0,    0,    0,    0,  -33,
   -3,    0,   -4,    7,  -12,    0,    0,    8,    0,    0,
    0,    0,    0,
};
final static int YYTABLESIZE=466;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         36,
   33,   37,   30,   37,   27,   30,   18,   27,   19,   20,
   21,   27,   41,   53,   15,    2,    9,   13,   54,   51,
   55,   24,   95,    8,   64,   15,   83,    7,   13,   59,
   74,    8,   13,   31,  117,   75,   60,   45,   46,   32,
   33,   33,   25,   42,   84,   56,   89,   61,   62,  124,
   13,   69,  126,   70,   48,   88,    9,   58,   76,   33,
  114,  101,  102,    2,    3,  103,   43,    7,  100,   86,
    2,    3,   87,   56,   77,   98,   99,    8,    8,   96,
  111,  115,   93,   69,   97,   70,  112,   90,   91,  105,
  106,  104,  107,  108,   57,   57,  109,   57,  113,   57,
  116,  118,   51,  121,  122,  123,   67,  125,  119,   76,
  120,   56,  127,   57,   57,   11,   57,   55,   55,  128,
   55,   51,   55,   15,    0,   33,   13,   54,   41,   42,
    0,    0,   79,    0,    0,    0,   55,   55,    0,   55,
    2,    9,   56,   56,    0,   56,   54,   56,    0,    0,
    0,   52,    7,    0,    0,    0,    0,    0,    0,    0,
   28,   56,   56,    0,   56,    0,   53,    0,    0,    0,
   52,    0,    0,   45,    0,   76,    0,    0,    0,   57,
    0,    0,    8,    0,    0,   53,    0,   51,    0,   71,
    0,    0,   72,    0,    0,    0,    0,    0,   79,    0,
    0,   94,   55,    0,    0,    0,    0,    0,   71,    8,
    8,   72,   54,   33,    0,   33,    0,   30,    0,   27,
    0,   18,    0,   19,   20,   21,    0,   56,   45,   46,
    0,   34,   35,   34,   35,   30,   52,   27,    0,   18,
    1,   19,   20,   21,    0,    2,    3,   69,    4,   70,
    0,   53,    5,    0,    6,    7,    0,   33,   45,    0,
    0,    0,   33,   33,   71,   33,   72,   33,   33,   33,
   33,   33,   33,    9,   71,    0,    0,   72,    9,    9,
    0,    9,    0,    0,    7,    9,    0,    9,    9,    7,
    7,    0,    7,   28,    1,    1,    7,    0,    7,    7,
    2,    3,    4,    4,    0,    0,    5,    5,    6,    6,
    7,   57,   57,   57,   57,   57,    0,   76,   76,   57,
   76,   57,   57,   57,   57,   57,    0,   51,    0,   51,
   51,   51,   51,   51,   55,   55,   55,   55,   55,    0,
   79,   79,   55,   79,   55,   55,   55,   55,   55,    0,
    0,    0,   54,    0,   54,   54,   54,   54,   54,   56,
   56,   56,   56,   56,    0,    0,    0,   56,   52,   56,
   56,   56,   56,   56,    0,    0,   52,    0,   52,   52,
   52,   52,   52,   53,    0,    0,    0,    0,    0,    0,
   45,   53,    0,   53,   53,   53,   53,   53,   45,    1,
   45,   45,   45,   45,   45,    0,   71,    4,    0,   72,
    0,    5,    0,    6,   71,    0,   71,   72,   71,   71,
   71,   72,   72,   72,    0,    0,    1,    1,    0,    0,
    0,    0,    0,    0,    4,    4,   50,    0,    5,    5,
    6,    6,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   65,   66,   67,   68,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
    0,   45,   41,   45,   41,   44,   41,   44,   41,   41,
   41,    4,   11,   41,    0,    0,    0,    0,   24,   23,
   25,   40,   56,   40,   37,   11,  256,    0,   11,  257,
   42,   40,  256,   40,  107,   47,  264,  256,  257,  257,
   40,   41,   61,   59,  274,   28,   50,  275,  276,  122,
  274,   43,  125,   45,  270,   48,   40,   59,  257,   59,
   94,   74,   75,  262,  263,  257,   40,   40,   73,   44,
  262,  263,   59,   56,  273,   69,   70,   40,   40,  125,
   86,   41,   41,   43,   41,   45,   90,  267,  268,  257,
   41,   77,   44,   61,   40,   41,  123,   43,  268,   45,
  257,  275,   40,   59,   44,  125,  270,   44,  125,   59,
  109,   94,   44,   59,   60,   40,   62,   40,   41,  127,
   43,   59,   45,  109,   -1,  125,  109,   40,  125,  125,
   -1,   -1,   59,   -1,   -1,   -1,   59,   60,   -1,   62,
  125,  125,   40,   41,   -1,   43,   59,   45,   -1,   -1,
   -1,   40,  125,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  123,   59,   60,   -1,   62,   -1,   40,   -1,   -1,   -1,
   59,   -1,   -1,   40,   -1,  125,   -1,   -1,   -1,  125,
   -1,   -1,   40,   -1,   -1,   59,   -1,  125,   -1,   40,
   -1,   -1,   40,   -1,   -1,   -1,   -1,   -1,  125,   -1,
   -1,   59,  125,   -1,   -1,   -1,   -1,   -1,   59,   40,
   40,   59,  125,  257,   -1,  257,   -1,  256,   -1,  256,
   -1,  256,   -1,  256,  256,  256,   -1,  125,  256,  257,
   -1,  275,  276,  275,  276,  274,  125,  274,   -1,  274,
  257,  274,  274,  274,   -1,  262,  263,   43,  265,   45,
   -1,  125,  269,   -1,  271,  272,   -1,  257,  125,   -1,
   -1,   -1,  262,  263,   60,  265,   62,  267,  268,  269,
  270,  271,  272,  257,  125,   -1,   -1,  125,  262,  263,
   -1,  265,   -1,   -1,  257,  269,   -1,  271,  272,  262,
  263,   -1,  265,  123,  257,  257,  269,   -1,  271,  272,
  262,  263,  265,  265,   -1,   -1,  269,  269,  271,  271,
  272,  257,  258,  259,  260,  261,   -1,  267,  268,  265,
  270,  267,  268,  269,  270,  271,   -1,  265,   -1,  267,
  268,  269,  270,  271,  257,  258,  259,  260,  261,   -1,
  267,  268,  265,  270,  267,  268,  269,  270,  271,   -1,
   -1,   -1,  265,   -1,  267,  268,  269,  270,  271,  257,
  258,  259,  260,  261,   -1,   -1,   -1,  265,  257,  267,
  268,  269,  270,  271,   -1,   -1,  265,   -1,  267,  268,
  269,  270,  271,  257,   -1,   -1,   -1,   -1,   -1,   -1,
  257,  265,   -1,  267,  268,  269,  270,  271,  265,  257,
  267,  268,  269,  270,  271,   -1,  257,  265,   -1,  257,
   -1,  269,   -1,  271,  265,   -1,  267,  265,  269,  270,
  271,  269,  270,  271,   -1,   -1,  257,  257,   -1,   -1,
   -1,   -1,   -1,   -1,  265,  265,  266,   -1,  269,  269,
  271,  271,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  258,  259,  260,  261,
};
}
final static short YYFINAL=9;
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
"bloque_sentencias : sentencia bloque_sentencias",
"sentencia : sentencia_declarativa",
"sentencia : sentencia_ejecutable ';'",
"sentencia_declarativa : nombre_proc params_proc ni_proc cuerpo_proc ';'",
"sentencia_declarativa : nombre_proc params_proc ni_proc cuerpo_proc",
"sentencia_declarativa : tipo lista_variables ';'",
"sentencia_declarativa : tipo lista_variables",
"nombre_proc : PROC ID",
"nombre_proc : PROC",
"params_proc : '(' lista_params ')'",
"params_proc : '(' lista_params",
"ni_proc : NI '=' CTE_UINT",
"ni_proc : error",
"cuerpo_proc : '{' bloque_sentencias '}'",
"cuerpo_proc : '{' '}'",
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
"bloque_sentencias_ejec : sentencia_ejecutable",
"bloque_sentencias_ejec : sentencia_ejecutable ';'",
"bloque_sentencias_ejec : sentencia_ejecutable bloque_sentencias_ejec",
"bloque_sentencias_ejec : sentencia_ejecutable ';' bloque_sentencias_ejec",
"bloque_estruct_ctrl : sentencia_ejecutable",
"bloque_estruct_ctrl : sentencia_ejecutable ';'",
"bloque_estruct_ctrl : '{' bloque_sentencias_ejec '}'",
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

//#line 177 "archivos/gramatica.y"

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
	
	
	
	
	
	
//#line 473 "Parser.java"
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
case 7:
//#line 28 "archivos/gramatica.y"
{yyerror("Falta ';' al final de la sentencia");}
break;
case 9:
//#line 30 "archivos/gramatica.y"
{yyerror("Falta ';' al final de la sentencia");}
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
case 17:
//#line 46 "archivos/gramatica.y"
{yyerror("Bloque de sentencias vacio");}
break;
case 22:
//#line 53 "archivos/gramatica.y"
{yyerror("El procedimiento no puede tener mas de 3 parametros");}
break;
case 26:
//#line 61 "archivos/gramatica.y"
{yyerror("Falta el tipo de un parametro");}
break;
case 27:
//#line 62 "archivos/gramatica.y"
{yyerror("Falta el nombre de un parametro");}
break;
case 29:
//#line 66 "archivos/gramatica.y"
{yyerror("Falta el tipo de un parametro");}
break;
case 30:
//#line 67 "archivos/gramatica.y"
{yyerror("Falta el nombre de un parametro");}
break;
case 35:
//#line 76 "archivos/gramatica.y"
{yyerror("Lista de variables mal definida");}
break;
case 51:
//#line 102 "archivos/gramatica.y"
{yyerror("Falta parentesis de cierre");}
break;
case 52:
//#line 103 "archivos/gramatica.y"
{yyerror("Falta parentesis de cierre");}
break;
case 54:
//#line 107 "archivos/gramatica.y"
{yyerror("Falta expresion para la asignacion");}
break;
case 64:
//#line 123 "archivos/gramatica.y"
{checkCambioSigno();}
break;
case 67:
//#line 130 "archivos/gramatica.y"
{yyerror("Cuerpo LOOP vacio");}
break;
case 71:
//#line 138 "archivos/gramatica.y"
{yyerror("Falta palabra clave END_IF");}
break;
case 72:
//#line 139 "archivos/gramatica.y"
{yyerror("Falta palabra clave END_IF");}
break;
case 74:
//#line 143 "archivos/gramatica.y"
{yyerror("Falta palabra clave IF");}
break;
case 76:
//#line 147 "archivos/gramatica.y"
{yyerror("Cuerpo THEN vacio");}
break;
case 77:
//#line 148 "archivos/gramatica.y"
{yyerror("Falta palabra clave THEN");}
break;
case 79:
//#line 152 "archivos/gramatica.y"
{yyerror("Cuerpo ELSE vacio");}
break;
case 80:
//#line 155 "archivos/gramatica.y"
{yyerror("Condicion vacia");}
break;
//#line 718 "Parser.java"
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
