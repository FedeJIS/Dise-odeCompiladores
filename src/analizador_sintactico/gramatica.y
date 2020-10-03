%token ID, COMP_MENOR_IGUAL, COMP_MAYOR_IGUAL, COMP_DISTINTO, COMP_IGUAL, UINT, DOUBLE, CADENA, IF , THEN, ELSE, END_IF,  LOOP, UNTIL, OUT , PROC , VAR,  NI, CTE_UINT, CTE_DOUBLE

%start programa

%%

programa	: bloque_sentencias
			;
			
bloque_sentencias	: sentencia                         {yyout("Error: Falta ';' al final de la sentencia");}
                    | sentencia ';'
					| sentencia ';' bloque_sentencias
					;

sentencia 	: sentencia_declarativa
            | sentencia_ejecutable
			;
			
sentencia_declarativa	: PROC ID '(' lista_params ')' NI '=' CTE_UINT '{' bloque_sentencias '}'    {yyout("Declaracion procedimiento.");}
						| tipo lista_variables
						;

lista_params    :
                | parametro
                | parametro ',' parametro
                | parametro ',' parametro ',' parametro
                ;

parametro	: VAR tipo ID   {yyout("Parametro_VAR");}
			| tipo ID       {yyout("Parametro");}
			;

tipo	: UINT      {yyout("UINT");}
		| DOUBLE    {yyout("DOUBLE");}
		;

lista_variables : ID                        {yyout("Variable");}
				| ID ',' lista_variables
				;

sentencia_ejecutable	: invocacion
                        | asignacion
						| sentencia_loop
						| sentencia_if
						| print
						;

invocacion	: ID '(' ')'                    {yyout("Invocacion_Vacia");}
            | ID '(' lista_variables ')'    {yyout("Invocacion");}
			;

asignacion	: ID '=' expresion      {yyout("Asignacion");}
			;

expresion	: expresion '+' termino
			| expresion '-' termino
	        | termino
			;
    		
termino	: termino '*' factor
		| termino '/' factor
		| factor
     	;	
		
factor 	: ID            {yyout("ID");}
		| CTE_UINT      {yyout("CTE_UINT");}
		| CTE_DOUBLE    {yyout("CTE_DOUBLE");}
		| '-' factor
		;

sentencia_loop	: LOOP bloque_estruct_ctrl UNTIL condicion    {yyout("LOOP");}
				;

sentencia_if    : encabezado_if rama_then rama_else END_IF  {yyout("IF-THEN-ELSE");}
                | encabezado_if rama_then END_IF            {yyout("IF-THEN");}
                | encabezado_if rama_then rama_else         {yyout("Error: Falta palabra clave END_IF");}
                | encabezado_if rama_then                   {yyout("Error: Falta palabra clave END_IF");}
                ;

encabezado_if   : IF condicion  {yyout("IF(condicion)");}
                | condicion     {yyout("Error: Falta palabra clave IF");}
                ;

rama_then   : THEN bloque_estruct_ctrl  {yyout("THEN bloque_estruct_ctrl");}
            | THEN                      {yyout("Error: Cuerpo THEN vacio");}
            | bloque_estruct_ctrl       {yyout("Error: Falta palabra clave THEN");}
            ;

rama_else   : ELSE bloque_estruct_ctrl  {yyout("ELSE bloque_estruct_ctrl");}
            | ELSE                      {yyout("Error: Cuerpo ELSE vacio");}
            ;

bloque_estruct_ctrl	: sentencia ';'
                    | '{' bloque_sentencias '}'
                    ;

condicion 	: '(' ')'                                   {yyout("Falta condicion");}
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

imprimible  : CADENA        {yyout("Print_CADENA");}
            | CTE_UINT      {yyout("Print_UINT");}
            | CTE_DOUBLE    {yyout("Print_DOUBLE");}
            | ID            {yyout("Print_ID");}
            ;
	
	
	
	
	
	
