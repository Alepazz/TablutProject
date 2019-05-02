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
	private final int MAX_VALUE = 10000;
	private final int MIN_VALUE = - MAX_VALUE;
	private final int VALUE_BLACK_PAWN = 100;
	private final int VALUE_WHITE_PAWN = 2 * VALUE_BLACK_PAWN;
	private Simulator simulatore;
	private CommonHeuristicFunction common;
	
	public IntelligenzaBianca() {
		this.simulatore = new Simulator();
		this.nodiEsistenti = new ArrayList<Nodo>();
		this.common= new CommonHeuristicFunction();
	}
	
	/**
	 * Funzione che valuta uno stato, e ritorna il corrispettivo valore intero
	 * 
	 * @param s StateTablut utile per valutare l'euristica
	 * @return int valore attribuito alla valutazione di quel particolare stato s
	 */
	private int getHeuristicValue(StateTablut s) {
		
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
		
		
		int value =0;
		
		//numero pedine
		int nBianchi=0;
		int nNeri=0;
		int rigaRe=-1;
		int colonnaRe=-1;
		/* Per ogni pezzo trovato viene aggiunto il suo valore al valore totale dello stato
		 * Valori definiti per adesso: <--- SI POSSONO CAMBIARE
		 * Pedina Nera: 100
		 * Pedina Mangiabile: 50
		 * Pedina Bianca: 200
		 * Pedina Bianca Mangiabile: 100
		 * 
		 * Il peso della pedina bianca e' il doppio di quella nera 
		 */
		
		for(int i=0; i<9; i++)
		{
			for(int j=0; j<9; j++)
			{
				if(s.getBoard()[i][j].equalsPawn("B"))
				{
					nNeri++;
					value=- VALUE_BLACK_PAWN;
					if(common.checkBlackCanBeCaptured(i, j, s))
					{
						value=+ VALUE_BLACK_PAWN/2;
					}
				}
				if(s.getBoard()[i][j].equalsPawn("W"))
				{
					nBianchi++;
					value=+ VALUE_WHITE_PAWN;
					if(common.checkWhiteCanBeCaptured(i, j, s))
					{
						value=- VALUE_WHITE_PAWN/2;
					}
				}
				if(s.getBoard()[i][j].equalsPawn("K"))
				{
					rigaRe=i;
					colonnaRe=j;
				}
			}
		}
		
		//Controllo se il re viene mangiato in qualsiasi posizione sia
		if(this.common.kingCanBeCaptured(rigaRe, colonnaRe, s))
		{
			return this.MIN_VALUE+1;
		}
		
		//controllo vie di fuga re
		int viedifuga=this.common.checkVieDiFugaRe(rigaRe, colonnaRe, s);
				
		//controllo se nella mossa del nero mi mangia il re
		if(viedifuga>1)
		{
			return this.MAX_VALUE-1;			
		}
		if(viedifuga==1 && s.getTurn().equalsTurn("W"))
		{
			return this.MAX_VALUE-1;
		}
		if(viedifuga==1 && s.getTurn().equalsTurn("B"))
		{
			if(common.blackCannotBlockEscape(s, rigaRe, colonnaRe))
			{
				return this.MAX_VALUE-1;
			}
		}		
		
		/*
		 * Funzione che controlla se, eseguita una mossa del re in orizzontale, esso ha liberato un'intera colonna (2 oppure 6), in cui vincere (al 100%) il turno successivo
		 */
		if (this.common.checkFreeColComingFromLeft(rigaRe, colonnaRe, s) || this.common.checkFreeColComingFromRight(rigaRe, colonnaRe, s)) {
			return this.MAX_VALUE;
		}
		
		/*
		 * Funzione che controlla se, eseguita una mossa del re in verticale, esso ha liberato un'intera riga (2 oppure 6), in cui vincere (al 100%) il turno successivo
		 */
		if (this.common.checkFreeRowComingFromTop(rigaRe, colonnaRe, s) || this.common.checkFreeRowComingFromBottom(rigaRe, colonnaRe, s)){
			return this.MAX_VALUE;
		}
			
				
		return value;	
	}
	
	
	
	
	/*
	 * Funzione di euristica, di prova <-- da modificare BRAVO ALE, HAI CAPITO COSA INTENDO
	 */
	private int getHeuristicValueOfState(StateTablut s) {
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
		int value =0;
		
		//numero pedine
		int nBianchi=0;
		int nNeri=0;
		int rigaRe=-1;
		int colonnaRe=-1;
		for(int i=0; i<9; i++)
		{
			for(int j=0; j<9; j++)
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
		//Controllo se il re viene mangiato in qualsiasi posizione sia
		if(this.common.kingCanBeCaptured(rigaRe, colonnaRe, s))
		{
			return this.MIN_VALUE+1;
		}
		
		return nBianchi - nNeri + 2*this.common.getNumberStarFree(s) + 2 * common.checkVieDiFugaRe(rigaRe, colonnaRe, s);
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
		int i = 0;
		Action a = null;
		long t1 = System.currentTimeMillis();
		float betterValue=-100000;
		try {
			Nodo node = new Nodo(s);
			Livello liv0 = new Livello();
			Livello liv1 = new Livello();
			Livello liv2 = new Livello();
			Livello liv3 = new Livello();
			
			liv0.add(this.simulatore.mossePossibiliComplete(node));
			System.out.println("Livello 0 espanso");
			//System.out.println("Tempo trascorso: "+(t2-t1)+" millisecondi");
			
			for(Nodo n : liv0.getNodi())
			{
				liv1.add(this.simulatore.mossePossibiliComplete(n));
			}
			System.out.println("Livello 1 espanso");
			
			for(Nodo n : liv1.getNodi())
			{
				liv2.add(this.simulatore.mossePossibiliComplete(n));
			}
			System.out.println("Livello 2 espanso");
			
			/*for(Nodo n : liv2.getNodi())
			{
				liv3.add(this.simulatore.mossePossibiliComplete(n));
				System.out.println(liv3.getNodi().size());
			}*/
			//System.out.println("Livello 3 espanso");
			
			i = liv0.getNodi().size() + liv1.getNodi().size() + liv2.getNodi().size() + liv3.getNodi().size();
			System.out.println("Nodi espansi: "+ i);
			//ciclo tutto il livello 4 (turno nero, becco il min)
			/*for(Nodo n : liv3.getNodi())
			{
				float heu =this.getHeuristicValueOfState(n.getStato());
				//System.out.println(n.getStato().toString()+ " " + heu);
				if(heu < n.getPadre().getValue() || Float.isNaN(n.getPadre().getValue()))
				{
					n.getPadre().setValue(heu);
				}
			}*/
			
			//ciclo tutti il livello 3 (turno bianco, becco il max)
			for(Nodo n : liv2.getNodi())
			{
				float heu =this.getHeuristicValueOfState(n.getStato());
				System.out.println(n.getStato().toString()+ " " + heu);
				if(heu > n.getPadre().getValue() || Float.isNaN(n.getPadre().getValue()))
				{
					n.getPadre().setValue(heu);
				}
				/*float b = n.getValue();
				if(betterValue<=b)
				{
					betterValue = b;
					n.getPadre().setValue(betterValue);
				}*/
			}
			
			//ciclo tutto il livello 2 (turno nero, quindi becco il min)
			betterValue=10000;
			for(Nodo n : liv1.getNodi())
			{
				float b = n.getValue();
				if(betterValue>=b)
				{
					betterValue = b;
					n.getPadre().setValue(betterValue);
				}
			}
			
			betterValue=-100000;
			//ciclo tutto il livello 1 (turno bianco, quindi becco il max)
			for(Nodo n : liv0.getNodi())
			{
				float b = n.getValue();
				if(betterValue<=b)
				{
					betterValue = b;
					a = n.getAzione();
				}
			}
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long t2 = System.currentTimeMillis();
		System.out.println("Tempo trascorso: "+(t2-t1)+" millisecondi");
	    
		return a;
	}

}
