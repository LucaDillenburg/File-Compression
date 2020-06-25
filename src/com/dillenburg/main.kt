package com.dillenburg

import com.dillenburg.compressor.Compressor
import com.dillenburg.compressor.Decompressor
import java.io.File

fun main(args: Array<String>) {
    try {
        println(" ## MENU ## ")
        println("1. Compress file or folder")
        println("2. Decompress file or folder")

        print("Which option do you choose? ")
        val option = readLine()

        print("Write the path to the file or directory you want to compress: ")
        val path = readLine()

        var newPath: String
        if (option == "1")
            newPath = Compressor.compress(File(path))
        else if (option == "2")
            newPath = Decompressor.decompress(File(path))
        else
            return

        print("The file/directory was successfully ${if (option=="1") "" else "de"}compressed. The name of the new file/directory is $newPath")
    } catch (e: Exception) {
        e.printStackTrace()
    }
}