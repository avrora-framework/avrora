#!/bin/bash

checkSuccess() {

    if [ "$?" = 0 ]; then
        echo " -> $1"
    else
        echo "*** STOP: $2 ***"
        $3
        exit 1
    fi
}

TESTS=$@
if [ -z "$TESTS" ]; then 
    TESTS='interpreter probes disassembler interrupts timers'
fi

for t in $TESTS; do

    echo Running tests in test/$t...
    test -d test/$t
    checkSuccess "test/$t exists." "test/$t does not exist." ''

    cd test/$t
    java avrora.Main -action=test -detail *.tst &> /tmp/test.log
    checkSuccess 'All tests passed.' 'There were test case failures.' 'cat /tmp/test.log'
    cd ../..
done
