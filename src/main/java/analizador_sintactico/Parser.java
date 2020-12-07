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
import analizador_sintactico.util.InfoProc;
import generacion_c_intermedio.MultiPolaca;
import generacion_c_intermedio.PilaAmbitos;
import generacion_c_intermedio.Polaca;
import util.TablaNotificaciones;
import util.tabla_simbolos.Celda;
import util.tabla_simbolos.TablaSimbolos;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
//#line 35 "Parser.java"




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
   25,   25,   25,   26,   26,   26,   26,   19,   27,   28,
   28,   30,   30,   30,   30,   31,   31,   31,   31,   29,
   29,   32,   32,   32,   32,   32,   33,   33,   33,   33,
   33,   33,   20,   20,   34,   34,   35,   35,   37,   37,
   36,   36,   21,   21,   21,   38,   38,   38,   38,
};
final static short yylen[] = {                            2,
    1,    1,    1,    2,    3,    1,    1,    0,    1,    4,
    2,    1,    3,    2,    1,    3,    2,    2,    1,    1,
    3,    5,    7,    0,    1,    1,    1,    3,    2,    2,
    2,    1,    3,    2,    2,    2,    1,    3,    2,    1,
    1,    1,    1,    1,    3,    4,    1,    1,    1,    1,
    3,    5,    7,    3,    3,    1,    3,    3,    3,    1,
    3,    3,    1,    1,    1,    1,    2,    3,    1,    1,
    0,    2,    3,    2,    2,    2,    3,    2,    3,    2,
    1,    5,    4,    4,    4,    3,    1,    1,    1,    1,
    1,    1,    4,    3,    2,    1,    2,    1,    2,    1,
    2,    1,    4,    3,    4,    1,    1,    1,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    2,    3,    0,   69,    0,    0,    0,    1,
    0,    0,    6,    7,    0,   40,   41,   42,   43,   44,
    0,    0,    0,    0,    0,    0,   95,    0,   14,    0,
   11,    9,    0,    0,    0,    0,    0,    0,    0,   70,
    0,    0,    0,   64,   65,   66,    0,    0,    0,   63,
   47,   48,   49,   45,    0,    0,   55,    0,    0,   88,
   87,   92,   91,   89,   90,    0,    0,    0,  109,  106,
  107,  108,    0,    0,    5,    0,   17,    0,    0,    0,
   26,   27,   37,    0,    0,    0,   74,    0,    0,    0,
   75,   72,    0,   68,    0,    0,    0,   94,   67,    0,
    0,    0,    0,   46,   25,    0,   86,    0,    0,  105,
  103,   13,   29,    0,   31,   16,    0,   36,    0,   35,
    0,   10,    0,    0,   73,   80,  101,   93,    0,    0,
   61,   62,    0,   85,    0,   84,   28,    0,   33,   39,
    0,   79,   77,    0,   82,    0,   38,    0,    0,    0,
    0,   53,   23,
};
final static short yydgoto[] = {                          9,
   10,   11,   12,   33,   13,   14,   15,   35,   86,  122,
   31,   79,   80,  106,   81,   82,   16,   17,   18,   19,
   20,   55,   56,   48,   49,   50,   21,   39,   94,   40,
   90,   27,   67,   22,   42,   97,   43,   73,
};
final static short yysindex[] = {                       202,
  -36,  -14,    0,    0,  -11,    0,   -1, -214,    0,    0,
 -202,   13,    0,    0,   39,    0,    0,    0,    0,    0,
 -109, -181,  -26,  -37,  147,  307,    0, -144,    0,   43,
    0,    0,  202,  -33,  -21,  -98,   13,   13, -176,    0,
 -109, -168, -166,    0,    0,    0,  -26,   33,   51,    0,
    0,    0,    0,    0,   63,   70,    0,   33,   80,    0,
    0,    0,    0,    0,    0,  313,  -26,   84,    0,    0,
    0,    0,   89, -202,    0, -225,    0, -124,  111,   70,
    0,    0,    0,  -56, -125,   32,    0,   13,   13,   45,
    0,    0,  -11,    0,    0, -109, -111,    0,    0,  -26,
  -26,  -26,  -26,    0,    0, -186,    0,  -39,   99,    0,
    0,    0,    0,  -96,    0,    0, -228,    0, -103,    0,
  -75,    0,  202,  202,    0,    0,    0,    0,   51,   51,
    0,    0,   70,    0,  100,    0,    0,   70,    0,    0,
   50,    0,    0, -186,    0, -228,    0,   70,   70, -186,
 -228,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,   75,    0,    0,  -90,    0,    0,  137,    0,    0,
    0,  191,    0,    0,    0,    0,    0,    0,    0,    0,
  -92,    0,    0,    0,    0,    0,    0,    0,    0,   92,
    0,    0,   10,   -9,    0,    0, -219, -219,    0,    0,
 -199,    0,    0,    0,    0,    0,    0,  109,    1,    0,
    0,    0,    0,    0,    0,  -34,    0,  127,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  149,    0,    0,    0,    0,  -41,   61,  214,
    0,    0,    0,    0,    0,    0,    0,  -58,  -58,    0,
    0,    0,  169,    0, -171,  -89,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  278,    0,    0,    0,    0,   57,    0,
    0,    0,   59,   60,    0,    0,    0,    0,   21,   41,
    0,    0,  -32,    0,   58,    0,    0,  235,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  -30,  244,    0,
    0,    0,    0,
};
final static short yygindex[] = {                         0,
  -16,  -22,    0,   19,   -8,   -5,    0,    0,    0,    0,
  115,   42,  -87,  -10,    0,    0,    0,    0,    0,    0,
    0,   52,  -91,   -2,   36,  -29,    0,    0,    0,  -19,
  -13,  102,  135,    0,    0,    0,    0,    0,
};
final static int YYTABLESIZE=583;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         32,
   60,  134,   32,   54,  119,   47,   50,   77,   51,    4,
   52,   78,   37,   36,  133,   38,   75,   99,   47,   32,
   58,   95,   58,   66,   23,   24,   87,   88,   26,  138,
   89,  113,   37,    3,    4,   38,    3,    4,   28,   85,
   59,   60,   29,   60,   76,   60,   25,    8,    8,  140,
    8,   19,  148,  114,   30,   91,   92,   83,  149,   60,
   60,   58,   60,   58,  109,   58,    8,   98,  100,  117,
   51,   32,  131,  132,   56,  100,  127,  101,   34,   58,
   58,   59,   58,   59,   41,   59,   74,   37,   52,   53,
   38,   12,  102,   93,   78,   97,   99,  103,   96,   59,
   59,   98,   59,  104,  141,  135,  123,  124,   57,  142,
  143,   68,   69,  105,   88,   88,   83,   89,   89,   70,
  107,   18,  144,   78,  110,   60,   54,  146,   78,  111,
   71,   72,  115,   56,    4,  129,  130,  150,  151,  136,
  145,  100,  100,  101,  101,   58,    1,    2,  104,  120,
   12,  116,    3,    4,  121,    5,  128,    1,    2,    6,
  137,    7,    8,    3,    4,   59,    5,   57,   81,  125,
    6,  139,    7,    8,  147,   96,   15,   71,  102,   34,
    1,    2,   83,   78,   76,   54,    3,    4,  112,    5,
    8,   47,  153,    6,  126,    7,    8,    8,    8,   56,
  108,  152,    0,    8,    8,    0,    8,  104,    0,    0,
    8,    0,    8,    8,   32,    0,   12,   44,  118,   51,
   32,   32,   24,    0,   24,    0,   24,   81,    3,    4,
   44,   32,   32,   57,   83,   45,   46,   52,   53,   76,
   24,   24,   24,   24,   24,   24,   19,    0,   45,   46,
    0,   54,   84,    0,   20,    0,   60,   60,   60,   60,
   60,   60,   60,   60,   19,   60,   60,   60,   60,   60,
   60,   60,   60,  104,   20,   21,   58,   58,   58,   58,
   58,   58,   58,   58,   22,   58,   58,   58,   58,   58,
   58,   58,   58,   81,    0,   21,   59,   59,   59,   59,
   59,   59,   59,   59,   22,   59,   59,   59,   59,   59,
   59,   59,   59,   83,   83,    8,   18,    0,   30,   83,
   83,   30,   83,   83,   83,   83,   83,   83,   83,   83,
   56,   56,    0,    0,   18,    0,   56,   56,   30,   56,
    0,   56,   56,   56,   56,   56,   56,   12,   12,    0,
    0,   47,    0,   12,   12,  100,   12,  101,   12,   12,
   12,   12,   12,   12,   57,   57,   64,    0,   65,    0,
   57,   57,   64,   57,   65,   57,   57,   57,   57,   57,
   57,    0,   54,   54,    0,    0,    0,    0,   54,   54,
    0,   54,    0,   54,   54,   54,   54,   54,   54,    0,
    0,    0,   57,   44,  104,  104,    0,    0,    0,    0,
  104,  104,    0,  104,    0,  104,  104,  104,  104,  104,
  104,   45,   46,    0,   81,   81,    0,    0,    0,    0,
   81,   81,    0,   81,    0,   81,   81,   81,   81,   81,
   81,    0,    0,    0,    0,    0,    8,    8,    0,    0,
    0,    0,    8,    8,    0,    8,    0,    1,    2,    8,
    0,    8,    8,    3,    4,    0,    5,    0,    0,   20,
    6,    0,    7,    8,    0,   24,   24,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   24,   20,    0,    0,
   21,    0,    0,    0,    0,    0,   24,   24,    0,   22,
    0,    0,    0,    0,    0,   24,   24,   24,   21,    0,
    0,    0,    0,    0,    0,    0,   24,   22,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
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
   41,   34,   21,  123,  106,   21,   33,   47,   45,   61,
    0,   41,   25,   26,   61,   40,  125,   36,   40,  117,
   36,  257,   41,  262,  263,   41,  262,  263,   40,   61,
    0,   41,  257,   43,  273,   45,   61,  267,  268,  125,
  270,   61,  144,   76,  257,   37,   38,    0,  146,   59,
   60,   41,   62,   43,   67,   45,  125,  267,  268,   80,
  257,   59,  102,  103,    0,   43,   96,   45,   40,   59,
   60,   41,   62,   43,  266,   45,   44,   96,  275,  276,
   96,    0,   42,  270,  117,  267,  268,   47,  267,   59,
   60,  268,   62,   41,  121,  108,   88,   89,    0,  123,
  124,  256,  257,   44,  123,  124,   59,  123,  124,  264,
   41,   61,  133,  146,   41,  125,    0,  138,  151,   41,
  275,  276,  257,   59,  125,  100,  101,  148,  149,   41,
   41,   43,   43,   45,   45,  125,  256,  257,    0,  275,
   59,   41,  262,  263,  123,  265,  268,  256,  257,  269,
  257,  271,  272,  262,  263,  125,  265,   59,    0,  125,
  269,  275,  271,  272,  125,  266,   40,  270,  268,  123,
  256,  257,  125,  125,  125,   59,  262,  263,   74,  265,
    0,   45,  151,  269,   93,  271,  272,  256,  257,  125,
   66,  150,   -1,  262,  263,   -1,  265,   59,   -1,   -1,
  269,   -1,  271,  272,  256,   -1,  125,  257,  275,  257,
  262,  263,  257,   -1,  257,   -1,  257,   59,  262,  263,
  257,  273,  274,  125,  256,  275,  276,  275,  276,  273,
  275,  276,  275,  276,  275,  276,  256,   -1,  275,  276,
   -1,  125,  274,   -1,   41,   -1,  256,  257,  258,  259,
  260,  261,  262,  263,  274,  265,  266,  267,  268,  269,
  270,  271,  272,  125,   61,   41,  256,  257,  258,  259,
  260,  261,  262,  263,   41,  265,  266,  267,  268,  269,
  270,  271,  272,  125,   -1,   61,  256,  257,  258,  259,
  260,  261,  262,  263,   61,  265,  266,  267,  268,  269,
  270,  271,  272,  256,  257,  125,  256,   -1,   41,  262,
  263,   44,  265,  266,  267,  268,  269,  270,  271,  272,
  256,  257,   -1,   -1,  274,   -1,  262,  263,   61,  265,
   -1,  267,  268,  269,  270,  271,  272,  256,  257,   -1,
   -1,   45,   -1,  262,  263,   43,  265,   45,  267,  268,
  269,  270,  271,  272,  256,  257,   60,   -1,   62,   -1,
  262,  263,   60,  265,   62,  267,  268,  269,  270,  271,
  272,   -1,  256,  257,   -1,   -1,   -1,   -1,  262,  263,
   -1,  265,   -1,  267,  268,  269,  270,  271,  272,   -1,
   -1,   -1,  256,  257,  256,  257,   -1,   -1,   -1,   -1,
  262,  263,   -1,  265,   -1,  267,  268,  269,  270,  271,
  272,  275,  276,   -1,  256,  257,   -1,   -1,   -1,   -1,
  262,  263,   -1,  265,   -1,  267,  268,  269,  270,  271,
  272,   -1,   -1,   -1,   -1,   -1,  256,  257,   -1,   -1,
   -1,   -1,  262,  263,   -1,  265,   -1,  256,  257,  269,
   -1,  271,  272,  262,  263,   -1,  265,   -1,   -1,  256,
  269,   -1,  271,  272,   -1,  262,  263,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  273,  274,   -1,   -1,
  256,   -1,   -1,   -1,   -1,   -1,  262,  263,   -1,  256,
   -1,   -1,   -1,   -1,   -1,  262,  263,  273,  274,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  273,  274,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
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
"print : OUT '(' imprimible ')'",
"print : OUT '(' imprimible",
"print : OUT '(' error ')'",
"imprimible : CADENA",
"imprimible : CTE_UINT",
"imprimible : CTE_DOUBLE",
"imprimible : ID",
};

