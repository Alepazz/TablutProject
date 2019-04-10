package IAPlayer;

import it.unibo.ai.didattica.competition.tablut.domain.*;
import it.unibo.ai.didattica.competition.tablut.exceptions.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



public class Nodo {
	
	private StateTablut stato;
	private Action azione;
	private Nodo padre;
	private List<Nodo> figli;
	
	public Nodo(StateTablut stato) {
		super();
		this.setFigli(new ArrayList<Nodo>());
		this.stato = stato;
		this.azione = null;
		this.padre = null;
	}
	public StateTablut getStato() {
		return stato;
	}
	public void setStato(StateTablut stato) {
		this.stato = stato;
	}
	public Action getAzione() {
		return azione;
	}
	public void setAzione(Action azione) {
		this.azione = azione;
	}
	
	public List<Nodo> generaFigli(Simulator s, boolean isLastLevel) throws IOException, BoardException, ActionException, StopException, PawnException, DiagonalException, ClimbingException, ThroneException, OccupitedException, ClimbingCitadelException, CitadelException
	{
		List<Nodo> figli = s.mossePossibiliComplete(stato, stato.getTurn());
		for(Nodo n: figli)
		{
			this.figli.add(n);
			n.setPadre(this);
		}
		return figli;
	}
	
	public Nodo getPadre() {
		return padre;
	}
	public void setPadre(Nodo padre) {
		this.padre = padre;
	}
	public List<Nodo> getFigli() {
		return figli;
	}
	public void setFigli(List<Nodo> figli) {
		this.figli = figli;
	}
	
	
	
}
