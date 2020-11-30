# File Compressors and Decompressors
Set of file compression algorithms.

## Algorithms
1. [Huffman Compression Algorithm](Huffman-Compression-Algorithm)
2. [Lempel-Ziv-Welch Compression Algorithm](Lempel-Ziv-Welch-Compression-Algorithm)

## Huffman Compression Algorithm
This is algorithm is based uppon having the most common bytes in a file being represented using less than 8 bits. In this case, this is achieved using a binary tree that has to be written to the compressed file. Check out a visual representation of the algorithm below:
![Huffman Compression](media/huffman-compression-algorithm.gif)
For more information you can access: [https://www.geeksforgeeks.org/huffman-coding-greedy-algo-3/](https://www.geeksforgeeks.org/huffman-coding-greedy-algo-3/).

#### Run
```sh
mvn clean package #compile
java -jar target/huffman-compression-algorithm-1.0-SNAPSHOT-jar-with-dependencies.jar #run
```

#### Example
You can find some examples in the ```examples``` directory:
- Lorem Ipsum (```examples/lorem-ipsum```)

	The ```ls -l``` commands shows as follows:
	```
	-rw-r--r-- your_user_name your_group  93.8KB Jun 25 22:44 compressed.huff
	-rw-r--r-- your_user_name your_group 175.1KB Jun 25 22:44 original_file.txt
	```
	This represents a **46.6% percent compression rate**.
- Book (```examples/book```)

	The ```ls -l``` commands shows as follows:
	```
	-rw-r--r-- your_user_name your_group 25.6KB Jun 25 22:44 compressed.huff
	-rw-r--r-- your_user_name your_group 46.3KB Jun 25 22:44 original_file.txt
	```
	This represents a **X% percent compression rate**.

## Lempel-Ziv-Welch-Compression-Algorithm
This algorithm is based a group of bits that can store more than one or more bytes. An entry table is generated with the bytes that have already been read so when the same sequence is read again it will be substituted to the associated code in the entry table. A really important thing is that the entry table doesn't have to be written in the compressed file since it can be generated again by the decompressor.

For more info you can access: [https://www.geeksforgeeks.org/lzw-lempel-ziv-welch-compression-technique/](https://www.geeksforgeeks.org/lzw-lempel-ziv-welch-compression-technique/) .

#### Run
```sh
make
./lzw compress file #compress
./lzw decompress compressedFile #decompress
```

#### Example
You can find some examples in the ```examples``` directory:
- Lorem Ipsum (```examples/lorem-ipsum```)

	The ```ls -l``` commands shows as follows:
	```
	-rw-r--r-- your_user_name your_group  56.7KB Jun 25 22:44 compressed_lzw
	-rw-r--r-- your_user_name your_group 175.1KB Jun 25 22:44 original_file.txt
	```
	This represents a **67.6% percent compression rate**.
- Book (```examples/book```)

	The ```ls -l``` commands shows as follows:
	```
	-rw-r--r-- your_user_name your_group 24.7KB Jun 25 22:44 compressed_lzw
	-rw-r--r-- your_user_name your_group 46.3KB Jun 25 22:44 original_file.txt
	```
	This represents a **46.7% percent compression rate**.

#### Limitations
- Size
  
  Since in this implementation the number of bits per code is fixed in 16 bits, the maximum number of elements in the entry table is 32767. Therefore, the algorithm may not work with files larger than 50kB. However, the LZW algorithm can be implemented to support a larger range of files (from the smallest to the largest ones).

- Extension
  
  In this implementation, the compressed file doesn't contain the extension of the original file. However, this can be achieved rather easily by adding the extension string as the first bytes of the file.

