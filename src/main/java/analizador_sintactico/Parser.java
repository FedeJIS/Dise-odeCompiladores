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
import generacion_c_intermedio.MultiPolaca;
import generacion_c_intermedio.PilaAmbitos;
import generacion_c_intermedio.Polaca;
import util.TablaNotificaciones;
import util.tabla_simbolos.Celda;
import util.tabla_simbolos.TablaSimbolos;

import java.util.ArrayList;
import java.util.List;
//#line 31 "Parser.java"




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
    6,    6,    6,    6,   17,   17,   22,   22,   22,   22,
   18,   18,   18,   23,   23,   23,   24,   24,   24,   25,
   25,   25,   25,   19,   26,   27,   27,   29,   29,   29,
   29,   30,   30,   30,   30,   28,   28,   31,   31,   31,
   31,   31,   32,   32,   32,   32,   32,   32,   20,   20,
   36,   36,   33,   33,   34,   34,   37,   37,   35,   35,
   21,   21,   21,   38,   38,   38,   38,
};
final static short yylen[] = {                            2,
    1,    1,    1,    2,    3,    1,    1,    0,    1,    4,
    2,    1,    3,    2,    1,    3,    2,    2,    1,    1,
    3,    5,    7,    0,    1,    1,    1,    3,    2,    2,
    2,    1,    3,    2,    2,    2,    1,    3,    2,    1,
    1,    1,    1,    1,    3,    4,    1,    3,    5,    7,
    3,    3,    1,    3,    3,    1,    3,    3,    1,    1,
    1,    1,    2,    3,    1,    1,    0,    2,    3,    2,
    2,    2,    3,    2,    3,    2,    1,    5,    4,    4,
    4,    3,    1,    1,    1,    1,    1,    1,    4,    3,
    1,    0,    2,    1,    2,    1,    2,    1,    2,    1,
    4,    3,    4,    1,    1,    1,    1,
};
final static short yydefred[] = {                         0,
    0,    2,    3,    0,   65,    0,    0,    0,    1,    0,
    0,    6,    7,    0,   40,   41,   42,   43,   44,    0,
    0,    0,    0,    0,   93,    0,   14,    0,   11,    9,
    0,    0,    0,    0,    0,    0,    0,   66,    0,    0,
    0,    0,   45,    0,   52,   60,   61,   62,    0,    0,
    0,   59,    0,   84,   83,   88,   87,   85,   86,    0,
    0,    0,  107,  104,  105,  106,    0,    0,    5,    0,
   17,    0,    0,    0,   26,   27,   37,    0,    0,    0,
   70,    0,    0,    0,   71,   68,    0,   64,    0,    0,
    0,   91,   90,   25,    0,   46,   63,    0,    0,    0,
    0,   82,    0,    0,  103,  101,   13,   29,    0,   31,
   16,    0,   36,    0,   35,    0,   10,    0,    0,   69,
   76,   99,   89,    0,    0,    0,   57,   58,   81,    0,
   80,   28,    0,   33,   39,    0,   75,   73,    0,   78,
    0,   38,    0,    0,    0,    0,   50,   23,
};
final static short yydgoto[] = {                          8,
    9,   10,   11,   31,   12,   13,   14,   33,   80,  117,
   29,   73,   74,   95,   75,   76,   15,   16,   17,   18,
   19,   44,   50,   51,   52,   20,   37,   88,   38,   84,
   25,   61,   21,   40,   91,   93,   41,   67,
};
final static short yysindex[] = {                      -118,
   -3,    0,    0,  -10,    0,    8, -231,    0,    0, -198,
   12,    0,    0,   25,    0,    0,    0,    0,    0,  -95,
 -171,  -39,  144,  354,    0, -146,    0,   54,    0,    0,
 -118,  -21,  -29,  -69,   12,   12, -162,    0,  -95, -168,
 -164,   65,    0,   74,    0,    0,    0,    0,  -26,   29,
   -7,    0,   78,    0,    0,    0,    0,    0,    0,  381,
  -26,   79,    0,    0,    0,    0,   82, -198,    0, -224,
    0, -140,   83,   65,    0,    0,    0,  -58, -150,   10,
    0,   12,   12,   11,    0,    0,  -10,    0,    0,  -95,
 -164,    0,    0,    0, -119,    0,    0,  -26,  -26,  -26,
  -26,    0,  -41,    2,    0,    0,    0,    0, -116,    0,
    0, -166,    0, -133,    0,  262,    0, -118, -118,    0,
    0,    0,    0,   65,   -7,   -7,    0,    0,    0,   49,
    0,    0,   65,    0,    0,   18,    0,    0, -108,    0,
 -166,    0,   65,   65, -107, -166,    0,    0,
};
final static short yyrindex[] = {                         0,
   73,    0,    0, -111,    0,    0,  116,    0,    0,    0,
  206,    0,    0,    0,    0,    0,    0,    0,    0, -113,
    0,    0,    0,    0,    0,    0,    0,   89,    0,    0,
    6,   76,    0,    0,  190,  190,    0,    0,  112,    0,
  238,  -36,    0,    0,    0,    0,    0,    0,    0,  105,
    1,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  121,    0,    0,    0,
    0,  -34,   91,  255,    0,    0,    0,    0,    0,    0,
    0,  310,  310,    0,    0,    0,  140,    0,  160,  128,
  238,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  -30,    0,
    0,    0,    0,   35,    0,    0,    0,   34,   36,    0,
    0,    0,    0,  -33,   21,   41,    0,    0,    0,   57,
    0,    0,  276,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  -32,  292,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  -14,  -19,    0,  -13,   -5,   16,    0,    0,    0,    0,
   95,   19,  -87,  -56,    0,    0,    0,    0,    0,    0,
    0,   24,  -12,  -46,  -25,    0,    0,    0,  -23,  -40,
   85,  113,    0,    0,    0,   84,    0,    0,
};
final static int YYTABLESIZE=642;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                        129,
   56,   43,  114,   49,   47,    4,   32,   48,   49,   32,
   30,   60,   72,   30,   35,   89,   69,  112,   49,   71,
   54,   85,   86,   97,  133,   27,   32,   34,   82,   24,
   30,   79,  108,   35,  100,   36,   22,    2,    3,  101,
   55,   56,  131,   56,   98,   56,   99,   26,  104,   83,
  109,  125,  126,  144,   36,   81,   79,   23,   28,   56,
   56,   54,   56,   54,   32,   54,  122,  139,  118,  119,
   30,   98,   53,   99,  127,  128,  141,  137,  138,   54,
   54,   55,   54,   55,   35,   55,  145,  146,   12,  140,
  130,   98,   72,   99,   39,    2,    3,   68,   90,   55,
   55,  136,   55,   92,   51,   36,   70,   87,   94,   62,
   63,   98,   82,   82,   96,   79,  110,   64,  102,  105,
  102,   72,  106,  111,  115,   56,   72,  100,   65,   66,
    4,   53,  116,   83,   83,  120,   19,  124,    1,   77,
  132,  134,  142,    2,    3,   54,    4,   12,  143,   42,
    5,   18,    6,    7,   94,   15,   67,   34,   74,   97,
   72,    1,  107,   51,  148,   55,    2,    3,  147,    4,
   98,  121,  103,    5,  123,    6,    7,    0,    0,  102,
    0,   79,    0,    0,    0,    0,  100,    1,   49,    8,
    0,    0,    2,    3,    0,    4,    0,   53,   77,    5,
    0,    6,    7,    0,    0,    8,    0,    0,    0,    0,
    0,    0,    0,   12,    0,   46,  113,   42,   97,    0,
   24,   32,    0,   24,   24,   30,   77,   32,   32,   51,
   46,   30,   30,   47,   48,    0,   98,   92,   32,   32,
    2,    3,   30,   30,   78,  102,    0,    0,   47,   48,
    0,   70,  100,    0,    0,    0,    0,   56,   56,   56,
   56,   56,   56,   56,   77,   56,   56,   56,   56,   56,
   56,   56,   56,    0,    0,    0,    0,   54,   54,   54,
   54,   54,   54,   54,   97,   54,   54,   54,   54,   54,
   54,   54,   54,    0,    0,   20,   92,   55,   55,   55,
   55,   55,   55,   55,    0,   55,   55,   55,   55,   55,
   55,   55,   55,   79,    8,   20,   21,    0,   79,   79,
    0,   79,   79,   79,   79,   79,   79,   79,   79,   53,
    8,   19,   22,    0,   53,   53,   21,   53,    0,   53,
   53,   53,   53,   53,   53,   12,   18,    0,    0,   19,
   12,   12,   22,   12,    0,   12,   12,   12,   12,   12,
   12,   51,   92,    0,   18,    0,   51,   51,    0,   51,
    0,   51,   51,   51,   51,   51,   51,  102,   96,   98,
    0,   98,  102,  102,    0,  102,  135,  102,  102,  102,
  102,  102,  102,    0,  100,  100,   77,  100,   49,   45,
   46,   77,   77,    0,   77,    0,   77,   77,   77,   77,
   77,   77,    0,   58,    0,   59,   97,    0,   47,   48,
    0,   97,   97,   98,   97,   99,   95,   97,   97,   97,
   97,   97,    0,    0,    8,    0,    0,    0,    0,    0,
   58,    0,   59,    0,    0,    0,    8,    0,    0,    0,
    0,    8,    8,    0,    8,    0,    8,    8,    8,    8,
    8,    8,    8,    0,    0,    0,    0,    8,    8,    0,
    8,    0,    0,    0,    8,    0,    8,    8,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   92,    0,    0,    0,    0,   92,
   92,    0,   92,    0,   92,    0,   92,   92,   92,   92,
   20,    0,    0,    0,    0,    0,   24,   24,    1,    0,
    0,    0,    0,    2,    3,    0,    4,   24,   20,    0,
    5,   21,    6,    7,    0,    0,    0,   24,   24,    0,
    0,    0,    0,    0,    0,    0,    0,   22,   24,   21,
    0,    0,    0,   24,   24,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   24,   22,    8,    0,    0,    0,
    0,    8,    8,    0,    8,    0,    0,    0,    8,    0,
    8,    8,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   53,
   46,   54,   55,   56,   57,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   47,   48,
    0,    0,    0,    0,    0,    0,    0,    0,   54,   55,
   56,   57,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
    0,   41,   61,   45,   41,    0,   41,   41,   41,   44,
   41,   24,   32,   44,   20,   39,   31,   74,   45,   41,
    0,   35,   36,   49,  112,  257,   61,  123,   34,   40,
   61,   61,  257,   39,   42,   20,   40,  262,  263,   47,
    0,   41,   41,   43,   43,   45,   45,   40,   61,   34,
   70,   98,   99,  141,   39,  125,    0,   61,  257,   59,
   60,   41,   62,   43,   40,   45,   90,  124,   82,   83,
   59,   43,    0,   45,  100,  101,  133,  118,  119,   59,
   60,   41,   62,   43,   90,   45,  143,  144,    0,   41,
  103,   43,  112,   45,  266,  262,  263,   44,  267,   59,
   60,  116,   62,  268,    0,   90,  273,  270,   44,  256,
  257,    0,  118,  119,   41,   59,  257,  264,   41,   41,
    0,  141,   41,   41,  275,  125,  146,    0,  275,  276,
  125,   59,  123,  118,  119,  125,   61,  257,  257,    0,
  257,  275,  125,  262,  263,  125,  265,   59,  257,  257,
  269,   61,  271,  272,  266,   40,  270,  123,  125,    0,
  125,  257,   68,   59,  146,  125,  262,  263,  145,  265,
   59,   87,   60,  269,   91,  271,  272,   -1,   -1,   59,
   -1,  125,   -1,   -1,   -1,   -1,   59,  257,   45,    0,
   -1,   -1,  262,  263,   -1,  265,   -1,  125,   59,  269,
   -1,  271,  272,   -1,   -1,    0,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  125,   -1,  257,  275,  257,   59,   -1,
  257,  256,   -1,  257,  257,  256,  256,  262,  263,  125,
  257,  262,  263,  275,  276,   -1,  125,    0,  273,  274,
  262,  263,  273,  274,  274,  125,   -1,   -1,  275,  276,
   -1,  273,  125,   -1,   -1,   -1,   -1,  257,  258,  259,
  260,  261,  262,  263,  125,  265,  266,  267,  268,  269,
  270,  271,  272,   -1,   -1,   -1,   -1,  257,  258,  259,
  260,  261,  262,  263,  125,  265,  266,  267,  268,  269,
  270,  271,  272,   -1,   -1,   41,   59,  257,  258,  259,
  260,  261,  262,  263,   -1,  265,  266,  267,  268,  269,
  270,  271,  272,  257,  125,   61,   41,   -1,  262,  263,
   -1,  265,  266,  267,  268,  269,  270,  271,  272,  257,
  125,  256,   41,   -1,  262,  263,   61,  265,   -1,  267,
  268,  269,  270,  271,  272,  257,  256,   -1,   -1,  274,
  262,  263,   61,  265,   -1,  267,  268,  269,  270,  271,
  272,  257,  125,   -1,  274,   -1,  262,  263,   -1,  265,
   -1,  267,  268,  269,  270,  271,  272,  257,  267,  268,
   -1,  270,  262,  263,   -1,  265,  125,  267,  268,  269,
  270,  271,  272,   -1,  267,  268,  257,  270,   45,  256,
  257,  262,  263,   -1,  265,   -1,  267,  268,  269,  270,
  271,  272,   -1,   60,   -1,   62,  257,   -1,  275,  276,
   -1,  262,  263,   43,  265,   45,  267,  268,  269,  270,
  271,  272,   -1,   -1,  125,   -1,   -1,   -1,   -1,   -1,
   60,   -1,   62,   -1,   -1,   -1,  257,   -1,   -1,   -1,
   -1,  262,  263,   -1,  265,   -1,  267,  268,  269,  270,
  271,  272,  257,   -1,   -1,   -1,   -1,  262,  263,   -1,
  265,   -1,   -1,   -1,  269,   -1,  271,  272,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  257,   -1,   -1,   -1,   -1,  262,
  263,   -1,  265,   -1,  267,   -1,  269,  270,  271,  272,
  256,   -1,   -1,   -1,   -1,   -1,  262,  263,  257,   -1,
   -1,   -1,   -1,  262,  263,   -1,  265,  273,  274,   -1,
  269,  256,  271,  272,   -1,   -1,   -1,  262,  263,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  256,  273,  274,
   -1,   -1,   -1,  262,  263,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  273,  274,  257,   -1,   -1,   -1,
   -1,  262,  263,   -1,  265,   -1,   -1,   -1,  269,   -1,
  271,  272,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  256,
  257,  258,  259,  260,  261,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  275,  276,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  258,  259,
  260,  261,
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
"lista_params_inv : ID",
"lista_params_inv : ID separador_variables ID",
"lista_params_inv : ID separador_variables ID separador_variables ID",
"lista_params_inv : ID separador_variables ID separador_variables ID separador_variables lista_params_inv",
"asignacion : ID '=' expresion",
"asignacion : ID '=' error",
"asignacion : ID",
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
"if : encabezado_if rama_then rama_else fin_if",
"if : encabezado_if rama_then_prima fin_if",
"fin_if : END_IF",
"fin_if :",
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

