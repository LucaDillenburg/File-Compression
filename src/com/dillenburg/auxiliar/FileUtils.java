package com.dillenburg.auxiliar;

import java.io.File;

public class FileUtils {
    public static String getPathFileDoesntExist(String pathNoExtension, String extension)
    {
        // no overwrite:
        int n = 1;
        String path = pathNoExtension + "." + extension;

        File file = new File(path);
        while (file.exists())
        {
            path = pathNoExtension + " (" + n + ")." + extension;
            file = new File(path);
            n++;
        }
        return path;
    }

    public static String getPathWithoutExtension(String path)
    {
        try
        {
            int indexOfDot = path.lastIndexOf(".");
            if (indexOfDot < 0)
                return path;
            else
                return path.substring(0, indexOfDot);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static String getFileExtension(File file)
    {
        try
        {
            String fileName = file.getName();
            return fileName.substring(fileName.lastIndexOf(".")+1);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
