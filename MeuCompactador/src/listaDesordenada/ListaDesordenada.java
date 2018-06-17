package listaDesordenada;
import java.lang.reflect.*;


public class ListaDesordenada <X> implements Cloneable{

	protected class No {
		protected X  info;
		protected No prox;
		protected No anterior;

		public X getInfo() {
			return this.info;
		}

		public No getProx() {
			return this.prox;
		}

		public void setInfo(X x) {
			this.info = x;
		}

		public void setProx(No n) {
			this.prox = n;
			this.prox.anterior = this;
		}

		public No (X x, No n) {
			this.info = x;
			this.prox = n;
		}

		public No (X x) {
			this(x,null);
		}

		public int hashCode() {
			int ret = 44;

			ret = ret*7 + info.hashCode();

			if (prox != null)
				ret = ret*7 + prox.hashCode();

			return ret;
		}

		public boolean equals (Object obj) {
			if (obj == this)
				return true;

			if (obj == null)
				return false;

			if (!this.getClass().equals(obj.getClass()))
				return false;

			No no = (No)obj;

			if (!(this.info).equals(no.info))
				return false;

			if (this.prox == null)
				if (no.prox == null)
					return true;
				else
					return false;

			if (!(this.prox).equals(no.prox))
				return false;

			return true;
		}
	}

	protected No prim   = null;
	protected No ultimo = null;
	
	public ListaDesordenada() 
	{}
	
	public X getPrimeiro()
	{
		return this.prim.getInfo();
	}
	
	public X getElemento(int index) throws Exception
	{
		No atual = this.prim;
		for(int i = 0; i < index && atual != null;i++)
			atual = atual.getProx();
		if (atual==null)
			throw new Exception("index errado");
		
		return atual.getInfo();	
	}

	public void insiraNoInicio (X x) throws Exception 
	{
		if (x == null)
			throw new Exception("Informacao ausente");

		X info;
		if (x instanceof Cloneable)
			info = meuCloneDeX(x);
		else
			info = x;

		this.prim = new No (info, this.prim);
		
		if(this.prim.getProx()==null)
			this.ultimo = this.prim;
		else
		{
			this.prim.getProx().anterior = this.prim;
			if (this.prim.getProx().getProx() == null)
				this.ultimo = this.prim.getProx();
		}	
	}

	public void insiraNoFim (X x) throws Exception {
		if (x == null)
			throw new Exception("Informacao ausente");

		X info;
		if (x instanceof Cloneable)
			info = meuCloneDeX(x);
		else
			info = x;

		if (this.prim == null)
		{
			this.prim = new No(info);
			this.prim.anterior = null;
			this.ultimo = this.prim;
		}
		else 
		{
			this.ultimo.setProx( new No (info));
			this.ultimo = this.ultimo.getProx();
		}

	}
	
	public void jogueFora(int index) throws Exception
	{
		if (index == 0)
		{
			this.jogueForaPrimeiro();
			return;
		}
		
		No atual = this.prim;
		for(int i = 0; i < index && atual != null;i++)
			atual = atual.getProx();
		if (atual==null)
			throw new Exception("index errado");
		
		if (atual.getProx() != null)
		{
			atual = atual.anterior;
			atual.setProx(atual.getProx().getProx());
		}
		else
			this.jogueForaUltimo();		
	}

	public void jogueForaPrimeiro() throws Exception {
		if(this.prim == null)
			throw new Exception("Nao ha nada para jogar fora");

		this.prim          = this.prim.getProx();
		if (this.prim != null)
		this.prim.anterior = null;
	}

	public void jogueForaUltimo() throws Exception {
		if(this.prim == null)
			throw new Exception("Nao ha nada para jogar fora");

		if (this.prim.getProx() != null) 
		{
			No aux = this.ultimo.anterior;
			
			this.ultimo = this.ultimo.anterior;
			
			this.ultimo.prox = null;
			
			this.ultimo.anterior = aux;
			
		}
		else
			this.prim = null;
	}
	
	public int Find(X x) throws Exception
	{
		No atual = this.prim;
		for(int i = 0; atual != null;i++)
		{
			if (atual.getInfo() == x)
				return i;
				
			atual = atual.getProx();
		}
		
		throw new Exception("index errado");
	}
	
	protected X meuCloneDeX (X x) {
		X ret = null;

		try {
			Class<?> classe = x.getClass();
			Class<?>[] tipoDoParametroFormal = null;
			Method metodo = classe.getMethod ("clone", tipoDoParametroFormal);
			Object[] parametroReal = null;

			ret = ((X)metodo.invoke (x, parametroReal));
		}
		catch (NoSuchMethodException erro){}
		catch (InvocationTargetException erro){}
		catch (IllegalAccessException erro){}

		return ret;
    }
	
	public int Length() 
	{
		No atual = this.prim;

		int i;
		for (i = 0; atual != null; i++) 
		{
			atual = atual.getProx();
		}
		
		return i;
	}
	
	public String toString() {
		String ret = "{";
		No atual = this.prim;

		while (atual != null) {
			ret += atual.getInfo();

			if (atual.getProx() != null)
				ret += ",";

			atual = atual.getProx();
		}

		ret += "}";

		return ret;
	}

	public int hashCode() {
		int ret = 44;

		ret = ret*7 + prim.hashCode();

		return ret;
	}

	public boolean equals (Object obj) {
		if (obj == this)
			return true;

		if (obj == null)
			return false;

		if (!this.getClass().equals(obj.getClass()))
			return false;

		ListaDesordenada<X> lis = (ListaDesordenada<X>)obj;

		if (!((this.prim).equals(lis.prim)))
			return false;

		return true;
	}

	public Object clone () {

		ListaDesordenada<X> ret = null;
		try {
			ret = new ListaDesordenada<X>(this);
		}catch(Exception erro) {}

		return ret;
	}

	public ListaDesordenada(ListaDesordenada<X> modelo) throws Exception {
		if (modelo == null)
			throw new Exception("Modelo ausente");

		if (modelo.prim != null) {
			this.prim = new No(modelo.prim.getInfo());

			No atual = modelo.prim.getProx();
			No aux   = this.prim;

			while (atual != null) {
				aux.setProx(new No(atual.getInfo()));

				atual = atual.getProx();
				aux   = aux.getProx();
			}
		}
	}
}