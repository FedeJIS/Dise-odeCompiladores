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






//#line 2 "C:\Users\federico.iribarren\Documents\Diseño de Compiladores I\src\main\java\analizador_sintactico\archivos\gramatica.y"
package analizador_sintactico;

import analizador_lexico.AnalizadorLexico;
import analizador_sintactico.util.ParserHelper;
import generacion_c_intermedio.MultiPolaca;
import generacion_c_intermedio.PilaAmbitos;
import generacion_c_intermedio.Polaca;
import util.TablaNotificaciones;
import util.tabla_simbolos.Celda;
import util.tabla_simbolos.TablaSimbolos;
//#line 28 "Parser.java"




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
public final static short MAS_IGUAL=277;
public final static short MENOS_IGUAL=278;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    2,    2,    1,    1,    3,    3,    4,    4,    5,
    5,   11,   11,    7,    7,    8,    8,    8,    8,   12,
   12,   12,   12,   14,   14,   13,   13,   15,   15,   15,
   16,   16,    9,    9,    9,    9,    9,   10,   10,    6,
    6,    6,    6,    6,   17,   17,   23,   23,   23,   22,
   22,   22,   22,   18,   18,   18,   18,   18,   18,   18,
   18,   24,   24,   24,   25,   25,   25,   26,   26,   26,
   26,   21,   21,   21,   27,   27,   19,   28,   29,   29,
   31,   31,   31,   31,   32,   32,   32,   32,   30,   30,
   33,   33,   33,   33,   33,   34,   34,   34,   34,   34,
   34,   20,   20,   35,   35,   36,   36,   38,   38,   37,
   37,
};
final static short yylen[] = {                            2,
    1,    1,    1,    2,    3,    1,    1,    0,    1,    4,
    2,    1,    3,    2,    1,    3,    2,    2,    1,    1,
    3,    5,    7,    0,    1,    1,    1,    3,    2,    2,
    2,    1,    3,    2,    2,    2,    1,    3,    2,    1,
    1,    1,    1,    1,    3,    4,    1,    1,    1,    1,
    3,    5,    7,    3,    3,    3,    3,    3,    3,    1,
    3,    3,    3,    1,    3,    3,    1,    1,    1,    1,
    2,    4,    3,    4,    1,    1,    3,    1,    1,    0,
    2,    3,    2,    2,    2,    3,    2,    3,    2,    1,
    5,    4,    4,    4,    3,    1,    1,    1,    1,    1,
    1,    4,    3,    2,    1,    2,    1,    2,    1,    2,
    1,
};
final static short yydefred[] = {                         0,
    0,    0,    2,    3,    0,   78,    0,    0,    0,    1,
    0,    0,    6,    7,    0,   40,   41,   42,   43,   44,
    0,    0,    0,    0,    0,    0,    0,    0,  104,    0,
   14,    0,   11,    9,    0,    0,    0,    0,    0,    0,
    0,   79,    0,    0,    0,   68,   69,   70,    0,    0,
    0,   67,   58,    0,   57,    0,   47,   48,   49,   45,
    0,    0,   59,    0,    0,   97,   96,  101,  100,   98,
   99,    0,    0,    0,   75,   76,    0,    0,    5,    0,
   17,    0,    0,    0,   26,   27,   37,    0,    0,    0,
   83,    0,    0,    0,   84,   81,    0,   77,    0,    0,
    0,  103,   71,    0,    0,    0,    0,   46,   25,    0,
   95,    0,    0,   74,   72,   13,   29,    0,   31,   16,
    0,   36,    0,   35,    0,   10,    0,    0,   82,   89,
  110,  102,    0,    0,   65,   66,    0,   94,    0,   93,
   28,    0,   33,   39,    0,   88,   86,    0,   91,    0,
   38,    0,    0,    0,    0,   53,   23,
};
final static short yydgoto[] = {                          9,
   10,   11,   12,   35,   13,   14,   15,   37,   90,  126,
   33,   83,   84,  110,   85,   86,   16,   17,   18,   19,
   20,   61,   62,   50,   51,   52,   77,   21,   41,   98,
   42,   94,   29,   73,   22,   44,  101,   45,
};
final static short yysindex[] = {                       -67,
  -37,  -38,    0,    0,  -13,    0,    7, -223,    0,    0,
 -217,    9,    0,    0,   11,    0,    0,    0,    0,    0,
  -92, -207,  167,  147,  152,  -32,  272,  340,    0,  111,
    0,   33,    0,    0,  -67,  -41,  -55,  304,    9,    9,
 -177,    0,  -92, -163, -160,    0,    0,    0,  167,   42,
   23,    0,    0,   42,    0,   42,    0,    0,    0,    0,
   84,   72,    0,   42,   89,    0,    0,    0,    0,    0,
    0,  390,  167,   90,    0,    0,   94, -217,    0, -227,
    0, -121,   97,   72,    0,    0,    0,  -57, -136,   17,
    0,    9,    9,   16,    0,    0,  -13,    0,    0,  -92,
 -126,    0,    0,  167,  167,  167,  167,    0,    0, -219,
    0,  -34,   26,    0,    0,    0,    0, -114,    0,    0,
 -234,    0, -131,    0,  355,    0,  -67,  -67,    0,    0,
    0,    0,   23,   23,    0,    0,   72,    0,   31,    0,
    0,   72,    0,    0,   20,    0,    0, -219,    0, -234,
    0,   72,   72, -219, -234,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,   75,    0,    0, -119,    0,    0,  108,    0,    0,
    0,  233,    0,    0,    0,    0,    0,    0,    0,    0,
 -120,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   92,    0,    0,    3,  -43,    0,    0, -171, -171,
    0,    0, -157,    0,    0,    0,    0,    0,    0,  109,
    1,    0,    0,  127,    0,  149,    0,    0,    0,    0,
    0,  -28,    0,  169,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  191,    0,    0,    0,
    0,  -36,   62,  244,    0,    0,    0,    0,    0,    0,
    0,  366,  366,    0,    0,    0,  216,    0, -155, -116,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  278,    0,    0,
    0,    0,   30,    0,    0,    0,   29,   32,    0,    0,
    0,    0,   21,   41,    0,    0,  -22,    0,   58,    0,
    0,  309,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   77,  332,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  -23,  -26,    0,   67,   -6,    5,    0,    0,    0,    0,
   80,    6, -105,  -64,    0,    0,    0,    0,    0,    0,
    0,    8,  -93,   25,   10,  -16,    0,    0,    0,    0,
  -21,   -8,   66,   83,    0,    0,    0,    0,
};
final static int YYTABLESIZE=651;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         81,
   64,   26,    4,  123,   32,   89,  138,   32,   60,   82,
   49,   79,   50,   76,   39,  142,  137,   19,   51,  121,
   62,   99,   27,   23,   32,   40,   28,    3,    4,  117,
   38,   92,  103,   31,    3,    4,   39,   57,   80,   32,
   63,   64,   93,   64,  153,   64,   30,   40,   54,   56,
   36,   64,   72,  118,  152,   58,   59,   92,   43,   64,
   64,   62,   64,   62,  106,   62,  140,   34,  104,  107,
  105,  149,  148,  104,   60,  105,   78,  150,  131,   62,
   62,   63,   62,   63,  104,   63,  105,  154,  155,  135,
  136,   12,   97,   39,   82,    8,    8,  113,    8,   63,
   63,  145,   63,  100,   40,   95,   96,  102,   61,  107,
  109,  106,  108,  133,  134,  109,   92,   52,  146,  147,
   92,   92,   18,   82,  108,   64,   56,    4,   82,  111,
  114,   93,   93,   60,  115,  119,  139,  120,  124,  125,
  129,  132,  141,  143,  151,   62,  105,   15,   55,   80,
   12,  111,   34,   87,  112,   49,   85,  116,  127,  128,
  157,  156,  130,    1,    2,   63,    0,   61,   54,    3,
    4,    0,    5,    0,    0,    0,    6,    0,    7,    8,
    0,    0,   92,    0,    0,   56,    0,    0,    1,    2,
   73,   49,    0,    0,    3,    4,   49,    5,    0,   60,
   87,    6,    0,    7,    8,    0,    0,   55,    0,    0,
    0,   49,   19,    0,    0,   90,   12,  122,   88,   32,
    3,    4,   46,    0,   57,   32,   32,   54,   24,    0,
   19,   80,    8,   61,   24,    0,   32,   32,   24,   25,
   47,   48,   58,   59,    0,    0,   24,   24,    0,   73,
    0,   56,   24,   24,    0,    0,   64,   64,   64,   64,
   64,   64,   64,   64,    0,   64,   64,   64,   64,   64,
   64,   64,   64,   55,   90,    0,   62,   62,   62,   62,
   62,   62,   62,   62,   20,   62,   62,   62,   62,   62,
   62,   62,   62,   54,    0,    0,   63,   63,   63,   63,
   63,   63,   63,   63,   20,   63,   63,   63,   63,   63,
   63,   63,   63,   92,   92,   73,   49,   18,   30,   92,
   92,   30,   92,   92,   92,   92,   92,   92,   92,   92,
   60,   60,    0,   24,    0,   18,   60,   60,   30,   60,
   90,   60,   60,   60,   60,   60,   60,   12,   12,   21,
    0,   24,   24,   12,   12,    0,   12,    8,   12,   12,
   12,   12,   12,   12,   61,   61,   74,   46,    0,   21,
   61,   61,   22,   61,   75,   61,   61,   61,   61,   61,
   61,    0,   56,   56,   49,   47,   48,    0,   56,   56,
    0,   56,   22,   56,   56,   56,   56,   56,   56,   70,
    0,   71,   53,   46,   55,   55,    0,   55,   46,    0,
   55,   55,    0,   55,    0,   55,   55,   55,   55,   55,
   55,   47,   48,   46,   54,   54,   47,   48,   91,    0,
   54,   54,  104,   54,  105,   54,   54,   54,   54,   54,
   54,   47,   48,    0,    0,    0,   73,   73,    0,   70,
    0,   71,   73,   73,    0,   73,    0,   73,   73,   73,
   73,   73,   73,    0,    0,    0,    0,    0,    0,    0,
    0,   90,   90,    0,    0,    0,    0,   90,   90,  144,
   90,    0,   90,   90,   90,   90,   90,   90,    8,    8,
    8,    0,    0,    0,    8,    8,    0,    8,    0,   20,
    0,    8,    0,    8,    8,   24,   24,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   24,   20,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   63,   46,    0,
    0,    0,    0,   30,    0,    0,    0,    0,    0,   30,
   30,    0,    0,    0,    0,    0,   47,   48,    0,    0,
   30,   30,    0,    0,    0,    0,    0,    0,    0,    1,
    2,    0,    0,    0,   21,    3,    4,    0,    5,    0,
   24,   24,    6,    0,    7,    8,    0,    0,    0,    0,
    0,   24,   21,    0,    0,    0,    0,   22,    0,    0,
    0,    0,    0,   24,   24,   65,   46,   66,   67,   68,
   69,    0,    0,    0,   24,   22,    0,    0,    0,    0,
    1,    2,    0,    0,   47,   48,    3,    4,    0,    5,
    0,    8,    8,    6,    0,    7,    8,    8,    8,    0,
    8,    0,    0,    0,    8,    0,    8,    8,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   66,   67,   68,
   69,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
    0,   40,    0,   61,   41,   61,   41,   44,   41,   36,
   45,   35,   41,   30,   21,  121,  110,   61,   41,   84,
    0,   43,   61,   61,   61,   21,   40,  262,  263,  257,
  123,   38,   49,  257,  262,  263,   43,  257,  273,  257,
    0,   41,   38,   43,  150,   45,   40,   43,   24,   25,
   40,   27,   28,   80,  148,  275,  276,    0,  266,   59,
   60,   41,   62,   43,   42,   45,   41,   59,   43,   47,
   45,   41,  137,   43,    0,   45,   44,  142,  100,   59,
   60,   41,   62,   43,   43,   45,   45,  152,  153,  106,
  107,    0,  270,  100,  121,  267,  268,   73,  270,   59,
   60,  125,   62,  267,  100,   39,   40,  268,    0,  267,
  268,  267,  268,  104,  105,   44,   59,   41,  127,  128,
  127,  128,   61,  150,   41,  125,    0,  125,  155,   41,
   41,  127,  128,   59,   41,  257,  112,   41,  275,  123,
  125,  268,  257,  275,  125,  125,  266,   40,    0,  270,
   59,  268,  123,  125,   72,   45,  125,   78,   92,   93,
  155,  154,   97,  256,  257,  125,   -1,   59,    0,  262,
  263,   -1,  265,   -1,   -1,   -1,  269,   -1,  271,  272,
   -1,   -1,  125,   -1,   -1,   59,   -1,   -1,  256,  257,
    0,   45,   -1,   -1,  262,  263,   45,  265,   -1,  125,
  256,  269,   -1,  271,  272,   -1,   -1,   59,   -1,   -1,
   -1,   45,  256,   -1,   -1,    0,  125,  275,  274,  256,
  262,  263,  257,   -1,  257,  262,  263,   59,  257,   -1,
  274,  273,    0,  125,  257,   -1,  273,  274,  277,  278,
  275,  276,  275,  276,   -1,   -1,  275,  276,   -1,   59,
   -1,  125,  275,  276,   -1,   -1,  256,  257,  258,  259,
  260,  261,  262,  263,   -1,  265,  266,  267,  268,  269,
  270,  271,  272,  125,   59,   -1,  256,  257,  258,  259,
  260,  261,  262,  263,   41,  265,  266,  267,  268,  269,
  270,  271,  272,  125,   -1,   -1,  256,  257,  258,  259,
  260,  261,  262,  263,   61,  265,  266,  267,  268,  269,
  270,  271,  272,  256,  257,  125,   45,  256,   41,  262,
  263,   44,  265,  266,  267,  268,  269,  270,  271,  272,
  256,  257,   -1,  257,   -1,  274,  262,  263,   61,  265,
  125,  267,  268,  269,  270,  271,  272,  256,  257,   41,
   -1,  275,  276,  262,  263,   -1,  265,  125,  267,  268,
  269,  270,  271,  272,  256,  257,  256,  257,   -1,   61,
  262,  263,   41,  265,  264,  267,  268,  269,  270,  271,
  272,   -1,  256,  257,   45,  275,  276,   -1,  262,  263,
   -1,  265,   61,  267,  268,  269,  270,  271,  272,   60,
   -1,   62,  256,  257,  256,  257,   -1,  256,  257,   -1,
  262,  263,   -1,  265,   -1,  267,  268,  269,  270,  271,
  272,  275,  276,  257,  256,  257,  275,  276,  125,   -1,
  262,  263,   43,  265,   45,  267,  268,  269,  270,  271,
  272,  275,  276,   -1,   -1,   -1,  256,  257,   -1,   60,
   -1,   62,  262,  263,   -1,  265,   -1,  267,  268,  269,
  270,  271,  272,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  256,  257,   -1,   -1,   -1,   -1,  262,  263,  125,
  265,   -1,  267,  268,  269,  270,  271,  272,  256,  257,
  125,   -1,   -1,   -1,  262,  263,   -1,  265,   -1,  256,
   -1,  269,   -1,  271,  272,  262,  263,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  273,  274,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  256,  257,   -1,
   -1,   -1,   -1,  256,   -1,   -1,   -1,   -1,   -1,  262,
  263,   -1,   -1,   -1,   -1,   -1,  275,  276,   -1,   -1,
  273,  274,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  256,
  257,   -1,   -1,   -1,  256,  262,  263,   -1,  265,   -1,
  262,  263,  269,   -1,  271,  272,   -1,   -1,   -1,   -1,
   -1,  273,  274,   -1,   -1,   -1,   -1,  256,   -1,   -1,
   -1,   -1,   -1,  262,  263,  256,  257,  258,  259,  260,
  261,   -1,   -1,   -1,  273,  274,   -1,   -1,   -1,   -1,
  256,  257,   -1,   -1,  275,  276,  262,  263,   -1,  265,
   -1,  256,  257,  269,   -1,  271,  272,  262,  263,   -1,
  265,   -1,   -1,   -1,  269,   -1,  271,  272,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  258,  259,  260,
  261,
};
}
final static short YYFINAL=9;
final static short YYMAXTOKEN=278;
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
"MAS_IGUAL","MENOS_IGUAL",
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
"asignacion : ID MENOS_IGUAL expresion",
"asignacion : ID MAS_IGUAL expresion",
"asignacion : ID MENOS_IGUAL error",
"asignacion : ID MAS_IGUAL error",
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

