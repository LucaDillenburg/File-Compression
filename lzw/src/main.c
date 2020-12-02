#include "compression.h"
#include "utils.h"
#include <stdbool.h>
#include <stdio.h>
#include <string.h>

#define COMPRESS_MODE_STR "compress"
#define DECOMPRESS_MODE_STR "decompress"

#define COMPRESS_FILE_NAME "compressed_lwz"
#define DECOMPRESS_FILE_NAME "decompressed_lwz"

#define MODE_INDEX 1
#define INPUT_FILE_INDEX 1
#define OUTPUT_FILE_INDEX 2

char printSizeRate(int input_file_size, int output_file_size);

int main(int argc, char **argv) {
  if (argc < 3) {
    printf("Missing arguments! The program expects two arguments in this "
           "order: the mode (compress or decompress) and the file name. \n");
    return 1;
  }

  char *mode = argv[1];
  char *input_file_name = argv[2];

  if (strcmp(mode, COMPRESS_MODE_STR) != 0 &&
      strcmp(mode, DECOMPRESS_MODE_STR) != 0) {
    printf("Unknown mode! The options are: \"compress\" and \"decompress\".\n");
    return 2;
  }

  char *output_file_name;
  if (strcmp(mode, COMPRESS_MODE_STR) == 0)
    output_file_name = COMPRESS_FILE_NAME;
  else
    output_file_name = DECOMPRESS_FILE_NAME;

  FILE *input_file = fopen(input_file_name, "r");
  if (input_file == NULL) {
    printf("Couldn't open input file: %s!\n", input_file_name);
    return 3;
  }
  FILE *output_file = fopen(output_file_name, "wb");
  if (output_file == NULL) {
    printf("Couldn't open output file: %s!\n", output_file_name);
    return 4;
  }

  vector *dictionary;
  if (strcmp(mode, COMPRESS_MODE_STR) == 0) {
    dictionary = compressByLZW(input_file, output_file);
    int original_file_size = getFileSize(input_file);
    int compressed_file_size = getFileSize(output_file);
    printf("The file %s was compressed to %s with a ", input_file_name,
           output_file_name);

    char did_increase = printSizeRate(original_file_size, compressed_file_size);
    printf(".");
    if (did_increase)
      printf(" The compression resulted in a larger file! You should use the "
             "original file!");
  } else {
    dictionary = decompressByLZW(input_file, output_file);
    printf("The file %s was decompressed to %s.", input_file_name,
           output_file_name);
  }

  fclose(input_file);
  fclose(output_file);
  freeVector(dictionary);

  return 0;
}

char printSizeRate(int input_file_size, int output_file_size) {
  double rate = (double)output_file_size / input_file_size;
  if (rate < 1) {
    double decreased_perc = 1 - rate;
    printf("%.1f%% decrease in size", decreased_perc * 100);
    return 0;
  } else {
    double increased_perc = rate - 1;
    printf("%.1f%% increase in size", increased_perc * 100);
    return 1;
  }
}
