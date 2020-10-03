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
    4,    4,    4,    9,    9,   10,   14,   14,   14,   15,
   15,   15,   16,   16,   16,   16,   11,   12,   12,   12,
   12,   19,   19,   20,   20,   20,   21,   21,   17,   17,
   18,   18,   22,   22,   22,   22,   22,   22,   13,   23,
   23,   23,   23,
};
final static short yylen[] = {                            2,
    1,    1,    2,    3,    1,    1,   11,    2,    0,    1,
    3,    5,    3,    2,    1,    1,    1,    3,    1,    1,
    1,    1,    1,    3,    4,    3,    3,    3,    1,    3,
    3,    1,    1,    1,    1,    2,    4,    4,    3,    3,
    2,    2,    1,    2,    1,    1,    2,    1,    2,    3,
    2,    5,    1,    1,    1,    1,    1,    1,    4,    1,
    1,    1,    1,
};
final static short yydefred[] = {                         0,
    0,   15,   16,    0,    0,    0,    0,    0,    0,    1,
    0,    5,    6,    0,   19,   20,   21,   22,   23,   43,
    0,    0,    0,   42,    0,    0,    0,    0,    0,   33,
   34,   35,   51,    0,    0,    0,   32,    0,    0,    8,
    0,   46,    0,   24,    0,    0,    0,   49,    0,   63,
   60,   61,   62,    0,    0,   36,   54,   53,   58,   57,
    0,    0,   55,   56,    0,    0,    0,    4,    0,   44,
    0,   39,    0,   25,   50,   37,   59,    0,    0,    0,
    0,    0,    0,    0,   30,   31,   18,   47,   38,    0,
    0,   14,    0,   52,   13,    0,    0,    0,    0,    0,
   12,    0,    0,    7,
};
final static short yydgoto[] = {                          9,
   10,   11,   12,   13,   79,   14,   40,   81,   15,   16,
   17,   18,   19,   35,   36,   37,   27,   20,   21,   43,
   73,   65,   54,
};
final static short yysindex[] = {                        29,
  -24,    0,    0,  -22,  -20,  -18, -218,  -41,    0,    0,
  -14,    0,    0, -216,    0,    0,    0,    0,    0,    0,
  -33,  -36,  -35,    0,   29,  -12, -220, -240,   40,    0,
    0,    0,    0,  -35,   -5,  -21,    0,   29,   45,    0,
  -20,    0, -234,    0,   52,  -15,  -39,    0,  -22,    0,
    0,    0,    0,   54, -175,    0,    0,    0,    0,    0,
  -35,  -35,    0,    0,  -35,  -35,  -35,    0, -216,    0,
  -20,    0, -169,    0,    0,    0,    0, -210,   61, -153,
   62,  -21,  -21,   51,    0,    0,    0,    0,    0, -152,
 -167,    0, -175,    0,    0,   47,   68, -162, -175,   -9,
    0,   29,  -10,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    6,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    3,    0,    9,    8,    0,
    1,    0,   11,    0,    0,   12,    0,    0,    0,    0,
    0,    0,    0,    0,   75,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    2,    0,   15,    0,    0,    0,    0,    0,    0,    0,
   76,   13,   23,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   77,    0,    0,    0,
    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  -11,   38,    0,    0,    0,   42,    7,  -74,    0,    0,
    0,    0,    0,   19,   39,   44,   10,   28,    0,    0,
    0,    0,    0,
};
final static int YYTABLESIZE=301;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         33,
   45,   48,   29,   34,   44,    2,    8,   17,    3,   34,
   41,   26,   27,   47,   40,   22,   50,    8,   97,    8,
   66,   28,   28,   51,  101,   67,   68,   61,   45,   62,
   42,   24,   71,   72,   52,   53,   23,   61,   29,   62,
   39,   46,   26,   29,   38,   29,   48,   29,   17,   49,
   70,    2,    3,   27,   63,   27,   64,   27,   26,   45,
   48,   29,   29,   28,   29,   28,   17,   28,    8,   41,
   26,   27,   27,   40,   27,   87,   76,   56,   26,   55,
   88,   28,   28,   84,   28,   75,    2,    3,   69,   25,
  103,   94,   74,   61,   77,   62,   80,   78,   89,   82,
   83,   91,   25,   92,   95,   93,   96,   98,   26,   85,
   86,   99,  100,  102,  104,    9,   10,   11,    0,   90,
    0,    0,    0,    0,    0,   45,   48,   29,    0,    0,
    2,    0,   17,    3,   80,   41,   26,   27,    0,   40,
   80,    0,    0,    0,    0,    0,    0,   28,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   30,    0,    0,    0,    0,
   39,   30,    0,    1,    0,    0,    0,    0,    2,    3,
    0,    4,   41,   31,   32,    5,    1,    6,    7,   31,
   32,    2,    3,    0,    4,    0,    0,    0,    5,    0,
    6,    7,   57,   58,   59,   60,    0,    0,    0,    0,
   29,   29,   29,   29,    0,    0,    0,   45,   45,   48,
   27,   27,   27,   27,    0,    0,    0,    0,    0,    0,
   28,   28,   28,   28,    0,    1,    0,    0,    0,    0,
    2,    3,    0,    4,    0,    0,    0,    5,    0,    6,
    7,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
    0,    0,    0,   45,   41,    0,   40,    0,    0,   45,
    0,    0,    0,   25,    0,   40,  257,   40,   93,   40,
   42,   40,    0,  264,   99,   47,   38,   43,   22,   45,
   21,    4,  267,  268,  275,  276,   61,   43,  257,   45,
  257,   23,    5,   41,   59,   43,   59,   45,   41,  270,
   41,  262,  263,   41,   60,   43,   62,   45,   21,   59,
   59,   59,   60,   41,   62,   43,   59,   45,   40,   59,
   59,   59,   60,   59,   62,   69,   49,   34,   41,   40,
   71,   59,   60,   65,   62,  125,  262,  263,   44,  123,
  102,   41,   41,   43,   41,   45,   55,  273,  268,   61,
   62,   41,  123,  257,  257,   44,  274,   61,   71,   66,
   67,   44,  275,  123,  125,   41,   41,   41,   -1,   78,
   -1,   -1,   -1,   -1,   -1,  125,  125,  125,   -1,   -1,
  125,   -1,  125,  125,   93,  125,  125,  125,   -1,  125,
   99,   -1,   -1,   -1,   -1,   -1,   -1,  125,   -1,   -1,
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
"invocacion : ID '(' ')'",
"invocacion : ID '(' lista_variables ')'",
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
"sentencia_loop : LOOP bloque_estruct_ctrl UNTIL condicion",
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
        yychar = aLexico.yylex();  //get next token
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
case 24:
//#line 48 "gramatica.y"
{yyout("Invocacion_Vacia");}
break;
case 25:
//#line 49 "gramatica.y"
{yyout("Invocacion");}
break;
case 26:
//#line 52 "gramatica.y"
{yyout("Asignacion");}
break;
case 33:
//#line 65 "gramatica.y"
{yyout("ID");}
break;
case 34:
//#line 66 "gramatica.y"
{yyout("CTE_UINT");}
break;
case 35:
//#line 67 "gramatica.y"
{yyout("CTE_DOUBLE");}
break;
case 37:
//#line 71 "gramatica.y"
{yyout("LOOP");}
break;
case 38:
//#line 74 "gramatica.y"
{yyout("IF-THEN-ELSE");}
break;
case 39:
//#line 75 "gramatica.y"
{yyout("IF-THEN");}
break;
case 40:
//#line 76 "gramatica.y"
{yyerror("Falta palabra clave END_IF");}
break;
case 41:
//#line 77 "gramatica.y"
{yyerror("Falta palabra clave END_IF");}
break;
case 42:
//#line 80 "gramatica.y"
{yyout("IF(condicion)");}
break;
case 43:
//#line 81 "gramatica.y"
{yyerror("Falta palabra clave IF");}
break;
case 44:
//#line 84 "gramatica.y"
{yyout("THEN bloque_estruct_ctrl");}
break;
case 45:
//#line 85 "gramatica.y"
{yyerror("Cuerpo THEN vacio");}
break;
case 46:
//#line 86 "gramatica.y"
{yyerror("Falta palabra clave THEN");}
break;
case 47:
//#line 89 "gramatica.y"
{yyout("ELSE bloque_estruct_ctrl");}
break;
case 48:
//#line 90 "gramatica.y"
{yyerror("Cuerpo ELSE vacio");}
break;
case 51:
//#line 97 "gramatica.y"
{yyout("Falta condicion");}
break;
case 60:
//#line 112 "gramatica.y"
{yyout("Print_CADENA");}
break;
case 61:
//#line 113 "gramatica.y"
{yyout("Print_UINT");}
break;
case 62:
//#line 114 "gramatica.y"
{yyout("Print_DOUBLE");}
break;
case 63:
//#line 115 "gramatica.y"
{yyout("Print_ID");}
break;
//#line 606 "Parser.java"
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
        yychar = aLexico.yylex();        //get next character
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

  private void yyout(String mensaje){
    System.out.println(mensaje);
  }

  private void yyerror(String mensajeError){
    System.out.println("Error en la linea "+aLexico.getLineaActual()+": "+mensajeError);
  }


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public Parser(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################
