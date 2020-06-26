package com.dillenburg.compressor;

import java.io.File;
import java.io.FileNotFoundException;

import com.dillenburg.auxiliar.FileUtils;
import com.dillenburg.auxiliar.MyRandomAccessFile;
import com.dillenburg.compressor.data.FreqSimb;
import com.dillenburg.dataStructures.binaryTree.*;

public class FileDecompressor extends BaseCompression
{
    protected String oldExtension = null;
    protected int amntBitsTrash = -1;
    protected String decompressedStr = null;

    public FileDecompressor(File file) throws FileNotFoundException, Exception {
        if (file == null || !file.exists())
            throw new FileNotFoundException("Invalid file");
        if(!CompressionUtils.isCompressed(file))
            throw new Exception("This file was not compressed by this compressor!");
        this.file = file;
    }

    public void decompress() throws Exception
    {
        MyRandomAccessFile reader = new MyRandomAccessFile(this.file, "r");
        this.getHeaderInfo(reader);
        this.root = buildTree();
        this.insertCodesInTree();
    }

    protected void getHeaderInfo(MyRandomAccessFile reader)
    {
        try
        {
            int amntExtension = reader.read();
            this.oldExtension = "";
            for(int i = 0; i < amntExtension; i++)
                this.oldExtension += (char)reader.read();

            this.amntBitsTrash = reader.read();

            if(this.amntBitsTrash >= 0)
            {
                this.amntSimb = reader.readInt();

                this.frequencyArray = new int[256];
                for(int i = 0; i < this.amntSimb; i++)
                {
                    int index = reader.read();
                    int freq = reader.readInt();

                    this.frequencyArray[index] = freq;
                }
            }else
                this.frequencyArray = new int[256];
        }catch(Exception e)
        {
            System.err.println(e.getMessage() + e.getStackTrace());
        }
    }

    public String writeToFile()
    {
        String pathWithoutExtension = FileUtils.getPathWithoutExtension(this.file.getAbsolutePath());
        String pathNoOverwrite = FileUtils.getPathFileDoesntExist(pathWithoutExtension, this.oldExtension);
        auxWriteToFile(pathNoOverwrite);
        return pathNoOverwrite;
    }

    public void writeToFile(String pathWithoutExtension)
    {
        auxWriteToFile(pathWithoutExtension + "." + this.oldExtension);
    }

    protected void auxWriteToFile(String path)
    {
        try
        {
            File newFile =  new File(path);

            MyRandomAccessFile fileReader = new MyRandomAccessFile(this.file, "r");
            this.getHeaderInfo(fileReader);

            MyRandomAccessFile fileWriter = new MyRandomAccessFile(newFile, "rw");
            if(root != null)
            {
                if(this.root.getLeft() == null && this.root.getRight() == null)
                {
                    for (long iBytes = fileReader.getFilePointer(); iBytes < fileReader.length(); iBytes++)
                    {
                        int amntBits = 8;
                        if(iBytes == fileReader.length()-1)
                            amntBits -= this.amntBitsTrash;

                        for(int i = 0; i < amntBits; i++)
                            fileWriter.write(this.root.getInfo().getSymbol());
                    }
                }else
                {
                    TreeNode<FreqSimb> current = this.root;
                    for (long iBytes = fileReader.getFilePointer(); iBytes < fileReader.length(); iBytes++)
                    {
                        int currentByte = fileReader.read();

                        int amntBits = 8;
                        if(iBytes == fileReader.length()-1)
                            amntBits -= this.amntBitsTrash;

                        for(int i = 0; i < amntBits; i++)
                        {
                            int bit = getBitFromByte(currentByte, 7-i);

                            if((bit == 1 && current.getRight()==null) || (bit == 0 && current.getLeft()==null))
                            {
                                fileWriter.write((current.getInfo()).getSymbol());
                                current = this.root;
                            }

                            if(bit == 0)
                                current = current.getLeft();
                            else
                                current = current.getRight();

                            if(iBytes == fileReader.length() && i == amntBits-1)
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
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
