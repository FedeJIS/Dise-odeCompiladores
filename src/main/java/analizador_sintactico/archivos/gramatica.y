%{
package analizador_sintactico;

import analizador_lexico.AnalizadorLexico;
import generacion_c_intermedio.MultiPolaca;
import generacion_c_intermedio.PilaAmbitos;
import generacion_c_intermedio.Polaca;
import util.TablaNotificaciones;
import util.tabla_simbolos.Celda;
import util.tabla_simbolos.TablaSimbolos;
%}

%token ID, COMP_MENOR_IGUAL, COMP_MAYOR_IGUAL, COMP_DISTINTO, COMP_IGUAL, UINT, DOUBLE, CADENA, IF , THEN, ELSE, END_IF,  LOOP, UNTIL, OUT , PROC , VAR,  NI, CTE_UINT, CTE_DOUBLE

%start programa

%%

programa	: bloque_sentencias
			;
			
tipo_id	: UINT {ultimoTipoLeido = "UINT";}
		| DOUBLE {ultimoTipoLeido = "DOUBLE";}
		;
			
bloque_sentencias	: tipo_sentencia fin_sentencia
					| tipo_sentencia fin_sentencia bloque_sentencias
					;
					
tipo_sentencia	: sentencia_decl
				| sentencia_ejec
				;

fin_sentencia	:       {yyerror("Falta ';' al final de la sentencia");}
				| ';'
				;
			
sentencia_decl	: nombre_proc params_proc ni_proc cuerpo_proc {
                                                                if (nombreIdValido) checkNIProc();
                                                                pilaAmbitos.eliminarUltimo();
                                                                }
				| tipo_id lista_variables
				;

lista_variables	: ID {checkDeclaracionId("Variable",$1.sval,ultimoTipoLeido);}
				| ID ',' lista_variables {checkDeclaracionId("Variable",$1.sval,ultimoTipoLeido);}
				;
				
nombre_proc	: PROC ID {
                        checkDeclaracionProc($2.sval);
                        pilaAmbitos.agregarAmbito($2.sval); //Guardo el nombre del procedimiento en caso de necesitarlo.
                        }
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
					| param separador_variables param separador_variables param separador_variables lista_params_decl {yyerror("Un procedimiento no puede tener mas de 3 parametros.");}
					;

separador_variables	:       {yyerror("Falta una ',' para separar dos parametros.");}
					| ','
					;

param   : param_var
        | param_comun
        ;
					
param_var	: VAR tipo_id ID {checkDeclaracionId("ParamCVR",$3.sval,ultimoTipoLeido);}
            | VAR ID {yyerror("Falta el tipo de un parametro.");}
            | VAR tipo_id {yyerror("Falta el identificador de un parametro.");}
		    ;

param_comun : tipo_id ID {checkDeclaracionId("ParamCV",$2.sval,ultimoTipoLeido);}
		    | tipo_id {yyerror("Falta el identificador de un parametro.");}
		    ;

ni_proc	: NI '=' CTE_UINT {maxInvocProc = Integer.parseInt($3.sval);}
        | NI '=' {yyerror("Falta el numero de invocaciones del procedimiento.");}
        | '=' CTE_UINT {yyerror("Falta la palabra clave 'NI' en el encabezado del procedimiento.");}
        | NI CTE_UINT {yyerror("Formato de declaracion de NI invalido. El formato correcto es 'NI = CTE_UINT'.");}
        | error {yyerror("Formato de declaracion de NI invalido. El formato correcto es 'NI = CTE_UINT'.");}
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

invocacion	: ID '(' ')' {checkInvocacionProc($1.sval);}
			| ID '(' lista_params_inv ')' {checkInvocacionProc($1.sval);}
			;
			
lista_params_inv	: ID
					| ID separador_variables ID
					| ID separador_variables ID separador_variables ID
					| ID separador_variables ID separador_variables ID separador_variables lista_params_inv {yyerror("Un procedimiento no puede tener mas de 3 parametros.");}
                    ;

asignacion	: ID '=' expresion {
                                checkAsignacion($1.sval);
                                agregarPasosRepr($1.sval,"=");
                                }
            | ID '=' error {
                            checkAsignacion($1.sval);
                            yyerror("El lado derecho de la asignacio no es valido.");
                            }
            ;
			
expresion	: expresion '+' termino {agregarPasosRepr("+");}
			| expresion '-' termino {agregarPasosRepr("-");}
	        | termino
			;
    		
termino	: termino '*' factor {agregarPasosRepr("*");}
		| termino '/' factor {agregarPasosRepr("/");}
		| factor
     	;	
		
factor 	: ID {
                checkAsignacion($1.sval);
                agregarPasosRepr($1.sval);
                }
		| CTE_UINT {agregarPasosRepr($1.sval);}
		| CTE_DOUBLE {agregarPasosRepr($1.sval);}
		| '-' factor    {checkCambioSigno(); agregarPasosRepr("-");}
		;
		
loop	: encab_loop cuerpo_loop cuerpo_until
		;

