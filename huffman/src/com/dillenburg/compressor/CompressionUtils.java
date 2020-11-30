package com.dillenburg.compressor;

import com.dillenburg.auxiliar.FileUtils;

import java.io.File;
import java.io.IOException;

public class CompressionUtils {
    public static final String COMPRESSED_FORMAT = "yeah";

    public static boolean isCompressed(File file)
    {
        return CompressionUtils.COMPRESSED_FORMAT.equals(FileUtils.getFileExtension(file));
    }
}
