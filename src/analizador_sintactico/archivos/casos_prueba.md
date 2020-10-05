## LISTA VARIABLES
# Mal definida
	+ "UINT ;"
	+ "UINT x,;"
	
# Falta ';' al final de la sentencia
	+ "UINT x,y,z,w"
	
## PROCEDIMIENTOS
# 4 parametros
	+ "PROC procedimiento (UINT x, DOUBLE y, UINT z, DOUBLE w) NI = 1 {\n
		x = 5\n;
	};"

# NI mal declarado
	+ "PROC proc() {\n
            x = 5;\n +
        };"
        
        + "PROC proc() NI = {\n
            x = 5;\n +
        };"
        
        + "PROC proc() = {\n
            x = 5;\n +
        };"
        
# Procedimiento sin nombre
	+ "PROC proc( NI = 1 {\n
            x = 5;\n +
        };"
        
# Falta ';' al final de la sentencia
	+ "PROC proc() NI = 1 {\n
            x = 5;\n +
        }"
        
# Parametro comun mal declarado
	+ "PROC proc(UINT) NI = 1 {\n
            x = 5;\n +
        }"
        
        
	+ "PROC proc(x) NI = 1 {\n
            x = 5;\n +
        }"
        
# Parametro VAR mal declarado
	+ "PROC proc(VAR UINT) NI = 1 {\n
            x = 5;\n +
        }"
        
        
	+ "PROC proc(VAR x) NI = 1 {\n
            x = 5;\n +
        }"
        
## ASIGNACIONES
# Falta expresion
	+ "x = ;"
	
# Falta ';' al final de la sentencia
	+ "x = 2721"
	
## CONDICIONALES
# ELSE vacio
	+ "IF (x==y) THEN\n" +
            "OUT(x);\n" +
        "ELSE\n" +
        "END_IF;"
        
# THEN vacio
	+ "IF (x==y) THEN\n" +
            "" +
        "END_IF;"
        
# Falta condicion
	+ "IF () THEN\n" +
            "UINT x;\n" +
        "END_IF;"

# Falta END_IF
	+ "IF (x==y) THEN\n" +
            "OUT(x);\n" +
        ";"
        
        + "IF (x==y) THEN\n" +
            "OUT(x);\n" +
        "ELSE\n" +
            "OUT(y);\n" +
        ";"

# Falta IF       
        + "(x==y) THEN\n" +
            "UINT x\n;" +
        "END_IF;"
        
# Falta THEN
	+ "IF (x==y)\n" +
            "OUT(x);\n" +
        "END_IF;"
        
## INVOCACIONES
# Falta parentesis cierre
	+ "w(;"
	+ "w(x,y,z,t,w,o;"
	
# Params mal definidos
	+ "mi_funcion\n(UINT,x,DOUBLE);"
	
## LOOPS
# Cuerpo vacio
	+ "LOOP\n" +
            "" +
        "UNTIL\n" +
            "(x==y)" +
        ";"	

# Falta condicion
	+ "LOOP\n" +
            "x = 1;\n" +
        "UNTIL ();";


