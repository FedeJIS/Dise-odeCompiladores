%token ID, COMP_MENOR_IGUAL, COMP_MAYOR_IGUAL, COMP_DISTINTO, COMP_IGUAL, UINT, DOUBLE, CADENA, IF , THEN, ELSE, END_IF,  LOOP, UNTIL, OUT , PROC , VAR,  NI, CTE_UINT, CTE_DOUBLE

%start programa

%%

programa	: bloque_sentencias
			;
			
tipo_id	: UINT
		| DOUBLE
		;
			
bloque_sentencias	: tipo_sentencia fin_sentencia
					| tipo_sentencia fin_sentencia bloque_sentencias
					;
					
tipo_sentencia	: sentencia_decl
				| sentencia_ejec
				;
				
fin_sentencia	: {yyerror("Falta ';' al final de la sentencia");}
				| ';'
				;
			
sentencia_decl	: nombre_proc params_proc ni_proc cuerpo_proc
				| tipo_id lista_variables
				;
				
nombre_proc	: PROC ID
			;
			
params_proc	: '(' lista_params_decl ')'
			;
			
lista_params_decl	: param
					| param ',' lista_params_decl
					;
				
param	: param_var
		| param_comun
		;

param_var	: VAR tipo_id ID
			;

param_comun	: tipo_id ID
			;
			
ni_proc	: NI '=' CTE_UINT
		;
		
cuerpo_proc	: '{' bloque_sentencias '}'
			;
			
lista_variables	: ID
				| ID ',' lista_variables
				;
				


sentencia_ejec	: invocacion
				| asignacion
				| loop
				| if
				| print
				;
				
invocacion	: ID lista_params_inv
			;
			
lista_params_inv	: '(' ')'
					| '(' lista_variables ')'
					;
					
asignacion	: ID '=' expresion
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
		| '-' factor    {}
		;
		
loop	: cuerpo_loop cuerpo_until
		;
		
cuerpo_loop	: LOOP bloque_estruct_ctrl
			;
		
bloque_estruct_ctrl	: sentencia_ejec fin_sentencia
					| '{' bloque_sentencias_ejec '}'
					;

bloque_sentencias_ejec	: sentencia_ejec fin_sentencia
						| sentencia_ejec fin_sentencia bloque_sentencias_ejec
						;
			
cuerpo_until	: UNTIL condicion

condicion	: '(' expresion comparador expresion ')'
			;
			
comparador 	: COMP_MAYOR_IGUAL
			| COMP_MENOR_IGUAL
			| '<'
			| '>'
			| COMP_IGUAL
			| COMP_DISTINTO
			;
			
if	: encabezado_if rama_then rama_else END_IF
	| encabezado_if rama_then END_IF
	;
	
encabezado_if	: IF condicion
				;
				
rama_then	: THEN bloque_estruct_ctrl
			;
			
rama_else	: ELSE bloque_estruct_ctrl
			;
			
print	: OUT '(' imprimible ')'
		;
		
imprimible	: CADENA
			| CTE_UINT
			| CTE_DOUBLE
			| ID
			;





























			

