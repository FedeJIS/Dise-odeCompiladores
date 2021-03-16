%{
package analizador_sintactico;

import analizador_lexico.AnalizadorLexico;
import analizador_sintactico.util.ParserHelper;
import generacion_c_intermedio.MultiPolaca;
import generacion_c_intermedio.PilaAmbitos;
import generacion_c_intermedio.Polaca;
import util.TablaNotificaciones;
import util.tabla_simbolos.Celda;
import util.tabla_simbolos.TablaSimbolos;
%}

%token ID, COMP_MENOR_IGUAL, COMP_MAYOR_IGUAL, COMP_DISTINTO, COMP_IGUAL, UINT, DOUBLE, CADENA, IF , THEN, ELSE, END_IF,  LOOP, UNTIL, OUT , PROC , VAR,  NI, CTE_UINT, CTE_DOUBLE, MAS_IGUAL, MENOS_IGUAL

%start programa

%%

programa	: bloque_sentencias
			;
			
tipo_id	: UINT {helper.setUltimoTipoLeido("UINT");}
		| DOUBLE {helper.setUltimoTipoLeido("DOUBLE");}
		;
			
bloque_sentencias	: tipo_sentencia fin_sentencia
					| tipo_sentencia fin_sentencia bloque_sentencias
					;
					
tipo_sentencia	: sentencia_decl
				| sentencia_ejec
				;

fin_sentencia	:       {TablaNotificaciones.agregarError(aLexico.getLineaActual()-1,"Falta ';' al final de la sentencia.");}
				| ';'
				;
			
sentencia_decl	: nombre_proc params_proc ni_proc cuerpo_proc {helper.eliminarUltimoAmbito();}
				| tipo_id lista_variables
				;

lista_variables	: ID {helper.declaracionVar($1.sval);}
				| ID ',' lista_variables {helper.declaracionVar($1.sval);}
				;
				
nombre_proc	: PROC ID {helper.lecturaIdProc($2.sval);}
			| PROC {yyerror("Falta el identificador del procedimiento.");}
			;
			
params_proc	: '(' lista_params_decl ')'
			| '(' ')'
			| '(' lista_params_decl {yyerror("Falta el parentesis de cierre para los parametros.");}
			| '(' {yyerror("Falta el parentesis de cierre para los parametros.");}
			;
			
lista_params_decl	: param
					| param separador_variables param
					| param separador_variables param separador_variables param
					| param separador_variables param separador_variables param separador_variables lista_params_decl
					                                {yyerror("Un procedimiento no puede tener mas de 3 parametros.");}
					;

separador_variables	:       {yyerror("Falta una ',' para separar dos parametros.");}
					| ','
					;

param   : param_var
        | param_comun
        ;
					
param_var	: VAR tipo_id ID {helper.lecturaParamFormal($3.sval, Celda.USO_PARAM_CVR);}
            | VAR ID {yyerror("Falta el tipo de un parametro.");}
            | VAR tipo_id {yyerror("Falta el identificador de un parametro.");}
		    ;

param_comun : tipo_id ID {helper.lecturaParamFormal($2.sval, Celda.USO_PARAM_CV);}
		    | tipo_id {yyerror("Falta el identificador de un parametro.");}
		    ;

ni_proc	: NI '=' CTE_UINT {helper.lecturaNumInvoc(Integer.parseInt(val_peek(0).sval), false, "");}
        | NI '=' {helper.lecturaNumInvoc(0, true, "Falta el numero de invocaciones del procedimiento.");}
        | '=' CTE_UINT {helper.lecturaNumInvoc(0, true, "Falta la palabra clave 'NI' en el encabezado del procedimiento.");}
        | NI CTE_UINT {helper.lecturaNumInvoc(0, true, "Formato de declaracion de NI invalido. El formato correcto es 'NI = CTE_UINT'.");}
        | error {helper.lecturaNumInvoc(0, true, "Formato de declaracion de NI invalido. El formato correcto es 'NI = CTE_UINT'.");}
		;
		
cuerpo_proc	: '{' bloque_sentencias '}'
			| '{' '}' {yyerror("Cuerpo del procedimiento vacio.");}
			;

sentencia_ejec	: invocacion
				| asignacion
				| loop
				| if
				| print
				;

invocacion	: ID '(' ')' {helper.invocacionProc($1.sval);}
			| ID '(' lista_params_inv ')' {helper.invocacionProc($1.sval);}
			;

param_inv   : ID {helper.guardaParamsInvoc($1.sval);}
            | CTE_UINT {helper.guardaParamsInvoc($1.sval);}
            | CTE_DOUBLE {helper.guardaParamsInvoc($1.sval);}
            ;
			
