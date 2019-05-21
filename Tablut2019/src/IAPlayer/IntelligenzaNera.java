package IAPlayer;

import it.unibo.ai.didattica.competition.tablut.domain.Action;
import it.unibo.ai.didattica.competition.tablut.domain.State;
import it.unibo.ai.didattica.competition.tablut.domain.StateTablut;
import it.unibo.ai.didattica.competition.tablut.domain.State.Turn;
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


public class IntelligenzaNera implements IA {
	
	private int TIMETOSTOPTREEGENERATOR;
	private List<String> citadels;
	private List<StateTablut> listState; 
	private static Action a = null;
	private static List<Livello> albero;
	//private List<Nodo> nodiEsistenti;
	private final int MAX_VALUE = 10000;
	private final int MIN_VALUE = - MAX_VALUE;
	private final int VALUE_BLACK_PAWN = 100;
	private final int VALUE_WHITE_PAWN = 3 * VALUE_BLACK_PAWN;
	private List<String> perfectPos;
	private int numeroMosse = 0;
	//private Simulator simulatore;
	private CommonHeuristicFunction common;
	
	public IntelligenzaNera(int secondi) {
		albero = new ArrayList<Livello>();
		//this.simulatore = new Simulator();
		this.common= new CommonHeuristicFunction();
		this.citadels = this.common.getCitadels();
		this.perfectPos= new ArrayList<String>();
		this.listState = new ArrayList<StateTablut>();
		this.TIMETOSTOPTREEGENERATOR = secondi * 1000;
		/*this.perfectPos.add("13");
		this.perfectPos.add("22");
		this.perfectPos.add("31");
		this.perfectPos.add("51");
		this.perfectPos.add("62");
		this.perfectPos.add("73");
		this.perfectPos.add("75");
		this.perfectPos.add("66");
		this.perfectPos.add("57");
		this.perfectPos.add("37");
		this.perfectPos.add("26");
		this.perfectPos.add("15");*/
		
		/*altra possibile diagonale buona
		 * basta solo scommentarla
		 */
		 this.perfectPos.add("12");
		 this.perfectPos.add("21");
		 this.perfectPos.add("16");
		 this.perfectPos.add("27");
		 this.perfectPos.add("61");
		 this.perfectPos.add("72");
		 this.perfectPos.add("67");
		 this.perfectPos.add("76");

		
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
		List<String> neri= new ArrayList<String>();
		List<String> bianchi = new ArrayList<String>();;

		
		for(int i=0; i<9; i++)
		{
			for(int j=0; j<9; j++)
			{
				if(s.getBoard()[i][j].equalsPawn("B"))
				{
					nNeri++; //volendo il numero delle pedine si pu� avere cercando lunghezza della lista
					//aggiungo la posizione ij di ogni nero
					neri.add(""+i+j);
				}
				if(s.getBoard()[i][j].equalsPawn("W"))
				{
					nBianchi++;
					bianchi.add(""+i+j);
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
					//nNeri++;
					value+= VALUE_BLACK_PAWN;
					if(common.checkBlackCanBeCaptured(i, j, s))
					{
						value-= VALUE_BLACK_PAWN*3/4;
					}
				}
				if(s.getBoard()[i][j].equalsPawn("W"))
				{
					//nBianchi++;
					value-= VALUE_WHITE_PAWN;
					if(common.checkWhiteCanBeCaptured(i, j, s))
					{
						value+= VALUE_WHITE_PAWN/2;
					}
				}
			}
		}
		
	/*	//Controllo se il re viene mangiato in qualsiasi posizione sia
		if(this.common.kingCanBeCaptured(rigaRe, colonnaRe, s))
		{
			
			return this.MAX_VALUE-1;
			
		}*/
		
		//controllo vie di fuga re
		int vieDiFuga=this.common.checkVieDiFugaRe(rigaRe, colonnaRe, s);
				
		//controllo se nella mossa del nero mi mangia il re
		if((vieDiFuga==1 && s.getTurn().equalsTurn("W")) || (vieDiFuga > 1 && !common.kingCanBeCaptured(rigaRe, colonnaRe, s)))
		{
			return this.MIN_VALUE+1;
		}
		if(vieDiFuga==1 && s.getTurn().equalsTurn("B"))
		{
			if(common.blackCannotBlockEscape(rigaRe, colonnaRe, s) && !common.kingCanBeCaptured(rigaRe, colonnaRe, s))
			{
				return this.MIN_VALUE+1;
			}
			
			if(common.kingCanBeCaptured(rigaRe, colonnaRe, s)) {
				return this.MAX_VALUE-1;
			}
		}
		
		if(s.getTurn().equalsTurn("B") && common.kingCanBeCaptured(rigaRe, colonnaRe, s)) {
			return this.MAX_VALUE-1;
		}
		
		if(vieDiFuga == 0) {
			int riga = this.checkOneBlackUp(rigaRe, colonnaRe, s);
			if(riga != -1) {
				if(common.checkBlackCanBeCaptured(riga, colonnaRe, s)) {
					value -= 200;
				}
			}
			
			riga = this.checkOneBlackDown(rigaRe, colonnaRe, s);
			if(riga != -1) {
				if(common.checkBlackCanBeCaptured(riga, colonnaRe, s)) {
					value -= 200;
				}
			}
			
			int colonna = this.checkOneBlackLeft(rigaRe, colonnaRe, s);
			if(colonna != -1) {
				if(common.checkBlackCanBeCaptured(rigaRe, colonna, s)) {
					value -= 200;
				}
			}
			
			colonna = this.checkOneBlackRight(rigaRe, colonnaRe, s);
			if(colonna != -1) {
				if(common.checkBlackCanBeCaptured(rigaRe, colonna, s)) {
					value -= 200;
				}
			}
		}
		
		/*
		 * Funzione che controlla se, eseguita una mossa del re in orizzontale, esso ha liberato un'intera colonna (2 oppure 6), in cui vincere (al 100%) il turno successivo
		 */
		if (this.common.checkFreeColComingFromLeft(rigaRe, colonnaRe, s) || this.common.checkFreeColComingFromRight(rigaRe, colonnaRe, s)) {
			
			return this.MIN_VALUE+1;
		}
		
		/*
		 * Funzione che controlla se, eseguita una mossa del re in verticale, esso ha liberato un'intera riga (2 oppure 6), in cui vincere (al 100%) il turno successivo
		 */
		if (this.common.checkFreeRowComingFromTop(rigaRe, colonnaRe, s) || this.common.checkFreeRowComingFromBottom(rigaRe, colonnaRe, s)){
			
			return this.MIN_VALUE+1;
		}
						
		//dalla funzione pi� importante alla meno importante
		value +=this.getValueOfReAccerchiato(neri, rigaRe, colonnaRe, s);
		if(value >= this.MAX_VALUE/1.5)
			return value;
		
		if(value >= this.MAX_VALUE/2) {
			if(nBianchi  < 3) {
				int v=0;
				for(String st : bianchi) {
					int posizione= Integer.parseInt(st);
					
					//le unit� sono le colonne mentre le decine sono le righe
					int riga = posizione/10;
					int colonna= posizione%10;
					
					 if(common.checkWhiteCanBeCaptured(riga, colonna, s))
						 v += 1;
					 
				}
				if(v> 0 )
					return this.MAX_VALUE-v*2;
			}
				int val=0;
				for(String st : bianchi) {
					int posizione= Integer.parseInt(st);
					
					//le unit� sono le colonne mentre le decine sono le righe
					int riga = posizione/10;
					int colonna= posizione%10;
					
					
					if(common.checkNeighbourBottom(riga, colonna, s).equals("K"))
						if(common.checkWhiteCanBeCaptured(riga, colonna, s))
							val += 1;
					
					if(common.checkNeighbourTop(riga, colonna, s).equals("K"))
						if(common.checkWhiteCanBeCaptured(riga, colonna, s))
							val += 1;
					
					if(common.checkNeighbourLeft(riga, colonna, s).equals("K"))
						if(common.checkWhiteCanBeCaptured(riga, colonna, s))
							val += 1;
					
					if(common.checkNeighbourRight(riga, colonna, s).equals("K"))
						if(common.checkWhiteCanBeCaptured(riga, colonna, s))
							val += 1;
					if(val > 0)
						return this.MAX_VALUE-(2*val);
				}
		}
		
		//maxvalue/4 � il massimo a cui si pu� arrivare
		int valueSpostamentoRe = this.getValueOfSpostamentoDelRe(neri, rigaRe, colonnaRe, s);
		if(valueSpostamentoRe > this.MAX_VALUE/4)
			return valueSpostamentoRe;
		value += valueSpostamentoRe;
		
		
		if((16 -nNeri) < (8 - nBianchi) +2) {
			//maxvalue/10 � il massimo a cui si pu� arrivare
			int valueDiagonali = this.getValueofDiagonali(neri, s);
			
			//se gi� ci sono 5 pedine sulla diagonale posso tornare perch� ho un buon valore
			if(valueDiagonali > 2000)
				return valueDiagonali;
			if(valueDiagonali >= this.MAX_VALUE/8)
			{
				value-= nBianchi*2*this.VALUE_WHITE_PAWN;
			}
			value += valueDiagonali;
		}
		
		value += this.getValueOfPosizioneDelleNere(neri, s);
		
		

		value +=this.getValueOfBianchiScappano(bianchi,rigaRe, colonnaRe, s);

		
		//itero su tutti i neri per vedere quali sono le mosse migliori per ogni pedina
	/*	 for(int i=0; i<neri.size(); i++ ) {
			
			int posizione= Integer.parseInt(neri.get(i));
			
			//le unit� sono le colonne mentre le decine sono le righe
			int riga = posizione/10;
			int colonna= posizione%10;
			
			//faccio uscire le nere dalle citadelle
			if(common.isCitadel(riga, colonna, s))
				value -=50;
			
			}*/
		//System.out.println("valore"+value);
		
		return value;
	}
	
