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
	 * Funzione di euristica, di prova <-- da modificare
	 */
	@Override
	public int getHeuristicValueOfState(StateTablut s) {
		return this.getNumberWhite(s) - this.getNumberBlack(s) + 2*this.getNumberStarFree(s);
	}
	
	/*
	 * Ritorna il numero di pedine bianche, compreso il re, ancora sul tabellone
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
	
	/*
	 * Ritorna il numero di pedine nere, ancora sul tabellone
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
	
	/*
	 * Ritorna il numero di colonne (o semicolonne) e righe (o semirighe) libere, al termine delle quali c'e' la casella blu che permette al bianco di vincere
	 * 
	 * semicolonna = per semicolonna si intendono le colonne 1 e 7 che sono interrotte da una cittadella
	 * 
	 * semiriga = per semiriga si intendono le righe 1 e 7 che sono interrotte da una cittadella
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
	
	/*
	 * Ritorna true se la colonna passata come parametro e' libera
	 * 
	 * Controlla di fatto solo le colonne 2 e 6, per altre colonne ritorna sicuramente false
	 */
	private boolean isColumnFree(int numberCol, StateTablut s) {
		
		boolean result = true;
		
		for(int i=0; i<9; i++) {
			if(s.getPawn(i, numberCol).equalsPawn("O")) {
				result = result && true;
			} else result = false;
		}
		
		return result;
	}
	
	/*
	 * Ritorna true se la riga passata come parametro e' libera
	 * 
	 * Controlla di fatto solo le righe 2 e 6, per altre righe ritorna sicuramente false
	 */
	private boolean isRowFree(int numberRow, StateTablut s) {
		
		boolean result = true;
		
		for(int i=0; i<9; i++) {
			if(s.getPawn(numberRow, i).equalsPawn("O")) {
				result = result && true;
			} else result = false;
		}
		
		return result;
	}
	
	/*
	 * Ritorna true se la riga passata come parametro e' libera
	 * 
	 * Controlla di fatto solo le righe 1 e 7, per altre righe ritorna sicuramente false
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
	
	/*
	 * Ritorna true se la colonna passata come parametro e' libera
	 * 
	 * Controlla di fatto solo le colonne 1 e 7, per altre righe ritorna sicuramente false
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
	
	/*
	 * Controlla la possibile cattura del re da parte dei neri, ritorna true se il re puo' essere catturato o false nel caso contrario
	 * Dopo aver controllato che sia il turno del nero la funzione distingue i tre casi di cattura:
	 * -Re sul trono e quindi servono 4 pedine per la cattura
	 * -Re adiacente al trono e quindi servono 3 pedine per la cattura
	 * -Re lontano dal trono e quindi servono 2 pedine per la cattura
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
	private int checkVieDiFugaRe(int rigaRe, int colonnaRe, StateTablut s)
	{
		int vieDiFuga=4;
		
		vieDiFuga += getViaDiFugaFromBottom(rigaRe, colonnaRe, s);
		vieDiFuga += getViaDiFugaFromTop(rigaRe, colonnaRe, s);
		vieDiFuga += getViaDiFugaFromRight(rigaRe, colonnaRe, s);
		vieDiFuga += getViaDiFugaFromLeft(rigaRe, colonnaRe, s);
		
		return vieDiFuga;
	}
	
	/*
	 * Controlla se esiste almeno una pedina nera nella riga sotto la riga in cui si trova il re
	 * 
	 * Return: ritorna -1 se la via di fuga non è disponibile, 0 se invece è disponibile
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
	
	/*
	 * Controlla se esiste almeno una pedina nera nella riga sopra la riga in cui si trova il re
	 * 
	 * Return: ritorna -1 se la via di fuga non è disponibile, 0 se invece è disponibile
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
	
	/*
	 * Controlla se esiste almeno una pedina nera nella colonna a destra della colonna in cui si trova il re
	 * 
	 * Return: ritorna -1 se la via di fuga non è disponibile, 0 se invece è disponibile
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
	
	/*
	 * Controlla se esiste almeno una pedina nera nella colonna a sinistra della colonna in cui si trova il re
	 * 
	 * Return: ritorna -1 se la via di fuga non è disponibile, 0 se invece è disponibile
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

	/*
	 * Controlla se e' presente una pedina nera(o un accampamento) adiacente a destra
	 */
	private boolean enemyOnTheRight(int riga, int colonna, StateTablut s)
	{
		if(s.getPawn(riga, colonna+1).equalsPawn("B") || this.citadels.contains(s.getBox(riga,  colonna+1)))
		{
			return true;
		}
		return false;
	}
	
	/*
	 * Controlla se e' presente una pedina nera(o un accampamento) adiacente a sinistra
	 */
	private boolean enemyOnTheLeft(int riga, int colonna, StateTablut s)
	{
		if(s.getPawn(riga, colonna-1).equalsPawn("B") || this.citadels.contains(s.getBox(riga,  colonna-1)))
		{
			return true;
		}
		return false;
	}
	
	/*
	 * Controlla se e' presente una pedina nera(o un accampamento) adiacente in alto
	 */
	private boolean enemyOnTheTop(int riga, int colonna, StateTablut s)
	{
		if(s.getPawn(riga-1, colonna).equalsPawn("B") || this.citadels.contains(s.getBox(riga-1,  colonna)))
		{
			return true;
		}
		return false;
	}
	
	/*
	 * Controlla se e' presente una pedina nera(o un accampamento) adiacente in basso
	 */
	private boolean enemyOnTheBottom(int riga, int colonna, StateTablut s)
	{
		if(s.getPawn(riga+1, colonna).equalsPawn("B") || this.citadels.contains(s.getBox(riga+1,  colonna)))
		{
			return true;
		}
		return false;
	}
	
	/*
	 * Funzione che controlla se il re, muovendosi di una o più mosse da sinistra a destra (orizzontale), arriva ad avere un'intera colonna libera, in cui vincere
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
	
	/*
	 * Funzione che controlla se il re, muovendosi di una o più mosse da destra a sinistra (orizzontale), arriva ad avere un'intera colonna libera, in cui vincere
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
	
	/*
	* Funzione che controlla se il re, muovendosi di una o più mosse dall'alto in basso (verticale), arriva ad avere un'intera riga libera, in cui vincere
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
	
	/*
	* Funzione che controlla se il re, muovendosi di una o più mosse dal basso all'alto (verticale), arriva ad avere un'intera riga libera, in cui vincere
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
	
	/*
	 * Controlla se una data riga, alla destra della pedina, e' libera
	 */
	private boolean checkFreeRowRight(int riga, int colonna, StateTablut s) {
		for(int i=colonna+1; i<=9; i++){
			if(s.getPawn(riga, colonna+i).equalsPawn("B") || s.getPawn(riga, colonna+i).equalsPawn("W") || s.getPawn(riga, colonna+i).equalsPawn("T") || this.citadels.contains(s.getBox(riga, colonna+i))) {
				return false;
			}
		}

		return true;
	}

	/*
	 * Controlla se una data riga, alla sinsitra della pedina, e' libera
	 */
	private boolean checkFreeRowLeft(int riga, int colonna, StateTablut s) {
		for(int i=colonna-1; i>=0; i--) {
			if(s.getPawn(riga, colonna-i).equalsPawn("B") || s.getPawn(riga, colonna-i).equalsPawn("W") || s.getPawn(riga, colonna-i).equalsPawn("T") || this.citadels.contains(s.getBox(riga, colonna-i))) {
				return false;
			}
		}
		return true;
	}
	
	private boolean checkFreeColTop(int rigaRe, int colonnaRe, StateTablut s) {
		
		for(int i=rigaRe-1; i>=0; i--) {
			if(s.getPawn(rigaRe-i, colonnaRe).equalsPawn("B") || s.getPawn(rigaRe-i, colonnaRe).equalsPawn("W")) {
				return false;
			}
		}
		
		return true;
		
	}
	
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
		try {
		/*	if(this.isStart(s)) {
				System.out.println("FATTOOOOOOOOOOO");
			} else {
				System.out.println("La scacchiera non e' riempita");

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

