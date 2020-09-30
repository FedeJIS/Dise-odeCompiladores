%token ID, COMP_MENOR_IGUAL, COMP_MAYOR_IGUAL, COMP_DISTINTO, COMP_IGUAL, UINT, DOUBLE, CADENA, IF , THEN, ELSE, END_IF,  LOOP, UNTIL, OUT , PROC , VAR,  NI, CTE_UINT, CTE_DOUBLE

%start programa

%%

programa	: bloque_sentencias {yyout("Reconocio programa");}
			;
			
bloque_sentencias	: sentencia {yyout("Reconocio sentencia");}
					| sentencia bloque_sentencias  {yyout("Reconocio bloque_sentencias");}
					;

sentencia 	: sentencia_declarativa {yyout("Reconocio sentencia_declarativa");}
            | sentencia_ejecutable {yyout("Reconocio sentencia_ejecutable");}
			;
			
sentencia_declarativa	: PROC ID '(' lista_parametros ')' NI '=' CTE_UINT '{' bloque_sentencias '}'  {yyout("Reconocio PROC");}
						| tipo lista_variables ';'{yyout("Reconocio tipo+lista_v");}
						;

lista_parametros	: parametro
					| parametro ',' lista_parametros {yyout("Reconocio lista_param");}
					;
					
parametro	: VAR tipo ID {yyout("Reconocio parametro VAR");}
			| tipo ID {yyout("Reconocio parametro");}
			;

lista_variables : ID ',' lista_variables {yyout("Reconocio lista_v");}
				| ID {yyout("Reconocio variable");}
				;

tipo	: UINT {yyout("Reconocio tipo UINT");}
		| DOUBLE {yyout("Reconocio tipo DOUBLE");}
		;

sentencia_ejecutable	: asignacion ';'{}
						| sentencia_loop {}
						| sentencia_if {}
						| invocacion ';'{}
						| print ';'{}
						;
			
expresion	: expresion '+' termino {yyout("Reconocio Expr+Term");}
			| expresion '-' termino {yyout("Reconocio Expr-Term");}
	        | termino {yyout("Reconocio Term");}
			;
    		
termino	: termino '*' factor {yyout("Reconocio Term*Fact");}
		| termino '/' factor {yyout("Reconocio Term/Fact");}
		| factor {yyout("Reconocio Fact");}
     	;	
		
factor 	: ID  {yyout("Reconocio ID");}
		| CTE_UINT  {yyout("Reconocio CTE_UINT");}
		| CTE_DOUBLE  {yyout("Reconocio CTE_DOUBLE");}
		;

asignacion	: ID '=' expresion {yyout("Reconocio asignacion");}
			;

sentencia_loop	: LOOP '{' bloque_sentencias '}' UNTIL '(' condicion ')' {yyout("Reconocio loop");}
				;

condicion 	: expresion comparador expresion {yyout("Reconocio condicion");}
			;
			
comparador 	: COMP_MAYOR_IGUAL {}
			| COMP_MENOR_IGUAL {}
			| '<' {}
			| '>' {}
			| COMP_IGUAL {}
			| COMP_DISTINTO {}
			;

sentencia_if 	: IF '(' condicion ')' THEN bloque_condicional END_IF {yyout("Reconocio IF THEN");}
				| IF '(' condicion ')' THEN bloque_condicional ELSE bloque_condicional END_IF {yyout("Reconocio IF THEN ELSE");}
				;

bloque_condicional	: sentencia {yyout("Reconocio bloque_condicional");}
					| '{' bloque_sentencias '}' {}
					;

invocacion	: ID '(' lista_variables ')'{yyout("Reconocio invocacion");}
			;

print	: OUT '(' CADENA ')' {yyout("Reconocio print");}
		;
	
	
	
	
	
	
