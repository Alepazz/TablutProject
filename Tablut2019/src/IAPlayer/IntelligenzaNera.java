package IAPlayer;

import it.unibo.ai.didattica.competition.tablut.domain.Action;
import it.unibo.ai.didattica.competition.tablut.domain.StateTablut;

import java.util.ArrayList;
import java.util.List;

public class IntelligenzaNera implements IA {
	
	private static Action a = null;
	private static List<Livello> albero;
	//private List<Nodo> nodiEsistenti;
	private final int MAX_VALUE = 10000;
	private final int MIN_VALUE = - MAX_VALUE;
	private final int VALUE_BLACK_PAWN = 100;
	private final int VALUE_WHITE_PAWN = 2 * VALUE_BLACK_PAWN;
	private Simulator simulatore;
	private CommonHeuristicFunction common;
	private String[] posNeri;
	
	public IntelligenzaNera() {
		albero = new ArrayList<Livello>();
		this.simulatore = new Simulator();
		//this.nodiEsistenti = new ArrayList<Nodo>();
		this.common= new CommonHeuristicFunction();
		this.posNeri = new String[] {"12","21","61","72","67","76","16","27"};

		
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
		
		//numero pedine
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
					nNeri++; //volendo il numero delle pedine si pu� avere cercando lunghezza della lista
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
			//itero su tutti i neri per vedere quali sono le mosse migliori per ogni pedina
		else {
			
			
			//controllo che possa andare in una delle posizioni buone
			if(common.checkBlackCanArriveAdjacentInBottomPosition(1, 1, s) || common.checkBlackCanArriveAdjacentInRightPosition(1, 1, s))
				value =+ 1000;
			if(common.checkBlackCanArriveAdjacentInTopPosition(7, 1, s) || common.checkBlackCanArriveAdjacentInRightPosition(7, 1, s))
				value =+ 1000;
			
			if(common.checkBlackCanArriveAdjacentInTopPosition(7, 7, s) || common.checkBlackCanArriveAdjacentInLeftPosition(7, 7, s))
				value =+ 1000;
			if(common.checkBlackCanArriveAdjacentInLeftPosition(1, 7, s) || common.checkBlackCanArriveAdjacentInBottomPosition(1, 7, s))
				value =+ 1000;
			
			for(int i=0; i<indexNeri; i++ ) {
				
				int posizione= Integer.parseInt(neri[i]);
				//le unit� sono le colonne mentre le decine sono le righe
				int riga = posizione/10;
				int colonna= posizione % 10;
			
				
				//controllo che ci siano delle pedine in diagonale
				if(common.checkNeighbourBottomLeft(riga, colonna, s).equals("B"))
					value =+ VALUE_BLACK_PAWN*3;
				if(	common.checkNeighbourBottomRight(riga, colonna, s).equals("B"))
					value =+ VALUE_BLACK_PAWN*3;
				if(common.checkNeighbourTopLeft(riga, colonna, s).equals("B"))
					value =+ VALUE_BLACK_PAWN*3;
				if(	common.checkNeighbourTopRight(riga, colonna, s).equals("B"))
					value =+ VALUE_BLACK_PAWN*3;
				
				//controllo pedine vicine sugli assi (� preferibile che siano in diagonale)
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
				if(common.checkNeighbourBottom(riga, colonna, s).equals("W") )
					if( common.checkWhiteCanBeCaptured(riga+1, colonna, s))
						value =+ this.VALUE_BLACK_PAWN*10;
				if(common.checkNeighbourTop(riga, colonna, s).equals("W") )
					if( common.checkWhiteCanBeCaptured(riga-1, colonna, s))
						value =+ this.VALUE_BLACK_PAWN*10;
				if(common.checkNeighbourLeft(riga, colonna, s).equals("W") )
					if( common.checkWhiteCanBeCaptured(riga, colonna-1, s))
						value =+ this.VALUE_BLACK_PAWN*10;
				if(common.checkNeighbourRight(riga, colonna, s).equals("W") )
					if( common.checkWhiteCanBeCaptured(riga, colonna+1, s))
						value =+ this.VALUE_BLACK_PAWN*10;
				
				//controllo se possono arrivare pedine bianche da dx-sx e up-down
				if(common.checkWhiteCanArriveFromBottom(riga, colonna, s) && common.checkWhiteCanArriveFromTop(riga, colonna, s))
					value =- this.VALUE_WHITE_PAWN;
				if(common.checkWhiteCanArriveFromLeft(riga, colonna, s) && common.checkWhiteCanArriveFromRight(riga, colonna, s))
					value =- this.VALUE_WHITE_PAWN;
				
				//controllo se mi viene mangiato il nero
				//DA MODIFICARE NELLA COMMON
				if(common.checkBlackCanBeCaptured(riga, colonna, s))
					value =- this.VALUE_BLACK_PAWN*7;

				
				}
			}
		System.out.println("valore"+value);
		return value;
	}

	
	//valuta gli ultimi rami dell'albero
	//da implementare i tagli ecc...
	private class HeuristicValuator implements Runnable {
		private IntelligenzaNera ia;
		
