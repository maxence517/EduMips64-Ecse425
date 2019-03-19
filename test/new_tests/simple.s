      .data
a:    .word 1
b:    .word 0
      .text
go:   ld r1,a(r0)
      daddi r2,r0,2   ; loop counter
      daddi r3,r0,0   ; end value
loop: daddi r2,r2,-1  ; dec counter
      daddi r1,r1,1   ; loop body
      bne r2,r3,loop  ; till done
      sd r1,b(r0)
      daddi r4,r0,1
      halt
