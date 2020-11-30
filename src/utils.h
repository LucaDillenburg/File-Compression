#ifndef UTILS_H
#define UTILS_H

#include <stdio.h>

char lowByteFromShort(short s);
char highByteFromShort(short s);

short shortFromBytes(char low, char high);

int getFileSize(FILE *file);

#endif