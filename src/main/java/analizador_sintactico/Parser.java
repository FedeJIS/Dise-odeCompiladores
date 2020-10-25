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
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    2,    2,    1,    1,    3,    3,    4,    4,    5,
    5,    7,    7,    8,    8,    8,    8,   12,   12,   12,
   12,   14,   14,   13,   13,   15,   15,   15,   16,   16,
    9,    9,    9,    9,    9,   10,   10,   11,   11,    6,
    6,    6,    6,    6,   17,   17,   22,   22,   22,   22,
   18,   18,   23,   23,   23,   24,   24,   24,   25,   25,
   25,   25,   19,   26,   27,   27,   29,   29,   29,   30,
   30,   28,   28,   31,   31,   31,   31,   31,   32,   32,
   32,   32,   32,   32,   20,   20,   33,   33,   34,   34,
   36,   36,   35,   35,   21,   21,   21,   37,   37,   37,
   37,
};
final static short yylen[] = {                            2,
    1,    1,    1,    2,    3,    1,    1,    0,    1,    4,
    2,    2,    1,    3,    2,    2,    1,    1,    3,    5,
    7,    0,    1,    1,    1,    3,    2,    2,    2,    1,
    3,    2,    2,    2,    1,    3,    2,    1,    3,    1,
    1,    1,    1,    1,    3,    4,    1,    3,    5,    7,
    3,    3,    3,    3,    1,    3,    3,    1,    1,    1,
    1,    2,    3,    1,    1,    0,    2,    3,    2,    2,
    3,    2,    1,    5,    4,    4,    4,    3,    1,    1,
    1,    1,    1,    1,    4,    3,    2,    1,    2,    1,
    2,    1,    2,    1,    4,    3,    4,    1,    1,    1,
    1,
};
final static short yydefred[] = {                         0,
    0,    2,    3,    0,   64,    0,    0,    0,    1,    0,
    0,    6,    7,    0,   40,   41,   42,   43,   44,    0,
    0,    0,    0,    0,   87,    0,   12,    0,   11,    9,
    0,    0,    0,    0,    0,    0,   65,    0,    0,    0,
    0,   45,    0,   52,   59,   60,   61,    0,    0,    0,
   58,    0,   80,   79,   84,   83,   81,   82,    0,    0,
    0,  101,   98,   99,  100,    0,    0,    5,    0,   15,
    0,    0,    0,   24,   25,   35,    0,    0,    0,   69,
    0,    0,   67,    0,   63,    0,    0,    0,   86,   23,
    0,   46,   62,    0,    0,    0,    0,   78,    0,    0,
   97,   95,   39,   27,    0,   29,   14,    0,   34,    0,
   33,    0,   10,    0,   68,   72,   93,   85,    0,    0,
    0,   56,   57,   77,    0,   76,   26,    0,   31,   37,
    0,   71,    0,   74,    0,   36,    0,    0,    0,    0,
   50,   21,
};
final static short yydgoto[] = {                          8,
    9,   71,   11,   31,   12,   13,   14,   33,   79,  113,
   29,   72,   73,   91,   74,   75,   15,   16,   17,   18,
   19,   43,   49,   50,   51,   20,   36,   85,   37,   82,
   25,   60,   21,   39,   88,   40,   66,
};
final static short yysindex[] = {                       -61,
  -23,    0,    0,   -7,    0,   15, -237,    0,    0, -199,
   13,    0,    0,   35,    0,    0,    0,    0,    0, -110,
 -187,  -39,   20,  191,    0, -158,    0,   49,    0,    0,
  -61,  -30,  -33, -113,   13, -173,    0, -110, -165, -164,
   71,    0,   83,    0,    0,    0,    0,  -26,   51,  -18,
    0,   84,    0,    0,    0,    0,    0,    0,  272,  -26,
   86,    0,    0,    0,    0,   88, -199,    0, -186,    0,
 -135,   92,   71,    0,    0,    0,  -31, -145,   11,    0,
   13,   10,    0,   -7,    0,    0, -110, -131,    0,    0,
 -119,    0,    0,  -26,  -26,  -26,  -26,    0,  -41,   66,
    0,    0,    0,    0, -118,    0,    0, -228,    0, -134,
    0,  -94,    0, -217,    0,    0,    0,    0,   71,  -18,
  -18,    0,    0,    0,   67,    0,    0,   71,    0,    0,
   18,    0, -112,    0, -228,    0,   71,   71, -108, -228,
    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0, -115,    0,    0,  113,    0,    0,    0,
  140,    0,    0,    0,    0,    0,    0,    0,    0, -116,
    0,    0,    0,    0,    0,    0,    0,  121,    0,    0,
    6,   75,    0,    0, -231,    0,    0, -218,    0,    0,
  -38,    0,    0,    0,    0,    0,    0,    0,   73,    1,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   89,    0,    0,    0,    0,
  -36,   81,  126,    0,    0,    0,    0,    0,    0,    0,
  -72,    0,    0,  105,    0, -154, -111,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  -34,    0,    0,    0,    0,   37,
    0,    0,    0,   40,    0,    0,    0,    0,  -32,   21,
   41,    0,    0,    0,   57,    0,    0,  145,    0,    0,
    0,    0,    0,    0,    0,    0,  -15,  154,    0,    0,
    0,    0,
};
final static short yygindex[] = {                         0,
  -17,   16,    0,  -13,    0,   36,    0,    0,    0,    0,
   95,   30,  -76,  -50,    0,    0,    0,    0,    0,    0,
    0,   33,   -9,   25,   -5,    0,    0,    0,  -20,   59,
   90,  117,    0,    0,    0,    0,    0,
};
final static int YYTABLESIZE=533;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                        124,
   55,   42,   47,   48,   30,    4,   28,   30,   48,   28,
   70,   80,   34,   68,   59,   10,   22,   86,   48,   27,
   53,   83,  108,   96,   30,   49,   28,   78,   97,  110,
  130,  128,   24,    2,    3,    8,    8,   23,    8,    1,
   54,   55,   93,   55,   69,   55,   10,    4,   90,   92,
  100,    5,    8,    6,   26,   35,   75,   28,  138,   55,
   55,   53,   55,   53,   48,   53,  117,  114,  133,   81,
  104,   30,   51,   35,   32,    2,    3,  135,   38,   53,
   53,   54,   53,   54,  105,   54,  139,  140,   96,  125,
  122,  123,   67,   94,  131,   95,   84,   61,   62,   54,
   54,   87,   54,   89,   73,   63,  126,  134,   94,   94,
   95,   95,   89,   91,   90,   75,   64,   65,  120,  121,
   38,  106,   35,   92,   98,   55,  101,   10,  102,  111,
    4,   51,  107,  112,  115,   17,  118,  119,  127,    8,
  129,   16,  136,    1,  137,   53,    1,   96,   41,   81,
   88,    4,   13,   66,    4,    5,   94,    6,    5,   32,
    6,  103,    1,   73,   70,   54,   18,    2,    3,  142,
    4,  141,  132,  116,    5,   99,    6,    7,    0,   38,
    0,   75,    0,    0,    8,   19,   18,    0,    0,    0,
    0,    0,    8,    0,   20,    1,    8,   51,    8,    0,
    2,    3,    0,    4,    0,   19,    0,    5,    0,    6,
    7,    0,    0,   96,   20,   45,    0,   41,   22,   30,
    0,   28,   76,    0,   22,   30,   30,   28,   28,   73,
   45,    2,    3,   46,   47,   48,   30,   30,   28,   28,
   77,   22,   69,  109,    0,   38,    0,    0,   46,   47,
   57,    0,   58,    0,    0,    0,    0,   55,   55,   55,
   55,   55,   55,   55,    8,   55,   55,   55,   55,   55,
   55,   55,   55,    0,    0,   44,   45,   53,   53,   53,
   53,   53,   53,   53,    0,   53,   53,   53,   53,   53,
   53,   53,   53,    0,   46,   47,    0,   54,   54,   54,
   54,   54,   54,   54,    0,   54,   54,   54,   54,   54,
   54,   54,   54,   75,   94,    0,   95,    0,   75,   75,
    0,   75,   75,   75,   75,   75,   75,   75,   75,   51,
   17,   57,    0,   58,   51,   51,   16,   51,    0,   51,
   51,   51,   51,   51,   51,   96,    0,    0,   17,    0,
   96,   96,    0,   96,   16,   96,   96,   96,   96,   96,
   96,   73,    0,    0,    0,    0,   73,   73,    0,   73,
    0,   73,   73,   73,   73,   73,   73,   38,    0,    0,
    0,   18,   38,   38,    0,   38,    0,   22,   22,   38,
    0,   38,   38,    0,    0,    0,    8,    0,   22,   18,
   19,    8,    8,    0,    8,    0,   22,   22,    8,   20,
    8,    8,    0,    0,    0,   22,   22,   22,   19,    0,
    0,    0,    0,    0,    0,    0,   22,   20,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   52,   45,   53,   54,
   55,   56,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   46,   47,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   53,
   54,   55,   56,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
    0,   41,   41,   45,   41,    0,   41,   44,   41,   44,
   41,  125,  123,   31,   24,    0,   40,   38,   45,  257,
    0,   35,   73,   42,   61,   41,   61,   61,   47,   61,
  125,  108,   40,  262,  263,  267,  268,   61,  270,  257,
    0,   41,   48,   43,  273,   45,   31,  265,  267,  268,
   60,  269,  125,  271,   40,   20,    0,  257,  135,   59,
   60,   41,   62,   43,   45,   45,   87,   81,  119,   34,
  257,   59,    0,   38,   40,  262,  263,  128,  266,   59,
   60,   41,   62,   43,   69,   45,  137,  138,    0,   99,
   96,   97,   44,   43,  112,   45,  270,  256,  257,   59,
   60,  267,   62,  268,    0,  264,   41,   41,   43,   43,
   45,   45,  267,  268,   44,   59,  275,  276,   94,   95,
    0,  257,   87,   41,   41,  125,   41,  112,   41,  275,
  125,   59,   41,  123,  125,   61,  268,  257,  257,    0,
  275,   61,  125,  257,  257,  125,  257,   59,  257,  114,
  266,  265,   40,  270,  265,  269,  268,  271,  269,  123,
  271,   67,  257,   59,  125,  125,   41,  262,  263,  140,
  265,  139,  114,   84,  269,   59,  271,  272,   -1,   59,
   -1,  125,   -1,   -1,  257,   41,   61,   -1,   -1,   -1,
   -1,   -1,  265,   -1,   41,  257,  269,  125,  271,   -1,
  262,  263,   -1,  265,   -1,   61,   -1,  269,   -1,  271,
  272,   -1,   -1,  125,   61,  257,   -1,  257,  257,  256,
   -1,  256,  256,   -1,  257,  262,  263,  262,  263,  125,
  257,  262,  263,  275,  276,   45,  273,  274,  273,  274,
  274,  257,  273,  275,   -1,  125,   -1,   -1,  275,  276,
   60,   -1,   62,   -1,   -1,   -1,   -1,  257,  258,  259,
  260,  261,  262,  263,  125,  265,  266,  267,  268,  269,
  270,  271,  272,   -1,   -1,  256,  257,  257,  258,  259,
  260,  261,  262,  263,   -1,  265,  266,  267,  268,  269,
  270,  271,  272,   -1,  275,  276,   -1,  257,  258,  259,
  260,  261,  262,  263,   -1,  265,  266,  267,  268,  269,
  270,  271,  272,  257,   43,   -1,   45,   -1,  262,  263,
   -1,  265,  266,  267,  268,  269,  270,  271,  272,  257,
  256,   60,   -1,   62,  262,  263,  256,  265,   -1,  267,
  268,  269,  270,  271,  272,  257,   -1,   -1,  274,   -1,
  262,  263,   -1,  265,  274,  267,  268,  269,  270,  271,
  272,  257,   -1,   -1,   -1,   -1,  262,  263,   -1,  265,
   -1,  267,  268,  269,  270,  271,  272,  257,   -1,   -1,
   -1,  256,  262,  263,   -1,  265,   -1,  262,  263,  269,
   -1,  271,  272,   -1,   -1,   -1,  257,   -1,  273,  274,
  256,  262,  263,   -1,  265,   -1,  262,  263,  269,  256,
  271,  272,   -1,   -1,   -1,  262,  263,  273,  274,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  273,  274,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  256,  257,  258,  259,
  260,  261,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  275,  276,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  258,
  259,  260,  261,
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
"asignacion : ID '=' error",
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
"bloque_sentencias_ejec : sentencia_ejec fin_sentencia",
"bloque_sentencias_ejec : sentencia_ejec fin_sentencia bloque_sentencias_ejec",
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

