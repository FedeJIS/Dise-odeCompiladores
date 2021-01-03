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






//#line 1 "archivos/gramatica.y"

package analizador_sintactico;

import analizador_lexico.AnalizadorLexico;
import analizador_sintactico.util.ParserHelper;
import generacion_c_intermedio.MultiPolaca;
import generacion_c_intermedio.PilaAmbitos;
import generacion_c_intermedio.Polaca;
import util.TablaNotificaciones;
import util.tabla_simbolos.Celda;
import util.tabla_simbolos.TablaSimbolos;
//#line 29 "Parser.java"




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
    5,   11,   11,    7,    7,    8,    8,    8,    8,   12,
   12,   12,   12,   14,   14,   13,   13,   15,   15,   15,
   16,   16,    9,    9,    9,    9,    9,   10,   10,    6,
    6,    6,    6,    6,   17,   17,   23,   23,   23,   22,
   22,   22,   22,   18,   18,   18,   18,   24,   24,   24,
   25,   25,   25,   26,   26,   26,   26,   21,   21,   21,
   27,   27,   19,   28,   29,   29,   31,   31,   31,   31,
   32,   32,   32,   32,   30,   30,   33,   33,   33,   33,
   33,   34,   34,   34,   34,   34,   34,   20,   20,   35,
   35,   36,   36,   38,   38,   37,   37,
};
final static short yylen[] = {                            2,
    1,    1,    1,    2,    3,    1,    1,    0,    1,    4,
    2,    1,    3,    2,    1,    3,    2,    2,    1,    1,
    3,    5,    7,    0,    1,    1,    1,    3,    2,    2,
    2,    1,    3,    2,    2,    2,    1,    3,    2,    1,
    1,    1,    1,    1,    3,    4,    1,    1,    1,    1,
    3,    5,    7,    3,    3,    1,    3,    3,    3,    1,
    3,    3,    1,    1,    1,    1,    2,    4,    3,    4,
    1,    1,    3,    1,    1,    0,    2,    3,    2,    2,
    2,    3,    2,    3,    2,    1,    5,    4,    4,    4,
    3,    1,    1,    1,    1,    1,    1,    4,    3,    2,
    1,    2,    1,    2,    1,    2,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    2,    3,    0,   74,    0,    0,    0,    1,
    0,    0,    6,    7,    0,   40,   41,   42,   43,   44,
    0,    0,    0,    0,    0,    0,  100,    0,   14,    0,
   11,    9,    0,    0,    0,    0,    0,    0,    0,   75,
    0,    0,    0,   64,   65,   66,    0,    0,    0,   63,
   47,   48,   49,   45,    0,    0,   55,    0,    0,   93,
   92,   97,   96,   94,   95,    0,    0,    0,   71,   72,
    0,    0,    5,    0,   17,    0,    0,    0,   26,   27,
   37,    0,    0,    0,   79,    0,    0,    0,   80,   77,
    0,   73,    0,    0,    0,   99,   67,    0,    0,    0,
    0,   46,   25,    0,   91,    0,    0,   70,   68,   13,
   29,    0,   31,   16,    0,   36,    0,   35,    0,   10,
    0,    0,   78,   85,  106,   98,    0,    0,   61,   62,
    0,   90,    0,   89,   28,    0,   33,   39,    0,   84,
   82,    0,   87,    0,   38,    0,    0,    0,    0,   53,
   23,
};
final static short yydgoto[] = {                          9,
   10,   11,   12,   33,   13,   14,   15,   35,   84,  120,
   31,   77,   78,  104,   79,   80,   16,   17,   18,   19,
   20,   55,   56,   48,   49,   50,   71,   21,   39,   92,
   40,   88,   27,   67,   22,   42,   95,   43,
};
final static short yysindex[] = {                       257,
  -27,  -18,    0,    0,    8,    0,   16, -226,    0,    0,
 -217,   31,    0,    0,   29,    0,    0,    0,    0,    0,
 -109, -172,  -26,  -37,  147,  307,    0,  208,    0,   55,
    0,    0,  257,  -33,   -9,  -98,   31,   31, -168,    0,
 -109, -160, -148,    0,    0,    0,  -26,   42,  -10,    0,
    0,    0,    0,    0,   80,   84,    0,   42,   83,    0,
    0,    0,    0,    0,    0,  313,  -26,   88,    0,    0,
   90, -217,    0, -189,    0, -125,   95,   84,    0,    0,
    0,  -56, -142,   14,    0,   31,   31,   13,    0,    0,
    8,    0,    0, -109, -127,    0,    0,  -26,  -26,  -26,
  -26,    0,    0, -204,    0,  -39,    4,    0,    0,    0,
    0, -115,    0,    0, -237,    0, -132,    0,  -75,    0,
  257,  257,    0,    0,    0,    0,  -10,  -10,    0,    0,
   84,    0,   52,    0,    0,   84,    0,    0,   19,    0,
    0, -204,    0, -237,    0,   84,   84, -204, -237,    0,
    0,
};
final static short yyrindex[] = {                         0,
    0,   75,    0,    0, -121,    0,    0,  110,    0,    0,
    0,  191,    0,    0,    0,    0,    0,    0,    0,    0,
 -118,    0,    0,    0,    0,    0,    0,    0,    0,   92,
    0,    0,   10,   61,    0,    0, -162, -162,    0,    0,
 -191,    0,    0,    0,    0,    0,    0,  109,    1,    0,
    0,    0,    0,    0,    0,  -34,    0,  127,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  149,    0,    0,    0,    0,  -41,   62,  214,    0,    0,
    0,    0,    0,    0,    0,  -58,  -58,    0,    0,    0,
  169,    0, -157, -113,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  278,    0,    0,    0,    0,   34,    0,    0,    0,
   36,   45,    0,    0,    0,    0,   21,   41,    0,    0,
  -32,    0,   58,    0,    0,  235,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  -30,  244,    0,    0,    0,
    0,
};
final static short yygindex[] = {                         0,
  -21,  -19,    0,   -8,   -3,   18,    0,    0,    0,    0,
  100,   26,  -87,  255,    0,    0,    0,    0,    0,    0,
    0,   28,  -91,   -2,   15,  -12,    0,    0,    0,    0,
  -24,   -6,   86,  112,    0,    0,    0,    0,
};
final static int YYTABLESIZE=583;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         32,
   60,  132,   32,   54,  117,   47,   50,   75,   51,    4,
   52,   73,  131,   36,   76,   70,   93,   37,   47,   32,
   58,   24,   58,   66,    3,    4,   85,  136,   89,   90,
   29,  100,   86,   23,   97,   74,  101,   37,   38,   30,
   59,   60,   25,   60,  134,   60,   98,   26,   99,  138,
  146,   83,   51,   87,  112,   28,  147,   88,   38,   60,
   60,   58,   60,   58,  107,   58,    8,  111,   34,  125,
   52,   53,    3,    4,   56,  103,  105,  121,  122,   58,
   58,   59,   58,   59,   98,   59,   99,  129,  130,   32,
   37,   12,  143,   41,   98,   76,   99,  139,   72,   59,
   59,   91,   59,  133,    8,    8,   94,    8,   57,  102,
  104,   38,  127,  128,  140,  141,   88,   86,   86,   96,
  102,   19,   18,  105,   76,   60,   54,  103,  108,   76,
  109,  113,  118,   56,    4,  114,  119,  123,   87,   87,
  126,  135,  137,  145,  101,   58,    1,    2,   69,   15,
   12,   76,    3,    4,  107,    5,   34,    1,    2,    6,
   83,    7,    8,    3,    4,   59,    5,   57,   86,   81,
    6,  110,    7,    8,  151,  150,  124,  106,    0,    0,
    1,    2,   88,    0,    0,   54,    3,    4,    0,    5,
    8,   47,    0,    6,    0,    7,    8,    8,    8,   56,
    0,    0,    0,    8,    8,    0,    8,   69,    0,    0,
    8,    0,    8,    8,   32,    0,   12,   44,  116,   51,
   32,   32,   24,    0,   24,    0,   24,   86,    3,    4,
   44,   32,   32,   57,    0,   45,   46,   52,   53,   74,
   24,   24,   24,   24,   24,   24,   81,    0,   45,   46,
    0,   54,   47,    0,   20,    0,   60,   60,   60,   60,
   60,   60,   60,   60,   82,   60,   60,   60,   60,   60,
   60,   60,   60,   69,   20,   21,   58,   58,   58,   58,
   58,   58,   58,   58,   22,   58,   58,   58,   58,   58,
   58,   58,   58,   86,    0,   21,   59,   59,   59,   59,
   59,   59,   59,   59,   22,   59,   59,   59,   59,   59,
   59,   59,   59,   88,   88,    8,   19,   18,   30,   88,
   88,   30,   88,   88,   88,   88,   88,   88,   88,   88,
   56,   56,  115,    0,   19,   18,   56,   56,   30,   56,
    0,   56,   56,   56,   56,   56,   56,   12,   12,    0,
    0,   47,    0,   12,   12,   98,   12,   99,   12,   12,
   12,   12,   12,   12,   57,   57,   64,    0,   65,    0,
   57,   57,   64,   57,   65,   57,   57,   57,   57,   57,
   57,    0,   54,   54,    0,  142,    0,    0,   54,   54,
  144,   54,    0,   54,   54,   54,   54,   54,   54,    0,
  148,  149,   57,   44,   69,   69,    0,    0,    0,    0,
   69,   69,    0,   69,    0,   69,   69,   69,   69,   69,
   69,   45,   46,    0,   86,   86,    0,    0,    0,    0,
   86,   86,    0,   86,    0,   86,   86,   86,   86,   86,
   86,    0,    0,    0,    0,    0,    8,    8,    0,    0,
    0,    0,    8,    8,    0,    8,    0,    0,    0,    8,
    0,    8,    8,   68,   44,    0,    0,    0,    0,   20,
    0,   69,    0,    0,    0,   24,   24,    0,    0,    0,
    0,    0,   45,   46,    0,    0,   24,   20,    0,    0,
   21,    0,    0,    0,    0,    0,   24,   24,    0,   22,
    0,    0,    0,    0,    0,   24,   24,   24,   21,    0,
    0,    0,    1,    2,    0,    0,   24,   22,    3,    4,
    0,    5,    0,    0,    0,    6,    0,    7,    8,    0,
    0,    0,    0,   30,    0,    0,    0,    0,    0,   30,
   30,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   30,   30,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   59,   44,   60,   61,   62,   63,    0,    0,
   60,   61,   62,   63,    0,    0,    0,    0,    0,    0,
    0,   45,   46,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
    0,   41,   44,   41,   61,   45,   41,   41,   41,    0,
   41,   33,  104,  123,   34,   28,   41,   21,   45,   61,
    0,   40,   25,   26,  262,  263,  125,  115,   37,   38,
  257,   42,   36,   61,   47,  273,   47,   41,   21,  257,
    0,   41,   61,   43,   41,   45,   43,   40,   45,  125,
  142,   61,  257,   36,   74,   40,  144,    0,   41,   59,
   60,   41,   62,   43,   67,   45,  125,  257,   40,   94,
  275,  276,  262,  263,    0,  267,  268,   86,   87,   59,
   60,   41,   62,   43,   43,   45,   45,  100,  101,   59,
   94,    0,   41,  266,   43,  115,   45,  119,   44,   59,
   60,  270,   62,  106,  267,  268,  267,  270,    0,  267,
  268,   94,   98,   99,  121,  122,   59,  121,  122,  268,
   41,   61,   61,   41,  144,  125,    0,   44,   41,  149,
   41,  257,  275,   59,  125,   41,  123,  125,  121,  122,
  268,  257,  275,  125,  266,  125,  256,  257,    0,   40,
   59,  270,  262,  263,  268,  265,  123,  256,  257,  269,
  125,  271,  272,  262,  263,  125,  265,   59,    0,  125,
  269,   72,  271,  272,  149,  148,   91,   66,   -1,   -1,
  256,  257,  125,   -1,   -1,   59,  262,  263,   -1,  265,
    0,   45,   -1,  269,   -1,  271,  272,  256,  257,  125,
   -1,   -1,   -1,  262,  263,   -1,  265,   59,   -1,   -1,
  269,   -1,  271,  272,  256,   -1,  125,  257,  275,  257,
  262,  263,  257,   -1,  257,   -1,  257,   59,  262,  263,
  257,  273,  274,  125,   -1,  275,  276,  275,  276,  273,
  275,  276,  275,  276,  275,  276,  256,   -1,  275,  276,
   -1,  125,   45,   -1,   41,   -1,  256,  257,  258,  259,
  260,  261,  262,  263,  274,  265,  266,  267,  268,  269,
  270,  271,  272,  125,   61,   41,  256,  257,  258,  259,
  260,  261,  262,  263,   41,  265,  266,  267,  268,  269,
  270,  271,  272,  125,   -1,   61,  256,  257,  258,  259,
  260,  261,  262,  263,   61,  265,  266,  267,  268,  269,
  270,  271,  272,  256,  257,  125,  256,  256,   41,  262,
  263,   44,  265,  266,  267,  268,  269,  270,  271,  272,
  256,  257,   78,   -1,  274,  274,  262,  263,   61,  265,
   -1,  267,  268,  269,  270,  271,  272,  256,  257,   -1,
   -1,   45,   -1,  262,  263,   43,  265,   45,  267,  268,
  269,  270,  271,  272,  256,  257,   60,   -1,   62,   -1,
  262,  263,   60,  265,   62,  267,  268,  269,  270,  271,
  272,   -1,  256,  257,   -1,  131,   -1,   -1,  262,  263,
  136,  265,   -1,  267,  268,  269,  270,  271,  272,   -1,
  146,  147,  256,  257,  256,  257,   -1,   -1,   -1,   -1,
  262,  263,   -1,  265,   -1,  267,  268,  269,  270,  271,
  272,  275,  276,   -1,  256,  257,   -1,   -1,   -1,   -1,
  262,  263,   -1,  265,   -1,  267,  268,  269,  270,  271,
  272,   -1,   -1,   -1,   -1,   -1,  256,  257,   -1,   -1,
   -1,   -1,  262,  263,   -1,  265,   -1,   -1,   -1,  269,
   -1,  271,  272,  256,  257,   -1,   -1,   -1,   -1,  256,
   -1,  264,   -1,   -1,   -1,  262,  263,   -1,   -1,   -1,
   -1,   -1,  275,  276,   -1,   -1,  273,  274,   -1,   -1,
  256,   -1,   -1,   -1,   -1,   -1,  262,  263,   -1,  256,
   -1,   -1,   -1,   -1,   -1,  262,  263,  273,  274,   -1,
   -1,   -1,  256,  257,   -1,   -1,  273,  274,  262,  263,
   -1,  265,   -1,   -1,   -1,  269,   -1,  271,  272,   -1,
   -1,   -1,   -1,  256,   -1,   -1,   -1,   -1,   -1,  262,
  263,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  273,  274,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  256,  257,  258,  259,  260,  261,   -1,   -1,
  258,  259,  260,  261,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  275,  276,
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
"lista_variables : ID",
"lista_variables : ID ',' lista_variables",
"nombre_proc : PROC ID",
"nombre_proc : PROC",
"params_proc : '(' lista_params_decl ')'",
"params_proc : '(' ')'",
"params_proc : '(' lista_params_decl",
"params_proc : '('",
"lista_params_decl : param",
"lista_params_decl : param separador_variables param",
"lista_params_decl : param separador_variables param separador_variables param",
"lista_params_decl : param separador_variables param separador_variables param separador_variables lista_params_decl",
"separador_variables :",
"separador_variables : ','",
"param : param_var",
"param : param_comun",
"param_var : VAR tipo_id ID",
"param_var : VAR ID",
"param_var : VAR tipo_id",
"param_comun : tipo_id ID",
"param_comun : tipo_id",
"ni_proc : NI '=' CTE_UINT",
"ni_proc : NI '='",
"ni_proc : '=' CTE_UINT",
"ni_proc : NI CTE_UINT",
"ni_proc : error",
"cuerpo_proc : '{' bloque_sentencias '}'",
"cuerpo_proc : '{' '}'",
"sentencia_ejec : invocacion",
"sentencia_ejec : asignacion",
"sentencia_ejec : loop",
"sentencia_ejec : if",
"sentencia_ejec : print",
"invocacion : ID '(' ')'",
"invocacion : ID '(' lista_params_inv ')'",
"param_inv : ID",
"param_inv : CTE_UINT",
"param_inv : CTE_DOUBLE",
"lista_params_inv : param_inv",
"lista_params_inv : param_inv separador_variables param_inv",
"lista_params_inv : param_inv separador_variables param_inv separador_variables param_inv",
"lista_params_inv : param_inv separador_variables param_inv separador_variables param_inv separador_variables lista_params_inv",
"asignacion : ID '=' expresion",
"asignacion : ID '=' error",
"asignacion : ID",
"asignacion : error '=' expresion",
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
"print : OUT '(' imprimible ')'",
"print : OUT '(' imprimible",
"print : OUT '(' error ')'",
"imprimible : CADENA",
"imprimible : factor",
"loop : encab_loop cuerpo_loop cuerpo_until",
"encab_loop : LOOP",
"cuerpo_loop : bloque_estruct_ctrl",
"cuerpo_loop :",
"bloque_estruct_ctrl : sentencia_ejec fin_sentencia",
"bloque_estruct_ctrl : '{' bloque_sentencias_ejec '}'",
"bloque_estruct_ctrl : '{' '}'",
"bloque_estruct_ctrl : sentencia_decl fin_sentencia",
"bloque_sentencias_ejec : sentencia_ejec fin_sentencia",
"bloque_sentencias_ejec : sentencia_ejec fin_sentencia bloque_sentencias_ejec",
"bloque_sentencias_ejec : sentencia_decl fin_sentencia",
"bloque_sentencias_ejec : sentencia_decl fin_sentencia bloque_sentencias_ejec",
"cuerpo_until : UNTIL condicion",
"cuerpo_until : UNTIL",
"condicion : '(' expresion comparador expresion ')'",
"condicion : '(' expresion comparador expresion",
"condicion : '(' comparador expresion ')'",
"condicion : '(' expresion comparador ')'",
"condicion : '(' error ')'",
"comparador : COMP_MAYOR_IGUAL",
"comparador : COMP_MENOR_IGUAL",
"comparador : '<'",
"comparador : '>'",
"comparador : COMP_IGUAL",
"comparador : COMP_DISTINTO",
"if : encabezado_if rama_then rama_else END_IF",
"if : encabezado_if rama_then_prima END_IF",
"encabezado_if : IF condicion",
"encabezado_if : IF",
"rama_then : THEN bloque_estruct_ctrl",
"rama_then : THEN",
"rama_then_prima : THEN bloque_estruct_ctrl",
"rama_then_prima : THEN",
"rama_else : ELSE bloque_estruct_ctrl",
"rama_else : ELSE",
};

