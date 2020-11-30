#ifndef COMPRESSIONS_H
#define COMPRESSIONS_H

#include "vector.h"
#include <stdio.h>

/* Both files in the parameter should be opened */
vector *compressByLZW(FILE *input_file, FILE *compressed_file_output);
/* Both files in the parameter should be opened */
vector *decompressByLZW(FILE *compressed_file_input,
                        FILE *output_original_file);

void printDictionaryByLZW(vector *dicionario);

#endif