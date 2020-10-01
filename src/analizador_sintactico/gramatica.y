%token ID, COMP_MENOR_IGUAL, COMP_MAYOR_IGUAL, COMP_DISTINTO, COMP_IGUAL, UINT, DOUBLE, CADENA, IF , THEN, ELSE, END_IF,  LOOP, UNTIL, OUT , PROC , VAR,  NI, CTE_UINT, CTE_DOUBLE

%start programa

%%

programa	: bloque_sentencias {}
			;
			
bloque_sentencias	: sentencia {}
					| sentencia bloque_sentencias  {}
					;

sentencia 	: sentencia_declarativa {}
            | sentencia_ejecutable {}
			;
			
sentencia_declarativa	: PROC ID '(' lista_parametros ')' NI '=' CTE_UINT '{' bloque_sentencias '}' {yyout("Declaracion procedimiento.");}
						| tipo lista_variables ';' {}
						;

lista_parametros	: parametro {}
					| parametro ',' parametro {}
					| parametro ',' parametro ',' parametro {}
					;
					
parametro	: VAR tipo ID {yyout("Parametro VAR");}
			| tipo ID {yyout("Parametro");}
			;

lista_variables : ID ',' lista_variables {}
				| ID {yyout("Variable");}
				;

tipo	: UINT {yyout("Tipo UINT");}
		| DOUBLE {yyout("Tipo DOUBLE");}
		;

sentencia_ejecutable	: asignacion ';'{}
						| sentencia_loop {}
						| sentencia_if {}
						| invocacion ';'{}
						| print ';'{}
						;
			
expresion	: expresion '+' termino {}
			| expresion '-' termino {}
	        | termino {}
			;
    		
termino	: termino '*' factor {}
		| termino '/' factor {}
		| factor {}
     	;	
		
factor 	: ID  {yyout("ID");}
		| CTE_UINT  {yyout("CTE_UINT");}
		| CTE_DOUBLE  {yyout("CTE_DOUBLE");}
		;

asignacion	: ID '=' expresion {yyout("Asignacion");}
			;

sentencia_loop	: LOOP '{' bloque_sentencias '}' UNTIL condicion {yyout("Loop");}
				;

condicion 	: '(' expresion comparador expresion ')' {}
			;
			
comparador 	: COMP_MAYOR_IGUAL {}
			| COMP_MENOR_IGUAL {}
			| '<' {}
			| '>' {}
			| COMP_IGUAL {}
			| COMP_DISTINTO {}
			;

sentencia_if 	: IF condicion THEN bloque_condicional END_IF {yyout("IF-THEN");}
				| IF condicion THEN bloque_condicional ELSE bloque_condicional END_IF {yyout("IF-THEN-ELSE");}
				;

bloque_condicional	: sentencia {}
					| '{' bloque_sentencias '}' {}
					;

invocacion	: ID '(' lista_variables ')'{yyout("Invocacion");}
			;

print	: OUT '(' CADENA ')' {yyout("Print");}
		;
	
	
	
	
	
	
