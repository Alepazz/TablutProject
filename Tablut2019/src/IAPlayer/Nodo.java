package IAPlayer;

import it.unibo.ai.didattica.competition.tablut.domain.*;


public class Nodo {
	
	private StateTablut stato;
	private Action azione;
	private Nodo padre;
	private float value;
	
	public Nodo(StateTablut stato) {
		super();
		this.stato = stato;
		this.azione = null;
		this.padre = null;
		this.value = Float.NaN;
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
	public State.Pawn[][] getBoard()
	{
		return this.stato.getBoard();
	}
	public State.Turn getTurn()
	{
		return this.stato.getTurn();
	}
	
	
	public Nodo getPadre() {
		return padre;
	}
	public void setPadre(Nodo padre) {
		this.padre = padre;
	}
	public float getValue() {
		return value;
	}
	public void setValue(float value) {
		this.value = value;
	}

	
	
	
}
