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
_PROGRAM@c DW 0
_PROGRAM@b DW 0
_PROGRAM@a DW 0
_CAD_BIEN DB 'BIEN', 0

@resta_neg DB 'Error: Resultado de resta menor a cero.', 0
@recursion DB 'Error: Recursiones en procedimientos no permitidas.', 0
@ejecucion_sin_error DB 'Ejecucion sin errores.', 0
.CODE

START:
MOV BX, 5
MOV _PROGRAM@a, BX
MOV BX, 6
MOV _PROGRAM@b, BX
MOV BX, _PROGRAM@a
ADD BX, _PROGRAM@b
MOV _PROGRAM@c, BX
invoke printf, cfm$("%i\n"),OFFSET _PROGRAM@c

MOV BX, _PROGRAM@a
ADD BX, _PROGRAM@b
MOV CX, _PROGRAM@c
CMP CX, BX
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
