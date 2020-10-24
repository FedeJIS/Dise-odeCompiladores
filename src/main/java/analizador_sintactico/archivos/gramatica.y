%{
package analizador_sintactico;

import analizador_lexico.AnalizadorLexico;
import util.TablaNotificaciones;
import util.tabla_simbolos.Celda;
import util.tabla_simbolos.TablaSimbolos;
%}

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
				
fin_sentencia	: {yyerror("Falta ';' al final de la sentencia.");}
				| ';'
				;
			
sentencia_decl	: nombre_proc params_proc ni_proc cuerpo_proc
				| tipo_id lista_variables
				;
				
nombre_proc	: PROC ID
			| PROC {yyerror("Falta el identificador del procedimiento.");}
			;
			
params_proc	: '(' lista_params_decl ')'
			| '(' ')'
			| '(' lista_params_decl {yyerror("Falta el parentesis de cierre para los parametros.");}
			| '(' {yyerror("Falta el parentesis de cierre para los parametros.");}
			;
			
lista_params_decl	: param
					| param separador_variables lista_params_decl
					;

separador_variables	: {yyerror("Falta una ',' para separar dos parametros.");}
					| ','
					;
					
param	: VAR tipo_id ID
		| tipo_id ID
		;
			
ni_proc	: NI '=' CTE_UINT
		;
		
cuerpo_proc	: '{' bloque_sentencias '}'
			| '{' '}' {yyerror("Cuerpo del procedimiento vacio.");}
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
				
invocacion	: ID '(' ')'
			| ID '(' lista_params_inv ')'
			;
			
lista_params_inv	: ID
					| ID separador_variables ID
					| ID separador_variables ID separador_variables ID
					| ID separador_variables ID separador_variables ID separador_variables lista_params_inv {yyerror("Un procedimiento no puede tener mas de 3 parametros.");}
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
		| '-' factor    {checkCambioSigno();}
		;
		
loop	: cuerpo_loop cuerpo_until
		;
		
cuerpo_loop	: LOOP bloque_estruct_ctrl
			;
		
bloque_estruct_ctrl	: sentencia_ejec fin_sentencia
					| '{' bloque_sentencias_ejec '}'
					| '{' '}' {yyerror("Bloque de sentencias vacio.");}
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

%%
    private AnalizadorLexico aLexico;
    private TablaSimbolos tablaS;

    /**
     * Create a parser, setting the debug to true or false.
     *
     * @param debugMe true for debugging, false for no debug.
     */
    public Parser(boolean debugMe, AnalizadorLexico aLexico, TablaSimbolos tablaS) {
        yydebug = debugMe;
        this.aLexico = aLexico;
        this.tablaS = tablaS;
    }

    private int yylex() {
        int token = aLexico.produceToken();
        yylval = new ParserVal(aLexico.ultimoLexemaGenerado);
        return token;
    }

    private void yyout(String mensaje) {
        System.out.println(mensaje);
    }

    private void yyerror(String mensajeError) {
        TablaNotificaciones.agregarError("Error en la linea " + aLexico.getLineaActual() + ": " + mensajeError);
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
        } else
            TablaNotificaciones.agregarError("Error en la linea " + aLexico.getLineaActual() + ": No se permiten UINT negativos");

    }



























			

