package com.dillenburg.huffmancompression

import com.dillenburg.huffmancompression.compressor.FileCompressor
import com.dillenburg.huffmancompression.compressor.FileDecompressor
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.system.exitProcess

fun getFileSize(fileName: String): Long {
    val path = Paths.get(fileName)
    return Files.size(path)
}

fun printSizeChange(inputSize: Long, outputSize: Long): Boolean {
    val rate = outputSize.toDouble() / inputSize.toDouble()
    if (rate < 1) {
        val decreased_perc = 1 - rate
        print("${"%.2f".format(decreased_perc * 100f)} decrease in size")
        return false
    } else {
        val increased_perc = rate - 1
        print("${"%.2f".format(increased_perc * 100f)} increase in size")
        return true
    }
}

fun main(args: Array<String>) {
    try {
        if (args.size < 2) {
            println("Missing arguments! The program expects two arguments in this order: the mode (compress or decompress) and the file name. \n")
            exitProcess(1)
        }

        val mode = args.get(0)
        if (mode != "compress" && mode != "decompress") {
            println("Unknown mode '$mode'. Available modes: 'compress' and 'decompress'.")
            exitProcess(2)
        }

        val inputPath = args.get(1)

        var outputPath: String
        if (mode == "compress") {
            val fileCompressor = FileCompressor(File(inputPath))
            fileCompressor.compress()
            outputPath = fileCompressor.writeToFile()
        } else {
            val fileDecompressor = FileDecompressor(File(inputPath))
            fileDecompressor.decompress()
            outputPath = fileDecompressor.writeToFile()
        }

        print("The file $inputPath was successfully ${mode}ed to $outputPath with a ")
        val increased = printSizeChange(getFileSize(inputPath), getFileSize(outputPath))
        print(".")
        if (increased && mode == "compress")
            print(" The compression resulted in a larger file! You should use the original file!")
    } catch (e: Exception) {
        print("Error: $e")
        exitProcess(3)
    }
}