//#line 370 "C:\Users\federico.iribarren\Documents\Diseño de Compiladores I\src\main\java\analizador_sintactico\archivos\gramatica.y"

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

//#line 534 "Parser.java"
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
//#line 23 "C:\Users\federico.iribarren\Documents\Diseño de Compiladores I\src\main\java\analizador_sintactico\archivos\gramatica.y"
{helper.setUltimoTipoLeido("UINT");}
break;
case 3:
//#line 24 "C:\Users\federico.iribarren\Documents\Diseño de Compiladores I\src\main\java\analizador_sintactico\archivos\gramatica.y"
{helper.setUltimoTipoLeido("DOUBLE");}
break;
case 8:
//#line 35 "C:\Users\federico.iribarren\Documents\Diseño de Compiladores I\src\main\java\analizador_sintactico\archivos\gramatica.y"
{TablaNotificaciones.agregarError(aLexico.getLineaActual()-1,"Falta ';' al final de la sentencia.");}
break;
case 10:
//#line 39 "C:\Users\federico.iribarren\Documents\Diseño de Compiladores I\src\main\java\analizador_sintactico\archivos\gramatica.y"
{helper.eliminarUltimoAmbito();}
break;
case 12:
//#line 43 "C:\Users\federico.iribarren\Documents\Diseño de Compiladores I\src\main\java\analizador_sintactico\archivos\gramatica.y"
{helper.declaracionVar(val_peek(0).sval);}
break;
case 13:
//#line 44 "C:\Users\federico.iribarren\Documents\Diseño de Compiladores I\src\main\java\analizador_sintactico\archivos\gramatica.y"
{helper.declaracionVar(val_peek(2).sval);}
break;
case 14:
//#line 47 "C:\Users\federico.iribarren\Documents\Diseño de Compiladores I\src\main\java\analizador_sintactico\archivos\gramatica.y"
{helper.lecturaIdProc(val_peek(0).sval);}
break;
case 15:
//#line 48 "C:\Users\federico.iribarren\Documents\Diseño de Compiladores I\src\main\java\analizador_sintactico\archivos\gramatica.y"
{yyerror("Falta el identificador del procedimiento.");}
break;
case 18:
//#line 53 "C:\Users\federico.iribarren\Documents\Diseño de Compiladores I\src\main\java\analizador_sintactico\archivos\gramatica.y"
{yyerror("Falta el parentesis de cierre para los parametros.");}
break;
case 19:
//#line 54 "C:\Users\federico.iribarren\Documents\Diseño de Compiladores I\src\main\java\analizador_sintactico\archivos\gramatica.y"
{yyerror("Falta el parentesis de cierre para los parametros.");}
break;
case 23:
//#line 61 "C:\Users\federico.iribarren\Documents\Diseño de Compiladores I\src\main\java\analizador_sintactico\archivos\gramatica.y"
{yyerror("Un procedimiento no puede tener mas de 3 parametros.");}
break;
case 24:
//#line 64 "C:\Users\federico.iribarren\Documents\Diseño de Compiladores I\src\main\java\analizador_sintactico\archivos\gramatica.y"
{yyerror("Falta una ',' para separar dos parametros.");}
break;
case 28:
//#line 72 "C:\Users\federico.iribarren\Documents\Diseño de Compiladores I\src\main\java\analizador_sintactico\archivos\gramatica.y"
{helper.lecturaParamFormal(val_peek(0).sval, Celda.USO_PARAM_CVR);}
break;
case 29:
//#line 73 "C:\Users\federico.iribarren\Documents\Diseño de Compiladores I\src\main\java\analizador_sintactico\archivos\gramatica.y"
{yyerror("Falta el tipo de un parametro.");}
break;
case 30:
//#line 74 "C:\Users\federico.iribarren\Documents\Diseño de Compiladores I\src\main\java\analizador_sintactico\archivos\gramatica.y"
{yyerror("Falta el identificador de un parametro.");}
break;
case 31:
//#line 77 "C:\Users\federico.iribarren\Documents\Diseño de Compiladores I\src\main\java\analizador_sintactico\archivos\gramatica.y"
{helper.lecturaParamFormal(val_peek(0).sval, Celda.USO_PARAM_CV);}
break;
case 32:
//#line 78 "C:\Users\federico.iribarren\Documents\Diseño de Compiladores I\src\main\java\analizador_sintactico\archivos\gramatica.y"
{yyerror("Falta el identificador de un parametro.");}
break;
case 33:
//#line 81 "C:\Users\federico.iribarren\Documents\Diseño de Compiladores I\src\main\java\analizador_sintactico\archivos\gramatica.y"
{helper.lecturaNumInvoc(Integer.parseInt(val_peek(0).sval), false, "");}
break;
case 34:
//#line 82 "C:\Users\federico.iribarren\Documents\Diseño de Compiladores I\src\main\java\analizador_sintactico\archivos\gramatica.y"
{helper.lecturaNumInvoc(0, true, "Falta el numero de invocaciones del procedimiento.");}
break;
case 35:
//#line 83 "C:\Users\federico.iribarren\Documents\Diseño de Compiladores I\src\main\java\analizador_sintactico\archivos\gramatica.y"
{helper.lecturaNumInvoc(0, true, "Falta la palabra clave 'NI' en el encabezado del procedimiento.");}
break;
case 36:
//#line 84 "C:\Users\federico.iribarren\Documents\Diseño de Compiladores I\src\main\java\analizador_sintactico\archivos\gramatica.y"
{helper.lecturaNumInvoc(0, true, "Formato de declaracion de NI invalido. El formato correcto es 'NI = CTE_UINT'.");}
break;
case 37:
//#line 85 "C:\Users\federico.iribarren\Documents\Diseño de Compiladores I\src\main\java\analizador_sintactico\archivos\gramatica.y"
{helper.lecturaNumInvoc(0, true, "Formato de declaracion de NI invalido. El formato correcto es 'NI = CTE_UINT'.");}
break;
case 39:
//#line 89 "C:\Users\federico.iribarren\Documents\Diseño de Compiladores I\src\main\java\analizador_sintactico\archivos\gramatica.y"
{yyerror("Cuerpo del procedimiento vacio.");}
break;
case 45:
//#line 99 "C:\Users\federico.iribarren\Documents\Diseño de Compiladores I\src\main\java\analizador_sintactico\archivos\gramatica.y"
{helper.invocacionProc(val_peek(2).sval);}
break;
case 46:
//#line 100 "C:\Users\federico.iribarren\Documents\Diseño de Compiladores I\src\main\java\analizador_sintactico\archivos\gramatica.y"
{helper.invocacionProc(val_peek(3).sval);}
break;
case 47:
//#line 103 "C:\Users\federico.iribarren\Documents\Diseño de Compiladores I\src\main\java\analizador_sintactico\archivos\gramatica.y"
{helper.guardaParamsInvoc(val_peek(0).sval);}
break;
case 48:
//#line 104 "C:\Users\federico.iribarren\Documents\Diseño de Compiladores I\src\main\java\analizador_sintactico\archivos\gramatica.y"
{helper.guardaParamsInvoc(val_peek(0).sval);}
break;
case 49:
//#line 105 "C:\Users\federico.iribarren\Documents\Diseño de Compiladores I\src\main\java\analizador_sintactico\archivos\gramatica.y"
{helper.guardaParamsInvoc(val_peek(0).sval);}
break;
case 53:
//#line 112 "C:\Users\federico.iribarren\Documents\Diseño de Compiladores I\src\main\java\analizador_sintactico\archivos\gramatica.y"
{yyerror("Un procedimiento no puede tener mas de 3 parametros.");}
break;
case 54:
//#line 115 "C:\Users\federico.iribarren\Documents\Diseño de Compiladores I\src\main\java\analizador_sintactico\archivos\gramatica.y"
{helper.lecturaDestAsign(val_peek(2).sval);}
break;
case 55:
//#line 116 "C:\Users\federico.iribarren\Documents\Diseño de Compiladores I\src\main\java\analizador_sintactico\archivos\gramatica.y"
{helper.lecturaDestMenosIgual(val_peek(2).sval);}
break;
case 56:
//#line 117 "C:\Users\federico.iribarren\Documents\Diseño de Compiladores I\src\main\java\analizador_sintactico\archivos\gramatica.y"
{helper.lecturaDestMasIgual(val_peek(2).sval);}
break;
case 57:
//#line 118 "C:\Users\federico.iribarren\Documents\Diseño de Compiladores I\src\main\java\analizador_sintactico\archivos\gramatica.y"
{helper.lecturaDestMenosIgual(val_peek(2).sval);   yyerror("El lado derecho de la asignacion no es valido."); }
break;
case 58:
//#line 119 "C:\Users\federico.iribarren\Documents\Diseño de Compiladores I\src\main\java\analizador_sintactico\archivos\gramatica.y"
{helper.lecturaDestMasIgual(val_peek(2).sval);    yyerror("El lado derecho de la asignacion no es valido.");}
break;
case 59:
//#line 120 "C:\Users\federico.iribarren\Documents\Diseño de Compiladores I\src\main\java\analizador_sintactico\archivos\gramatica.y"
{
                            helper.lecturaDestAsign(val_peek(2).sval);
                            yyerror("El lado derecho de la asignacion no es valido.");
                            }
