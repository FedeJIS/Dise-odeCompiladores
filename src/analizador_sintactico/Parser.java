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
    0,    1,    1,    1,    2,    2,    3,    3,    5,    5,
    5,    5,    8,    8,    6,    6,    7,    7,    4,    4,
    4,    4,    4,    9,   14,   14,   14,   14,   10,   10,
   15,   15,   15,   16,   16,   16,   17,   17,   17,   17,
   11,   18,   18,   19,   12,   12,   12,   12,   22,   22,
   23,   23,   23,   24,   24,   20,   20,   21,   21,   25,
   25,   25,   25,   25,   25,   13,   26,   26,   26,   26,
};
final static short yylen[] = {                            2,
    1,    1,    2,    3,    1,    1,   11,    2,    0,    1,
    3,    5,    3,    2,    1,    1,    1,    3,    1,    1,
    1,    1,    1,    2,    2,    3,    1,    2,    3,    2,
    3,    3,    1,    3,    3,    1,    1,    1,    1,    2,
    2,    2,    1,    2,    4,    3,    3,    2,    2,    1,
    2,    1,    1,    2,    1,    2,    3,    2,    5,    1,
    1,    1,    1,    1,    1,    4,    1,    1,    1,    1,
};
final static short yydefred[] = {                         0,
    0,   15,   16,    0,    0,    0,    0,    0,    0,    1,
    0,    5,    6,    0,   19,   20,   21,   22,   23,    0,
   50,    0,    0,    0,   24,   49,    0,    0,   42,    0,
    0,   37,   38,   39,   58,    0,    0,    0,   36,    0,
    0,    8,    0,   41,    0,   53,    0,   25,    0,    0,
    0,   56,   70,   67,   68,   69,    0,    0,   40,   61,
   60,   65,   64,    0,    0,   62,   63,    0,    0,    0,
    4,    0,   44,   51,    0,   46,    0,   26,   57,   66,
    0,    0,    0,    0,    0,    0,    0,   34,   35,   18,
   54,   45,    0,    0,   14,    0,   59,   13,    0,    0,
    0,    0,    0,   12,    0,    0,    7,
};
final static short yydgoto[] = {                          9,
   10,   11,   12,   13,   82,   14,   42,   84,   15,   16,
   17,   18,   19,   25,   37,   38,   39,   20,   44,   29,
   21,   22,   47,   77,   68,   57,
};
final static short yysindex[] = {                        29,
  -24,    0,    0,  -14,  -20,  -11, -212,  -41,    0,    0,
  -12,    0,    0, -204,    0,    0,    0,    0,    0, -186,
    0,  -33,  -36,  -35,    0,    0,   29,   42,    0, -233,
   47,    0,    0,    0,    0,  -35,   -5,  -17,    0,   29,
   58,    0,  -14,    0,  -20,    0, -216,    0,   63,  -10,
  -19,    0,    0,    0,    0,    0,   64, -241,    0,    0,
    0,    0,    0,  -35,  -35,    0,    0,  -35,  -35,  -35,
    0, -204,    0,    0,  -20,    0, -161,    0,    0,    0,
 -165,   67, -147,   68,  -17,  -17,   50,    0,    0,    0,
    0,    0, -146, -158,    0, -241,    0,    0,   52,   73,
 -157, -241,   -3,    0,   29,   -4,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0, -186,    0,    0,    0,    0,    0,
    6,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   11,   12,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    3,    0,    9,
    8,    0,    0,    0,    1,    0,   15,    0,   17,   18,
    0,    0,    0,    0,    0,    0,    0,   81,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    2,    0,   19,    0,    0,    0,
    0,    0,    0,   82,   13,   23,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   84,
    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  -13,   74,    0,    0,    0,   28,   16,  -68,    0,    0,
    0,    0,    0,    0,   26,   35,   45,    0,    0,   14,
   37,    0,    0,    0,    0,    0,
};
final static int YYTABLESIZE=301;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         35,
   52,   55,   33,   36,   48,    2,    8,   17,    3,   36,
   27,   30,   31,   51,   48,   23,   28,   29,   47,    8,
    2,    3,   32,   53,   69,    8,   71,  100,   30,   70,
   54,   81,   64,  104,   65,   46,   24,   64,   49,   65,
   26,   55,   56,   33,   31,   33,   40,   33,   17,   50,
   75,   76,   41,   31,   66,   31,   67,   31,   74,   52,
   55,   33,   33,   32,   33,   32,   17,   32,    8,   27,
   30,   31,   31,   48,   31,   28,   29,   47,   28,   73,
   59,   32,   32,   43,   32,   83,   58,   90,   91,   27,
   97,  106,   64,   87,   65,   28,    2,    3,   85,   86,
   52,   72,   27,   78,   80,   79,   92,   94,   93,   95,
   98,   96,  101,   88,   89,   99,  102,  103,   28,  105,
  107,    9,   10,   83,   11,   52,   55,   33,    0,   83,
    2,    0,   17,    3,    0,   27,   30,   31,    0,   48,
    0,   28,   29,   47,    0,    0,    0,   32,   28,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   32,    0,    0,    0,    0,
   41,   32,    0,    1,    0,    0,    0,    0,    2,    3,
    0,    4,   45,   33,   34,    5,    1,    6,    7,   33,
   34,    2,    3,    0,    4,    0,    0,    0,    5,    0,
    6,    7,   60,   61,   62,   63,    0,    0,    0,    0,
   33,   33,   33,   33,    0,    0,    0,   52,   52,   55,
   31,   31,   31,   31,    0,    0,    0,    0,    0,    0,
   32,   32,   32,   32,    0,    1,    0,    0,    0,    0,
    2,    3,    0,    4,    0,    0,    0,    5,    0,    6,
    7,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
    0,    0,    0,   45,   41,    0,   40,    0,    0,   45,
    0,    0,    0,   27,    0,   40,    0,    0,    0,   40,
  262,  263,    0,  257,   42,   40,   40,   96,   40,   47,
  264,  273,   43,  102,   45,   22,   61,   43,   23,   45,
    4,  275,  276,   41,  257,   43,   59,   45,   41,   24,
  267,  268,  257,   41,   60,   43,   62,   45,   45,   59,
   59,   59,   60,   41,   62,   43,   59,   45,   40,   59,
   59,   59,   60,   59,   62,   59,   59,   59,    5,   43,
   36,   59,   60,  270,   62,   58,   40,   72,   75,  123,
   41,  105,   43,   68,   45,   22,  262,  263,   64,   65,
   59,   44,  123,   41,   41,  125,  268,   41,   81,  257,
  257,   44,   61,   69,   70,  274,   44,  275,   45,  123,
  125,   41,   41,   96,   41,  125,  125,  125,   -1,  102,
  125,   -1,  125,  125,   -1,  125,  125,  125,   -1,  125,
   -1,  125,  125,  125,   -1,   -1,   -1,  125,   75,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  257,   -1,   -1,   -1,   -1,
  257,  257,   -1,  257,   -1,   -1,   -1,   -1,  262,  263,
   -1,  265,  266,  275,  276,  269,  257,  271,  272,  275,
  276,  262,  263,   -1,  265,   -1,   -1,   -1,  269,   -1,
  271,  272,  258,  259,  260,  261,   -1,   -1,   -1,   -1,
  258,  259,  260,  261,   -1,   -1,   -1,  267,  268,  268,
  258,  259,  260,  261,   -1,   -1,   -1,   -1,   -1,   -1,
  258,  259,  260,  261,   -1,  257,   -1,   -1,   -1,   -1,
  262,  263,   -1,  265,   -1,   -1,   -1,  269,   -1,  271,
  272,
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
"bloque_sentencias : sentencia ';'",
"bloque_sentencias : sentencia ';' bloque_sentencias",
"sentencia : sentencia_declarativa",
"sentencia : sentencia_ejecutable",
"sentencia_declarativa : PROC ID '(' lista_params ')' NI '=' CTE_UINT '{' bloque_sentencias '}'",
"sentencia_declarativa : tipo lista_variables",
"lista_params :",
"lista_params : parametro",
"lista_params : parametro ',' parametro",
"lista_params : parametro ',' parametro ',' parametro",
"parametro : VAR tipo ID",
"parametro : tipo ID",
"tipo : UINT",
"tipo : DOUBLE",
"lista_variables : ID",
"lista_variables : ID ',' lista_variables",
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
//#line 10 "gramatica.y"
{yyerror("Falta ';' al final de la sentencia");}
break;
case 7:
//#line 19 "gramatica.y"
{yyout("Declaracion procedimiento.");}
break;
case 13:
//#line 29 "gramatica.y"
{yyout("Parametro_VAR");}
break;
case 14:
//#line 30 "gramatica.y"
{yyout("Parametro");}
break;
case 15:
//#line 33 "gramatica.y"
{yyout("UINT");}
break;
case 16:
//#line 34 "gramatica.y"
{yyout("DOUBLE");}
break;
case 17:
//#line 37 "gramatica.y"
{yyout("Variable");}
break;
case 25:
//#line 51 "gramatica.y"
{yyout("Invocacion_Vacia");}
break;
case 26:
//#line 52 "gramatica.y"
{yyout("Invocacion");}
break;
case 27:
//#line 53 "gramatica.y"
{yyerror("Falta parentesis de cierre");}
break;
case 28:
//#line 54 "gramatica.y"
{yyerror("Falta parentesis de cierre");}
break;
case 29:
//#line 57 "gramatica.y"
{yyout("Asignacion");}
break;
case 30:
//#line 58 "gramatica.y"
{yyerror("Falta expresion para la asignacion");}
break;
case 37:
//#line 71 "gramatica.y"
{yyout("ID");}
break;
case 38:
//#line 72 "gramatica.y"
{yyout("CTE_UINT");}
break;
case 39:
//#line 73 "gramatica.y"
{yyout("CTE_DOUBLE");}
break;
case 42:
//#line 80 "gramatica.y"
{yyout("LOOP");}
break;
case 43:
//#line 81 "gramatica.y"
{yyerror("Cuerpo LOOP vacio");}
break;
case 44:
//#line 84 "gramatica.y"
{yyout("UNTIL");}
break;
case 45:
//#line 87 "gramatica.y"
{yyout("IF-THEN-ELSE");}
break;
case 46:
//#line 88 "gramatica.y"
{yyout("IF-THEN");}
break;
case 47:
//#line 89 "gramatica.y"
{yyerror("Falta palabra clave END_IF");}
break;
case 48:
//#line 90 "gramatica.y"
{yyerror("Falta palabra clave END_IF");}
break;
case 50:
//#line 94 "gramatica.y"
{yyerror("Falta palabra clave IF");}
break;
case 51:
//#line 97 "gramatica.y"
{yyout("THEN bloque_estruct_ctrl");}
break;
case 52:
//#line 98 "gramatica.y"
{yyerror("Cuerpo THEN vacio");}
break;
case 53:
//#line 99 "gramatica.y"
{yyerror("Falta palabra clave THEN");}
break;
case 54:
//#line 102 "gramatica.y"
{yyout("ELSE bloque_estruct_ctrl");}
break;
case 55:
//#line 103 "gramatica.y"
{yyerror("Cuerpo ELSE vacio");}
break;
case 58:
//#line 110 "gramatica.y"
{yyerror("Condicion vacia");}
break;
case 67:
//#line 125 "gramatica.y"
{yyout("Print_CADENA");}
break;
case 68:
//#line 126 "gramatica.y"
{yyout("Print_UINT");}
break;
case 69:
//#line 127 "gramatica.y"
{yyout("Print_DOUBLE");}
break;
case 70:
//#line 128 "gramatica.y"
{yyout("Print_ID");}
break;
//#line 629 "Parser.java"
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
private AnalizadorLexico aLexico;
  /**
   * Default constructor.  Turn off with -Jnoconstruct .
   */
  public Parser() {
    //nothing to do
  }
  /**
   * Create a parser, setting the debug to true or false.
   *
   * @param debugMe true for debugging, false for no debug.
   */
  public Parser(boolean debugMe, AnalizadorLexico aLexico) {
    yydebug = debugMe;
    this.aLexico = aLexico;
  }

  private int yylex(){
    return aLexico.yylex();
  }

  private void yyout(String mensaje){
    System.out.println(mensaje);
  }

  private void yyerror(String mensajeError){
    System.err.println("Error en la linea "+aLexico.getLineaActual()+": "+mensajeError);
  }

//###############################################################



}
//################### END OF CLASS ##############################
