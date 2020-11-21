.386
.model flat, stdcall
option casemap :none
include \masm32\include\windows.inc
include \masm32\include\kernel32.inc
include \masm32\include\user32.inc
includelib \masm32\lib\kernel32.lib
includelib \masm32\lib\user32.lib
include \masm32\include\masm32rt.inc
dll_dllcrt0 PROTO C
printf PROTO C :VARARG
.DATA
_CAD_MAL DB 'MAL', 0
_60p0 DQ 60.0
_PROGRAM@c DQ 0
_PROGRAM@b DQ 0
_PROGRAM@a DQ 0
@aux1 DQ 0
@aux0 DQ 0
_CAD_BIEN DB 'BIEN', 0
_70p0 DQ 70.0

@resta_neg DB 'Error: Resultado de resta menor a cero.', 0
@recursion DB 'Error: Recursiones en procedimientos no permitidas.', 0
@ejecucion_sin_error DB 'Ejecucion sin errores.', 0
.CODE

START:
FLD _60p0
FSTP _PROGRAM@a
FLD _70p0
FSTP _PROGRAM@b
FLD _PROGRAM@a
FLD _PROGRAM@b
FADD
FSTP @aux0
FLD @aux0
FSTP _PROGRAM@c
invoke printf, cfm$("%f\n"),OFFSET _PROGRAM@c

FLD _PROGRAM@a
FLD _PROGRAM@b
FADD
FSTP @aux1
FLD @aux1
FLD _PROGRAM@c
FCOMP
FSTSW AX
SAHF
JNE L24
invoke printf, cfm$("%s\n"),OFFSET _CAD_BIEN

JMP L27
L24:
invoke printf, cfm$("%s\n"),OFFSET _CAD_MAL

L27:
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