//#line 225 "archivos/gramatica.y"

    private final ParserHelper helper;

    private final AnalizadorLexico aLexico;
    private final TablaSimbolos tablaS;
    private final PilaAmbitos pilaAmbitos;
    private final Polaca polacaProgram;
    private final MultiPolaca polacaProcedimientos;

    public Parser(AnalizadorLexico aLexico, TablaSimbolos tablaS) {
        this.aLexico = aLexico;
        this.tablaS = tablaS;
        this.pilaAmbitos = new PilaAmbitos();
        this.polacaProgram = new Polaca();
        this.polacaProcedimientos = new MultiPolaca();

        helper = new ParserHelper(aLexico, tablaS, pilaAmbitos, polacaProgram, polacaProcedimientos);
    }

    private int yylex() {
        int token = aLexico.produceToken();
        yylval = new ParserVal(aLexico.ultimoLexemaGenerado);
        return token;
    }

    private void yyerror(String mensajeError) {
        TablaNotificaciones.agregarError(aLexico.getLineaActual(), mensajeError);
    }

    private void checkCambioSigno() {
        String lexemaSignoNoC = yylval.sval; //Obtengo el lexema del factor.
        Celda celdaOriginal = tablaS.getEntrada(lexemaSignoNoC); //La sentencia va aca si o si, porque mas adelante ya no existe la entrada en la TS.

        if (celdaOriginal.getTipo().equals("DOUBLE")) {
            tablaS.quitarReferencia(lexemaSignoNoC); //El lexema esta en la TS si o si. refs--.
            if (tablaS.entradaSinReferencias(lexemaSignoNoC)) tablaS.eliminarEntrada(lexemaSignoNoC);

            String lexemaSignoC = String.valueOf(Double.parseDouble(lexemaSignoNoC) * -1); //Cambio el signo del factor.

            quitarUltimoPasoRepr(); //Saco el factor que quedo con signo incorrecto.
            agregarPasosRepr(lexemaSignoC); //Agrego el factor con el signo que le corresponde.

            tablaS.agregarEntrada(celdaOriginal.getToken(), lexemaSignoC, celdaOriginal.getTipo());
            tablaS.setUsoEntrada(lexemaSignoC, "CTE");
        } else TablaNotificaciones.agregarError(aLexico.getLineaActual(), "No se permiten UINT negativos");
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

  private void checkValidezAsign(String lexema) {
    String ambito = getAmbitoId(lexema);

    if (ambito.isEmpty()) { //La TS no contiene el lexema recibido en ningun ambito.
      TablaNotificaciones.agregarError(aLexico.getLineaActual(), "El identificador '" + lexema + "' no esta declarado.");
      return; //Es necesario cortar aca para que 'ambito' no cause problemas por estar vacio.
    }

    String nLexema = ambito + "@" + lexema;
    if (!tablaS.isEntradaDeclarada(nLexema)) //Existe el lexema en la TS y tiene el flag de declaracion desactivado.
      TablaNotificaciones.agregarError(aLexico.getLineaActual(), "El identificador '" + lexema + "' no esta declarado.");

    if (tablaS.isEntradaProc(nLexema)) //Esta declarado pero es un procedimiento.
      TablaNotificaciones.agregarError(aLexico.getLineaActual(), "Un procedimiento no puede estar a la izquierda una asignacion.");

    agregarPasosRepr(nLexema, "=");
  }

  private void checkValidezFactor(String lexema) {
    String ambito = getAmbitoId(lexema);

    if (ambito.isEmpty()) { //La TS no contiene el lexema recibido en ningun ambito.
      TablaNotificaciones.agregarError(aLexico.getLineaActual(), "El identificador '" + lexema + "' no esta declarado.");
      return; //Es necesario cortar aca para que 'ambito' no cause problemas por estar vacio.
    }

    String nLexema = ambito + "@" + lexema;
    if (!tablaS.isEntradaDeclarada(nLexema)) //Existe el lexema en la TS y tiene el flag de declaracion desactivado.
      TablaNotificaciones.agregarError(aLexico.getLineaActual(), "El identificador '" + lexema + "' no esta declarado.");

    agregarPasosRepr(nLexema);
  }

  //---OUT---

  private String tipoImpresion; //Almacena temporalmente el tipo de dato que debe imprimirse.

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
//#line 636 "Parser.java"
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
//#line 29 "archivos/gramatica.y"
{helper.setUltimoTipoLeido("UINT");}
break;
case 3:
//#line 30 "archivos/gramatica.y"
{helper.setUltimoTipoLeido("DOUBLE");}
break;
case 8:
//#line 41 "archivos/gramatica.y"
{TablaNotificaciones.agregarError(aLexico.getLineaActual()-1,"Falta ';' al final de la sentencia.");}
break;
case 10:
//#line 45 "archivos/gramatica.y"
{helper.eliminarUltimoAmbito();}
break;
case 12:
//#line 49 "archivos/gramatica.y"
{helper.declaracionVar(val_peek(0).sval);}
break;
case 13:
//#line 50 "archivos/gramatica.y"
{helper.declaracionVar(val_peek(2).sval);}
break;
case 14:
//#line 53 "archivos/gramatica.y"
{helper.lecturaIdProc(val_peek(0).sval);}
break;
case 15:
//#line 54 "archivos/gramatica.y"
{yyerror("Falta el identificador del procedimiento.");}
break;
case 18:
//#line 59 "archivos/gramatica.y"
{yyerror("Falta el parentesis de cierre para los parametros.");}
break;
case 19:
//#line 60 "archivos/gramatica.y"
{yyerror("Falta el parentesis de cierre para los parametros.");}
break;
case 23:
//#line 66 "archivos/gramatica.y"
{yyerror("Un procedimiento no puede tener mas de 3 parametros.");}
break;
case 24:
//#line 69 "archivos/gramatica.y"
{yyerror("Falta una ',' para separar dos parametros.");}
break;
case 28:
//#line 77 "archivos/gramatica.y"
{helper.lecturaParamFormal(val_peek(0).sval, Celda.USO_PARAM_CVR);}
break;
case 29:
//#line 78 "archivos/gramatica.y"
{yyerror("Falta el tipo de un parametro.");}
break;
case 30:
//#line 79 "archivos/gramatica.y"
{yyerror("Falta el identificador de un parametro.");}
break;
case 31:
//#line 82 "archivos/gramatica.y"
{helper.lecturaParamFormal(val_peek(0).sval, Celda.USO_PARAM_CV);}
break;
case 32:
//#line 83 "archivos/gramatica.y"
{yyerror("Falta el identificador de un parametro.");}
break;
case 33:
//#line 86 "archivos/gramatica.y"
{helper.lecturaNumInvoc(Integer.parseInt(val_peek(0).sval), false, "");}
break;
case 34:
//#line 87 "archivos/gramatica.y"
{helper.lecturaNumInvoc(0, true, "Falta el numero de invocaciones del procedimiento.");}
break;
case 35:
//#line 88 "archivos/gramatica.y"
{helper.lecturaNumInvoc(0, true, "Falta la palabra clave 'NI' en el encabezado del procedimiento.");}
break;
case 36:
//#line 89 "archivos/gramatica.y"
{helper.lecturaNumInvoc(0, true, "Formato de declaracion de NI invalido. El formato correcto es 'NI = CTE_UINT'.");}
break;
case 37:
//#line 90 "archivos/gramatica.y"
{helper.lecturaNumInvoc(0, true, "Formato de declaracion de NI invalido. El formato correcto es 'NI = CTE_UINT'.");}
break;
case 39:
//#line 94 "archivos/gramatica.y"
{yyerror("Cuerpo del procedimiento vacio.");}
break;
case 45:
//#line 104 "archivos/gramatica.y"
{helper.invocacionProc(val_peek(2).sval);}
break;
case 46:
//#line 105 "archivos/gramatica.y"
{helper.invocacionProc(val_peek(3).sval);}
break;
case 47:
//#line 108 "archivos/gramatica.y"
{helper.guardaParamsInvoc(val_peek(0).sval);}
break;
case 48:
//#line 109 "archivos/gramatica.y"
{helper.guardaParamsInvoc(val_peek(0).sval);}
break;
case 49:
//#line 110 "archivos/gramatica.y"
{helper.guardaParamsInvoc(val_peek(0).sval);}
break;
case 50:
//#line 113 "archivos/gramatica.y"
{}
break;
case 51:
//#line 114 "archivos/gramatica.y"
{}
break;
case 52:
//#line 115 "archivos/gramatica.y"
{}
break;
case 53:
//#line 117 "archivos/gramatica.y"
{
                                                    
                                                    yyerror("Un procedimiento no puede tener mas de 3 parametros.");
                                                    }
break;
case 54:
//#line 123 "archivos/gramatica.y"
{checkValidezAsign(val_peek(2).sval);}
break;
case 55:
//#line 124 "archivos/gramatica.y"
{
                            checkValidezAsign(val_peek(2).sval);
                            yyerror("El lado derecho de la asignacio no es valido.");
                            }
break;
case 56:
//#line 128 "archivos/gramatica.y"
{
                    checkValidezAsign(val_peek(0).sval);
                    yyerror("Un identificador en solitario no es una sentencia valida.");
                    }
break;
case 57:
//#line 132 "archivos/gramatica.y"
{yyerror("El lado izquierdo de la asignacion no es valido");}
break;
case 58:
//#line 136 "archivos/gramatica.y"
{agregarPasosRepr("+");}
break;
case 59:
//#line 137 "archivos/gramatica.y"
{agregarPasosRepr("-");}
break;
case 61:
//#line 141 "archivos/gramatica.y"
{agregarPasosRepr("*");}
break;
case 62:
//#line 142 "archivos/gramatica.y"
{agregarPasosRepr("/");}
break;
case 64:
//#line 146 "archivos/gramatica.y"
{checkValidezFactor(val_peek(0).sval);}
break;
case 65:
//#line 147 "archivos/gramatica.y"
{agregarPasosRepr(val_peek(0).sval);}
break;
case 66:
//#line 148 "archivos/gramatica.y"
{agregarPasosRepr(val_peek(0).sval);}
break;
case 67:
//#line 149 "archivos/gramatica.y"
{checkCambioSigno();}
break;
case 69:
//#line 155 "archivos/gramatica.y"
{puntoControlLoop();}
break;
case 71:
//#line 159 "archivos/gramatica.y"
{yyerror("Falta el bloque de sentencias ejecutables del LOOP.");}
break;
case 74:
//#line 164 "archivos/gramatica.y"
{yyerror("Bloque de sentencias vacio.");}
break;
case 75:
//#line 165 "archivos/gramatica.y"
{yyerror("No se permiten sentencias declarativas dentro de un bloque de estructura de control.");}
break;
case 78:
//#line 170 "archivos/gramatica.y"
{yyerror("No se permiten sentencias declarativas dentro de un bloque de estructura de control.");}
break;
case 79:
//#line 171 "archivos/gramatica.y"
{yyerror("No se permiten sentencias declarativas dentro de un bloque de estructura de control.");}
break;
case 80:
//#line 174 "archivos/gramatica.y"
{puntoControlUntil();}
break;
case 81:
//#line 175 "archivos/gramatica.y"
{yyerror("Falta la condicion de corte del LOOP.");}
break;
case 82:
//#line 178 "archivos/gramatica.y"
{agregarPasosRepr(val_peek(2).sval);}
break;
case 83:
//#line 179 "archivos/gramatica.y"
{yyerror("Falta parentesis de cierre de la condicion.");}
break;
case 84:
//#line 180 "archivos/gramatica.y"
{yyerror("Falta expresion en el lado izquierdo de la condicion.");}
break;
case 85:
//#line 181 "archivos/gramatica.y"
{yyerror("Falta expresion en el lado derecho de la condicion.");}
break;
case 86:
//#line 182 "archivos/gramatica.y"
{yyerror("Error en la condicion.");}
break;
case 95:
//#line 197 "archivos/gramatica.y"
{puntoControlThen();}
break;
case 96:
//#line 198 "archivos/gramatica.y"
{yyerror("Falta la condicion del IF.");}
break;
case 97:
//#line 201 "archivos/gramatica.y"
{puntoControlElse();}
break;
case 98:
//#line 202 "archivos/gramatica.y"
{yyerror("Falta el bloque de sentencias ejecutables de la rama THEN.");}
break;
case 99:
//#line 205 "archivos/gramatica.y"
{puntoControlFinCondicional();}
break;
case 100:
//#line 206 "archivos/gramatica.y"
{yyerror("Falta el bloque de sentencias ejecutables de la rama THEN.");}
break;
case 101:
//#line 209 "archivos/gramatica.y"
{puntoControlFinCondicional();}
break;
case 102:
//#line 210 "archivos/gramatica.y"
{yyerror("Falta el bloque de sentencias ejecutables de la rama ELSE.");}
break;
case 106:
//#line 218 "archivos/gramatica.y"
{}
break;
case 107:
//#line 219 "archivos/gramatica.y"
{}
break;
case 108:
//#line 221 "archivos/gramatica.y"
{}
break;
case 109:
//#line 222 "archivos/gramatica.y"
{}
break;
//#line 1070 "Parser.java"
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
