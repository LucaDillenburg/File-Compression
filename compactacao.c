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
*/

#include "compactacao.h"
#include "dicionario.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define PRIMEIRO_CODIGO_DEPOIS_ASCII 128
#define QTDE_MAXIMA_ENTRADAS 32767
#define MAX_LENGTH_ENTRADA 100

/*
 * Função: byteBaixoDoCodigo
 * ---------------------------------
 * A funcao retorna o byte baixo do short.
 */
char byteBaixoDoCodigo(short s) { return s & 0xff; }

/*
 * Função: highByteOfShort
 * ---------------------------------
 * A funcao retorna o byte alto do short.
 */
char byteAltoDoCodigo(short s) { return s >> 8; }

/*
 * Função: concatBytes
 * ---------------------------------
 * A funcao retorna um byte de tal forma que o byte baixo do mesmo
 * eh o primeiro parametro e o alto, a segunda.
 */
short concatBytes(char baixo, char alto) {
  short s = (unsigned char)alto;
  s = s << 8;
  s = s & 0xff00;
  s = s | (unsigned char)baixo;
  return s;
}

/*
 * Função: escreveCodigoAssociado
 * ---------------------------------
 * Escreve codigo da entrada.
 * A função tenta obter esse codigo com o dicionario e entrada.
 * Caso o codigo seja valido, ela escreve primeiro o byte baixo e
 * depois o alto do codigo. Caso contrario, ela escreve os caracteres
 * da entrada com um '\0' entre cada um deles.
 */
void escreveCodigoAssociado(tDicionario *dicionario, FILE *arq_binario_saida,
                            char *entrada, char prim) {
  short codigo = obtemCodigo(dicionario, entrada);
  if (codigo >= 0) {
    char baixo = byteBaixoDoCodigo(codigo + PRIMEIRO_CODIGO_DEPOIS_ASCII);
    char alto = byteAltoDoCodigo(codigo + PRIMEIRO_CODIGO_DEPOIS_ASCII);
    fputc(baixo, arq_binario_saida);
    fputc(alto, arq_binario_saida);
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

  int primeiro_vez = 1;

  while (1) {
    char char_atual = fgetc(arq_texto_entrada);
    if (char_atual == EOF)
      break;

    char *entrada_com_ultimo_char_lido =
        (char *)malloc(sizeof(char) * MAX_LENGTH_ENTRADA);
    strcpy(entrada_com_ultimo_char_lido, entrada);
    strncat(entrada_com_ultimo_char_lido, &char_atual, 1);

    if (strlen(entrada) > 0 &&
        obtemCodigo(dicionario, entrada_com_ultimo_char_lido) < 0) {
      escreveCodigoAssociado(dicionario, arq_binario_saida, entrada,
                             primeiro_vez);
      primeiro_vez = 0;
      adicionaEntrada(dicionario, entrada_com_ultimo_char_lido);

      entrada[0] = char_atual;
      entrada[1] = '\0';
    } else {
      strncat(entrada, &char_atual, 1);
      free(entrada_com_ultimo_char_lido);
    }
  }

  escreveCodigoAssociado(dicionario, arq_binario_saida, entrada, primeiro_vez);
  return dicionario;
}

tDicionario *descompactaComLZW(FILE *arq_binario_entrada,
                               FILE *arq_texto_saida) {
  tDicionario *dicionario = criaDicionario(QTDE_MAXIMA_ENTRADAS);
  int primeira_vez = 1;

  char *entrada = (char *)malloc(sizeof(char) * MAX_LENGTH_ENTRADA);
  entrada[0] = '\0';
  while (1) {
    char byte_baixo = fgetc(arq_binario_entrada);
    char byte_alto = fgetc(arq_binario_entrada);
    if (byte_baixo == EOF && byte_alto == EOF)
      break;

    char *entrada_anterior = entrada;
    entrada = (char *)malloc(sizeof(char) * MAX_LENGTH_ENTRADA);

    if (byte_baixo > 0 && byte_alto == 0) {
      entrada[0] = byte_baixo;
      entrada[1] = '\0';
    } else {
      short s = concatBytes(byte_baixo, byte_alto);
      short cod_dicionario = s - PRIMEIRO_CODIGO_DEPOIS_ASCII;
      char *str_dicionario = obtemEntrada(dicionario, cod_dicionario);
      strcpy(entrada, str_dicionario);
    }

    fputs(entrada, arq_texto_saida);

    if (!primeira_vez) {
      strncat(entrada_anterior, entrada, 1);
      adicionaEntrada(dicionario, entrada_anterior);
    } else {
      primeira_vez = 0;
      free(entrada_anterior);
    }
  }

  return dicionario;
}

void imprimeDicionarioLZW(tDicionario *dicionario) {
  int i;
  for (i = 0; i < dicionario->tamanho; i++)
    printf("%5d | %s\n", i + PRIMEIRO_CODIGO_DEPOIS_ASCII,
           dicionario->entradas[i]);
}