lista_params_inv	: param_inv
					| param_inv separador_variables param_inv
					| param_inv separador_variables param_inv separador_variables param_inv
					| param_inv separador_variables param_inv separador_variables param_inv separador_variables lista_params_inv
                                                    {yyerror("Un procedimiento no puede tener mas de 3 parametros.");}
                    ;

asignacion	: ID '=' expresion {helper.lecturaDestAsign($1.sval);}
            | ID MENOS_IGUAL expresion {helper.lecturaDestMenosIgual($1.sval);}
            | ID MAS_IGUAL expresion {helper.lecturaDestMasIgual($1.sval);}
            | ID MENOS_IGUAL error {helper.lecturaDestMenosIgual($1.sval);   yyerror("El lado derecho de la asignacion no es valido."); }
            | ID MAS_IGUAL  error {helper.lecturaDestMasIgual($1.sval);    yyerror("El lado derecho de la asignacion no es valido.");}
            | ID '=' error {
                            helper.lecturaDestAsign($1.sval);
                            yyerror("El lado derecho de la asignacion no es valido.");
                            }
            | ID {
                    helper.lecturaDestAsign($1.sval);
                    yyerror("Un identificador en solitario no es una sentencia valida.");
                    }
            | error '=' expresion {yyerror("El lado izquierdo de la asignacion no es valido");}

            ;

expresion	: expresion '+' termino {
                                      if (!TablaNotificaciones.hayErrores()){
                                          String[] ultimosPasos = helper.getUltimosPasos();
                                          String ultimoPaso = ultimosPasos[0];
                                          String anteultimoPaso = ultimosPasos[1];
                                          if(helper.entradaCte(ultimoPaso) && helper.entradaCte(anteultimoPaso))
                                          {
                                             if(helper.getTipoEntrada(ultimoPaso).equals(helper.getTipoEntrada(anteultimoPaso)))
                                             {
                                                if(helper.getTipoEntrada(ultimoPaso).equals("UINT"))
                                                {

                                                    int ultimo = Integer.parseInt(ultimoPaso);
                                                    int anteultimo = Integer.parseInt(anteultimoPaso);
                                                    int calculo = ultimo + anteultimo;
                                                    helper.quitarUltimoPasoRepr();
                                                    helper.quitarUltimoPasoRepr();
                                                    helper.agregarPasosRepr(String.valueOf(calculo));
                                                    helper.agregarEntradaTS(new Celda(Parser.CTE_UINT,String.valueOf(calculo),Celda.TIPO_UINT,Celda.USO_CTE,true));
                                                }
                                                else
                                                {
                                                    double ultimo = Double.parseDouble(ultimoPaso);
                                                    double anteultimo = Double.parseDouble(anteultimoPaso);
                                                    double calculo = ultimo + anteultimo;
                                                    helper.quitarUltimoPasoRepr();
                                                    helper.quitarUltimoPasoRepr();
                                                    helper.agregarPasosRepr(String.valueOf(calculo));
                                                    helper.agregarEntradaTS(new Celda(Parser.CTE_DOUBLE,String.valueOf(calculo),Celda.TIPO_DOUBLE,Celda.USO_CTE,true));
                                                }
                                              } else { yyerror("Los operandos son de diferentes tipos.");}
                                          } else {  helper.agregarPasosRepr("+");}
                                         }
                                      }
			| expresion '-' termino {
			                        if (!TablaNotificaciones.hayErrores()){
                                                      String[] ultimosPasos = helper.getUltimosPasos();
                                                      String ultimoPaso = ultimosPasos[0];
                                                      String anteultimoPaso = ultimosPasos[1];
                                                      if(helper.entradaCte(ultimoPaso) && helper.entradaCte(anteultimoPaso))
                                                      {
                                                         if(helper.getTipoEntrada(ultimoPaso).equals(helper.getTipoEntrada(anteultimoPaso)))
                                                         {
                                                            if(helper.getTipoEntrada(ultimoPaso).equals("UINT"))
                                                            {

                                                                int ultimo = Integer.parseInt(ultimoPaso);
                                                                int anteultimo = Integer.parseInt(anteultimoPaso);
                                                                int calculo = anteultimo - ultimo;
                                                                helper.quitarUltimoPasoRepr();
                                                                helper.quitarUltimoPasoRepr();
                                                                helper.agregarPasosRepr(String.valueOf(calculo));
                                                                helper.agregarEntradaTS(new Celda(Parser.CTE_UINT,String.valueOf(calculo),Celda.TIPO_UINT,Celda.USO_CTE,true));
                                                            }
                                                            else
                                                            {
                                                                double ultimo = Double.parseDouble(ultimoPaso);
                                                                double anteultimo = Double.parseDouble(anteultimoPaso);
                                                                double calculo = anteultimo - ultimo;
                                                                helper.quitarUltimoPasoRepr();
                                                                helper.quitarUltimoPasoRepr();
                                                                helper.agregarPasosRepr(String.valueOf(calculo));
                                                                helper.agregarEntradaTS(new Celda(Parser.CTE_DOUBLE,String.valueOf(calculo),Celda.TIPO_DOUBLE,Celda.USO_CTE,true));
                                                            }
                                                          } else { yyerror("Los operandos son de diferentes tipos.");}
                                                      } else {  helper.agregarPasosRepr("-");}
                                                     }
                                    }
	        | termino
			;
    		
