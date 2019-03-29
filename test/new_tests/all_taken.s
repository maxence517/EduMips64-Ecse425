;This program simply performs a loop 20 times
;And stores the running sum.
      .data
a:    .word 1
b:    .word 0
      .text
Start:		addi r1, r0, 2			;store 2 into r1. This is a constant.
		addi r2, r0, 0			;store  into r1
		slti r3, r1, 3			;check if r1 = 2 > 3
		add r0, r0, r0
		add r0, r0, r0
		add r0, r0, r0
		bne r3, r0, Take1		;it is not, thus the branch is taken
		add r0, r0, r0
Take1:		slti r3, r1, 3			;check if r1 = 2 > 3
		bne r3, r0, Take2		;it is not, thus the branch is taken
		add r0, r0, r0
Take2:		slti r3, r1, 3			;--
		bne r3, r0, Take3
		add r0, r0, r0
Take3:		slti r3, r1, 3
		bne r3, r0, Take4
		add r0, r0, r0
Take4:		slti r3, r1, 3			;--
		bne r3, r0, Take5
		add r0, r0, r0
Take5:		slti r3, r1, 3			;--
		bne r3, r0, Take6
		add r0, r0, r0
Take6:		slti r3, r1, 3			;--
		bne r3, r0, Take7
		add r0, r0, r0
Take7:		slti r3, r1, 3
		bne r3, r0, Take8
		add r0, r0, r0
Take8:		slti r3, r1, 3			;--
		bne r3, r0, Take9
		add r0, r0, r0
Take9:		slti r3, r1, 3
		bne r3, r0, Take10
		add r0, r0, r0
Take10:		slti r3, r1, 3			;--
		bne r3, r0, Take11
		add r0, r0, r0
Take11:		slti r3, r1, 3
		bne r3, r0, Take12
		add r0, r0, r0
Take12:		slti r3, r1, 3			;--
		bne r3, r0, Take13
		add r0, r0, r0
Take13:		slti r3, r1, 3
		bne r3, r0, Take14
		add r0, r0, r0
Take14:		slti r3, r1, 3			;--
		bne r3, r0, Take15
		add r0, r0, r0
Take15:		slti r3, r1, 3
		bne r3, r0, Take16
		add r0, r0, r0
Take16:		slti r3, r1, 3			;check if r1 = 2 > 3
		bne r3, r0, Take17		;it is not, thus the branch is taken
		add r0, r0, r0
Take17:		slti r3, r1, 3			;check if r1 = 2 > 3
		bne r3, r0, Take18		;it is not, thus the branch is taken
		add r0, r0, r0
Take18:		slti r3, r1, 3
		bne r3, r0, Take19
		add r0, r0, r0
Take19:		slti r3, r1, 3
		bne r3, r0, Take20
		add r0, r0, r0
Take20:		add r0, r0, r0			;stall before terminating