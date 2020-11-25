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

tDicionario *criaDicionario(short qtdeMaximaEntradas) {
  tDicionario *novoDicionario = (tDicionario *)malloc(sizeof(tDicionario));
  novoDicionario->entradas =
      (char **)malloc(sizeof(char *) * qtdeMaximaEntradas);
  novoDicionario->capacidade = qtdeMaximaEntradas;
  novoDicionario->tamanho = 0;
  return novoDicionario;
}

/*
 * Função: destroiDicionario
 * ---------------------------------
 * Desaloca um dicionario alocado dinamicamente (desalocando, inclusive
 * os espaços alocados paras as strings das entradas que mantém).
 */
void destroiDicionario(tDicionario *dicionario) {
  int i;
  for (i = 0; i < dicionario->tamanho; i++)
    free(dicionario->entradas[i]);
  free(dicionario);
}
