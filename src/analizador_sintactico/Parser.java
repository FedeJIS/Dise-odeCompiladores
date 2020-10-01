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
            8, 8, 7, 7, 6, 6, 4, 4, 4, 4,
            4, 14, 14, 14, 15, 15, 15, 16, 16, 16,
            9, 10, 17, 18, 18, 18, 18, 18, 18, 11,
            11, 19, 19, 12, 13,
    };
    final static short[] yylen = {2,
            1, 1, 2, 1, 1, 11, 3, 1, 3, 5,
            3, 2, 3, 1, 1, 1, 2, 1, 1, 2,
            2, 3, 3, 1, 3, 3, 1, 1, 1, 1,
            3, 6, 5, 1, 1, 1, 1, 1, 1, 5,
            7, 1, 3, 4, 4,
    };
    final static short[] yydefred = {0,
            0, 15, 16, 0, 0, 0, 0, 0, 1, 0,
            4, 5, 0, 0, 18, 19, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 3, 0, 0, 17, 20,
            21, 0, 28, 29, 30, 0, 0, 27, 0, 0,
            0, 0, 0, 0, 7, 44, 0, 0, 0, 0,
            35, 34, 39, 38, 36, 37, 0, 0, 42, 0,
            0, 45, 0, 0, 0, 0, 13, 0, 0, 25,
            26, 0, 0, 0, 40, 0, 0, 0, 12, 0,
            33, 43, 0, 32, 11, 0, 0, 41, 0, 0,
            0, 10, 0, 0, 6,
    };
    final static short[] yydgoto = {8,
            9, 10, 11, 12, 64, 13, 28, 66, 14, 15,
            16, 17, 18, 36, 37, 38, 22, 57, 60,
    };
    final static short[] yysindex = {-216,
            -13, 0, 0, -32, -91, 4, -200, 0, 0, -216,
            0, 0, -198, 2, 0, 0, 8, 10, -198, -241,
            -241, -186, -216, -187, 41, 0, 38, 24, 0, 0,
            0, 43, 0, 0, 0, -7, 16, 0, -23, -122,
            -40, 45, -211, -198, 0, 0, -241, -241, -241, -241,
            0, 0, 0, 0, 0, 0, -241, -216, 0, -196,
            -183, 0, -189, 47, -168, 46, 0, 16, 16, 0,
            0, 25, -34, -122, 0, -32, -165, -181, 0, -211,
            0, 0, -174, 0, 0, 34, 52, 0, -178, -211,
            -25, 0, -216, -24, 0,
    };
    final static short[] yyrindex = {0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 3,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, -16, 0, 0, 0,
            0, 0, 0, 0, 0, 10, -41, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 58, 0, -36, -31, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 61, 0, 0, 0,
            0, 0, 0, 0, 0,
    };
    final static short[] yygindex = {0,
            7, -29, 0, 0, 0, -30, -4, -26, 0, 0,
            0, 0, 0, -15, 28, 29, 27, 0, 30,
    };
    final static int YYTABLESIZE = 238;
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
            "bloque_sentencias : sentencia",
            "bloque_sentencias : sentencia bloque_sentencias",
            "sentencia : sentencia_declarativa",
            "sentencia : sentencia_ejecutable",
            "sentencia_declarativa : PROC ID '(' lista_parametros ')' NI '=' CTE_UINT '{' bloque_sentencias '}'",
            "sentencia_declarativa : tipo lista_variables ';'",
            "lista_parametros : parametro",
            "lista_parametros : parametro ',' parametro",
            "lista_parametros : parametro ',' parametro ',' parametro",
            "parametro : VAR tipo ID",
            "parametro : tipo ID",
            "lista_variables : ID ',' lista_variables",
            "lista_variables : ID",
            "tipo : UINT",
            "tipo : DOUBLE",
            "sentencia_ejecutable : asignacion ';'",
            "sentencia_ejecutable : sentencia_loop",
            "sentencia_ejecutable : sentencia_if",
            "sentencia_ejecutable : invocacion ';'",
            "sentencia_ejecutable : print ';'",
            "expresion : expresion '+' termino",
            "expresion : expresion '-' termino",
            "expresion : termino",
            "termino : termino '*' factor",
            "termino : termino '/' factor",
            "termino : factor",
            "factor : ID",
            "factor : CTE_UINT",
            "factor : CTE_DOUBLE",
            "asignacion : ID '=' expresion",
            "sentencia_loop : LOOP '{' bloque_sentencias '}' UNTIL condicion",
            "condicion : '(' expresion comparador expresion ')'",
            "comparador : COMP_MAYOR_IGUAL",
            "comparador : COMP_MENOR_IGUAL",
            "comparador : '<'",
            "comparador : '>'",
            "comparador : COMP_IGUAL",
            "comparador : COMP_DISTINTO",
            "sentencia_if : IF condicion THEN bloque_condicional END_IF",
            "sentencia_if : IF condicion THEN bloque_condicional ELSE bloque_condicional END_IF",
            "bloque_condicional : sentencia",
            "bloque_condicional : '{' bloque_sentencias '}'",
            "invocacion : ID '(' lista_variables ')'",
            "print : OUT '(' CADENA ')'",
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

    static void yytable() {
        yytable = new short[]{24,
                58, 24, 2, 24, 22, 39, 22, 21, 22, 23,
                59, 23, 65, 23, 32, 33, 26, 24, 24, 47,
                24, 48, 22, 22, 14, 22, 19, 23, 23, 41,
                23, 23, 77, 34, 35, 47, 55, 48, 56, 67,
                1, 72, 14, 24, 59, 2, 3, 20, 4, 65,
                2, 3, 5, 87, 6, 7, 25, 49, 27, 65,
                29, 63, 50, 92, 73, 81, 30, 47, 31, 48,
                74, 75, 2, 3, 68, 69, 42, 70, 71, 40,
                43, 44, 45, 46, 61, 62, 76, 78, 79, 80,
                82, 85, 86, 88, 89, 90, 91, 93, 8, 94,
                95, 9, 84, 83, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 2, 0, 0,
                0, 0, 0, 0, 1, 0, 0, 0, 0, 2,
                3, 0, 4, 0, 0, 0, 5, 0, 6, 7,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 24, 24, 24, 24,
                0, 22, 22, 22, 22, 0, 23, 23, 23, 23,
                0, 0, 0, 0, 51, 52, 53, 54,
        };
    }

    static void yycheck() {
        yycheck = new short[]{41,
                123, 43, 0, 45, 41, 21, 43, 40, 45, 41,
                40, 43, 43, 45, 19, 257, 10, 59, 60, 43,
                62, 45, 59, 60, 41, 62, 40, 59, 60, 23,
                62, 123, 63, 275, 276, 43, 60, 45, 62, 44,
                257, 57, 59, 40, 74, 262, 263, 61, 265, 80,
                262, 263, 269, 80, 271, 272, 257, 42, 257, 90,
                59, 273, 47, 90, 58, 41, 59, 43, 59, 45,
                267, 268, 262, 263, 47, 48, 264, 49, 50, 266,
                40, 44, 59, 41, 125, 41, 270, 41, 257, 44,
                125, 257, 274, 268, 61, 44, 275, 123, 41, 93,
                125, 41, 76, 74, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, 125, -1, -1,
                -1, -1, -1, -1, 257, -1, -1, -1, -1, 262,
                263, -1, 265, -1, -1, -1, 269, -1, 271, 272,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, 258, 259, 260, 261,
                -1, 258, 259, 260, 261, -1, 258, 259, 260, 261,
                -1, -1, -1, -1, 258, 259, 260, 261,
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
                case 1:
//#line 7 "E:\Bruno\facultad\disenio_compiladores\compilador_g15\src\analizador_sintactico\gramatica.y"
                {
                }
                break;
                case 2:
//#line 10 "E:\Bruno\facultad\disenio_compiladores\compilador_g15\src\analizador_sintactico\gramatica.y"
                {
                }
                break;
                case 3:
//#line 11 "E:\Bruno\facultad\disenio_compiladores\compilador_g15\src\analizador_sintactico\gramatica.y"
                {
                }
                break;
                case 4:
//#line 14 "E:\Bruno\facultad\disenio_compiladores\compilador_g15\src\analizador_sintactico\gramatica.y"
                {
                }
                break;
                case 5:
//#line 15 "E:\Bruno\facultad\disenio_compiladores\compilador_g15\src\analizador_sintactico\gramatica.y"
                {
                }
                break;
                case 6:
//#line 18 "E:\Bruno\facultad\disenio_compiladores\compilador_g15\src\analizador_sintactico\gramatica.y"
                {
                    yyout("Declaracion procedimiento.");
                }
                break;
                case 7:
//#line 19 "E:\Bruno\facultad\disenio_compiladores\compilador_g15\src\analizador_sintactico\gramatica.y"
                {
                }
                break;
                case 8:
//#line 22 "E:\Bruno\facultad\disenio_compiladores\compilador_g15\src\analizador_sintactico\gramatica.y"
                {
                }
                break;
                case 9:
//#line 23 "E:\Bruno\facultad\disenio_compiladores\compilador_g15\src\analizador_sintactico\gramatica.y"
                {
                }
                break;
                case 10:
//#line 24 "E:\Bruno\facultad\disenio_compiladores\compilador_g15\src\analizador_sintactico\gramatica.y"
                {
                }
                break;
                case 11:
//#line 27 "E:\Bruno\facultad\disenio_compiladores\compilador_g15\src\analizador_sintactico\gramatica.y"
                {
                    yyout("Parametro VAR, ");
                }
                break;
                case 12:
//#line 28 "E:\Bruno\facultad\disenio_compiladores\compilador_g15\src\analizador_sintactico\gramatica.y"
                {
                    yyout("Parametro, ");
                }
                break;
                case 13:
//#line 31 "E:\Bruno\facultad\disenio_compiladores\compilador_g15\src\analizador_sintactico\gramatica.y"
                {
                }
                break;
                case 14:
//#line 32 "E:\Bruno\facultad\disenio_compiladores\compilador_g15\src\analizador_sintactico\gramatica.y"
                {
                    yyout("Variable");
                }
                break;
                case 15:
//#line 35 "E:\Bruno\facultad\disenio_compiladores\compilador_g15\src\analizador_sintactico\gramatica.y"
                {
                    yyout("UINT ");
                }
                break;
                case 16:
//#line 36 "E:\Bruno\facultad\disenio_compiladores\compilador_g15\src\analizador_sintactico\gramatica.y"
                {
                    yyout("DOUBLE ");
                }
                break;
                case 17:
//#line 39 "E:\Bruno\facultad\disenio_compiladores\compilador_g15\src\analizador_sintactico\gramatica.y"
                {
                }
                break;
                case 18:
//#line 40 "E:\Bruno\facultad\disenio_compiladores\compilador_g15\src\analizador_sintactico\gramatica.y"
                {
                }
                break;
                case 19:
//#line 41 "E:\Bruno\facultad\disenio_compiladores\compilador_g15\src\analizador_sintactico\gramatica.y"
                {
                }
                break;
                case 20:
//#line 42 "E:\Bruno\facultad\disenio_compiladores\compilador_g15\src\analizador_sintactico\gramatica.y"
                {
                }
                break;
                case 21:
//#line 43 "E:\Bruno\facultad\disenio_compiladores\compilador_g15\src\analizador_sintactico\gramatica.y"
                {
                }
                break;
                case 22:
//#line 46 "E:\Bruno\facultad\disenio_compiladores\compilador_g15\src\analizador_sintactico\gramatica.y"
                {
                }
                break;
                case 23:
//#line 47 "E:\Bruno\facultad\disenio_compiladores\compilador_g15\src\analizador_sintactico\gramatica.y"
                {
                }
                break;
                case 24:
//#line 48 "E:\Bruno\facultad\disenio_compiladores\compilador_g15\src\analizador_sintactico\gramatica.y"
                {
                }
                break;
                case 25:
//#line 51 "E:\Bruno\facultad\disenio_compiladores\compilador_g15\src\analizador_sintactico\gramatica.y"
                {
                }
                break;
                case 26:
//#line 52 "E:\Bruno\facultad\disenio_compiladores\compilador_g15\src\analizador_sintactico\gramatica.y"
                {
                }
                break;
                case 27:
//#line 53 "E:\Bruno\facultad\disenio_compiladores\compilador_g15\src\analizador_sintactico\gramatica.y"
                {
                }
                break;
                case 28:
//#line 56 "E:\Bruno\facultad\disenio_compiladores\compilador_g15\src\analizador_sintactico\gramatica.y"
                {
                    yyout("ID");
                }
                break;
                case 29:
//#line 57 "E:\Bruno\facultad\disenio_compiladores\compilador_g15\src\analizador_sintactico\gramatica.y"
                {
                    yyout("CTE_UINT");
                }
                break;
                case 30:
//#line 58 "E:\Bruno\facultad\disenio_compiladores\compilador_g15\src\analizador_sintactico\gramatica.y"
                {
                    yyout("CTE_DOUBLE");
                }
                break;
                case 31:
//#line 61 "E:\Bruno\facultad\disenio_compiladores\compilador_g15\src\analizador_sintactico\gramatica.y"
                {
                    yyout("Asignacion");
                }
                break;
                case 32:
//#line 64 "E:\Bruno\facultad\disenio_compiladores\compilador_g15\src\analizador_sintactico\gramatica.y"
                {
                    yyout("Loop");
                }
                break;
                case 33:
//#line 67 "E:\Bruno\facultad\disenio_compiladores\compilador_g15\src\analizador_sintactico\gramatica.y"
                {
                }
                break;
                case 34:
//#line 70 "E:\Bruno\facultad\disenio_compiladores\compilador_g15\src\analizador_sintactico\gramatica.y"
                {
                }
                break;
                case 35:
//#line 71 "E:\Bruno\facultad\disenio_compiladores\compilador_g15\src\analizador_sintactico\gramatica.y"
                {
                }
                break;
                case 36:
//#line 72 "E:\Bruno\facultad\disenio_compiladores\compilador_g15\src\analizador_sintactico\gramatica.y"
                {
                }
                break;
                case 37:
//#line 73 "E:\Bruno\facultad\disenio_compiladores\compilador_g15\src\analizador_sintactico\gramatica.y"
                {
                }
                break;
                case 38:
//#line 74 "E:\Bruno\facultad\disenio_compiladores\compilador_g15\src\analizador_sintactico\gramatica.y"
                {
                }
                break;
                case 39:
//#line 75 "E:\Bruno\facultad\disenio_compiladores\compilador_g15\src\analizador_sintactico\gramatica.y"
                {
                }
                break;
                case 40:
//#line 78 "E:\Bruno\facultad\disenio_compiladores\compilador_g15\src\analizador_sintactico\gramatica.y"
                {
                    yyout("IF-THEN");
                }
                break;
                case 41:
//#line 79 "E:\Bruno\facultad\disenio_compiladores\compilador_g15\src\analizador_sintactico\gramatica.y"
                {
                    yyout("IF-THEN-ELSE");
                }
                break;
                case 42:
//#line 82 "E:\Bruno\facultad\disenio_compiladores\compilador_g15\src\analizador_sintactico\gramatica.y"
                {
                }
                break;
                case 43:
//#line 83 "E:\Bruno\facultad\disenio_compiladores\compilador_g15\src\analizador_sintactico\gramatica.y"
                {
                }
                break;
                case 44:
//#line 86 "E:\Bruno\facultad\disenio_compiladores\compilador_g15\src\analizador_sintactico\gramatica.y"
                {
                    yyout("Invocacion");
                }
                break;
                case 45:
//#line 89 "E:\Bruno\facultad\disenio_compiladores\compilador_g15\src\analizador_sintactico\gramatica.y"
                {
                    yyout("Print");
                }
                break;
//#line 625 "Parser.java"
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

    private void yyout(String msg) {
        System.out.println(msg);
    }

    private void yyerror(String syntaxError) {
        System.err.println(syntaxError);
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
