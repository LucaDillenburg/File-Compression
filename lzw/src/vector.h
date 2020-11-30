#ifndef DICIONARIO_H
#define DICIONARIO_H

typedef struct {
  char **array;
  short available_length;
  short last;
} vector;

vector *createVector(short qtde_maxima_entradas);
void freeVector(vector *dictionary);
short pushItem(vector *dictionary, char *item);
char *getItem(vector *dictionary, short index);
short indexOf(vector *dictionary, char *str);

#endif