//#line 196 "archivos/gramatica.y"

    private final AnalizadorLexico aLexico;
    private final TablaSimbolos tablaS;
    private final Polaca polacaProgram = new Polaca();
    private final MultiPolaca polacaProcedimientos = new MultiPolaca();

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
            if (!tablaS.contieneLexema(lexemaSignoC)) {
                tablaS.agregarEntrada(celdaOriginal.getToken(), lexemaSignoC, celdaOriginal.getTipo());
            }
        } else
            TablaNotificaciones.agregarError("Error en la linea " + aLexico.getLineaActual() + ": No se permiten UINT negativos");

    }

    private final PilaAmbitos pilaAmbitos = new PilaAmbitos();

    private void agregarPasosRepr(String... pasos){
        if (pilaAmbitos.inAmbitoGlobal())
            polacaProgram.agregarPasos(pasos);
        else polacaProcedimientos.agregarPasos(pilaAmbitos.getAmbitosConcatenados(), pasos);
    }

    private void puntoControlThen(){
        if (pilaAmbitos.inAmbitoGlobal())
            polacaProgram.puntoControlThen();
        else polacaProcedimientos.ejecutarPuntoControl(pilaAmbitos.getAmbitosConcatenados(),Polaca.PC_THEN);
    }

    private void puntoControlElse(){
        if (pilaAmbitos.inAmbitoGlobal())
            polacaProgram.puntoControlElse();
        else polacaProcedimientos.ejecutarPuntoControl(pilaAmbitos.getAmbitosConcatenados(),Polaca.PC_ELSE);
    }

    private void puntoControlFinCondicional(){
        if (pilaAmbitos.inAmbitoGlobal())
            polacaProgram.puntoControlFinCondicional();
        else polacaProcedimientos.ejecutarPuntoControl(pilaAmbitos.getAmbitosConcatenados(),Polaca.PC_FIN_COND);
    }

    private void puntoControlLoop(){
        if (pilaAmbitos.inAmbitoGlobal())
            polacaProgram.puntoControlLoop();
        else polacaProcedimientos.ejecutarPuntoControl(pilaAmbitos.getAmbitosConcatenados(),Polaca.PC_LOOP);
    }

    private void puntoControlUntil(){
        if (pilaAmbitos.inAmbitoGlobal())
            polacaProgram.puntoControlUntil();
        else polacaProcedimientos.ejecutarPuntoControl(pilaAmbitos.getAmbitosConcatenados(),Polaca.PC_UNTIL);
    }

    public void printPolaca() {
        polacaProgram.print();
    }

    public void printPolacaProcs() {
        polacaProcedimientos.print();
    }
