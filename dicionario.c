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

#include "dicionario.h"
#include <stdlib.h>
#include <string.h>

tDicionario *criaDicionario(short qtde_maxima_entradas) {
  int i;
  tDicionario *novo_dicionario = (tDicionario *)malloc(sizeof(tDicionario));

  novo_dicionario->capacidade = qtde_maxima_entradas;
  novo_dicionario->tamanho = 0;

  novo_dicionario->entradas =
      (char **)malloc(sizeof(char *) * qtde_maxima_entradas);
  for (i = 0; i < qtde_maxima_entradas; i++)
    novo_dicionario->entradas[i] = NULL;

  return novo_dicionario;
}

void destroiDicionario(tDicionario *dicionario) {
  int i;
  for (i = 0; i < dicionario->tamanho; i++)
    free(dicionario->entradas[i]);
  free(dicionario);
}

short adicionaEntrada(tDicionario *dicionario, char *entrada) {
  if (dicionario->tamanho == dicionario->capacidade)
    return -1;
  int codigo = dicionario->tamanho;
  dicionario->tamanho++;
  dicionario->entradas[codigo] = entrada;
  return codigo;
}

char *obtemEntrada(tDicionario *dicionario, short codigo) {
  if (codigo >= dicionario->tamanho || codigo < 0)
    return NULL;
  return dicionario->entradas[codigo];
}

short obtemCodigo(tDicionario *dicionario, char *entrada) {
  int i;
  for (i = 0; i < dicionario->tamanho; i++)
    if (strcmp(dicionario->entradas[i], entrada) == 0)
      return i;
  return -1;
}
