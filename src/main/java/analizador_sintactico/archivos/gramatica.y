%{
package analizador_sintactico;

import analizador_lexico.AnalizadorLexico;
import generacion_c_intermedio.MultiPolaca;
import generacion_c_intermedio.PilaAmbitos;
import generacion_c_intermedio.Polaca;
import util.TablaNotificaciones;
import util.tabla_simbolos.Celda;
import util.tabla_simbolos.TablaSimbolos;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
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

fin_sentencia	:       {TablaNotificaciones.agregarError(aLexico.getLineaActual()-1,"Falta ';' al final de la sentencia.");}
				| ';'
				;
			
sentencia_decl	: nombre_proc params_proc ni_proc cuerpo_proc {
                                                                if (nombreIdValido) {
                                                                    declaraProc();
                                                                }
                                                                pilaAmbitos.eliminarUltimo();
                                                                }
				| tipo_id lista_variables
				;

lista_variables	: ID {declaraId("Variable",$1.sval,ultimoTipoLeido);}
				| ID ',' lista_variables {declaraId("Variable",$1.sval,ultimoTipoLeido);}
				;
				
nombre_proc	: PROC ID {
                        declaraIdProc($2.sval);
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
					
param_var	: VAR tipo_id ID {
                                //mapaListaParametros.put(pilaAmbitos.getAmbitosConcatenados(), pilaAmbitos.getAmbitosConcatenados()+":"+$3.sval);
                                declaraId("ParamCVR",$3.sval,ultimoTipoLeido);
                                }
            | VAR ID {yyerror("Falta el tipo de un parametro.");}
            | VAR tipo_id {yyerror("Falta el identificador de un parametro.");}
		    ;

param_comun : tipo_id ID {
                            //mapaListaParametros.put(pilaAmbitos.getAmbitosConcatenados(), pilaAmbitos.getAmbitosConcatenados()+":"+$2.sval);
                            declaraId("ParamCV",$2.sval,ultimoTipoLeido);
                            }
		    | tipo_id {yyerror("Falta el identificador de un parametro.");}
		    ;

ni_proc	: NI '=' CTE_UINT {
                          int maxInvocProc = Integer.parseInt(val_peek(0).sval);
                          if (maxInvocProc < 1 || maxInvocProc > 4)
                            TablaNotificaciones.agregarError(lineaNI,
                                    "El numero de invocaciones de un procedimiento debe estar en el rango [1,4].");
                          else tablaS.setMaxInvoc(pilaNombreProc.get(pilaNombreProc.size()-1), maxInvocProc);
                          }
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

invocacion	: ID '(' ')' {
                            guardaParamsInvoc();
                            invocaProc($1.sval);
                            }
			| ID '(' lista_params_inv ')' {invocaProc($1.sval);}
			;


			
lista_params_inv	: ID {guardaParamsInvoc($1.sval);}
					| ID separador_variables ID {guardaParamsInvoc($1.sval, $3.sval);}
					| ID separador_variables ID separador_variables ID {guardaParamsInvoc($1.sval, $3.sval, $5.sval);}
					| ID separador_variables ID separador_variables ID separador_variables lista_params_inv
                                                    {
                                                    guardaParamsInvoc($1.sval, $3.sval, $5.sval);
                                                    yyerror("Un procedimiento no puede tener mas de 3 parametros.");
                                                    }
                    ;

asignacion	: ID '=' expresion {checkValidezAsign($1.sval);}
            | ID '=' error {
                            checkValidezAsign($1.sval);
                            yyerror("El lado derecho de la asignacio no es valido.");
                            }
            | ID {
                    checkValidezAsign($1.sval);
                    yyerror("Un identificador en solitario no es una sentencia valida.");
                    }
            | error '=' expresion {yyerror("El lado izquierdo de la asignacion no es valido");}

            ;
			
expresion	: expresion '+' termino {agregarPasosRepr("+");}
			| expresion '-' termino {agregarPasosRepr("-");}
	        | termino
			;
    		
termino	: termino '*' factor {agregarPasosRepr("*");}
		| termino '/' factor {agregarPasosRepr("/");}
		| factor
     	;	
		
factor 	: ID {checkValidezFactor($1.sval);}
		| CTE_UINT {agregarPasosRepr($1.sval);}
		| CTE_DOUBLE {agregarPasosRepr($1.sval);}
		| '-' factor    {checkCambioSigno();}
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
			
print	: OUT '(' imprimible ')' {agregarPasosRepr(tipoImpresion);}
        | OUT '(' imprimible {yyerror("Falta parentesis de cierre de la sentencia OUT.");}
        | OUT '(' error ')' {yyerror("El contenido de la sentencia OUT no es valido.");}
		;
		
imprimible	: CADENA {
                    tipoImpresion = "OUT_CAD";
                    if (pilaAmbitos.inAmbitoGlobal()) polacaProgram.agregarPasos($1.sval);
                    else polacaProcedimientos.agregarPasos(pilaAmbitos.getAmbitosConcatenados(),$1.sval);
                    }
			| CTE_UINT {
                    tipoImpresion = "OUT_UINT";
                    if (pilaAmbitos.inAmbitoGlobal()) polacaProgram.agregarPasos($1.sval);
                    else polacaProcedimientos.agregarPasos(pilaAmbitos.getAmbitosConcatenados(),$1.sval);
                    }

			| CTE_DOUBLE {
                        tipoImpresion = "OUT_DOU";
                        if (pilaAmbitos.inAmbitoGlobal()) polacaProgram.agregarPasos($1.sval);
                        else polacaProcedimientos.agregarPasos(pilaAmbitos.getAmbitosConcatenados(),$1.sval);
                        }
			| ID {
                  String nLexema = getAmbitoId(val_peek(0).sval) + "@" + val_peek(0).sval;
                  tipoImpresion = "OUT_" + tablaS.getTipo(nLexema);

                  if (pilaAmbitos.inAmbitoGlobal()) polacaProgram.agregarPasos(nLexema);
                  else polacaProcedimientos.agregarPasos(pilaAmbitos.getAmbitosConcatenados(),nLexema);
			    }
			;

%%

  private final AnalizadorLexico aLexico;
  private final TablaSimbolos tablaS;
  private final PilaAmbitos pilaAmbitos;
  private final Polaca polacaProgram;
  private final MultiPolaca polacaProcedimientos;

  public Parser(AnalizadorLexico aLexico, TablaSimbolos tablaS) {
    this.aLexico = aLexico;
    this.tablaS = tablaS;
    this.pilaAmbitos = new PilaAmbitos();
    this.polacaProgram = new Polaca();
    this.polacaProcedimientos = new MultiPolaca();
  }

  private int yylex() {
    int token = aLexico.produceToken();
    yylval = new ParserVal(aLexico.ultimoLexemaGenerado);
    return token;
  }

  private void yyerror(String mensajeError) {
    TablaNotificaciones.agregarError(aLexico.getLineaActual(), mensajeError);
  }

  private void checkCambioSigno() {
    String lexemaSignoNoC = yylval.sval; //Obtengo el lexema del factor.
    Celda celdaOriginal = tablaS.getEntrada(lexemaSignoNoC); //La sentencia va aca si o si, porque mas adelante ya no existe la entrada en la TS.

    if (celdaOriginal.getTipo().equals("DOUBLE")) {
      tablaS.quitarReferencia(lexemaSignoNoC); //El lexema esta en la TS si o si. refs--.
      if (tablaS.entradaSinReferencias(lexemaSignoNoC)) tablaS.eliminarEntrada(lexemaSignoNoC);

      String lexemaSignoC = String.valueOf(Double.parseDouble(lexemaSignoNoC) * -1); //Cambio el signo del factor.

      quitarUltimoPasoRepr(); //Saco el factor que quedo con signo incorrecto.
      agregarPasosRepr(lexemaSignoC); //Agrego el factor con el signo que le corresponde.

      tablaS.agregarEntrada(celdaOriginal.getToken(), lexemaSignoC, celdaOriginal.getTipo());
      tablaS.setUsoEntrada(lexemaSignoC, "CTE");
    } else
      TablaNotificaciones.agregarError(aLexico.getLineaActual(), "No se permiten UINT negativos");
  }

  private String getAmbitoId(String lexema) {
    StringBuilder builderAmbito = new StringBuilder(pilaAmbitos.getAmbitosConcatenados());

    while (!builderAmbito.toString().isEmpty()) {
      //Busca el id en el ambito actual.
      if (tablaS.contieneLexema(builderAmbito.toString() + "@" + lexema))
        return builderAmbito.toString();

      //"Baja" un nivel en la pila de ambitos.
      if (!builderAmbito.toString().equals("PROGRAM")) //Chequea no estar en el ambito global.
        builderAmbito.delete(builderAmbito.lastIndexOf("@"), builderAmbito.length());
      else builderAmbito.delete(0, builderAmbito.length());
    }
    return ""; //La variable no esta declarada.
  }

  //---DECLARACION VARIABLES Y PARAMS---

  private String ultimoTipoLeido; //Almacena temporalmente el ultimo tipo leido.

  private boolean nombreIdValido = true;

  private void declaraId(String uso, String lexema, String tipo) {
    String ambito = getAmbitoId(lexema);

    if (!ambito.isEmpty() //La TS contiene el lexema recibido.
            && tablaS.isEntradaDeclarada(ambito + "@" + lexema)) {//Tiene el flag de declaracion activado.
      TablaNotificaciones.agregarError(aLexico.getLineaActual(), "El identificador '" + lexema + "' ya se encuentra declarado.");
      pilaNombreProc.remove(pilaNombreProc.size()-1);
    }
    else {
    tablaS.setTipoEntrada(lexema, tipo);
    tablaS.setUsoEntrada(lexema, uso);
    tablaS.setDeclaracionEntrada(lexema, true);
    tablaS.setAmbitoEntrada(lexema, pilaAmbitos.getAmbitosConcatenados()); //Actualizo el lexema en la TS.

    if (uso.equals("ParamCVR") || uso.equals("ParamCV"))
      agregaParamDecl(pilaNombreProc.get(pilaNombreProc.size()-1),ambito+"@"+lexema);

    nombreIdValido = true;
    }
  }

  //---DECLARACION PROCS---

  private final List<Integer> pilaMaxInvocProc = new ArrayList<>();

  private final List<String> pilaNombreProc = new ArrayList<>();

  private final Map<String, List<String>> mapaListaParametros = new HashMap<>();

  private int lineaNI; //Guarda la linea donde se declaro el proc.

  private void declaraIdProc(String lexema) {
    declaraId(TablaSimbolos.USO_ENTRADA_PROC, lexema, "-");
    pilaNombreProc.add(pilaAmbitos.getAmbitosConcatenados() + "@" + lexema);
    mapaListaParametros.put(pilaAmbitos.getAmbitosConcatenados() + "@" + lexema, new ArrayList<>());
    lineaNI = aLexico.getLineaActual();
    mapaListaParametros.put(pilaNombreProc.get(pilaNombreProc.size()-1),new ArrayList<>());
  }

  private void agregaParamDecl(String nombreProc, String nombreParam){
    List<String> paramsProc = mapaListaParametros.get(nombreProc);
    paramsProc.add(nombreParam);
  }

  private void declaraProc() {
    //Este metodo se invoca al cierre de la declaracion de un procedimiento.
    //Por lo tanto, saco el tope de la pila de nombres y de max invocs.
    String nombreProc = pilaNombreProc.remove(pilaNombreProc.size()-1);

    List<String> listaParams = mapaListaParametros.remove(nombreProc);
    int nParams = listaParams.size();
    if (nParams > 3) nParams = 3; //Se queda con los primeros 3 params y descarta el resto.
    tablaS.setParamsProc(nombreProc, listaParams.subList(0, nParams)); //A esta altura ya se verificaron los ids correspondientes a cada
                                                                      // parametro. Solo resta asociarlos con el lexema del proc.
    listaParams.clear();
    nombreIdValido = true; //Reinicia el valor.
  }

  //---INVOCACION PROCS---

  private final List<String> listaParams = new ArrayList<>();

  private void guardaParamsInvoc(String... lexemaParams) {
    for (String lexemaParam : lexemaParams)
      listaParams.add(getAmbitoId(lexemaParam) + "@" + lexemaParam);
  }

  private boolean tipoParamsValidos(String lexema, int nParamsDecl) {
    System.out.println(lexema);
    boolean invocValida = true;
    for (int i = 0; i < nParamsDecl; i++) {
      String tipoParamInvoc = tablaS.getTipo(listaParams.get(i));
      String tipoParamDecl = tablaS.getTipoParam(lexema, i);
      if (!tipoParamInvoc.equals(tipoParamDecl)) {
        TablaNotificaciones.agregarError(aLexico.getLineaActual(),
                "En la posicion " + (i + 1) + " se esperaba un " + tipoParamDecl + ", pero se encontro un " + tipoParamInvoc + ".");
        invocValida = false;
      }
    }
    return invocValida;
  }

  private void invocaProc(String lexema) {
    String ambito = getAmbitoId(lexema);

    boolean invocValida = true;

    if (ambito.isEmpty()) {
      TablaNotificaciones.agregarError(aLexico.getLineaActual(), "El procedimiento '" + lexema + "' no esta declarado.");
      return; //Es necesario cortar aca para que 'ambito' no cause problemas por estar vacio.
    }
    String nLexema = ambito + "@" + lexema;

    if (tablaS.maxInvocAlcanzadas(nLexema)) {
      TablaNotificaciones.agregarError(aLexico.getLineaActual(), "El procedimiento '" + lexema + "' ya alcanzo su numero maximo de invocaciones.");
      invocValida = false;
    } else tablaS.incrementaNInvoc(nLexema);

    int nParamsDecl = tablaS.getNParams(nLexema);
    if (nParamsDecl != listaParams.size()) {
      TablaNotificaciones.agregarError(aLexico.getLineaActual(), "Se esperaban " + nParamsDecl + " parametros, pero se encontraron " + listaParams.size() + ".");
      invocValida = false;
    }

    if (nParamsDecl != 0 && listaParams.size() >= nParamsDecl) //El '>=' permite analizar los primeros parametros, aunque se tengan mas de los declarados.
      invocValida = tipoParamsValidos(nLexema, nParamsDecl);

    if (invocValida)
      generaCodigoInvocacion(nLexema, nParamsDecl);

    listaParams.clear();
  }

  private void generaCodigoInvocacion(String lexemaProc, int nParamsDecl) {
    String paramDecl, paramInvoc;
    for (int i = 0; i < nParamsDecl; i++) { //Pasa el valor de los parametros reales a los formales.
      paramDecl = tablaS.getParam(lexemaProc, i);
      paramInvoc = listaParams.get(i);
      agregarPasosRepr(paramInvoc, paramDecl, "="); //paramDecl = paramInvoc.
    }

    agregarPasosRepr(lexemaProc,lexemaProc);
    agregarPasosRepr(lexemaProc,Polaca.PASO_INVOC);

    for (int i = 0; i < nParamsDecl; i++) { //Pasa el valor de los param formales a los reales (En caso de param CVR).
      paramDecl = tablaS.getParam(lexemaProc, i);
      if (tablaS.isEntradaParamCVR(paramDecl)) {
        paramInvoc = listaParams.get(i);
        agregarPasosRepr(paramDecl, paramInvoc, "="); //paramInvoc = paramDecl.
      }
    }
  }

  //---ASIGN---

  private void checkValidezAsign(String lexema) {
    String ambito = getAmbitoId(lexema);

    if (ambito.isEmpty()) { //La TS no contiene el lexema recibido en ningun ambito.
      TablaNotificaciones.agregarError(aLexico.getLineaActual(), "El identificador '" + lexema + "' no esta declarado.");
      return; //Es necesario cortar aca para que 'ambito' no cause problemas por estar vacio.
    }

    String nLexema = ambito + "@" + lexema;
    if (!tablaS.isEntradaDeclarada(nLexema)) //Existe el lexema en la TS y tiene el flag de declaracion desactivado.
      TablaNotificaciones.agregarError(aLexico.getLineaActual(), "El identificador '" + lexema + "' no esta declarado.");

    if (tablaS.isEntradaProc(nLexema)) //Esta declarado pero es un procedimiento.
      TablaNotificaciones.agregarError(aLexico.getLineaActual(), "Un procedimiento no puede estar a la izquierda una asignacion.");

    agregarPasosRepr(nLexema, "=");
  }

  private void checkValidezFactor(String lexema) {
    String ambito = getAmbitoId(lexema);

    if (ambito.isEmpty()) { //La TS no contiene el lexema recibido en ningun ambito.
      TablaNotificaciones.agregarError(aLexico.getLineaActual(), "El identificador '" + lexema + "' no esta declarado.");
      return; //Es necesario cortar aca para que 'ambito' no cause problemas por estar vacio.
    }

    String nLexema = ambito + "@" + lexema;
    if (!tablaS.isEntradaDeclarada(nLexema)) //Existe el lexema en la TS y tiene el flag de declaracion desactivado.
      TablaNotificaciones.agregarError(aLexico.getLineaActual(), "El identificador '" + lexema + "' no esta declarado.");

    agregarPasosRepr(nLexema);
  }

  //---OUT---

  private String tipoImpresion; //Almacena temporalmente el tipo de dato que debe imprimirse.

  private boolean isIdDeclarado(String lexema) {
    String ambito = getAmbitoId(lexema);

    if (ambito.isEmpty()) { //La TS no contiene el lexema recibido en ningun ambito.
      TablaNotificaciones.agregarError(aLexico.getLineaActual(), "El identificador '" + lexema + "' no esta declarado.");
      return false; //Es necesario cortar aca para que 'ambito' no cause problemas por estar vacio.
    }

    if (!tablaS.isEntradaDeclarada(ambito + "@" + lexema)) { //Existe el lexema en la TS y tiene el flag de declaracion desactivado.
      TablaNotificaciones.agregarError(aLexico.getLineaActual(), "El identificador '" + lexema + "' no esta declarado.");
      return false;
    }
    return true;
  }

  //---POLACA---

  private void quitarUltimoPasoRepr() {
    if (pilaAmbitos.inAmbitoGlobal())
      polacaProgram.quitarUltimoPaso();
    else polacaProcedimientos.quitarUltimoPaso(pilaAmbitos.getAmbitosConcatenados());
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

  public Polaca getPolacaProgram(){
    return polacaProgram;
  }

  public MultiPolaca getPolacaProcs(){
    return polacaProcedimientos;
  }


