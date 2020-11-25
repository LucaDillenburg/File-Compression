# Compactação LZW

O objetivo deste exerc´ıcio programa ´e implementar um algoritmo de compacta¸c˜ao sem perdas – o
LZW (Lempel-Ziv-Welch). O LZW ´e derivado do algoritmo LZ78, baseado na localiza¸c˜ao e no registro das
padronagens de uma estrutura. Foi desenvolvido e patenteado em 1984 por Terry Welch. Imagens GIF
s˜ao compactadas usando LZW para reduzir o tamanho do arquivo sem degradar a qualidade visual1
.
Apesar da sua efic´acia para a compacta¸c˜ao de arquivos de imagens, neste EP o LZW ser´a usado na
compacta¸c˜ao de arquivos de texto

## Explicação Algoritmo
– E se tiv´essemos c´odigos que, em lugar de representar um ´unico caractere, representassem uma
sequˆencia de 1 ou mais caracteres?
Resposta: Conseguir´ıamos codificar um texto com uma sequˆencia de n´umeros menor do que a que ´e necess´aria quando usamos somente os c´odigos da tabela ASCII
O algoritmo de compacta¸c˜ao LZW (assim como outros algoritmos de compacta¸c˜ao) explora essa ideia
para compactar arquivos de texto. Ele identifica as sequˆencias de 2 ou mais caracteres que aparecem no
arquivo de entrada e associa a essas sequˆencias um novo c´odigo. A primeira sequˆencia identificada recebe
o c´odigo 128 (que ´e o n´umero seguinte ao ´ultimo c´odigo usado na tabela ASCII); os c´odigos seguintes
s˜ao atribu´ıdos `as novas sequˆencias `a medida em que elas s˜ao identificadas. Os pares sequˆencia–c´odigo
identificados pelo algoritmo s˜ao mantidos em um dicion´ario, que aumenta `a medida em que o arquivo de
entrada ´e lido caractere por caractere. As entradas desse dicion´ario mais a pr´opria tabela ASCII s˜ao ent˜ao
usadas para codificar os dados do arquivo de entrada, resultando em um arquivo compactado.
Na descompacta¸c˜ao, o dicion´ario tamb´em ´e criado de forma dinˆamica, `a medida em que os dados do
arquivo compactado s˜ao lidos. Esse dicion´ario mais a pr´opria tabela ASCII s˜ao usados para decodificar o
arquivo compactado e gerar o arquivo de texto original

O seu programa n˜ao precisa funcionar para arquivos de entrada grandes (com mais de 50kB). Para
arquivos grandes, seria preciso um dicion´ario com mais de 30.000 entradas e, consequentemente,
c´odigos que passariam dos 2 bytes (que ´e o tamanho de uma vari´avel do tipo short).
Vocˆe pode considerar que o n´umero m´aximo de entradas no dicion´ario de uma compacta¸c˜ao/descompacta¸c˜ao
´e 30.000 e o tamanho m´aximo de uma entrada ´e 20.
• Como j´a dito anteriormente, neste exerc´ıcio lidaremos com arquivos de texto codificados em ASCII.
Portanto, n˜ao use em seus testes arquivos contendo caracteres que n˜ao pertencem ao alfabeto ASCII
(como ´e o caso de ´a, ˜a, ´e, ¸c, etc.).

O algoritmo de descompacta¸c˜ao descrito na Se¸c˜ao 3 n˜ao funciona quando o arquivo de entrada cont´em
uma sequˆencia de 3 caracteres ou strings repetidos (como “rrr” ou “aiaiai”). Portanto, n˜ao use em
seus testes sequˆencias de caracteres com essas caracter´ısticas.