break;
case 60:
//#line 124 "C:\Users\federico.iribarren\Documents\Diseño de Compiladores I\src\main\java\analizador_sintactico\archivos\gramatica.y"
{
                    helper.lecturaDestAsign(val_peek(0).sval);
                    yyerror("Un identificador en solitario no es una sentencia valida.");
                    }
break;
case 61:
//#line 128 "C:\Users\federico.iribarren\Documents\Diseño de Compiladores I\src\main\java\analizador_sintactico\archivos\gramatica.y"
{yyerror("El lado izquierdo de la asignacion no es valido");}
break;
case 62:
//#line 132 "C:\Users\federico.iribarren\Documents\Diseño de Compiladores I\src\main\java\analizador_sintactico\archivos\gramatica.y"
{
                                      if (!TablaNotificaciones.hayErrores()){
                                          String[] ultimosPasos = helper.getUltimosPasos();
                                          String ultimoPaso = ultimosPasos[1];
                                          String anteultimoPaso = ultimosPasos[0];
                                          if(!helper.entradaOperador(ultimoPaso) && !helper.entradaOperador(anteultimoPaso)){
                                              if(helper.entradaCte(ultimoPaso) && helper.entradaCte(anteultimoPaso))
                                              {
                                                 if(helper.getTipoEntrada(ultimoPaso).equals(helper.getTipoEntrada(anteultimoPaso)))
                                                 {
                                                    if(helper.getTipoEntrada(ultimoPaso).equals("UINT"))
                                                    {

                                                        int ultimo = Integer.parseInt(ultimoPaso);
                                                        int anteultimo = Integer.parseInt(anteultimoPaso);
                                                        int calculo = ultimo + anteultimo;
                                                        helper.quitarUltimoPasoRepr();
                                                        helper.quitarUltimoPasoRepr();
                                                        helper.agregarPasosRepr(String.valueOf(calculo));
                                                        helper.agregarEntradaTS(new Celda(Parser.CTE_UINT,String.valueOf(calculo),Celda.TIPO_UINT,Celda.USO_CTE,true));
                                                    }
                                                    else
                                                    {
                                                        double ultimo = Double.parseDouble(ultimoPaso);
                                                        double anteultimo = Double.parseDouble(anteultimoPaso);
                                                        double calculo = ultimo + anteultimo;
                                                        helper.quitarUltimoPasoRepr();
                                                        helper.quitarUltimoPasoRepr();
                                                        helper.agregarPasosRepr(String.valueOf(calculo));
                                                        helper.agregarEntradaTS(new Celda(Parser.CTE_DOUBLE,String.valueOf(calculo),Celda.TIPO_DOUBLE,Celda.USO_CTE,true));
                                                    }
                                                  } else { yyerror("Los operandos son de diferentes tipos.");}
                                              }else {helper.agregarPasosRepr("+");}
                                              }else {helper.agregarPasosRepr("+");}
                                             }
                                      }
