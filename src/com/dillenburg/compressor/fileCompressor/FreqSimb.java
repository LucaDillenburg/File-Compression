package com.dillenburg.compressor.fileCompressor;

public class FreqSimb
{
    protected int frequency;
    protected int symbol;

    public FreqSimb(int f)
    {
        this(f, -1);
    }

    public FreqSimb(int f, int s)
    {
        this.frequency = f;
        this.symbol = s;
    }

    public int getSymbol()
    {
        return this.symbol;
    }

    public int getFrequency()
    {
        return this.frequency;
    }

    public String toString()
    {
        return "Frequencia: " + this.getFrequency() + " Simbolo: " + this.getSymbol();
    }
}
