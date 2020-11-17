.386
.model flat, stdcall
option casemap :none
include \masm32\include\windows.inc
include \masm32\include\kernel32.inc
include \masm32\include\user32.inc
includelib \masm32\lib\kernel32.lib
includelib \masm32\lib\user32.lib
.DATA
60.0 DD 60.0
PROGRAM:c DD 0
PROGRAM:b DD 0
PROGRAM:a DD 0
80.0 DD 80.0
70.0 DD 70.0

@resta_neg DB 'Error: Resultado de resta menor a cero.', 0
@recursion DB 'Error: Recursiones en procedimientos no permitidas.', 0
@ejecucion_sin_error DB 'Ejecucion sin errores.', 0
.CODE

START:
MOV BX, 60.0
MOV _PROGRAM:a, BX
MOV BX, 70.0
MOV _PROGRAM:b, BX
MOV BX, 80.0
MOV _PROGRAM:c, BX
FLD PROGRAM:a
FLD PROGRAM:b
FDIV
FSTP @aux2
FLD @aux2
FLD PROGRAM:c
FDIV
FSTP @aux3
MOV BX, @aux3
MOV _PROGRAM:a, BX
JMP L_final
L_resta_neg:
invoke MessageBox, NULL, addr @resta_neg, addr@resta_neg , MB_OK
JMP L_final
L_recursion:
invoke MessageBox, NULL, addr @recursion, addr@recursion , MB_OK
JMP L_final
L_final:
invoke MessageBox, NULL, addr @ejecucion_sin_error, addr@ejecucion_sin_error , MB_OK
invoke ExitProcess, 0
END START
