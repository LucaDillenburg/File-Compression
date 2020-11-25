#ifndef COMPACTACAO_H
#define COMPACTACAO_H

#include <stdio.h>
#include "dicionario.h"

/*
 * Função: compactaComLZW
 * ---------------------------------
 * A função recebe como entrada um arquivo texto aberto para leitura e um 
 * arquivo binário aberto para escrita.
 * A função lê o arquivo de texto de entrada, compacta o seu conteúdo usando
 * o método LZW e grava o resultado da compactação no arquivo binário de 
 * saída.
 * A função devolve um ponteiro para o dicionário criado na compactação.
 */
tDicionario *compactaComLZW(FILE *arq_texto_entrada, FILE *arq_binario_saida);

/*
 * Função: descompactaComLZW
 * ---------------------------------
 * A função recebe como entrada um arquivo binário aberto para leitura e
 * um arquivo de texto aberto para escrita.
 * A função lê o arquivo binário de entrada e descompacta o seu conteúdo 
 * usando o método LZW. O texto descompactado é gravado no arquivo de texto
 * de saída.
 * A função devolve um ponteiro para o dicionário criado na 
 * descompactação.
 */
tDicionario *descompactaComLZW(FILE *arq_binario_entrada, FILE *arq_texto_saida);

/*
 * Função: imprimeDicionarioLZW
 * ---------------------------------
 * A função recebe como entrada um dicionário criado numa compactação ou
 * descompactação com LZW e o imprime na saída padrão.
 * A função imprime um par (código, entrada) por linha, sendo que o código
 * é separado da entrada por " | ". 
 * Um código é impresso na saída como um número inteiro de 5 dígitos 
 * (preenchido com espaços à esquerda). 
 * Exemplo de impressão feita pela função:
 
  128 | as
  129 | s_
  130 | _a
  131 | asa

 */
void imprimeDicionarioLZW(tDicionario *dicionario);

#endif