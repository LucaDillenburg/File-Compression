package com.dillenburg.huffmancompression.compressor.dataStructures.listaDesordenada;
import com.dillenburg.huffmancompression.auxiliar.GenericClone;

public class UnorderedList<X> implements Cloneable{

	protected Node<X> front = null;
	protected Node<X> back = null;
	
	public UnorderedList()
	{}
	
	public X front()
	{
		return this.front.getInfo();
	}
	
	public X get(int index) throws Exception
	{
		Node<X> current = this.front;
		for(int i = 0; i < index && current != null;i++)
			current = current.getNext();
		if (current==null)
			throw new Exception("Invalid index");
		
		return current.getInfo();
	}

	public void pushFront(X x) throws Exception
	{
		if (x == null)
			throw new Exception("Null information");

		X info;
		if (x instanceof Cloneable)
			info = (X) GenericClone.clone(x);
		else
			info = x;

		this.front = new Node(info, this.front);
		
		if(this.front.getNext()==null)
			this.back = this.front;
		else
		{
			this.front.getNext().prev = this.front;
			if (this.front.getNext().getNext() == null)
				this.back = this.front.getNext();
		}	
	}

	public void pushBack(X x) throws Exception {
		if (x == null)
			throw new Exception("Informacao ausente");

		X info;
		if (x instanceof Cloneable)
			info = (X) GenericClone.clone(x);
		else
			info = x;

		if (this.front == null)
		{
			this.front = new Node(info);
			this.front.prev = null;
			this.back = this.front;
		}
		else 
		{
			this.back.setNext( new Node(info));
			this.back = this.back.getNext();
		}

	}
	
	public void remove(int index) throws Exception
	{
		if (index == 0)
		{
			this.popFront();
			return;
		}
		
		Node current = this.front;
		for(int i = 0; i < index && current != null;i++)
			current = current.getNext();
		if (current==null)
			throw new Exception("index errado");
		
		if (current.getNext() != null)
		{
			current = current.prev;
			current.setNext(current.getNext().getNext());
		}
		else
			this.popBack();
	}

	public void popFront() throws Exception {
		if(this.front == null)
			throw new Exception("Nao ha nada para jogar fora");

		this.front = this.front.getNext();
		if (this.front != null)
		this.front.prev = null;
	}

	public void popBack() throws Exception {
		if(this.front == null)
			throw new Exception("Nao ha nada para jogar fora");

		if (this.front.getNext() != null)
		{
			Node aux = this.back.prev;
			this.back = this.back.prev;
			this.back.next = null;
			this.back.prev = aux;
		}
		else
			this.front = null;
	}
	
	public int find(X x) throws Exception
	{
		Node current = this.front;
		for(int i = 0; current != null;i++)
		{
			if (current.getInfo() == x)
				return i;
			current = current.getNext();
		}
		
		throw new Exception("invalid index");
	}
	
	public String toString() {
		String ret = "{";
		Node current = this.front;

		while (current != null) {
			ret += current.getInfo();

			if (current.getNext() != null)
				ret += ",";

			current = current.getNext();
		}

		ret += "}";

		return ret;
	}

	public int hashCode() {
		int ret = 44;

		ret = ret*7 + front.hashCode();

		return ret;
	}

	public boolean equals (Object obj) {
		if (obj == this)
			return true;

		if (obj == null)
			return false;

		if (!this.getClass().equals(obj.getClass()))
			return false;

		UnorderedList<X> list = (UnorderedList<X>)obj;

		if (!((this.front).equals(list.front)))
			return false;

		return true;
	}

	public Object clone () {

		UnorderedList<X> ret = null;
		try {
			ret = new UnorderedList<X>(this);
		}catch(Exception erro) {}

		return ret;
	}

	public UnorderedList(UnorderedList<X> model) throws Exception {
		if (model == null)
			throw new Exception("Null modelo");

		if (model.front != null) {
			this.front = new Node(model.front.getInfo());

			Node current = model.front.getNext();
			Node aux   = this.front;

			while (current != null) {
				aux.setNext(new Node(current.getInfo()));
				current = current.getNext();
				aux   = aux.getNext();
			}
		}
	}
}