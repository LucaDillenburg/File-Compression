package com.dillenburg.huffmancompression.compressor.data;

public class Code
{
	protected String cod = "";
	
	public Code()
	{}
	
	public Code(String c)
	{
		this.cod = c;
	}
	
	public Code(byte[] vCodigo)
	{
		for(int i = 0; i < vCodigo.length; i++)
		{
			char c = (char) vCodigo[i];
			cod += cod;
		}
	}

	public void append(int i)
	{
		this.cod += i;
	}

	public void append(byte[] b)
	{
		for (int i = 0; i < b.length; i++)
			this.cod += b[i];
	}

	public byte getByte()
	{
		return (byte)Integer.parseInt(this.cod);
	}
	
	public int getAmntBits()
	{
		return this.cod.length();
	}

	public void popLast()
	{
		this.cod = this.cod.substring(0, this.cod.length() - 1);
	}

	public Code(Code c)
	{
		this.cod = c.cod;
	}

	public Object clone()
	{
		Code ret = null;

		try
		{
			ret = new Code(this);
		}catch(Exception e)
		{}

		return ret;
	}
	
	public String getCodigo()
	{
		return this.cod;
	}
	
	public String toString()
	{
		return this.cod;
	}

	public int toInt()
	{
		if(this.cod != "")
			return Integer.parseInt(this.cod);
		return 0;
	}
}