//#line 233 "archivos/gramatica.y"


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
    }

    private int yylex() {
        int token = aLexico.produceToken();
        yylval = new ParserVal(aLexico.ultimoLexemaGenerado);
        return token;
    }

    private void yyerror(String mensajeError) {
        TablaNotificaciones.agregarError(aLexico.getLineaActual(),mensajeError);
    }

    private void checkCambioSigno() {
        String lexemaSignoNoC = yylval.sval; //Obtengo el lexema del factor.
        Celda celdaOriginal = tablaS.getEntrada(lexemaSignoNoC); //La sentencia va aca si o si, porque mas adelante ya no existe la entrada en la TS.

        if (celdaOriginal.getTipo().equals("DOUBLE")) {
            tablaS.quitarReferencia(lexemaSignoNoC); //El lexema esta en la TS si o si. refs--.
            if (tablaS.entradaSinReferencias(lexemaSignoNoC)) tablaS.eliminarEntrada(lexemaSignoNoC);

            String lexemaSignoC = String.valueOf(Double.parseDouble(lexemaSignoNoC) * -1); //Cambio el signo del factor.
            tablaS.agregarEntrada(celdaOriginal.getToken(), lexemaSignoC, celdaOriginal.getTipo());

        } else
            TablaNotificaciones.agregarError(aLexico.getLineaActual(),"No se permiten UINT negativos");
    }

    private String getAmbitoId(String lexema) {
        StringBuilder builderAmbito = new StringBuilder(pilaAmbitos.getAmbitosConcatenados());

        String aux;
        while (!builderAmbito.toString().isEmpty()) {
            //Busca el id en el ambito actual.
            if (tablaS.contieneLexema(builderAmbito.toString()+":"+lexema))
                return builderAmbito.toString();

            //"Baja" un nivel en la pila de ambitos.
            if (!builderAmbito.toString().equals("PROGRAM")) //Chequea no estar en el ambito global.
                builderAmbito.delete(builderAmbito.lastIndexOf(":"), builderAmbito.length());
            else builderAmbito.delete(0, builderAmbito.length());
        }
        return ""; //La variable no esta declarada.
    }

    //---DECLARACION VARIABLES Y PARAMS---

    private String ultimoTipoLeido; //Almacena temporalmente el ultimo tipo leido.

    private boolean nombreIdValido = false;

    private void declaraId(String uso, String lexema, String tipo) {
        String ambito = getAmbitoId(lexema);

        if (!ambito.isEmpty() //La TS contiene el lexema recibido.
                && tablaS.isEntradaDeclarada(ambito+":"+lexema))//Tiene el flag de declaracion activado.
            TablaNotificaciones.agregarError(aLexico.getLineaActual(),"El identificador '" + lexema + "' ya se encuentra declarado.");
        else {
            tablaS.setTipoEntrada(lexema, tipo);
            tablaS.setUsoEntrada(lexema, uso);
            tablaS.setDeclaracionEntrada(lexema, true);
            tablaS.setAmbitoEntrada(lexema, pilaAmbitos.getAmbitosConcatenados()); //Actualizo el lexema en la TS.

            nombreIdValido = true;
        }
    }

    //---DECLARACION PROCS---

    private String nombreProc; //Almacena temporalmente el nombre de un procedimiento.

    private int maxInvocProc; //Almacena temporalmente el maximo de invocaciones para un procedimiento.

    private final List<String> listaParams = new ArrayList<>();

    private int lineaNI; //Guarda la linea donde se declaro el proc.

    private void declaraIdProc(String lexema) {
        declaraId(TablaSimbolos.USO_ENTRADA_PROC,lexema,"-");
        nombreProc = pilaAmbitos.getAmbitosConcatenados()+":"+lexema;
        lineaNI = aLexico.getLineaActual();
    }

    private void declaraProc(){
        if (maxInvocProc < 1 || maxInvocProc > 4)
            TablaNotificaciones.agregarError(lineaNI,
                    "El numero de invocaciones de un procedimiento debe estar en el rango [1,4].");
        else {
            tablaS.setMaxInvoc(nombreProc, maxInvocProc);

            int nParams = listaParams.size();
            if (nParams > 3) nParams = 3; //Se queda con los primeros 3 params y descarta el resto.
            tablaS.setParamsProc(nombreProc, listaParams.subList(0,nParams)); //A esta altura ya se verificaron los ids correspondientes a cada
                                                                                // parametro. Solo resta asociarlos con el lexema del proc.
            listaParams.clear();
            nombreIdValido = false; //Reinicia el valor.
        }
    }

    //---INVOCACION PROCS---

    private void guardaParamsInvoc(String... lexemaParams){
        for (String lexemaParam : lexemaParams)
            listaParams.add(getAmbitoId(lexemaParam) + ":" + lexemaParam);
    }

    private boolean tipoParamsValidos(String lexema, int nParamsDecl){
        boolean invocValida = true;
        for (int i = 0; i < nParamsDecl; i++){
            String tipoParamInvoc = tablaS.getTipo(listaParams.get(i));
            String tipoParamDecl = tablaS.getTipoParam(lexema,i);
            if (!tipoParamInvoc.equals(tipoParamDecl)){
                TablaNotificaciones.agregarError(aLexico.getLineaActual(),
                        "En la posicion "+(i+1)+" se esperaba un "+tipoParamDecl+", pero se encontro un "+tipoParamInvoc+".");
                invocValida = false;
            }
        }
        return invocValida;
    }

    private void invocaProc(String lexema) {
        String ambito = getAmbitoId(lexema);
        boolean invocValida = true;

        if (ambito.isEmpty()) {
            TablaNotificaciones.agregarError(aLexico.getLineaActual(),"El procedimiento '" + lexema + "' no esta declarado.");
            return; //Es necesario cortar aca para que 'ambito' no cause problemas por estar vacio.
        }
        String nLexema = ambito+":"+lexema;
        if (tablaS.maxInvocAlcanzadas(nLexema)) {
            TablaNotificaciones.agregarError(aLexico.getLineaActual(),"El procedimiento '" + lexema + "' ya alcanzo su numero maximo de invocaciones.");
            invocValida = false;
        } else tablaS.incrementaNInvoc(nLexema);

        int nParamsDecl = tablaS.getNParams(nLexema);
        if (nParamsDecl != listaParams.size()){
            TablaNotificaciones.agregarError(aLexico.getLineaActual(),"Se esperaban "+nParamsDecl+" parametros, pero se encontraron "+ listaParams.size()+".");
            invocValida = false;
        }

        if (nParamsDecl != 0 && listaParams.size() >= nParamsDecl) //El '>=' permite analizar los primeros parametros, aunque se tengan mas de los declarados.
            invocValida = tipoParamsValidos(nLexema,nParamsDecl);

        if (invocValida)
            generaCodigoInvocacion(nombreProc,nParamsDecl);

        listaParams.clear();
    }

    private void generaCodigoInvocacion(String lexemaProc, int nParamsDecl){
        String paramDecl, paramInvoc;
        for (int i = 0; i < nParamsDecl; i++) { //Pasa el valor de los parametros reales a los formales.
            paramDecl = tablaS.getParam(lexemaProc, i);
            paramInvoc = listaParams.get(i);
            agregarPasosRepr(paramInvoc,paramDecl,"="); //paramDecl = paramInvoc.
        }

        int posPreInvocacion = polacaProgram.longitud();
        int posActual = posPreInvocacion;
        for (String paso : polacaProcedimientos.getListaPasos(lexemaProc)){ //Obtiene el codigo intermedio de la funcion.
            agregarPasosRepr(paso);
            if (paso.equals(Polaca.PASO_BF) || paso.equals(Polaca.PASO_BI)) //Hay que ajustar el paso a donde hay que saltar.
                polacaProgram.ajustaPaso(posActual, posPreInvocacion);
            posActual++;
        }

        for (int i = 0; i < nParamsDecl ; i++){ //Pasa el valor de los param formales a los reales (En caso de param CVR).
            paramDecl = tablaS.getParam(lexemaProc,i);
            if (tablaS.isEntradaParamCVR(paramDecl)){
                paramInvoc = listaParams.get(i);
                agregarPasosRepr(paramDecl,paramInvoc,"="); //paramInvoc = paramDecl.
            }
        }
    }

    //---ASIGN---

    private void checkValidezAsign(String lexema) {
        String ambito = getAmbitoId(lexema);

        if (ambito.isEmpty()) { //La TS no contiene el lexema recibido en ningun ambito.
            TablaNotificaciones.agregarError(aLexico.getLineaActual(),"El identificador '"+lexema+"' no esta declarado.");
            return; //Es necesario cortar aca para que 'ambito' no cause problemas por estar vacio.
        }

        String nLexema = ambito+":"+lexema;
        if (!tablaS.isEntradaDeclarada(nLexema)) //Existe el lexema en la TS y tiene el flag de declaracion desactivado.
            TablaNotificaciones.agregarError(aLexico.getLineaActual(),"El identificador '" + lexema + "' no esta declarado.");
        if (tablaS.isEntradaProc(nLexema)) { //Esta declarado pero es un procedimiento.
            TablaNotificaciones.agregarError(aLexico.getLineaActual(),"Un procedimiento no puede estar a la izquierda una asignacion.");
        }

        agregarPasosRepr(nLexema,"=");
    }

    private void checkValidezFactor(String lexema){
        String ambito = getAmbitoId(lexema);

        if (ambito.isEmpty()) { //La TS no contiene el lexema recibido en ningun ambito.
            TablaNotificaciones.agregarError(aLexico.getLineaActual(),"El identificador '"+lexema+"' no esta declarado.");
            return; //Es necesario cortar aca para que 'ambito' no cause problemas por estar vacio.
        }

        String nLexema = ambito+":"+lexema;
        if (!tablaS.isEntradaDeclarada(nLexema)) //Existe el lexema en la TS y tiene el flag de declaracion desactivado.
            TablaNotificaciones.agregarError(aLexico.getLineaActual(),"El identificador '" + lexema + "' no esta declarado.");
        if (tablaS.isEntradaProc(nLexema)) { //Esta declarado pero es un procedimiento.
            TablaNotificaciones.agregarError(aLexico.getLineaActual(),"Un procedimiento no puede estar a la derecha una asignacion.");
        }

        agregarPasosRepr(nLexema);
    }

    //---OUT---

    private String tipoImpresion; //Almacena temporalmente el tipo de dato que debe imprimirse.

    private boolean isIdDeclarado(String lexema){
        String ambito = getAmbitoId(lexema);

        if (ambito.isEmpty()) { //La TS no contiene el lexema recibido en ningun ambito.
            TablaNotificaciones.agregarError(aLexico.getLineaActual(),"El identificador '"+lexema+"' no esta declarado.");
            return false; //Es necesario cortar aca para que 'ambito' no cause problemas por estar vacio.
        }

        if (!tablaS.isEntradaDeclarada(ambito+":"+lexema)) { //Existe el lexema en la TS y tiene el flag de declaracion desactivado.
            TablaNotificaciones.agregarError(aLexico.getLineaActual(),"El identificador '" + lexema + "' no esta declarado.");
            return false;
        }
        return true;
    }

    //---POLACA---

    private void agregarPasosRepr(String... pasos) {
        if (pilaAmbitos.inAmbitoGlobal())
            polacaProgram.agregarPasos(pasos);
        else polacaProcedimientos.agregarPasos(pilaAmbitos.getAmbitosConcatenados(), pasos);
    }

    private void puntoControlThen() {
        if (pilaAmbitos.inAmbitoGlobal())
            polacaProgram.puntoControlThen();
        else polacaProcedimientos.ejecutarPuntoControl(pilaAmbitos.getAmbitosConcatenados(), Polaca.PC_THEN);
    }

    private void puntoControlElse() {
        if (pilaAmbitos.inAmbitoGlobal())
            polacaProgram.puntoControlElse();
        else polacaProcedimientos.ejecutarPuntoControl(pilaAmbitos.getAmbitosConcatenados(), Polaca.PC_ELSE);
    }

    private void puntoControlFinCondicional() {
        if (pilaAmbitos.inAmbitoGlobal())
            polacaProgram.puntoControlFinCondicional();
        else polacaProcedimientos.ejecutarPuntoControl(pilaAmbitos.getAmbitosConcatenados(), Polaca.PC_FIN_COND);
    }

    private void puntoControlLoop() {
        if (pilaAmbitos.inAmbitoGlobal())
            polacaProgram.puntoControlLoop();
        else polacaProcedimientos.ejecutarPuntoControl(pilaAmbitos.getAmbitosConcatenados(), Polaca.PC_LOOP);
    }

    private void puntoControlUntil() {
        if (pilaAmbitos.inAmbitoGlobal())
            polacaProgram.puntoControlUntil();
        else polacaProcedimientos.ejecutarPuntoControl(pilaAmbitos.getAmbitosConcatenados(), Polaca.PC_UNTIL);
    }

    public void printPolaca() {
        System.out.println(polacaProgram.toString());
    }


