.386
.model flat, stdcall
option casemap :none
include \masm32\include\windows.inc
include \masm32\include\kernel32.inc
include \masm32\include\user32.inc
includelib \masm32\lib\kernel32.lib
includelib \masm32\lib\user32.lib
.DATA
PROGRAM@aa DW 0
PROGRAM@x@y@www DD 0
PROGRAM@x@a DW 0

@resta_neg DB 'Error: Resultado de resta menor a cero.', 0
@recursion DB 'Error: Recursiones en procedimientos no permitidas.', 0
@ejecucion_sin_error DB 'Ejecucion sin errores.', 0
.CODE
_PROGRAM@x@y PROC
OUT_UINT 5
RET
_PROGRAM@x@y ENDP
_PROGRAM@x@z PROC
OUT_UINT 6
MOV BX, 1
CMP BX, 0
JE L_recursion
CALL _PROGRAM@x@z
RET
_PROGRAM@x@z ENDP
_PROGRAM@x PROC
OUT_UINT 5
MOV BX, 1
CMP BX, 1
JE L_recursion
CALL _PROGRAM@x@z
RET
_PROGRAM@x ENDP

START:
MOV BX, 5
MOV _PROGRAM@aa, BX
MOV BX, _PROGRAM@aa
MOV _PROGRAM@x@a, BX
MOV BX, 1
CMP BX, 0
JE L_recursion
CALL _PROGRAM@x
MOV BX, _PROGRAM@x@a
MOV _PROGRAM@aa, BX
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
