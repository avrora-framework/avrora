# @Harness: probes
# @Result: 
# @Program: /edu/ucla/cs/compilers/avrora/test/probes/array.asm
# @Purpose: tests read watches

watch A { | | | }

main {
  insert A 0x100;
}

result {
  4 A.beforeRead;
  4 A.afterRead;
}
