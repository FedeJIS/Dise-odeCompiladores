.386
.model flat, stdcall
option casemap :none
include \masm32\include\windows.inc
include \masm32\include\kernel32.inc
include \masm32\include\user32.inc
includelib \masm32\lib\kernel32.lib
includelib \masm32\lib\user32.lib
.DATA
_PROGRAM@c DD 0
_PROGRAM@b DD 0
_PROGRAM@a DD 0
@aux1 DD 0
@aux0 DD 0

@resta_neg DB 'Error: Resultado de resta menor a cero.', 0
@recursion DB 'Error: Recursiones en procedimientos no permitidas.', 0
@ejecucion_sin_error DB 'Ejecucion sin errores.', 0
.CODE

START:
MOV _PROGRAM@a, 60.0
MOV _PROGRAM@b, 70.0
MOV _PROGRAM@c, 80.0
FLD _PROGRAM@a
FLD _PROGRAM@b
FDIV
FSTP @aux0
FLD @aux0
FLD _PROGRAM@c
FDIV
FSTP @aux1
MOV _PROGRAM@a, @aux1
JMP L_final
L_resta_neg:
invoke MessageBox, NULL, addr @resta_neg, addr @resta_neg , MB_OK
JMP L_final
L_recursion:
invoke MessageBox, NULL, addr @recursion, addr @recursion , MB_OK
JMP L_final
L_final:
invoke MessageBox, NULL, addr @ejecucion_sin_error, addr @ejecucion_sin_error , MB_OK
invoke ExitProcess, 0
END START
