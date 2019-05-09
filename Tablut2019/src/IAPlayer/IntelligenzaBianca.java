package IAPlayer;

import it.unibo.ai.didattica.competition.tablut.domain.Action;
import it.unibo.ai.didattica.competition.tablut.domain.State;
import it.unibo.ai.didattica.competition.tablut.domain.State.Turn;
import it.unibo.ai.didattica.competition.tablut.domain.StateTablut;
import it.unibo.ai.didattica.competition.tablut.exceptions.ActionException;
import it.unibo.ai.didattica.competition.tablut.exceptions.BoardException;
import it.unibo.ai.didattica.competition.tablut.exceptions.CitadelException;
import it.unibo.ai.didattica.competition.tablut.exceptions.ClimbingCitadelException;
import it.unibo.ai.didattica.competition.tablut.exceptions.ClimbingException;
import it.unibo.ai.didattica.competition.tablut.exceptions.DiagonalException;
import it.unibo.ai.didattica.competition.tablut.exceptions.OccupitedException;
import it.unibo.ai.didattica.competition.tablut.exceptions.PawnException;
import it.unibo.ai.didattica.competition.tablut.exceptions.StopException;
import it.unibo.ai.didattica.competition.tablut.exceptions.ThroneException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class IntelligenzaBianca implements IA {


	private static final int TIMETOSTOPTREEGENERATOR = 30000;
	private static final int TIMETOSTOPHEURISTICVALUATOR = 30000;
	private List<String> citadels;
	private static List<Livello> albero;
	private static Action a = null;
	private final int MAX_VALUE = 10000;
	private final int MIN_VALUE = - MAX_VALUE;
	private final int VALUE_BLACK_PAWN = 100;
	private final int VALUE_WHITE_PAWN = 2 * VALUE_BLACK_PAWN;
	private Simulator simulatore;
	private CommonHeuristicFunction common;
	private List<StateTablut> listState; 
	
	public IntelligenzaBianca() {
		albero = new ArrayList<Livello>();
		this.simulatore = new Simulator();
		this.common= new CommonHeuristicFunction();
		this.listState = new ArrayList<StateTablut>();
		this.citadels = common.getCitadels();
	}
	
	/**
	 * Aggiunge un nuovo stato all'elenco degli static che sono stati visitati fin'ora
	 * @param s StateTablut ovvero lo stato che si vuole aggiungere alla lista
	 */
	public void setState(StateTablut s) {
		listState.add(s);
	}
	
	/**
	 * Ritorna il numero di stati differenti che si sono presentati fino a questo momento
	 * @return
	 */
	public int getDimState() {
		return listState.size();
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
					if(common.checkPawnBlocked(i, j, s)) {
						value += VALUE_BLACK_PAWN/4;
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
					if(common.checkPawnBlocked(i, j, s)) {
						value -= VALUE_WHITE_PAWN/4;
					}
				}
				if(s.getBoard()[i][j].equalsPawn("K"))
				{
					rigaRe=i;
					colonnaRe=j;
				}
			}
		}
		
		if(common.getNumberStarFree(s) < 4) {
			
			value -= common.getNumberStarFree(s) * 100; // se le possibilità di vittoria diminuiscono, diminuisce anche il valore di value (100 per ogni star non più libera
			
		} else {
			value += 2*common.getNumberStarFree(s);
		}
		
		if(common.getNumberOfColor("W", s)*3 < common.getNumberOfColor("B", s)) {
			value -= 900 - (common.getNumberOfColor("W", s) * 50); //per ogni pedina bianca tolgo 50
		}
				
		//Controlla se il re si può muovere, se si, riassegna il valore value
		if(common.checkPawnBlocked(rigaRe, colonnaRe, s)) {
			value = getKingPositionValue(rigaRe, colonnaRe, s);
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
			return this.MAX_VALUE-1;
		}
		
		/*
		 * Funzione che controlla se, eseguita una mossa del re in verticale, esso ha liberato un'intera riga (2 oppure 6), in cui vincere (al 100%) il turno successivo
		 */
		if (this.common.checkFreeRowComingFromTop(rigaRe, colonnaRe, s) || this.common.checkFreeRowComingFromBottom(rigaRe, colonnaRe, s)){
			return this.MAX_VALUE-1;
		}
		
		/*
		 * Controllo che il re non vada adiacente ad una cittadella, rischiando di essere mangiato
		 */
		if((this.common.enemyOnTheBottom(rigaRe, colonnaRe, s) || 
				this.common.enemyOnTheLeft(rigaRe, colonnaRe, s) ||
				this.common.enemyOnTheRight(rigaRe, colonnaRe, s) ||
				this.common.enemyOnTheTop(rigaRe, colonnaRe, s )) && rigaRe!=4 && colonnaRe !=4) {
			return this.MIN_VALUE+1;
		}
				
		return value;	
	}
	
	/**
	 * Controlla il numero di pedine e cittadelle presenti sulla stessa riga o colonna del re
	 * @param rigaRe Riga in cui si trova il re
	 * @param colonnaRe Colonna in cui si trova il re
	 * @param s StateTablut ovvero lo stato da valutare
	 * @return un intero che rappresenta il valore della posizione del re
	 */
	private int getKingPositionValue(int rigaRe, int colonnaRe, StateTablut s) {
		int value=0;
		
		//Ciclo che controlla la parte alta della croce
		for(int i=colonnaRe;i>=0;i--) {
			value=+this.getObstacleValue(rigaRe,i,s);
		}
		//Ciclo che controlla la parte destra della croce
		for(int i=rigaRe;i<9;i++) {
			value=+this.getObstacleValue(i,colonnaRe,s);
		}
		for(int i=colonnaRe;i<9;i++) {
			value=+this.getObstacleValue(rigaRe,i,s);
		}
		for(int i=rigaRe;i>=0;i--) {
			value=+this.getObstacleValue(i,colonnaRe,s);
		}
		
		return -value;
	}
	
	private final int WHITE_OBSTACLE = 100;
	private final int BLACK_OBSTACLE = 200;
	private final int CITADEL_OBSTACLE = 1000;
	//Il trono non è considerato perché si trova sullo stesso asse delle cittadelle
	
	
	/**
	 * Controlla se nella posizione passata come parametro è presente un ostacolo
	 * @param riga Riga della posizione da controllare
	 * @param colonna Colonna della posizione da controllare
	 * @param s StateTablut ovvero lo stato da valutare
	 * @return un intero che rappresenta il valore dell'ostacolo presente nella posizione
	 */
	
	private int getObstacleValue(int riga, int colonna, StateTablut s) {
		//Controlla se è una cittadella
		if(common.isCitadel(s.getBox(riga, colonna))) {
			return CITADEL_OBSTACLE;
		}
		//Controlla se è una pedina nera
		if(s.getPawn(riga, colonna).equalsPawn("B")) {
			return BLACK_OBSTACLE;
		}
		//Controlla se è una pedina bianca
		if(s.getPawn(riga, colonna).equalsPawn("W")) {
			return WHITE_OBSTACLE;
		}
		return 0;
	}

	/**
	 * Controlla lo stato genera una situazione di pareggio
	 * @param s StateTablut ovvero lo stato da valutare
	 * @return true se lo stato è già presente nella lista degli stati visitati, false in caso contrario
	 */
	public boolean checkDraw(StateTablut s) {
		if(listState.isEmpty()) {
			listState.add(s);
			return false;
		} else { //se il numero di pedine sulla scacchiera è cambiato vuol dire che una pedina è stata mangiata, quindi svuoto l'elenco degli stati
			if(common.getNumberPawns(s) != common.getNumberPawns(listState.get(listState.size()-1))) {
				listState.clear();
				listState.add(s);
				return false;
			} else { //controllo se lo stato esiste già
				for(int i=0; i<listState.size()-1; i++) {
					if(listState.get(i).equals(s)) {
						//System.out.println("Lo stato esisteva già\n1.\n" + listState.get(i).toString() + "\n2.\n" + s.toString());
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * Funzione euristica: calcola l'euristica
	 * @param s StateTablut ovvero lo stato da valutare
	 * @return Ritorna un intero che indica il valore che è stato assegnato allo stato passato come parametro
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
	

	/*
	 * valuta gli ultimi rami dell'albero
	 * da implementare i tagli ecc...
	 */
	private class HeuristicValuator implements Runnable {
		private IntelligenzaBianca ia;
		//private boolean !Thread.currentThread().isInterrupted();
		private int timeToStopHeuristicValuator;
		
		public HeuristicValuator(IntelligenzaBianca ia, int timeToStopHeuristicValuator){
			this.ia = ia;
			//this.!Thread.currentThread().isInterrupted()=true;
			this.timeToStopHeuristicValuator = timeToStopHeuristicValuator;
		}
		
		public void stopThread()
		{
			//this.!Thread.currentThread().isInterrupted()=false;
		}
		
		public void run() {
			System.out.println("Thread heuristicValuator avviato");
			int x =0;
			//inizializzato per non avere errori di tempo
			a=albero.get(1).getNodi().get(0).getAzione();
			System.out.println("Mossa di default: "+a.toString());
			//ciclo sull'ultimo livello
			for(int i = 0; i<albero.get(albero.size()-1).getNodi().size() && !!Thread.currentThread().isInterrupted(); i++)
			{
				Nodo n = albero.get(albero.size()-1).getNodi().get(i);
				if(!Float.isNaN(n.getPadre().getValue()) 
						&& !Float.isNaN(n.getPadre().getPadre().getValue()) 
						&& n.getPadre().getPadre().getValue()<=n.getPadre().getValue())
				{}
				else
				{
					//non so se sia il giusto metodo, al massimo da cambiare con l'altro
					float heu = ia.getHeuristicValue(n.getStato());
					x++;
					//siccome evolviamo per 4 livelli saranno stati nostri 
					if(Float.isNaN(n.getPadre().getValue()) || heu>n.getPadre().getValue())
					{
						n.getPadre().setValue(heu);
						if(Float.isNaN(n.getPadre().getPadre().getValue()) || n.getPadre().getValue()<n.getPadre().getPadre().getValue())
						{
							n.getPadre().getPadre().setValue(heu);
						}
					}	
				}
				
							
			}
			//ciclo il penultimo livello
			for(int i = 0; i<albero.get(albero.size()-2).getNodi().size() && !!Thread.currentThread().isInterrupted(); i++)
			{
				Nodo n = albero.get(albero.size()-2).getNodi().get(i);
				if(!Float.isNaN(n.getPadre().getValue()) 
						&& !Float.isNaN(n.getPadre().getPadre().getValue()) 
						&& n.getPadre().getPadre().getValue()>=n.getPadre().getValue())
				{}
				else
				{
					if(Float.isNaN(n.getValue()))
					{
						//non so se sia il giusto metodo, al massimo da cambiare con l'altro
						float heu = ia.getHeuristicValue(n.getStato());
						x++;
						if(Float.isNaN(n.getPadre().getValue()) || heu<n.getPadre().getValue())
						{
							n.getPadre().setValue(heu);
							if(Float.isNaN(n.getPadre().getPadre().getValue()) || n.getPadre().getValue()>n.getPadre().getPadre().getValue())
							{
								n.getPadre().getPadre().setValue(heu);
							}
						}	
					}	
				}
							
			}
			
			/*for(Nodo n : albero.get(3).getNodi())
			{
				if(Float.isNaN(n.getPadre().getValue()) || n.getValue()>n.getPadre().getValue())
				{
					n.getPadre().setValue(n.getValue());
				}
			}*/
			for(int i = 0; i<albero.get(2).getNodi().size() && !Thread.currentThread().isInterrupted(); i++)
			{
				Nodo n = albero.get(2).getNodi().get(i);
				if(Float.isNaN(n.getPadre().getValue()) || n.getValue()>n.getPadre().getValue())
				{
					n.getPadre().setValue(n.getValue());
				}
			}
			for(int i = 0; i<albero.get(1).getNodi().size() && !Thread.currentThread().isInterrupted(); i++)
			{
				Nodo n = albero.get(1).getNodi().get(i);
				if(Float.isNaN(n.getPadre().getValue()) || n.getValue()>n.getPadre().getValue())
				{
					n.getPadre().setValue(n.getValue());
					a=n.getAzione();
				}
			}
			//System.out.println(x + " calcoli fatti");
			//System.out.println(a.toString());
			/*for(Livello l: albero)
			{
				l.getNodi().clear();
			}*/
			
			for(Nodo nodo : albero.get(1).getNodi())
			{
				System.out.println("STATO ARRIVABILE ATTRAVERSO MOSSA "+nodo.getAzione());
				//System.out.println("Stato: \n"+nodo.getStato().toString());
				System.out.println("VALORE STATO: "+nodo.getValue());
				System.out.println();
				System.out.println();
				System.out.println();
			}
			albero.clear();
			System.out.println("Albero ripulito: " + albero.size());
			System.out.println("Thread heuristicValuator terminato");
		}
		
	}
	
	/*
	 * thread che crea l'albero di gioco
	 */
	private class TreeGenerator implements Runnable {
		private Nodo nodoAttuale;
		//private Simulator simulatore;
		//private boolean !Thread.currentThread().isInterrupted();
		//private CommonHeuristicFunction iaB;
		private List<String> citadels;
		private int timeToStopTreeGenerator;
		private IntelligenzaBianca ia;
		
		public TreeGenerator(Nodo n, List<String> cit, int timeToStopTreeGenerator, IntelligenzaBianca i) {
			this.nodoAttuale = n;
			this.ia = i;
			//this.simulatore = s;
			//this.!Thread.currentThread().isInterrupted()=true;
			//this.iaB = ia;
			this.citadels = cit;
			this.timeToStopTreeGenerator = timeToStopTreeGenerator;
		}

		public void stopThread()
		{
			//this.!Thread.currentThread().isInterrupted()=false;
		}

		public boolean assiSimmetrici(StateTablut s)
		{
			for(int i=0; i<8; i++)
			{
				if(!s.getPawn(i, 4).equalsPawn(s.getPawn(4, i).toString()))
				{
					return false;
				}
			}
			return true;
		}
		
		//controllo che lo stato sia simmetrico Verticalmente(asse di simmetrica colonna e)
		public boolean statoSimmetricoVerticalmente(StateTablut s)
		{
			//ciclo ogni riga
			for(int i=0; i<9; i++)
			{
				for(int j=0; j<4; j++)
				{
					if(!s.getPawn(i, j).equalsPawn(s.getPawn(i, 8-j).toString()))
					{
						return false;
					}
				}
			}
			return true;
		}
		
		//controllo che lo stato sia simmetrico Orizontalmente(asse di simmetrica riga 5)
		public boolean statoSimmetricoOrizontalmente(StateTablut s)
			{
				//ciclo righe
				for(int i=0; i<4; i++)
				{
					for(int j=0; j<9; j++)
					{
						if(!s.getPawn(i, j).equalsPawn(s.getPawn(8-i, j).toString()))
						{
							return false;
						}
					}
				}
				return true;
			}
		
		//restituisce tutti i nodi a cui è possibile arrivare a partire dal nodo passato
		public List<Nodo> mossePossibiliComplete(Nodo node) throws IOException, BoardException, ActionException, StopException, PawnException, DiagonalException, ClimbingException, ThroneException, OccupitedException, ClimbingCitadelException, CitadelException{
			
			List<Nodo> listaMossePossibili = new ArrayList<Nodo>();
			boolean simmV = this.statoSimmetricoVerticalmente(node.getStato());
			boolean simmO = this.statoSimmetricoOrizontalmente(node.getStato());
			boolean statiSimm = this.assiSimmetrici(node.getStato());
			//itero su tutta la scacchiera
			int righeDaControllare = 9;
			int colonneDaControllare = 9;
			if(simmV)
			{
				colonneDaControllare = 5;
			}
			if(simmO)
			{
				righeDaControllare = 5;
			}
			if(simmV && simmO && statiSimm)
			{
				righeDaControllare = 4;
			}
			
			
			//prima le righe
			for(int i=0; i<righeDaControllare && !Thread.currentThread().isInterrupted(); i++)
			{
				//poi le colonne
				for(int j=0; j<colonneDaControllare && !Thread.currentThread().isInterrupted(); j++)
				{
					//se è il turno nero conto le mosse delle pedine nere
					if(node.getTurn().equalsTurn(State.Turn.BLACK.toString()) && State.Pawn.BLACK.equalsPawn(node.getBoard()[i][j].toString()) && !Thread.currentThread().isInterrupted())
					{
						if(statiSimm && j==4)
						{
							for(Nodo nod: mossePossibiliPedinaCS(node, i, j))
							{
								listaMossePossibili.add(nod);
							}
						}
						else
						{
							if(simmV && simmO && j==4)
							{
								for(Nodo nod: mossePossibiliPedinaCCS(node, i, j))
								{
									listaMossePossibili.add(nod);
								}
							}
							else
							{
								if(simmV && j==4)
								{
									for(Nodo nod: mossePossibiliPedinaSV(node, i, j))
									{
										listaMossePossibili.add(nod);
									}
								}
								else
								{
									if(simmO && i==4)
									{
										for(Nodo nod: mossePossibiliPedinaSO(node, i, j))
										{
											listaMossePossibili.add(nod);
										}
									}
									else
									{
										for(Nodo nod: mossePossibiliPedina(node, i, j))
										{
											listaMossePossibili.add(nod);
										}
									}
								}	
							}	
						}
								
					}
					
					//se è il turno bianco conto le mosse delle pedine bianche
					if(node.getTurn().equalsTurn(State.Turn.WHITE.toString()) && !Thread.currentThread().isInterrupted()) 
					{
						if((node.getStato().getPawn(i, j).equalsPawn("W") || node.getStato().getPawn(i, j).equalsPawn("K")) && !Thread.currentThread().isInterrupted())
						{
							if(statiSimm && j==4)
							{
								for(Nodo nod: mossePossibiliPedinaCS(node, i, j))
								{
									listaMossePossibili.add(nod);
								}
							}
							else
							{
								if(simmV && simmO && j==4)
								{
									for(Nodo nod: mossePossibiliPedinaCCS(node, i, j))
									{
										listaMossePossibili.add(nod);
									}
								}
								else
								{
									if(simmV && j==4)
									{
										for(Nodo nod: mossePossibiliPedinaSV(node, i, j))
										{
											listaMossePossibili.add(nod);
										}
									}
									else
									{
										if(simmO && i==4)
										{
											for(Nodo nod: mossePossibiliPedinaSO(node, i, j))
											{
												listaMossePossibili.add(nod);
											}
										}
										else
										{
											for(Nodo nod: mossePossibiliPedina(node, i, j))
											{
												listaMossePossibili.add(nod);
											}
										}
									}	
								}		
							}	
						}
					}
				}
			}
			for(Nodo n : listaMossePossibili)
			{
				n.setPadre(node);
			}
			return listaMossePossibili;
		}
		
		//ritorna i nodi nei quali è possibile trovarsi col movimento della pedina bianca indicata
		private List<Nodo> mossePossibiliPedina(Nodo node, int riga, int colonna) throws IOException, BoardException, ActionException, StopException, PawnException, DiagonalException, ClimbingException, ThroneException, OccupitedException, ClimbingCitadelException, CitadelException
		{
			List<Nodo> listaMossePossibili = new ArrayList<Nodo>();
			if(canMoveUp(node.getStato(), riga, colonna) && !Thread.currentThread().isInterrupted())
			{
				for(Nodo nod: mossePossibiliPedinaSopra(node, riga, colonna))
				{
					listaMossePossibili.add(nod);
				}
			}
			if(canMoveDown(node.getStato(), riga, colonna) && !Thread.currentThread().isInterrupted())
			{
				
				for(Nodo nod: mossePossibiliPedinaSotto(node, riga, colonna))
				{
					listaMossePossibili.add(nod);
				}
			}
			if(canMoveLeft(node.getStato(), riga, colonna) && !Thread.currentThread().isInterrupted())
			{
				for(Nodo nod: mossePossibiliPedinaSinistra(node, riga, colonna))
				{
					listaMossePossibili.add(nod);
				}
			}
			if(canMoveRight(node.getStato(), riga, colonna) && !Thread.currentThread().isInterrupted())
			{
				for(Nodo nod: mossePossibiliPedinaDestra(node, riga, colonna))
				{
					listaMossePossibili.add(nod);
				}
			}
			return listaMossePossibili;
		}
		
		private List<Nodo> mossePossibiliPedinaCCS(Nodo node, int riga, int colonna) throws IOException, BoardException, ActionException, StopException, PawnException, DiagonalException, ClimbingException, ThroneException, OccupitedException, ClimbingCitadelException, CitadelException
		{
			List<Nodo> listaMossePossibili = new ArrayList<Nodo>();
			if(canMoveUp(node.getStato(), riga, colonna) && !Thread.currentThread().isInterrupted())
			{
				for(Nodo nod: mossePossibiliPedinaSopra(node, riga, colonna))
				{
					listaMossePossibili.add(nod);
				}
			}
			if(canMoveLeft(node.getStato(), riga, colonna))
			{
				for(Nodo nod: mossePossibiliPedinaSinistra(node, riga, colonna))
				{
					listaMossePossibili.add(nod);
				}
			}
			return listaMossePossibili;
		}
		
		private List<Nodo> mossePossibiliPedinaCS(Nodo node, int riga, int colonna) throws IOException, BoardException, ActionException, StopException, PawnException, DiagonalException, ClimbingException, ThroneException, OccupitedException, ClimbingCitadelException, CitadelException
		{
			List<Nodo> listaMossePossibili = new ArrayList<Nodo>();
			if(canMoveLeft(node.getStato(), riga, colonna) && !Thread.currentThread().isInterrupted())
			{
				for(Nodo nod: mossePossibiliPedinaSinistra(node, riga, colonna))
				{
					listaMossePossibili.add(nod);
				}
			}
			return listaMossePossibili;
		}
		
		private List<Nodo> mossePossibiliPedinaSV(Nodo node, int riga, int colonna) throws IOException, BoardException, ActionException, StopException, PawnException, DiagonalException, ClimbingException, ThroneException, OccupitedException, ClimbingCitadelException, CitadelException
		{
			List<Nodo> listaMossePossibili = new ArrayList<Nodo>();
			if(canMoveUp(node.getStato(), riga, colonna) && !Thread.currentThread().isInterrupted())
			{
				for(Nodo nod: mossePossibiliPedinaSopra(node, riga, colonna))
				{
					listaMossePossibili.add(nod);
				}
			}
			if(canMoveDown(node.getStato(), riga, colonna) && !Thread.currentThread().isInterrupted())
			{
				
				for(Nodo nod: mossePossibiliPedinaSotto(node, riga, colonna))
				{
					listaMossePossibili.add(nod);
				}
			}
			if(canMoveLeft(node.getStato(), riga, colonna) && !Thread.currentThread().isInterrupted())
			{
				for(Nodo nod: mossePossibiliPedinaSinistra(node, riga, colonna))
				{
					listaMossePossibili.add(nod);
				}
			}
			return listaMossePossibili;
		}
		
		private List<Nodo> mossePossibiliPedinaSO(Nodo node, int riga, int colonna) throws IOException, BoardException, ActionException, StopException, PawnException, DiagonalException, ClimbingException, ThroneException, OccupitedException, ClimbingCitadelException, CitadelException
		{
			List<Nodo> listaMossePossibili = new ArrayList<Nodo>();
			if(canMoveUp(node.getStato(), riga, colonna) && !Thread.currentThread().isInterrupted())
			{
				for(Nodo nod: mossePossibiliPedinaSopra(node, riga, colonna))
				{
					listaMossePossibili.add(nod);
				}
			}
			if(canMoveLeft(node.getStato(), riga, colonna) && !Thread.currentThread().isInterrupted())
			{
				for(Nodo nod: mossePossibiliPedinaSinistra(node, riga, colonna))
				{
					listaMossePossibili.add(nod);
				}
			}
			if(canMoveRight(node.getStato(), riga, colonna) && !Thread.currentThread().isInterrupted())
			{
				for(Nodo nod: mossePossibiliPedinaDestra(node, riga, colonna))
				{
					listaMossePossibili.add(nod);
				}
			}
			return listaMossePossibili;
		}
		
		//ritorna i nodi nei quali è possibile trovarsi col movimento verso l'alto della pedina indicata
		private List<Nodo> mossePossibiliPedinaSopra(Nodo node, int riga, int colonna) throws IOException, BoardException, ActionException, StopException, PawnException, DiagonalException, ClimbingException, ThroneException, OccupitedException, ClimbingCitadelException, CitadelException
		{
			List<Nodo> listaMossePossibili = new ArrayList<Nodo>();
			int c = 0;
			//stato.setTurn(turno);
			while(canMoveUp(node.getStato(), riga-c, colonna) && !Thread.currentThread().isInterrupted())
			{
				c++;
				Action ac = new Action(node.getStato().getBox(riga, colonna), node.getStato().getBox(riga-c, colonna), node.getTurn());
				StateTablut nuovoStato = (StateTablut) this.checkMove(node.getStato(), ac);
				Nodo nodo2 = new Nodo(nuovoStato);
				nodo2.setAzione(ac);
				listaMossePossibili.add(nodo2);
				//System.out.println(ac);
			}
			return listaMossePossibili;
		}
		
		//ritorna i nodi nei quali è possibile trovarsi col movimento verso il basso della pedina indicata
		private List<Nodo> mossePossibiliPedinaSotto(Nodo node, int riga, int colonna) throws IOException, BoardException, ActionException, StopException, PawnException, DiagonalException, ClimbingException, ThroneException, OccupitedException, ClimbingCitadelException, CitadelException
		{
			List<Nodo> listaMossePossibili = new ArrayList<Nodo>();
			int c = 0;
			//stato.setTurn(turno);
			while(canMoveDown(node.getStato(), riga+c, colonna) && !Thread.currentThread().isInterrupted())
			{
				c++;
				Action ac = new Action(node.getStato().getBox(riga, colonna), node.getStato().getBox(riga+c, colonna), node.getTurn());
				StateTablut nuovoStato = (StateTablut) this.checkMove(node.getStato(), ac);
				Nodo nodo2 = new Nodo(nuovoStato);
				nodo2.setAzione(ac);
				listaMossePossibili.add(nodo2);
				//System.out.println(ac);
			}		
			return listaMossePossibili;
		}
		
		//ritorna i nodi nei quali è possibile trovarsi col movimento verso destra della pedina indicata
		private List<Nodo> mossePossibiliPedinaDestra(Nodo node, int riga, int colonna) throws IOException, BoardException, ActionException, StopException, PawnException, DiagonalException, ClimbingException, ThroneException, OccupitedException, ClimbingCitadelException, CitadelException
		{
			List<Nodo> listaMossePossibili = new ArrayList<Nodo>();
			int c = 0;
			while(canMoveRight(node.getStato(), riga, colonna+c) && !Thread.currentThread().isInterrupted())
			{
				c++;
				Action ac = new Action(node.getStato().getBox(riga, colonna), node.getStato().getBox(riga, colonna+c), node.getTurn());
				StateTablut nuovoStato = (StateTablut) this.checkMove(node.getStato(), ac);
				Nodo nodo2 = new Nodo(nuovoStato);
				nodo2.setAzione(ac);
				listaMossePossibili.add(nodo2);
				//System.out.println(ac);
			}		
			return listaMossePossibili;
		}
		
		//ritorna i nodi nei quali è possibile trovarsi col movimento verso destra della pedina indicata
		private List<Nodo> mossePossibiliPedinaSinistra(Nodo node, int riga, int colonna) throws IOException, BoardException, ActionException, StopException, PawnException, DiagonalException, ClimbingException, ThroneException, OccupitedException, ClimbingCitadelException, CitadelException
		{
			List<Nodo> listaMossePossibili = new ArrayList<Nodo>();
			int c = 0;
			while(canMoveLeft(node.getStato(), riga, colonna-c) && !Thread.currentThread().isInterrupted())
			{
				c++;
				Action ac = new Action(node.getStato().getBox(riga, colonna), node.getStato().getBox(riga, colonna-c), node.getTurn());
				StateTablut nuovoStato =  (StateTablut) this.checkMove(node.getStato(), ac);
				/*System.out.println("AAAAA");
				System.out.println(nuovoStato);*/
				Nodo nodo2 = new Nodo(nuovoStato);
				nodo2.setAzione(ac);
				listaMossePossibili.add(nodo2);
				//System.out.println(ac);
			}			
			return listaMossePossibili;
		}
		
		//dice se una data pedina può muoversi verso l'alto
		private boolean canMoveUp(State state, int row, int column) {
			if(row==0)
			{
				return false;
			}
			if(!state.getPawn(row-1, column).equalsPawn(State.Pawn.EMPTY.toString()) || (this.citadels.contains(state.getBox(row-1, column))&& !this.citadels.contains(state.getBox(row, column))))
			{
				return false;
			}
			return true;		
		}
		
		//dice se una data pedina può muoversi verso il basso
		private boolean canMoveDown(State state, int row, int column) {
			if(row==state.getBoard().length-1)
			{
				return false;
			}
			if(!state.getPawn(row+1, column).equalsPawn(State.Pawn.EMPTY.toString()) || (this.citadels.contains(state.getBox(row+1, column))&& !this.citadels.contains(state.getBox(row, column))))
			{
				return false;
			}
			return true;	
		}
		
		//dice se una data pedina può muoversi verso sinistra
		private boolean canMoveLeft(State state, int row, int column) {
			if(column==0)
			{
				return false;
			}
			if(!state.getPawn(row, column-1).equalsPawn(State.Pawn.EMPTY.toString()) || (this.citadels.contains(state.getBox(row, column-1)) && !this.citadels.contains(state.getBox(row, column))))
			{
				return false;
			}
			return true;	
		}
		
		//dice se una data pedina può muoversi verso destra
		private boolean canMoveRight(State state, int row, int column) {
			if(column==state.getBoard().length-1)
			{
				return false;
			}
			if(!state.getPawn(row, column+1).equalsPawn(State.Pawn.EMPTY.toString()) || (this.citadels.contains(state.getBox(row, column+1)) && !this.citadels.contains(state.getBox(row, column))))
			{
				return false;
			}
			return true;
		}
		
		//applico la mossa
		private State checkMove(State state, Action a) throws BoardException, ActionException, StopException, PawnException, DiagonalException, ClimbingException, ThroneException, OccupitedException, ClimbingCitadelException, CitadelException {
			
			// se sono arrivato qui, muovo la pedina
			StateTablut newState = (StateTablut) this.movePawn(state, a);

			// a questo punto controllo lo stato per eventuali catture
			if (newState.getTurn().equalsTurn("W")) {
				newState = this.checkCaptureBlack(newState, a);
			} else if (newState.getTurn().equalsTurn("B")) {
				newState = this.checkCaptureWhite(newState, a);
			}

			// if something has been captured, clear cache for draws
			/*if (this.movesWithutCapturing == 0) {
				this.drawConditions.clear();
			}

			// controllo pareggio
			int trovati = 0;
			for (State s : drawConditions) {

				//System.out.println(s.toString());

				if (s.equals(state)) {
						trovati++;
					if (trovati > repeated_moves_allowed) {
						//state.setTurn(State.Turn.DRAW);
						break;
					}
				}
			}
			if (cache_size >= 0 && this.drawConditions.size() > cache_size) {
				this.drawConditions.remove(0);
			}*/
			//this.drawConditions.add(state.clone());


			return newState;
		}
		
		private State movePawn(State state, Action a) {
			StateTablut newState = new StateTablut();
			
			State.Pawn pawn = state.getPawn(a.getRowFrom(), a.getColumnFrom());
			State.Pawn[][] newBoard = new State.Pawn[9][9];
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {
					newBoard[i][j] = state.getPawn(i, j);
				}
			}
			//libero il trono o una casella qualunque
			if(a.getColumnFrom()==4 && a.getRowFrom()==4)
			{
				newBoard[a.getRowFrom()][a.getColumnFrom()]= State.Pawn.THRONE;
			}
			else
			{
				newBoard[a.getRowFrom()][a.getColumnFrom()]= State.Pawn.EMPTY;
			}
			
			//metto nel nuovo tabellone la pedina mossa
			newBoard[a.getRowTo()][a.getColumnTo()]=pawn;
			//aggiorno il tabellone
			newState.setBoard(newBoard);
			
			//cambio il turno
			if(state.getTurn().equalsTurn(State.Turn.WHITE.toString()))
			{
				newState.setTurn(State.Turn.BLACK);
			}
			else
			{
				newState.setTurn(State.Turn.WHITE);
			}
			
			
			return newState;
		}

		private StateTablut checkCaptureWhite(StateTablut state, Action a) {
			// controllo se mangio a destra
			if (a.getColumnTo() < state.getBoard().length - 2
					&& state.getPawn(a.getRowTo(), a.getColumnTo() + 1).equalsPawn("B")
					&& (state.getPawn(a.getRowTo(), a.getColumnTo() + 2).equalsPawn("W")
							|| state.getPawn(a.getRowTo(), a.getColumnTo() + 2).equalsPawn("T")
							|| state.getPawn(a.getRowTo(), a.getColumnTo() + 2).equalsPawn("K")
							|| (this.citadels.contains(state.getBox(a.getRowTo(), a.getColumnTo() + 2)) &&!(a.getColumnTo()+2==8&&a.getRowTo()==4)&&!(a.getColumnTo()+2==4&&a.getRowTo()==0)&&!(a.getColumnTo()+2==4&&a.getRowTo()==8)&&!(a.getColumnTo()+2==0&&a.getRowTo()==4)))) {
				state.removePawn(a.getRowTo(), a.getColumnTo() + 1);
				//this.movesWithutCapturing = -1;
			}
			// controllo se mangio a sinistra
			if (a.getColumnTo() > 1 && state.getPawn(a.getRowTo(), a.getColumnTo() - 1).equalsPawn("B")
					&& (state.getPawn(a.getRowTo(), a.getColumnTo() - 2).equalsPawn("W")
							|| state.getPawn(a.getRowTo(), a.getColumnTo() - 2).equalsPawn("T")
							|| state.getPawn(a.getRowTo(), a.getColumnTo() - 2).equalsPawn("K")
							|| (this.citadels.contains(state.getBox(a.getRowTo(), a.getColumnTo() - 2)) &&!(a.getColumnTo()-2==8&&a.getRowTo()==4)&&!(a.getColumnTo()-2==4&&a.getRowTo()==0)&&!(a.getColumnTo()-2==4&&a.getRowTo()==8)&&!(a.getColumnTo()-2==0&&a.getRowTo()==4)))) {
				state.removePawn(a.getRowTo(), a.getColumnTo() - 1);
				//this.movesWithutCapturing = -1;
			}
			// controllo se mangio sopra
			if (a.getRowTo() > 1 && state.getPawn(a.getRowTo() - 1, a.getColumnTo()).equalsPawn("B")
					&& (state.getPawn(a.getRowTo() - 2, a.getColumnTo()).equalsPawn("W")
							|| state.getPawn(a.getRowTo() - 2, a.getColumnTo()).equalsPawn("T")
							|| state.getPawn(a.getRowTo() - 2, a.getColumnTo()).equalsPawn("K")
							|| (this.citadels.contains(state.getBox(a.getRowTo() - 2, a.getColumnTo()))&&!(a.getColumnTo()==8&&a.getRowTo()-2==4)&&!(a.getColumnTo()==4&&a.getRowTo()-2==0)&&!(a.getColumnTo()==4&&a.getRowTo()-2==8)&&!(a.getColumnTo()==0&&a.getRowTo()-2==4)) )) {
				state.removePawn(a.getRowTo() - 1, a.getColumnTo());
				//this.movesWithutCapturing = -1;
			}
			// controllo se mangio sotto
			if (a.getRowTo() < state.getBoard().length - 2
					&& state.getPawn(a.getRowTo() + 1, a.getColumnTo()).equalsPawn("B")
					&& (state.getPawn(a.getRowTo() + 2, a.getColumnTo()).equalsPawn("W")
							|| state.getPawn(a.getRowTo() + 2, a.getColumnTo()).equalsPawn("T")
							|| state.getPawn(a.getRowTo() + 2, a.getColumnTo()).equalsPawn("K")
							|| (this.citadels.contains(state.getBox(a.getRowTo() + 2, a.getColumnTo()))&&!(a.getColumnTo()==8&&a.getRowTo()+2==4)&&!(a.getColumnTo()==4&&a.getRowTo()+2==0)&&!(a.getColumnTo()==4&&a.getRowTo()+2==8)&&!(a.getColumnTo()==0&&a.getRowTo()+2==4)))) {
				state.removePawn(a.getRowTo() + 1, a.getColumnTo());
				//this.movesWithutCapturing = -1;
			}
			// controllo se ho vinto
			if (a.getRowTo() == 0 || a.getRowTo() == state.getBoard().length - 1 || a.getColumnTo() == 0
					|| a.getColumnTo() == state.getBoard().length - 1) {
				if (state.getPawn(a.getRowTo(), a.getColumnTo()).equalsPawn("K")) {
					state.setTurn(State.Turn.WHITEWIN);
				}
			}

			//this.movesWithutCapturing++;
			return state;
		}

		private StateTablut checkCaptureBlack(StateTablut state, Action a) {
			
			this.checkCaptureBlackPawnRight(state, a);
			this.checkCaptureBlackPawnLeft(state, a);
			this.checkCaptureBlackPawnUp(state, a);
			this.checkCaptureBlackPawnDown(state, a);
			this.checkCaptureBlackKingRight(state, a);
			this.checkCaptureBlackKingLeft(state, a);
			this.checkCaptureBlackKingDown(state, a);
			this.checkCaptureBlackKingUp(state, a);

			//this.movesWithutCapturing++;
			return state;
		}

		private State checkCaptureBlackKingLeft(State state, Action a){
		//ho il re sulla sinistra
		if (a.getColumnTo()>1&&state.getPawn(a.getRowTo(), a.getColumnTo()-1).equalsPawn("K"))
		{
			//re sul trono
			if(state.getBox(a.getRowTo(), a.getColumnTo()-1).equals("e5"))
			{
				if(state.getPawn(3, 4).equalsPawn("B")
						&& state.getPawn(4, 3).equalsPawn("B")
						&& state.getPawn(5, 4).equalsPawn("B"))
				{
					state.setTurn(State.Turn.BLACKWIN);
				}
			}
			//re adiacente al trono
			if(state.getBox(a.getRowTo(), a.getColumnTo()-1).equals("e4"))
			{		
				if(state.getPawn(2, 4).equalsPawn("B")
						&& state.getPawn(3, 3).equalsPawn("B"))
				{
					state.setTurn(State.Turn.BLACKWIN);
				}
			}
			if(state.getBox(a.getRowTo(), a.getColumnTo()-1).equals("f5"))
			{
				if(state.getPawn(5, 5).equalsPawn("B")
						&& state.getPawn(3, 5).equalsPawn("B"))
				{
					state.setTurn(State.Turn.BLACKWIN);
				}
			}
			if(state.getBox(a.getRowTo(), a.getColumnTo()-1).equals("e6"))
			{
				if(state.getPawn(6, 4).equalsPawn("B")
						&& state.getPawn(5, 3).equalsPawn("B"))
				{
					state.setTurn(State.Turn.BLACKWIN);
				}
			}
			//sono fuori dalle zone del trono
			if(!state.getBox(a.getRowTo(), a.getColumnTo()-1).equals("e5")
					&& !state.getBox(a.getRowTo(), a.getColumnTo()-1).equals("e6")
					&& !state.getBox(a.getRowTo(), a.getColumnTo()-1).equals("e4")
					&& !state.getBox(a.getRowTo(), a.getColumnTo()-1).equals("f5"))
			{
				if(state.getPawn(a.getRowTo(), a.getColumnTo() - 2).equalsPawn("B")
						|| this.citadels.contains(state.getBox(a.getRowTo(), a.getColumnTo()-2)))
				{
					state.setTurn(State.Turn.BLACKWIN);
				}					
			}
		}		
		return state;
	}
	
		private State checkCaptureBlackKingRight(State state, Action a){
		//ho il re sulla destra
			if (a.getColumnTo()<state.getBoard().length-2&&(state.getPawn(a.getRowTo(),a.getColumnTo()+1).equalsPawn("K")))				
			{
				//re sul trono
				if(state.getBox(a.getRowTo(), a.getColumnTo()+1).equals("e5"))
				{
					if(state.getPawn(3, 4).equalsPawn("B")
							&& state.getPawn(4, 5).equalsPawn("B")
							&& state.getPawn(5, 4).equalsPawn("B"))
					{
						state.setTurn(State.Turn.BLACKWIN);
					}
				}
				//re adiacente al trono
			if(state.getBox(a.getRowTo(), a.getColumnTo()+1).equals("e4"))
			{
				if(state.getPawn(2, 4).equalsPawn("B")
						&& state.getPawn(3, 5).equalsPawn("B"))
				{
					state.setTurn(State.Turn.BLACKWIN);
				}
			}
			if(state.getBox(a.getRowTo(), a.getColumnTo()+1).equals("e6"))
			{
				if(state.getPawn(5, 5).equalsPawn("B")
						&& state.getPawn(6, 4).equalsPawn("B"))
				{
					state.setTurn(State.Turn.BLACKWIN);
				}
			}
			if(state.getBox(a.getRowTo(), a.getColumnTo()+1).equals("d5"))
			{
				if(state.getPawn(3, 3).equalsPawn("B")
						&& state.getPawn(5, 3).equalsPawn("B"))
				{
					state.setTurn(State.Turn.BLACKWIN);
				}
			}
			//sono fuori dalle zone del trono
			if(!state.getBox(a.getRowTo(), a.getColumnTo()+1).equals("d5")
					&& !state.getBox(a.getRowTo(), a.getColumnTo()+1).equals("e6")
					&& !state.getBox(a.getRowTo(), a.getColumnTo()+1).equals("e4")
					&& !state.getBox(a.getRowTo(), a.getColumnTo()+1).equals("e5"))
			{
				if(state.getPawn(a.getRowTo(), a.getColumnTo() + 2).equalsPawn("B")
						|| this.citadels.contains(state.getBox(a.getRowTo(), a.getColumnTo()+2)))
				{
					state.setTurn(State.Turn.BLACKWIN);
				}					
			}
		}
		return state;
	}
	
		private State checkCaptureBlackKingDown(State state, Action a){
		//ho il re sotto
		if (a.getRowTo()<state.getBoard().length-2&&state.getPawn(a.getRowTo()+1,a.getColumnTo()).equalsPawn("K"))
		{
			//System.out.println("Ho il re sotto");
			//re sul trono
			if(state.getBox(a.getRowTo()+1, a.getColumnTo()).equals("e5"))
			{
				if(state.getPawn(5, 4).equalsPawn("B")
						&& state.getPawn(4, 5).equalsPawn("B")
						&& state.getPawn(4, 3).equalsPawn("B"))
				{
					state.setTurn(State.Turn.BLACKWIN);
				}
			}
			//re adiacente al trono
			if(state.getBox(a.getRowTo()+1, a.getColumnTo()).equals("e4"))
			{
				if(state.getPawn(3, 3).equalsPawn("B")
						&& state.getPawn(3, 5).equalsPawn("B"))
				{
					state.setTurn(State.Turn.BLACKWIN);
				}
			}
			if(state.getBox(a.getRowTo()+1, a.getColumnTo()).equals("d5"))
			{
				if(state.getPawn(4, 2).equalsPawn("B")
						&& state.getPawn(5, 3).equalsPawn("B"))
				{
					state.setTurn(State.Turn.BLACKWIN);
				}
			}
			if(state.getBox(a.getRowTo()+1, a.getColumnTo()).equals("f5"))
			{
				if(state.getPawn(4, 6).equalsPawn("B")
						&& state.getPawn(5, 5).equalsPawn("B"))
				{
					state.setTurn(State.Turn.BLACKWIN);
				}
			}
			//sono fuori dalle zone del trono
			if(!state.getBox(a.getRowTo()+1, a.getColumnTo()).equals("d5")
					&& !state.getBox(a.getRowTo()+1, a.getColumnTo()).equals("e4")
					&& !state.getBox(a.getRowTo()+1, a.getColumnTo()).equals("f5")
					&& !state.getBox(a.getRowTo()+1, a.getColumnTo()).equals("e5"))
			{
				if(state.getPawn(a.getRowTo()+2, a.getColumnTo()).equalsPawn("B")
						|| this.citadels.contains(state.getBox(a.getRowTo()+2, a.getColumnTo())))
				{
					state.setTurn(State.Turn.BLACKWIN);
				}					
			}			
		}		
		return state;
	}
	
		private State checkCaptureBlackKingUp(State state, Action a){
		//ho il re sopra
		if (a.getRowTo()>1&&state.getPawn(a.getRowTo()-1, a.getColumnTo()).equalsPawn("K"))
		{
			//re sul trono
			if(state.getBox(a.getRowTo()-1, a.getColumnTo()).equals("e5"))
			{
				if(state.getPawn(3, 4).equalsPawn("B")
						&& state.getPawn(4, 5).equalsPawn("B")
						&& state.getPawn(4, 3).equalsPawn("B"))
				{
					state.setTurn(State.Turn.BLACKWIN);
				}
			}
			//re adiacente al trono
			if(state.getBox(a.getRowTo()-1, a.getColumnTo()).equals("e6"))
			{
				if(state.getPawn(5, 3).equalsPawn("B")
						&& state.getPawn(5, 5).equalsPawn("B"))
				{
					state.setTurn(State.Turn.BLACKWIN);
				}
			}
			if(state.getBox(a.getRowTo()-1, a.getColumnTo()).equals("d5"))
			{
				if(state.getPawn(4, 2).equalsPawn("B")
						&& state.getPawn(3, 3).equalsPawn("B"))
				{
					state.setTurn(State.Turn.BLACKWIN);
				}
			}
			if(state.getBox(a.getRowTo()-1, a.getColumnTo()).equals("f5"))
			{
				if(state.getPawn(4, 6).equalsPawn("B")
						&& state.getPawn(3, 5).equalsPawn("B"))
				{
					state.setTurn(State.Turn.BLACKWIN);
				}
			}
			//sono fuori dalle zone del trono
			if(!state.getBox(a.getRowTo()-1, a.getColumnTo()).equals("d5")
					&& !state.getBox(a.getRowTo()-1, a.getColumnTo()).equals("e4")
					&& !state.getBox(a.getRowTo()-1, a.getColumnTo()).equals("f5")
					&& !state.getBox(a.getRowTo()-1, a.getColumnTo()).equals("e5"))
			{
				if(state.getPawn(a.getRowTo()-2, a.getColumnTo()).equalsPawn("B")
						|| this.citadels.contains(state.getBox(a.getRowTo()-2, a.getColumnTo())))
				{
					state.setTurn(State.Turn.BLACKWIN);
				}					
			}	
		}
		return state;
	}
	
		private void checkCaptureBlackPawnRight(State state, Action a)	{
			//mangio a destra
			if (a.getColumnTo() < state.getBoard().length - 2 && state.getPawn(a.getRowTo(), a.getColumnTo() + 1).equalsPawn("W"))
			{
				if(state.getPawn(a.getRowTo(), a.getColumnTo() + 2).equalsPawn("B"))
				{
					state.removePawn(a.getRowTo(), a.getColumnTo() + 1);
					//this.movesWithutCapturing = -1;
				}
				if(state.getPawn(a.getRowTo(), a.getColumnTo() + 2).equalsPawn("T"))
				{
					state.removePawn(a.getRowTo(), a.getColumnTo() + 1);
					//this.movesWithutCapturing = -1;
				}
				if(this.citadels.contains(state.getBox(a.getRowTo(), a.getColumnTo() + 2)))
				{
					state.removePawn(a.getRowTo(), a.getColumnTo() + 1);
					//this.movesWithutCapturing = -1;
				}
				if(state.getBox(a.getRowTo(), a.getColumnTo()+2).equals("e5"))
				{
					state.removePawn(a.getRowTo(), a.getColumnTo() + 1);
					//this.movesWithutCapturing = -1;
				}
				
			}
		}
		
		private void checkCaptureBlackPawnLeft(State state, Action a){
			//mangio a sinistra
			if (a.getColumnTo() > 1
					&& state.getPawn(a.getRowTo(), a.getColumnTo() - 1).equalsPawn("W")
					&& (state.getPawn(a.getRowTo(), a.getColumnTo() - 2).equalsPawn("B")
							|| state.getPawn(a.getRowTo(), a.getColumnTo() - 2).equalsPawn("T")
							|| this.citadels.contains(state.getBox(a.getRowTo(), a.getColumnTo() - 2))
							|| (state.getBox(a.getRowTo(), a.getColumnTo()-2).equals("e5"))))
			{
				state.removePawn(a.getRowTo(), a.getColumnTo() - 1);
				//this.movesWithutCapturing = -1;
			}
		}
		
		private void checkCaptureBlackPawnUp(State state, Action a){
			// controllo se mangio sopra
			if (a.getRowTo() > 1
					&& state.getPawn(a.getRowTo() - 1, a.getColumnTo()).equalsPawn("W")
					&& (state.getPawn(a.getRowTo() - 2, a.getColumnTo()).equalsPawn("B")
							|| state.getPawn(a.getRowTo() - 2, a.getColumnTo()).equalsPawn("T")
							|| this.citadels.contains(state.getBox(a.getRowTo() - 2, a.getColumnTo()))
							|| (state.getBox(a.getRowTo()-2, a.getColumnTo()).equals("e5"))))
			{
				state.removePawn(a.getRowTo()-1, a.getColumnTo());
				//this.movesWithutCapturing = -1;
			}
		}
		
		private void checkCaptureBlackPawnDown(State state, Action a){
			// controllo se mangio sotto
			if (a.getRowTo() < state.getBoard().length - 2
					&& state.getPawn(a.getRowTo() + 1, a.getColumnTo()).equalsPawn("W")
					&& (state.getPawn(a.getRowTo() + 2, a.getColumnTo()).equalsPawn("B")
							|| state.getPawn(a.getRowTo() + 2, a.getColumnTo()).equalsPawn("T")
							|| this.citadels.contains(state.getBox(a.getRowTo() + 2, a.getColumnTo()))
							|| (state.getBox(a.getRowTo()+2, a.getColumnTo()).equals("e5"))))
			{
				state.removePawn(a.getRowTo()+1, a.getColumnTo());
				//this.movesWithutCapturing = -1;
			}
		}
		
		public void run() {
			System.out.println("Attivazione thread treeGenerator");
			try {
				System.out.println("Nodo attuale: \n"+this.nodoAttuale.getStato());
				Livello liv = new Livello();
				liv.add(this.nodoAttuale);
				albero.add(liv);
				Livello livEspanso = null;
				for(int livelloDaEspandere=0; !Thread.currentThread().isInterrupted() ;livelloDaEspandere++)
				{
					livEspanso = new Livello();
					albero.add(livEspanso);
					for(int x=0; x<albero.get(livelloDaEspandere).getNodi().size() && !Thread.currentThread().isInterrupted(); x++)
					{
						Nodo n = albero.get(livelloDaEspandere).getNodi().get(x);
						long x1 = System.currentTimeMillis();
						List<Nodo> mosse = this.mossePossibiliComplete(n);
						for(int y=0; y<mosse.size() && !Thread.currentThread().isInterrupted(); y++)
						{
							Nodo nodo = mosse.get(y);
							if(nodo.getStato().getTurn().equalsTurn("WW"))
							{
								nodo.setValue(10000);
							}
							if(nodo.getStato().getTurn().equalsTurn("BW"))
							{
								nodo.setValue(-10000);
							}
							if(this.ia.checkDraw(nodo.getStato()))
							{
								nodo.setValue(-5000);
								nodo.getStato().setTurn(Turn.DRAW);
							}
							livEspanso.add(nodo);
						}
						if(Thread.currentThread().isInterrupted())
						{
							long x2 = System.currentTimeMillis();
							System.out.println("Tempo utilizzato: " + (x2-x1) + " Numero mosse trovate: "+ mosse.size());
						}
						if(livelloDaEspandere==0)
						{
							for(Nodo nodo : albero.get(1).getNodi())
							{
								if(nodo.getStato().getTurn().equalsTurn("WW"))
								{
									nodo.setValue(10000);
								}
								if(nodo.getStato().getTurn().equalsTurn("BW"))
								{
									nodo.setValue(10000);
								}						
								if(nodo.getStato().getTurn().equalsTurn("W") || nodo.getStato().getTurn().equalsTurn("B"))
								{
									nodo.setValue(this.setSimpleHeuristic(nodo.getStato()));
								}
							}
							Collections.sort(albero.get(1).getNodi(), new Comparator<Nodo>() {
								@Override
						    	public int compare(Nodo n2, Nodo n1)
								{
									return  (int) (n1.getValue()-n2.getValue());
								}
						    });
							for(Nodo nodo : albero.get(1).getNodi())
							{
								System.out.println("STATO ARRIVABILE ATTRAVERSO MOSSA "+nodo.getAzione());
								//System.out.println("Stato: \n"+nodo.getStato().toString());
								System.out.println("VALORE STATO: "+nodo.getValue());
								System.out.println();
								System.out.println();
								System.out.println();
								if(nodo.getValue()!=10000 && nodo.getValue()!=-10000 && !nodo.getTurn().equalsTurn("D"))
								{
									nodo.setValue(Float.NaN);
								}
							}
						}
					}
				}
				System.out.println("Thread treeGenerator interrotto");
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		private float setSimpleHeuristic(StateTablut s) {
			int nBianchi = 1;
			int nNeri=0;
			for(int x =0 ; x<9; x++)
			{
				for(int y=0; y<9; y++)
				{
					if(s.getPawn(x, y).equalsPawn("B"))
					{
						nNeri++;
					}
					if(s.getPawn(x, y).equalsPawn("W"))
					{
						nBianchi++;
					}
				}
			}
			return 2*nBianchi-nNeri;				
		}

	}
	
	@SuppressWarnings("static-access")
	@Override
	public synchronized Action getBetterMove(StateTablut s) {


		long t1 = System.currentTimeMillis();
		long t3 = 0;

		try {
			Nodo node = new Nodo(s);
			TreeGenerator treeGenerator = new TreeGenerator(node, this.citadels, TIMETOSTOPTREEGENERATOR, this);
			Thread t = new Thread(treeGenerator);
			t.start();
			//this.wait(30000);
			Thread.sleep(3000);
			//System.out.println("Lancio l'interruzione");
			//treeGenerator.stopThread();
			t.interrupt();
			//t.stop();
			//System.out.println("Finito sviluppo albero");
			t3 = System.currentTimeMillis();
			System.out.println("Tempo trascorso sviluppo albero: "+(t3-t1)+" millisecondi");
			for(int x=0; x<albero.size(); x++)
			{
				System.out.println("Nodi espansi livello " + x +": "+albero.get(x).getNodi().size());
			}
			
			
			HeuristicValuator heuristicValuator = new HeuristicValuator(this, TIMETOSTOPHEURISTICVALUATOR);
			t = new Thread(heuristicValuator);
			t.start();
			//this.wait(10000);
			Thread.sleep(3000);
			//System.out.println("Lancio l'interruzione");
			t.interrupt();
			//heuristicValuator.stopThread();
			//System.out.println("Finito sviluppo euristica");
			//t.stop();
			

			
			//System.out.println("Livello 3 espanso");

			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long t2 = System.currentTimeMillis();
		System.out.println("Tempo trascorso sviluppo euristica: "+(t2-t3)+" millisecondi");
		System.out.println("Mossa: "+a.toString());
		System.out.println("");
		return a;
	}
	
}