encab_loop  : LOOP {puntoControlLoop();}
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
					    | sentencia_decl fin_sentencia {yyerror("No se permiten sentencias declarativas dentro de un bloque de estructura de control.");}
					    | sentencia_decl fin_sentencia bloque_sentencias_ejec {yyerror("No se permiten sentencias declarativas dentro de un bloque de estructura de control.");}
						;

cuerpo_until	: UNTIL condicion {puntoControlUntil();}
                | UNTIL {yyerror("Falta la condicion de corte del LOOP.");}
                ;

condicion	: '(' expresion comparador expresion ')' {agregarPasosRepr($3.sval);}
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

encabezado_if	: IF condicion {puntoControlThen();}
                | IF {yyerror("Falta la condicion del IF.");}
				;
				
rama_then	: THEN bloque_estruct_ctrl {puntoControlElse();}
            | THEN {yyerror("Falta el bloque de sentencias ejecutables de la rama THEN.");}
			;

rama_then_prima : THEN bloque_estruct_ctrl {puntoControlFinCondicional();}
                | THEN {yyerror("Falta el bloque de sentencias ejecutables de la rama THEN.");}
                ;
			
rama_else	: ELSE bloque_estruct_ctrl {puntoControlFinCondicional();}
            | ELSE {yyerror("Falta el bloque de sentencias ejecutables de la rama ELSE.");}
			;
			
print	: OUT '(' imprimible ')' {agregarPasosRepr($3.sval,"OUT");}
        | OUT '(' imprimible {yyerror("Falta parentesis de cierre de la sentencia OUT.");}
        | OUT '(' error ')' {yyerror("El contenido de la sentencia OUT no es valido.");}
		;
		
imprimible	: CADENA
			| CTE_UINT
			| CTE_DOUBLE
			| ID {}
			;

%%

public Parser(AnalizadorLexico aLexico, TablaSimbolos tablaS) {
    this.aLexico = aLexico;
    this.tablaS = tablaS;
    this.pilaAmbitos = new PilaAmbitos();
    this.polacaProgram = new Polaca();
    this.polacaProcedimientos = new MultiPolaca();
}

private final AnalizadorLexico aLexico;
private final TablaSimbolos tablaS;
private final PilaAmbitos pilaAmbitos;
private final Polaca polacaProgram;
private final MultiPolaca polacaProcedimientos;

private String nombreProc; //Almacena temporalmente el nombre de un procedimiento.
private int lineaNI; //Guarda la linea donde se detecto el NI del proc.
private boolean nombreIdValido = false;

private String ultimoTipoLeido; //Almacena temporalmente el ultimo tipo leido.

private int nInvocProc;
private int maxInvocProc; //Almacena temporalmente el maximo de invocaciones para un procedimiento.

private int yylex() {
    int token = aLexico.produceToken();
    yylval = new ParserVal(aLexico.ultimoLexemaGenerado);
    return token;
}

private void yyout(String mensaje) {
    System.out.println(mensaje);
}

private void yyerror(String mensajeError) {
    TablaNotificaciones.agregarError("Linea " + aLexico.getLineaActual() + ": " + mensajeError);
}

private void checkCambioSigno() {
    String lexemaSignoNoC = yylval.sval; //Obtengo el lexema del factor.
    Celda celdaOriginal = tablaS.getEntrada(lexemaSignoNoC); //La sentencia va aca si o si, porque mas adelante ya no existe la entrada en la TS.

    if (celdaOriginal.getTipo().equals("DOUBLE")) {
        tablaS.quitarReferencia(lexemaSignoNoC); //El lexema esta en la TS si o si. refs--.
        if (tablaS.entradaSinReferencias(lexemaSignoNoC)) tablaS.eliminarEntrada(lexemaSignoNoC);

        String lexemaSignoC = String.valueOf(Double.parseDouble(lexemaSignoNoC) * -1); //Cambio el signo del factor.
        tablaS.agregarEntrada(celdaOriginal.getToken(), lexemaSignoC, celdaOriginal.getTipo());

    } else
        TablaNotificaciones.agregarError("Linea " + aLexico.getLineaActual() + ": No se permiten UINT negativos");
}

private String getAmbitoId(String lexema) {
    StringBuilder builderAmbito = new StringBuilder(pilaAmbitos.getAmbitosConcatenados());

    while (!builderAmbito.toString().isEmpty()) {
        //Busca el id en el ambito actual.
        if (tablaS.contieneLexema(lexema, builderAmbito.toString()))
            return builderAmbito.toString();

        //"Baja" un nivel en la pila de ambitos.
        if (!builderAmbito.toString().equals("PROGRAM")) //Chequea no estar en el ambito global.
            builderAmbito.delete(builderAmbito.lastIndexOf(":"), builderAmbito.length());
        else builderAmbito.delete(0, builderAmbito.length());
    }
    return ""; //La variable no esta declarada.
}