break;
case 63:
//#line 168 "C:\Users\federico.iribarren\Documents\Diseño de Compiladores I\src\main\java\analizador_sintactico\archivos\gramatica.y"
{
			                        if (!TablaNotificaciones.hayErrores()){
                                                      String[] ultimosPasos = helper.getUltimosPasos();
                                                      String ultimoPaso = ultimosPasos[1];
                                                      String anteultimoPaso = ultimosPasos[0];
                                                      if(!helper.entradaOperador(ultimoPaso) && !helper.entradaOperador(anteultimoPaso)){
                                                          if(helper.entradaCte(ultimoPaso) && helper.entradaCte(anteultimoPaso))
                                                          {
                                                             if(helper.getTipoEntrada(ultimoPaso).equals(helper.getTipoEntrada(anteultimoPaso)))
                                                             {
                                                                if(helper.getTipoEntrada(ultimoPaso).equals("UINT"))
                                                                {
                                                                    int ultimo = Integer.parseInt(ultimoPaso);
                                                                    int anteultimo = Integer.parseInt(anteultimoPaso);
                                                                    int calculo = anteultimo - ultimo;
                                                                    helper.quitarUltimoPasoRepr();
                                                                    helper.quitarUltimoPasoRepr();
                                                                    helper.agregarPasosRepr(String.valueOf(calculo));
                                                                    helper.agregarEntradaTS(new Celda(Parser.CTE_UINT,String.valueOf(calculo),Celda.TIPO_UINT,Celda.USO_CTE,true));
                                                                }
                                                                else
                                                                {
                                                                    double ultimo = Double.parseDouble(ultimoPaso);
                                                                    double anteultimo = Double.parseDouble(anteultimoPaso);
                                                                    double calculo = anteultimo - ultimo;
                                                                    helper.quitarUltimoPasoRepr();
                                                                    helper.quitarUltimoPasoRepr();
                                                                    helper.agregarPasosRepr(String.valueOf(calculo));
                                                                    helper.agregarEntradaTS(new Celda(Parser.CTE_DOUBLE,String.valueOf(calculo),Celda.TIPO_DOUBLE,Celda.USO_CTE,true));
                                                                }
                                                              } else { yyerror("Los operandos son de diferentes tipos.");}
                                                          }else {  helper.agregarPasosRepr("-");}
                                                      }else {  helper.agregarPasosRepr("-");}
                                                     }
                                    }