//#line 776 "Parser.java"
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
//#line 25 "archivos/gramatica.y"
{ultimoTipoLeido = "UINT";}
break;
case 3:
//#line 26 "archivos/gramatica.y"
{ultimoTipoLeido = "DOUBLE";}
break;
case 8:
//#line 37 "archivos/gramatica.y"
{TablaNotificaciones.agregarError(aLexico.getLineaActual()-1,"Falta ';' al final de la sentencia.");}
break;
case 10:
//#line 41 "archivos/gramatica.y"
{
                                                                if (nombreIdValido) {
                                                                    declaraProc();
                                                                }
                                                                pilaAmbitos.eliminarUltimo();
                                                                }
break;
case 12:
//#line 50 "archivos/gramatica.y"
{declaraId("Variable",val_peek(0).sval,ultimoTipoLeido);}
break;
case 13:
//#line 51 "archivos/gramatica.y"
{declaraId("Variable",val_peek(2).sval,ultimoTipoLeido);}
break;
case 14:
//#line 54 "archivos/gramatica.y"
{
                        declaraIdProc(val_peek(0).sval);
                        pilaAmbitos.agregarAmbito(val_peek(0).sval); /*Guardo el nombre del procedimiento en caso de necesitarlo.*/
                        }
break;
case 15:
//#line 58 "archivos/gramatica.y"
{yyerror("Falta el identificador del procedimiento.");}
break;
case 18:
//#line 63 "archivos/gramatica.y"
{yyerror("Falta el parentesis de cierre para los parametros.");}
break;
case 19:
//#line 64 "archivos/gramatica.y"
{yyerror("Falta el parentesis de cierre para los parametros.");}
break;
case 23:
//#line 70 "archivos/gramatica.y"
{yyerror("Un procedimiento no puede tener mas de 3 parametros.");}
break;
case 24:
//#line 73 "archivos/gramatica.y"
{yyerror("Falta una ',' para separar dos parametros.");}
break;
case 28:
//#line 81 "archivos/gramatica.y"
{
                                listaParams.add(pilaAmbitos.getAmbitosConcatenados()+":"+val_peek(0).sval);
                                declaraId("ParamCVR",val_peek(0).sval,ultimoTipoLeido);
                                }
