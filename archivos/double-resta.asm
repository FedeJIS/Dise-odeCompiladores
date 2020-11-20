.386
.model flat, stdcall
option casemap :none
include \masm32\include\windows.inc
include \masm32\include\kernel32.inc
include \masm32\include\user32.inc
includelib \masm32\lib\kernel32.lib
includelib \masm32\lib\user32.lib
.DATA
60p0 DD 60.0
_PROGRAM@c DD 0
_PROGRAM@b DD 0
_PROGRAM@a DD 0
@aux1 DD 0
@aux0 DD 0
80p0 DD 80.0
70p0 DD 70.0

@resta_neg DB 'Error: Resultado de resta menor a cero.', 0
@recursion DB 'Error: Recursiones en procedimientos no permitidas.', 0
@ejecucion_sin_error DB 'Ejecucion sin errores.', 0
.CODE

START:
FLD 60p0
FSTP_PROGRAM@a
FLD 70p0
FSTP_PROGRAM@b
FLD 80p0
FSTP_PROGRAM@c
FLD _PROGRAM@a
FLD _PROGRAM@b
FSUB
FSTP @aux0
FLD @aux0
FLD _PROGRAM@c
FSUB
FSTP @aux1
FLD @aux1
FSTP_PROGRAM@a
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