	/**
	 * Controlla se c'è solo una pedina nera nella direzione specificata che può ostacolare il re nel muoversi fino al bordo
	 * @param rigaRe
	 * @param colonnaRe
	 * @param s
	 * @return riga in cui si trova la pedina, -1 altrimenti
	 */
	private int checkOneBlackUp(int rigaRe, int colonnaRe, StateTablut s) {
		
		if(colonnaRe == 3 || colonnaRe == 4 || colonnaRe == 5) {
			return -1;
		}
		
		int pedineNereTrovate = 0;
		int riga = -1;
		
		for(int i=rigaRe-1; i>=0; i--) {
			if(s.getPawn(i, colonnaRe).equalsPawn("B")) {
				pedineNereTrovate++;
				riga = i;
			}
			if(s.getPawn(i, colonnaRe).equalsPawn("W") || s.getPawn(i, colonnaRe).equalsPawn("T") || this.citadels.contains(s.getBox(i, colonnaRe))) {
				return -1;
			}
		}
		if(pedineNereTrovate == 1) {
			return riga;
		}
		
		return -1;
		
	}
	
	/**
	 * Controlla se c'è solo una pedina nera nella direzione specificata che può ostacolare il re nel muoversi fino al bordo
	 * @param rigaRe
	 * @param colonnaRe
	 * @param s
	 * @return riga in cui si trova la pedina, -1 altrimenti
	 */
	private int checkOneBlackDown(int rigaRe, int colonnaRe, StateTablut s) {
		
		if(colonnaRe == 3 || colonnaRe == 4 || colonnaRe == 5) {
			return -1;
		}
		
		int pedineNereTrovate = 0;
		int riga = -1;
		
		for(int i=rigaRe+1; i<9; i++) {
			if(s.getPawn(i, colonnaRe).equalsPawn("B")) {
				pedineNereTrovate++;
				riga = i;
			}
			if(s.getPawn(i, colonnaRe).equalsPawn("W") || s.getPawn(i, colonnaRe).equalsPawn("T") || this.citadels.contains(s.getBox(i, colonnaRe))) {
				return -1;
			}
		}
		if(pedineNereTrovate == 1) {
			return riga;
		}
		
		return -1;
		
	}
	
