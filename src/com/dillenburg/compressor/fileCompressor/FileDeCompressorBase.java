package com.dillenburg.compressor.fileCompressor;

import com.dillenburg.auxiliar.MyRandomAccessFile;
import com.dillenburg.compressor.Code;
import com.dillenburg.compressor.CompressionUtils;
import com.dillenburg.dataStructures.binaryTree.TreeNode;

import java.io.File;

public class FileDeCompressorBase {

    // Header: length old extension, old extension, amnt bits trash, length array<No>

    protected Code[] codes;
    protected File file;
    protected int amntSimb;
    protected int[] frequencyArray;
    protected TreeNode<FreqSimb> root;

    protected String newName;

    public FileDeCompressorBase()
    {
        this.resetInfo();
    }

    protected void resetInfo()
    {
        this.codes = null;
        this.file = null;
        this.amntSimb = 0;
        this.frequencyArray = null;
        this.root = null;
        this.newName = null;
    }

    protected void insertCodesInTree()
    {
        this.codes = new Code[256];

        for(int i = 0; i < this.codes.length; i++)
            this.codes[i] = null;

        if(this.root != null)
        {
            if(this.root.getLeft()==null && this.root.getRight()==null && this.root.getInfo().getSymbol() >= 0)
                this.codes[this.root.getInfo().getSymbol()] = new Code("0");
            else
                this.insertCodesInTree(this.root, new Code());
        }
    }

    protected void insertCodesInTree(TreeNode<FreqSimb> r, Code c)
    {
        if(r != null)
        {
            int simb = r.getInfo().getSymbol();
            if(simb >= 0)
                this.codes[simb] = (Code)c.clone();
            else
            {
                c.append(0);
                this.insertCodesInTree(r.getLeft(), c);
                c.popLast();
                c.append(1);
                this.insertCodesInTree(r.getRight(), c);
                c.popLast();
            }
        }
    }

    protected TreeNode<FreqSimb> buildTree()
    {
        // ordered array
        TreeNode<FreqSimb>[] nos = new TreeNode[256];
        this.amntSimb = 0;

        for(int i = 0; i < this.frequencyArray.length; i++)
            if(this.frequencyArray[i] != 0)
            {
                try
                {
                    nos[this.amntSimb] = new TreeNode<FreqSimb>(new FreqSimb(this.frequencyArray[i], i));
                } catch (Exception e)
                {
                    e.printStackTrace();
                }

                this.amntSimb++;

                // order last inserted element
                changeNodePosition(nos, this.amntSimb);
            }


        for(int qtd = this.amntSimb; qtd>1; qtd--)
        {
            changeNodePosition(nos, qtd);
            int freq = nos[qtd-2].getInfo().getFrequency() + nos[qtd-1].getInfo().getFrequency();

            TreeNode<FreqSimb> aux = null;
            try
            {
                aux = new TreeNode<FreqSimb>(new FreqSimb(freq));
            }catch (Exception e){e.printStackTrace();}

            aux.setLeft(nos[qtd-2]);
            aux.setRight(nos[qtd-1]);
            nos[qtd-2] = aux;
            nos[qtd-1] = null;
        }

        //root
        return nos[0];
    }

    protected void changeNodePosition(TreeNode<FreqSimb>[] nos, int qtd)
    {
        for(int i=qtd-1; i > 0; i--)
        {
            if(((FreqSimb) nos[i].getInfo()).getFrequency() <= ((FreqSimb) nos[i-1].getInfo()).getFrequency())
                break;
            else
            {
                TreeNode<FreqSimb> aux = nos[i];
                nos[i] = nos[i-1];
                nos[i-1] = aux;
            }
        }
    }

    protected void buildFrequencyArray()
    {
        this.frequencyArray = new int[256];

        try
        {
            MyRandomAccessFile leitor = new MyRandomAccessFile(this.file, "r");
            for(int i = 0; i<leitor.length(); i++)
                this.frequencyArray[leitor.read()]++;
        }catch(Exception e)
        {}
    }

    protected int getBitFromByte(int byteAtual, int pos)
    {
        return ((byteAtual >> pos) & 0x01);
    }

    protected File createFile(String path, String extension)
    {
        File arquivo = new File(path + "." + extension);

        // no overwrite:
        int n = 1;
        while (arquivo.exists())
        {
            arquivo = new File(path + " (" + n + ")." + extension);
            n++;
        }
        return arquivo;
    }

    protected static String getFileExtension(File file) throws Exception
    {
        try
        {
            String nomeArq = file.getName();
            return nomeArq.substring(nomeArq.lastIndexOf(".")+1);
        }
        catch (Exception e)
        {
            throw new Exception("Error: coudn't find file extension!");
        }
    }

    protected String getFileDirectory()
    {
        try
        {
            String dir = this.file.getCanonicalPath();
            return dir.substring(0, dir.lastIndexOf('.'));
        }
        catch (Exception e) {
            return "";
        }
    }

    public static boolean isCompressed(File arquivo)
    {
        try
        {
            return (CompressionUtils.COMPRESSED_FORMAT.equals(FileCompressor.getFileExtension(arquivo)));
        }catch(Exception e)
        {
            return false;
        }
    }

}