//#line 550 "Parser.java"
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
//#line 34 "archivos/gramatica.y"
{yyerror("Falta ';' al final de la sentencia");}
break;
case 10:
//#line 38 "archivos/gramatica.y"
{pilaAmbitos.eliminarUltimo();}
break;
case 12:
//#line 42 "archivos/gramatica.y"
{pilaAmbitos.agregarAmbito(val_peek(0).sval); /*Guardo el nombre del procedimiento en caso de necesitarlo.*/}
break;
case 13:
//#line 43 "archivos/gramatica.y"
{yyerror("Falta el identificador del procedimiento.");}
break;
case 16:
//#line 48 "archivos/gramatica.y"
{yyerror("Falta el parentesis de cierre para los parametros.");}
break;
case 17:
//#line 49 "archivos/gramatica.y"
{yyerror("Falta el parentesis de cierre para los parametros.");}
break;
case 21:
//#line 55 "archivos/gramatica.y"
{yyerror("Un procedimiento no puede tener mas de 3 parametros.");}
break;
case 22:
//#line 58 "archivos/gramatica.y"
{yyerror("Falta una ',' para separar dos parametros.");}
break;
case 27:
//#line 67 "archivos/gramatica.y"
{yyerror("Falta el tipo de un parametro.");}
break;
case 28:
//#line 68 "archivos/gramatica.y"
{yyerror("Falta el identificador de un parametro.");}
break;
case 30:
//#line 72 "archivos/gramatica.y"
{yyerror("Falta el identificador de un parametro.");}
break;
case 32:
//#line 76 "archivos/gramatica.y"
{yyerror("Formato de declaracion de NI invalido. El formato correcto es 'NI = CTE_UINT'.");}
break;
case 33:
//#line 77 "archivos/gramatica.y"
{yyerror("Formato de declaracion de NI invalido. El formato correcto es 'NI = CTE_UINT'.");}
break;
case 34:
//#line 78 "archivos/gramatica.y"
{yyerror("Formato de declaracion de NI invalido. El formato correcto es 'NI = CTE_UINT'.");}
break;
case 35:
//#line 79 "archivos/gramatica.y"
{yyerror("Formato de declaracion de NI invalido. El formato correcto es 'NI = CTE_UINT'.");}
break;
case 37:
//#line 83 "archivos/gramatica.y"
{yyerror("Cuerpo del procedimiento vacio.");}
break;
case 50:
//#line 104 "archivos/gramatica.y"
{yyerror("Un procedimiento no puede tener mas de 3 parametros.");}
break;
case 51:
//#line 107 "archivos/gramatica.y"
{agregarPasosRepr(val_peek(2).sval,"=");}
break;
case 52:
//#line 108 "archivos/gramatica.y"
{yyerror("El lado izquierdo de la asignacio no es valido.");}
break;
case 53:
//#line 111 "archivos/gramatica.y"
{agregarPasosRepr("+");}
break;
case 54:
//#line 112 "archivos/gramatica.y"
{agregarPasosRepr("-");}
break;
case 56:
//#line 116 "archivos/gramatica.y"
{agregarPasosRepr("*");}
break;
case 57:
//#line 117 "archivos/gramatica.y"
{agregarPasosRepr("/");}
break;
case 59:
//#line 121 "archivos/gramatica.y"
{agregarPasosRepr(val_peek(0).sval);}
break;
case 60:
//#line 122 "archivos/gramatica.y"
{agregarPasosRepr(val_peek(0).sval);}
break;
case 61:
//#line 123 "archivos/gramatica.y"
{agregarPasosRepr(val_peek(0).sval);}
break;
case 62:
//#line 124 "archivos/gramatica.y"
{checkCambioSigno(); agregarPasosRepr("-");}
break;
case 64:
//#line 130 "archivos/gramatica.y"
{puntoControlLoop();}
break;
case 66:
//#line 134 "archivos/gramatica.y"
{yyerror("Falta el bloque de sentencias ejecutables del LOOP.");}
break;
case 69:
//#line 139 "archivos/gramatica.y"
{yyerror("Bloque de sentencias vacio.");}
break;
case 72:
//#line 146 "archivos/gramatica.y"
{puntoControlUntil();}
break;
case 73:
//#line 147 "archivos/gramatica.y"
{yyerror("Falta la condicion de corte del LOOP.");}
break;
case 74:
//#line 150 "archivos/gramatica.y"
{agregarPasosRepr(val_peek(2).sval);}
break;
case 75:
//#line 151 "archivos/gramatica.y"
{yyerror("Falta parentesis de cierre de la condicion.");}
break;
case 76:
//#line 152 "archivos/gramatica.y"
{yyerror("Falta expresion en el lado izquierdo de la condicion.");}
break;
case 77:
//#line 153 "archivos/gramatica.y"
{yyerror("Falta expresion en el lado derecho de la condicion.");}
break;
case 78:
//#line 154 "archivos/gramatica.y"
{yyerror("Error en la condicion.");}
break;
case 87:
//#line 169 "archivos/gramatica.y"
{puntoControlThen();}
break;
case 88:
//#line 170 "archivos/gramatica.y"
{yyerror("Falta la condicion del IF.");}
break;
case 89:
//#line 173 "archivos/gramatica.y"
{puntoControlElse();}
break;
case 90:
//#line 174 "archivos/gramatica.y"
{yyerror("Falta el bloque de sentencias ejecutables de la rama THEN.");}
break;
case 91:
//#line 177 "archivos/gramatica.y"
{puntoControlFinCondicional();}
break;
case 92:
//#line 178 "archivos/gramatica.y"
{yyerror("Falta el bloque de sentencias ejecutables de la rama THEN.");}
break;
case 93:
//#line 181 "archivos/gramatica.y"
{puntoControlFinCondicional();}
break;
case 94:
//#line 182 "archivos/gramatica.y"
{yyerror("Falta el bloque de sentencias ejecutables de la rama ELSE.");}
break;
case 95:
//#line 185 "archivos/gramatica.y"
{agregarPasosRepr(val_peek(1).sval,"OUT");}
break;
case 96:
//#line 186 "archivos/gramatica.y"
{yyerror("Falta parentesis de cierre de la sentencia OUT.");}
break;
case 97:
//#line 187 "archivos/gramatica.y"
{yyerror("El contenido de la sentencia OUT no es valido.");}
break;
//#line 891 "Parser.java"
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