		public HeuristicValuator(IntelligenzaNera ia){
			this.ia = ia;
		}
		
		public void run() {
			int x =0;
			//ciclo sull'ultimo livello
			for(int i = 0; i<albero.get(albero.size()-1).getNodi().size() && !Thread.interrupted(); i++)
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
			for(int i = 0; i<albero.get(albero.size()-2).getNodi().size() && !Thread.interrupted(); i++)
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
			for(int i = 0; i<albero.get(2).getNodi().size() && !Thread.interrupted(); i++)
			{
				Nodo n = albero.get(2).getNodi().get(i);
				if(Float.isNaN(n.getPadre().getValue()) || n.getValue()>n.getPadre().getValue())
				{
					n.getPadre().setValue(n.getValue());
				}
			}
			for(int i = 0; i<albero.get(1).getNodi().size() && !Thread.interrupted(); i++)
			{
				Nodo n = albero.get(1).getNodi().get(i);
				if(Float.isNaN(n.getPadre().getValue()) || n.getValue()<n.getPadre().getValue())
				{
					n.getPadre().setValue(n.getValue());
					a=n.getAzione();
				}
			}
			System.out.println(x + " calcoli fatti");
			System.out.println(a.toString());
			/*for(Livello l: albero)
			{
				l.getNodi().clear();
			}*/
			albero.clear();
			System.out.println("Albero ripulito" + albero.size());
		}
		
	}
			
	//thread che crea l'albero di gioco
	private class TreeGenerator implements Runnable {
		private Nodo nodoAttuale;
		private Simulator simulatore;
		private boolean isRunning;

		public TreeGenerator(Nodo n, Simulator s) {
			this.nodoAttuale = n;
			this.simulatore = s;
			this.isRunning=true;
		}

		public void stopThread()
		{
			this.isRunning=false;
		}

		public void run() {
			try {
				Livello liv = new Livello();
				liv.add(this.nodoAttuale);
				albero.add(liv);
				Livello livEspanso = null;
				for(int livelloDaEspandere=0; isRunning ;livelloDaEspandere++)
				{
					livEspanso = new Livello();
					albero.add(livEspanso);
					for(int x=0; x<albero.get(livelloDaEspandere).getNodi().size() && isRunning; x++)
					{
						Nodo n = albero.get(livelloDaEspandere).getNodi().get(x);
						//long x1 = System.currentTimeMillis();
						List<Nodo> mosse = this.simulatore.mossePossibiliComplete(n);
						//long x2 = System.currentTimeMillis();
						//System.out.println("Tempo utilizzato: " + (x2-x1) + " Numero mosse trovate: "+ mosse.size());
						for(int y=0; y<mosse.size() && isRunning; y++)
						{
							Nodo nodo = mosse.get(y);
							livEspanso.add(nodo);
						}
						
					}
				}
				System.out.println("Thread treeGenerator interrotto");
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}

	}
			
	@SuppressWarnings("static-access")
	@Override
	public synchronized Action getBetterMove(StateTablut s) {

		long t1 = System.currentTimeMillis();
		long t3 = 0;

		try {
			Nodo node = new Nodo(s);
			TreeGenerator treeGenerator = new TreeGenerator(node, this.simulatore);
			Thread t = new Thread(treeGenerator);
			t.start();
			this.wait(10000);
			System.out.println("Lancio l'interruzione");
			treeGenerator.stopThread();
			//t.stop();
			
			for(int x=0; x<albero.size(); x++)
			{
				System.out.println("Nodi espansi livello " + x +": "+albero.get(x).getNodi().size());
			}
			t3 = System.currentTimeMillis();
			System.out.println("Tempo trascorso: "+(t3-t1)+" millisecondi");
			
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
		System.out.println("Tempo trascorso: "+(t2-t3)+" millisecondi");
		System.out.println("");
		System.out.println("");
		return a;
	}
}
