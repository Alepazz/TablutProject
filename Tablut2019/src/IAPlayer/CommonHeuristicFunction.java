package IAPlayer;

import java.util.ArrayList;
import java.util.List;

import it.unibo.ai.didattica.competition.tablut.domain.StateTablut;

/*
 * contiene tutte le funzioni che possono essere utilizzate sia per l'euristica bianca che per quella nera
 * */
public class CommonHeuristicFunction {
	private List<String> citadels;
		
	
	public CommonHeuristicFunction() {
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
	
	public boolean blackCannotBlockEscape(StateTablut s, int rigaRe, int colonnaRe) {
			
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
					if(this.checkPawnCanArrive(i, colonnaRe, "W", s))
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
					i=-1;
				}
			}
			if(i!=20)
			{
				for(i=rigaRe-1; i>=0; i--)
				{
					if(this.checkPawnCanArrive(i, colonnaRe, "W", s))
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
					i=20;
				}
			}
			if(i!=20)
			{
				for(i=colonnaRe+1; i<9; i++)
				{
					if(this.checkPawnCanArrive(rigaRe, i, "W", s))
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
					if(this.checkPawnCanArrive(rigaRe, i, "W", s))
					{
						return false;
					}
				}
			}
			
			return true;
		}
	
	
	//QUESTA MI PIACE POCO PERCHE NON RIUSCIAMO A CAPIRE DA UNA SINGOLA PASSATA QUANTE PEDINE CI SONO SUL TABELLONE E DOVE STA IL RE
	/**
	 * Restituisce il numero di pedine bianche presenti sul tabellone, dato lo stato s
	 * Nel conteggio è compreso anche il re
	 * @param s StateTablut rappresenta lo stato da valutare
	 * @return numero di pedine bianche, comprensive del re, presenti sulla scacchiera
	 */
	public int getNumberWhite(StateTablut s) {
		
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
	public int getNumberBlack(StateTablut s) {
		
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
	public int getNumberStarFree(StateTablut s) {
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
	public boolean isColumnFree(int numberCol, StateTablut s) {
		
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
	public boolean isRowFree(int numberRow, StateTablut s) {
		
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
	public boolean isSemirowFree(int numberRow, StateTablut s) {
		
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
	public boolean isSemicolumnFree(int numberColumn, StateTablut s) {
		
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
	public boolean kingCanBeCaptured(int rigaRe, int colonnaRe, StateTablut s)
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
					if(this.checkPawnCanArrive(5, 4, "W", s))
					{
						return true;
					}
				}
				//bloccato sotto, destra, sinistra
				if(this.enemyOnTheBottom(rigaRe, colonnaRe, s) && this.enemyOnTheRight(rigaRe, colonnaRe, s) && this.enemyOnTheLeft(rigaRe, colonnaRe, s))
				{
					if(this.checkPawnCanArrive(3, 4, "W", s))
					{
						return true;
					}
				}
				//bloccato sopra, sotto, destra
				if(this.enemyOnTheTop(rigaRe, colonnaRe, s) && this.enemyOnTheBottom(rigaRe, colonnaRe, s) && this.enemyOnTheRight(rigaRe, colonnaRe, s))
				{
					System.out.println("enemyOnLeft");
					if(this.checkPawnCanArrive(4, 3, "W", s))
					{
						return true;
					}
				}
				//bloccato sopra, sotto, sinistra
				if(this.enemyOnTheTop(rigaRe, colonnaRe, s) && this.enemyOnTheBottom(rigaRe, colonnaRe, s) && this.enemyOnTheLeft(rigaRe, colonnaRe, s))
				{
					if(this.checkPawnCanArrive(4, 5, "W", s))
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
						if(this.checkPawnCanArrive(3, 3, "W", s))
						{
							return true;
						}
					}
					//bloccato sinistra e destra
					if(this.enemyOnTheLeft(rigaRe, colonnaRe, s) && this.enemyOnTheRight(rigaRe, colonnaRe, s))
					{
						if(this.checkPawnCanArrive(2, 4, "W", s))
						{
							return true;
						}
					}
					//bloccato sopra e a sinistra
					if(this.enemyOnTheTop(rigaRe, colonnaRe, s) && this.enemyOnTheLeft(rigaRe, colonnaRe, s))
					{
						if(this.checkPawnCanArrive(3, 5, "W", s))
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
						if(this.checkPawnCanArrive(6, 4, "W", s))
						{
							return true;
						}
					}
					//bloccato sotto e a destra
					if(this.enemyOnTheBottom(rigaRe, colonnaRe, s) && this.enemyOnTheRight(rigaRe, colonnaRe, s))
					{
						if(this.checkPawnCanArrive(5, 3, "W", s))
						{
							return true;
						}
					}
					//bloccato sotto e a sinistra
					if(this.enemyOnTheBottom(rigaRe, colonnaRe, s) && this.enemyOnTheLeft(rigaRe, colonnaRe, s))
					{
						if(this.checkPawnCanArrive(5, 5, "W", s))
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
						if(this.checkPawnCanArrive(3, 5, "W", s))
						{
							return true;
						}
					}
					//bloccato sopra e a destra
					if(this.enemyOnTheTop(rigaRe, colonnaRe, s) && this.enemyOnTheRight(rigaRe, colonnaRe, s))
					{
						if(this.checkPawnCanArrive(5, 5, "W", s))
						{
							return true;
						}
					}
					//bloccato sopra e sotto
					if(this.enemyOnTheTop(rigaRe, colonnaRe, s) && this.enemyOnTheBottom(rigaRe, colonnaRe, s))
					{
						if(this.checkPawnCanArrive(4, 6, "W", s))
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
						if(this.checkPawnCanArrive(4, 2, "W", s))
						{
							return true;
						}
					}	
					//bloccato sotto e a sinistra
					if(this.enemyOnTheBottom(rigaRe, colonnaRe, s) && this.enemyOnTheLeft(rigaRe, colonnaRe, s))
					{
						if(this.checkPawnCanArrive(3, 3, "W", s))
						{
							return true;
						}
					}//bloccato sopra e a sinistra
					if(this.enemyOnTheTop(rigaRe, colonnaRe, s) && this.enemyOnTheLeft(rigaRe, colonnaRe, s))
					{
						if(this.checkPawnCanArrive(5, 3, "W", s))
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
					if(checkPawnCanArrive(rigaRe, colonnaRe-1, "W", s))
					{
						return true;
					}
				}
				if(this.enemyOnTheLeft(rigaRe, colonnaRe, s))
				{
					if(checkPawnCanArrive(rigaRe, colonnaRe+1, "W", s))
					{
						return true;
					}
				}
				if(this.enemyOnTheTop(rigaRe, colonnaRe, s))
				{
					if(checkPawnCanArrive(rigaRe+1, colonnaRe, "W", s))
					{
						return true;
					}
				}
				if(this.enemyOnTheBottom(rigaRe, colonnaRe, s))
				{
					if(checkPawnCanArrive(rigaRe-1, colonnaRe, "W", s))
					{
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public boolean checkBlackCanBeCaptured(int riga, int colonna, StateTablut s) {
		
		//sottointeso turno bianco
		
		if(this.checkPedinaIsolata(riga, colonna, s)) {
			return false; //il nero non può essere catturato
		}
		
		if(this.checkNeighbourTop(riga, colonna, s).equals("B") && this.checkNeighbourLeft(riga, colonna, s).equals("B")) {
			return false; // se la pedina ha due vicini neri, in due lati opposti, allora non può essere catturata
		}
		
		if(this.checkNeighbourTop(riga, colonna, s).equals("B") && this.checkNeighbourRight(riga, colonna, s).equals("B")) {
			return false; // se la pedina ha due vicini neri, in due lati opposti, allora non può essere catturata
		}
		
		if(this.checkNeighbourBottom(riga, colonna, s).equals("B") && this.checkNeighbourLeft(riga, colonna, s).equals("B")) {
			return false; // se la pedina ha due vicini neri, in due lati opposti, allora non può essere catturata
		}
		
		if(this.checkNeighbourBottom(riga, colonna, s).equals("B") && this.checkNeighbourRight(riga, colonna, s).equals("B")) {
			return false; // se la pedina ha due vicini neri, in due lati opposti, allora non può essere catturata
		}
		
		if(this.checkNeighbourTop(riga, colonna, s).equals("C") || this.checkNeighbourTop(riga, colonna, s).equals("W") || this.checkNeighbourTop(riga, colonna, s).equals("T")) {
			if(this.checkPawnCanArrive(riga, colonna, "B", s)) {
				return true;
			}
		}
		
		if(this.checkNeighbourBottom(riga, colonna, s).equals("C") || this.checkNeighbourBottom(riga, colonna, s).equals("W") || this.checkNeighbourBottom(riga, colonna, s).equals("T")) {
			if(this.checkPawnCanArrive(riga, colonna, "B", s)) {
				return true;
			}
		}
		
		if(this.checkNeighbourLeft(riga, colonna, s).equals("C") || this.checkNeighbourLeft(riga, colonna, s).equals("W") || this.checkNeighbourLeft(riga, colonna, s).equals("T")) {
			if(this.checkPawnCanArrive(riga, colonna, "B", s)) {
				return true;
			}
		}
		
		if(this.checkNeighbourRight(riga, colonna, s).equals("C") || this.checkNeighbourRight(riga, colonna, s).equals("W") || this.checkNeighbourRight(riga, colonna, s).equals("T")) {
			if(this.checkPawnCanArrive(riga, colonna, "B", s)) {
				return true;
			}
		}
		
		return false;
	}
	
	
	/**
	 * Controlla se una pedina passata come parametro può essere catturata
	 * @param riga Riga in cui si trova la pedina
	 * @param colonna Colonna in cui si trova la pedina
	 * @param s StateTablut ovvero lo stato da valutare
	 * @return true se la pedina può essere catturata, false in caso contrario
	 */
	public boolean checkWhiteCanBeCaptured(int riga, int colonna, StateTablut s) {
		
		//sottointeso turno nero
		
		if( this.checkPedinaIsolata(riga, colonna, s)) {
			return false; //se la pedina non ha vicini, non può essere catturata
		}
		
		if((this.checkNeighbourTop(riga, colonna, s).equals("W")|| this.checkNeighbourTop(riga, colonna, s).equals("K")) && (this.checkNeighbourLeft(riga, colonna, s).equals("W") || this.checkNeighbourTop(riga, colonna, s).equals("K"))) {
			return false; // se la pedina ha due vicini bianchi, in due lati opposti, allora non può essere catturata
		}
		
		if((this.checkNeighbourTop(riga, colonna, s).equals("W") || this.checkNeighbourTop(riga, colonna, s).equals("K")) && (this.checkNeighbourRight(riga, colonna, s).equals("W") || this.checkNeighbourTop(riga, colonna, s).equals("K"))) {
			return false; // se la pedina ha due vicini bianchi, in due lati opposti, allora non può essere catturata
		}
		
		if((this.checkNeighbourBottom(riga, colonna, s).equals("W") || this.checkNeighbourTop(riga, colonna, s).equals("K")) && (this.checkNeighbourLeft(riga, colonna, s).equals("W") || this.checkNeighbourTop(riga, colonna, s).equals("K"))) {
			return false; // se la pedina ha due vicini bianchi, in due lati opposti, allora non può essere catturata
		}
		
		if((this.checkNeighbourBottom(riga, colonna, s).equals("W") || this.checkNeighbourTop(riga, colonna, s).equals("K")) && (this.checkNeighbourRight(riga, colonna, s).equals("W") || this.checkNeighbourTop(riga, colonna, s).equals("K"))) {
			return false; // se la pedina ha due vicini bianchi, in due lati opposti, allora non può essere catturata
		}
		
		if(this.checkNeighbourTop(riga, colonna, s).equals("C") || this.checkNeighbourTop(riga, colonna, s).equals("B") || this.checkNeighbourTop(riga, colonna, s).equals("T")) {
			if(this.checkPawnCanArrive(riga, colonna, "W", s)) {
				return true;
			}
		}
		
		if(this.checkNeighbourBottom(riga, colonna, s).equals("C") || this.checkNeighbourBottom(riga, colonna, s).equals("B") || this.checkNeighbourBottom(riga, colonna, s).equals("T")) {
			if(this.checkPawnCanArrive(riga, colonna, "W", s)) {
				return true;
			}
		}
		
		if(this.checkNeighbourLeft(riga, colonna, s).equals("C") || this.checkNeighbourLeft(riga, colonna, s).equals("B") || this.checkNeighbourLeft(riga, colonna, s).equals("T")) {
			if(this.checkPawnCanArrive(riga, colonna, "W", s)) {
				return true;
			}
		}
		
		if(this.checkNeighbourRight(riga, colonna, s).equals("C") || this.checkNeighbourRight(riga, colonna, s).equals("B") || this.checkNeighbourRight(riga, colonna, s).equals("T")) {
			if(this.checkPawnCanArrive(riga, colonna, "W", s)) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Controlla se una pedina non ha vicini
	 * @param riga Riga in cui si trova la pedina
	 * @param colonna Colonna in cui si trova la pedina
	 * @param s StateTablut ovvero lo stato da valutare
	 * @return true se la pedina non ha vicini, false in caso contrario
	 */
	public boolean checkPedinaIsolata(int riga, int colonna, StateTablut s) {
		return (this.checkNeighbourTop(riga, colonna, s).equals("O") || this.checkNeighbourTop(riga, colonna, s).equals("X")) &&
				(this.checkNeighbourBottom(riga, colonna, s).equals("O") || this.checkNeighbourBottom(riga, colonna, s).equals("X")) &&
				(this.checkNeighbourLeft(riga, colonna, s).equals("O") || this.checkNeighbourLeft(riga, colonna, s).equals("X")) &&
				(this.checkNeighbourRight(riga, colonna, s).equals("O") || this.checkNeighbourRight(riga, colonna, s).equals("X"));
	}
	
	/**
	 * Controlla chi è il vicino superiore della pedina passata come parametro
	 * @param riga Riga della pedina
	 * @param colonna Colonna della pedina
	 * @param s StateTablut ovvero lo stato da valutare
	 * @return Una stringa indicante il tipo di vicino 
	 * -O = cella libera;
	 * -K = il re;
	 * -B = una pedina nera;
	 * -W = una pedina bianca;
	 * -T = il trono;
	 * -C = una cittadella;
	 * -X = cella fuori dalla tavola;
	 */
	public String checkNeighbourTop(int riga, int colonna, StateTablut s) {
		if(riga!=0) {
			if(s.getPawn(riga-1, colonna).equalsPawn("O") && !this.citadels.contains(s.getBox(riga-1, colonna))) {
				return "O"; //c'è una cella libera
			} else {
				if(s.getPawn(riga-1, colonna).equalsPawn("K")) {
					return "K"; //c'è il re
				}
				
				if(s.getPawn(riga-1, colonna).equalsPawn("B")) {
					return "B"; //c'è un nero
				}
				
				if(s.getPawn(riga-1, colonna).equalsPawn("W")) {
					return "W"; //c'è un bianco
				}
				
				if(s.getPawn(riga-1, colonna).equalsPawn("T")) {
					return "T"; //c'è il trono
				}
			}
			
			return "C"; //c'è la cittadella
		} else {
			return "X";
		}		
	}
	
	/**
	 * Controlla chi è il vicino inferiore della pedina passata come parametro
	 * @param riga Riga della pedina
	 * @param colonna Colonna della pedina
	 * @param s StateTablut ovvero lo stato da valutare
	 * @return Una stringa indicante il tipo di vicino 
	 * -O = cella libera;
	 * -K = il re;
	 * -B = una pedina nera;
	 * -W = una pedina bianca;
	 * -T = il trono;
	 * -C = una cittadella;
	 * -X = cella fuori dalla tavola;
	 */
	public String checkNeighbourBottom(int riga, int colonna, StateTablut s) {
		if(riga!=8) {
			if(s.getPawn(riga+1, colonna).equalsPawn("O") && !this.citadels.contains(s.getBox(riga+1, colonna))) {
				return "O"; //c'è una cella libera
			} else {
				if(s.getPawn(riga+1, colonna).equalsPawn("K")) {
					return "K"; //c'è il re
				}
				
				if(s.getPawn(riga+1, colonna).equalsPawn("B")) {
					return "B"; //c'è un nero
				}
				
				if(s.getPawn(riga+1, colonna).equalsPawn("W")) {
					return "W"; //c'è un bianco
				}
				
				if(s.getPawn(riga+1, colonna).equalsPawn("T")) {
					return "T"; //c'è il trono
				}
			}
			
			return "C"; //c'è la cittadella
		} else {
			return "X";
		}	
	}
	
	/**
	 * Controlla chi è il vicino sinistro della pedina passata come parametro
	 * @param riga Riga della pedina
	 * @param colonna Colonna della pedina
	 * @param s StateTablut ovvero lo stato da valutare
	 * @return Una stringa indicante il tipo di vicino 
	 * -O = cella libera;
	 * -K = il re;
	 * -B = una pedina nera;
	 * -W = una pedina bianca;
	 * -T = il trono;
	 * -C = una cittadella;
	 * -X = cella fuori dalla tavola;
	 */
	public String checkNeighbourLeft(int riga, int colonna, StateTablut s) {
		if(colonna!=0) {
			if(s.getPawn(riga, colonna-1).equalsPawn("O") && !this.citadels.contains(s.getBox(riga, colonna-1))) {
				return "O"; //c'è una cella libera
			} else {
				if(s.getPawn(riga, colonna-1).equalsPawn("K")) {
					return "K"; //c'è il re
				}
				
				if(s.getPawn(riga, colonna-1).equalsPawn("B")) {
					return "B"; //c'è un nero
				}
				
				if(s.getPawn(riga, colonna-1).equalsPawn("W")) {
					return "W"; //c'è un bianco
				}
				
				if(s.getPawn(riga, colonna-1).equalsPawn("T")) {
					return "T"; //c'è il trono
				}
			}
			
			return "C"; //c'è la cittadella
		} else {
			return "X";
		}		
	}
	
	/**
	 * Controlla chi è il vicino destro della pedina passata come parametro
	 * @param riga Riga della pedina
	 * @param colonna Colonna della pedina
	 * @param s StateTablut ovvero lo stato da valutare
	 * @return Una stringa indicante il tipo di vicino 
	 * -O = cella libera;
	 * -K = il re;
	 * -B = una pedina nera;
	 * -W = una pedina bianca;
	 * -T = il trono;
	 * -C = una cittadella;
	 * -X = cella fuori dalla tavola;
	 */
	public String checkNeighbourRight(int riga, int colonna, StateTablut s) {
		if(colonna!=8) {
			if(s.getPawn(riga, colonna+1).equalsPawn("O") && !this.citadels.contains(s.getBox(riga, colonna-1))) {
				return "O"; //c'è una cella libera
			} else {
				if(s.getPawn(riga, colonna+1).equalsPawn("K")) {
					return "K"; //c'è il re
				}
				
				if(s.getPawn(riga, colonna+1).equalsPawn("B")) {
					return "B"; //c'è un nero
				}
				
				if(s.getPawn(riga, colonna+1).equalsPawn("W")) {
					return "W"; //c'è un bianco
				}
				
				if(s.getPawn(riga, colonna+1).equalsPawn("T")) {
					return "T"; //c'è il trono
				}
			}
			
			return "C"; //c'è la cittadella
		} else {
			return "X";
		}		
	}
	
	/**
	 * Verifica dove è posizionato il re (metà superiore, riga centrale, metà inferiore)
	 * @param s StateTablut ovvero lo stato da valutare
	 * @return 0 se il re si trova nella metà superiore della scacchiera;
	 * 1 se il re si trova esattamente nella riga 5 della scacchiera;
	 * 2 se il re si trova nella metà inferiore della scacchiera
	 */
	public int kingInTop(StateTablut s) {
		for(int i=1; i<3; i++) {
			for(int j=1; j<8; j++) {
				if(s.getPawn(i, j).equalsPawn("K")){
					return 0; //il re è nella meta superiore
				}
			}
		}
		
		for(int i=0; i<9; i++) {
			if(s.getPawn(4, i).equalsPawn("K")) {
				return 1; //il re è nella riga 5(quella del trono)
			}
		}
		
		return 2; //il re è nella meta inferiore
	}
	
	/**
	 * Controlla se il re ha vie di fuga. Inizialmente le vie sono 4, e per ogni pedina nera su una delle possibili vie di fuga,
	 * queste diminuiscono (-1). Se non ci sono pedine nere su quella particolare riga/colonna, allora la funzione getViaDiFugaFromLATO in questione
	 * ritorna 0, non andando a modificare il numero di vie di fuga disponibili
	 * @param rigaRe Riga in cui si trova il re, al momento della valutazione
	 * @param colonnaRe Colonna in cui si trova il re, al momento della valutazione
	 * @param s StateTablut ovvero stato che si vuole valutare
	 * @return Numero di vie di fuga disponibili per il re. Essendo 4 le mosse che il re può fare, questa funzione restituisce un numero compreso tra [0, 4]
	 */
	public int checkVieDiFugaRe(int rigaRe, int colonnaRe, StateTablut s)
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
	public int getViaDiFugaFromBottom(int rigaRe, int colonnaRe, StateTablut s) {
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
	public int getViaDiFugaFromTop(int rigaRe, int colonnaRe, StateTablut s) {
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
	public int getViaDiFugaFromRight(int rigaRe, int colonnaRe, StateTablut s) {
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
	public int getViaDiFugaFromLeft(int rigaRe, int colonnaRe, StateTablut s) {
		for(int i=colonnaRe-1; i>=0; i--)
		{
			if(!s.getPawn(rigaRe, i).equalsPawn("O") || this.citadels.contains(s.getBox(rigaRe, i)))
			{
				return -1;
			}
		}
		return 0;
	}
	/**
	 * Controlla se una pedina del colore opposto può giungere adiacente, in uno qualunque dei lati, alla pedina passata come parametro
	 * @param riga Riga in cui si trova la pedina che vogliamo controllare
	 * @param colonna Colonna in cui si trova la pedina che vogliamo controllare
	 * @param pedina Accetta solo due parametri: W se la pedina che vogliamo controllare è una bianca, B se una nera
	 * @param s StateTablut ovvero lo stato che si vuole valutare
	 * @return True se una pedina del colore opposto può arrivare, false in caso contrario
	 */
	public boolean checkPawnCanArrive(int riga, int colonna, String turno, StateTablut s)
	{
		return checkBlackCanArriveFromBottom(riga, colonna, turno, s) || 
				checkBlackCanArriveFromTop(riga, colonna, turno, s) || 
				checkBlackCanArriveFromRight(riga, colonna, turno, s) || 
				checkBlackCanArriveFromLeft(riga, colonna, turno, s);
	}
	
	/*
	 * Controlla se esiste un nero che possa arrivare, dal basso, nella casella adiacente a quella passata 
	 */
	//TODO:verificare se il secondo if e' utile oppure no
	public boolean checkBlackCanArriveFromBottom(int riga, int colonna, String turno, StateTablut s) {
		if(turno.equals("W")) {
			for(int i=riga+1; i<9;i++)
			{
				if(s.getPawn(i, colonna).equalsPawn("B"))
				{
					return true;
				}
				if(s.getPawn(i, colonna).equalsPawn("W") || s.getPawn(i, colonna).equalsPawn("T") || this.citadels.contains(s.getBox(i, colonna)))
				{
					return false;
				}			
			}
		} else if(turno.equals("B")){
			for(int i=riga+1; i<9;i++)
			{
				if(s.getPawn(i, colonna).equalsPawn("W"))
				{
					return true;
				}
				if(s.getPawn(i, colonna).equalsPawn("B") || s.getPawn(i, colonna).equalsPawn("T") || this.citadels.contains(s.getBox(i, colonna)))
				{
					return false;
				}			
			}
		}
		
		return false;
	}
	
	/*
	 * Controlla se esiste un nero che possa arrivare, dall'alto, adiacente alla pedina passata come parametro
	 */
	public boolean checkBlackCanArriveFromTop(int riga, int  colonna, String turno, StateTablut s) {
		if(turno.equals("W")) {
			for(int i=riga-1; i>=0;i--) {
				if(s.getPawn(i, colonna).equalsPawn("B"))
				{
					return true;
				}
				if(s.getPawn(i, colonna).equalsPawn("W") || s.getPawn(i, colonna).equalsPawn("T") || this.citadels.contains(s.getBox(i, colonna)))
				{
					return false;
				}			
			}
		} else if(turno.equals("B")) {
			for(int i=riga-1; i>=0;i--) {
				if(s.getPawn(i, colonna).equalsPawn("W"))
				{
					return true;
				}
				if(s.getPawn(i, colonna).equalsPawn("B") || s.getPawn(i, colonna).equalsPawn("T") || this.citadels.contains(s.getBox(i, colonna)))
				{
					return false;
				}			
			}
		}
		
		return false;
	}
	
	/*
	 * Controlla se esiste un nero che possa arrivare, da destra, adiacente alla pedina passata come parametro
	 */
	public boolean checkBlackCanArriveFromRight(int riga, int colonna, String turno, StateTablut s) {
		if(turno.equals("W")) {
			for(int i=colonna+1; i<9;i++)
			{
				if(s.getPawn(riga, i).equalsPawn("B"))
				{
					return true;
				}
				if(s.getPawn(riga, i).equalsPawn("W") || s.getPawn(riga, i).equalsPawn("T") || this.citadels.contains(s.getBox(riga, i)))
				{
					return false;
				}			
			}
		} else if(turno.equals("B")) {
			for(int i=colonna+1; i<9;i++)
			{
				if(s.getPawn(riga, i).equalsPawn("W"))
				{
					return true;
				}
				if(s.getPawn(riga, i).equalsPawn("B") || s.getPawn(riga, i).equalsPawn("T") || this.citadels.contains(s.getBox(riga, i)))
				{
					return false;
				}			
			}
		}
		
		return false;
	}
	
	/*
	 * Controlla se esiste un nero che possa arrivare, da sinistra, sulla casella (riga, colonna) passata come parametro
	 */
	public boolean checkBlackCanArriveFromLeft(int riga, int colonna, String turno, StateTablut s) {
		if(turno.equals("W")) {	
			for(int i=colonna; i>=0;i--)
			{
				if(s.getPawn(riga, i).equalsPawn("B"))
				{
					return true;
				}
				System.out.println("Riga: " + riga + ", colonna: " + i);
				if(s.getPawn(riga, i).equalsPawn("W") || s.getPawn(riga, i).equalsPawn("T") || this.citadels.contains(s.getBox(riga, i)))
				{
					return false;
				}			
			}
		}else if(turno.equals("B")) {
			for(int i=colonna-1; i>=0;i--)
			{
				if(s.getPawn(riga, i).equalsPawn("W"))
				{
					return true;
				}
				if(s.getPawn(riga, i).equalsPawn("B") || s.getPawn(riga, i).equalsPawn("T") || this.citadels.contains(s.getBox(riga, i)))
				{
					return false;
				}			
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
	public boolean enemyOnTheRight(int riga, int colonna, StateTablut s)
	{
		if(colonna!=8) {
			if(s.getPawn(riga, colonna+1).equalsPawn("B") || this.citadels.contains(s.getBox(riga,  colonna+1)))
			{
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}	
	}
	
	/**
	 * Controlla se esiste un nemico o un accampamento alla sinistra di una pedina bianca posizionata in @riga e @colonna
	 * 
	 * @param riga Riga in cui si trova la pedina
	 * @param colonna Colonna in cui si trova la pedina
	 * @param s StateTablut ovvero lo stato da valutare
	 * @return true se una pedina nera o un accampamento è accando, sulla sinistra, alla pedina bianca passata come parametro, false in caso contrario
	 */
	public boolean enemyOnTheLeft(int riga, int colonna, StateTablut s)
	{
		if(colonna!=0) {
			if(s.getPawn(riga, colonna-1).equalsPawn("B") || this.citadels.contains(s.getBox(riga,  colonna-1)))
			{
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}				
	}
	
	/**
	 * Controlla se esiste un nemico o un accampamento in alto rispetto alla pedina bianca posizionata in @riga e @colonna
	 * 
	 * @param riga Riga in cui si trova la pedina
	 * @param colonna Colonna in cui si trova la pedina
	 * @param s StateTablut ovvero lo stato da valutare
	 * @return true se una pedina nera o un accampamento è sopra alla pedina bianca passata come parametro, false in caso contrario
	 */
	public boolean enemyOnTheTop(int riga, int colonna, StateTablut s)
	{
		if(riga!=0) {
			if(s.getPawn(riga-1, colonna).equalsPawn("B") || this.citadels.contains(s.getBox(riga-1,  colonna)))
			{
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}	
	}
	
	/**
	 * Controlla se esiste un nemico o un accampamento in basso (sotto) rispetto alla pedina bianca posizionata in @riga e @colonna
	 * 
	 * @param riga Riga in cui si trova la pedina
	 * @param colonna Colonna in cui si trova la pedina
	 * @param s StateTablut ovvero lo stato da valutare
	 * @return true se una pedina nera o un accampamento è sotto alla pedina bianca passata come parametro, false in caso contrario
	 */
	public boolean enemyOnTheBottom(int riga, int colonna, StateTablut s)
	{
		if(riga!=8) {
			if(s.getPawn(riga+1, colonna).equalsPawn("B") || this.citadels.contains(s.getBox(riga+1,  colonna)))
			{
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}	
	}
	
	/**
	 * Funzione che controlla se il re, muovendosi di una o più mosse da sinistra a destra (orizzontale), arriva ad avere un'intera colonna libera, in cui vincere
	 * 
	 * @param rigaRe Riga in cui si trova il re
	 * @param colonnaRe Colonna in cui si trova il re
	 * @param s StateTablut ovvero lo stato da valutare
	 * @return true se il re, spostandosi orizzontalmente da sinistra a destra, si porta in una situazione in cui non ha nessun ostacolo sopra e sotto di lui
	 */
	public boolean checkFreeColComingFromLeft(int rigaRe, int colonnaRe, StateTablut s) {
		for(int i=colonnaRe+1; i==6;i++)
		{
			if(s.getPawn(rigaRe, i).equalsPawn("B") || s.getPawn(rigaRe, i).equalsPawn("W") || s.getPawn(rigaRe, i).equalsPawn("T") || this.citadels.contains(s.getBox(rigaRe,  i))){
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
				!checkBlackCanArriveFromTop(rigaRe, colonnaRe-1, "W", s) && //nessun nero puo' arrivare  da sopra, alla sinistra del re, per chiuderlo
				!checkBlackCanArriveFromBottom(rigaRe, colonnaRe-1, "W", s) && //nessun nero puo' arrivare  da sotto, alla sinistra del re, per chiuderlo
				!checkBlackCanArriveFromLeft(rigaRe, colonnaRe, "W", s); //nessun nero puo' arrivare dalla stessa riga in cui e' il re
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
	public boolean checkFreeColComingFromRight(int rigaRe, int colonnaRe, StateTablut s) {
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
				!checkBlackCanArriveFromTop(rigaRe, colonnaRe+1, "W", s) && //nessun nero puo' arrivare alla destra del re, venendo dall'alto
				!checkBlackCanArriveFromBottom(rigaRe, colonnaRe+1, "W", s) && //nessun nero puo' arrivare alla destra del re, venendo dal basso
				!checkBlackCanArriveFromRight(rigaRe, colonnaRe, "W", s); //nessun nero puo' arrivare alla destra del re, venendo dalla sua destra (stessa riga
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
	public boolean checkFreeRowComingFromTop(int rigaRe, int colonnaRe, StateTablut s){
		for(int i=rigaRe+1; i==6;i++)
		{
			if(s.getPawn(i, colonnaRe).equalsPawn("B") || s.getPawn(i, colonnaRe).equalsPawn("W") || s.getPawn(i, colonnaRe).equalsPawn("T") || this.citadels.contains(s.getBox(i, colonnaRe)))
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
				!checkBlackCanArriveFromTop(rigaRe, colonnaRe, "W", s) &&  //nessun nero puo' arrivare e chiudere da sopra il re
				!checkBlackCanArriveFromLeft(rigaRe-1, colonnaRe, "W", s) && //nessun nero puo' arrivare a chiudere il re, provendendo da sinistra, nella riga precedente quella il posizionamento del re
				!checkBlackCanArriveFromRight(rigaRe-1, colonnaRe, "W", s); //nessun nero puo' arrivare a chiudere il re, provendendo da destra, nella riga precedente quella il posizionamento del re
	}
	
	/**
	 * Funzione che controlla se il re, muovendosi di una o più mosse dal basso all' alto (verticale), arriva ad avere un'intera riga libera, in cui vincere
	 * 
	 * @param rigaRe Riga in cui si trova il re
	 * @param colonnaRe Colonna in cui si trova il re
	 * @param s StateTablut ovvero lo stato da valutare
	 * @return true se il re, spostandosi verticalmente dal basso all' alto, si porta in una situazione in cui non ha nessun ostacolo a sinistra e a destra di lui
	 */
	public boolean checkFreeRowComingFromBottom(int rigaRe, int colonnaRe, StateTablut s){
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
				!checkBlackCanArriveFromBottom(rigaRe, colonnaRe, "W", s) && 
				!checkBlackCanArriveFromLeft(rigaRe+1, colonnaRe, "W", s) &&
				!checkBlackCanArriveFromRight(rigaRe+1, colonnaRe, "W", s);
	}
	
	/**
	 * Controlla se data una cella, tutte le celle alla destra sono libere (non occupate da bianchi, neri, trono o cittadelle)
	 * 
	 * @param rigaRe Riga in cui si trova la cella da valutare
	 * @param colonnaRe Colonna in cui si trova la cella da valutare
	 * @param s StateTablut ovvero lo stato da valutare
	 * @return true se non ci sono elementi alla destra della cella specificata da @riga + @colonna, false in caso contrario
	 */
	public boolean checkFreeRowRight(int riga, int colonna, StateTablut s) {
		for(int i=colonna+1; i<9; i++){
			if(s.getPawn(riga, i).equalsPawn("B") || s.getPawn(riga, i).equalsPawn("W") || s.getPawn(riga, i).equalsPawn("T") || this.citadels.contains(s.getBox(riga, i))) {
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
	public boolean checkFreeRowLeft(int riga, int colonna, StateTablut s) {
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
	public boolean checkFreeColTop(int rigaRe, int colonnaRe, StateTablut s) {
		
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
	public boolean checkFreeColBottom(int rigaRe, int colonnaRe, StateTablut s) {
		for(int i=rigaRe+1; i<9; i++) {
			if(s.getPawn(i, colonnaRe).equalsPawn("B") || s.getPawn(i, colonnaRe).equalsPawn("W")) {
				return false;
			}
		}
		
		return true;
	}

}
