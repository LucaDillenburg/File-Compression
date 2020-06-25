package com.dillenburg.compressor;

import com.dillenburg.compressor.fileCompressor.FileDecompressor;

import java.io.File;
import java.io.FileNotFoundException;

public class Decompressor {

    public static String decompress(File file) throws Exception
    {
        if (file == null)
            throw new FileNotFoundException("Arquivo nulo");

        return Decompressor.generalDecompress(file, null);
    }

    public static String decompress(File file, String newDir) throws Exception
    {
        if (file == null)
            throw new FileNotFoundException("Arquivo nulo");
        if(newDir == null || newDir == "")
            throw new Exception("Novo diretorio nulo!");

        return Decompressor.generalDecompress(file, newDir);
    }

    protected static String generalDecompress(File file, String newDir) throws Exception
    {
        if(!file.isDirectory())
        {
            FileDecompressor fileCompressor = new FileDecompressor();

            if(newDir == null || newDir == "")
                return fileCompressor.decompress(file);
            return fileCompressor.decompress(file, newDir);
        }else
        {
            String dir;
            if(newDir == null || newDir == "")
                dir = Decompressor.getNameDecompressed(file.getCanonicalPath());
            else
                dir = newDir;

            // not to overwrite:
            String nomeArq = dir;
            int n = 1;
            while (new File(nomeArq).exists())
            {
                nomeArq = dir + " (" + n + ")";
                n++;
            }

            Decompressor.decompressedAux(file, nomeArq, true);
            return nomeArq;
        }
    }

    protected static String getNameDecompressed(String newDir) throws Exception
    {
        int indexOf = newDir.lastIndexOf(CompressionUtils.FILE_NAME_SUFIX);
        if(indexOf<0)
            throw new Exception("Esse arquivo n�o foi compactado!");
        return newDir.substring(0, indexOf);
    }

    protected static void decompressedAux(File file, String fileName, boolean firstTime) throws Exception
    {
        if(!file.isDirectory())
        {
            FileDecompressor compactadorArq = new FileDecompressor();

            try
            {
                compactadorArq.decompress(file, fileName);
            }catch (Exception e) {e.printStackTrace();}
        }else
        {
            if(!firstTime)
                fileName = Decompressor.getNameDecompressed(fileName);
            for (final File insideFile : file.listFiles())
            {
                String nomeArquivo = CompressionUtils.getNewFileName(fileName, insideFile);
                Decompressor.decompressedAux(insideFile, nomeArquivo, false);
            }
        }
    }

}
