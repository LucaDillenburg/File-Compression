/*
  AO PREENCHER(MOS) ESTE CABEÇALHO COM O(S) MEU(NOSSOS) NOME(S) E
  O(S) MEU(NOSSOS) NÚMERO(S) USP, DECLARO(AMOS) QUE SOU(SOMOS) O(S)
  ÚNICO(S) AUTOR(ES) E RESPONSÁVEL(IS) POR ESTE ARQUIVO. TODAS AS
  PARTES ORIGINAIS DO EXERCÍCIO PROGRAMA (EP) FORAM DESENVOLVIDAS
  E IMPLEMENTADAS POR MIM(NÓS) SEGUINDO AS INSTRUÇÕES DO EP E,
  PORTANTO, NÃO CONSTITUEM DESONESTIDADE ACADÊMICA OU PLÁGIO.
  DECLARO TAMBÉM QUE SOU(SOMOS) RESPONSÁVEL(IS) POR TODAS AS CÓPIAS
  DESTE ARQUIVO E QUE EU(NÓS) NÃO DISTRIBUÍ(MOS) OU FACILITEI(AMOS)
  A SUA DISTRIBUIÇÃO. ESTOU(AMOS) CIENTE(S) DE QUE OS CASOS DE PLÁGIO
  E DESONESTIDADE ACADÊMICA SERÃO TRATADOS CONFORME SUA GRAVIDADE.
  ENTENDO(EMOS) QUE EPS SEM ASSINATURA NÃO SERÃO CORRIGIDOS E,
  AINDA ASSIM, PODERÃO SER PUNIDOS POR DESONESTIDADE ACADÊMICA.

  Nome(s) : Luca Assumpção Dillenburg

  Referências: Com exceção das rotinas fornecidas no enunciado e em
  sala de aula, caso você(s) tenha(m) utilizado alguma referência,
  liste(m) abaixo para que o programa não seja considerado plágio ou
  irregular.

  Exemplo:
  - O algoritmo Quicksort foi baseado em
  http://www.ime.usp.br/~pf/algoritmos/aulas/quick.html
*/

#include "compactacao.h"
#include "dicionario.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define PRIMEIRO_CODIGO_DEPOIS_ASCII 128
#define QTDE_MAXIMA_ENTRADAS 32767
#define MAX_LENGTH_ENTRADA 100

/* SHORT TO CHARS */
char lowByteOfShort(short s) { return s & 0xff; }
char highByteOfShort(short s) { return s >> 8; }
/* CHARS TO SHORT */
short shortFromBytes(char low, char high) {
  short s = (unsigned char)high;
  s = s << 8;
  s = s & 0xff00;
  s = s | (unsigned char)low;
  return s;
}

void escreveCodigoAssociado(tDicionario *dicionario, FILE *arq_binario_saida,
                            char *entrada, char prim) {
  short code = obtemCodigo(dicionario, entrada);
  if (code >= 0) {
    char low = lowByteOfShort(code + PRIMEIRO_CODIGO_DEPOIS_ASCII);
    char high = highByteOfShort(code + PRIMEIRO_CODIGO_DEPOIS_ASCII);
    fputc(low, arq_binario_saida);
    fputc(high, arq_binario_saida);
  } else {
    int i;
    for (i = 0;; i++)
      if (entrada[i] != '\0') {
        fputc(entrada[i], arq_binario_saida);
        fprintf(arq_binario_saida, "%c", (char)0);
      } else
        break;
  }
}

tDicionario *compactaComLZW(FILE *arq_texto_entrada, FILE *arq_binario_saida) {
  tDicionario *dicionario = criaDicionario(QTDE_MAXIMA_ENTRADAS);
  char *entrada = (char *)malloc(sizeof(char) * MAX_LENGTH_ENTRADA);
  entrada[0] = '\0';

  int prim = 1;

  while (1) {
    char c = fgetc(arq_texto_entrada);
    if (c == EOF)
      break;

    char *entradaComC = (char *)malloc(sizeof(char) * MAX_LENGTH_ENTRADA);
    strcpy(entradaComC, entrada);
    strncat(entradaComC, &c, 1);

    if (strlen(entrada) > 0 && obtemCodigo(dicionario, entradaComC) < 0) {
      escreveCodigoAssociado(dicionario, arq_binario_saida, entrada, prim);
      prim = 0;
      adicionaEntrada(dicionario, entradaComC);

      entrada[0] = c;
      entrada[1] = '\0';
    } else {
      strncat(entrada, &c, 1);
      free(entradaComC);
    }
  }

  escreveCodigoAssociado(dicionario, arq_binario_saida, entrada, prim);
  return dicionario;
}

tDicionario *descompactaComLZW(FILE *arq_binario_entrada,
                               FILE *arq_texto_saida) {
  tDicionario *dicionario = criaDicionario(QTDE_MAXIMA_ENTRADAS);

  char low, high;
  low = fgetc(arq_binario_entrada);
  high = fgetc(arq_binario_entrada);

  char *entrada = (char *)malloc(sizeof(char) * MAX_LENGTH_ENTRADA);
  entrada[0] = low;
  entrada[1] = '\0';

  fputs(entrada, arq_texto_saida);

  while (1) {
    low = fgetc(arq_binario_entrada);
    high = fgetc(arq_binario_entrada);
    if (low == EOF && high == EOF)
      break;

    char *entrada_anterior = entrada;
    entrada = (char *)malloc(sizeof(char) * MAX_LENGTH_ENTRADA);

    if (low > 0 && high == 0) {
      entrada[0] = low;
      entrada[1] = '\0';
    } else {
      short s = shortFromBytes(low, high);
      short cod_dicionario = s - PRIMEIRO_CODIGO_DEPOIS_ASCII;
      char *str_dicionario = obtemEntrada(dicionario, cod_dicionario);
      strcpy(entrada, str_dicionario);
    }

    fputs(entrada, arq_texto_saida);

    strncat(entrada_anterior, entrada, 1);
    adicionaEntrada(dicionario, entrada_anterior);
    // free(entrada_anterior);
  }
  // free(entrada);

  return dicionario;
}

void imprimeDicionarioLZW(tDicionario *dicionario) {
  int i;
  for (i = 0; i < dicionario->tamanho; i++)
    printf("%5d | %s\n", i + PRIMEIRO_CODIGO_DEPOIS_ASCII,
           dicionario->entradas[i]);
}
