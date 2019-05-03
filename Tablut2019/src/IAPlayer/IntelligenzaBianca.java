package IAPlayer;

import it.unibo.ai.didattica.competition.tablut.domain.Action;
import it.unibo.ai.didattica.competition.tablut.domain.StateTablut;

import java.util.ArrayList;
import java.util.List;

public class IntelligenzaBianca implements IA {

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
		
		public HeuristicValuator(IntelligenzaBianca ia){
			this.ia = ia;
		}
		
		public void run() {
			int x =0;
			//ciclo sull'ultimo livello
			for(Nodo n : albero.get(albero.size()-1).getNodi())
			{
				if(!Float.isNaN(n.getPadre().getValue()) 
						&& !Float.isNaN(n.getPadre().getPadre().getValue()) 
						&& n.getPadre().getPadre().getValue()>=n.getPadre().getValue())
				{}
				else
				{
					//non so se sia il giusto metodo, al massimo da cambiare con l'altro
					float heu = ia.getHeuristicValue(n.getStato());
					x++;
					//siccome evolviamo per 5 livelli saranno stati dell'avversario 
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
			//ciclo il penultimo livello
			for(Nodo n : albero.get(albero.size()-2).getNodi())
			{
				if(!Float.isNaN(n.getPadre().getValue()) 
						&& !Float.isNaN(n.getPadre().getPadre().getValue()) 
						&& n.getPadre().getPadre().getValue()<=n.getPadre().getValue())
				{}
				else
				{
					if(Float.isNaN(n.getValue()))
					{
						//non so se sia il giusto metodo, al massimo da cambiare con l'altro
						float heu = ia.getHeuristicValue(n.getStato());
						x++;
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
							
			}
			
			/*for(Nodo n : albero.get(3).getNodi())
			{
				if(Float.isNaN(n.getPadre().getValue()) || n.getValue()>n.getPadre().getValue())
				{
					n.getPadre().setValue(n.getValue());
				}
			}*/
			for(Nodo n : albero.get(2).getNodi())
			{
				if(Float.isNaN(n.getPadre().getValue()) || n.getValue()<n.getPadre().getValue())
				{
					n.getPadre().setValue(n.getValue());
				}
			}
			for(Nodo n : albero.get(1).getNodi())
			{
				if(Float.isNaN(n.getPadre().getValue()) || n.getValue()>n.getPadre().getValue())
				{
					n.getPadre().setValue(n.getValue());
					a=n.getAzione();
				}
			}
			System.out.println(x + " calcoli fatti");
			for(Livello l: albero)
			{
				l.getNodi().clear();
			}
			albero.clear();
		}
		
	}
	
	/*
	 * thread che crea l'albero di gioco
	 */
	private class TreeGenerator implements Runnable {
		private Nodo nodoAttuale;
		private Simulator simulatore;

		public TreeGenerator(Nodo n, Simulator s) {
			this.nodoAttuale = n;
			this.simulatore = s;
		}


		public void run() {
			try {
				Livello liv = new Livello();
				liv.add(this.nodoAttuale);
				albero.add(liv);
				Livello livEspanso = null;
				for(int livelloDaEspandere=0; !Thread.interrupted() ;livelloDaEspandere++)
				{
					livEspanso = new Livello();
					albero.add(livEspanso);
					for(Nodo n : albero.get(livelloDaEspandere).getNodi())
					{
						livEspanso.add(this.simulatore.mossePossibiliComplete(n));
					}
				}
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}

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
	
	@SuppressWarnings("static-access")
	@Override
	public synchronized Action getBetterMove(StateTablut s) {

		long t1 = System.currentTimeMillis();

		try {
			Nodo node = new Nodo(s);
			TreeGenerator treeGenerator = new TreeGenerator(node, this.simulatore);
			Thread t = new Thread(treeGenerator);
			t.start();
			this.wait(30000);
			System.out.println("Lancio l'interruzione");
			t.interrupt();
			t.stop();
			
			for(int x=0; x<albero.size(); x++)
			{
				System.out.println("Nodi espansi livello " + x +": "+albero.get(x).getNodi().size());
			}
			
			HeuristicValuator heuristicValuator = new HeuristicValuator(this);
			t = new Thread(heuristicValuator);
			t.start();
			this.wait(20000);
			System.out.println("Lancio l'interruzione");
			t.interrupt();
			t.stop();
			

			
			//System.out.println("Livello 3 espanso");

			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long t2 = System.currentTimeMillis();
		System.out.println("Tempo trascorso: "+(t2-t1)+" millisecondi");
		
		return a;
	}
	
}
