package com.dillenburg.huffmancompression.compressor;

import com.dillenburg.huffmancompression.auxiliar.FileUtils;

import java.io.File;
import java.io.IOException;

public class CompressionUtils {
    public static final String COMPRESSED_FORMAT = "huff";

    public static boolean isCompressed(File file)
    {
        return CompressionUtils.COMPRESSED_FORMAT.equals(FileUtils.getFileExtension(file));
    }
}
