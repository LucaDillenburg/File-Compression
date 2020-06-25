package com.dillenburg.auxiliar;

import java.io.*;
import com.dillenburg.compressor.Code;

public class MyRandomAccessFile extends RandomAccessFile
{
	protected int bits;
	protected int amntBitsUsed;
	
	public MyRandomAccessFile(File file, String s) throws FileNotFoundException
	{
		super(file, s);
		this.initVariables();
	}

	public MyRandomAccessFile(String file, String s) throws FileNotFoundException
	{      
        super(file, s);
        this.initVariables();
    }

	protected void initVariables()
	{
		this.bits = 0;
		this.amntBitsUsed = 0;
	}
	
	public void writeCode(Code code) throws Exception
	{
		if (code == null)
			throw new Exception("Code is null");
		
		String strCode = code.getCodigo();
		for(int i = 0; i < strCode.length(); i++)
		{		    
		    this.bits = this.bits << 1;
	        int bit = Integer.parseInt(strCode.charAt(i) + "");
	        this.bits = this.bits | bit;
	        this.amntBitsUsed++;
			
			if(this.amntBitsUsed == 8)
		    {
		        this.write(this.bits);
		        this.initVariables();
		    }
		}	
	}
	
	public int getAmntTrashBits()
	{
		return (8 - this.amntBitsUsed);
	}
	
	public void fillTrashBits()
	{
		this.bits = this.bits << (8 - this.amntBitsUsed);
		
		try
		{
			this.write(this.bits);
		}
		catch (Exception e) {}

		this.initVariables();
	}
}