	/**
	 * Controlla se c'è solo una pedina nera nella direzione specificata che può ostacolare il re nel muoversi fino al bordo
	 * @param rigaRe
	 * @param colonnaRe
	 * @param s
	 * @return colonna in cui si trova la pedina, -1 altrimenti
	 */
	private int checkOneBlackLeft(int rigaRe, int colonnaRe, StateTablut s) {
		
		if(rigaRe == 3 || rigaRe == 4 || rigaRe == 5) {
			return -1;
		}
		
		int pedineNereTrovate = 0;
		int colonna = -1;
		
		for(int i=colonnaRe-1; i>=0; i--) {
			if(s.getPawn(rigaRe, i).equalsPawn("B")) {
				pedineNereTrovate++;
				colonna = i;
			}
			if(s.getPawn(rigaRe, i).equalsPawn("W") || s.getPawn(rigaRe, i).equalsPawn("T") || this.citadels.contains(s.getBox(rigaRe, i))) {
				return -1;
			}
		}
		if(pedineNereTrovate == 1) {
			return colonna;
		}
		
		return -1;	
	}
	
	/**
	 * Controlla se c'è solo una pedina nera nella direzione specificata che può ostacolare il re nel muoversi fino al bordo
	 * @param rigaRe
	 * @param colonnaRe
	 * @param s
	 * @return colonna in cui si trova la pedina, -1 altrimenti
	 */
	private int checkOneBlackRight(int rigaRe, int colonnaRe, StateTablut s) {
		
		if(colonnaRe == 3 || colonnaRe == 4 || colonnaRe == 5) {
			return -1;
		}
		
		int pedineNereTrovate = 0;
		int colonna = -1;
		
		for(int i=colonnaRe+1; i<9; i++) {
			if(s.getPawn(rigaRe, i).equalsPawn("B")) {
				pedineNereTrovate++;
				colonna = i;
			}
			if(s.getPawn(rigaRe, i).equalsPawn("W") || s.getPawn(rigaRe, i).equalsPawn("T") || this.citadels.contains(s.getBox(rigaRe, i))) {
				return -1;
			}
		}
		if(pedineNereTrovate == 1) {
			return colonna;
		}
		
		return -1;	
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
	

	//microfunzioni di cui si analizza il valore
	//caso in cui pedina sulla diagonale
	//caso in cui pedine vicine sulla diagonale
	//caso re fuori dalle macroaree
	//caso re fuori orizz o verticale
	private int getValueofDiagonali(List<String> posNeri, StateTablut s) {
		int value=0;
		 //controllo che possa andare in una delle posizioni buone (per le prime mosse)
		/*
		 * OOOOOOOOO
		 * OOOBOBOOO
		 * OOBOOOBOO
		 * OBOOOOOBO
		 * OOOOOOOOO
		 * OBOOOOOBO
		 * OOBOOOBOO
		 * OOOBOBOOO
		 * OOOOOOOOO
		 * */
		//caso in cui le pedine ci sono gi�
		for(String st : this.perfectPos ) {
			if(posNeri.contains(st)) {
				//diviso 10 � il valore che gli do mentre /8 � perch� sono 8 le posizioni giuste
				value += (this.MAX_VALUE/36);
				}
			}
		
	/*	if(value >= MAX_VALUE/12) {
		//caso in cui le pedine possano avvicinarsi alla diagonale
			if(common.checkBlackCanArriveAdjacentInBottomPosition(2, 1, s) && !common.checkBlackCanBeCaptured(1, 3, s)) 
				value += 20;
			if(common.checkBlackCanArriveAdjacentInBottomPosition(1, 2, s) && !common.checkBlackCanBeCaptured(2, 2, s)) 
				value +=20;
			if(common.checkBlackCanArriveAdjacentInRightPosition(1, 2, s) && !common.checkBlackCanBeCaptured(3, 1, s)) 
				value += 20;
			
			if(common.checkBlackCanArriveAdjacentInBottomPosition(2, 7, s) && !common.checkBlackCanBeCaptured(3, 7, s)) 
				value +=20;
			if(common.checkBlackCanArriveAdjacentInBottomPosition(1, 6, s) && !common.checkBlackCanBeCaptured(2, 6, s)) 
				value +=20;
			if(common.checkBlackCanArriveAdjacentInLeftPosition(1, 6, s) && !common.checkBlackCanBeCaptured(1, 5, s)) 
				value += 20;
				
			if(common.checkBlackCanArriveAdjacentInTopPosition(6, 1, s) && !common.checkBlackCanBeCaptured(5, 1, s))
				value +=20;
			if(common.checkBlackCanArriveAdjacentInTopPosition(7, 2, s) && !common.checkBlackCanBeCaptured(6, 2, s))
				value +=20;
			if(common.checkBlackCanArriveAdjacentInRightPosition(7, 2, s) && !common.checkBlackCanBeCaptured(7, 3, s)) 
				value += 20;
			
			if(common.checkBlackCanArriveAdjacentInTopPosition(6, 7, s) && !common.checkBlackCanBeCaptured(5, 7, s)) 
				value+=20;
			if(common.checkBlackCanArriveAdjacentInTopPosition(7, 6, s) && !common.checkBlackCanBeCaptured(6, 6, s)) 
				value+=20;
			if( common.checkBlackCanArriveAdjacentInLeftPosition(7, 6, s) && !common.checkBlackCanBeCaptured(7, 5, s)) 
				value+=20;
		}*/
		//System.out.println("valorediagonale"+value);
		return value;
	}
	
	/***
	 * se ci sono delle nere gi� posizionate do pi� importanza alle pedine che ci sono nella diagonale opposta
	 * */
	
	private int getValueOfPosizioneDelleNere(List<String> posNeri, StateTablut s) {
		int value=0;
		//hanno tutte lo stesso valore
		//ora faccio le casistiche in cui ci sono gi� dei neri sulla diagonale
		for(String st : this.perfectPos ) {
			if(posNeri.contains(st)) {
				int riga= Integer.parseInt(st)/10;
				int colonna = Integer.parseInt(st)%10;
				//se si trova in basso a destra
				if(riga> 4 && colonna>4){
					if(posNeri.contains("12") && !common.checkBlackCanBeCaptured(2, 3, s))
						value+= 80;
					if(posNeri.contains("21") && !common.checkBlackCanBeCaptured(3, 2, s))
						value+= 80;
					
					if(posNeri.contains("13")&& !common.checkBlackCanBeCaptured(1, 3, s))
						value+= 20;
					if(posNeri.contains("22")&& !common.checkBlackCanBeCaptured(2, 2, s))
						value+= 20;
					if(posNeri.contains("31")&& !common.checkBlackCanBeCaptured(3, 1, s))
						value+= 20;
					
					if(posNeri.contains("23") && !common.checkBlackCanBeCaptured(2, 3, s))
						value+= 30;
					if(posNeri.contains("32") && !common.checkBlackCanBeCaptured(3, 2, s))
						value+= 30;
			/*		if(common.checkBlackCanArriveAdjacentInBottomPosition(2, 1, s) && !common.checkBlackCanBeCaptured(3, 1, s)) 
						value +=10;
					if(common.checkBlackCanArriveAdjacentInBottomPosition(1, 2, s) && !common.checkBlackCanBeCaptured(2, 2, s)) 
						value +=10;
					if(common.checkBlackCanArriveAdjacentInRightPosition(1, 2, s) && !common.checkBlackCanBeCaptured(1, 3, s)) 
						value += 10;*/
				}
				//se si trova in basso a sinistra
				if(riga>4 && colonna <4) {
					if(posNeri.contains("16") && !common.checkBlackCanBeCaptured(2, 3, s))
						value+= 80;
					if(posNeri.contains("27") && !common.checkBlackCanBeCaptured(3, 2, s))
						value+= 80;
					
					
					if(posNeri.contains("15") && !common.checkBlackCanBeCaptured(1, 5, s))
						value+= 20;
					if(posNeri.contains("26") && !common.checkBlackCanBeCaptured(2, 6, s))
						value+= 2;
					if(posNeri.contains("37") && !common.checkBlackCanBeCaptured(3, 7, s))
					 	value+= 20;
					if(posNeri.contains("25") && !common.checkBlackCanBeCaptured(2, 5, s))
						value+= 30;
					if(posNeri.contains("36") && !common.checkBlackCanBeCaptured(3, 6, s))
						value+= 30;
		/*			if(common.checkBlackCanArriveAdjacentInBottomPosition(2, 7, s) && !common.checkBlackCanBeCaptured(3, 7, s)) 
						value +=10;
					if(common.checkBlackCanArriveAdjacentInBottomPosition(1, 6, s) && !common.checkBlackCanBeCaptured(2, 6, s)) 
						value +=10;
					if(common.checkBlackCanArriveAdjacentInLeftPosition(1, 6, s) && !common.checkBlackCanBeCaptured(1, 5, s)) 
						value += 10;*/
				}
				//se si trova in alto a destra
				if(riga < 4 && colonna > 4) {
					if(posNeri.contains("61") && !common.checkBlackCanBeCaptured(2, 3, s))
						value+= 80;
					if(posNeri.contains("72") && !common.checkBlackCanBeCaptured(3, 2, s))
						value+= 80;
					
					if(posNeri.contains("51") && !common.checkBlackCanBeCaptured(5, 1, s))
						value+= 20;
					if(posNeri.contains("62") && !common.checkBlackCanBeCaptured(6, 2, s))
						value+= 20;
					if(posNeri.contains("73") && !common.checkBlackCanBeCaptured(7, 3, s))
						value+= 20;
					if(posNeri.contains("52") && !common.checkBlackCanBeCaptured(5, 2, s))
						value+= 30;
					if(posNeri.contains("63") && !common.checkBlackCanBeCaptured(6, 3, s))
						value+= 30;
						
					
				/*	if(common.checkBlackCanArriveAdjacentInTopPosition(6, 1, s) && !common.checkBlackCanBeCaptured(5, 1, s))
						value +=10;
					if(common.checkBlackCanArriveAdjacentInTopPosition(7, 2, s) && !common.checkBlackCanBeCaptured(6, 2, s))
						value +=10;
					if(common.checkBlackCanArriveAdjacentInRightPosition(7, 2, s) && !common.checkBlackCanBeCaptured(7, 3, s)) 
						value += 10;*/
					
					
					
				}
				if(riga < 4 && colonna < 4) {
					if(posNeri.contains("76") && !common.checkBlackCanBeCaptured(2, 3, s))
						value+= 80;
					if(posNeri.contains("67") && !common.checkBlackCanBeCaptured(3, 2, s))
						value+= 80;
					
					
					if(posNeri.contains("75") && !common.checkBlackCanBeCaptured(7, 5, s))
						value+= 20;
					if(posNeri.contains("66") && !common.checkBlackCanBeCaptured(6, 6, s))
						value+= 20;
					if(posNeri.contains("57") && !common.checkBlackCanBeCaptured(5, 7, s))
						value+= 20;
					if(posNeri.contains("56") && !common.checkBlackCanBeCaptured(5, 6, s))
						value+= 30;
					if(posNeri.contains("65") && !common.checkBlackCanBeCaptured(6, 5, s))
						value+= 30;
			/*		if(common.checkBlackCanArriveAdjacentInTopPosition(6, 7, s) && !common.checkBlackCanBeCaptured(5, 7, s)) 
						value+=10;
					if(common.checkBlackCanArriveAdjacentInTopPosition(7, 6, s) && !common.checkBlackCanBeCaptured(6, 6, s)) 
						value+=10;
					if( common.checkBlackCanArriveAdjacentInLeftPosition(7, 6, s) && !common.checkBlackCanBeCaptured(7, 5, s)) 
						value+=10;*/
				}
			}
				
		}
		return value;
	}
	
	/***
	 * valuta quando il re si muove e cerca di mettere pi� pedine nere nelle 4 macro aree 
	 * nel caso il re si muove solo sulla linea verticale o orizzontale metto le pedine nel semipiano
	 * @param posNeri
	 * @param rigaRe
	 * @param colonnaRe
	 * @param s
	 * @return
	 */
	private int getValueOfSpostamentoDelRe(List<String> posNeri,int rigaRe,int colonnaRe, StateTablut s) {
		int value =0;
		//caso in cui il re si sposta in una delle 4 macroaree
		if(rigaRe != 4 && colonnaRe != 4) {
			if(rigaRe> 4 && colonnaRe >4){
			//pedina gi� nella diagonale valore molto alto, se ci pu� arrivare valore minore
				if(posNeri.contains("75"))
					value+= (this.MAX_VALUE-1)/6;
				if(posNeri.contains("66"))
					value+= (this.MAX_VALUE-1)/6;
				if(posNeri.contains("57"))
					value+= (this.MAX_VALUE-1)/6;
			/*	if(common.checkBlackCanArriveAdjacentInTopPosition(6, 7, s) && !common.checkBlackCanBeCaptured(5, 7, s)) 
					value+=60;
				if(common.checkBlackCanArriveAdjacentInTopPosition(7, 6, s) && !common.checkBlackCanBeCaptured(6, 6, s)) 
					value+=60;
				if( common.checkBlackCanArriveAdjacentInLeftPosition(7, 6, s) && !common.checkBlackCanBeCaptured(7, 5, s)) 
					value+=60; */
			}
			//se si trova in basso a sinistra
			if(rigaRe>4 && colonnaRe <4) {
				if(posNeri.contains("51"))
					value+= (this.MAX_VALUE-1)/6;
				if(posNeri.contains("62"))
					value+= (this.MAX_VALUE-1)/6;
				if(posNeri.contains("73"))
					value+= (this.MAX_VALUE-1)/6;
			/*	if(common.checkBlackCanArriveAdjacentInBottomPosition(2, 7, s) && !common.checkBlackCanBeCaptured(3, 7, s)) 
					value +=60;
				if(common.checkBlackCanArriveAdjacentInBottomPosition(1, 6, s) && !common.checkBlackCanBeCaptured(2, 6, s)) 
					value +=60;
				if(common.checkBlackCanArriveAdjacentInLeftPosition(1, 6, s) && !common.checkBlackCanBeCaptured(1, 5, s)) 
					value += 60;*/
			}
			//se si trova in alto a destra
			if(rigaRe < 4 && colonnaRe > 4) {
				if(posNeri.contains("15"))
					value+= (this.MAX_VALUE-1)/6;
				if(posNeri.contains("26"))
					value+= (this.MAX_VALUE-1)/6;
				if(posNeri.contains("37"))
					value+= (this.MAX_VALUE-1)/6;
			/*	if(common.checkBlackCanArriveAdjacentInTopPosition(6, 1, s) && !common.checkBlackCanBeCaptured(5, 1, s))
					value +=60;
				if(common.checkBlackCanArriveAdjacentInTopPosition(7, 2, s) && !common.checkBlackCanBeCaptured(6, 2, s))
					value +=60;
				if(common.checkBlackCanArriveAdjacentInRightPosition(7, 2, s) && !common.checkBlackCanBeCaptured(7, 3, s)) 
					value += 60;*/
			}
			if(rigaRe < 4 && colonnaRe < 4) {
				if(posNeri.contains("13"))
					value+= (this.MAX_VALUE-1)/6;
				if(posNeri.contains("22"))
					value+= (this.MAX_VALUE-1)/6;
				if(posNeri.contains("31"))
					value+= (this.MAX_VALUE-1)/6;
		/*		if(common.checkBlackCanArriveAdjacentInBottomPosition(2, 1, s) && !common.checkBlackCanBeCaptured(3, 1, s)) 
					value +=60;
				if(common.checkBlackCanArriveAdjacentInBottomPosition(1, 2, s) && !common.checkBlackCanBeCaptured(2, 2, s)) 
					value +=60;
				if(common.checkBlackCanArriveAdjacentInRightPosition(1, 2, s) && !common.checkBlackCanBeCaptured(1, 3, s)) 
					value += 60;
			*/
			}
		}
		//caso re si sposta solo orizzotale o voerticale dal trono
		//si sposta in verticale
		if(rigaRe != 4 && colonnaRe ==4) {
			if(rigaRe <4){
				if(posNeri.contains("13"))
					value+= (this.MAX_VALUE-1)/12;
				if(posNeri.contains("22"))
					value+= (this.MAX_VALUE-1)/12;
				if(posNeri.contains("31"))
					value+= (this.MAX_VALUE-1)/12;
				if(posNeri.contains("15"))
					value+= (this.MAX_VALUE-1)/12;
				if(posNeri.contains("26"))
					value+= (this.MAX_VALUE-1)/12;
				if(posNeri.contains("37"))
					value+= (this.MAX_VALUE-1)/12;
				/*if(common.checkBlackCanArriveAdjacentInBottomPosition(2, 1, s) && !common.checkBlackCanBeCaptured(3, 1, s)) 
					value +=50;
				if(common.checkBlackCanArriveAdjacentInBottomPosition(1, 2, s) && !common.checkBlackCanBeCaptured(2, 2, s)) 
					value +=50;
				if(common.checkBlackCanArriveAdjacentInRightPosition(1, 2, s) && !common.checkBlackCanBeCaptured(1, 3, s)) 
					value += 50;
				if(common.checkBlackCanArriveAdjacentInBottomPosition(2, 7, s) && !common.checkBlackCanBeCaptured(3, 7, s)) 
					value +=50;
				if(common.checkBlackCanArriveAdjacentInBottomPosition(1, 6, s) && !common.checkBlackCanBeCaptured(2, 6, s)) 
					value +=50;
				if(common.checkBlackCanArriveAdjacentInLeftPosition(1, 6, s) && !common.checkBlackCanBeCaptured(1, 5, s)) 
					value += 50;*/
			}
			if(rigaRe > 4) {
				if(posNeri.contains("51"))
					value+= (this.MAX_VALUE-1)/12;
				if(posNeri.contains("62"))
					value+= (this.MAX_VALUE-1)/12;
				if(posNeri.contains("73"))
					value+= (this.MAX_VALUE-1)/12;
				if(posNeri.contains("75"))
					value+= (this.MAX_VALUE-1)/12;
				if(posNeri.contains("66"))
					value+= (this.MAX_VALUE-1)/12;
				if(posNeri.contains("57"))
					value+= (this.MAX_VALUE-1)/12;
			/*	if(common.checkBlackCanArriveAdjacentInTopPosition(6, 1, s) && !common.checkBlackCanBeCaptured(5, 1, s))
					value +=50;
				if(common.checkBlackCanArriveAdjacentInTopPosition(7, 2, s) && !common.checkBlackCanBeCaptured(6, 2, s))
					value +=50;
				if(common.checkBlackCanArriveAdjacentInRightPosition(7, 2, s) && !common.checkBlackCanBeCaptured(7, 3, s)) 
					value += 50;
				if(common.checkBlackCanArriveAdjacentInTopPosition(6, 7, s) && !common.checkBlackCanBeCaptured(5, 7, s)) 
					value+=50;
				if(common.checkBlackCanArriveAdjacentInTopPosition(7, 6, s) && !common.checkBlackCanBeCaptured(6, 6, s)) 
					value+=50;
				if( common.checkBlackCanArriveAdjacentInLeftPosition(7, 6, s) && !common.checkBlackCanBeCaptured(7, 5, s)) 
					value+=50;*/
			}
		}
		if(rigaRe == 4 && colonnaRe !=4) {
			if( colonnaRe <4){
				if(posNeri.contains("13"))
					value+= (this.MAX_VALUE-1)/12;
				if(posNeri.contains("22"))
					value+= (this.MAX_VALUE-1)/12;
				if(posNeri.contains("31"))
					value+= (this.MAX_VALUE-1)/12;
				if(posNeri.contains("51"))
					value+= (this.MAX_VALUE-1)/12;
				if(posNeri.contains("62"))
					value+= (this.MAX_VALUE-1)/12;
				if(posNeri.contains("73"))
					value+= (this.MAX_VALUE-1)/12;
			/*	if(common.checkBlackCanArriveAdjacentInBottomPosition(2, 1, s) && !common.checkBlackCanBeCaptured(3, 1, s)) 
					value +=50;
				if(common.checkBlackCanArriveAdjacentInBottomPosition(1, 2, s) && !common.checkBlackCanBeCaptured(2, 2, s)) 
					value +=50;
				if(common.checkBlackCanArriveAdjacentInRightPosition(1, 2, s) && !common.checkBlackCanBeCaptured(1, 3, s)) 
					value += 50;
				if(common.checkBlackCanArriveAdjacentInTopPosition(6, 1, s) && !common.checkBlackCanBeCaptured(5, 1, s))
					value +=50;
				if(common.checkBlackCanArriveAdjacentInTopPosition(7, 2, s) && !common.checkBlackCanBeCaptured(6, 2, s))
					value +=50;
				if(common.checkBlackCanArriveAdjacentInRightPosition(7, 2, s) && !common.checkBlackCanBeCaptured(7, 3, s)) 
					value += 50;*/
			}
			if(colonnaRe >4) {
				if(posNeri.contains("15"))
					value+= (this.MAX_VALUE-1)/12;
				if(posNeri.contains("26"))
					value+= (this.MAX_VALUE-1)/12;
				if(posNeri.contains("37"))
					value+= (this.MAX_VALUE-1)/12;
				if(posNeri.contains("75"))
					value+= (this.MAX_VALUE-1)/12;
				if(posNeri.contains("66"))
					value+= (this.MAX_VALUE-1)/12;
				if(posNeri.contains("57"))
					value+= (this.MAX_VALUE-1)/12;
		/*		if(common.checkBlackCanArriveAdjacentInBottomPosition(2, 7, s) && !common.checkBlackCanBeCaptured(3, 7, s)) 
					value +=50;
				if(common.checkBlackCanArriveAdjacentInBottomPosition(1, 6, s) && !common.checkBlackCanBeCaptured(2, 6, s)) 
					value +=50;
				if(common.checkBlackCanArriveAdjacentInLeftPosition(1, 6, s) && !common.checkBlackCanBeCaptured(1, 5, s)) 
					value += 50;
				if(common.checkBlackCanArriveAdjacentInTopPosition(6, 7, s) && !common.checkBlackCanBeCaptured(5, 7, s)) 
					value+=50;
				if(common.checkBlackCanArriveAdjacentInTopPosition(7, 6, s) && !common.checkBlackCanBeCaptured(6, 6, s)) 
					value+=50;
				if( common.checkBlackCanArriveAdjacentInLeftPosition(7, 6, s) && !common.checkBlackCanBeCaptured(7, 5, s)) 
					value+=50;*/
			}
		}
		
			
		//System.out.println("valorespostamento"+value);
		
		return value;
	}
	
	/***
	 * dalla posizione del re vedo se delle pedine possono mangiarlo
	 * @param posNeri
	 * @param rigaRe
	 * @param colonnaRe
	 * @param s
	 * @return il valore "massimo" qui pu� essere diviso per 4 se il re � sul trono, per 3 se � vicino altrimenti per 2
	 */
	private int getValueOfReAccerchiato(List<String> posNeri,int rigaRe,int colonnaRe, StateTablut s) {
		int value=0;
		
		//casistiche del re dentro o fuori dal trono
		if(common.kingOnTheThrone(rigaRe, colonnaRe)) {
			//deve essere accerchiato sui 4 lati
			//se una pedina � gi� vicino al re e non rischia di essere mangiata deve rimanere l�
			if(common.checkNeighbourBottom(rigaRe, colonnaRe, s).equals("B") && !common.checkBlackCanBeCaptured(rigaRe-1, colonnaRe, s))
				value+= (this.MAX_VALUE)/4;
			if(common.checkNeighbourTop(rigaRe, colonnaRe, s).equals("B") && !common.checkBlackCanBeCaptured(rigaRe+1, colonnaRe, s))
				value+=(this.MAX_VALUE)/4;
			if(common.checkNeighbourLeft(rigaRe, colonnaRe, s).equals("B") && !common.checkBlackCanBeCaptured(rigaRe, colonnaRe-1, s))
				value+=(this.MAX_VALUE)/4;
			if(common.checkNeighbourRight(rigaRe, colonnaRe, s).equals("B") && !common.checkBlackCanBeCaptured(rigaRe, colonnaRe+1, s))
				value+=(this.MAX_VALUE)/4;
			//controllo che le pedine possano arrivare al re senza essere mangiate
			if(common.checkBlackCanArriveAdjacentInBottomPosition(rigaRe, colonnaRe, s) && !common.checkBlackCanBeCaptured(rigaRe, colonnaRe-1, s))
				value+= 46;
			if(common.checkBlackCanArriveAdjacentInTopPosition(rigaRe, colonnaRe, s) && !common.checkBlackCanBeCaptured(rigaRe, colonnaRe+1, s))
				value+= 46;
			if(common.checkBlackCanArriveAdjacentInLeftPosition(rigaRe, colonnaRe, s) && !common.checkBlackCanBeCaptured(rigaRe-1, colonnaRe, s))
				value+= 46;
			if(common.checkBlackCanArriveAdjacentInRightPosition(rigaRe, colonnaRe, s) && !common.checkBlackCanBeCaptured(rigaRe+1, colonnaRe, s))
				value+= 46;
		}
		//se il re si trova vicino al trono (servono 3 neri per catturarlo)
		if(common.kingAdjacentToTheThrone(rigaRe, colonnaRe)) {
			
			if(common.checkNeighbourBottom(rigaRe, colonnaRe, s).equals("B") && !common.checkBlackCanBeCaptured(rigaRe-1, colonnaRe, s))
				value+= (this.MAX_VALUE)/3;
			if(common.checkNeighbourTop(rigaRe, colonnaRe, s).equals("B") && !common.checkBlackCanBeCaptured(rigaRe+1, colonnaRe, s))
				value+=(this.MAX_VALUE)/3;
			if(common.checkNeighbourLeft(rigaRe, colonnaRe, s).equals("B") && !common.checkBlackCanBeCaptured(rigaRe, colonnaRe-1, s))
				value+=(this.MAX_VALUE)/3;
			if(common.checkNeighbourRight(rigaRe, colonnaRe, s).equals("B") && !common.checkBlackCanBeCaptured(rigaRe, colonnaRe+1, s))
				value+=(this.MAX_VALUE)/3;
			//controllo che le pedine possano arrivare al re senza essere mangiate
			if(common.checkBlackCanArriveAdjacentInBottomPosition(rigaRe, colonnaRe, s) && !common.checkBlackCanBeCaptured(rigaRe, colonnaRe-1, s))
				value+= 46;
			if(common.checkBlackCanArriveAdjacentInTopPosition(rigaRe, colonnaRe, s) && !common.checkBlackCanBeCaptured(rigaRe, colonnaRe+1, s))
				value+= 46;
			if(common.checkBlackCanArriveAdjacentInLeftPosition(rigaRe, colonnaRe, s) && !common.checkBlackCanBeCaptured(rigaRe-1, colonnaRe, s))
				value+= 46;
			if(common.checkBlackCanArriveAdjacentInRightPosition(rigaRe, colonnaRe, s) && !common.checkBlackCanBeCaptured(rigaRe+1, colonnaRe, s))
				value+= 46;
				
		}
		//se si trova  vicino ad una cittadella
		if(common.checkNeighbourBottom(rigaRe, colonnaRe, s).equals("C") || common.checkNeighbourTop(rigaRe, colonnaRe, s).equals("C") || 
				common.checkNeighbourRight(rigaRe, colonnaRe, s).equals("C") || common.checkNeighbourLeft(rigaRe, colonnaRe, s).equals("C")) {
			if(common.checkNeighbourBottom(rigaRe, colonnaRe, s).equals("B") && !common.checkBlackCanBeCaptured(rigaRe-1, colonnaRe, s))
				value+= this.MAX_VALUE-1;
			if(common.checkNeighbourTop(rigaRe, colonnaRe, s).equals("B") && !common.checkBlackCanBeCaptured(rigaRe+1, colonnaRe, s))
				value+=this.MAX_VALUE-1;
			if(common.checkNeighbourLeft(rigaRe, colonnaRe, s).equals("B") && !common.checkBlackCanBeCaptured(rigaRe, colonnaRe-1, s))
				value+=this.MAX_VALUE-1;
			if(common.checkNeighbourRight(rigaRe, colonnaRe, s).equals("B") && !common.checkBlackCanBeCaptured(rigaRe, colonnaRe+1, s))
				value+=this.MAX_VALUE-1;
		}
		
		//se il re si trova in qualsiasi altro punto del piano (servono 2 neri per catturarlo)
		else {

			if(common.checkNeighbourBottom(rigaRe, colonnaRe, s).equals("B") && !common.checkBlackCanBeCaptured(rigaRe-1, colonnaRe, s))
				value+= (this.MAX_VALUE)/2;
			if(common.checkNeighbourTop(rigaRe, colonnaRe, s).equals("B") && !common.checkBlackCanBeCaptured(rigaRe+1, colonnaRe, s))
				value+=(this.MAX_VALUE)/2;
			if(common.checkNeighbourLeft(rigaRe, colonnaRe, s).equals("B") && !common.checkBlackCanBeCaptured(rigaRe, colonnaRe-1, s))
				value+=(this.MAX_VALUE)/2;
			if(common.checkNeighbourRight(rigaRe, colonnaRe, s).equals("B") && !common.checkBlackCanBeCaptured(rigaRe, colonnaRe+1, s))
				value+=(this.MAX_VALUE)/2;
			//controllo che le pedine possano arrivare al re senza essere mangiate
			if(common.checkBlackCanArriveAdjacentInBottomPosition(rigaRe, colonnaRe, s) && !common.checkBlackCanBeCaptured(rigaRe, colonnaRe-1, s))
				value+= 46;
			if(common.checkBlackCanArriveAdjacentInTopPosition(rigaRe, colonnaRe, s) && !common.checkBlackCanBeCaptured(rigaRe, colonnaRe+1, s))
				value+= 46;
			if(common.checkBlackCanArriveAdjacentInLeftPosition(rigaRe, colonnaRe, s) && !common.checkBlackCanBeCaptured(rigaRe-1, colonnaRe, s))
				value+= 46;
			if(common.checkBlackCanArriveAdjacentInRightPosition(rigaRe, colonnaRe, s) && !common.checkBlackCanBeCaptured(rigaRe+1, colonnaRe, s))
				value+= 46;
				
		}

		
		if(value > this.MAX_VALUE)
			return this.MAX_VALUE-1;
		//System.out.println("valoreaccerchiato"+value);
		return value;
		
	}
	
	/**se un bianco si trova dietro a dei neri (inteso nelle posizioni dietro la diagonale perfetta) lui deve essere mangiato. 
	*se ci si trova ma non pu� essere mangiato devo restituire un valore molto negativo(ovvero evito di finire in questo stato)
	*
	**/
	private int getValueOfBianchiScappano(List<String> posBianchi, int rigaRe, int colonnaRe, StateTablut s) {
		int value=0;
		for(String st : posBianchi){
			int riga= Integer.parseInt(st)/10;
			int colonna = Integer.parseInt(st)%10;
			if(riga == 0 || colonna ==0 || riga ==8 || colonna == 8 || 
					((riga == 1 || riga==7) && ( colonna == 1 || colonna == 2 || colonna == 6 || colonna ==7)) ||
					((riga ==2 || riga==6)&& (colonna ==1 || colonna ==7)) )
				if(common.checkWhiteCanBeCaptured(riga, colonna, s))
					//non so se inserire anche il rischio che i neri vengano mangiati
					value+=400; // molto importante
				else value -= 400;
		
		
		//controllo se ci sono dei bianchi vicino al re e se sono mangiabili lo faccio in modo da avere libera la strada
			if(!common.kingOnTheThrone(rigaRe, colonnaRe)) {
				if(common.checkNeighbourBottom(riga, colonna, s).equals("K") )
					if( common.checkWhiteCanBeCaptured(riga, colonna, s))		 
						value =+ 250;
				if(common.checkNeighbourTop(riga, colonna, s).equals("K") )
					if( common.checkWhiteCanBeCaptured(riga, colonna, s))
						value =+ 250;
				if(common.checkNeighbourLeft(riga, colonna, s).equals("K") )
					if( common.checkWhiteCanBeCaptured(riga, colonna, s))
						value =+ 250;
				if(common.checkNeighbourRight(riga, colonna, s).equals("K") )
					if( common.checkWhiteCanBeCaptured(riga, colonna, s))
						value =+ 250;
			}
		}
		
		
		return value;
	}
	
			
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
			
			System.out.println("Valore root: "+albero.get(0).getNodi().get(0).getValue());
			albero.clear();
			System.gc();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		numeroMosse += 1;
		System.out.println("Mossa: "+a.toString());
		System.out.println("");
		return a;
	}
	
