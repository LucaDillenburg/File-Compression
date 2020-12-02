#include "vector.h"
#include <stdlib.h>
#include <string.h>

vector *createVector(short available_length) {
  int i;
  vector *new_dictionary = (vector *)malloc(sizeof(vector));

  new_dictionary->available_length = available_length;
  new_dictionary->last = -1;

  new_dictionary->array = (char **)malloc(sizeof(char *) * available_length);
  for (i = 0; i < available_length; i++)
    new_dictionary->array[i] = NULL;

  return new_dictionary;
}

void freeVector(vector *dictionary) {
  int i;
  for (i = 0; i <= dictionary->last; i++)
    free(dictionary->array[i]);
  free(dictionary);
}

short pushItem(vector *dictionary, char *item) {
  if (dictionary->last == dictionary->available_length - 1)
    return -1;
  dictionary->last++;
  int index = dictionary->last;
  dictionary->array[index] = item;
  return index;
}

char *getItem(vector *dictionary, short index) {
  if (index > dictionary->last || index < 0)
    return NULL;
  return dictionary->array[index];
}

short indexOf(vector *dictionary, char *str) {
  int i;
  for (i = 0; i <= dictionary->last; i++)
    if (strcmp(dictionary->array[i], str) == 0)
      return i;
  return -1;
}
