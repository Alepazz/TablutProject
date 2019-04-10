package IAPlayer;

import java.util.ArrayList;
import java.util.List;

public class Livello {
	private List<Nodo> nodi;

	public Livello() {
		super();
		this.setNodi(new ArrayList<Nodo>());
	}

	public List<Nodo> getNodi() {
		return nodi;
	}

	public void setNodi(List<Nodo> nodi) {
		this.nodi = nodi;
	}
	
	public void add(Nodo n)
	{
		this.nodi.add(n);
	}
	
	public void add(List<Nodo> n)
	{
		for(Nodo x: n)
		{
			this.nodi.add(x);
		}
	}

}