break;
case 65:
//#line 209 "C:\Users\federico.iribarren\Documents\Diseño de Compiladores I\src\main\java\analizador_sintactico\archivos\gramatica.y"
{
                                if (!TablaNotificaciones.hayErrores()){
                                                      String[] ultimosPasos = helper.getUltimosPasos();
                                                      String ultimoPaso = ultimosPasos[1];
                                                      String anteultimoPaso = ultimosPasos[0];
                                                      if(!helper.entradaOperador(ultimoPaso) && !helper.entradaOperador(anteultimoPaso)){
                                                          if(helper.entradaCte(ultimoPaso) && helper.entradaCte(anteultimoPaso))
                                                          {
                                                             if(helper.getTipoEntrada(ultimoPaso).equals(helper.getTipoEntrada(anteultimoPaso)))
                                                             {
                                                                if(helper.getTipoEntrada(ultimoPaso).equals("UINT"))
                                                                {

                                                                    int ultimo = Integer.parseInt(ultimoPaso);
                                                                    int anteultimo = Integer.parseInt(anteultimoPaso);
                                                                    int calculo = ultimo * anteultimo;
                                                                    helper.quitarUltimoPasoRepr();
                                                                    helper.quitarUltimoPasoRepr();
                                                                    helper.agregarPasosRepr(String.valueOf(calculo));
                                                                    helper.agregarEntradaTS(new Celda(Parser.CTE_UINT,String.valueOf(calculo),Celda.TIPO_UINT,Celda.USO_CTE,true));
                                                                }
                                                                else
                                                                {
                                                                    double ultimo = Double.parseDouble(ultimoPaso);
                                                                    double anteultimo = Double.parseDouble(anteultimoPaso);
                                                                    double calculo = ultimo * anteultimo;
                                                                    helper.quitarUltimoPasoRepr();
                                                                    helper.quitarUltimoPasoRepr();
                                                                    helper.agregarPasosRepr(String.valueOf(calculo));
                                                                    helper.agregarEntradaTS(new Celda(Parser.CTE_DOUBLE,String.valueOf(calculo),Celda.TIPO_DOUBLE,Celda.USO_CTE,true));
                                                                }
                                                              } else { yyerror("Los operandos son de diferentes tipos.");}
                                                          }else {  helper.agregarPasosRepr("*");}
                                                      } else {  helper.agregarPasosRepr("*");}
                                                     }

}
break;
case 66:
//#line 246 "C:\Users\federico.iribarren\Documents\Diseño de Compiladores I\src\main\java\analizador_sintactico\archivos\gramatica.y"
{
                                if (!TablaNotificaciones.hayErrores()){
                                                      String[] ultimosPasos = helper.getUltimosPasos();
                                                      String ultimoPaso = ultimosPasos[1];
                                                      String anteultimoPaso = ultimosPasos[0];
                                                      if(!helper.entradaOperador(ultimoPaso) && !helper.entradaOperador(anteultimoPaso)){
                                                          if(helper.entradaCte(ultimoPaso) && helper.entradaCte(anteultimoPaso))
                                                          {
                                                             if(helper.getTipoEntrada(ultimoPaso).equals(helper.getTipoEntrada(anteultimoPaso)))
                                                             {
                                                                if(helper.getTipoEntrada(ultimoPaso).equals("UINT"))
                                                                {

                                                                    int ultimo = Integer.parseInt(ultimoPaso);
                                                                    int anteultimo = Integer.parseInt(anteultimoPaso);
                                                                    if(ultimo > 0)
                                                                    {
                                                                        int calculo = anteultimo / ultimo;
                                                                        helper.quitarUltimoPasoRepr();
                                                                        helper.quitarUltimoPasoRepr();
                                                                        helper.agregarPasosRepr(String.valueOf(calculo));
                                                                        helper.agregarEntradaTS(new Celda(Parser.CTE_UINT,String.valueOf(calculo),Celda.TIPO_UINT,Celda.USO_CTE,true));
                                                                    }else yyerror("No se puede dividir por cero");
                                                                }
                                                                else
                                                                {
                                                                    double ultimo = Double.parseDouble(ultimoPaso);
                                                                    double anteultimo = Double.parseDouble(anteultimoPaso);
                                                                    if(ultimo > 0)
                                                                    {
                                                                        double calculo = anteultimo / ultimo;
                                                                        helper.quitarUltimoPasoRepr();
                                                                        helper.quitarUltimoPasoRepr();
                                                                        helper.agregarPasosRepr(String.valueOf(calculo));
                                                                        helper.agregarEntradaTS(new Celda(Parser.CTE_DOUBLE,String.valueOf(calculo),Celda.TIPO_DOUBLE,Celda.USO_CTE,true));
                                                                    }else yyerror("No se puede dividir por cero");
                                                                }
                                                              } else { yyerror("Los operandos son de diferentes tipos.");}
                                                          }else {  helper.agregarPasosRepr("/");}
                                                      }else {  helper.agregarPasosRepr("/");}
                                                     }
		                        }