termino	: termino '*' factor {
                                if (!TablaNotificaciones.hayErrores()){
                                                      String[] ultimosPasos = helper.getUltimosPasos();
                                                      String ultimoPaso = ultimosPasos[0];
                                                      String anteultimoPaso = ultimosPasos[1];
                                                      if(helper.entradaCte(ultimoPaso) && helper.entradaCte(anteultimoPaso))
                                                      {
                                                         if(helper.getTipoEntrada(ultimoPaso).equals(helper.getTipoEntrada(anteultimoPaso)))
                                                         {
                                                            if(helper.getTipoEntrada(ultimoPaso).equals("UINT"))
                                                            {

                                                                int ultimo = Integer.parseInt(ultimoPaso);
                                                                int anteultimo = Integer.parseInt(anteultimoPaso);
                                                                int calculo = ultimo * anteultimo;
                                                                helper.quitarUltimoPasoRepr();
                                                                helper.quitarUltimoPasoRepr();
                                                                helper.agregarPasosRepr(String.valueOf(calculo));
                                                                helper.agregarEntradaTS(new Celda(Parser.CTE_UINT,String.valueOf(calculo),Celda.TIPO_UINT,Celda.USO_CTE,true));
                                                            }
                                                            else
                                                            {
                                                                double ultimo = Double.parseDouble(ultimoPaso);
                                                                double anteultimo = Double.parseDouble(anteultimoPaso);
                                                                double calculo = ultimo * anteultimo;
                                                                helper.quitarUltimoPasoRepr();
                                                                helper.quitarUltimoPasoRepr();
                                                                helper.agregarPasosRepr(String.valueOf(calculo));
                                                                helper.agregarEntradaTS(new Celda(Parser.CTE_DOUBLE,String.valueOf(calculo),Celda.TIPO_DOUBLE,Celda.USO_CTE,true));
                                                            }
                                                          } else { yyerror("Los operandos son de diferentes tipos.");}
                                                      } else {  helper.agregarPasosRepr("*");}
                                                     }

}
		| termino '/' factor {
                                if (!TablaNotificaciones.hayErrores()){
                                                      String[] ultimosPasos = helper.getUltimosPasos();
                                                      String ultimoPaso = ultimosPasos[0];
                                                      String anteultimoPaso = ultimosPasos[1];
                                                      if(helper.entradaCte(ultimoPaso) && helper.entradaCte(anteultimoPaso))
                                                      {
                                                         if(helper.getTipoEntrada(ultimoPaso).equals(helper.getTipoEntrada(anteultimoPaso)))
                                                         {
                                                            if(helper.getTipoEntrada(ultimoPaso).equals("UINT"))
                                                            {

                                                                int ultimo = Integer.parseInt(ultimoPaso);
                                                                int anteultimo = Integer.parseInt(anteultimoPaso);
                                                                if(ultimo > 0)
                                                                {
                                                                    int calculo = anteultimo / ultimo;
                                                                    helper.quitarUltimoPasoRepr();
                                                                    helper.quitarUltimoPasoRepr();
                                                                    helper.agregarPasosRepr(String.valueOf(calculo));
                                                                    helper.agregarEntradaTS(new Celda(Parser.CTE_UINT,String.valueOf(calculo),Celda.TIPO_UINT,Celda.USO_CTE,true));
                                                                }else yyerror("No se puede dividir por cero");
                                                            }
                                                            else
                                                            {
                                                                double ultimo = Double.parseDouble(ultimoPaso);
                                                                double anteultimo = Double.parseDouble(anteultimoPaso);
                                                                if(ultimo > 0)
                                                                {
                                                                    double calculo = anteultimo / ultimo;
                                                                    helper.quitarUltimoPasoRepr();
                                                                    helper.quitarUltimoPasoRepr();
                                                                    helper.agregarPasosRepr(String.valueOf(calculo));
                                                                    helper.agregarEntradaTS(new Celda(Parser.CTE_DOUBLE,String.valueOf(calculo),Celda.TIPO_DOUBLE,Celda.USO_CTE,true));
                                                                }else yyerror("No se puede dividir por cero");
                                                            }
                                                          } else { yyerror("Los operandos son de diferentes tipos.");}
                                                      } else {  helper.agregarPasosRepr("/");}
                                                     }
		                        }
		| factor
     	;	
		
