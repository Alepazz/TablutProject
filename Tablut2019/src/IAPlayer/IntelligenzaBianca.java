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
			return 10000;
		}
		if(s.getTurn().equalsTurn("BW"))
		{
			return -10000;
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
		
		
		int viedifuga=4;
		for(int i=rigaRe+1; i<9; i++)
		{
			if(!s.getPawn(rigaRe+i, colonnaRe).equalsPawn("O") || this.citadels.contains(s.getBox(rigaRe+i, colonnaRe)))
			{
				viedifuga--;
				i=20;
			}
		}
		for(int i=rigaRe-1; i>=0; i--)
		{
			if(!s.getPawn(rigaRe-i, colonnaRe).equalsPawn("O") || this.citadels.contains(s.getBox(rigaRe-i, colonnaRe)))
			{
				viedifuga--;
				i=20;
			}
		}
		for(int i=colonnaRe+1; i<9; i++)
		{
			if(!s.getPawn(rigaRe, colonnaRe+i).equalsPawn("O") || this.citadels.contains(s.getBox(rigaRe, colonnaRe+i)))
			{
				viedifuga--;
				i=-1;
			}
		}
		for(int i=colonnaRe-1; i>=0; i--)
		{
			if(!s.getPawn(rigaRe, colonnaRe-i).equalsPawn("O") || this.citadels.contains(s.getBox(rigaRe, colonnaRe-i)))
			{
				viedifuga--;
				i=-1;
			}
		}
		
		
		
		
		
		if(viedifuga>=1 && s.getTurn().equalsTurn("W"))
		{
			return 10000;
		}
		if(viedifuga>1 && s.getTurn().equalsTurn("B"))
		{
			//controllo se nella mossa del nero mi mangia il re
			if(getKingNearBlackRight(rigaRe, colonnaRe, s))
			{
				if(checkBlackCanArrive(rigaRe, colonnaRe-1, s))
				{
					return -10000;
				}
				else
				{
					return 10000;
				}
			}
			if(getKingNearBlackLeft(rigaRe, colonnaRe, s))
			{
				if(checkBlackCanArrive(rigaRe, colonnaRe+1, s))
				{
					return -10000;
				}
				else
				{
					return 10000;
				}
			}
			if(getKingNearBlackUp(rigaRe, colonnaRe, s))
			{
				if(checkBlackCanArrive(rigaRe+1, colonnaRe, s))
				{
					return -10000;
				}
				else
				{
					return 10000;
				}
			}
			if(getKingNearBlackDown(rigaRe, colonnaRe, s))
			{
				if(checkBlackCanArrive(rigaRe-1, colonnaRe, s))
				{
					return -10000;
				}
				else
				{
					return 10000;
				}
			}
			
			
			
		}
		if(viedifuga==1 && s.getTurn().equalsTurn("B"))
		{
			
		}
		
		return value;
		
	}
	
	private boolean checkBlackCanArrive(int riga, int colonna, StateTablut s)
	{
		for(int i=riga+1; i<9;i++)
		{
			if(s.getPawn(riga+i, colonna).equalsPawn("B"))
			{
				return false;
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
				return false;
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
				return false;
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
				return false;
			}
			if(s.getPawn(riga, colonna-i).equalsPawn("W") || s.getPawn(riga, colonna-i).equalsPawn("T") || this.citadels.contains(s.getBox(riga, colonna-i)))
			{
				i=-1;
			}			
		}
		
		
		
		return true;
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
