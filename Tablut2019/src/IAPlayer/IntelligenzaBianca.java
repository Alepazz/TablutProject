package IAPlayer;

import it.unibo.ai.didattica.competition.tablut.domain.Action;
import it.unibo.ai.didattica.competition.tablut.domain.State;
import it.unibo.ai.didattica.competition.tablut.domain.StateTablut;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class IntelligenzaBianca implements IA {

	private List<String> citadels;
	private List<Nodo> nodiEsistenti;
	private final static int MAX_VALUE = 10000;
	private final static int MIN_VALUE = - MAX_VALUE;
	
	public IntelligenzaBianca() {
		this.nodiEsistenti = new ArrayList<Nodo>();
		this.citadels = new ArrayList<String>();
		this.citadels.add("a4");
		this.citadels.add("a5");
		this.citadels.add("a6");
		this.citadels.add("b5");
		this.citadels.add("d1");
		this.citadels.add("e1");
		this.citadels.add("f1");
		this.citadels.add("e2");
		this.citadels.add("i4");
		this.citadels.add("i5");
		this.citadels.add("i6");
		this.citadels.add("h5");
		this.citadels.add("d9");
		this.citadels.add("e9");
		this.citadels.add("f9");
		this.citadels.add("e8");
	}
	
	
	
	
	private float getHeuristicValue(StateTablut s) {
		
		if(s.getTurn().equalsTurn("WW"))
		{
			return this.MAX_VALUE;
		}
		if(s.getTurn().equalsTurn("BW"))
		{
			return this.MIN_VALUE;
		}
		if(s.getTurn().equalsTurn("D"))
		{
			return 0;
		}
		
		
		float value =0;
		
		//numero pedine
		int nBianchi=0;
		int nNeri=0;
		int rigaRe=-1;
		int colonnaRe=-1;
		for(int i=0; i<8; i++)
		{
			for(int j=0; j<8; j++)
			{
				if(s.getBoard()[i][j].equalsPawn("B"))
				{
					nNeri++;
				}
				if(s.getBoard()[i][j].equalsPawn("W"))
				{
					nBianchi++;
				}
				if(s.getBoard()[i][j].equalsPawn("K"))
				{
					rigaRe=i;
					colonnaRe=j;
				}
			}
		}
		
		//controllo se re viene mangiato
		if(this.kingCanBeCaptured(s, rigaRe, colonnaRe))
		{
			return this.MIN_VALUE;
		}
		
		//controllo vie di fuga re
		int viedifuga=this.checkVieDiFugaRe(s, rigaRe, colonnaRe);
				
		//controllo se nella mossa del nero mi mangia il re
		if(viedifuga>1)
		{
			return this.MAX_VALUE;			
		}
		if(viedifuga==1 && s.getTurn().equalsTurn("W"))
		{
			return this.MAX_VALUE;
		}
		if(viedifuga==1 && s.getTurn().equalsTurn("B"))
		{
			if(balckCannotBlockEscape(s, rigaRe, colonnaRe))
			{
				return this.MAX_VALUE;
			}
		}
		
		
		
		return value;
		
	}
	
	private boolean balckCannotBlockEscape(StateTablut s, int rigaRe, int colonnaRe) {
		
		int i;
		
		//via di fuga sotto
		for(i=rigaRe+1; i<9; i++)
		{
			if(!s.getPawn(i, colonnaRe).equalsPawn("O") || this.citadels.contains(s.getBox(i, colonnaRe)))
			{
				i=20;
			}
		}
		if(i!=20)
		{
			for(i=rigaRe+1; i<9; i++)
			{
				if(this.checkBlackCanArrive(i, colonnaRe, s))
				{
					return false;
				}
			}
		}
		
		//via di fuga sopra
		for(i=rigaRe-1; i>=0; i--)
		{
			if(!s.getPawn(i, colonnaRe).equalsPawn("O") || this.citadels.contains(s.getBox(i, colonnaRe)))
			{
				i=20;
			}
		}
		if(i!=20)
		{
			for(i=rigaRe-1; i>=0; i--)
			{
				if(this.checkBlackCanArrive(i, colonnaRe, s))
				{
					return false;
				}
			}
		}
		
		//via di fuga a destra
		for(i=colonnaRe+1; i<9; i++)
		{
			if(!s.getPawn(rigaRe, i).equalsPawn("O") || this.citadels.contains(s.getBox(rigaRe, i)))
			{
				i=-1;
			}
		}
		if(i!=20)
		{
			for(i=colonnaRe+1; i<9; i++)
			{
				if(this.checkBlackCanArrive(rigaRe, i, s))
				{
					return false;
				}
			}
		}
		
		//via di fuga a sinistra
		for(i=colonnaRe-1; i>=0; i--)
		{
			if(!s.getPawn(rigaRe, i).equalsPawn("O") || this.citadels.contains(s.getBox(rigaRe, i)))
			{
				i=-1;
			}
		}
		if(i!=20)
		{
			for(i=colonnaRe-1; i>=0; i--)
			{
				if(this.checkBlackCanArrive(rigaRe, i, s))
				{
					return false;
				}
			}
		}
		
		return true;
	}

	private boolean kingCanBeCaptured(StateTablut s, int rigaRe, int colonnaRe)
	{
		if(s.getTurn().equalsTurn("B"))
		{
			if(getKingNearBlackRight(rigaRe, colonnaRe, s))
			{
				if(checkBlackCanArrive(rigaRe, colonnaRe-1, s))
				{
					return true;
				}
			}
			if(getKingNearBlackLeft(rigaRe, colonnaRe, s))
			{
				if(checkBlackCanArrive(rigaRe, colonnaRe+1, s))
				{
					return true;
				}
			}
			if(getKingNearBlackUp(rigaRe, colonnaRe, s))
			{
				if(checkBlackCanArrive(rigaRe+1, colonnaRe, s))
				{
					return true;
				}
			}
			if(getKingNearBlackDown(rigaRe, colonnaRe, s))
			{
				if(checkBlackCanArrive(rigaRe-1, colonnaRe, s))
				{
					return true;
				}
			}
		}
		return false;
	}
	
	
	private int checkVieDiFugaRe(StateTablut s, int rigaRe, int colonnaRe)
	{
		int viedifuga=4;
		for(int i=rigaRe+1; i<9; i++)
		{
			if(!s.getPawn(i, colonnaRe).equalsPawn("O") || this.citadels.contains(s.getBox(i, colonnaRe)))
			{
				viedifuga--;
				i=20;
			}
		}
		for(int i=rigaRe-1; i>=0; i--)
		{
			if(!s.getPawn(i, colonnaRe).equalsPawn("O") || this.citadels.contains(s.getBox(i, colonnaRe)))
			{
				viedifuga--;
				i=20;
			}
		}
		for(int i=colonnaRe+1; i<9; i++)
		{
			if(!s.getPawn(rigaRe, i).equalsPawn("O") || this.citadels.contains(s.getBox(rigaRe, i)))
			{
				viedifuga--;
				i=-1;
			}
		}
		for(int i=colonnaRe-1; i>=0; i--)
		{
			if(!s.getPawn(rigaRe, i).equalsPawn("O") || this.citadels.contains(s.getBox(rigaRe, i)))
			{
				viedifuga--;
				i=-1;
			}
		}
		return viedifuga;
	}
	
	private boolean checkBlackCanArrive(int riga, int colonna, StateTablut s)
	{
		for(int i=riga+1; i<9;i++)
		{
			if(s.getPawn(riga+i, colonna).equalsPawn("B"))
			{
				return true;
			}
			if(s.getPawn(riga+i, colonna).equalsPawn("W") || s.getPawn(riga+i, colonna).equalsPawn("T") || this.citadels.contains(s.getBox(riga+i, colonna)))
			{
				i=20;
			}			
		}
		for(int i=riga-1; i>=0;i--)
		{
			if(s.getPawn(riga-i, colonna).equalsPawn("B"))
			{
				return true;
			}
			if(s.getPawn(riga-i, colonna).equalsPawn("W") || s.getPawn(riga-i, colonna).equalsPawn("T") || this.citadels.contains(s.getBox(riga-i, colonna)))
			{
				i=-1;
			}			
		}
		for(int i=colonna+1; i<9;i++)
		{
			if(s.getPawn(riga, colonna+i).equalsPawn("B"))
			{
				return true;
			}
			if(s.getPawn(riga, colonna+i).equalsPawn("W") || s.getPawn(riga, colonna+i).equalsPawn("T") || this.citadels.contains(s.getBox(riga, colonna+i)))
			{
				i=20;
			}			
		}
		for(int i=colonna-1; i>=0;i--)
		{
			if(s.getPawn(riga, colonna-i).equalsPawn("B"))
			{
				return true;
			}
			if(s.getPawn(riga, colonna-i).equalsPawn("W") || s.getPawn(riga, colonna-i).equalsPawn("T") || this.citadels.contains(s.getBox(riga, colonna-i)))
			{
				i=-1;
			}			
		}
		
		return false;
	}

	private boolean getKingNearBlackRight(int rigaRe, int colonnaRe, StateTablut s)
	{
		if(s.getPawn(rigaRe, colonnaRe+1).equalsPawn("B") || this.citadels.contains(s.getBox(rigaRe,  colonnaRe+1)))
		{
			return true;
		}
		return false;
	}
	
	
	private boolean getKingNearBlackLeft(int rigaRe, int colonnaRe, StateTablut s)
	{
		if(s.getPawn(rigaRe, colonnaRe-1).equalsPawn("B") || this.citadels.contains(s.getBox(rigaRe,  colonnaRe-1)))
		{
			return true;
		}
		return false;
	}
	
	private boolean getKingNearBlackUp(int rigaRe, int colonnaRe, StateTablut s)
	{
		if(s.getPawn(rigaRe-1, colonnaRe).equalsPawn("B") || this.citadels.contains(s.getBox(rigaRe-1,  colonnaRe)))
		{
			return true;
		}
		return false;
	}
	
	private boolean getKingNearBlackDown(int rigaRe, int colonnaRe, StateTablut s)
	{
		if(s.getPawn(rigaRe+1, colonnaRe).equalsPawn("B") || this.citadels.contains(s.getBox(rigaRe+1,  colonnaRe)))
		{
			return true;
		}
		return false;
	}

	private List<Action> getMossePossibili(StateTablut s) {
		// TODO Auto-generated method stub
		return null;
	}

	private StateTablut getNewState(StateTablut s, Action a) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Action getBetterMove(StateTablut s) {
		
		//QUESTA � SOLO UNA PROVA PER VEDERE SE EFFETTIVAMENTE IL NOSTRO GIOCATORE FUNZIONA
		//RISULTATO POSITIVO
		Action a = null;
		try {
		/*	if(this.isStart(s)) {
				System.out.println("FATTOOOOOOOOOOO");
			} else {
				System.out.println("La scacchiera non è riempita");

			}
			
			*/
			a = new Action("e4", "f4", State.Turn.WHITE);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return a;
	}

}

/*
 * ALLEGO DOMANDE
 * 
 * Che metodo va chiamato per sapere quando siamo nello stato iniziale (nessuna mossa ancora effettuata)?
 * Come ottengo qual'è l'ultima mossa effettuata dal giocatore avversario?
 */
