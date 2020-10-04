%token ID, COMP_MENOR_IGUAL, COMP_MAYOR_IGUAL, COMP_DISTINTO, COMP_IGUAL, UINT, DOUBLE, CADENA, IF , THEN, ELSE, END_IF,  LOOP, UNTIL, OUT , PROC , VAR,  NI, CTE_UINT, CTE_DOUBLE

%start programa

%%

programa	: bloque_sentencias
			;
			
bloque_sentencias	: sentencia                         {yyerror("Falta ';' al final de la sentencia");}
                    | sentencia ';'
					| sentencia ';' bloque_sentencias
					;

sentencia 	: sentencia_declarativa
            | sentencia_ejecutable
			;
			
sentencia_declarativa	: nombre_proc params_proc ni_proc cuerpo_proc
						| tipo lista_variables
						;

nombre_proc : PROC ID
            | PROC      {yyerror("Procedimiento sin nombre");}
            ;

params_proc : '(' lista_params ')'
            | '(' lista_params      {yyerror("Falta parentesis de cierre de parametros");}
            ;

ni_proc : NI '=' CTE_UINT
        | NI '='            {yyerror("Formato incorrecto de NI. El formato correcto es: 'NI = CTE_UINT'");}
        |                   {yyerror("Formato incorrecto de NI. El formato correcto es: 'NI = CTE_UINT'");}
        ;

cuerpo_proc : '{' bloque_estruct_ctrl '}'
            ;

lista_params    :
                | param
                | param ',' param
                | param ',' param ',' param
                | param ',' param ',' param ',' param   {yyerror("El procedimiento no puede tener mas de 3 parametros");}
                | param ',' param ',' param ',' param ',' lista_params {yyerror("El procedimiento no puede tener mas de 3 parametros");}
                ;

param	: param_var
        | param_comun
        ;

param_var   : VAR tipo ID
            | VAR ID        {yyerror("Falta el tipo de un parametro");}
            | VAR tipo      {yyerror("Falta el nombre de un parametro");}
            ;

param_comun : tipo ID
            | ID        {yyerror("Falta el tipo de un parametro");}
            | tipo      {yyerror("Falta el nombre de un parametro");}
            ;

tipo	: UINT
		| DOUBLE
		;

lista_variables : ID
				| ID ',' lista_variables
				;

sentencia_ejecutable	: invocacion
                        | asignacion
						| sentencia_loop
						| sentencia_if
						| print
						;

invocacion	: ID params_invocacion
			;

params_invocacion   : '(' ')'
                    | '(' lista_variables ')'
                    | '('                       {yyerror("Falta parentesis de cierre");}
                    | '(' lista_variables       {yyerror("Falta parentesis de cierre");}
                    ;

asignacion	: ID '=' expresion
            | ID '='                {yyerror("Falta expresion para la asignacion");}
			;

expresion	: expresion '+' termino
			| expresion '-' termino
	        | termino
			;
    		
termino	: termino '*' factor
		| termino '/' factor
		| factor
     	;	
		
factor 	: ID
		| CTE_UINT
		| CTE_DOUBLE
		| '-' factor    {checkCambioSigno();}
		;

sentencia_loop	: cuerpo_loop cuerpo_until
				;

cuerpo_loop : LOOP bloque_estruct_ctrl
            | LOOP                      {yyerror("Cuerpo LOOP vacio");}
            ;

cuerpo_until    : UNTIL condicion
                ;

sentencia_if    : encabezado_if rama_then rama_else END_IF
                | encabezado_if rama_then END_IF
                | encabezado_if rama_then rama_else         {yyerror("Falta palabra clave END_IF");}
                | encabezado_if rama_then                   {yyerror("Falta palabra clave END_IF");}
                ;

encabezado_if   : IF condicion
                | condicion     {yyerror("Falta palabra clave IF");}
                ;

rama_then   : THEN bloque_estruct_ctrl
            | THEN                      {yyerror("Cuerpo THEN vacio");}
            | bloque_estruct_ctrl       {yyerror("Falta palabra clave THEN");}
            ;

rama_else   : ELSE bloque_estruct_ctrl
            | ELSE                      {yyerror("Cuerpo ELSE vacio");}
            ;

bloque_estruct_ctrl : sentencia ';'
                    | '{' bloque_sentencias '}'
                    ;

condicion 	: '(' ')'                                   {yyerror("Condicion vacia");}
            | '(' expresion comparador expresion ')'
			;
			
comparador 	: COMP_MAYOR_IGUAL
			| COMP_MENOR_IGUAL
			| '<'
			| '>'
			| COMP_IGUAL
			| COMP_DISTINTO
			;

print	: OUT '(' imprimible ')'
		;

imprimible  : CADENA
            | CTE_UINT
            | CTE_DOUBLE
            | ID
            ;

%%

    private AnalizadorLexico aLexico;
    private TablaDeSimbolos tablaS;

    /**
     * Create a parser, setting the debug to true or false.
     *
     * @param debugMe true for debugging, false for no debug.
     */
    public Parser(boolean debugMe, AnalizadorLexico aLexico, TablaDeSimbolos tablaS) {
        yydebug = debugMe;
        this.aLexico = aLexico;
        this.tablaS = tablaS;
    }

    private int yylex() {
        int token = aLexico.produceToken();
        yylval = aLexico.ultimoLexemaGenerado;
        return token;
    }

    private void yyout(String mensaje) {
        System.out.println(mensaje);
    }

    private void yyerror(String mensajeError) {
        System.err.println("Error en la linea " + aLexico.getLineaActual() + ": " + mensajeError);
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
        }
    }
	
	
	
	
	
	