//#line 216 "archivos/gramatica.y"

    private final ParserHelper helper;

    private final AnalizadorLexico aLexico;
    private final Polaca polacaProgram;
    private final MultiPolaca polacaProcedimientos;

    public Parser(AnalizadorLexico aLexico, TablaSimbolos tablaS) {
        this.aLexico = aLexico;
        this.polacaProgram = new Polaca();
        this.polacaProcedimientos = new MultiPolaca();

        helper = new ParserHelper(aLexico, tablaS, new PilaAmbitos(), polacaProgram, polacaProcedimientos);
    }

    private int yylex() {
        int token = aLexico.produceToken();
        yylval = new ParserVal(aLexico.ultimoLexemaGenerado);
        return token;
    }

    private void yyerror(String mensajeError) {
        TablaNotificaciones.agregarError(aLexico.getLineaActual(), mensajeError);
    }

    //---POLACA---

    public Polaca getPolacaProgram() {
        return polacaProgram;
    }

    public MultiPolaca getPolacaProcs() {
        return polacaProcedimientos;
    }

//#line 515 "Parser.java"
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
//        yyerror("syntax error");
        yynerrs++;
        }t
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
//#line 23 "archivos/gramatica.y"
{helper.setUltimoTipoLeido("UINT");}
break;
case 3:
//#line 24 "archivos/gramatica.y"
{helper.setUltimoTipoLeido("DOUBLE");}
break;
case 8:
//#line 35 "archivos/gramatica.y"
{TablaNotificaciones.agregarError(aLexico.getLineaActual()-1,"Falta ';' al final de la sentencia.");}
break;
case 10:
//#line 39 "archivos/gramatica.y"
{helper.eliminarUltimoAmbito();}
break;
case 12:
//#line 43 "archivos/gramatica.y"
{helper.declaracionVar(val_peek(0).sval);}
break;
case 13:
//#line 44 "archivos/gramatica.y"
{helper.declaracionVar(val_peek(2).sval);}
break;
case 14:
//#line 47 "archivos/gramatica.y"
{helper.lecturaIdProc(val_peek(0).sval);}
break;
case 15:
//#line 48 "archivos/gramatica.y"
{yyerror("Falta el identificador del procedimiento.");}
break;
case 18:
//#line 53 "archivos/gramatica.y"
{yyerror("Falta el parentesis de cierre para los parametros.");}
break;
case 19:
//#line 54 "archivos/gramatica.y"
{yyerror("Falta el parentesis de cierre para los parametros.");}
break;
case 23:
//#line 61 "archivos/gramatica.y"
{yyerror("Un procedimiento no puede tener mas de 3 parametros.");}
break;
case 24:
//#line 64 "archivos/gramatica.y"
{yyerror("Falta una ',' para separar dos parametros.");}
break;
case 28:
//#line 72 "archivos/gramatica.y"
{helper.lecturaParamFormal(val_peek(0).sval, Celda.USO_PARAM_CVR);}
break;
case 29:
//#line 73 "archivos/gramatica.y"
{yyerror("Falta el tipo de un parametro.");}
break;
case 30:
//#line 74 "archivos/gramatica.y"
{yyerror("Falta el identificador de un parametro.");}
break;
case 31:
//#line 77 "archivos/gramatica.y"
{helper.lecturaParamFormal(val_peek(0).sval, Celda.USO_PARAM_CV);}
break;
case 32:
//#line 78 "archivos/gramatica.y"
{yyerror("Falta el identificador de un parametro.");}
break;
case 33:
//#line 81 "archivos/gramatica.y"
{helper.lecturaNumInvoc(Integer.parseInt(val_peek(0).sval), false, "");}
break;
case 34:
//#line 82 "archivos/gramatica.y"
{helper.lecturaNumInvoc(0, true, "Falta el numero de invocaciones del procedimiento.");}
break;
case 35:
//#line 83 "archivos/gramatica.y"
{helper.lecturaNumInvoc(0, true, "Falta la palabra clave 'NI' en el encabezado del procedimiento.");}
break;
case 36:
//#line 84 "archivos/gramatica.y"
{helper.lecturaNumInvoc(0, true, "Formato de declaracion de NI invalido. El formato correcto es 'NI = CTE_UINT'.");}
break;
case 37:
//#line 85 "archivos/gramatica.y"
{helper.lecturaNumInvoc(0, true, "Formato de declaracion de NI invalido. El formato correcto es 'NI = CTE_UINT'.");}
break;
case 39:
//#line 89 "archivos/gramatica.y"
{yyerror("Cuerpo del procedimiento vacio.");}
break;
case 45:
//#line 99 "archivos/gramatica.y"
{helper.invocacionProc(val_peek(2).sval);}
break;
case 46:
//#line 100 "archivos/gramatica.y"
{helper.invocacionProc(val_peek(3).sval);}
break;
case 47:
//#line 103 "archivos/gramatica.y"
{helper.guardaParamsInvoc(val_peek(0).sval);}
break;
case 48:
//#line 104 "archivos/gramatica.y"
{helper.guardaParamsInvoc(val_peek(0).sval);}
break;
case 49:
//#line 105 "archivos/gramatica.y"
{helper.guardaParamsInvoc(val_peek(0).sval);}
break;
case 53:
//#line 112 "archivos/gramatica.y"
{yyerror("Un procedimiento no puede tener mas de 3 parametros.");}
break;
case 54:
//#line 115 "archivos/gramatica.y"
{helper.lecturaDestAsign(val_peek(2).sval);}
break;
case 55:
//#line 116 "archivos/gramatica.y"
{
                            helper.lecturaDestAsign(val_peek(2).sval);
                            yyerror("El lado derecho de la asignacio no es valido.");
                            }