	private class TreeGenerator3 implements Runnable{

		private Nodo nodoAttuale;
		private Simulator simulatore;
		private List<String> citadels;
		private IntelligenzaNera ia;
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
		
		public TreeGenerator3(Nodo n, List<String> cit, IntelligenzaNera i) {
			this.nodoAttuale = n;
			this.ia = i;
			//this.simulatore = s;
			//this.!Thread.currentThread().isInterrupted()=true;
			//this.iaB = ia;
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
			if(!this.liv1.getNodi().get(0).getTurn().equalsTurn("BW"))
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
					this.nodoLiv1.setValue(10000);
				}
				if(daAggiungere.get(0).getTurn().equalsTurn("WW"))
				{
					this.nodoLiv1.setValue(-10000);
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
					this.nodoLiv2.setValue(10000);
				}
				if(daAggiungere.get(0).getTurn().equalsTurn("WW"))
				{
					this.nodoLiv2.setValue(-10000);
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
					this.nodoLiv3.setValue(10000);
				}
				if(daAggiungere.get(0).getTurn().equalsTurn("WW"))
				{
					this.nodoLiv3.setValue(-10000);
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
					this.nodoLiv4.setValue(10000);
				}
				if(daAggiungere.get(0).getTurn().equalsTurn("WW"))
				{
					this.nodoLiv4.setValue(-10000);
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
		
		/*private void getValueOfNodeLiv5() {
			try {
				if(this.nodoLiv5.getValue() <= this.nodoLiv4.getValue()) {
					return;
				}
				List<Nodo> daAggiungere = this.simulatore.mossePossibiliComplete(this.nodoLiv5);
				this.sortLivGenD(daAggiungere);
				this.liv6.add(daAggiungere);
				if(daAggiungere.get(0).getTurn().equalsTurn("BW"))
				{
					this.nodoLiv5.setValue(-10000);
				}
				if(daAggiungere.get(0).getTurn().equalsTurn("WW"))
				{
					this.nodoLiv5.setValue(10000);
				}
				if(!daAggiungere.get(0).getTurn().equalsTurn("BW") && !daAggiungere.get(0).getTurn().equalsTurn("WW"))
				{
					for(int i=0; i<daAggiungere.size() && !taglioLivello6 && !Thread.currentThread().isInterrupted(); i++)
					{
						this.nodoLiv6 = daAggiungere.get(i);
						this.nodoLiv6.setValue(this.nodoLiv0.getValue());
						this.getValueOfNodeLiv6();
						if(Float.isNaN(this.nodoLiv5.getValue()) || this.nodoLiv5.getValue()>this.nodoLiv6.getValue())
						{
							this.nodoLiv5.setValue(this.nodoLiv6.getValue());
						}
						if(this.nodoLiv5.getValue() <= this.nodoLiv4.getValue()) {
							taglioLivello6 = true;
						}
					}
				}
				
				taglioLivello6 = false;
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}			
		}*/
		
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
					nodo.setValue(-100000);
				}
				if(nodo.getStato().getTurn().equalsTurn("BW"))
				{
					nodo.setValue(100000);
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
			/*int nBianchi = 1;
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
			return 2*nBianchi-nNeri;*/				
		}
	
		//metodo per ordinare in ordine crescente (con simple heuristic) una lista di nodi
		private void sortLivGenC(List<Nodo> lista)
		{
			for(int x=0; x<lista.size(); x++)
			{
				Nodo nodo = lista.get(x);
				if(nodo.getStato().getTurn().equalsTurn("WW"))
				{
					nodo.setValue(-100000);
				}
				if(nodo.getStato().getTurn().equalsTurn("BW"))
				{
					nodo.setValue(+100000);
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
					nodo.setValue(-100000);
				}
				if(nodo.getStato().getTurn().equalsTurn("BW"))
				{
					nodo.setValue(100000);
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
