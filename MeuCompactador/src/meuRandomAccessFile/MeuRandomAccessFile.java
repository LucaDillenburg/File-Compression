package meuRandomAccessFile;

import java.io.*;
import codigo.*;

public class MeuRandomAccessFile extends RandomAccessFile
{
	protected int bits;
	protected int qtdBitsUsados;
	
	public MeuRandomAccessFile(File arq, String s) throws FileNotFoundException
	{
		super(arq, s);
		this.inicializarVariaveis();
	}

	public MeuRandomAccessFile(String arq, String s) throws FileNotFoundException
	{      
        super(arq, s);
        this.inicializarVariaveis();
    }

	protected void inicializarVariaveis()
	{
		this.bits = 0;
		this.qtdBitsUsados = 0;
	}
	
	public void escreverCodigo(Codigo c) throws Exception
	{
		if (c == null)
			throw new Exception("Codigo nulo");
		
		String codigo = c.getCodigo();
		for(int i = 0; i < codigo.length(); i++)
		{		    
		    this.bits = this.bits << 1;
	        int bit = Integer.parseInt(codigo.charAt(i) + "");
	        this.bits = this.bits | bit;
	        this.qtdBitsUsados++;
			
			if(this.qtdBitsUsados == 8)
		    {
		        this.write(this.bits);
		        this.inicializarVariaveis();
		    }
		}	
	}
	
	public int getQtdLixo()
	{
		return (8 - this.qtdBitsUsados);
	}
	
	public void preencherLixo()
	{
		this.bits = this.bits << (8 - this.qtdBitsUsados);
		
		try
		{
			this.write(this.bits);
		}
		catch (Exception e) {}

		this.inicializarVariaveis();
	}
}