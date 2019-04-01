.data
a:		.word 1
b:		.word 4
c:      .word 3
zero:	.word 0

.code
start:	ld 	r1,a(r0)	; r1 = 1
        ld  r3,zero(r0) ; r3 = 0
		ld 	r2,b(r0)	; r2 = 4
loop:   add r3, r3, r1  ; r3 = x + 1    
		beq	r3,r2,next	; Exit if register is equal to b
		beq r1,r1,loop	; loop around
next:   ld r4,c(r0)     ; Store 3 in register 4