break;
case 68:
//#line 291 "C:\Users\federico.iribarren\Documents\Diseño de Compiladores I\src\main\java\analizador_sintactico\archivos\gramatica.y"
{helper.lecturaFactor(val_peek(0).sval);}
break;
case 69:
//#line 292 "C:\Users\federico.iribarren\Documents\Diseño de Compiladores I\src\main\java\analizador_sintactico\archivos\gramatica.y"
{helper.agregarPasosRepr(val_peek(0).sval);helper.setTipoUltimoFactor("UINT");}
break;
case 70:
//#line 293 "C:\Users\federico.iribarren\Documents\Diseño de Compiladores I\src\main\java\analizador_sintactico\archivos\gramatica.y"
{helper.agregarPasosRepr(val_peek(0).sval);helper.setTipoUltimoFactor("DOUBLE");}
break;
case 71:
//#line 294 "C:\Users\federico.iribarren\Documents\Diseño de Compiladores I\src\main\java\analizador_sintactico\archivos\gramatica.y"
{helper.cambioSignoFactor(yylval.sval);}
break;
case 75:
//#line 302 "C:\Users\federico.iribarren\Documents\Diseño de Compiladores I\src\main\java\analizador_sintactico\archivos\gramatica.y"
{helper.agregarPasosRepr(val_peek(0).sval, "OUT_CAD");}
break;
case 76:
//#line 303 "C:\Users\federico.iribarren\Documents\Diseño de Compiladores I\src\main\java\analizador_sintactico\archivos\gramatica.y"
{helper.impresionFactor();}
break;
case 78:
//#line 309 "C:\Users\federico.iribarren\Documents\Diseño de Compiladores I\src\main\java\analizador_sintactico\archivos\gramatica.y"
{helper.puntoControlLoop();}
break;
case 80:
//#line 313 "C:\Users\federico.iribarren\Documents\Diseño de Compiladores I\src\main\java\analizador_sintactico\archivos\gramatica.y"
{yyerror("Falta el bloque de sentencias ejecutables del LOOP.");}
break;
case 83:
//#line 318 "C:\Users\federico.iribarren\Documents\Diseño de Compiladores I\src\main\java\analizador_sintactico\archivos\gramatica.y"
{yyerror("Bloque de sentencias vacio.");}
break;
case 84:
//#line 319 "C:\Users\federico.iribarren\Documents\Diseño de Compiladores I\src\main\java\analizador_sintactico\archivos\gramatica.y"
{yyerror("No se permiten sentencias declarativas dentro de un bloque de estructura de control.");}
break;
case 87:
//#line 325 "C:\Users\federico.iribarren\Documents\Diseño de Compiladores I\src\main\java\analizador_sintactico\archivos\gramatica.y"
{yyerror("No se permiten sentencias declarativas dentro de un bloque de estructura de control.");}
break;
case 88:
//#line 327 "C:\Users\federico.iribarren\Documents\Diseño de Compiladores I\src\main\java\analizador_sintactico\archivos\gramatica.y"
{yyerror("No se permiten sentencias declarativas dentro de un bloque de estructura de control.");}
break;
case 89:
//#line 330 "C:\Users\federico.iribarren\Documents\Diseño de Compiladores I\src\main\java\analizador_sintactico\archivos\gramatica.y"
{helper.puntoControlUntil();}
break;
case 90:
//#line 331 "C:\Users\federico.iribarren\Documents\Diseño de Compiladores I\src\main\java\analizador_sintactico\archivos\gramatica.y"
{yyerror("Falta la condicion de corte del LOOP.");}
break;
case 91:
//#line 334 "C:\Users\federico.iribarren\Documents\Diseño de Compiladores I\src\main\java\analizador_sintactico\archivos\gramatica.y"
{helper.agregarPasosRepr(val_peek(2).sval);}
break;
case 92:
//#line 335 "C:\Users\federico.iribarren\Documents\Diseño de Compiladores I\src\main\java\analizador_sintactico\archivos\gramatica.y"
{yyerror("Falta parentesis de cierre de la condicion.");}
break;
case 93:
//#line 336 "C:\Users\federico.iribarren\Documents\Diseño de Compiladores I\src\main\java\analizador_sintactico\archivos\gramatica.y"
{yyerror("Falta expresion en el lado izquierdo de la condicion.");}
break;
case 94:
//#line 337 "C:\Users\federico.iribarren\Documents\Diseño de Compiladores I\src\main\java\analizador_sintactico\archivos\gramatica.y"
{yyerror("Falta expresion en el lado derecho de la condicion.");}
break;
case 95:
//#line 338 "C:\Users\federico.iribarren\Documents\Diseño de Compiladores I\src\main\java\analizador_sintactico\archivos\gramatica.y"
{yyerror("Error en la condicion.");}
break;
case 104:
//#line 353 "C:\Users\federico.iribarren\Documents\Diseño de Compiladores I\src\main\java\analizador_sintactico\archivos\gramatica.y"
{helper.puntoControlThen();}
break;
case 105:
//#line 354 "C:\Users\federico.iribarren\Documents\Diseño de Compiladores I\src\main\java\analizador_sintactico\archivos\gramatica.y"
{yyerror("Falta la condicion del IF.");}
break;
case 106:
//#line 357 "C:\Users\federico.iribarren\Documents\Diseño de Compiladores I\src\main\java\analizador_sintactico\archivos\gramatica.y"
{helper.puntoControlElse();}
break;
case 107:
//#line 358 "C:\Users\federico.iribarren\Documents\Diseño de Compiladores I\src\main\java\analizador_sintactico\archivos\gramatica.y"
{yyerror("Falta el bloque de sentencias ejecutables de la rama THEN.");}
break;
case 108:
//#line 361 "C:\Users\federico.iribarren\Documents\Diseño de Compiladores I\src\main\java\analizador_sintactico\archivos\gramatica.y"
{helper.puntoControlFinCondicional();}
break;
case 109:
//#line 362 "C:\Users\federico.iribarren\Documents\Diseño de Compiladores I\src\main\java\analizador_sintactico\archivos\gramatica.y"
{yyerror("Falta el bloque de sentencias ejecutables de la rama THEN.");}
break;
case 110:
//#line 365 "C:\Users\federico.iribarren\Documents\Diseño de Compiladores I\src\main\java\analizador_sintactico\archivos\gramatica.y"
{helper.puntoControlFinCondicional();}
break;
case 111:
//#line 366 "C:\Users\federico.iribarren\Documents\Diseño de Compiladores I\src\main\java\analizador_sintactico\archivos\gramatica.y"
{yyerror("Falta el bloque de sentencias ejecutables de la rama ELSE.");}
break;
//#line 1110 "Parser.java"
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
