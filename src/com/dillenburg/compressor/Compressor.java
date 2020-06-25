package com.dillenburg.compressor;

import com.dillenburg.compressor.fileCompressor.FileCompressor;

import java.io.File;
import java.io.FileNotFoundException;

public class Compressor
{
	public static String compress(File file) throws Exception
	{
		if (file == null)
			throw new FileNotFoundException("Null file");
		
		return Compressor.generalCompress(file, null);
	}
	
	public static String compress(File file, String newDirectory) throws Exception
	{
		if (file == null)
			throw new FileNotFoundException("Null file");
		if(newDirectory == null || newDirectory == "")
			throw new Exception("New directory is null!");
		
		return Compressor.generalCompress(file, newDirectory);
	}
	
	protected static String generalCompress(File file, String newDirectory) throws Exception
	{
		if(!file.isDirectory())
		{
			FileCompressor compressor = new FileCompressor();
			
			if(newDirectory == null || newDirectory == "")
				return compressor.compress(file);
			return compressor.compress(file, newDirectory);
		}else
		{
			String dir;
			if(newDirectory == null || newDirectory == "")
				dir = file.getCanonicalPath() + CompressionUtils.FILE_NAME_SUFIX;
			else
				dir = newDirectory;
			
			//not to overwrite any other directory
			String fileName = dir;
			int n = 1;
			while (new File(fileName).exists())
			{
				fileName = dir + " (" + n + ")";
				n++;
			}
			
			Compressor.compressAux(file, fileName, true);
			return fileName;
		}
	}
	
	protected static void compressAux(File file, String fileName, boolean firstTime)
	{
		if(!file.isDirectory())
		{
			FileCompressor compressor = new FileCompressor();

			try
			{
				if(fileName == null || fileName == "")
					compressor.compress(file);
				else
					compressor.compress(file, fileName);
			}catch (Exception e) {e.printStackTrace();}
		}else
		{
			if(!firstTime)
				fileName += CompressionUtils.FILE_NAME_SUFIX;
			
			for (final File insideFile : file.listFiles())
			{
				String nomeArquivo = CompressionUtils.getNewFileName(fileName, insideFile);
				Compressor.compressAux(insideFile, nomeArquivo, false);
		    }
		}
	}
}