factor 	: ID {helper.lecturaFactor($1.sval);}
		| CTE_UINT {helper.agregarPasosRepr($1.sval);helper.setTipoUltimoFactor("UINT");}
		| CTE_DOUBLE {helper.agregarPasosRepr($1.sval);helper.setTipoUltimoFactor("DOUBLE");}
		| '-' factor    {helper.cambioSignoFactor(yylval.sval);}
		;

print	: OUT '(' imprimible ')'
        | OUT '(' imprimible
        | OUT '(' error ')'
		;

imprimible	: CADENA {helper.agregarPasosRepr($1.sval, "OUT_CAD");}
			| factor {helper.impresionFactor();}
			;
		
loop	: encab_loop cuerpo_loop cuerpo_until
		;

encab_loop  : LOOP {helper.puntoControlLoop();}
            ;
		
cuerpo_loop	: bloque_estruct_ctrl
            | {yyerror("Falta el bloque de sentencias ejecutables del LOOP.");}
			;
		
bloque_estruct_ctrl	: sentencia_ejec fin_sentencia
					| '{' bloque_sentencias_ejec '}'
					| '{' '}' {yyerror("Bloque de sentencias vacio.");}
					| sentencia_decl fin_sentencia {yyerror("No se permiten sentencias declarativas dentro de un bloque de estructura de control.");}
					;

bloque_sentencias_ejec	: sentencia_ejec fin_sentencia
						| sentencia_ejec fin_sentencia bloque_sentencias_ejec
					    | sentencia_decl fin_sentencia
					        {yyerror("No se permiten sentencias declarativas dentro de un bloque de estructura de control.");}
					    | sentencia_decl fin_sentencia bloque_sentencias_ejec
					        {yyerror("No se permiten sentencias declarativas dentro de un bloque de estructura de control.");}
						;

cuerpo_until	: UNTIL condicion {helper.puntoControlUntil();}
                | UNTIL {yyerror("Falta la condicion de corte del LOOP.");}
                ;

condicion	: '(' expresion comparador expresion ')' {helper.agregarPasosRepr($3.sval);}
            | '(' expresion comparador expresion {yyerror("Falta parentesis de cierre de la condicion.");}
            | '(' comparador expresion ')' {yyerror("Falta expresion en el lado izquierdo de la condicion.");}
            | '(' expresion comparador ')' {yyerror("Falta expresion en el lado derecho de la condicion.");}
            | '(' error ')' {yyerror("Error en la condicion.");}
			;
			
comparador 	: COMP_MAYOR_IGUAL
			| COMP_MENOR_IGUAL
			| '<'
			| '>'
			| COMP_IGUAL
			| COMP_DISTINTO
			;

if	: encabezado_if rama_then rama_else END_IF
	| encabezado_if rama_then_prima END_IF
	;

encabezado_if	: IF condicion {helper.puntoControlThen();}
                | IF {yyerror("Falta la condicion del IF.");}
				;
				
rama_then	: THEN bloque_estruct_ctrl {helper.puntoControlElse();}
            | THEN {yyerror("Falta el bloque de sentencias ejecutables de la rama THEN.");}
			;

rama_then_prima : THEN bloque_estruct_ctrl {helper.puntoControlFinCondicional();}
                | THEN {yyerror("Falta el bloque de sentencias ejecutables de la rama THEN.");}
                ;
			
rama_else	: ELSE bloque_estruct_ctrl {helper.puntoControlFinCondicional();}
            | ELSE {yyerror("Falta el bloque de sentencias ejecutables de la rama ELSE.");}
			;

%%

    private final ParserHelper helper;

    private final AnalizadorLexico aLexico;
    private final Polaca polacaProgram;
    private final MultiPolaca polacaProcedimientos;

    public Parser(AnalizadorLexico aLexico, TablaSimbolos tablaS) {
        this.aLexico = aLexico;
        this.polacaProgram = new Polaca();
        this.polacaProcedimientos = new MultiPolaca();

        helper = new ParserHelper(aLexico, tablaS, new PilaAmbitos(), polacaProgram, polacaProcedimientos);
    }

    private int yylex() {
        int token = aLexico.produceToken();
        yylval = new ParserVal(aLexico.ultimoLexemaGenerado);
        return token;
    }

    private void yyerror(String mensajeError) {
        TablaNotificaciones.agregarError(aLexico.getLineaActual(), mensajeError);
    }

    //---POLACA---

    public Polaca getPolacaProgram() {
        return polacaProgram;
    }

    public MultiPolaca getPolacaProcs() {
        return polacaProcedimientos;
    }

