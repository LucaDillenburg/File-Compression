package com.dillenburg.compressor;

import com.dillenburg.auxiliar.FileUtils;
import com.dillenburg.auxiliar.MyRandomAccessFile;
import com.dillenburg.compressor.data.Code;
import com.dillenburg.compressor.data.FreqSimb;
import com.dillenburg.dataStructures.binaryTree.TreeNode;

import java.io.File;

public class BaseCompression {

    // Header: length old extension, old extension, amnt bits trash, length array<No>

    protected Code[] codes = null;
    protected File file = null;
    protected int amntSimb = 0;
    protected int[] frequencyArray = null;
    protected TreeNode<FreqSimb> root = null;

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
        TreeNode<FreqSimb>[] nodes = new TreeNode[256];
        this.amntSimb = 0;

        for(int i = 0; i < this.frequencyArray.length; i++)
            if(this.frequencyArray[i] != 0)
            {
                try
                {
                    nodes[this.amntSimb] = new TreeNode<FreqSimb>(new FreqSimb(this.frequencyArray[i], i));
                } catch (Exception e)
                {
                    e.printStackTrace();
                }

                this.amntSimb++;

                // order last inserted element
                changeNodePosition(nodes, this.amntSimb);
            }


        for(int amnt = this.amntSimb; amnt>1; amnt--)
        {
            changeNodePosition(nodes, amnt);
            int freq = nodes[amnt-2].getInfo().getFrequency() + nodes[amnt-1].getInfo().getFrequency();

            TreeNode<FreqSimb> aux = null;
            try
            {
                aux = new TreeNode<FreqSimb>(new FreqSimb(freq));
            }catch (Exception e){e.printStackTrace();}

            aux.setLeft(nodes[amnt-2]);
            aux.setRight(nodes[amnt-1]);
            nodes[amnt-2] = aux;
            nodes[amnt-1] = null;
        }

        //root
        return nodes[0];
    }

    protected void changeNodePosition(TreeNode<FreqSimb>[] nodes, int amnt)
    {
        for(int i=amnt-1; i > 0; i--)
        {
            if((nodes[i].getInfo()).getFrequency() <= (nodes[i-1].getInfo()).getFrequency())
                break;
            else
            {
                TreeNode<FreqSimb> aux = nodes[i];
                nodes[i] = nodes[i-1];
                nodes[i-1] = aux;
            }
        }
    }

    protected void buildFrequencyArray()
    {
        this.frequencyArray = new int[256];

        try
        {
            MyRandomAccessFile reader = new MyRandomAccessFile(this.file, "r");
            for(int i = 0; i<reader.length(); i++)
                this.frequencyArray[reader.read()]++;
        }catch(Exception e)
        {}
    }

    protected int getBitFromByte(int currentByte, int pos)
    {
        return ((currentByte >> pos) & 0x01);
    }

}
