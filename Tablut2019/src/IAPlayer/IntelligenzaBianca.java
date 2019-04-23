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
	private Simulator simulatore;
	
	public IntelligenzaBianca() {
		this.simulatore = new Simulator();
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
		
		//Controllo se il re viene mangiato in qualsiasi posizione sia
		if(this.kingCanBeCaptured(rigaRe, colonnaRe, s))
		{
			return this.MIN_VALUE+1;
		}
		
		//controllo vie di fuga re
		int viedifuga=this.checkVieDiFugaRe(rigaRe, colonnaRe, s);
				
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
			if(blackCannotBlockEscape(s, rigaRe, colonnaRe))
			{
				return this.MAX_VALUE-1;
			}
		}		
		
		/*
		 * Funzione che controlla se, eseguita una mossa del re in orizzontale, esso ha liberato un'intera colonna (2 oppure 6), in cui vincere (al 100%) il turno successivo
		 */
		if (this.checkFreeColComingFromLeft(rigaRe, colonnaRe, s) || this.checkFreeColComingFromRight(rigaRe, colonnaRe, s)) {
			return this.MAX_VALUE;
		}
		
		/*
		 * Funzione che controlla se, eseguita una mossa del re in verticale, esso ha liberato un'intera riga (2 oppure 6), in cui vincere (al 100%) il turno successivo
		 */
		if (this.checkFreeRowComingFromTop(rigaRe, colonnaRe, s) || this.checkFreeRowComingFromBottom(rigaRe, colonnaRe, s)){
			return this.MAX_VALUE;
		}
			
				
		return value;
		
	}
	
	
	private boolean blackCannotBlockEscape(StateTablut s, int rigaRe, int colonnaRe) {
		
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
	
	/*
	 * Funzione di euristica, di prova <-- da modificare BRAVO ALE, HAI CAPITO COSA INTENDO
	 */
	private int getHeuristicValueOfState(StateTablut s) {
		return this.getNumberWhite(s) - this.getNumberBlack(s) + 2*this.getNumberStarFree(s);
	}
	
	
	//QUESTA MI PIACE POCO PERCHE NON RIUSCIAMO A CAPIRE DA UNA SINGOLA PASSATA QUANTE PEDINE CI SONO SUL TABELLONE E DOVE STA IL RE
	/**
	 * Restituisce il numero di pedine bianche presenti sul tabellone, dato lo stato s
	 * Nel conteggio è compreso anche il re
	 * @param s StateTablut rappresenta lo stato da valutare
	 * @return numero di pedine bianche, comprensive del re, presenti sulla scacchiera
	 */
	private int getNumberWhite(StateTablut s) {
		
		int result = 0;
		
		for(int i=0; i<9; i++) {
			for(int j=0; j<9; j++) {
				if(s.getPawn(i, j).equalsPawn("W")) {
					result++;
				}
			}
		}
		
		return result += 1; //aggiungo il re
	}
	
	//COME SOPRA
	/**
	 * Restituisce il numero di pedine nere presenti sul tabellone
	 * @param s StateTablut rappresenta lo stato da valutare
	 * @return numero di pedine nere, presenti sulla scacchiera
	 */
	private int getNumberBlack(StateTablut s) {
		
		int result = 0;
		
		for(int i=0; i<9; i++) {
			for(int j=0; j<9; j++) {
				if(s.getPawn(i, j).equalsPawn("B")) {
					result++;
				}
			}
		}
		
		return result;
	}
	
	
	//DOMANDA: SE UNA COLONNA è LIBERA ALLORA SI AGGIUNGE 3 PERCHE CONSIDERIAMO ANCHE LE 2 SEMICOLONNE?
	/**
	 * Ritorna il numero di colonne (o semicolonne) e righe (o semirighe) libere, al termine delle quali c'è la casella blu che permette al bianco di vincere
	 * 
	 * semicolonna = per semicolonna si intendono le colonne 1 e 7 che sono interrotte da una cittadella
	 * 
	 * semiriga = per semiriga si intendono le righe 1 e 7 che sono interrotte da una cittadella
	 * @param s StateTablut ovvero lo stato da valutare
	 * @return numero di colonne/semicolonne e righe/semirighe libere, per permettere ai bianchi di vincere
	 */
	private int getNumberStarFree(StateTablut s) {
		int result = 0;
		
		if(this.isColumnFree(2, s)) {
			result += 1;
		}
		
		if(this.isColumnFree(6, s)) {
			result += 1;
		}
		
		if(this.isSemicolumnFree(1, s)) {
			result += 1;
		}
		
		if(this.isSemicolumnFree(7, s)) {
			result += 1;
		}
		
		if(this.isRowFree(2, s)) {
			result += 1;
		}
		
		if(this.isRowFree(6, s)) {
			result += 1;
		}
		
		if(this.isSemirowFree(1, s)) {
			result += 1;
		}
		
		if(this.isSemirowFree(7, s)) {
			result += 1;
		}
		
		return result;
	}
	
	/**
	 * Controlla se una data colonna è libera ovvero se non ci sono ostacoli che impediscano ad una pedina nera o bianca che sia, di percorrerla
	 * Attenzione! Funziona solo se viene passata come parametro la colonna 2 o 6
	 * @param numberCol Colonna per la quale si vuole effettuare tale controllo
	 * @param s StateTablut ovvero lo stato da valutare
	 * @return true se la colonna, dalla cella 0 alla 8 è libera, false se invece è presente almeno una pedina bianca/nera, c'è il castello, oppure una o più cittadelle
	 */
	private boolean isColumnFree(int numberCol, StateTablut s) {
		
		boolean result = true;
		
		for(int i=0; i<9; i++) {
			if(s.getPawn(i, numberCol).equalsPawn("O") && !this.citadels.contains(s.getBox(i, numberCol))) {
				result = result && true;
			} else result = false;
		}
		
		return result;
	}
	
	/**
	 * Controlla se una data riga è libera ovvero se non ci sono ostacoli che impediscano ad una pedina nera o bianca che sia, di percorrerla
	 * Attenzione! Funziona solo se viene passata come parametro la riga 2 o 6
	 * @param numberRow Riga per la quale si vuole effettuare tale controllo
	 * @param s StateTablut ovvero lo stato da valutare
	 * @return true se la riga, dalla cella 0 alla 8 è libera, false se invece è presente almeno una pedina bianca/nera, c'è il castello, oppure una o più cittadelle
	 */
	private boolean isRowFree(int numberRow, StateTablut s) {
		
		boolean result = true;
		
		for(int i=0; i<9; i++) {
			if(s.getPawn(numberRow, i).equalsPawn("O") && !this.citadels.contains(s.getBox(numberRow, i))) {
				result = result && true;
			} else result = false;
		}
		
		return result;
	}
	
	/**
	 * Controlla se una data riga, intervallata da una cittadella, è libera ovvero se non ci sono ostacoli che impediscano ad una pedina nera o bianca che sia, di percorrerla
	 * Attenzione! Funziona solo se viene passata come parametro la riga 1 e 7 (quelle intervallate da una cittadella)
	 * @param numberRow Riga per la quale si vuole effettuare tale controllo
	 * @param s StateTablut ovvero lo stato da valutare
	 * @return true se le righe, dalla cella 0 alla 3 e dalla cella 5 alla cella 8 sono libere, false se invece è presente almeno una pedina bianca/nera in tali celle
	 */
	private boolean isSemirowFree(int numberRow, StateTablut s) {
		
		boolean result = true;
		
		for(int i=0; i<4; i++) {
			if(s.getPawn(numberRow, i).equalsPawn("O")) {
				result = result && true;
			} else result = false;
		} //controlla dalla colonna 0 alla 3
		
		if(!result) {
			return result;
		} else {
			for(int i=5; i<9; i++) {
				if(s.getPawn(numberRow, i).equalsPawn("O")) {
					result = result && true;
				} else result = false;
			} //controlla dalla colonna 5 (quella dopo la cittadella) alla colonna 8
		} 
		
		return result;
	}
	
	/**
	 * Controlla se una data colonna, intervallata da una cittadella, è libera ovvero se non ci sono ostacoli che impediscano ad una pedina nera o bianca che sia, di percorrerla
	 * Attenzione! Funziona solo se viene passata come parametro la colonna 1 e 7
	 * @param numberCol Colonna per la quale si vuole effettuare tale controllo
	 * @param s StateTablut ovvero lo stato da valutare
	 * @return true se le colonne, dalla cella 0 alla 3 e dalla cella 5 alla cella 8 sono libere, false se invece è presente almeno una pedina bianca/nera in tali celle
	 */
	private boolean isSemicolumnFree(int numberColumn, StateTablut s) {
		
		boolean result = true;
		
		for(int i=0; i<4; i++) {
			if(s.getPawn(i, numberColumn).equalsPawn("O")) {
				result = result && true;
			} else result = false;
		} //controlla dalla riga 0 alla 3
		
		if(!result) {
			return result;
		} else {
			for(int i=5; i<9; i++) {
				if(s.getPawn(i, numberColumn).equalsPawn("O")) {
					result = result && true;
				} else result = false;
			} //controlla dalla riga 5 (quella dopo la cittadella) alla riga 8
		} 
		
		return result;
	}

	/**
	 * Controlla la possibile cattura del re da parte dei neri, ritorna true se il re puo' essere catturato o false nel caso contrario
	 * Dopo aver controllato che sia il turno del nero la funzione distingue i tre casi di cattura:
	 * -Re sul trono e quindi servono 4 pedine per la cattura
	 * -Re adiacente al trono e quindi servono 3 pedine per la cattura
	 * -Re lontano dal trono e quindi servono 2 pedine per la cattura
	 * @param rigaRe Riga in cui si trova il re, al momento della valutazione
	 * @param colonnaRe Colonna in cui si trova il re, al momento della valutazione
	 * @param s StateTablut ovvero lo stato al momento della valutazione
	 * @return true se il re, in quello stato s, può essere catturato, false in caso contrario
	 */
	private boolean kingCanBeCaptured(int rigaRe, int colonnaRe, StateTablut s)
	{
		//Se e' il turno del bianco ritorna false se no controlla i 3 casi
		if(s.getTurn().equalsTurn("B"))
		{
			//Controllo di cattura con il re sul trono
			if(rigaRe==4 && colonnaRe==4)
			{
				//bloccato sopra, destra e sinistra
				if(this.enemyOnTheTop(rigaRe, colonnaRe, s) && this.enemyOnTheRight(rigaRe, colonnaRe, s) && this.enemyOnTheLeft(rigaRe, colonnaRe, s))
				{
					if(this.checkBlackCanArrive(5, 4, s))
					{
						return true;
					}
				}
				//bloccato sotto, destra, sinistra
				if(this.enemyOnTheBottom(rigaRe, colonnaRe, s) && this.enemyOnTheRight(rigaRe, colonnaRe, s) && this.enemyOnTheLeft(rigaRe, colonnaRe, s))
				{
					if(this.checkBlackCanArrive(3, 4, s))
					{
						return true;
					}
				}
				//bloccato sopra, sotto, destra
				if(this.enemyOnTheTop(rigaRe, colonnaRe, s) && this.enemyOnTheBottom(rigaRe, colonnaRe, s) && this.enemyOnTheRight(rigaRe, colonnaRe, s))
				{
					if(this.checkBlackCanArrive(4, 3, s))
					{
						return true;
					}
				}
				//bloccato sopra, sotto, sinistra
				if(this.enemyOnTheTop(rigaRe, colonnaRe, s) && this.enemyOnTheBottom(rigaRe, colonnaRe, s) && this.enemyOnTheLeft(rigaRe, colonnaRe, s))
				{
					if(this.checkBlackCanArrive(4, 5, s))
					{
						return true;
					}
				}
			} //Controllo di cattura con il re adiacente al trono
			else if(rigaRe==3 && colonnaRe==4 || rigaRe==5 && colonnaRe==4 || rigaRe==4 && colonnaRe==5 || rigaRe==4 && colonnaRe==3) 
			{
				//controllo casella adiacente sopra
				if(rigaRe==3 && colonnaRe==4)
				{
					//bloccato sopra e a destra
					if(this.enemyOnTheTop(rigaRe, colonnaRe, s) && this.enemyOnTheRight(rigaRe, colonnaRe, s))
					{
						if(this.checkBlackCanArrive(3, 3, s))
						{
							return true;
						}
					}
					//bloccato sinistra e destra
					if(this.enemyOnTheLeft(rigaRe, colonnaRe, s) && this.enemyOnTheRight(rigaRe, colonnaRe, s))
					{
						if(this.checkBlackCanArrive(2, 4, s))
						{
							return true;
						}
					}
					//bloccato sopra e a sinistra
					if(this.enemyOnTheTop(rigaRe, colonnaRe, s) && this.enemyOnTheLeft(rigaRe, colonnaRe, s))
					{
						if(this.checkBlackCanArrive(3, 5, s))
						{
							return true;
						}
					}
				}
				//controllo casella adiacente sotto
				if(rigaRe==5 && colonnaRe==4)
				{
					//bloccato destra e sinistra
					if(this.enemyOnTheLeft(rigaRe, colonnaRe, s) && this.enemyOnTheRight(rigaRe, colonnaRe, s))
					{
						if(this.checkBlackCanArrive(6, 4, s))
						{
							return true;
						}
					}
					//bloccato sotto e a destra
					if(this.enemyOnTheBottom(rigaRe, colonnaRe, s) && this.enemyOnTheRight(rigaRe, colonnaRe, s))
					{
						if(this.checkBlackCanArrive(5, 3, s))
						{
							return true;
						}
					}
					//bloccato sotto e a sinistra
					if(this.enemyOnTheBottom(rigaRe, colonnaRe, s) && this.enemyOnTheLeft(rigaRe, colonnaRe, s))
					{
						if(this.checkBlackCanArrive(5, 5, s))
						{
							return true;
						}
					}
				}
				//controllo casella adiacente destra
				if(rigaRe==4 && colonnaRe==5)
				{
					//bloccato sotto e a destra
					if(this.enemyOnTheBottom(rigaRe, colonnaRe, s) && this.enemyOnTheRight(rigaRe, colonnaRe, s))
					{
						if(this.checkBlackCanArrive(3, 5, s))
						{
							return true;
						}
					}
					//bloccato sopra e a destra
					if(this.enemyOnTheTop(rigaRe, colonnaRe, s) && this.enemyOnTheRight(rigaRe, colonnaRe, s))
					{
						if(this.checkBlackCanArrive(5, 5, s))
						{
							return true;
						}
					}
					//bloccato sopra e sotto
					if(this.enemyOnTheTop(rigaRe, colonnaRe, s) && this.enemyOnTheBottom(rigaRe, colonnaRe, s))
					{
						if(this.checkBlackCanArrive(4, 6, s))
						{
							return true;
						}
					}			
				}
				//controllo casella adiacente sinistra
				if(rigaRe==4 && colonnaRe==3)
				{
					//bloccato sopra e sotto
					if(this.enemyOnTheTop(rigaRe, colonnaRe, s) && this.enemyOnTheBottom(rigaRe, colonnaRe, s))
					{
						if(this.checkBlackCanArrive(4, 2, s))
						{
							return true;
						}
					}	
					//bloccato sotto e a sinistra
					if(this.enemyOnTheBottom(rigaRe, colonnaRe, s) && this.enemyOnTheLeft(rigaRe, colonnaRe, s))
					{
						if(this.checkBlackCanArrive(3, 3, s))
						{
							return true;
						}
					}//bloccato sopra e a sinistra
					if(this.enemyOnTheTop(rigaRe, colonnaRe, s) && this.enemyOnTheLeft(rigaRe, colonnaRe, s))
					{
						if(this.checkBlackCanArrive(5, 3, s))
						{
							return true;
						}
					}			
				}
			} //Controllo di cattura con il re lontano dal trono
			else
			{
				if(this.enemyOnTheRight(rigaRe, colonnaRe, s))
				{
					if(checkBlackCanArrive(rigaRe, colonnaRe-1, s))
					{
						return true;
					}
				}
				if(this.enemyOnTheLeft(rigaRe, colonnaRe, s))
				{
					if(checkBlackCanArrive(rigaRe, colonnaRe+1, s))
					{
						return true;
					}
				}
				if(this.enemyOnTheTop(rigaRe, colonnaRe, s))
				{
					if(checkBlackCanArrive(rigaRe+1, colonnaRe, s))
					{
						return true;
					}
				}
				if(this.enemyOnTheBottom(rigaRe, colonnaRe, s))
				{
					if(checkBlackCanArrive(rigaRe-1, colonnaRe, s))
					{
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/*
	 * Controlla se il re ha vie di fuga. Inizialmente le vie sono 4, e per ogni pedina nera su una delle possibili vie di fuga,
	 * queste diminuiscono (-1). Se non ci sono pedine nere su quella particolare riga/colonna, allora la funzione get in questione
	 * ritorna 0, non andando a modificare il numero di vie di fuga disponibili
	 */
	/**
	 * Controlla se il re ha vie di fuga. Inizialmente le vie sono 4, e per ogni pedina nera su una delle possibili vie di fuga,
	 * queste diminuiscono (-1). Se non ci sono pedine nere su quella particolare riga/colonna, allora la funzione getViaDiFugaFromLATO in questione
	 * ritorna 0, non andando a modificare il numero di vie di fuga disponibili
	 * @param rigaRe Riga in cui si trova il re, al momento della valutazione
	 * @param colonnaRe Colonna in cui si trova il re, al momento della valutazione
	 * @param s StateTablut ovvero stato che si vuole valutare
	 * @return Numero di vie di fuga disponibili per il re. Essendo 4 le mosse che il re può fare, questa funzione restituisce un numero compreso tra [0, 4]
	 */
	private int checkVieDiFugaRe(int rigaRe, int colonnaRe, StateTablut s)
	{
		int vieDiFuga=4;
		
		vieDiFuga += getViaDiFugaFromBottom(rigaRe, colonnaRe, s);
		vieDiFuga += getViaDiFugaFromTop(rigaRe, colonnaRe, s);
		vieDiFuga += getViaDiFugaFromRight(rigaRe, colonnaRe, s);
		vieDiFuga += getViaDiFugaFromLeft(rigaRe, colonnaRe, s);
		
		return vieDiFuga;
	}
	
	/**
	 * Controlla se esiste almeno una pedina nera nella riga sotto la riga in cui si trova il re
	 * 
	 * @param rigaRe Riga in cui si trova il re, al momento della valutazione
	 * @param colonnaRe Colonna in cui si trova il re, al momento della valutazione
	 * @param s StateTablut ovvero stato che si vuole valutare
	 * @return int: -1 se la via di fuga non è disponibile, 0 se invece è disponibile
	 */
	private int getViaDiFugaFromBottom(int rigaRe, int colonnaRe, StateTablut s) {
		for (int i=rigaRe+1; i<9; i++) {
			if(!s.getPawn(i, colonnaRe).equalsPawn("O") || this.citadels.contains(s.getBox(i, colonnaRe)))
			{
				return -1;
			}
		}
		
		return 0;
	}

	/**
	 * Controlla se esiste almeno una pedina nera nella riga sopra la riga in cui si trova il re
	 * 
	 * @param rigaRe Riga in cui si trova il re, al momento della valutazione
	 * @param colonnaRe Colonna in cui si trova il re, al momento della valutazione
	 * @param s StateTablut ovvero stato che si vuole valutare
	 * @return int: -1 se la via di fuga non è disponibile, 0 se invece è disponibile
	 */
	private int getViaDiFugaFromTop(int rigaRe, int colonnaRe, StateTablut s) {
		for(int i=rigaRe-1; i>=0; i--)
		{
			if(!s.getPawn(i, colonnaRe).equalsPawn("O") || this.citadels.contains(s.getBox(i, colonnaRe)))
			{
				return -1;
			}
		}
		return 0;
	}

	/**
	 * Controlla se esiste almeno una pedina nera nella colonna a destra della colonna in cui si trova il re
	 * 
	 * @param rigaRe Riga in cui si trova il re, al momento della valutazione
	 * @param colonnaRe Colonna in cui si trova il re, al momento della valutazione
	 * @param s StateTablut ovvero stato che si vuole valutare
	 * @return int: -1 se la via di fuga non è disponibile, 0 se invece è disponibile
	 */
	private int getViaDiFugaFromRight(int rigaRe, int colonnaRe, StateTablut s) {
		for(int i=colonnaRe+1; i<9; i++)
		{
			if(!s.getPawn(rigaRe, i).equalsPawn("O") || this.citadels.contains(s.getBox(rigaRe, i)))
			{
				return -1;
			}
		}
		
		return 0;
	}

	/**
	 * Controlla se esiste almeno una pedina nera nella colonna a sinistra della colonna in cui si trova il re
	 * 
	 * @param rigaRe Riga in cui si trova il re, al momento della valutazione
	 * @param colonnaRe Colonna in cui si trova il re, al momento della valutazione
	 * @param s StateTablut ovvero stato che si vuole valutare
	 * @return int: -1 se la via di fuga non è disponibile, 0 se invece è disponibile
	 */
	private int getViaDiFugaFromLeft(int rigaRe, int colonnaRe, StateTablut s) {
		for(int i=colonnaRe-1; i>=0; i--)
		{
			if(!s.getPawn(rigaRe, i).equalsPawn("O") || this.citadels.contains(s.getBox(rigaRe, i)))
			{
				return -1;
			}
		}
		return 0;
	}
	
	private boolean checkBlackCanArrive(int riga, int colonna, StateTablut s)
	{
		return checkBlackCanArriveFromBottom(riga, colonna, s) && 
				checkBlackCanArriveFromTop(riga, colonna, s) && 
				checkBlackCanArriveFromRight(riga, colonna, s) && 
				checkBlackCanArriveFromLeft(riga, colonna, s);
	}
	
	/*
	 * Controlla se esiste un nero che possa arrivare, dal basso, adiacente alla pedina passata come parametro
	 */
	//TODO:verificare se il secondo if e' utile oppure no
	private boolean checkBlackCanArriveFromBottom(int riga, int colonna, StateTablut s) {
		for(int i=riga+1; i<9;i++)
		{
			if(s.getPawn(riga+i, colonna).equalsPawn("B"))
			{
				return true;
			}
			if(s.getPawn(riga+i, colonna).equalsPawn("W") || s.getPawn(riga+i, colonna).equalsPawn("T") || this.citadels.contains(s.getBox(riga+i, colonna)))
			{
				return false;
			}			
		}
		
		return false;
	}
	
	/*
	 * Controlla se esiste un nero che possa arrivare, dall'alto, adiacente alla pedina passata come parametro
	 */
	private boolean checkBlackCanArriveFromTop(int riga, int  colonna, StateTablut s) {
		for(int i=riga-1; i>=0;i--)
		{
			if(s.getPawn(riga-i, colonna).equalsPawn("B"))
			{
				return true;
			}
			if(s.getPawn(riga-i, colonna).equalsPawn("W") || s.getPawn(riga-i, colonna).equalsPawn("T") || this.citadels.contains(s.getBox(riga-i, colonna)))
			{
				return false;
			}			
		}
		
		return false;
	}
	
	/*
	 * Controlla se esiste un nero che possa arrivare, da destra, adiacente alla pedina passata come parametro
	 */
	//TODO:verificare se il secondo if e' utile oppure no
	private boolean checkBlackCanArriveFromRight(int riga, int colonna, StateTablut s) {
		for(int i=colonna+1; i<9;i++)
		{
			if(s.getPawn(riga, colonna+i).equalsPawn("B"))
			{
				return true;
			}
			if(s.getPawn(riga, colonna+i).equalsPawn("W") || s.getPawn(riga, colonna+i).equalsPawn("T") || this.citadels.contains(s.getBox(riga, colonna+i)))
			{
				return false;
			}			
		}
		
		return false;
	}
	
	/*
	 * Controlla se esiste un nero che possa arrivare, da sinistra, adiacente alla pedina passata come parametro
	 */
	//TODO:verificare se il secondo if e' utile oppure no
	private boolean checkBlackCanArriveFromLeft(int riga, int colonna, StateTablut s) {
		for(int i=colonna-1; i>=0;i--)
		{
			if(s.getPawn(riga, colonna-i).equalsPawn("B"))
			{
				return true;
			}
			if(s.getPawn(riga, colonna-i).equalsPawn("W") || s.getPawn(riga, colonna-i).equalsPawn("T") || this.citadels.contains(s.getBox(riga, colonna-i)))
			{
				return false;
			}			
		}
		
		return false;
	}

	/**
	 * Controlla se esiste un nemico o un accampamento alla destra di una pedina bianca posizionata in @riga e @colonna
	 * 
	 * @param riga Riga in cui si trova la pedina
	 * @param colonna Colonna in cui si trova la pedina
	 * @param s StateTablut ovvero lo stato da valutare
	 * @return true se una pedina nera o un accampamento è accando, sulla destra, alla pedina bianca passata come parametro, false in caso contrario
	 */
	private boolean enemyOnTheRight(int riga, int colonna, StateTablut s)
	{
		if(s.getPawn(riga, colonna+1).equalsPawn("B") || this.citadels.contains(s.getBox(riga,  colonna+1)))
		{
			return true;
		}
		return false;
	}

	/**
	 * Controlla se esiste un nemico o un accampamento alla sinistra di una pedina bianca posizionata in @riga e @colonna
	 * 
	 * @param riga Riga in cui si trova la pedina
	 * @param colonna Colonna in cui si trova la pedina
	 * @param s StateTablut ovvero lo stato da valutare
	 * @return true se una pedina nera o un accampamento è accando, sulla sinistra, alla pedina bianca passata come parametro, false in caso contrario
	 */
	private boolean enemyOnTheLeft(int riga, int colonna, StateTablut s)
	{
		if(s.getPawn(riga, colonna-1).equalsPawn("B") || this.citadels.contains(s.getBox(riga,  colonna-1)))
		{
			return true;
		}
		return false;
	}
	
	/**
	 * Controlla se esiste un nemico o un accampamento in alto rispetto alla pedina bianca posizionata in @riga e @colonna
	 * 
	 * @param riga Riga in cui si trova la pedina
	 * @param colonna Colonna in cui si trova la pedina
	 * @param s StateTablut ovvero lo stato da valutare
	 * @return true se una pedina nera o un accampamento è sopra alla pedina bianca passata come parametro, false in caso contrario
	 */
	private boolean enemyOnTheTop(int riga, int colonna, StateTablut s)
	{
		if(s.getPawn(riga-1, colonna).equalsPawn("B") || this.citadels.contains(s.getBox(riga-1,  colonna)))
		{
			return true;
		}
		return false;
	}
	
	/**
	 * Controlla se esiste un nemico o un accampamento in basso (sotto) rispetto alla pedina bianca posizionata in @riga e @colonna
	 * 
	 * @param riga Riga in cui si trova la pedina
	 * @param colonna Colonna in cui si trova la pedina
	 * @param s StateTablut ovvero lo stato da valutare
	 * @return true se una pedina nera o un accampamento è sotto alla pedina bianca passata come parametro, false in caso contrario
	 */
	private boolean enemyOnTheBottom(int riga, int colonna, StateTablut s)
	{
		if(s.getPawn(riga+1, colonna).equalsPawn("B") || this.citadels.contains(s.getBox(riga+1,  colonna)))
		{
			return true;
		}
		return false;
	}
	
	/**
	 * Funzione che controlla se il re, muovendosi di una o più mosse da sinistra a destra (orizzontale), arriva ad avere un'intera colonna libera, in cui vincere
	 * 
	 * @param rigaRe Riga in cui si trova il re
	 * @param colonnaRe Colonna in cui si trova il re
	 * @param s StateTablut ovvero lo stato da valutare
	 * @return true se il re, spostandosi orizzontalmente da sinistra a destra, si porta in una situazione in cui non ha nessun ostacolo sopra e sotto di lui
	 */
	private boolean checkFreeColComingFromLeft(int rigaRe, int colonnaRe, StateTablut s) {
		for(int i=colonnaRe+1; i==6;i++)
		{
			if(s.getPawn(rigaRe, colonnaRe+i).equalsPawn("B") || s.getPawn(rigaRe, colonnaRe+i).equalsPawn("W") || s.getPawn(rigaRe, colonnaRe+i).equalsPawn("T") || this.citadels.contains(s.getBox(rigaRe,  colonnaRe+i))){
				return false; //c'e' una pedina nera/bianca che intralcia la mossa o c'e' il trono
			}//il caso della cittadella si verifica solo nella riga 1 e nella riga 7		
		}
		
		colonnaRe = 6; //colonna libera con possibilita' di vittoria
		
		if(!s.getPawn(rigaRe, 7).equalsPawn("B") && //se nella colonna 7, alla destra del re, non c'e' un nero
				!this.citadels.contains(s.getBox(rigaRe, 7)) && //se nella colonna 7, alla destra del re c'e' una cittadella
				checkFreeColTop(rigaRe, colonnaRe, s) && 
				checkFreeColBottom(rigaRe, colonnaRe, s)){
			return true;
		}
		
		return checkFreeColTop(rigaRe, colonnaRe, s) && //nessuno ostacolo nella colonna in cui il re si e' posizionato
				checkFreeColBottom(rigaRe, colonnaRe, s) && //nessuno ostacolo nella colonna in cui il re si e' posizionato
				!checkBlackCanArriveFromTop(rigaRe, colonnaRe-1, s) && //nessun nero puo' arrivare  da sopra, alla sinistra del re, per chiuderlo
				!checkBlackCanArriveFromBottom(rigaRe, colonnaRe-1, s) && //nessun nero puo' arrivare  da sotto, alla sinistra del re, per chiuderlo
				!checkBlackCanArriveFromLeft(rigaRe, colonnaRe, s); //nessun nero puo' arrivare dalla stessa riga in cui e' il re
		//checkBlack deve essere falso per far ritornare true il return
	}
	
	/**
	 * Funzione che controlla se il re, muovendosi di una o più mosse da destra a sinistra (orizzontale), arriva ad avere un'intera colonna libera, in cui vincere
	 * 
	 * @param rigaRe Riga in cui si trova il re
	 * @param colonnaRe Colonna in cui si trova il re
	 * @param s StateTablut ovvero lo stato da valutare
	 * @return true se il re, spostandosi orizzontalmente da destra a sinistra, si porta in una situazione in cui non ha nessun ostacolo sopra e sotto di lui
	 */
	private boolean checkFreeColComingFromRight(int rigaRe, int colonnaRe, StateTablut s) {
		for(int i=colonnaRe-1; i==2;i--)
		{
			if(s.getPawn(rigaRe, colonnaRe-i).equalsPawn("B") || s.getPawn(rigaRe, colonnaRe-i).equalsPawn("W") || s.getPawn(rigaRe, colonnaRe-i).equalsPawn("T") || this.citadels.contains(s.getBox(rigaRe, colonnaRe-i))){
				return false; //c'e' una pedina nera/bianca che intralcia la mossa o c'e' il trono
			}//il caso della cittadella si verifica solo nella riga 1 e nella riga 7		
		}
		
		colonnaRe = 2; //colonna libera con possibilita' di vittoria
		
		if(!s.getPawn(rigaRe, 1).equalsPawn("B") && 
				!this.citadels.contains(s.getBox(rigaRe, 1)) && 
				checkFreeColTop(rigaRe, colonnaRe, s) && 
				checkFreeColBottom(rigaRe, colonnaRe, s)){
			return true;
		}
		
		return checkFreeColTop(rigaRe, colonnaRe, s) && //nessun ostacolo nella colonna in cui il re si e' posizionato
				checkFreeColBottom(rigaRe, colonnaRe, s) && //nessun ostacolo nella colonna in cui il re si e' posizionato
				!checkBlackCanArriveFromTop(rigaRe, colonnaRe+1, s) && //nessun nero puo' arrivare alla destra del re, venendo dall'alto
				!checkBlackCanArriveFromBottom(rigaRe, colonnaRe+1, s) && //nessun nero puo' arrivare alla destra del re, venendo dal basso
				!checkBlackCanArriveFromRight(rigaRe, colonnaRe, s); //nessun nero puo' arrivare alla destra del re, venendo dalla sua destra (stessa riga
		//checkBlack deve essere falso per far ritornare true il return;
	}

	/**
	 * Funzione che controlla se il re, muovendosi di una o più mosse dall'alto al basso (verticale), arriva ad avere un'intera riga libera, in cui vincere
	 * 
	 * @param rigaRe Riga in cui si trova il re
	 * @param colonnaRe Colonna in cui si trova il re
	 * @param s StateTablut ovvero lo stato da valutare
	 * @return true se il re, spostandosi verticalmente dall'alto al basso, si porta in una situazione in cui non ha nessun ostacolo a sinistra e a destra di lui
	 */
	private boolean checkFreeRowComingFromTop(int rigaRe, int colonnaRe, StateTablut s){
		for(int i=rigaRe+1; i==6;i++)
		{
			if(s.getPawn(rigaRe+i, colonnaRe).equalsPawn("B") || s.getPawn(rigaRe+i, colonnaRe).equalsPawn("W") || s.getPawn(rigaRe+i, colonnaRe).equalsPawn("T") || this.citadels.contains(s.getBox(rigaRe+i, colonnaRe)))
			{
				return false;
			} // il caso in cui sia la cittadella ad intralciare la mossa del re, e' il caso in cui il re si muova lungo la colonna 1 o 7
		}

		rigaRe = 6;

		if(!s.getPawn(7, colonnaRe).equalsPawn("B") && //la riga 7 non contiene un nero
			!this.citadels.contains(s.getBox(7, colonnaRe)) && //la riga 7 non contiene la cittadella (quindi siamo nella colonna 4)
			checkFreeRowLeft(rigaRe, colonnaRe, s) && //controlla che tutta la riga, dalla parte a sx del re, sia libera
			checkFreeRowRight(rigaRe, colonnaRe, s)){ //controlla che tutta la riga, dalla parte a dx del re, sia libera
			return true;
		}	
		
		return checkFreeRowLeft(rigaRe, colonnaRe, s) &&
				checkFreeRowRight(rigaRe, colonnaRe, s) && 
				!checkBlackCanArriveFromTop(rigaRe, colonnaRe, s) &&  //nessun nero puo' arrivare e chiudere da sopra il re
				!checkBlackCanArriveFromLeft(rigaRe-1, colonnaRe, s) && //nessun nero puo' arrivare a chiudere il re, provendendo da sinistra, nella riga precedente quella il posizionamento del re
				!checkBlackCanArriveFromRight(rigaRe-1, colonnaRe, s); //nessun nero puo' arrivare a chiudere il re, provendendo da destra, nella riga precedente quella il posizionamento del re
	}
	
	/**
	 * Funzione che controlla se il re, muovendosi di una o più mosse dal basso all' alto (verticale), arriva ad avere un'intera riga libera, in cui vincere
	 * 
	 * @param rigaRe Riga in cui si trova il re
	 * @param colonnaRe Colonna in cui si trova il re
	 * @param s StateTablut ovvero lo stato da valutare
	 * @return true se il re, spostandosi verticalmente dal basso all' alto, si porta in una situazione in cui non ha nessun ostacolo a sinistra e a destra di lui
	 */
	private boolean checkFreeRowComingFromBottom(int rigaRe, int colonnaRe, StateTablut s){
		for(int i=rigaRe-1; i==2; i++){
			if(s.getPawn(rigaRe-i, colonnaRe).equalsPawn("B") || s.getPawn(rigaRe-i, colonnaRe).equalsPawn("W") || s.getPawn(rigaRe-i, colonnaRe).equalsPawn("T") || this.citadels.contains(s.getBox(rigaRe-i, colonnaRe))) {
				return false;
			} // il caso in cui sia la cittadella ad intralciare la mossa del re, e' il caso in cui il re si muova lungo la colonna 1 o 7
		}

		rigaRe = 2;

		if(!s.getPawn(1, colonnaRe).equalsPawn("B") && //la riga 1, stessa colonna del re, non contiene un nero
			!this.citadels.contains(s.getBox(rigaRe, 1)) && //la riga 1, stessa colonna del re, non contiene una cittadella (quindi siamo nella colonna 4)
			checkFreeRowLeft(rigaRe, colonnaRe, s) && //controlla che tutta la riga, dalla parte a sx del re, sia libera
			checkFreeRowRight(rigaRe, colonnaRe, s)){ //controlla che tutta la riga, dalla parte a sx del re, sia libera
			return true;
		}

		return checkFreeRowLeft(rigaRe, colonnaRe, s) && 
				checkFreeRowRight(rigaRe, colonnaRe, s) && 
				!checkBlackCanArriveFromBottom(rigaRe, colonnaRe, s) && 
				!checkBlackCanArriveFromLeft(rigaRe+1, colonnaRe, s) &&
				!checkBlackCanArriveFromRight(rigaRe+1, colonnaRe, s);
	}

	/**
	 * Controlla se data una cella, tutte le celle alla destra sono libere (non occupate da bianchi, neri, trono o cittadelle)
	 * 
	 * @param rigaRe Riga in cui si trova la cella da valutare
	 * @param colonnaRe Colonna in cui si trova la cella da valutare
	 * @param s StateTablut ovvero lo stato da valutare
	 * @return true se non ci sono elementi alla destra della cella specificata da @riga + @colonna, false in caso contrario
	 */
	private boolean checkFreeRowRight(int riga, int colonna, StateTablut s) {
		for(int i=colonna+1; i<=9; i++){
			if(s.getPawn(riga, colonna+i).equalsPawn("B") || s.getPawn(riga, colonna+i).equalsPawn("W") || s.getPawn(riga, colonna+i).equalsPawn("T") || this.citadels.contains(s.getBox(riga, colonna+i))) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Controlla se data una cella, tutte le celle alla sinsitra sono libere (non occupate da bianchi, neri, trono o cittadelle)
	 * 
	 * @param rigaRe Riga in cui si trova la cella da valutare
	 * @param colonnaRe Colonna in cui si trova la cella da valutare
	 * @param s StateTablut ovvero lo stato da valutare
	 * @return true se non ci sono elementi alla sinistra della cella specificata da @riga + @colonna, false in caso contrario
	 */
	private boolean checkFreeRowLeft(int riga, int colonna, StateTablut s) {
		for(int i=colonna-1; i>=0; i--) {
			if(s.getPawn(riga, colonna-i).equalsPawn("B") || s.getPawn(riga, colonna-i).equalsPawn("W") || s.getPawn(riga, colonna-i).equalsPawn("T") || this.citadels.contains(s.getBox(riga, colonna-i))) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Controlla se data una cella, tutte le celle sopra di essa sono libere (non occupate da bianchi, neri, trono o cittadelle)
	 * 
	 * @param rigaRe Riga in cui si trova la cella da valutare
	 * @param colonnaRe Colonna in cui si trova la cella da valutare
	 * @param s StateTablut ovvero lo stato da valutare
	 * @return true se non ci sono elementi sopra la cella specificata da @riga + @colonna, false in caso contrario
	 */
	private boolean checkFreeColTop(int rigaRe, int colonnaRe, StateTablut s) {
		
		for(int i=rigaRe-1; i>=0; i--) {
			if(s.getPawn(rigaRe-i, colonnaRe).equalsPawn("B") || s.getPawn(rigaRe-i, colonnaRe).equalsPawn("W")) {
				return false;
			}
		}
		
		return true;
		
	}
	
	/**
	 * Controlla se data una cella, tutte le celle sotto di essa sono libere (non occupate da bianchi, neri, trono o cittadelle)
	 * 
	 * @param rigaRe Riga in cui si trova la cella da valutare
	 * @param colonnaRe Colonna in cui si trova la cella da valutare
	 * @param s StateTablut ovvero lo stato da valutare
	 * @return true se non ci sono elementi sotto la cella specificata da @riga + @colonna, false in caso contrario
	 */
	private boolean checkFreeColBottom(int rigaRe, int colonnaRe, StateTablut s) {
		for(int i=rigaRe+1; i<=9; i++) {
			if(s.getPawn(rigaRe+i, colonnaRe).equalsPawn("B") || s.getPawn(rigaRe+i, colonnaRe).equalsPawn("W")) {
				return false;
			}
		}
		
		return true;
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
		
		//QUESTA e' SOLO UNA PROVA PER VEDERE SE EFFETTIVAMENTE IL NOSTRO GIOCATORE FUNZIONA
		//RISULTATO POSITIVO
		Action a = null;
		int betterValue=-100000;
		try {
			Nodo node = new Nodo(s);
			for(Nodo n : this.simulatore.mossePossibiliComplete(node))
			{
				if(this.getHeuristicValueOfState(n.getStato())>=betterValue)
				{
					a = n.getAzione();
				}
			}
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return a;
	}

}