break;
case 56:
//#line 120 "archivos/gramatica.y"
{
                    helper.lecturaDestAsign(val_peek(0).sval);
                    yyerror("Un identificador en solitario no es una sentencia valida.");
                    }
break;
case 57:
//#line 124 "archivos/gramatica.y"
{yyerror("El lado izquierdo de la asignacion no es valido");}
break;
case 58:
//#line 128 "archivos/gramatica.y"
{helper.agregarPasosRepr("+");}
break;
case 59:
//#line 129 "archivos/gramatica.y"
{helper.agregarPasosRepr("-");}
break;
case 61:
//#line 133 "archivos/gramatica.y"
{helper.agregarPasosRepr("*");}
break;
case 62:
//#line 134 "archivos/gramatica.y"
{helper.agregarPasosRepr("/");}
break;
case 64:
//#line 138 "archivos/gramatica.y"
{helper.lecturaFactor(val_peek(0).sval);}
break;
case 65:
//#line 139 "archivos/gramatica.y"
{helper.agregarPasosRepr(val_peek(0).sval);helper.setTipoUltimoFactor("UINT");}
break;
case 66:
//#line 140 "archivos/gramatica.y"
{helper.agregarPasosRepr(val_peek(0).sval);helper.setTipoUltimoFactor("DOUBLE");}
break;
case 67:
//#line 141 "archivos/gramatica.y"
{helper.cambioSignoFactor(yylval.sval);}
break;
case 71:
//#line 149 "archivos/gramatica.y"
{helper.agregarPasosRepr(val_peek(0).sval, "OUT_CAD");}
break;
case 72:
//#line 150 "archivos/gramatica.y"
{helper.impresionFactor();}
break;
case 74:
//#line 156 "archivos/gramatica.y"
{helper.puntoControlLoop();}
break;
case 76:
//#line 160 "archivos/gramatica.y"
{yyerror("Falta el bloque de sentencias ejecutables del LOOP.");}
break;
case 79:
//#line 165 "archivos/gramatica.y"
{yyerror("Bloque de sentencias vacio.");}
break;
case 80:
//#line 166 "archivos/gramatica.y"
{yyerror("No se permiten sentencias declarativas dentro de un bloque de estructura de control.");}
break;
case 83:
//#line 172 "archivos/gramatica.y"
{yyerror("No se permiten sentencias declarativas dentro de un bloque de estructura de control.");}
break;
case 84:
//#line 174 "archivos/gramatica.y"
{yyerror("No se permiten sentencias declarativas dentro de un bloque de estructura de control.");}
break;
case 85:
//#line 177 "archivos/gramatica.y"
{helper.puntoControlUntil();}
break;
case 86:
//#line 178 "archivos/gramatica.y"
{yyerror("Falta la condicion de corte del LOOP.");}
break;
case 87:
//#line 181 "archivos/gramatica.y"
{helper.agregarPasosRepr(val_peek(2).sval);}
break;
case 88:
//#line 182 "archivos/gramatica.y"
{yyerror("Falta parentesis de cierre de la condicion.");}
break;
case 89:
//#line 183 "archivos/gramatica.y"
{yyerror("Falta expresion en el lado izquierdo de la condicion.");}
break;
case 90:
//#line 184 "archivos/gramatica.y"
{yyerror("Falta expresion en el lado derecho de la condicion.");}
break;
case 91:
//#line 185 "archivos/gramatica.y"
{yyerror("Error en la condicion.");}
break;
case 100:
//#line 200 "archivos/gramatica.y"
{helper.puntoControlThen();}
break;
case 101:
//#line 201 "archivos/gramatica.y"
{yyerror("Falta la condicion del IF.");}
break;
case 102:
//#line 204 "archivos/gramatica.y"
{helper.puntoControlElse();}
break;
case 103:
//#line 205 "archivos/gramatica.y"
{yyerror("Falta el bloque de sentencias ejecutables de la rama THEN.");}
break;
case 104:
//#line 208 "archivos/gramatica.y"
{helper.puntoControlFinCondicional();}
break;
case 105:
//#line 209 "archivos/gramatica.y"
{yyerror("Falta el bloque de sentencias ejecutables de la rama THEN.");}
break;
case 106:
//#line 212 "archivos/gramatica.y"
{helper.puntoControlFinCondicional();}
break;
case 107:
//#line 213 "archivos/gramatica.y"
{yyerror("Falta el bloque de sentencias ejecutables de la rama ELSE.");}
break;
//#line 926 "Parser.java"
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
