package com.dillenburg.compressor;

import java.io.File;
import java.io.FileNotFoundException;

import com.dillenburg.auxiliar.FileUtils;
import com.dillenburg.auxiliar.MyRandomAccessFile;

public class FileCompressor extends BaseCompression
{
	public FileCompressor(File file) throws FileNotFoundException {
		if (file == null || !file.exists())
			throw new FileNotFoundException("Invalid file");
		this.file = file;
	}
	
	public void compress()
	{
		this.buildFrequencyArray();
		this.root = buildTree();
		this.insertCodesInTree();
	}

	public String writeToFile()
	{
		String pathWithoutExtension = FileUtils.getPathWithoutExtension(this.file.getAbsolutePath());
		String pathNoOverwrite = FileUtils.getPathFileDoesntExist(pathWithoutExtension, CompressionUtils.COMPRESSED_FORMAT);
		auxWriteToFile(pathNoOverwrite);
		return pathNoOverwrite;
	}

	public void writeToFile(String pathWithoutExtension) {
		auxWriteToFile(pathWithoutExtension + "." + CompressionUtils.COMPRESSED_FORMAT);
	}

	protected void auxWriteToFile(String path)
	{
		try
		{
			String fileExtension = FileUtils.getFileExtension(this.file);
			File file = new File(path);
			
			MyRandomAccessFile writer = new MyRandomAccessFile(file, "rw");

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
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}