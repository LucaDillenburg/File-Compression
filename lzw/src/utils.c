#include "utils.h"

char lowByteFromShort(short s) { return s & 0xff; }
char highByteFromShort(short s) { return s >> 8; }

short shortFromBytes(char low, char high) {
  short concat = (unsigned char)high;
  concat = concat << 8;
  concat = concat & 0xff00;
  concat = concat | (unsigned char)low;
  return concat;
}

int getFileSize(FILE *file) {
  int cur_position = ftell(file);

  fseek(file, 0L, SEEK_END);
  int size = ftell(file);

  fseek(file, cur_position, SEEK_SET);

  return size;
}