package com.dillenburg.compressor.fileCompressor;

import java.io.File;
import java.io.FileNotFoundException;

import com.dillenburg.auxiliar.MyRandomAccessFile;
import com.dillenburg.compressor.CompressionUtils;
import com.dillenburg.dataStructures.binaryTree.*;

public class FileDecompressor extends FileDeCompressorBase
{
    protected String oldExtension;
    protected int amntBitsTrash;

    @Override
    protected void resetInfo()
    {
        super.resetInfo();
        this.oldExtension = null;
        this.amntBitsTrash = -1;
    }

    public String decompress(File file, String fileName) throws Exception
    {
        if (file == null)
            throw new FileNotFoundException("Null file");

        this.resetInfo();
        this.file = file;
        if(!CompressionUtils.COMPRESSED_FORMAT.equals(FileCompressor.getFileExtension(this.file)))
            throw new Exception("This file was not compressed by Dillen's Compresser!");
        this.newName = fileName;

        return this.decompress();
    }

    public String decompress(File file) throws Exception
    {
        if (file == null)
            throw new FileNotFoundException("Null file");

        this.resetInfo();
        this.file = file;
        if(!CompressionUtils.COMPRESSED_FORMAT.equals(FileCompressor.getFileExtension(this.file)))
            throw new Exception("This file was not compressed by Dillen's Compresser!");

        return this.decompress();
    }

    protected String decompress() throws Exception
    {
        MyRandomAccessFile leitor = new MyRandomAccessFile(this.file, "r");
        this.getHeaderInfo(leitor);
        this.root = buildTree();
        this.insertCodesInTree();
        return this.writeFile(leitor);
    }

    protected void getHeaderInfo(MyRandomAccessFile leitor)
    {
        try
        {
            int qtdExtensao = leitor.read();
            this.oldExtension = "";
            for(int i = 0; i < qtdExtensao; i++)
                this.oldExtension += (char)leitor.read();

            this.amntBitsTrash = leitor.read();

            if(this.amntBitsTrash >= 0)
            {
                this.amntSimb = leitor.readInt();

                this.frequencyArray = new int[256];
                for(int i = 0; i < this.amntSimb; i++)
                {
                    int indice = leitor.read();
                    int freq = leitor.readInt();

                    this.frequencyArray[indice] = freq;
                }
            }else
                this.frequencyArray = new int[256];
        }catch(Exception e)
        {
            System.err.println(e.getMessage() + e.getStackTrace());
        }
    }

    protected String writeFile(MyRandomAccessFile fileReader)
    {
        try
        {
            File newFile = null;
            if(this.newName == null)
                newFile = this.createFile(this.getFileDirectory(), this.oldExtension);
            else
                newFile = new File(this.newName + "." + this.oldExtension);

            MyRandomAccessFile fileWriter = new MyRandomAccessFile(newFile, "rw");
            if(root != null)
            {
                if(this.root.getLeft() == null && this.root.getRight() == null)
                {
                    for (long iBytes = fileReader.getFilePointer(); iBytes < fileReader.length(); iBytes++)
                    {
                        int qtdBits = 8;
                        if(iBytes == fileReader.length()-1)
                            qtdBits -= this.amntBitsTrash;

                        for(int i = 0; i < qtdBits; i++)
                            fileWriter.write(this.root.getInfo().getSymbol());
                    }
                }else
                {
                    TreeNode<FreqSimb> current = this.root;
                    for (long iBytes = fileReader.getFilePointer(); iBytes < fileReader.length(); iBytes++)
                    {
                        int byteAtual = fileReader.read();

                        int qtdBits = 8;
                        if(iBytes == fileReader.length()-1)
                            qtdBits -= this.amntBitsTrash;

                        for(int i = 0; i < qtdBits; i++)
                        {
                            int bit = getBitFromByte(byteAtual, 7-i);

                            if((bit == 1 && current.getRight()==null) || (bit == 0 && current.getLeft()==null))
                            {
                                fileWriter.write((current.getInfo()).getSymbol());
                                current = this.root;
                            }

                            if(bit == 0)
                                current = current.getLeft();
                            else
                                current = current.getRight();

                            if(iBytes == fileReader.length() && i == qtdBits-1)
                                if((bit == 1 && current.getRight()==null) || (bit == 0 && current.getLeft()==null))
                                {
                                    fileWriter.write((current.getInfo()).getSymbol());
                                    current = this.root;
                                }
                        }
                    }

                    fileWriter.write((current.getInfo()).getSymbol());
                }
            }
            fileWriter.close();
            fileReader.close();

            return newFile.getAbsolutePath();
        }catch(Exception e)
        {
            e.printStackTrace();
            return "";
        }
    }
}