break;
case 29:
//#line 85 "archivos/gramatica.y"
{yyerror("Falta el tipo de un parametro.");}
break;
case 30:
//#line 86 "archivos/gramatica.y"
{yyerror("Falta el identificador de un parametro.");}
break;
case 31:
//#line 89 "archivos/gramatica.y"
{
                            listaParams.add(pilaAmbitos.getAmbitosConcatenados()+":"+val_peek(0).sval);
                            declaraId("ParamCV",val_peek(0).sval,ultimoTipoLeido);
                            }
break;
case 32:
//#line 93 "archivos/gramatica.y"
{yyerror("Falta el identificador de un parametro.");}
break;
case 33:
//#line 96 "archivos/gramatica.y"
{maxInvocProc = Integer.parseInt(val_peek(0).sval);}
break;
case 34:
//#line 97 "archivos/gramatica.y"
{yyerror("Falta el numero de invocaciones del procedimiento.");}
break;
case 35:
//#line 98 "archivos/gramatica.y"
{yyerror("Falta la palabra clave 'NI' en el encabezado del procedimiento.");}
break;
case 36:
//#line 99 "archivos/gramatica.y"
{yyerror("Formato de declaracion de NI invalido. El formato correcto es 'NI = CTE_UINT'.");}
break;
case 37:
//#line 100 "archivos/gramatica.y"
{yyerror("Formato de declaracion de NI invalido. El formato correcto es 'NI = CTE_UINT'.");}
break;
case 39:
//#line 104 "archivos/gramatica.y"
{yyerror("Cuerpo del procedimiento vacio.");}
break;
case 45:
//#line 114 "archivos/gramatica.y"
{
                            guardaParamsInvoc();
                            invocaProc(val_peek(2).sval);
                            }
