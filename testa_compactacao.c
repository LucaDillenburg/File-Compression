#include <stdio.h>
#include <stdbool.h>
#include "compactacao.h"

#define TAM_NOME_ARQ 25
#define NOME_ARQ_COMPACTADO "compactado.bin"
#define NOME_ARQ_DESCOMPACTADO "descompactado.txt"

int obtemTamanhoArquivo(FILE *arq);
bool arquivosSaoIguais(FILE *arq1, FILE *arq2);
void exibeDicionarioCompactacao(char *nome_arq_entrada);
void testaCompactacao(char *nome_arq_entrada, char *nome_arq_compactado_gabarito);
void exibeDicionarioDescompactacao(char *nome_arq_entrada);
void testaDescompactacao(char *nome_arq_compactado, char *nome_arq_descompactado_gabarito);

int main()
{
    int modo_operacao;
    char nome_arq_entrada[TAM_NOME_ARQ];
    char nome_arq_compactado[TAM_NOME_ARQ];
    char nome_arq_compactado_gabarito[TAM_NOME_ARQ];
    char nome_arq_descompactado_gabarito[TAM_NOME_ARQ];

    printf("Digite modo do programa: ");
    scanf("%d", &modo_operacao);

    switch (modo_operacao)
    {
    case 1: /* exibe dicionário da compactação */ 
        printf("Digite o nome do arquivo de texto a ser compactado: ");
        scanf("%s", nome_arq_entrada);

        exibeDicionarioCompactacao(nome_arq_entrada);
        break;
    case 2: /* testa compactação */
        printf("Digite o nome do arquivo de texto a ser compactado: ");
        scanf("%s", nome_arq_entrada);
        printf("Digite o nome do arquivo compactado gabarito: ");
        scanf("%s", nome_arq_compactado_gabarito);

        testaCompactacao(nome_arq_entrada,nome_arq_compactado_gabarito);
        break;
    case 3: /* exibe dicionário da descompactação */ 
        printf("Digite o nome do arquivo binário a ser descompactado: ");
        scanf("%s", nome_arq_compactado);

        exibeDicionarioDescompactacao(nome_arq_compactado);
        break;
    case 4: /* testa descompactação */
        printf("Digite o nome do arquivo binário a ser descompactado: ");
        scanf("%s", nome_arq_compactado);
        printf("Digite o nome do arquivo descompactado gabarito: ");
        scanf("%s", nome_arq_descompactado_gabarito);

        testaDescompactacao(nome_arq_compactado, nome_arq_descompactado_gabarito);
        break;
    default:
        printf("Modo de operação inválido!\n");
    }

    return 0;
}

/*
 * Função: obtemTamanhoArquivo
 * ---------------------------------
 * Devolve o tamanho em bytes do arquivo arq.
 */
int obtemTamanhoArquivo(FILE *arq)
{
    int pos_atual, tamanho;

    /* Obtém a posição atual no arquivo */
    pos_atual = ftell(arq);

    /* Vai para o fim do arquivo
       Posição no fim corresponde ao tamanho em bytes do arquivo */
    fseek(arq, 0L, SEEK_END);
    tamanho = ftell(arq);

    /* Volta para a posição onde estava inicialmente */
    fseek(arq, pos_atual, SEEK_SET);

    return tamanho;
}

/*
 * Função: arquivosSaoIguais
 * ---------------------------------
 * Devolve true quando o arquivo texto arq1 possui o mesmo conteúdo do
 * que o arquivo texto arq2. A função devolve false no caso contrário.
 */
bool arquivosSaoIguais(FILE *arq1, FILE *arq2)
{
    char c1, c2;

    /* Vai para o início dos arquivos */
    fseek(arq1, 0, SEEK_SET);
    fseek(arq2, 0, SEEK_SET);

    /* Compara os arquivos caracter a caracter */
    do
    {
        c1 = fgetc(arq1);
        c2 = fgetc(arq2);

        if (c1 != c2)
            return false;

    } while (c1 != EOF && c2 != EOF);

    return (c1 == EOF && c2 == EOF);
}

/*
 * Função: exibeDicionarioCompactacao
 * ---------------------------------
 * Imprime na saída padrão o dicionário criado para a compactação
 * do arquivo de texto cujo nome é passado em nome_arq_entrada.
 */
void exibeDicionarioCompactacao(char *nome_arq_entrada)
{
    FILE *arq_entrada, *arq_compactado;
    tDicionario *dicionario;

    printf(">>> IMPRESSÃO DO DICIONÁRIO DE COMPACTAÇÃO | Arquivo: %s\n", nome_arq_entrada);

    /* Abre para leitura o arquivo texto de entrada */  
    arq_entrada = fopen(nome_arq_entrada, "r");

    /* Cria o arquivo binário com os dados compactados */
    arq_compactado = fopen(NOME_ARQ_COMPACTADO, "wb");
    dicionario = compactaComLZW(arq_entrada, arq_compactado);

    fclose(arq_entrada);
    fclose(arq_compactado);

    /* Exibe o dicionário criado para a compactação */
    imprimeDicionarioLZW(dicionario);

    destroiDicionario(dicionario);
}

/*
 * Função: exibeDicionarioDescompactacao
 * ---------------------------------
 * Imprime na saída padrão o dicionário criado para a descompactação
 * do arquivo binário cujo nome é passado em nome_arq_compactado.
 */
