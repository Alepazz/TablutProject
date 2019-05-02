package IAPlayer;

import it.unibo.ai.didattica.competition.tablut.domain.Action;
import it.unibo.ai.didattica.competition.tablut.domain.StateTablut;

import java.util.ArrayList;
import java.util.List;

public class IntelligenzaNera implements IA {
	
	private List<Nodo> nodiEsistenti;
	private final int MAX_VALUE = 10000;
	private final int MIN_VALUE = - MAX_VALUE;
	private final int VALUE_BLACK_PAWN = 100;
	private final int VALUE_WHITE_PAWN = 2 * VALUE_BLACK_PAWN;
	private Simulator simulatore;
	private CommonHeuristicFunction common;
	
	public IntelligenzaNera() {
		this.simulatore = new Simulator();
		this.nodiEsistenti = new ArrayList<Nodo>();
		this.common= new CommonHeuristicFunction();
	}


	private int getHeuristicValue(StateTablut s) {
		
		if(s.getTurn().equalsTurn("WW"))
		{
			return this.MIN_VALUE;
		}
		if(s.getTurn().equalsTurn("BW"))
		{
			return this.MAX_VALUE;
		}
		if(s.getTurn().equalsTurn("D"))
		{
			return 0;
		}
		
		
		int value =0;
		
		///numero pedine
		int nBianchi=0;
		int nNeri=0;
		int rigaRe=-1;
		int colonnaRe=-1;
		//faccio una lista di tutti i neri/bianchi e delle loro posizioni
		String[] neri = new String[16];
		String[] bianchi = new String[8];
	
		int indexNeri=0;
		int indexBianchi=0;
		
		for(int i=0; i<9; i++)
		{
			for(int j=0; j<9; j++)
			{
				if(s.getBoard()[i][j].equalsPawn("B"))
				{
					nNeri++; //volendo il numero delle pedine si può avere cercando lunghezza della lista
					//aggiungo la posizione ij di ogni nero
					neri[indexNeri]= ""+i+j;
					indexNeri++;
				}
				if(s.getBoard()[i][j].equalsPawn("W"))
				{
					nBianchi++;
					bianchi[indexBianchi]= ""+i+j;
					indexBianchi++;
				}
				if(s.getBoard()[i][j].equalsPawn("K"))
				{
					rigaRe=i;
					colonnaRe=j;
				}
			}
		}
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
			return this.MAX_VALUE+1;
		}
		
		//controllo vie di fuga re
		int viedifuga=this.common.checkVieDiFugaRe(rigaRe, colonnaRe, s);
				
		//controllo se nella mossa del nero mi mangia il re
		if(viedifuga>1)
		{
			return this.MIN_VALUE+viedifuga;			
		}
		if(viedifuga==1 && s.getTurn().equalsTurn("W"))
		{
			return this.MIN_VALUE+1;
		}
		if(viedifuga==1 && s.getTurn().equalsTurn("B"))
		{
			if(common.blackCannotBlockEscape(s, rigaRe, colonnaRe))
			{
				return this.MIN_VALUE+1;
			}
		}		
		/*
		 * Funzione che controlla se, eseguita una mossa del re in orizzontale, esso ha liberato un'intera colonna (2 oppure 6), in cui vincere (al 100%) il turno successivo
		 */
		if (this.common.checkFreeColComingFromLeft(rigaRe, colonnaRe, s) || this.common.checkFreeColComingFromRight(rigaRe, colonnaRe, s)) {
			return this.MIN_VALUE;
		}
		
		/*
		 * Funzione che controlla se, eseguita una mossa del re in verticale, esso ha liberato un'intera riga (2 oppure 6), in cui vincere (al 100%) il turno successivo
		 */
		if (this.common.checkFreeRowComingFromTop(rigaRe, colonnaRe, s) || this.common.checkFreeRowComingFromBottom(rigaRe, colonnaRe, s)){
			return this.MIN_VALUE;
		}
			
		else {
			for(int i=0; i<indexNeri; i++ ) {
				
				int posizione= Integer.parseInt(neri[i]);
				//le unità sono le colonne mentre le decine sono le righe
				int riga = posizione/10;
				int colonna= posizione % 10;
				
				//controllo che ci siano delle pedine in diagonale
				if(common.checkNeighbourBottomLeft(riga, colonna, s).equals("B"))
					value =+ VALUE_BLACK_PAWN*2;
				if(	common.checkNeighbourBottomRight(riga, colonna, s).equals("B"))
					value =+ VALUE_BLACK_PAWN*2;
				if(common.checkNeighbourTopLeft(riga, colonna, s).equals("B"))
					value =+ VALUE_BLACK_PAWN*2;
				if(	common.checkNeighbourTopRight(riga, colonna, s).equals("B"))
					value =+ VALUE_BLACK_PAWN*2;
				
				//controllo pedine vicine sugli assi (è preferibile che siano in diagonale)
				if( common.checkNeighbourBottom(riga, colonna, s).equals("B"))
					value =+ this.VALUE_BLACK_PAWN/2;
				if( common.checkNeighbourTop(riga, colonna, s).equals("B"))
					value =+ this.VALUE_BLACK_PAWN/2;
				if( common.checkNeighbourLeft(riga, colonna, s).equals("B"))
					value =+ this.VALUE_BLACK_PAWN/2;
				if( common.checkNeighbourRight(riga, colonna, s).equals("B"))
					value =+ this.VALUE_BLACK_PAWN/2;
				
				//controllo che ci siano pedine nere isolate (che non va bene)
				if(common.blackIsIsolated(riga, colonna, s))
					value =- this.VALUE_BLACK_PAWN*5;
				
				
				//controllo che ci siano bianchi mangiabili
				if( common.checkWhiteCanBeCaptured(riga, colonna, s))
					value =+ this.VALUE_BLACK_PAWN*8;
				
				//controllo se possono arrivare pedine bianche da dx-sx e up-down
				if(common.checkWhiteCanArriveFromBottom(riga, colonna, s) && common.checkWhiteCanArriveFromTop(riga, colonna, s))
					value =- this.VALUE_WHITE_PAWN;
				if(common.checkWhiteCanArriveFromLeft(riga, colonna, s) && common.checkWhiteCanArriveFromRight(riga, colonna, s))
					value =- this.VALUE_WHITE_PAWN;
				
				//controllo se mi viene mangiato il nero
				if(common.checkBlackCanBeCaptured(riga, colonna, s))
					value =- this.VALUE_BLACK_PAWN*7;
			}
		}
		
		
		
		
		
		return value;
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
				float heu =this.getHeuristicValue(n.getStato());
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
			betterValue=-10000;
			for(Nodo n : liv1.getNodi())
			{
				float b = n.getValue();
				if(betterValue<=b)
				{
					betterValue = b;
					n.getPadre().setValue(betterValue);
				}
			}
			
			betterValue=100000;
			//ciclo tutto il livello 1 (turno bianco, quindi becco il max)
			for(Nodo n : liv0.getNodi())
			{
				float b = n.getValue();
				if(betterValue>=b)
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