break;
case 46:
//#line 118 "archivos/gramatica.y"
{invocaProc(val_peek(3).sval);}
break;
case 47:
//#line 123 "archivos/gramatica.y"
{guardaParamsInvoc(val_peek(0).sval);}
break;
case 48:
//#line 124 "archivos/gramatica.y"
{guardaParamsInvoc(val_peek(2).sval, val_peek(0).sval);}
break;
case 49:
//#line 125 "archivos/gramatica.y"
{guardaParamsInvoc(val_peek(4).sval, val_peek(2).sval, val_peek(0).sval);}
break;
case 50:
//#line 127 "archivos/gramatica.y"
{
                                                    guardaParamsInvoc(val_peek(6).sval, val_peek(4).sval, val_peek(2).sval);
                                                    yyerror("Un procedimiento no puede tener mas de 3 parametros.");
                                                    }
break;
case 51:
//#line 133 "archivos/gramatica.y"
{checkValidezAsign(val_peek(2).sval);}
break;
case 52:
//#line 134 "archivos/gramatica.y"
{
                            checkValidezAsign(val_peek(2).sval);
                            yyerror("El lado derecho de la asignacio no es valido.");
                            }
break;
case 53:
//#line 138 "archivos/gramatica.y"
{yyerror("Un identificador en solitario no es una sentencia valida.");}
break;
case 54:
//#line 141 "archivos/gramatica.y"
{agregarPasosRepr("+");}
break;
case 55:
//#line 142 "archivos/gramatica.y"
{agregarPasosRepr("-");}
break;
case 57:
//#line 146 "archivos/gramatica.y"
{agregarPasosRepr("*");}
break;
case 58:
//#line 147 "archivos/gramatica.y"
{agregarPasosRepr("/");}
break;
case 60:
//#line 151 "archivos/gramatica.y"
{checkValidezFactor(val_peek(0).sval);}
break;
case 61:
//#line 152 "archivos/gramatica.y"
{agregarPasosRepr(val_peek(0).sval);}
break;
case 62:
//#line 153 "archivos/gramatica.y"
{agregarPasosRepr(val_peek(0).sval);}
break;
case 63:
//#line 154 "archivos/gramatica.y"
{checkCambioSigno(); agregarPasosRepr("-");}
break;
case 65:
//#line 160 "archivos/gramatica.y"
{puntoControlLoop();}
break;
case 67:
//#line 164 "archivos/gramatica.y"
{yyerror("Falta el bloque de sentencias ejecutables del LOOP.");}
break;
case 70:
//#line 169 "archivos/gramatica.y"
{yyerror("Bloque de sentencias vacio.");}
break;
case 71:
//#line 170 "archivos/gramatica.y"
{yyerror("No se permiten sentencias declarativas dentro de un bloque de estructura de control.");}
break;
case 74:
//#line 175 "archivos/gramatica.y"
{yyerror("No se permiten sentencias declarativas dentro de un bloque de estructura de control.");}
break;
case 75:
//#line 176 "archivos/gramatica.y"
{yyerror("No se permiten sentencias declarativas dentro de un bloque de estructura de control.");}
break;
case 76:
//#line 179 "archivos/gramatica.y"
{puntoControlUntil();}
break;
case 77:
//#line 180 "archivos/gramatica.y"
{yyerror("Falta la condicion de corte del LOOP.");}
break;
case 78:
//#line 183 "archivos/gramatica.y"
{agregarPasosRepr(val_peek(2).sval);}
break;
case 79:
//#line 184 "archivos/gramatica.y"
{yyerror("Falta parentesis de cierre de la condicion.");}
break;
case 80:
//#line 185 "archivos/gramatica.y"
{yyerror("Falta expresion en el lado izquierdo de la condicion.");}
break;
case 81:
//#line 186 "archivos/gramatica.y"
{yyerror("Falta expresion en el lado derecho de la condicion.");}
break;
case 82:
//#line 187 "archivos/gramatica.y"
{yyerror("Error en la condicion.");}
break;
case 93:
//#line 206 "archivos/gramatica.y"
{puntoControlThen();}
break;
case 94:
//#line 207 "archivos/gramatica.y"
{yyerror("Falta la condicion del IF.");}
break;
case 95:
//#line 210 "archivos/gramatica.y"
{puntoControlElse();}
break;
case 96:
//#line 211 "archivos/gramatica.y"
{yyerror("Falta el bloque de sentencias ejecutables de la rama THEN.");}
break;
case 97:
//#line 214 "archivos/gramatica.y"
{puntoControlFinCondicional();}
break;
case 98:
//#line 215 "archivos/gramatica.y"
{yyerror("Falta el bloque de sentencias ejecutables de la rama THEN.");}
break;
case 99:
//#line 218 "archivos/gramatica.y"
{puntoControlFinCondicional();}
break;
case 100:
//#line 219 "archivos/gramatica.y"
{yyerror("Falta el bloque de sentencias ejecutables de la rama ELSE.");}
break;
case 101:
//#line 222 "archivos/gramatica.y"
{agregarPasosRepr(val_peek(1).sval,tipoImpresion);}
break;
case 102:
//#line 223 "archivos/gramatica.y"
{yyerror("Falta parentesis de cierre de la sentencia OUT.");}
break;
case 103:
//#line 224 "archivos/gramatica.y"
{yyerror("El contenido de la sentencia OUT no es valido.");}
break;
case 104:
//#line 227 "archivos/gramatica.y"
{tipoImpresion = "OUT_CAD";}
break;
case 105:
//#line 228 "archivos/gramatica.y"
{tipoImpresion = "OUT_UINT";}
break;
case 106:
//#line 229 "archivos/gramatica.y"
{tipoImpresion = "OUT_DOU";}
break;
case 107:
//#line 230 "archivos/gramatica.y"
{if (isIdDeclarado(val_peek(0).sval)) tipoImpresion = "OUT_"+tablaS.getTipo(getAmbitoId(val_peek(0).sval)+":"+val_peek(0).sval);}
break;
//#line 1220 "Parser.java"
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
