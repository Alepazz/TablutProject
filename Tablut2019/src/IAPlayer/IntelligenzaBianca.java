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
import java.util.LinkedList;
import java.util.List;

public class IntelligenzaBianca implements IA {


	private int TIMETOSTOPTREEGENERATOR;
	private List<String> citadels;
	private static List<Livello> albero;
	private static Action a = null;
	private final int MAX_VALUE = 100000;
	private final int MIN_VALUE = - MAX_VALUE;
	private final int VALUE_BLACK_PAWN = 100;
	private final int VALUE_WHITE_PAWN = 2 * VALUE_BLACK_PAWN;
	private Simulator simulatore;
	private CommonHeuristicFunction common;
	private List<StateTablut> listState; 
	
	public IntelligenzaBianca(int secondi) {
		albero = new ArrayList<Livello>();
		this.simulatore = new Simulator();
		this.common= new CommonHeuristicFunction();
		this.listState = new ArrayList<StateTablut>();
		this.citadels = common.getCitadels();
		this.TIMETOSTOPTREEGENERATOR = secondi * 1000;
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
		 * Pedina Bianca Mangiabile: 150
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
						value += VALUE_BLACK_PAWN/2;
					}
					/*if(common.checkPawnBlocked(i, j, s)) {
						value += VALUE_BLACK_PAWN/4;
					}*/
				}
				if(s.getBoard()[i][j].equalsPawn("W"))
				{
					nBianchi++;
					value += VALUE_WHITE_PAWN;
					if(common.checkWhiteCanBeCaptured(i, j, s))
					{
						value -= VALUE_WHITE_PAWN/2;
					}
					/*if(common.checkPawnBlocked(i, j, s)) {
						value -= VALUE_WHITE_PAWN/4;
					}*/
				}
				if(s.getBoard()[i][j].equalsPawn("K"))
				{
					rigaRe=i;
					colonnaRe=j;
				}
			}
		}
		
		//condizioni finali di vittoria
		int vieDiFuga=this.common.checkVieDiFugaRe(rigaRe, colonnaRe, s);
				
		//controllo se nella mossa del nero mi mangia il re
		if((vieDiFuga==1 && s.getTurn().equalsTurn("W")) || (vieDiFuga > 1 && !common.kingCanBeCaptured(rigaRe, colonnaRe, s)))
		{
			return this.MAX_VALUE-1;
		}
		if(vieDiFuga==1 && s.getTurn().equalsTurn("B"))
		{
			if(common.blackCannotBlockEscape(rigaRe, colonnaRe, s) && !common.kingCanBeCaptured(rigaRe, colonnaRe, s))
			{
				return this.MAX_VALUE-1;
			}
		}
		
		/*if(common.checkFreeRowComingFromBottom(rigaRe, colonnaRe, s) 
				|| common.checkFreeRowComingFromTop(rigaRe, colonnaRe, s)
				|| common.checkFreeColComingFromLeft(rigaRe, colonnaRe, s)
				|| common.checkFreeColComingFromRight(rigaRe, colonnaRe, s)) {
			if(s.getTurn().equalsTurn("W")) {
				value += 600;
			} else { //turno nero
				value += 100;
			}
		}*/
		
		//valuto molto il fatto che il re sia fuori dal trono
		if(!common.kingOnTheThrone(rigaRe, colonnaRe)) {
			value += 800;
		}
		
		//valuto molto il fatto che il re, possa essere mangiato -- compensa quella di prima
		if(common.kingCanBeCaptured(rigaRe, colonnaRe, s)) {
			if(s.getTurn().equalsTurn("B")) {
				return this.MIN_VALUE+1;
			} else { //turno del bianco
				value -= 3000;
			}
		}
				
		int numberOfStarFree = common.getNumberStarFree(s);
		if(numberOfStarFree < 4) {		
			value -= (8-numberOfStarFree) * 350; // se le possibilità di vittoria diminuiscono, diminuisce anche il valore di value (350 per ogni star non più libera) -- max -2800		
		}
		
		//cerchiamo di tenere il re con un po' di spazio
		if(common.checkPawnBlocked(rigaRe, colonnaRe, s)) {
			value -= 300;
		}
		
		//cerchiamo di tenere il re isolato, così da non essere schiacciato
		/*if(common.checkPedinaIsolata(rigaRe, colonnaRe, s)) {
			value += 100;
		}*/
		
		//controlla che il re non abbia più di una nera vicino
		if((common.checkNeighbourBottom(rigaRe, colonnaRe, s).equals("B") ||
				//common.checkNeighbourBottomLeft(rigaRe, colonnaRe, s).contentEquals("B") ||
				//common.checkNeighbourBottomRight(rigaRe, colonnaRe, s).equals("B") ||
				common.checkNeighbourLeft(rigaRe, colonnaRe, s).equals("B") ||
				common.checkNeighbourRight(rigaRe, colonnaRe, s).equals("B") ||
				common.checkNeighbourTop(rigaRe, colonnaRe, s).equals("B")) //||
				//common.checkNeighbourTopLeft(rigaRe, colonnaRe, s).equals("B") ||
				//common.checkNeighbourTopRight(rigaRe, colonnaRe, s).equals("B")) 
				&& common.checkBlackCanArriveAdjacent(rigaRe, colonnaRe, s)); {
			value -= 200;
			
		}
		
		//evito situazioni di vittoria per il nero (nel turno del nero), o di possibile pericolo nel turno del bianco
		/*if(common.checkNeighbourBottom(rigaRe, colonnaRe, s).equals("B") || common.checkNeighbourBottom(rigaRe, colonnaRe, s).equals("T") || common.checkNeighbourBottom(rigaRe, colonnaRe, s).equals("C")) {
			if(common.checkBlackCanArriveAdjacentInTopPosition(rigaRe, colonnaRe, s)) {
				if(s.getTurn().equalsTurn("B")) {
					return this.MIN_VALUE-1;
				} else { //turno del bianco
					value -= 200;
				}		
			}
		}
		
		if(common.checkNeighbourTop(rigaRe, colonnaRe, s).equals("B") || common.checkNeighbourTop(rigaRe, colonnaRe, s).equals("T") || common.checkNeighbourTop(rigaRe, colonnaRe, s).equals("C")) {
			if(common.checkBlackCanArriveAdjacentInBottomPosition(rigaRe, colonnaRe, s)) {
				if(s.getTurn().equalsTurn("B")) {
					return this.MIN_VALUE-1;
				} else { //turno del bianco
					value -= 200;
				}		
			}
		}
		
		if(common.checkNeighbourLeft(rigaRe, colonnaRe, s).equals("B") || common.checkNeighbourLeft(rigaRe, colonnaRe, s).equals("T") || common.checkNeighbourLeft(rigaRe, colonnaRe, s).equals("C")) {
			if(common.checkBlackCanArriveAdjacentInRightPosition(rigaRe, colonnaRe, s)) {
				if(s.getTurn().equalsTurn("B")) {
					return this.MIN_VALUE-1;
				} else { //turno del bianco
					value -= 200;
				}		
			}
		}
		
		if(common.checkNeighbourRight(rigaRe, colonnaRe, s).equals("B") || common.checkNeighbourRight(rigaRe, colonnaRe, s).equals("T") || common.checkNeighbourRight(rigaRe, colonnaRe, s).equals("C")) {
			if(common.checkBlackCanArriveAdjacentInLeftPosition(rigaRe, colonnaRe, s)) {
				if(s.getTurn().equalsTurn("B")) {
					return this.MIN_VALUE-1;
				} else { //turno del bianco
					value -= 200;
				}		
			}
		}*/
		
		/*
		 * this.perfectPosition.add("12");
		 * this.perfectPosition.add("21");
		 * this.perfectPosition.add("16");
		 * this.perfectPosition.add("27");
		 * this.perfectPosition.add("61");
		 * this.perfectPosition.add("72");
		 * this.perfectPosition.add("67");
		 * this.perfectPosition.add("76");
		 */
		
		//top left
		if(s.getPawn(1, 2).equalsPawn("B") && !s.getPawn(2, 1).equalsPawn("B")) {
			if(s.getPawn(2, 1).equalsPawn("W") && !common.checkWhiteCanBeCaptured(2, 1, s)) {
				value += 200;
			}
		}
		
		if(!s.getPawn(1, 2).equalsPawn("B") && s.getPawn(2, 1).equalsPawn("B")) {
			if(s.getPawn(1, 2).equalsPawn("W") && !common.checkWhiteCanBeCaptured(1, 2, s)) {
				value += 200;
			}
		}
		
		if(s.getPawn(1, 2).equalsPawn("B") && s.getPawn(2, 1).equalsPawn("B")) {
			value -= 400;
		}
		
		//top right
		if(s.getPawn(1, 6).equalsPawn("B") && !s.getPawn(2, 7).equalsPawn("B")) {
			if(s.getPawn(2, 7).equalsPawn("W")) {
				value += 200;
			}
		}
		
		if(!s.getPawn(1, 6).equalsPawn("B") && s.getPawn(2, 7).equalsPawn("B")) {
			if(s.getPawn(1, 6).equalsPawn("W")) {
				value += 200;
			}
		}
		
		if(s.getPawn(1, 6).equalsPawn("B") && s.getPawn(2, 7).equalsPawn("B")) {
			value -= 400;
		}
		
		//bottom left
		if(s.getPawn(6, 1).equalsPawn("B") && !s.getPawn(7, 2).equalsPawn("B")) {
			if(s.getPawn(7, 2).equalsPawn("W")) {
				value += 200;
			}
		}
		
		if(!s.getPawn(6, 1).equalsPawn("B") && s.getPawn(7, 2).equalsPawn("B")) {
			if(s.getPawn(6, 1).equalsPawn("W")) {
				value += 200;
			}
		}
		
		if(s.getPawn(6, 1).equalsPawn("B") && s.getPawn(7, 2).equalsPawn("B")) {
			value -= 400;
		}
		
		//bottom right
		if(s.getPawn(6, 7).equalsPawn("B") && !s.getPawn(7, 6).equalsPawn("B")) {
			if(s.getPawn(7, 6).equalsPawn("W")) {
				value += 200;
			}
		}
		
		if(!s.getPawn(6, 7).equalsPawn("B") && s.getPawn(7, 6).equalsPawn("B")) {
			if(s.getPawn(6, 7).equalsPawn("W")) {
				value += 200;
			}
		}
		
		if(s.getPawn(6, 7).equalsPawn("B") && s.getPawn(7, 6).equalsPawn("B")) {
			value -= 400;
		}
							
		return value;	
	}
	
		
	/**
	 * Controlla se il re, che si trova sul trono, può fare un passo o due verso l'alto (o verso il basso, o verso sinistra, o verso destra)
	 * 
	 * @param s StateTablut ovvero lo stato da valutare
	 * @return true se il re può uscire dal trono, false in caso contrario
	 */
	private boolean checkKingCanComeOutFromThrone(StateTablut s) {
		
		if(common.canMoveWhite(4, 4, "T", s) != -1) {
			
			if(common.canMoveWhite(4, 4, "T", s) == 1) {
				if(!common.kingCanBeCaptured(3, 4, s)) {
					return true;
				}
			} else if(common.canMoveWhite(4, 4, "T", s) == 2) {
				if(!common.kingCanBeCaptured(2, 4, s)) {
					return true;
				}
			}
			
			if(common.canMoveWhite(4, 4, "B", s) == 1) {
				if(!common.kingCanBeCaptured(5, 4, s)) {
					return true;
				}
			} else if(common.canMoveWhite(4, 4, "B", s) == 2) {
				if(!common.kingCanBeCaptured(6, 4, s)) {
					return true;
				}
			}
			
			if(common.canMoveWhite(4, 4, "L", s) == 1) {
				if(!common.kingCanBeCaptured(4, 3, s)) {
					return true;
				}
			} else if(common.canMoveWhite(4, 4, "L", s) == 2) {
				if(!common.kingCanBeCaptured(4, 2, s)) {
					return true;
				}
			}
			
			if(common.canMoveWhite(4, 4, "R", s) == 1) {
				if(!common.kingCanBeCaptured(4, 5, s)) {
					return true;
				}
			} else if(common.canMoveWhite(4, 4, "R", s) == 2) {
				if(!common.kingCanBeCaptured(4, 6, s)) {
					return true;
				}
			}
		}
		return false;
		
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
	
	//GET BETTER MOVE DI PIRO
	@Override
	public synchronized Action getBetterMove(StateTablut s) {


		long t1 = System.currentTimeMillis();
		long t3 = 0;

		try {
			Nodo node = new Nodo(s);
			TreeGenerator3 treeGenerator2 = new TreeGenerator3(node, this.citadels, this);
			Thread t = new Thread(treeGenerator2);
			t.start();
			Thread.sleep(TIMETOSTOPTREEGENERATOR);
			t.interrupt();
			t3 = System.currentTimeMillis();
			System.out.println("Tempo trascorso sviluppo albero: "+(t3-t1)+" millisecondi");
			
			for(int x=0; x<albero.size(); x++)
			{
				System.out.println("Nodi espansi livello " + x +": "+albero.get(x).getNodi().size());
			}
			System.out.println("Livello 1");
			for(int x=0; x<albero.get(1).getNodi().size(); x++)
			{
				System.out.println("Nodo: " + x +" ha valore "+albero.get(1).getNodi().get(x).getValue());
			}
			/*System.out.println("Livello 2");
			for(int x=0; x<albero.get(2).getNodi().size(); x++)
			{
				System.out.println("Nodo: " + x +" ha valore "+albero.get(2).getNodi().get(x).getValue());
			}
			System.out.println("Livello 3");
			for(int x=0; x<100; x++)
			{
				System.out.println("Nodo: " + x +" ha valore "+albero.get(3).getNodi().get(x).getValue());
			}
			System.out.println("Livello 4");
			for(int x=0; x<100; x++)
			{
				System.out.println("Nodo: " + x +" ha valore "+albero.get(4).getNodi().get(x).getValue());
				if(albero.get(4).getNodi().get(x).getValue()==-92)
				{
					System.out.println(albero.get(4).getNodi().get(x).getStato());
				}
			}
			System.out.println(albero.get(4).getNodi().get(0).getStato());*/
			System.out.println("Valore root: "+albero.get(0).getNodi().get(0).getValue());
			albero.clear();
			System.gc();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		long t2 = System.currentTimeMillis();
		System.out.println("Tempo trascorso sviluppo euristica: "+(t2-t3)+" millisecondi");
		System.out.println("Mossa: "+a.toString());
		System.out.println("");
		return a;
	}
	
	private class TreeGenerator3 implements Runnable {
		private Nodo nodoAttuale;
		private Simulator simulatore;
		private List<String> citadels;
		private IntelligenzaBianca ia;
		private boolean taglioLivello1;
		private boolean taglioLivello2;
		private boolean taglioLivello3;
		private boolean taglioLivello4;
		private boolean taglioLivello5;
		private boolean taglioLivello6;
		private Livello liv0;
		private Livello liv1;
		private Livello liv2;
		private Livello liv3;
		private Livello liv4;
		private Livello liv5;
		private Livello liv6;
		private Nodo nodoLiv0;
		private Nodo nodoLiv1;
		private Nodo nodoLiv5;
		private Nodo nodoLiv2;
		private Nodo nodoLiv3;
		private Nodo nodoLiv4;
		private Nodo nodoLiv6;
		
		public TreeGenerator3(Nodo n, List<String> cit, IntelligenzaBianca i) {
			this.nodoAttuale = n;
			this.ia = i;
			this.citadels = cit;
			this.simulatore = new Simulator();
		}
		
		public void run() {
			
			long t1 = System.currentTimeMillis();
			System.out.println("Attivazione thread treeGenerator");
			taglioLivello1 = false;
			taglioLivello2 = false;
			taglioLivello3 = false;
			taglioLivello4 = false;
			taglioLivello5 = false;
			taglioLivello6 = false;

			//aggiungo il livello 0
			this.liv0 = new Livello();
			liv0.add(this.nodoAttuale);
			albero.add(liv0);
			this.liv1 = new Livello();
			albero.add(liv1);
			this.liv2 = new Livello();
			albero.add(liv2);
			this.liv3 = new Livello();
			albero.add(liv3);
			this.liv4 = new Livello();
			albero.add(liv4);
			this.liv5 = new Livello();
			albero.add(liv5);
			this.liv6 = new Livello();
			albero.add(liv6);
			this.nodoLiv0 = liv0.getNodi().get(0);
			liv0.getNodi().get(0).setValue(Float.NaN);
			//calcolo TUTTE le mosse possibili al livello 1 (le mosse effettive che posso fare)
			//facendogli poi la sort per avere l'ordine giusto e per sapere se ho già vinto
			try 
			{
				this.liv1.add(this.simulatore.mossePossibiliComplete(this.nodoAttuale));
				this.sortLiv1(liv1);
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
			if(!this.liv1.getNodi().get(0).getTurn().equalsTurn("WW"))
			{
				for(int i=0; i<this.liv1.getNodi().size() && !Thread.currentThread().isInterrupted(); i++)
				{
					this.nodoLiv1 = this.liv1.getNodi().get(i);
					this.nodoLiv1.setValue(Float.NaN);
					this.getValueOfNodeLiv1();
					if(Float.isNaN(this.nodoLiv0.getValue()) || this.nodoLiv0.getValue()<this.nodoLiv1.getValue())
					{
						this.nodoLiv0.setValue(this.nodoLiv1.getValue());
						a = this.nodoLiv1.getAzione();
					}
				}
			}
			
			
			System.out.println("Terminazione thread treeGenerator");
			System.out.println("Tempo utilizzato: "+(System.currentTimeMillis()-t1));
			//this.ia.notify();
		}
		
		private void getValueOfNodeLiv1() {
			try {
				if(this.nodoLiv1.getValue() <= this.nodoLiv0.getValue()) {
					return;
				}
				List<Nodo> daAggiungere = this.simulatore.mossePossibiliComplete(this.nodoLiv1);
				this.sortLivGenC(daAggiungere);
				this.liv2.add(daAggiungere);
				
				if(daAggiungere.get(0).getTurn().equalsTurn("BW"))
				{
					this.nodoLiv1.setValue(-10000);
				}
				if(daAggiungere.get(0).getTurn().equalsTurn("WW"))
				{
					this.nodoLiv1.setValue(10000);
				}
				if(!daAggiungere.get(0).getTurn().equalsTurn("BW") && !daAggiungere.get(0).getTurn().equalsTurn("WW"))
				{
					for(int i=0; i<daAggiungere.size() && !taglioLivello2 && !Thread.currentThread().isInterrupted(); i++)
					{
						this.nodoLiv2 = daAggiungere.get(i);
						this.nodoLiv2.setValue(Float.NaN);
						this.getValueOfNodeLiv2();
						if(Float.isNaN(this.nodoLiv1.getValue()) || this.nodoLiv2.getValue()<this.nodoLiv1.getValue())
						{
							this.nodoLiv1.setValue(this.nodoLiv2.getValue());
						}
						if(this.nodoLiv1.getValue() <= this.nodoLiv0.getValue()) {
							taglioLivello2 = true;
						}
					}
				}
				taglioLivello2 = false;			
				
			} catch (Exception e) {
				e.printStackTrace();
			}			
		}
		
		private void getValueOfNodeLiv2() {
			try {
				if(this.nodoLiv2.getValue() >= this.nodoLiv1.getValue()) {
					return;
				}
				List<Nodo> daAggiungere = this.simulatore.mossePossibiliComplete(this.nodoLiv2);
				this.sortLivGenD(daAggiungere);
				this.liv3.add(daAggiungere);
				if(daAggiungere.get(0).getTurn().equalsTurn("BW"))
				{
					this.nodoLiv2.setValue(-10000);
				}
				if(daAggiungere.get(0).getTurn().equalsTurn("WW"))
				{
					this.nodoLiv2.setValue(10000);
				}
				if(!daAggiungere.get(0).getTurn().equalsTurn("BW") && !daAggiungere.get(0).getTurn().equalsTurn("WW"))
				{
					for(int i=0; i<daAggiungere.size() && !taglioLivello3 && !Thread.currentThread().isInterrupted(); i++)
					{
						this.nodoLiv3 = daAggiungere.get(i);
						this.nodoLiv3.setValue(Float.NaN);
						this.getValueOfNodeLiv3();
						if(Float.isNaN(this.nodoLiv2.getValue()) || this.nodoLiv2.getValue()<this.nodoLiv3.getValue())
						{
							this.nodoLiv2.setValue(this.nodoLiv3.getValue());
						}
						if(this.nodoLiv2.getValue() >= this.nodoLiv1.getValue()) {
							taglioLivello3 = true;
						}
					}
				}
				taglioLivello3 = false;
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}			
		}

		private void getValueOfNodeLiv3() {
			try {
				if(this.nodoLiv3.getValue() <= this.nodoLiv2.getValue()) {
					return;
				}
				List<Nodo> daAggiungere = this.simulatore.mossePossibiliComplete(this.nodoLiv3);
				this.sortLivGenC(daAggiungere);
				this.liv4.add(daAggiungere);
				if(daAggiungere.get(0).getTurn().equalsTurn("BW"))
				{
					this.nodoLiv3.setValue(-10000);
				}
				if(daAggiungere.get(0).getTurn().equalsTurn("WW"))
				{
					this.nodoLiv3.setValue(10000);
				}
				if(!daAggiungere.get(0).getTurn().equalsTurn("BW") && !daAggiungere.get(0).getTurn().equalsTurn("WW"))
				{
					for(int i=0; i<daAggiungere.size() && !taglioLivello4 && !Thread.currentThread().isInterrupted(); i++)
					{
						this.nodoLiv4 = daAggiungere.get(i);
						this.nodoLiv4.setValue(Float.NaN);
						if(albero.get(1).getNodi().size()<30)
						{
							this.getValueOfNodeLiv4();
						}
						else
						{
							this.getValueOfNodeLiv4Bis();
						}
						if(Float.isNaN(this.nodoLiv3.getValue()) || this.nodoLiv4.getValue()<this.nodoLiv3.getValue())
						{
							this.nodoLiv3.setValue(this.nodoLiv4.getValue());
						}
						if(this.nodoLiv3.getValue() <= this.nodoLiv2.getValue() || this.nodoLiv3.getValue() <= this.nodoLiv0.getValue()) {
							taglioLivello4 = true;
						}					
					}
				}
				taglioLivello4 = false;				
			} catch (Exception e) {
				e.printStackTrace();
			}			
		}
		
		private void getValueOfNodeLiv4Bis(){
			this.nodoLiv4.setValue(this.ia.getHeuristicValue(this.nodoLiv4.getStato()));
		}
		
		private void getValueOfNodeLiv4() {
			try {
				if(this.nodoLiv4.getValue() >= this.nodoLiv3.getValue()) {
					return;
				}
				List<Nodo> daAggiungere = this.simulatore.mossePossibiliComplete(this.nodoLiv4);
				this.sortLivGenD(daAggiungere);
				this.liv5.add(daAggiungere);
				if(daAggiungere.get(0).getTurn().equalsTurn("BW"))
				{
					this.nodoLiv4.setValue(-10000);
				}
				if(daAggiungere.get(0).getTurn().equalsTurn("WW"))
				{
					this.nodoLiv4.setValue(10000);
				}
				if(!daAggiungere.get(0).getTurn().equalsTurn("BW") && !daAggiungere.get(0).getTurn().equalsTurn("WW"))
				{
					for(int i=0; i<daAggiungere.size() && !taglioLivello5 && !Thread.currentThread().isInterrupted(); i++)
					{
						this.nodoLiv5 = daAggiungere.get(i);
						this.nodoLiv5.setValue(Float.NaN);
						this.getValueOfNodeLiv5();
						if(Float.isNaN(this.nodoLiv4.getValue()) || this.nodoLiv4.getValue()<this.nodoLiv5.getValue())
						{
							this.nodoLiv4.setValue(this.nodoLiv5.getValue());
						}
						if(this.nodoLiv4.getValue() >= this.nodoLiv3.getValue() || this.nodoLiv4.getValue() >= this.nodoLiv1.getValue()) {
							taglioLivello5 = true;
						}
					}
				}
				
				taglioLivello5 = false;
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}			
		}
		
		private void getValueOfNodeLiv5() {
			this.nodoLiv5.setValue(this.ia.getHeuristicValue(this.nodoLiv5.getStato()));
		}
		
		private void getValueOfNodeLiv6() {
			this.nodoLiv6.setValue(this.ia.getHeuristicValue(this.nodoLiv6.getStato()));
		}
		
		//metodo di sort del livello 1
		private void sortLiv1(Livello liv1)
		{
			for(int x=0; x<liv1.getNodi().size(); x++)
			{
				Nodo nodo = liv1.getNodi().get(x);
				if(nodo.getStato().getTurn().equalsTurn("WW"))
				{
					nodo.setValue(100000);
				}
				if(nodo.getStato().getTurn().equalsTurn("BW"))
				{
					nodo.setValue(-100000);
				}
				if(this.ia.checkDraw(nodo.getStato()))
				{
					nodo.setValue(-5000);
					nodo.getStato().setTurn(Turn.DRAW);
				}
				if(nodo.getStato().getTurn().equalsTurn("W") || nodo.getStato().getTurn().equalsTurn("B"))
				{
					nodo.setValue(this.ia.getHeuristicValue(nodo.getStato()));
				}
				Collections.sort(liv1.getNodi(), new Comparator<Nodo>() {
					@Override
					public int compare(Nodo n2, Nodo n1)
					{
						return  (int) (n1.getValue()-n2.getValue());
					}
				});
			}
			//imposto la mossa migliore
			a=albero.get(1).getNodi().get(0).getAzione();
			for(Nodo nodo : albero.get(1).getNodi())
			{
				if(!nodo.getTurn().equalsTurn("BW") && !nodo.getTurn().equalsTurn("WW") && !nodo.getTurn().equalsTurn("D"))
				{
					nodo.setValue(Float.NaN);
				}
			}
			albero.get(0).getNodi().get(0).setValue(albero.get(1).getNodi().get(0).getValue());
		}
				
		//metodo per il riordinamento livello 1, da cambiare
		private float setSimpleHeuristic(StateTablut s) {
			return this.ia.getHeuristicValue(s);		
		}
	
		//metodo per ordinare in ordine crescente (con simple heuristic) una lista di nodi
		private void sortLivGenC(List<Nodo> lista)
		{
			for(int x=0; x<lista.size(); x++)
			{
				Nodo nodo = lista.get(x);
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
				}
				if(nodo.getStato().getTurn().equalsTurn("W") || nodo.getStato().getTurn().equalsTurn("B"))
				{
					nodo.setValue(this.setSimpleHeuristic(nodo.getStato()));
				}
				
				Collections.sort(lista, new Comparator<Nodo>() {
					@Override
			    	public int compare(Nodo n2, Nodo n1)
					{
						return  (int) (n2.getValue()-n1.getValue());
					}
			    });
			}
			for(Nodo nodo : lista)
			{
				if(!nodo.getTurn().equalsTurn("BW") && !nodo.getTurn().equalsTurn("WW") && !nodo.getTurn().equalsTurn("D"))
				{
					nodo.setValue(Float.NaN);
				}
			}
		}
		
		//metodo per ordinare in ordine decrescente (con simple heuristic) una lista di nodi
		private void sortLivGenD(List<Nodo> lista)
		{
			for(int x=0; x<lista.size(); x++)
			{
				Nodo nodo = lista.get(x);
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
				}
				if(nodo.getStato().getTurn().equalsTurn("W") || nodo.getStato().getTurn().equalsTurn("B"))
				{
					nodo.setValue(this.setSimpleHeuristic(nodo.getStato()));
				}
				
				Collections.sort(lista, new Comparator<Nodo>() {
					@Override
			    	public int compare(Nodo n2, Nodo n1)
					{
						return  (int) (n1.getValue()-n2.getValue());
					}
			    });
			}
			for(Nodo nodo : lista)
			{
				if(!nodo.getTurn().equalsTurn("BW") && !nodo.getTurn().equalsTurn("WW") && !nodo.getTurn().equalsTurn("D"))
				{
					nodo.setValue(albero.get(0).getNodi().get(0).getValue());
				}
			}
		}

	}
	
	
	
}
