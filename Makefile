CCFLAGS = -Wall
CC = gcc

testa_compactacao : testa_compactacao.c compactacao.h compactacao.o dicionario.o
	$(CC) $(CCFLAGS) -o testa_compactacao testa_compactacao.c compactacao.o dicionario.o

compactacao.o : compactacao.c compactacao.h dicionario.h
	$(CC) $(CCFLAGS) -c compactacao.c 

dicionario.o : dicionario.c dicionario.h
	$(CC) $(CCFLAGS) -c dicionario.c

.PHONY : clean
clean :
	-rm -f *.o testa_compactacao