void exibeDicionarioDescompactacao(char *nome_arq_compactado)
{
    FILE *arq_compactado, *arq_descompactado;;
    tDicionario *dicionario;

    printf(">>> IMPRESSÃO DO DICIONÁRIO DE DESCOMPACTAÇÃO | Arquivo: %s\n", 
           nome_arq_compactado);

    /* Abre para leitura o arquivo binário com o texto compactado */  
    arq_compactado = fopen(nome_arq_compactado, "rb");

    /* Cria o arquivo de texto com os dados descompactados */
    arq_descompactado = fopen(NOME_ARQ_COMPACTADO, "w");
    dicionario = descompactaComLZW(arq_compactado, arq_descompactado);

    fclose(arq_compactado);
    fclose(arq_descompactado);

    /* Exibe o dicionário criado para a compactação */
    imprimeDicionarioLZW(dicionario);

    destroiDicionario(dicionario);
}

/*
 * Função: testaCompactacao
 * ---------------------------------
 * Faz a compactação do arquivo de texto cujo nome é passado em 
 * nome_arq_entrada e compara o arquivo compactado gerado com o
 * arquivo compactado "gabarito", para verificar se a compactação
 * foi feita corretamente.
 */
void testaCompactacao(char *nome_arq_entrada, char *nome_arq_compactado_gabarito)
{
    FILE *arq_entrada, *arq_compactado, *arq_compactado_gabarito;
    tDicionario *dicionario;

    printf(">>> TESTE DE COMPACTAÇÃO | Arquivo: %s\n", nome_arq_entrada);

    /* Abre para leitura o arquivo de texto de entrada */  
    arq_entrada = fopen(nome_arq_entrada, "r");

    /* Cria o arquivo binário com os dados compactados */
    arq_compactado = fopen(NOME_ARQ_COMPACTADO, "wb");
    dicionario = compactaComLZW(arq_entrada, arq_compactado);
    fclose(arq_compactado);

   /* Abre para leitura os arquivos compactados */  
    arq_compactado = fopen(NOME_ARQ_COMPACTADO, "rb");
    arq_compactado_gabarito = fopen(nome_arq_compactado_gabarito, "rb");

    printf("Arquivo de texto de entrada: %s - Tamanho: %d bytes\n",
           nome_arq_entrada, obtemTamanhoArquivo(arq_entrada));
    printf("Arquivo binário com o texto compactado: %s - Tamanho: %d bytes\n",
           NOME_ARQ_COMPACTADO, obtemTamanhoArquivo(arq_compactado));
    printf("Tamanho esperado para o arquivo compactado: %d bytes\n",
           obtemTamanhoArquivo(arq_compactado_gabarito));

    if (arquivosSaoIguais(arq_compactado,arq_compactado_gabarito))
        printf("O conteúdo do arquivo compactado está CORRETO.\n");
    else
        printf("O conteúdo do arquivo compactado está INCORRETO.\n");

    fclose(arq_entrada);
    fclose(arq_compactado);
    fclose(arq_compactado_gabarito);

    destroiDicionario(dicionario);
}

/*
 * Função: testaDescompactacao
 * ---------------------------------
 * Faz a descompactação do arquivo binário cujo nome é passado em 
 * nome_arq_compactado e compara o arquivo descompactado gerado com o
 * arquivo descompactado "gabarito", para verificar se a descompactação
 * foi feita corretamente.
 */
void testaDescompactacao(char *nome_arq_compactado, char *nome_arq_descompactado_gabarito)
{
    FILE *arq_compactado, *arq_descompactado, *arq_descompactado_gabarito;
    tDicionario *dicionario;

    printf(">>> TESTE DE DESCOMPACTAÇÃO | Arquivo: %s\n", nome_arq_compactado);

    /* Abre para leitura o arquivo binário com os dados compactados */  
    arq_compactado = fopen(nome_arq_compactado, "rb");

    /* Cria o arquivo de texto com os dados descompactados */
    arq_descompactado = fopen(NOME_ARQ_DESCOMPACTADO, "w");
    dicionario = descompactaComLZW(arq_compactado, arq_descompactado);
    fclose(arq_descompactado);

   /* Abre para leitura os arquivos de texto descompactados */  
    arq_descompactado = fopen(NOME_ARQ_DESCOMPACTADO, "r");
    arq_descompactado_gabarito = fopen(nome_arq_descompactado_gabarito, "r");

    printf("Arquivo binário com os dados compactados: %s - Tamanho: %d bytes\n",
           nome_arq_compactado, obtemTamanhoArquivo(arq_compactado));
    printf("Arquivo com o texto descompactado: %s - Tamanho: %d bytes\n",
           NOME_ARQ_DESCOMPACTADO, obtemTamanhoArquivo(arq_descompactado));
    printf("Tamanho esperado para o arquivo descompactado: %d bytes\n",
           obtemTamanhoArquivo(arq_descompactado_gabarito));

    if (arquivosSaoIguais(arq_descompactado,arq_descompactado_gabarito))
        printf("O conteúdo do arquivo descompactado está CORRETO.\n");
    else 
        printf("O conteúdo do arquivo descompactado está INCORRETO.\n");

    fclose(arq_compactado);
    fclose(arq_descompactado);
    fclose(arq_descompactado_gabarito);

    destroiDicionario(dicionario);
}
