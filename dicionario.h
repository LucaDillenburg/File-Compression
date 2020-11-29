#ifndef DICIONARIO_H
#define DICIONARIO_H

/* Quantidade máxima de caracteres numa entrada no dicionário */
#define TAM_MAX_ENTRADA 20

/* Definição da estrutura de dados dicionário */
typedef struct {
  /* Vetor das entradas (strings) do dicionário  */
  char **entradas;

  /* Tamanho do vetor alocado para o dicionário, ou seja, quantidade
  máxima de entradas que o dicionário pode conter  */
  short capacidade;

  /* Número de posições do vetor em uso, ou seja,quantidade de entradas
  que o dicionário contém */
  short tamanho;
} tDicionario;

/*
 * Função: criaDicionario
 * ---------------------------------
 * Aloca dinamicamente um dicionário com a capacidade passada no parâmetro
 * qtde_maxima_entradas.
 * Na estrutura interna do dicionário, a função aloca espaço para
 * qtde_maxima_entradas strings de tamanho máximo TAM_MAX_ENTRADA.
 * A função devolve um ponteiro para o dicionário alocado.
 */
tDicionario *criaDicionario(short qtde_maxima_entradas);

/*
 * Função: destroiDicionario
 * ---------------------------------
 * Desaloca um dicionario alocado dinamicamente (desalocando, inclusive
 * os espaços alocados paras as strings das entradas que mantém).
 */
void destroiDicionario(tDicionario *dicionario);

/*
 * Função: adicionaEntrada
 * ---------------------------------
 * Se o dicionário não está cheio, a função adiciona a entrada nele.
 * Nesse caso, a entrada é incluída na posição seguinte à última posição
 * atualmente ocupada no vetor de entradas.
 * A função devolve o código da entrada no dicionário (ou seja, o número
 * da posição onde a entrada foi incluída) ou devolve -1 quando o
 * dicionário está cheio.
 */
short adicionaEntrada(tDicionario *dicionario, char *entrada);

/*
 * Função: obtemEntrada
 * ---------------------------------
 * A função devolve a entrada que corresponde ao código no dicionário.
 * O código indica a posição em que a entrada está armazenada.
 * A função devolve NULL quando o código passado no parâmetro é inválido.
 */
char *obtemEntrada(tDicionario *dicionario, short codigo);

/*
 * Função: obtemCodigo
 * ---------------------------------
 * A função devolve o código da entrada no dicionário (ou seja, o número
 * da posição onde ela está armazenada) ou devolve -1 quando a entrada
 * não está no dicionário.
 */
short obtemCodigo(tDicionario *dicionario, char *entrada);

#endif