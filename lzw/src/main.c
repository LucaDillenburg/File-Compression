#include "compression.h"
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

int getFileSize(FILE *arq);
char printSizeRate(int input_file_size, int output_file_size);
bool arquivosSaoIguais(FILE *arq1, FILE *arq2);

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

  char *output_file_name = NULL;
  if (argc >= 4)
    output_file_name = argv[3];
  else {
    if (strcmp(mode, COMPRESS_MODE_STR) == 0)
      output_file_name = COMPRESS_FILE_NAME;
    else
      output_file_name = DECOMPRESS_FILE_NAME;
  }

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

  if (strcmp(mode, COMPRESS_MODE_STR) == 0) {
    compressByLZW(input_file, output_file);
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
    decompressByLZW(input_file, output_file);
    int compressed_file_size = getFileSize(input_file);
    int original_file_size = getFileSize(output_file);
    printf("The file %s was decompressed to %s with a ", input_file_name,
           output_file_name);

    printSizeRate(original_file_size, compressed_file_size);
    printf(".");
  }

  return 0;
}

char printSizeRate(int input_file_size, int output_file_size) {
  double rate = (double)output_file_size / input_file_size;
  if (rate < 1) {
    double decreased_perc = 1 - rate;
    printf("%.1f decrease in size", decreased_perc * 100);
    return 0;
  } else {
    double increased_perc = rate - 1;
    printf("%.1f increase in size", increased_perc * 100);
    return 1;
  }
}

bool arquivosSaoIguais(FILE *arq1, FILE *arq2) {
  int c1, c2;

  /* Vai para o início dos arquivos */
  fseek(arq1, 0, SEEK_SET);
  fseek(arq2, 0, SEEK_SET);

  int is_equal = 1;

  /* Compara os arquivos caracter a caracter */
  int i = 0;
  do {
    c1 = fgetc(arq1);
    c2 = fgetc(arq2);

    printf("%d: decimal: %d | usinged char: %d\n", i, c1, (unsigned char)c1);
    printf("%d: decimal: %d | usinged char: %d\n", i, c2, (unsigned char)c2);
    // printf("%d: decimal: %d | char: %d | usinged char: %d", i, c1, (char)c1,
    //        (unsigned char)c1);
    // printf("%d: decimal: %d | char: %d | usinged char: %d", i, c2, (char)c2,
    //        (unsigned char)c2);
    printf("\n");

    if (c1 != c2)
      is_equal = 0;
    i++;
  } while (c1 != EOF && c2 != EOF);

  return is_equal && (c1 == EOF && c2 == EOF);
}