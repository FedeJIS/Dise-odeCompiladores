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

package analizador_sintactico;

import analizador_lexico.AnalizadorLexico;

public class Parser {

    //#### end semantic value section ####
    public final static short ID = 257;
    public final static short COMP_MENOR_IGUAL = 258;
    public final static short COMP_MAYOR_IGUAL = 259;
    public final static short COMP_DISTINTO = 260;
    public final static short COMP_IGUAL = 261;
    public final static short UINT = 262;
    public final static short DOUBLE = 263;
    public final static short CADENA = 264;
    public final static short IF = 265;
    public final static short THEN = 266;
    public final static short ELSE = 267;
    public final static short END_IF = 268;
    public final static short LOOP = 269;
    public final static short UNTIL = 270;
    public final static short OUT = 271;
    public final static short PROC = 272;


//########## SEMANTIC VALUES ##########
//public class ParserVal is defined in ParserVal.java
    public final static short VAR = 273;
    public final static short NI = 274;
    public final static short CTE_UINT = 275;
    public final static short CTE_DOUBLE = 276;
    public final static short YYERRCODE = 256;
    //########## STATE STACK ##########
    final static int YYSTACKSIZE = 500;  //maximum stack size
    final static short[] yylhs = {-1,
            0, 1, 1, 2, 2, 3, 3, 5, 5, 5,
            5, 8, 8, 6, 6, 7, 7, 4, 4, 4,
            4, 4, 9, 9, 10, 14, 14, 14, 15, 15,
            15, 16, 16, 16, 16, 11, 12, 12, 17, 17,
            18, 19, 19, 19, 19, 19, 19, 13, 20, 20,
            20, 20,
    };
    final static short[] yylen = {2,
            1, 2, 3, 1, 1, 11, 2, 0, 1, 3,
            5, 3, 2, 1, 1, 1, 3, 1, 1, 1,
            1, 1, 3, 4, 3, 3, 3, 1, 3, 3,
            1, 1, 1, 1, 2, 4, 5, 7, 2, 3,
            5, 1, 1, 1, 1, 1, 1, 4, 1, 1,
            1, 1,
    };
    final static short[] yydefred = {0,
            0, 14, 15, 0, 0, 0, 0, 0, 1, 0,
            4, 5, 0, 18, 19, 20, 21, 22, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 7,
            23, 0, 32, 33, 34, 0, 0, 0, 31, 0,
            0, 0, 39, 0, 52, 49, 50, 51, 0, 0,
            3, 0, 24, 35, 0, 0, 0, 0, 43, 42,
            47, 46, 44, 45, 0, 0, 40, 36, 48, 0,
            0, 0, 0, 17, 0, 0, 29, 30, 0, 0,
            37, 0, 0, 13, 0, 41, 0, 12, 0, 0,
            38, 0, 0, 0, 11, 0, 0, 6,
    };
    final static short[] yydgoto = {8,
            9, 10, 11, 12, 71, 13, 30, 73, 14, 15,
            16, 17, 18, 37, 38, 39, 25, 22, 65, 49,
    };
    final static short[] yysindex = {-202,
            -9, 0, 0, 19, -121, 22, -239, 0, 0, 7,
            0, 0, -189, 0, 0, 0, 0, 0, -30, -45,
            -45, -195, -202, 14, -184, -218, 41, -202, 43, 0,
            0, 47, 0, 0, 0, -45, -8, -6, 0, -22,
            -121, -36, 0, 19, 0, 0, 0, 0, 49, -198,
            0, -189, 0, 0, -45, -45, -45, -45, 0, 0,
            0, 0, 0, 0, -45, -190, 0, 0, 0, -183,
            50, -165, 51, 0, -6, -6, 0, 0, 31, -121,
            0, -164, -180, 0, -198, 0, -172, 0, 36, 54,
            0, -176, -198, -23, 0, -202, -24, 0,
    };
    final static short[] yyrindex = {0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 4, -27, 0,
            0, 0, 0, 0, 0, 0, 44, -40, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 61,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 63, 0, -35, -15, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 64,
            0, 0, 0, 0, 0, 0, 0, 0,
    };
    final static short[] yygindex = {0,
            -11, 2, 0, 0, 0, -37, -3, -51, 0, 0,
            0, 0, 0, -12, 28, -7, -26, 62, 0, 0,
    };
    final static int YYTABLESIZE = 246;
    final static short YYFINAL = 8;
    final static short YYMAXTOKEN = 276;
    final static String[] yyname = {
            "end-of-file", null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, "'('", "')'", "'*'", "'+'", "','",
            "'-'", null, "'/'", null, null, null, null, null, null, null, null, null, null, null, "';'",
            "'<'", "'='", "'>'", null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            "'{'", null, "'}'", null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, "ID", "COMP_MENOR_IGUAL", "COMP_MAYOR_IGUAL",
            "COMP_DISTINTO", "COMP_IGUAL", "UINT", "DOUBLE", "CADENA", "IF", "THEN", "ELSE",
            "END_IF", "LOOP", "UNTIL", "OUT", "PROC", "VAR", "NI", "CTE_UINT", "CTE_DOUBLE",
    };
    final static String[] yyrule = {
            "$accept : programa",
            "programa : bloque_sentencias",
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
            "sentencia_if : IF condicion THEN bloque_estruct_ctrl END_IF",
            "sentencia_if : IF condicion THEN bloque_estruct_ctrl ELSE bloque_estruct_ctrl END_IF",
            "bloque_estruct_ctrl : sentencia ';'",
            "bloque_estruct_ctrl : '{' bloque_sentencias '}'",
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
    static short[] yytable;
    static short[] yycheck;

    static {
        yytable();
    }

    static {
        yycheck();
    }

    boolean yydebug;        //do I want debug output?
    int yynerrs;            //number of errors so far
    int yyerrflag;          //was there an error?
    int yychar;             //the current working character
    int[] statestk = new int[YYSTACKSIZE]; //state stack
    int stateptr;
    int stateptrmax;                     //highest index of stackptr
    int statemax;                        //state when highest index reached
    String yytext;//user variable to return contextual strings
    ParserVal yyval; //used to return semantic vals from action routines
    ParserVal yylval;//the 'lval' (result) I got from yylex()
    ParserVal[] valstk;
    int valptr;
    //The following are now global, to aid in error reporting
    int yyn;       //next next thing to do
    int yym;       //
    int yystate;   //current parsing state from state table
    String yys;    //current token string
    /**
     * Default constructor.  Turn off with -Jnoconstruct .
     */
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

  private void yyout(String mensaje) {
    System.out.println(mensaje);
  }

  private void yyerror(String syntaxError) {
    System.err.println(syntaxError);
  }

    static void yytable() {
        yytable = new short[]{36,
                28, 23, 28, 2, 28, 26, 24, 26, 40, 26,
                31, 42, 72, 16, 66, 32, 51, 27, 28, 28,
                55, 28, 56, 26, 26, 27, 26, 27, 54, 27,
                19, 16, 82, 90, 55, 57, 56, 63, 45, 64,
                58, 95, 24, 27, 27, 46, 27, 72, 74, 77,
                78, 20, 79, 87, 1, 72, 47, 48, 21, 2,
                3, 26, 4, 2, 3, 28, 5, 29, 6, 7,
                41, 86, 43, 55, 70, 56, 80, 81, 2, 3,
                50, 24, 75, 76, 97, 44, 52, 53, 67, 69,
                83, 84, 88, 89, 85, 91, 92, 93, 94, 96,
                98, 8, 25, 9, 10, 68, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 2, 0,
                0, 0, 0, 0, 0, 1, 0, 0, 0, 0,
                2, 3, 0, 4, 0, 0, 0, 5, 0, 6,
                7, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 33, 0, 0, 0, 0, 0, 28, 28, 28,
                28, 0, 26, 26, 26, 26, 29, 0, 0, 34,
                35, 0, 0, 0, 0, 59, 60, 61, 62, 0,
                0, 0, 27, 27, 27, 27,
        };
    }

    static void yycheck() {
        yycheck = new short[]{45,
                41, 123, 43, 0, 45, 41, 5, 43, 21, 45,
                41, 23, 50, 41, 41, 19, 28, 257, 59, 60,
                43, 62, 45, 59, 60, 41, 62, 43, 36, 45,
                40, 59, 70, 85, 43, 42, 45, 60, 257, 62,
                47, 93, 41, 59, 60, 264, 62, 85, 52, 57,
                58, 61, 65, 80, 257, 93, 275, 276, 40, 262,
                263, 40, 265, 262, 263, 59, 269, 257, 271, 272,
                266, 41, 59, 43, 273, 45, 267, 268, 262, 263,
                40, 80, 55, 56, 96, 270, 44, 41, 125, 41,
                41, 257, 257, 274, 44, 268, 61, 44, 275, 123,
                125, 41, 59, 41, 41, 44, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, 125, -1,
                -1, -1, -1, -1, -1, 257, -1, -1, -1, -1,
                262, 263, -1, 265, -1, -1, -1, 269, -1, 271,
                272, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, 257, -1, -1, -1, -1, -1, 258, 259, 260,
                261, -1, 258, 259, 260, 261, 257, -1, -1, 275,
                276, -1, -1, -1, -1, 258, 259, 260, 261, -1,
                -1, -1, 258, 259, 260, 261,
        };
    }

    //########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
    void debug(String msg) {
        if (yydebug)
            System.out.println(msg);
    }

    //###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
    final void state_push(int state) {
        try {
            stateptr++;
            statestk[stateptr] = state;
        } catch (ArrayIndexOutOfBoundsException e) {
            int oldsize = statestk.length;
            int newsize = oldsize * 2;
            int[] newstack = new int[newsize];
            System.arraycopy(statestk, 0, newstack, 0, oldsize);
            statestk = newstack;
            statestk[stateptr] = state;
        }
    }

    final int state_pop() {
        return statestk[stateptr--];
    }

    final void state_drop(int cnt) {
        stateptr -= cnt;
    }

    final int state_peek(int relative) {
        return statestk[stateptr - relative];
    }

    //###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
    final boolean init_stacks() {
        stateptr = -1;
        val_init();
        return true;
    }

    //###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
    void dump_stacks(int count) {
        int i;
        System.out.println("=index==state====value=     s:" + stateptr + "  v:" + valptr);
        for (i = 0; i < count; i++)
            System.out.println(" " + i + "    " + statestk[i] + "      " + valstk[i]);
        System.out.println("======================");
    }

    //###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
    void val_init() {
        valstk = new ParserVal[YYSTACKSIZE];
        yyval = new ParserVal();
        yylval = new ParserVal();
        valptr = -1;
    }

    void val_push(ParserVal val) {
        if (valptr >= YYSTACKSIZE)
            return;
        valstk[++valptr] = val;
    }

    ParserVal val_pop() {
        if (valptr < 0)
            return new ParserVal();
        return valstk[valptr--];
    }

    void val_drop(int cnt) {
        int ptr;
        ptr = valptr - cnt;
        if (ptr < 0)
            return;
        valptr = ptr;
    }

    ParserVal val_peek(int relative) {
        int ptr;
        ptr = valptr - relative;
        if (ptr < 0)
            return new ParserVal();
        return valstk[ptr];
    }

    final ParserVal dup_yyval(ParserVal val) {
        ParserVal dup = new ParserVal();
        dup.ival = val.ival;
        dup.dval = val.dval;
        dup.sval = val.sval;
        dup.obj = val.obj;
        return dup;
    }
//## end of method parse() ######################################


//## run() --- for Thread #######################################

    //###############################################################
// method: yylexdebug : check lexer state
//###############################################################
    void yylexdebug(int state, int ch) {
        String s = null;
        if (ch < 0) ch = 0;
        if (ch <= YYMAXTOKEN) //check index bounds
            s = yyname[ch];    //now get it
        if (s == null)
            s = "illegal-symbol";
        debug("state " + state + ", reading " + ch + " (" + s + ")");
    }
//## end of method run() ########################################


//## Constructors ###############################################

    //###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
    int yyparse() {
        boolean doaction;
        init_stacks();
        yynerrs = 0;
        yyerrflag = 0;
        yychar = -1;          //impossible char forces a read
        yystate = 0;            //initial state
        state_push(yystate);  //save it
        val_push(yylval);     //save empty value
        while (true) //until parsing is done, either correctly, or w/error
        {
            doaction = true;
            if (yydebug) debug("loop");
            //#### NEXT ACTION (from reduction table)
            for (yyn = yydefred[yystate]; yyn == 0; yyn = yydefred[yystate]) {
                if (yydebug) debug("yyn:" + yyn + "  state:" + yystate + "  yychar:" + yychar);
                if (yychar < 0)      //we want a char?
                {
                    yychar = aLexico.yylex();  //get next token
                    if (yydebug) debug(" next yychar:" + yychar);
                    //#### ERROR CHECK ####
                    if (yychar < 0)    //it it didn't work/error
                    {
                        yychar = 0;      //change it to default string (no -1!)
                        if (yydebug)
                            yylexdebug(yystate, yychar);
                    }
                }//yychar<0
                yyn = yysindex[yystate];  //get amount to shift by (shift index)
                if ((yyn != 0) && (yyn += yychar) >= 0 &&
                        yyn <= YYTABLESIZE && yycheck[yyn] == yychar) {
                    if (yydebug)
                        debug("state " + yystate + ", shifting to state " + yytable[yyn]);
                    //#### NEXT STATE ####
                    yystate = yytable[yyn];//we are in a new state
                    state_push(yystate);   //save it
                    val_push(yylval);      //push our lval as the input for next rule
                    yychar = -1;           //since we have 'eaten' a token, say we need another
                    if (yyerrflag > 0)     //have we recovered an error?
                        --yyerrflag;        //give ourselves credit
                    doaction = false;        //but don't process yet
                    break;   //quit the yyn=0 loop
                }

                yyn = yyrindex[yystate];  //reduce
                if ((yyn != 0) && (yyn += yychar) >= 0 &&
                        yyn <= YYTABLESIZE && yycheck[yyn] == yychar) {   //we reduced!
                    if (yydebug) debug("reduce");
                    yyn = yytable[yyn];
                    doaction = true; //get ready to execute
                    break;         //drop down to actions
                } else //ERROR RECOVERY
                {
                    if (yyerrflag == 0) {
                        yyerror("syntax error");
                        yynerrs++;
                    }
                    if (yyerrflag < 3) //low error count?
                    {
                        yyerrflag = 3;
                        while (true)   //do until break
                        {
                            if (stateptr < 0)   //check for under & overflow here
                            {
                                yyerror("stack underflow. aborting...");  //note lower case 's'
                                return 1;
                            }
                            yyn = yysindex[state_peek(0)];
                            if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE) {
                                if (yydebug)
                                    debug("state " + state_peek(0) + ", error recovery shifting to state " + yytable[yyn] + " ");
                                yystate = yytable[yyn];
                                state_push(yystate);
                                val_push(yylval);
                                doaction = false;
                                break;
                            } else {
                                if (yydebug)
                                    debug("error recovery discarding state " + state_peek(0) + " ");
                                if (stateptr < 0)   //check for under & overflow here
                                {
                                    yyerror("Stack underflow. aborting...");  //capital 'S'
                                    return 1;
                                }
                                state_pop();
                                val_pop();
                            }
                        }
                    } else            //discard this token
                    {
                        if (yychar == 0)
                            return 1; //yyabort
                        if (yydebug) {
                            yys = null;
                            if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
                            if (yys == null) yys = "illegal-symbol";
                            debug("state " + yystate + ", error recovery discards token " + yychar + " (" + yys + ")");
                        }
                        yychar = -1;  //read another
                    }
                }//end error recovery
            }//yyn=0 loop
            if (!doaction)   //any reason not to proceed?
                continue;      //skip action
            yym = yylen[yyn];          //get count of terminals on rhs
            if (yydebug)
                debug("state " + yystate + ", reducing " + yym + " by rule " + yyn + " (" + yyrule[yyn] + ")");
            if (yym > 0)                 //if count of rhs not 'nil'
                yyval = val_peek(yym - 1); //get current semantic value
            yyval = dup_yyval(yyval); //duplicate yyval if ParserVal is used as semantic value
            switch (yyn) {
//########## USER-SUPPLIED ACTIONS ##########
                case 6:
//#line 18 "gramatica.y"
                {
                    yyout("Declaracion procedimiento.");
                }
                break;
                case 12:
//#line 28 "gramatica.y"
                {
                    yyout("Parametro_VAR");
                }
                break;
                case 13:
//#line 29 "gramatica.y"
                {
                    yyout("Parametro");
                }
                break;
                case 14:
//#line 32 "gramatica.y"
                {
                    yyout("UINT");
                }
                break;
                case 15:
//#line 33 "gramatica.y"
                {
                    yyout("DOUBLE");
                }
                break;
                case 16:
//#line 36 "gramatica.y"
                {
                    yyout("Variable");
                }
                break;
                case 23:
//#line 47 "gramatica.y"
                {
                    yyout("Invocacion_Vacia");
                }
                break;
                case 24:
//#line 48 "gramatica.y"
                {
                    yyout("Invocacion");
                }
                break;
                case 25:
//#line 51 "gramatica.y"
                {
                    yyout("Asignacion");
                }
                break;
                case 32:
//#line 64 "gramatica.y"
                {
                    yyout("ID");
                }
                break;
                case 33:
//#line 65 "gramatica.y"
                {
                    yyout("CTE_UINT");
                }
                break;
                case 34:
//#line 66 "gramatica.y"
                {
                    yyout("CTE_DOUBLE");
                }
                break;
                case 36:
//#line 70 "gramatica.y"
                {
                    yyout("LOOP");
                }
                break;
                case 37:
//#line 73 "gramatica.y"
                {
                    yyout("IF-THEN");
                }
                break;
                case 38:
//#line 74 "gramatica.y"
                {
                    yyout("IF-THEN-ELSE");
                }
                break;
                case 49:
//#line 95 "gramatica.y"
                {
                    yyout("Print_CADENA");
                }
                break;
                case 50:
//#line 96 "gramatica.y"
                {
                    yyout("Print_UINT");
                }
                break;
                case 51:
//#line 97 "gramatica.y"
                {
                    yyout("Print_DOUBLE");
                }
                break;
                case 52:
//#line 98 "gramatica.y"
                {
                    yyout("Print_ID");
                }
                break;
//#line 532 "Parser.java"
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
                if (yydebug) debug("After reduction, shifting from state 0 to state " + YYFINAL + "");
                yystate = YYFINAL;         //explicitly say we're done
                state_push(YYFINAL);       //and save it
                val_push(yyval);           //also save the semantic value of parsing
                if (yychar < 0)            //we want another character?
                {
                    yychar = aLexico.yylex();        //get next character
                    if (yychar < 0) yychar = 0;  //clean, if necessary
                    if (yydebug)
                        yylexdebug(yystate, yychar);
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
                if (yydebug)
                    debug("after reduction, shifting from state " + state_peek(0) + " to state " + yystate + "");
                state_push(yystate);     //going again, so push state & val...
                val_push(yyval);         //for next action
            }
        }//main loop
        return 0;//yyaccept!!
    }

    /**
     * A default run method, used for operating this parser
     * object in the background.  It is intended for extending Thread
     * or implementing Runnable.  Turn off with -Jnorun .
     */
    public void run() {
        yyparse();
    }
//###############################################################


}
//################### END OF CLASS ##############################