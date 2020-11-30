#include "compression.h"
#include "utils.h"
#include "vector.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define FIRST_NOT_CODE_ASCII 128
#define MAX_ELEMS_DICTIONARY 32767
#define MAX_LENGTH_DICTIONARY_ELEM 20

/* Find associated code then write it to the file. */
void writeAssociatedCode(vector *dictionary, FILE *compressed_file_output,
                         char *dictionary_elem) {
  short associatedCode = indexOf(dictionary, dictionary_elem);
  if (associatedCode >= 0) {
    char low_byte = lowByteFromShort(associatedCode + FIRST_NOT_CODE_ASCII);
    char high_byte = highByteFromShort(associatedCode + FIRST_NOT_CODE_ASCII);
    fputc(low_byte, compressed_file_output);
    fputc(high_byte, compressed_file_output);
  } else {
    int i;
    for (i = 0;; i++)
      if (dictionary_elem[i] != '\0') {
        fputc(dictionary_elem[i], compressed_file_output);
        fprintf(compressed_file_output, "%c", (char)0);
      } else
        break;
  }
}

vector *compressByLZW(FILE *input_file, FILE *compressed_file_output) {
  vector *dictionary = createVector(MAX_ELEMS_DICTIONARY);
  char *input = (char *)malloc(sizeof(char) * MAX_LENGTH_DICTIONARY_ELEM);
  input[0] = '\0';

  while (1) {
    char cur_char = fgetc(input_file);
    if (cur_char == EOF)
      break;

    char *input_with_cur_char =
        (char *)malloc(sizeof(char) * MAX_LENGTH_DICTIONARY_ELEM);
    strcpy(input_with_cur_char, input);
    strncat(input_with_cur_char, &cur_char, 1);

    if (strlen(input) > 0 && indexOf(dictionary, input_with_cur_char) < 0) {
      writeAssociatedCode(dictionary, compressed_file_output, input);
      pushItem(dictionary, input_with_cur_char);

      input[0] = cur_char;
      input[1] = '\0';
    } else {
      strncat(input, &cur_char, 1);
      free(input_with_cur_char);
    }
  }

  writeAssociatedCode(dictionary, compressed_file_output, input);
  return dictionary;
}

vector *decompressByLZW(FILE *compressed_file_input,
                        FILE *output_original_file) {
  vector *dictionary = createVector(MAX_ELEMS_DICTIONARY);
  int is_first_time = 1;

  char *input = (char *)malloc(sizeof(char) * MAX_LENGTH_DICTIONARY_ELEM);
  input[0] = '\0';
  while (1) {
    char low_byte = fgetc(compressed_file_input);
    char high_byte = fgetc(compressed_file_input);
    if (low_byte == EOF && high_byte == EOF)
      break;

    char *last_input = input;
    input = (char *)malloc(sizeof(char) * MAX_LENGTH_DICTIONARY_ELEM);

    if (low_byte > 0 && high_byte == 0) {
      input[0] = low_byte;
      input[1] = '\0';
    } else {
      short code_from_dictionary =
          shortFromBytes(low_byte, high_byte) - FIRST_NOT_CODE_ASCII;
      char *str_from_dictionary = getItem(dictionary, code_from_dictionary);
      strcpy(input, str_from_dictionary);
    }

    fputs(input, output_original_file);

    if (!is_first_time) {
      strncat(last_input, input, 1);
      pushItem(dictionary, last_input);
    } else {
      is_first_time = 0;
      free(last_input);
    }
  }

  return dictionary;
}

void printDictionaryFromLZW(vector *dictionary) {
  int i;
  for (i = 0; i <= dictionary->last; i++)
    printf("%5d | %s\n", i + FIRST_NOT_CODE_ASCII, dictionary->array[i]);
}
