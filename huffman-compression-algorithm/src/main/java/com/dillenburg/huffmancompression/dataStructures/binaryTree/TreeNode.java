package com.dillenburg.huffmancompression.dataStructures.binaryTree;

import com.dillenburg.huffmancompression.auxiliar.GenericClone;

public class TreeNode<X>
{
	protected TreeNode prev = null;
	protected X  info;
	protected TreeNode left = null;
	protected TreeNode right = null;

	public TreeNode(X x) throws Exception
	{
		this(null, null, x, null);
	}

	public TreeNode(X x, TreeNode d) throws Exception
	{
		this(null, null, x, d);
	}

	public TreeNode(TreeNode e, X x, TreeNode d) throws Exception
	{
		this(null, e, x, d);

		if(e != null)
			e.prev = this;
		if(d != null)
			d.prev = this;
	}

	public TreeNode(TreeNode a, TreeNode e, X x, TreeNode d) throws Exception
	{
		this.setInfo(x);
		this.prev = a;
		this.left = e;
		this.right = d;
	}


	public void setPrev(TreeNode treeNode)
	{
		this.prev = treeNode;
	}

	public void setInfo(X x) throws Exception
	{
		if(x == null)
			throw new Exception("Informacao nula!");
		this.info = (X) GenericClone.clone(x);
	}

	public void setLeft(TreeNode<X> treeNode)
	{
		this.left = treeNode;
	}

	public void setRight(TreeNode<X> treeNode)
	{
		this.right = treeNode;
	}

	public TreeNode getPrev()
	{
		return this.prev;
	}

	public X getInfo()
	{
		return this.info;
	}

	public TreeNode<X> getLeft()
	{
		return this.left;
	}

	public TreeNode<X> getRight()
	{
		return this.right;
	}

    public String toString()
    {
        return "{" + this.left +
        	"(" + this.info + ") " + this.right;
    }

    public int hashCode()
    {
        int ret = 3;

        if (this.left != null)
            ret = ret*7 + this.left.hashCode();

        ret = ret*7 + this.info.hashCode();

        if (this.right != null)
            ret = ret*7 + this.right.hashCode();

        return ret;
    }

    public boolean equals(Object obj)
    {
        if (obj == null)
            return false;
        if (obj == this)
            return true;

        if (obj.getClass() != this.getClass())
            return false;

        TreeNode<X> treeNode = (TreeNode<X>)obj;

        if (!this.info.equals(treeNode.info))
        	return false;

        if (this.left != null)
        {
            if(!this.left.equals(treeNode.left))
                return false;
        }else
        	if (treeNode.left != null)
        		return false;

        if (this.right != null) {
            if (!this.right.equals(treeNode.right))
                return false;
        } else
             if (treeNode.right != null)
                return false;

        return true;
    }

    public TreeNode(TreeNode<X> model) throws Exception
    {
    	if (model == null)
    		throw new Exception("Null Model!");

    	this.info = model.info;

    	if (model.right != null)
    		this.right = (TreeNode<X>)model.right.clone();
    	else
    		this.right = null;

    	if (model.left != null)
    		this.left = (TreeNode<X>)model.left.clone();
    	else
    		this.left = null;
    }

    public Object clone()
    {
    	TreeNode<X> ret = null;

        try {
            ret = new TreeNode<X>(this);
        } 
        catch (Exception e) {}

        return ret;
    }
}