private void checkDeclaracionId(String uso, String lexema, String tipo) {
    String ambito = getAmbitoId(lexema);

    if (!ambito.isEmpty() //La TS contiene el lexema recibido.
            && tablaS.isEntradaDeclarada(lexema, ambito))//Existe el lexema en la TS y tiene el flag de declaracion activado.
        TablaNotificaciones.agregarError("Linea " + aLexico.getLineaActual() + ": El identificador '" + lexema + "' ya se encuentra declarado.");
    else {
        tablaS.setTipoEntrada(lexema, tipo);
        tablaS.setUsoEntrada(lexema, uso);
        tablaS.setDeclaracionEntrada(lexema, true);
        tablaS.setAmbitoEntrada(lexema, pilaAmbitos.getAmbitosConcatenados()); //Actualizo el lexema en la TS.

        nombreIdValido = true;
    }
}

private void checkDeclaracionProc(String lexema) {
    checkDeclaracionId(TablaSimbolos.USO_ENTRADA_PROC,lexema,"VOID");
    nombreProc = pilaAmbitos.getAmbitosConcatenados() + "::" + lexema;
    lineaNI = aLexico.getLineaActual();
}

private void checkAsignacion(String lexema) {
    String ambito = getAmbitoId(lexema);

    if (ambito.isEmpty()) { //La TS no contiene el lexema recibido en ningun ambito.
        TablaNotificaciones.agregarError("Linea " + aLexico.getLineaActual() + ": El identificador '"+lexema+"' no esta declarado.");
        return; //Es necesario cortar aca para que 'ambito' no cause problemas por estar vacio.
    }
    //Existe el lexema en la TS y tiene el flag de declaracion desactivado.
    if (!tablaS.isEntradaDeclarada(lexema, ambito))
        TablaNotificaciones.agregarError("Linea " + aLexico.getLineaActual() + ": El identificador '" + lexema + "' no esta declarado.");
    if (tablaS.isEntradaProc(lexema, ambito)) { //Esta declarado pero es un procedimiento.
        TablaNotificaciones.agregarError("Linea " + aLexico.getLineaActual() + ": Un procedimiento no puede estar a la izquierda una asignacion.");
    }
}

private void checkNIProc(){
    if (maxInvocProc < 1 || maxInvocProc > 4){
        TablaNotificaciones.agregarError("Linea " + lineaNI + ": El numero de invocaciones de " +
                "un procedimiento debe estar en el rango [1,4].");
    }
    else {
        tablaS.setMaxInvoc(nombreProc, maxInvocProc);
        nombreIdValido = false; //Reinicia el valor.
    }
}

private void checkInvocacionProc(String lexema) {
    String ambito = getAmbitoId(lexema);

    if (ambito.isEmpty())
        TablaNotificaciones.agregarError("Linea " + aLexico.getLineaActual() + ": El procedimiento '" + lexema + "' no esta declarado.");
    else if (tablaS.maxInvocAlcanzadas(lexema, ambito))
        TablaNotificaciones.agregarError("Linea " + aLexico.getLineaActual() + ": El procedimiento '" + lexema + "' ya alcanzo su numero maximo de invocaciones.");
    else {
        tablaS.incrementaNInvoc(lexema, ambito);
        //Generar codigo
    }

}

private void agregarPasosRepr(String... pasos) {
    if (pilaAmbitos.inAmbitoGlobal())
        polacaProgram.agregarPasos(pasos);
    else polacaProcedimientos.agregarPasos(pilaAmbitos.getAmbitosConcatenados(), pasos);
}

private void puntoControlThen() {
    if (pilaAmbitos.inAmbitoGlobal())
        polacaProgram.puntoControlThen();
    else polacaProcedimientos.ejecutarPuntoControl(pilaAmbitos.getAmbitosConcatenados(), Polaca.PC_THEN);
}

private void puntoControlElse() {
    if (pilaAmbitos.inAmbitoGlobal())
        polacaProgram.puntoControlElse();
    else polacaProcedimientos.ejecutarPuntoControl(pilaAmbitos.getAmbitosConcatenados(), Polaca.PC_ELSE);
}

private void puntoControlFinCondicional() {
    if (pilaAmbitos.inAmbitoGlobal())
        polacaProgram.puntoControlFinCondicional();
    else polacaProcedimientos.ejecutarPuntoControl(pilaAmbitos.getAmbitosConcatenados(), Polaca.PC_FIN_COND);
}

private void puntoControlLoop() {
    if (pilaAmbitos.inAmbitoGlobal())
        polacaProgram.puntoControlLoop();
    else polacaProcedimientos.ejecutarPuntoControl(pilaAmbitos.getAmbitosConcatenados(), Polaca.PC_LOOP);
}

private void puntoControlUntil() {
    if (pilaAmbitos.inAmbitoGlobal())
        polacaProgram.puntoControlUntil();
    else polacaProcedimientos.ejecutarPuntoControl(pilaAmbitos.getAmbitosConcatenados(), Polaca.PC_UNTIL);
}

public void printPolaca() {
    polacaProgram.print();
}

public void printPolacaProcs() {
    polacaProcedimientos.print();
}