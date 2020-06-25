package com.dillenburg.compressor;

import com.dillenburg.compressor.fileCompressor.FileCompressor;

import java.io.File;
import java.io.IOException;

public class CompressionUtils {
    public static final String FILE_NAME_SUFIX = "-[Compressed]";
    public static final String COMPRESSED_FORMAT = "dillen";

    public static String getNewFileName(String fileName,File file) {
        try {
            boolean worked = new File(fileName).mkdirs();
            String path = file.getCanonicalPath();
            int indexUltimoPonto = path.lastIndexOf(".");
            if (indexUltimoPonto < 0)
                return fileName + path.substring(path.lastIndexOf("/"));
            else
                return fileName + path.substring(path.lastIndexOf("/"), indexUltimoPonto);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static boolean isCompressed(File arquivo)
    {
        if(arquivo == null)
            return false;

        if(!arquivo.isDirectory())
            return FileCompressor.isCompressed(arquivo);

        String diretorio = null;
        try
        {
            diretorio = arquivo.getCanonicalPath();
        } catch (IOException e) {}

        return diretorio.indexOf(FILE_NAME_SUFIX) >= 0;
    }

}
