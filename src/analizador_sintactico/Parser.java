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
            0, 1, 1, 1, 2, 2, 3, 3, 5, 5,
            6, 6, 7, 7, 7, 8, 11, 11, 11, 11,
            11, 11, 13, 13, 14, 14, 14, 15, 15, 15,
            9, 9, 10, 10, 4, 4, 4, 4, 4, 16,
            21, 21, 21, 21, 17, 17, 22, 22, 22, 23,
            23, 23, 24, 24, 24, 24, 18, 25, 25, 26,
            19, 19, 19, 19, 28, 28, 29, 29, 29, 30,
            30, 12, 12, 27, 27, 31, 31, 31, 31, 31,
            31, 20, 32, 32, 32, 32,
    };
    final static short[] yylen = {2,
            1, 1, 2, 3, 1, 1, 4, 2, 2, 1,
            3, 2, 3, 2, 0, 3, 0, 1, 3, 5,
            7, 9, 1, 1, 3, 2, 2, 2, 1, 1,
            1, 1, 1, 3, 1, 1, 1, 1, 1, 2,
            2, 3, 1, 2, 3, 2, 3, 3, 1, 3,
            3, 1, 1, 1, 1, 2, 2, 2, 1, 2,
            4, 3, 3, 2, 2, 1, 2, 1, 1, 2,
            1, 2, 3, 2, 5, 1, 1, 1, 1, 1,
            1, 4, 1, 1, 1, 1,
    };
    final static short[] yydefred = {0,
            0, 31, 32, 0, 0, 0, 0, 0, 0, 1,
            0, 5, 6, 0, 0, 35, 36, 37, 38, 39,
            0, 66, 0, 0, 0, 40, 65, 0, 0, 58,
            0, 9, 53, 54, 55, 74, 0, 0, 0, 52,
            0, 0, 0, 0, 8, 0, 57, 0, 69, 0,
            41, 0, 0, 0, 72, 86, 83, 84, 85, 0,
            56, 77, 76, 81, 80, 0, 0, 78, 79, 0,
            0, 0, 4, 29, 0, 0, 0, 0, 23, 24,
            0, 0, 0, 60, 67, 0, 62, 0, 42, 73,
            82, 0, 0, 0, 50, 51, 26, 0, 28, 11,
            0, 0, 0, 7, 34, 70, 61, 75, 25, 0,
            13, 0, 0, 16, 0, 0, 0, 0, 22,
    };
    final static short[] yydgoto = {9,
            10, 29, 12, 13, 14, 43, 82, 104, 15, 45,
            77, 30, 78, 79, 80, 16, 17, 18, 19, 20,
            26, 38, 39, 40, 21, 47, 22, 23, 50, 88,
            70, 60,
    };
    final static short[] yysindex = {51,
            -14, 0, 0, -6, 40, 46, -216, -41, 0, 0,
            12, 0, 0, 60, -156, 0, 0, 0, 0, 0,
            -167, 0, 29, -25, -26, 0, 0, 51, 48, 0,
            -224, 0, 0, 0, 0, 0, -26, -7, 73, 0,
            51, -149, -152, 74, 0, -6, 0, 40, 0, -240,
            0, 88, -8, 5, 0, 0, 0, 0, 0, 90,
            0, 0, 0, 0, 0, -26, -26, 0, 0, -26,
            -26, -26, 0, 0, -146, -124, 93, 91, 0, 0,
            75, 14, -156, 0, 0, 40, 0, -129, 0, 0,
            0, 73, 73, 80, 0, 0, 0, -117, 0, 0,
            -149, -134, 40, 0, 0, 0, 0, 0, 0, 100,
            0, 26, -149, 0, 109, -149, 110, -149, 0,
    };
    final static short[] yyrindex = {0,
            0, 0, 0, 0, -115, 0, 117, 0, 0, 0,
            21, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 17, 20, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 3, 0,
            24, -34, 36, 18, 0, 0, 0, 1, 0, 22,
            0, 25, 31, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, -36, -108, -31, 0, 0,
            0, 0, 0, 0, 0, 2, 0, 39, 0, 0,
            0, 13, 23, 0, 0, 0, 0, -35, 0, 0,
            0, 37, 0, 0, 0, 0, 0, 0, 0, -30,
            0, 0, 0, 0, -29, 0, -27, -34, 0,
    };
    final static short[] yygindex = {0,
            69, 78, 0, 0, 0, 0, 0, 0, 57, 19,
            43, 9, -71, 0, 0, 0, 0, 0, 0, 0,
            0, 4, -17, 33, 0, 0, 63, 0, 0, 0,
            0, 0,
    };
    final static int YYTABLESIZE = 323;
    final static short YYFINAL = 9;
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
            "bloque_sentencias : sentencia ';'",
            "bloque_sentencias : sentencia ';' bloque_sentencias",
            "sentencia : sentencia_declarativa",
            "sentencia : sentencia_ejecutable",
            "sentencia_declarativa : nombre_proc params_proc ni_proc cuerpo_proc",
            "sentencia_declarativa : tipo lista_variables",
            "nombre_proc : PROC ID",
            "nombre_proc : PROC",
            "params_proc : '(' lista_params ')'",
            "params_proc : '(' lista_params",
            "ni_proc : NI '=' CTE_UINT",
            "ni_proc : NI '='",
            "ni_proc :",
            "cuerpo_proc : '{' bloque_estruct_ctrl '}'",
            "lista_params :",
            "lista_params : param",
            "lista_params : param ',' param",
            "lista_params : param ',' param ',' param",
            "lista_params : param ',' param ',' param ',' param",
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
    //## Constructors ###############################################
    private final AnalizadorLexico aLexico;
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
        yytable = new short[]{36,
                68, 71, 49, 37, 30, 27, 17, 30, 27, 18,
                19, 20, 47, 21, 12, 51, 43, 33, 37, 46,
                2, 64, 48, 3, 44, 24, 86, 87, 53, 110,
                45, 49, 56, 8, 66, 66, 67, 67, 63, 57,
                32, 115, 52, 49, 117, 49, 25, 49, 92, 93,
                58, 59, 68, 47, 69, 47, 85, 47, 33, 68,
                71, 49, 49, 48, 49, 48, 27, 48, 8, 61,
                41, 47, 47, 94, 47, 43, 33, 11, 46, 8,
                64, 48, 48, 44, 48, 31, 30, 27, 17, 45,
                8, 18, 19, 20, 106, 21, 54, 63, 76, 42,
                44, 105, 46, 95, 96, 11, 55, 74, 84, 73,
                97, 112, 2, 3, 71, 2, 3, 83, 11, 72,
                108, 81, 66, 75, 67, 68, 71, 49, 89, 90,
                91, 98, 99, 100, 101, 102, 103, 47, 107, 109,
                111, 43, 33, 113, 46, 2, 64, 48, 3, 44,
                114, 28, 116, 118, 59, 45, 10, 76, 15, 14,
                119, 0, 28, 63, 0, 12, 0, 0, 0, 76,
                0, 0, 76, 0, 76, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 33, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                33, 44, 0, 34, 35, 0, 0, 30, 27, 17,
                0, 0, 18, 19, 20, 0, 21, 0, 34, 35,
                62, 63, 64, 65, 0, 0, 0, 0, 0, 0,
                49, 49, 49, 49, 0, 0, 0, 68, 68, 71,
                47, 47, 47, 47, 0, 0, 0, 0, 0, 0,
                48, 48, 48, 48, 0, 1, 0, 0, 0, 0,
                2, 3, 0, 4, 48, 0, 1, 5, 0, 6,
                7, 2, 3, 0, 4, 0, 0, 1, 5, 0,
                6, 7, 2, 3, 0, 4, 0, 0, 0, 5,
                0, 6, 7,
        };
    }

    static void yycheck() {
        yycheck = new short[]{41,
                0, 0, 0, 45, 41, 41, 41, 44, 44, 41,
                41, 41, 0, 41, 123, 41, 0, 0, 45, 0,
                0, 0, 0, 0, 0, 40, 267, 268, 25, 101,
                0, 23, 257, 40, 43, 43, 45, 45, 0, 264,
                257, 113, 24, 41, 116, 43, 61, 45, 66, 67,
                275, 276, 60, 41, 62, 43, 48, 45, 41, 59,
                59, 59, 60, 41, 62, 43, 4, 45, 40, 37,
                59, 59, 60, 70, 62, 59, 59, 0, 59, 40,
                59, 59, 60, 59, 62, 40, 123, 123, 123, 59,
                40, 123, 123, 123, 86, 123, 28, 59, 42, 40,
                257, 83, 270, 71, 72, 28, 59, 257, 46, 41,
                257, 103, 262, 263, 42, 262, 263, 44, 41, 47,
                41, 274, 43, 273, 45, 125, 125, 125, 41, 125,
                41, 75, 257, 41, 44, 61, 123, 125, 268, 257,
                275, 125, 125, 44, 125, 125, 125, 125, 125, 125,
                125, 123, 44, 44, 270, 125, 40, 101, 123, 123,
                118, -1, 123, 125, -1, 274, -1, -1, -1, 113,
                -1, -1, 116, -1, 118, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, 257, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                257, 257, -1, 275, 276, -1, -1, 274, 274, 274,
                -1, -1, 274, 274, 274, -1, 274, -1, 275, 276,
                258, 259, 260, 261, -1, -1, -1, -1, -1, -1,
                258, 259, 260, 261, -1, -1, -1, 267, 268, 268,
                258, 259, 260, 261, -1, -1, -1, -1, -1, -1,
                258, 259, 260, 261, -1, 257, -1, -1, -1, -1,
                262, 263, -1, 265, 266, -1, 257, 269, -1, 271,
                272, 262, 263, -1, 265, -1, -1, 257, 269, -1,
                271, 272, 262, 263, -1, 265, -1, -1, -1, 269,
                -1, 271, 272,
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
                    yychar = yylex();  //get next token
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
                case 2:
//#line 10 "gramatica.y"
                {
                    yyerror("Falta ';' al final de la sentencia");
                }
                break;
                case 9:
//#line 23 "gramatica.y"
                {
                    yyout("Declaracion_nombre_proc");
                }
                break;
                case 10:
//#line 24 "gramatica.y"
                {
                    yyerror("Procedimiento sin nombre");
                }
                break;
                case 11:
//#line 27 "gramatica.y"
                {
                    yyout("Declaracion_params_proc");
                }
                break;
                case 12:
//#line 28 "gramatica.y"
                {
                    yyerror("Falta parentesis de cierre de parametros");
                }
                break;
                case 13:
//#line 31 "gramatica.y"
                {
                    yyout("Declaracion_ni_proc");
                }
                break;
                case 14:
//#line 32 "gramatica.y"
                {
                    yyerror("Formato incorrecto de NI. El formato correcto es: 'NI = CTE_UINT'");
                }
                break;
                case 15:
//#line 33 "gramatica.y"
                {
                    yyerror("Formato incorrecto de NI. El formato correcto es: 'NI = CTE_UINT'");
                }
                break;
                case 16:
//#line 36 "gramatica.y"
                {
                    yyout("Declaracion_cuerpo_proc");
                }
                break;
                case 21:
//#line 43 "gramatica.y"
                {
                    yyerror("El procedimiento no puede tener mas de 3 parametros");
                }
                break;
                case 22:
//#line 44 "gramatica.y"
                {
                    yyerror("El procedimiento no puede tener mas de 3 parametros");
                }
                break;
                case 25:
//#line 51 "gramatica.y"
                {
                    yyout("Parametro_VAR");
                }
                break;
                case 26:
//#line 52 "gramatica.y"
                {
                    yyerror("Falta el tipo de un parametro");
                }
                break;
                case 27:
//#line 53 "gramatica.y"
                {
                    yyerror("Falta el nombre de un parametro");
                }
                break;
                case 28:
//#line 56 "gramatica.y"
                {
                    yyout("Parametro_comun");
                }
                break;
                case 29:
//#line 57 "gramatica.y"
                {
                    yyerror("Falta el tipo de un parametro");
                }
                break;
                case 30:
//#line 58 "gramatica.y"
                {
                    yyerror("Falta el nombre de un parametro");
                }
                break;
                case 31:
//#line 61 "gramatica.y"
                {
                    yyout("UINT");
                }
                break;
                case 32:
//#line 62 "gramatica.y"
                {
                    yyout("DOUBLE");
                }
                break;
                case 33:
//#line 65 "gramatica.y"
                {
                    yyout("Variable");
                }
                break;
                case 41:
//#line 79 "gramatica.y"
                {
                    yyout("Invocacion_Vacia");
                }
                break;
                case 42:
//#line 80 "gramatica.y"
                {
                    yyout("Invocacion");
                }
                break;
                case 43:
//#line 81 "gramatica.y"
                {
                    yyerror("Falta parentesis de cierre");
                }
                break;
                case 44:
//#line 82 "gramatica.y"
                {
                    yyerror("Falta parentesis de cierre");
                }
                break;
                case 45:
//#line 85 "gramatica.y"
                {
                    yyout("Asignacion");
                }
                break;
                case 46:
//#line 86 "gramatica.y"
                {
                    yyerror("Falta expresion para la asignacion");
                }
                break;
                case 53:
//#line 99 "gramatica.y"
                {
                    yyout("ID");
                }
                break;
                case 54:
//#line 100 "gramatica.y"
                {
                    yyout("CTE_UINT");
                }
                break;
                case 55:
//#line 101 "gramatica.y"
                {
                    yyout("CTE_DOUBLE");
                }
                break;
                case 58:
//#line 108 "gramatica.y"
                {
                    yyout("LOOP");
                }
                break;
                case 59:
//#line 109 "gramatica.y"
                {
                    yyerror("Cuerpo LOOP vacio");
                }
                break;
                case 60:
//#line 112 "gramatica.y"
                {
                    yyout("UNTIL");
                }
                break;
                case 61:
//#line 115 "gramatica.y"
                {
                    yyout("IF-THEN-ELSE");
                }
                break;
                case 62:
//#line 116 "gramatica.y"
                {
                    yyout("IF-THEN");
                }
                break;
                case 63:
//#line 117 "gramatica.y"
                {
                    yyerror("Falta palabra clave END_IF");
                }
                break;
                case 64:
//#line 118 "gramatica.y"
                {
                    yyerror("Falta palabra clave END_IF");
                }
                break;
                case 66:
//#line 122 "gramatica.y"
                {
                    yyerror("Falta palabra clave IF");
                }
                break;
                case 67:
//#line 125 "gramatica.y"
                {
                    yyout("THEN bloque_estruct_ctrl");
                }
                break;
                case 68:
//#line 126 "gramatica.y"
                {
                    yyerror("Cuerpo THEN vacio");
                }
                break;
                case 69:
//#line 127 "gramatica.y"
                {
                    yyerror("Falta palabra clave THEN");
                }
                break;
                case 70:
//#line 130 "gramatica.y"
                {
                    yyout("ELSE bloque_estruct_ctrl");
                }
                break;
                case 71:
//#line 131 "gramatica.y"
                {
                    yyerror("Cuerpo ELSE vacio");
                }
                break;
                case 74:
//#line 138 "gramatica.y"
                {
                    yyerror("Condicion vacia");
                }
                break;
                case 83:
//#line 153 "gramatica.y"
                {
                    yyout("Print_CADENA");
                }
                break;
                case 84:
//#line 154 "gramatica.y"
                {
                    yyout("Print_UINT");
                }
                break;
                case 85:
//#line 155 "gramatica.y"
                {
                    yyout("Print_DOUBLE");
                }
                break;
                case 86:
//#line 156 "gramatica.y"
                {
                    yyout("Print_ID");
                }
                break;
//#line 710 "Parser.java"
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
                    yychar = yylex();        //get next character
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

    private int yylex() {
        return aLexico.yylex();
    }

    private void yyout(String mensaje) {
        System.out.println(mensaje);
    }

    private void yyerror(String mensajeError) {
        System.err.println("Error en la linea " + aLexico.getLineaActual() + ": " + mensajeError);
    }

//###############################################################


}
//################### END OF CLASS ##############################
