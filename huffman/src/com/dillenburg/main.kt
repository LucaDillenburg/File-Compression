package com.dillenburg

import com.dillenburg.compressor.FileCompressor
import com.dillenburg.compressor.FileDecompressor
import java.io.File

fun main(args: Array<String>) {
    try {
        println(" ## MENU ## ")
        println("1. Compress file")
        println("2. Decompress file")

        print("Which option do you choose? ")
        val option = readLine()

        if (option != "1" && option != "2")
            return

        print("Write the path to the file you want to compress: ")
        val path = readLine()

        var newPath: String
        if (option == "1") {
            val fileCompressor = FileCompressor(File(path))
            fileCompressor.compress()
            newPath = fileCompressor.writeToFile()
        }
        else {
            val fileDecompressor = FileDecompressor(File(path))
            fileDecompressor.decompress()
            newPath = fileDecompressor.writeToFile()
        }

        print("The file was successfully ${if (option=="1") "" else "de"}compressed. The name of the new file is $newPath")
    } catch (e: Exception) {
        print("Error: $e")
    }
}