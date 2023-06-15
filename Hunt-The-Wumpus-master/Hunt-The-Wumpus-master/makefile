all: playwumpus testwumpus

playwumpus: playwumpus.c wumpus.c wumpus.h
	gcc -Wall -ansi -pedantic -o playwumpus playwumpus.c wumpus.c

testwumpus: testwumpus.c wumpus.c wumpus.h
	gcc -Wall -ansi -pedantic -o testwumpus testwumpus.c wumpus.c


.PHONY: clean
clean:
	-rm *~
	-rm playwumpus testwumpus
