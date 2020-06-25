package com.dillenburg.compressor.fileCompressor;

import java.io.File;
import java.io.FileNotFoundException;

import com.dillenburg.auxiliar.MyRandomAccessFile;
import com.dillenburg.compressor.CompressionUtils;

public class FileCompressor extends FileDeCompressorBase
{
	public String compress(File file, String newName) throws Exception
	{
		if (file == null)
			throw new FileNotFoundException("Null file");
		
		this.resetInfo();
		this.file = file;
		this.newName = newName;
		
		return this.compress();
	}
	
	public String compress(File file) throws Exception
	{
		if (file == null)
			throw new FileNotFoundException("Null file");
		
		this.resetInfo();
		this.file = file;
		
		return this.compress();
	}
	
	protected String compress()
	{
		this.buildFrequencyArray();
		this.root = buildTree();
		this.insertCodesInTree();
		return this.writeFile();
	}
	
	protected String writeFile()
	{
		try
		{
			String fileExtension = FileCompressor.getFileExtension(this.file);
			File fileName;
			if(this.newName == null)
				fileName = this.createFile(this.getFileDirectory(), CompressionUtils.COMPRESSED_FORMAT);
			else
				fileName = new File(this.newName + "." + CompressionUtils.COMPRESSED_FORMAT);
			
			MyRandomAccessFile writer = new MyRandomAccessFile(fileName, "rw");

			writer.write(fileExtension.length());
			for (int i = 0; i < fileExtension.length(); i++)
				writer.write(fileExtension.charAt(i));
			
			if(this.root != null)
			{
				MyRandomAccessFile reader = new MyRandomAccessFile(this.file, "r");

				long posTrash = writer.getFilePointer();
				writer.write(0);

				writer.writeInt(this.amntSimb);

				for (int i = 0; i < this.frequencyArray.length; i++)
					if (this.frequencyArray[i] > 0)
					{
						writer.write(i);
						writer.writeInt(this.frequencyArray[i]);
					}

				for (int i = 0; i < reader.length(); i++)
				{
					int ind = reader.read();
					writer.writeCode(this.codes[ind]);
				}

				int amntBitsTrash = writer.getAmntTrashBits();
				writer.fillTrashBits();

				writer.seek(posTrash);
				writer.write(amntBitsTrash);
				
				reader.close();
			}
			writer.close();
			
			return fileName.getAbsolutePath();
		}catch(Exception e)
		{
			return "";
		}
	}
}