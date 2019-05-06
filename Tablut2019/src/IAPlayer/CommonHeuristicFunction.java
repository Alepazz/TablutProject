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
	
	public List<String> getCitadels() {
		return this.citadels;	
	}
	
	/**
	 * Controlla se la posizione passata come parametro(coordinate riga-colonna) corrisponde ad una cittadella
	 * @param riga Riga della posizione passata
	 * @param colonna Colonna della posizione passata
	 * @param s StateTablut ovvero lo stato da valutare
	 * @return true se la posizione passata corrisponde ad una cittadella, false in caso contrario
	 */
	public boolean isCitadel(int riga, int colonna, StateTablut s) {
		return isCitadel(s.getBox(riga, colonna));
	}
	
	/**
	 * Controlla se la posizione passata come parametro(formato testuale) corrisponde ad una cittadella
	 * @param position Stringa che rappresenta la posizione(es. "a4")
	 * @return true se la posizione passata corrisponde ad una cittadella, false in caso contrario
	 */
	public boolean isCitadel(String position) {
		return this.citadels.contains(position);
	}
	
	//TODO: Commentare cosa fa questa funzione
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
					i=-1;
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
					i=20;
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
		
	/**
	 * Restituisce il numero di pedine bianche/nere presenti sul tabellone, dato lo stato s
	 *
	 * Nel conteggio è compreso anche il re solo nel caso in cui color = "W"
	 * @param color Colore indicante la fazione di cui si vuole sapere il numero di pedine ("W" = bianche, "B" = nere)
	 * @param s StateTablut rappresenta lo stato da valutare
	 * @return numero di pedine bianche, comprensive del re, presenti sulla scacchiera
	 */
	public int getNumberOfColor(String color,StateTablut s) {
		
		int result = 0;
		
		for(int i=0; i<9; i++) {
			for(int j=0; j<9; j++) {
				if(s.getPawn(i, j).equalsPawn(color)) {
					result++;
				}
			}
		}
		
		if(color.equals("W")) {
			result +=1; //aggiungo il re
		}
		
		return result; 
	}
	
	/**
	 * Ritorna il numero di pedine totali presenti sulla scacchiera
	 * @param s StateTablut ovvero lo stato che deve essere valutato
	 * @return numero di pedine (bianche e nere) presenti nella scacchiera
	 */
	public int getNumberPawns(StateTablut s) {
		int result = 0;
		for(int i=0; i<9; i++) {
			for(int j=0; j<9; j++) {
				if(s.getPawn(i, j).equalsPawn("W") || s.getPawn(i, j).equalsPawn("B") || s.getPawn(i, j).equalsPawn("K")) {
					result ++;
				}
			}
		}
		return result;
	}
	
	/**
	 * Ritorna il numero di colonne (o semicolonne) e righe (o semirighe) libere, al termine delle quali c'è la casella blu che permette al bianco di vincere
	 * 
	 * semicolonna = per semicolonna si intendono le colonne 1 e 7 che sono interrotte da una cittadella
	 * 
	 * semiriga = per semiriga si intendono le righe 1 e 7 che sono interrotte da una cittadella
	 * 
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
	 * @return true se il re, in quello stato s, puù essere catturato, false in caso contrario
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
	
	/**
	 * Controlla se una pedina nera passata come parametro può essere catturata
	 * @param riga Riga in cui si trova la pedina
	 * @param colonna Colonna in cui si trova la pedina
	 * @param s StateTablut ovvero lo stato da valutare
	 * @return true se la pedina puòessere catturata, false in caso contrario
	 */
	public boolean checkBlackCanBeCaptured(int riga, int colonna, StateTablut s) {
		
		//sottointeso turno bianco
		
		//Controllo inutile, volendo si può togliere
		if(this.checkPedinaIsolata(riga, colonna, s)) {
			return false; //il nero non può essere catturato
		}
		
		if((this.enemyOnTheTop(riga, colonna, s) && this.checkWhiteCanArriveAdjacentInBottomPosition(riga, colonna, s))
				|| (this.enemyOnTheRight(riga, colonna, s) && this.checkWhiteCanArriveAdjacentInLeftPosition(riga, colonna, s))
				|| (this.enemyOnTheBottom(riga, colonna, s) && this.checkWhiteCanArriveAdjacentInTopPosition(riga, colonna, s))
				|| (this.enemyOnTheLeft(riga, colonna, s) && this.checkWhiteCanArriveAdjacentInRightPosition(riga, colonna, s))) {
			return true;
		}
		return false;
	}
	
	/**
	 * Controlla se una pedina bianca passata come parametro può essere catturata
	 * @param riga Riga in cui si trova la pedina
	 * @param colonna Colonna in cui si trova la pedina
	 * @param s StateTablut ovvero lo stato da valutare
	 * @return true se la pedina può essere catturata, false in caso contrario
	 */
	public boolean checkWhiteCanBeCaptured(int riga, int colonna, StateTablut s) {
		
		//sottointeso turno nero
		//deve arrivare il nero da qualsiasi posizione && deve avere un nero, il trono, un cittadella o il muro vicino
		
		if( this.checkPedinaIsolata(riga, colonna, s)) {
			return false; //se la pedina non ha vicini, non pu� essere catturata
		}
		//pedina che può arrivare da sotto (sopra c'è già)
		if((this.checkNeighbourTop(riga, colonna, s).equals("B")|| this.checkNeighbourTop(riga, colonna, s).equals("C") || this.checkNeighbourTop(riga, colonna, s).equals("X") || this.checkNeighbourTop(riga, colonna, s).equals("T")) 
				&& (this.checkBlackCanArriveAdjacentInBottomPosition(riga, colonna, s))) {
			return true; // se la pedina ha due pedine nere sopra e sotto, allora non pu� essere catturata
		}
		//pedina che può arrivare da sopra (sotto c'è già)
		if((this.checkNeighbourBottom(riga, colonna, s).equals("B")|| this.checkNeighbourBottom(riga, colonna, s).equals("C") || this.checkNeighbourBottom(riga, colonna, s).equals("X") || this.checkNeighbourBottom(riga, colonna, s).equals("T")) 
				&& (this.checkBlackCanArriveAdjacentInTopPosition(riga, colonna, s))) {
			return true; // se la pedina ha due pedine nere sopra e sotto, allora non pu� essere catturata
		}
		
		//pedina che può arrivare da destra
		if((this.checkNeighbourLeft(riga, colonna, s).equals("B")|| this.checkNeighbourLeft(riga, colonna, s).equals("C") || this.checkNeighbourLeft(riga, colonna, s).equals("X") || this.checkNeighbourLeft(riga, colonna, s).equals("T")) 
				&& (this.checkBlackCanArriveAdjacentInRightPosition(riga, colonna, s))) {
			return true; // se la pedina ha due pedine nere sopra e sotto, allora non pu� essere catturata
		}
		
		//pedina che può arrivare da sinistra
		if((this.checkNeighbourRight(riga, colonna, s).equals("B")|| this.checkNeighbourRight(riga, colonna, s).equals("C") || this.checkNeighbourRight(riga, colonna, s).equals("X") || this.checkNeighbourRight(riga, colonna, s).equals("T")) 
				&& (this.checkBlackCanArriveAdjacentInLeftPosition(riga, colonna, s))) {
			return true; // se la pedina ha due pedine nere sopra e sotto, allora non pu� essere catturata
		}

		return false;
	}
	
	/**
	 * Controlla se una pedina è circondata e quindi non si può muovere
	 * @param riga Riga in cui si trova la pedina
	 * @param colonna Colonna in cui si trova la pedina
	 * @param s StateTablut ovvero lo stato da valutare
	 * @return true se la pedina è circondata, false in caso contrario
	 */
	public boolean checkPawnBlocked(int riga, int colonna, StateTablut s) {
		//La pedina passata è una pedina bianca, il re oppure una pedina nera fuori dalla cittadella
		if(s.getPawn(riga, colonna).equalsPawn("W") || s.getPawn(riga, colonna).equalsPawn("K")
				|| (s.getPawn(riga, colonna).equalsPawn("B") && !this.citadels.contains(s.getBox(riga, colonna)))) {
			return (!this.checkNeighbourTop(riga, colonna, s).equals("O")) &&
					(!this.checkNeighbourBottom(riga, colonna, s).equals("O")) &&
					(!this.checkNeighbourLeft(riga, colonna, s).equals("O")) &&
					(!this.checkNeighbourRight(riga, colonna, s).equals("O"));
		}
		//La pedina passata è una pedina nera dentro una cittadella
		if(s.getPawn(riga, colonna).equalsPawn("B") && this.citadels.contains(s.getBox(riga, colonna))) {
			return (!this.checkNeighbourTop(riga, colonna, s).equals("O") && !this.citadels.contains(s.getBox(riga, colonna))) &&
					(!this.checkNeighbourBottom(riga, colonna, s).equals("O") && !this.citadels.contains(s.getBox(riga, colonna))) &&
					(!this.checkNeighbourLeft(riga, colonna, s).equals("O") && !this.citadels.contains(s.getBox(riga, colonna))) &&
					(!this.checkNeighbourRight(riga, colonna, s).equals("O") && !this.citadels.contains(s.getBox(riga, colonna)));
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
				return "O"; //c'� una cella libera
			} else {
				if(s.getPawn(riga-1, colonna).equalsPawn("K")) {
					return "K"; //c'� il re
				}
				
				if(s.getPawn(riga-1, colonna).equalsPawn("B")) {
					return "B"; //c'� un nero
				}
				
				if(s.getPawn(riga-1, colonna).equalsPawn("W")) {
					return "W"; //c'� un bianco
				}
				
				if(s.getPawn(riga-1, colonna).equalsPawn("T")) {
					return "T"; //c'� il trono
				}
			}
			
			return "C"; //c'� la cittadella
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
				return "O"; //c'� una cella libera
			} else {
				if(s.getPawn(riga+1, colonna).equalsPawn("K")) {
					return "K"; //c'� il re
				}
				
				if(s.getPawn(riga+1, colonna).equalsPawn("B")) {
					return "B"; //c'� un nero
				}
				
				if(s.getPawn(riga+1, colonna).equalsPawn("W")) {
					return "W"; //c'� un bianco
				}
				
				if(s.getPawn(riga+1, colonna).equalsPawn("T")) {
					return "T"; //c'� il trono
				}
			}
			
			return "C"; //c'� la cittadella
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
				return "O"; //c'� una cella libera
			} else {
				if(s.getPawn(riga, colonna-1).equalsPawn("K")) {
					return "K"; //c'� il re
				}
				
				if(s.getPawn(riga, colonna-1).equalsPawn("B")) {
					return "B"; //c'� un nero
				}
				
				if(s.getPawn(riga, colonna-1).equalsPawn("W")) {
					return "W"; //c'� un bianco
				}
				
				if(s.getPawn(riga, colonna-1).equalsPawn("T")) {
					return "T"; //c'� il trono
				}
			}
			
			return "C"; //c'� la cittadella
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
				return "O"; //c'� una cella libera
			} else {
				if(s.getPawn(riga, colonna+1).equalsPawn("K")) {
					return "K"; //c'� il re
				}
				
				if(s.getPawn(riga, colonna+1).equalsPawn("B")) {
					return "B"; //c'� un nero
				}
				
				if(s.getPawn(riga, colonna+1).equalsPawn("W")) {
					return "W"; //c'� un bianco
				}
				
				if(s.getPawn(riga, colonna+1).equalsPawn("T")) {
					return "T"; //c'� il trono
				}
			}
			
			return "C"; //c'� la cittadella
		} else {
			return "X";
		}		
	}
	
	/**
	 * Controlla la vicinanza delle pedine in alto a sinistra
	 * @param riga Riga della pedina che si vuole controllare
	 * @param colonna Colonna della pedina che si vuole controllare
	 * @param s StateTablut ovvero lo stato da valutare
	 * @return Ritorna la stringa corrispondende l'elemento che si trova nelle vicinanze della pedina passata come parametro
	 * Es. "W" se c'è una pedina bianca, "O" se c'è una cella libera
	 */
	public String checkNeighbourTopLeft(int riga, int colonna, StateTablut s) {
		if(colonna!=0 && riga !=0 ) {
			if(s.getPawn(riga-1, colonna-1).equalsPawn("O") && !this.citadels.contains(s.getBox(riga, colonna-1))) {
				return "O"; //c'è una cella libera
			} else {
				if(s.getPawn(riga-1, colonna-1).equalsPawn("K")) {
					return "K"; //c'è il re
				}
				
				if(s.getPawn(riga-1, colonna-1).equalsPawn("B")) {
					return "B"; //c'è un nero
				}
				
				if(s.getPawn(riga-1, colonna-1).equalsPawn("W")) {
					return "W"; //c'è un bianco
				}
				
				if(s.getPawn(riga-1, colonna-1).equalsPawn("T")) {
					return "T"; //c'è il trono
				}
			}
			
			return "C"; //c'è la cittadella
		} else {
			return "X";
		}		
	}
	
	/**
	 * Controlla la vicinanza delle pedine in alto a destra
	 * 
	 * @param riga Riga della pedina che si vuole controllare
	 * @param colonna Colonna della pedina che si vuole controllare
	 * @param s StateTablut ovvero lo stato da valutare
	 * @return Ritorna la stringa corrispondende l'elemento che si trova nelle vicinanze della pedina passata come parametro
	 * Es. "W" se c'è una pedina bianca, "O" se c'è una cella libera
	 */
	public String checkNeighbourTopRight(int riga, int colonna, StateTablut s) {
		if(colonna!=8 && riga !=0) {
			if(s.getPawn(riga-1, colonna+1).equalsPawn("O") && !this.citadels.contains(s.getBox(riga, colonna-1))) {
				return "O"; //c'� una cella libera
			} else {
				if(s.getPawn(riga-1, colonna+1).equalsPawn("K")) {
					return "K"; //c'� il re
				}
				
				if(s.getPawn(riga-1, colonna+1).equalsPawn("B")) {
					return "B"; //c'� un nero
				}
				
				if(s.getPawn(riga-1, colonna+1).equalsPawn("W")) {
					return "W"; //c'� un bianco
				}
				
				if(s.getPawn(riga-1, colonna+1).equalsPawn("T")) {
					return "T"; //c'� il trono
				}
			}
			
			return "C"; //c'� la cittadella
		} else {
			return "X";
		}		
	}
	
	/**
	 * Controlla la vicinanza delle pedine in basso a sinistra
	 * 
	 * @param riga Riga della pedina che si vuole controllare
	 * @param colonna Colonna della pedina che si vuole controllare
	 * @param s StateTablut ovvero lo stato da valutare
	 * @return Ritorna la stringa corrispondende l'elemento che si trova nelle vicinanze della pedina passata come parametro
	 * Es. "W" se c'è una pedina bianca, "O" se c'è una cella libera
	 */
	public String checkNeighbourBottomLeft(int riga, int colonna, StateTablut s) {
		if(colonna!=0 && riga !=8) {
			if(s.getPawn(riga+1, colonna-1).equalsPawn("O") && !this.citadels.contains(s.getBox(riga, colonna-1))) {
				return "O"; //c'� una cella libera
			} else {
				if(s.getPawn(riga+1, colonna-1).equalsPawn("K")) {
					return "K"; //c'� il re
				}
				
				if(s.getPawn(riga+1, colonna-1).equalsPawn("B")) {
					return "B"; //c'� un nero
				}
				
				if(s.getPawn(riga+1, colonna-1).equalsPawn("W")) {
					return "W"; //c'� un bianco
				}
				
				if(s.getPawn(riga+1, colonna-1).equalsPawn("T")) {
					return "T"; //c'� il trono
				}
			}
			
			return "C"; //c'� la cittadella
		} else {
			return "X";
		}		
	}
	
	/**
	 * Controlla la vicinanza delle pedine in basso a destra
	 * 
	 * @param riga Riga della pedina che si vuole controllare
	 * @param colonna Colonna della pedina che si vuole controllare
	 * @param s StateTablut ovvero lo stato da valutare
	 * @return Ritorna la stringa corrispondende l'elemento che si trova nelle vicinanze della pedina passata come parametro
	 * Es. "W" se c'è una pedina bianca, "O" se c'è una cella libera
	 */
	public String checkNeighbourBottomRight(int riga, int colonna, StateTablut s) {
		if(colonna!=8 && riga !=8) {
			if(s.getPawn(riga+1, colonna+1).equalsPawn("O") && !this.citadels.contains(s.getBox(riga, colonna-1))) {
				return "O"; //c'� una cella libera
			} else {
				if(s.getPawn(riga+1, colonna+1).equalsPawn("K")) {
					return "K"; //c'� il re
				}
				
				if(s.getPawn(riga+1, colonna+1).equalsPawn("B")) {
					return "B"; //c'� un nero
				}
				
				if(s.getPawn(riga+1, colonna+1).equalsPawn("W")) {
					return "W"; //c'� un bianco
				}
				
				if(s.getPawn(riga+1, colonna+1).equalsPawn("T")) {
					return "T"; //c'� il trono
				}
			}
			
			return "C"; //c'� la cittadella
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
					return 0; //il re � nella meta superiore
				}
			}
		}
		
		for(int i=0; i<9; i++) {
			if(s.getPawn(4, i).equalsPawn("K")) {
				return 1; //il re � nella riga 5(quella del trono)
			}
		}
		
		return 2; //il re � nella meta inferiore
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
	 * @return int: -1 se la via di fuga non � disponibile, 0 se invece � disponibile
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
	 * Controlla se una pedina nera puo' giungere adiacente, in uno qualunque dei lati, alla pedina passata come parametro
	 * @param riga Riga in cui si trova la pedina che vogliamo controllare
	 * @param colonna Colonna in cui si trova la pedina che vogliamo controllare
	 * @param s StateTablut ovvero lo stato che si vuole valutare
	 * @return True se una pedina bianca puo' arrivare, false in caso contrario
	 */
	public boolean checkBlackCanArriveAdjacent(int riga, int colonna, StateTablut s)
	{
		if(checkBlackCanArriveAdjacentInBottomPosition(riga, colonna, s) || 
				checkBlackCanArriveAdjacentInTopPosition(riga, colonna, s) || 
				checkBlackCanArriveAdjacentInRightPosition(riga, colonna, s) || 
				checkBlackCanArriveAdjacentInLeftPosition(riga, colonna, s))
			return true;
		return false;
	}
	
	/**
	 * Controlla se una pedina nera puo' giungere adiacente, nel lato alto, alla pedina passata come parametro
	 * @param riga Riga in cui si trova la pedina che vogliamo controllare
	 * @param colonna Colonna in cui si trova la pedina che vogliamo controllare
	 * @param s StateTablut ovvero lo stato che si vuole valutare
	 * @return True se una pedina nera puo' arrivare, false in caso contrario
	 */
	public boolean checkBlackCanArriveAdjacentInTopPosition(int riga, int  colonna, StateTablut s) {
		if(riga!=0) {
			if(checkBlackCanArriveFromTop(riga-1, colonna, s) || checkBlackCanArriveFromRight(riga-1, colonna, s) || checkBlackCanArriveFromLeft(riga-1, colonna, s)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Controlla se una pedina nera puo' giungere adiacente, nel lato destro, alla pedina passata come parametro
	 * @param riga Riga in cui si trova la pedina che vogliamo controllare
	 * @param colonna Colonna in cui si trova la pedina che vogliamo controllare
	 * @param s StateTablut ovvero lo stato che si vuole valutare
	 * @return True se una pedina nera puo' arrivare, false in caso contrario
	 */
	public boolean checkBlackCanArriveAdjacentInRightPosition(int riga, int  colonna, StateTablut s) {
		if(colonna!=8) {
			if(checkBlackCanArriveFromTop(riga, colonna+1, s) || checkBlackCanArriveFromRight(riga, colonna+1, s) || checkBlackCanArriveFromLeft(riga, colonna+1, s)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Controlla se una pedina nera puo' giungere adiacente, nel lato basso, alla pedina passata come parametro
	 * @param riga Riga in cui si trova la pedina che vogliamo controllare
	 * @param colonna Colonna in cui si trova la pedina che vogliamo controllare
	 * @param s StateTablut ovvero lo stato che si vuole valutare
	 * @return True se una pedina nera puo' arrivare, false in caso contrario
	 */
	public boolean checkBlackCanArriveAdjacentInBottomPosition(int riga, int  colonna, StateTablut s) {
		if(riga!=8) {
			if(checkBlackCanArriveFromTop(riga+1, colonna, s) || checkBlackCanArriveFromRight(riga+1, colonna, s) || checkBlackCanArriveFromLeft(riga+1, colonna, s)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Controlla se una pedina nera puo' giungere adiacente, nel lato sinistro, alla pedina passata come parametro
	 * @param riga Riga in cui si trova la pedina che vogliamo controllare
	 * @param colonna Colonna in cui si trova la pedina che vogliamo controllare
	 * @param s StateTablut ovvero lo stato che si vuole valutare
	 * @return True se una pedina nera puo' arrivare, false in caso contrario
	 */
	public boolean checkBlackCanArriveAdjacentInLeftPosition(int riga, int  colonna, StateTablut s) {
		if(colonna!=0) {
			if(checkBlackCanArriveFromTop(riga, colonna-1, s) || checkBlackCanArriveFromRight(riga, colonna-1, s) || checkBlackCanArriveFromLeft(riga, colonna-1, s)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Controlla se una pedina nera puo' giungere da qualsiasi lato alla posizione passata come parametro
	 * @param riga Riga della posizione dove vogliamo controllare se puo' arrivare una pedina nera 
	 * @param colonna Colonna della posizione dove vogliamo controllare se puo' arrivare una pedina nera
	 * @param s StateTablut ovvero lo stato che si vuole valutare
	 * @return True se una pedina del nera puo' arrivare, false in caso contrario
	 */
	public boolean checkBlackCanArrive(int riga, int colonna, StateTablut s)
	{
		if(checkBlackCanArriveFromBottom(riga, colonna, s) || 
				checkBlackCanArriveFromTop(riga, colonna, s) || 
				checkBlackCanArriveFromRight(riga, colonna, s) || 
				checkBlackCanArriveFromLeft(riga, colonna, s))
			return true;
		return false;
	}
	
	/**
	 * Controlla se una pedina nera puo' giungere da sopra alla posizione passata come parametro
	 * @param riga Riga della posizione dove vogliamo controllare se puo' arrivare una pedina nera 
	 * @param colonna Colonna della posizione dove vogliamo controllare se puo' arrivare una pedina nera
	 * @param s StateTablut ovvero lo stato che si vuole valutare
	 * @return True se una pedina nera puo' arrivare, false in caso contrario
	 */
	public boolean checkBlackCanArriveFromTop(int riga, int  colonna, StateTablut s) {
		//Domanda: Se e' gia' presente una pedina nera in quella posizione ritorna true o false?
		//Controlla la posizione corrente e se e' gia' presente una pedina nera ritorna false
		if(s.getPawn(riga, colonna).equalsPawn("B")) {
			return false;
		}
		//Ciclo di controllo ostacoli partendo dalla posizione passata fino al bordo
		for(int i=riga; i>=0;i--) {
			//Se trova la pedina nera ritorna risposta positiva
			if(s.getPawn(i, colonna).equalsPawn("B"))
			{
				return true;
			}
			//Se trova un ostacolo(pedina bianca o trono) ritorna risposta negativa
			if(!s.getPawn(i, colonna).equalsPawn("O"))
			{
				return false;
			}
			//Se trova una cittadella ritorna risposta negativa
			//Tranne nel caso particolare delle cittadelle che permettono il passaggio della pedina nera
			if(this.citadels.contains(s.getBox(i, colonna)) && !this.citadels.contains("a6") 
					&& !this.citadels.contains("e2") && !this.citadels.contains("i6"))
			{
				return false;
			}
		}
		return false;
	}
	
	/**
	 * Controlla se una pedina nera puo' giungere da destra alla posizione passata come parametro
	 * @param riga Riga della posizione dove vogliamo controllare se puo' arrivare una pedina nera 
	 * @param colonna Colonna della posizione dove vogliamo controllare se puo' arrivare una pedina nera
	 * @param s StateTablut ovvero lo stato che si vuole valutare
	 * @return True se una pedina nera puo' arrivare, false in caso contrario
	 */
	public boolean checkBlackCanArriveFromRight(int riga, int colonna, StateTablut s) {
		//Domanda: Se e' gia' presente una pedina nera in quella posizione ritorna true o false?
		//Controlla la posizione corrente e se e' gia' presente una pedina nera ritorna false
		if(s.getPawn(riga, colonna).equalsPawn("B")) {
			return false;
		}
		//Ciclo di controllo ostacoli partendo dalla posizione passata fino al bordo
		for(int i=colonna; i<9;i++)
		{
			//Se trova la pedina nera ritorna risposta positiva
			if(s.getPawn(riga, i).equalsPawn("B"))
			{
				return true;
			}
			//Se trova un ostacolo(pedina bianca o trono) ritorna risposta negativa
			if(!s.getPawn(riga, i).equalsPawn("O"))
			{
				return false;
			}
			//Se trova una cittadella ritorna risposta negativa
			//Tranne nel caso particolare delle cittadelle che permettono il passaggio della pedina nera
			if(this.citadels.contains(s.getBox(riga, i)) && !this.citadels.contains("d1") 
					&& !this.citadels.contains("d9") && !this.citadels.contains("h5"))
			{
				return false;
			}			
		}		
		return false;
	}
	
	/**
	 * Controlla se una pedina nera puo' giungere dal basso alla posizione passata come parametro
	 * @param riga Riga della posizione dove vogliamo controllare se puo' arrivare una pedina nera 
	 * @param colonna Colonna della posizione dove vogliamo controllare se puo' arrivare una pedina nera
	 * @param s StateTablut ovvero lo stato che si vuole valutare
	 * @return True se una pedina nera puo' arrivare, false in caso contrario
	 */
	public boolean checkBlackCanArriveFromBottom(int riga, int colonna, StateTablut s) {
		//Domanda: Se e' gia' presente una pedina nera in quella posizione ritorna true o false?
		//Controlla la posizione corrente e se e' gia' presente una pedina nera ritorna false
		if(s.getPawn(riga, colonna).equalsPawn("B")) {
			return false;
		}
		//Ciclo di controllo ostacoli partendo dalla posizione passata fino al bordo
		for(int i=riga; i<9;i++)
		{
			//Se trova la pedina nera ritorna risposta positiva
			if(s.getPawn(i, colonna).equalsPawn("B"))
			{
				return true;
			}
			//Se trova un ostacolo(pedina bianca o trono) ritorna risposta negativa
			if(!s.getPawn(i, colonna).equalsPawn("O"))
			{
				return false;
			}
			//Se trova una cittadella ritorna risposta negativa
			//Tranne nel caso particolare delle cittadelle che permettono il passaggio della pedina nera
			if(this.citadels.contains(s.getBox(i, colonna)) && !this.citadels.contains("a4") 
					&& !this.citadels.contains("i4") && !this.citadels.contains("e8"))
			{
				return false;
			}			
		}
		return false;
	}
	
	/**
	 * Controlla se una pedina nera puo' giungere da sinistra alla posizione passata come parametro
	 * @param riga Riga della posizione dove vogliamo controllare se puo' arrivare una pedina nera 
	 * @param colonna Colonna della posizione dove vogliamo controllare se puo' arrivare una pedina nera
	 * @param s StateTablut ovvero lo stato che si vuole valutare
	 * @return True se una pedina nera puo' arrivare, false in caso contrario
	 */
	public boolean checkBlackCanArriveFromLeft(int riga, int colonna, StateTablut s) {
		if(s.getPawn(riga, colonna).equalsPawn("B")) {
			return false;
		}
		//Ciclo di controllo ostacoli partendo dalla posizione passata fino al bordo
		for(int i=colonna; i>=0;i--)
		{
			//Se trova la pedina nera ritorna risposta positiva
			if(s.getPawn(riga, i).equalsPawn("B"))
			{
				return true;
			}
			//Se trova un ostacolo(pedina bianca o trono) ritorna risposta negativa
			if(!s.getPawn(riga, i).equalsPawn("O"))
			{
				return false;
			}
			//Se trova una cittadella ritorna risposta negativa
			//Tranne nel caso particolare delle cittadelle che permettono il passaggio della pedina nera
			if(this.citadels.contains(s.getBox(riga, i)) && !this.citadels.contains("b5") 
					&& !this.citadels.contains("f1") && !this.citadels.contains("f9"))
			{
				return false;
			}			
		}
		return false;
	}
	
	/**
	 * Controlla se una pedina bianca puo' giungere adiacente, in uno qualunque dei lati, alla pedina passata come parametro
	 * @param riga Riga in cui si trova la pedina che vogliamo controllare
	 * @param colonna Colonna in cui si trova la pedina che vogliamo controllare
	 * @param s StateTablut ovvero lo stato che si vuole valutare
	 * @return True se una pedina bianca puo' arrivare, false in caso contrario
	 */
	public boolean checkWhiteCanArriveAdjacent(int riga, int colonna, StateTablut s)
	{
		if(checkWhiteCanArriveAdjacentInBottomPosition(riga, colonna, s) || 
				checkWhiteCanArriveAdjacentInTopPosition(riga, colonna, s) || 
				checkWhiteCanArriveAdjacentInRightPosition(riga, colonna, s) || 
				checkWhiteCanArriveAdjacentInLeftPosition(riga, colonna, s))
			return true;
		return false;
	}
	
	/**
	 * Controlla se una pedina bianca puo' giungere adiacente, nel lato alto, alla pedina passata come parametro
	 * @param riga Riga in cui si trova la pedina che vogliamo controllare
	 * @param colonna Colonna in cui si trova la pedina che vogliamo controllare
	 * @param s StateTablut ovvero lo stato che si vuole valutare
	 * @return True se una pedina nera puo' arrivare, false in caso contrario
	 */
	public boolean checkWhiteCanArriveAdjacentInTopPosition(int riga, int  colonna, StateTablut s) {
		if(riga!=0) {
			if(checkWhiteCanArriveFromTop(riga-1, colonna, s) || checkWhiteCanArriveFromRight(riga-1, colonna, s) || checkWhiteCanArriveFromLeft(riga-1, colonna, s)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Controlla se una pedina bianca puo' giungere adiacente, nel lato destro, alla pedina passata come parametro
	 * @param riga Riga in cui si trova la pedina che vogliamo controllare
	 * @param colonna Colonna in cui si trova la pedina che vogliamo controllare
	 * @param s StateTablut ovvero lo stato che si vuole valutare
	 * @return True se una pedina nera puo' arrivare, false in caso contrario
	 */
	public boolean checkWhiteCanArriveAdjacentInRightPosition(int riga, int  colonna, StateTablut s) {
		if(colonna!=8) {
			if(checkWhiteCanArriveFromTop(riga, colonna+1, s) || checkWhiteCanArriveFromBottom(riga, colonna+1, s) || checkWhiteCanArriveFromRight(riga, colonna+1, s)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Controlla se una pedina bianca puo' giungere adiacente, nel lato basso, alla pedina passata come parametro
	 * @param riga Riga in cui si trova la pedina che vogliamo controllare
	 * @param colonna Colonna in cui si trova la pedina che vogliamo controllare
	 * @param s StateTablut ovvero lo stato che si vuole valutare
	 * @return True se una pedina nera puo' arrivare, false in caso contrario
	 */
	public boolean checkWhiteCanArriveAdjacentInBottomPosition(int riga, int  colonna, StateTablut s) {
		if(riga!=8) {
			if(checkWhiteCanArriveFromBottom(riga+1, colonna, s) || checkWhiteCanArriveFromRight(riga+1, colonna, s) || checkWhiteCanArriveFromLeft(riga+1, colonna, s)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Controlla se una pedina bianca puo' giungere adiacente, nel lato sinistro, alla pedina passata come parametro
	 * @param riga Riga in cui si trova la pedina che vogliamo controllare
	 * @param colonna Colonna in cui si trova la pedina che vogliamo controllare
	 * @param s StateTablut ovvero lo stato che si vuole valutare
	 * @return True se una pedina nera puo' arrivare, false in caso contrario
	 */
	public boolean checkWhiteCanArriveAdjacentInLeftPosition(int riga, int  colonna, StateTablut s) {
		if(colonna!=0) {
			if(checkWhiteCanArriveFromTop(riga, colonna-1, s) || checkWhiteCanArriveFromBottom(riga, colonna-1, s) || checkWhiteCanArriveFromLeft(riga, colonna-1, s)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Controlla se una pedina bianca puo' giungere da qualsiasi lato alla posizione passata come parametro
	 * @param riga Riga della posizione dove vogliamo controllare se puo' arrivare una pedina bianca
	 * @param colonna Colonna della posizione dove vogliamo controllare se puo' arrivare una pedina bianca
	 * @param s StateTablut ovvero lo stato che si vuole valutare
	 * @return True se una pedina bianca puo' arrivare, false in caso contrario
	 */
	public boolean checkWhiteCanArrive(int riga, int colonna, StateTablut s)
	{
		//Negli spostamenti non è compreso il re, da aggiungere
		if(checkWhiteCanArriveFromBottom(riga, colonna, s) || 
				checkWhiteCanArriveFromTop(riga, colonna, s) || 
				checkWhiteCanArriveFromRight(riga, colonna, s) || 
				checkWhiteCanArriveFromLeft(riga, colonna, s))
			return true;
		return false;
	}
	
	/**
	 * Controlla se una pedina bianca puo' giungere da sopra alla posizione passata come parametro
	 * @param riga Riga della posizione dove vogliamo controllare se puo' arrivare una pedina bianca
	 * @param colonna Colonna della posizione dove vogliamo controllare se puo' arrivare una pedina bianca
	 * @param s StateTablut ovvero lo stato che si vuole valutare
	 * @return True se una pedina bianca puo' arrivare, false in caso contrario
	 */
	public boolean checkWhiteCanArriveFromTop(int riga, int  colonna, StateTablut s) {
		if(s.getPawn(riga, colonna).equalsPawn("W")) {
			return false;
		}
		//Ciclo di controllo ostacoli partendo dalla posizione passata fino al bordo
		for(int i=riga; i>=0;i--) {
			//Se trova la pedina bianca ritorna risposta positiva
			if(s.getPawn(i, colonna).equalsPawn("W"))
			{
				return true;
			}
			//Se trova un ostacolo(pedina nera, trono, cittadella) ritorna risposta negativa
			if(!s.getPawn(i, colonna).equalsPawn("O") || this.citadels.contains(s.getBox(i, colonna)))
			{
				return false;
			}
		}
		return false;
	}
	
	/**
	 * Controlla se una pedina bianca puo' giungere da destra alla posizione passata come parametro
	 * @param riga Riga della posizione dove vogliamo controllare se puo' arrivare una pedina bianca
	 * @param colonna Colonna della posizione dove vogliamo controllare se puo' arrivare una pedina bianca
	 * @param s StateTablut ovvero lo stato che si vuole valutare
	 * @return True se una pedina bianca puo' arrivare, false in caso contrario
	 */
	public boolean checkWhiteCanArriveFromRight(int riga, int colonna, StateTablut s) {
		if(s.getPawn(riga, colonna).equalsPawn("W")) {
			return false;
		}
		//Ciclo di controllo ostacoli partendo dalla posizione passata fino al bordo
		for(int i=colonna; i<9;i++)
		{
			//Se trova la pedina bianca ritorna risposta positiva
			if(s.getPawn(riga, i).equalsPawn("W"))
			{
				return true;
			}
			//Se trova un ostacolo(pedina nera, trono, cittadella) ritorna risposta negativa
			if(!s.getPawn(riga, i).equalsPawn("O") || this.citadels.contains(s.getBox(riga, i)))
			{
				return false;
			}			
		}		
		return false;
	}
	
	/**
	 * Controlla se una pedina bianca puo' giungere da sotto alla posizione passata come parametro
	 * @param riga Riga della posizione dove vogliamo controllare se puo' arrivare una pedina bianca
	 * @param colonna Colonna della posizione dove vogliamo controllare se puo' arrivare una pedina bianca
	 * @param s StateTablut ovvero lo stato che si vuole valutare
	 * @return True se una pedina bianca puo' arrivare, false in caso contrario
	 */
	public boolean checkWhiteCanArriveFromBottom(int riga, int colonna, StateTablut s) {
		if(s.getPawn(riga, colonna).equalsPawn("W")) {
			return false;
		}
		//Ciclo di controllo ostacoli partendo dalla posizione passata fino al bordo
		for(int i=riga; i<9;i++)
		{
			//Se trova la pedina bianca ritorna risposta positiva
			if(s.getPawn(i, colonna).equalsPawn("W"))
			{
				return true;
			}
			//Se trova un ostacolo(pedina nera, trono, cittadella) ritorna risposta negativa
			if(!s.getPawn(i, colonna).equalsPawn("O") || this.citadels.contains(s.getBox(i, colonna)))
			{
				return false;
			}			
		}
		return false;
	}
	
	/**
	 * Controlla se una pedina bianca puo' giungere da sinistra alla posizione passata come parametro
	 * @param riga Riga della posizione dove vogliamo controllare se puo' arrivare una pedina bianca
	 * @param colonna Colonna della posizione dove vogliamo controllare se puo' arrivare una pedina bianca
	 * @param s StateTablut ovvero lo stato che si vuole valutare
	 * @return True se una pedina bianca puo' arrivare, false in caso contrario
	 */
	public boolean checkWhiteCanArriveFromLeft(int riga, int colonna, StateTablut s) {
		if(s.getPawn(riga, colonna).equalsPawn("W")) {
			return false;
		}
		//Ciclo di controllo ostacoli partendo dalla posizione passata fino al bordo
		for(int i=colonna-1; i>=0;i--)
		{
			//Se trova la pedina bianca ritorna risposta positiva
			if(s.getPawn(riga, i).equalsPawn("W"))
			{
				return true;
			}
			//Se trova un ostacolo(pedina nera, trono, cittadella) ritorna risposta negativa
			if(!s.getPawn(riga, i).equalsPawn("O") || this.citadels.contains(s.getBox(riga, i)))
			{
				return false;
			}			
		}
		return false;
	}
	
	/**
	 * Controlla se esiste un nemico o un accampamento in alto rispetto alla pedina(bianca o nera) posizionata in @riga e @colonna
	 * 
	 * @param riga Riga in cui si trova la pedina
	 * @param colonna Colonna in cui si trova la pedina
	 * @param s StateTablut ovvero lo stato da valutare
	 * @return true se una pedina avversaria o un accampamento è sopra alla pedina passata come parametro, false in caso contrario
	 */
	public boolean enemyOnTheTop(int riga, int colonna, StateTablut s)
	{
		if(riga!=0) {
			//Nel caso la pedina passata come parametro sia una pedina bianca(o il re) allora controlla se c'è una pedina nera(o cittadella) in alto
			if(s.getPawn(riga, colonna).equalsPawn("W") || s.getPawn(riga, colonna).equalsPawn("K")) {
				//Controlla se in alto c'è una pedina nera, il trono o una cittadella
				if(s.getPawn(riga-1, colonna).equalsPawn("B") || s.getPawn(riga-1, colonna).equalsPawn("T") || this.citadels.contains(s.getBox(riga-1,  colonna))){
					return true;
				}
			//Nel caso la pedina passata come parametro sia una pedina nera allora controlla se c'è una pedina bianca(o cittadella) in alto
			} else if(s.getPawn(riga, colonna).equalsPawn("B")) {
				//Caso particolare in cui le pedine nere dentro alle cittadelle non possono essere catturate
				if(this.citadels.contains(s.getBox(riga,  colonna))){
					return false;
				}
				//Controlla se in alto c'è una pedina bianca, il re, il trono o una cittadella
				if(s.getPawn(riga-1, colonna).equalsPawn("W") || s.getPawn(riga-1, colonna).equalsPawn("K")
						|| s.getPawn(riga-1, colonna).equalsPawn("T") || this.citadels.contains(s.getBox(riga-1,  colonna))){
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Controlla se esiste un nemico o un accampamento a destra rispetto alla pedina(bianca o nera) posizionata in @riga e @colonna
	 * 
	 * @param riga Riga in cui si trova la pedina
	 * @param colonna Colonna in cui si trova la pedina
	 * @param s StateTablut ovvero lo stato da valutare
	 * @return true se una pedina avversaria o un accampamento è accanto, sulla destra, alla pedina passata come parametro, false in caso contrario
	 */
	public boolean enemyOnTheRight(int riga, int colonna, StateTablut s)
	{
		if(colonna!=8) {
			//Nel caso la pedina passata come parametro sia una pedina bianca(o il re) allora controlla se c'è una pedina nera(o cittadella) a destra
			if(s.getPawn(riga, colonna).equalsPawn("W") || s.getPawn(riga, colonna).equalsPawn("K")) {
				//Controlla se a destra c'è una pedina nera, il trono o una cittadella
				if(s.getPawn(riga, colonna+1).equalsPawn("B") || s.getPawn(riga, colonna+1).equalsPawn("T") || this.citadels.contains(s.getBox(riga,  colonna+1))){
					return true;
				}
			//Nel caso la pedina passata come parametro sia una pedina nera allora controlla se c'è una pedina bianca(o cittadella) a destra
			} else if(s.getPawn(riga, colonna).equalsPawn("B")) {
				//Caso particolare in cui le pedine nere dentro alle cittadelle non possono essere catturate
				if(this.citadels.contains(s.getBox(riga,  colonna))){
					return false;
				}
				//Controlla se a destra c'è una pedina bianca, il re, il trono o una cittadella
				if(s.getPawn(riga, colonna+1).equalsPawn("W") || s.getPawn(riga, colonna+1).equalsPawn("K")
						|| s.getPawn(riga, colonna+1).equalsPawn("T") || this.citadels.contains(s.getBox(riga,  colonna+1))){
					return true;
				}
			}
		}
		return false;	
	}
	
	/**
	 * Controlla se esiste un nemico o un accampamento in basso rispetto alla pedina(bianca o nera) posizionata in @riga e @colonna
	 * 
	 * @param riga Riga in cui si trova la pedina
	 * @param colonna Colonna in cui si trova la pedina
	 * @param s StateTablut ovvero lo stato da valutare
	 * @return true se una pedina avversaria o un accampamento è sotto alla pedina passata come parametro, false in caso contrario
	 */
	public boolean enemyOnTheBottom(int riga, int colonna, StateTablut s)
	{
		if(riga!=8) {
			//Nel caso la pedina passata come parametro sia una pedina bianca(o il re) allora controlla se c'è una pedina nera(o cittadella) in basso
			if(s.getPawn(riga, colonna).equalsPawn("W") || s.getPawn(riga, colonna).equalsPawn("K")) {
				//Controlla se in basso c'è una pedina nera, il trono o una cittadella
				if(s.getPawn(riga+1, colonna).equalsPawn("B") || s.getPawn(riga+1, colonna).equalsPawn("T") || this.citadels.contains(s.getBox(riga+1,  colonna))){
					return true;
				}
			//Nel caso la pedina passata come parametro sia una pedina nera allora controlla se c'è una pedina bianca(o cittadella) in basso
			} else if(s.getPawn(riga, colonna).equalsPawn("B")) {
				//Caso particolare in cui le pedine nere dentro alle cittadelle non possono essere catturate
				if(this.citadels.contains(s.getBox(riga,  colonna))){
					return false;
				}
				//Controlla se in basso c'è una pedina bianca, il re, il trono o una cittadella
				if(s.getPawn(riga+1, colonna).equalsPawn("W") || s.getPawn(riga+1, colonna).equalsPawn("K")
						|| s.getPawn(riga+1, colonna).equalsPawn("T") || this.citadels.contains(s.getBox(riga+1,  colonna))){
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Controlla se esiste un nemico o un accampamento alla sinistra di una pedina bianca posizionata in @riga e @colonna
	 * 
	 * @param riga Riga in cui si trova la pedina
	 * @param colonna Colonna in cui si trova la pedina
	 * @param s StateTablut ovvero lo stato da valutare
	 * @return true se una pedina nera o un accampamento � accando, sulla sinistra, alla pedina bianca passata come parametro, false in caso contrario
	 */
	public boolean enemyOnTheLeft(int riga, int colonna, StateTablut s)
	{
		if(colonna!=0) {
			//Nel caso la pedina passata come parametro sia una pedina bianca(o il re) allora controlla se c'è una pedina nera(o cittadella) a destra
			if(s.getPawn(riga, colonna).equalsPawn("W") || s.getPawn(riga, colonna).equalsPawn("K")) {
				//Controlla se a destra c'è una pedina nera, il trono o una cittadella
				if(s.getPawn(riga, colonna-1).equalsPawn("B") || s.getPawn(riga, colonna-1).equalsPawn("T") || this.citadels.contains(s.getBox(riga,  colonna-1))){
					return true;
				}
			//Nel caso la pedina passata come parametro sia una pedina nera allora controlla se c'è una pedina bianca(o cittadella) a destra
			} else if(s.getPawn(riga, colonna).equalsPawn("B")) {
				//Caso particolare in cui le pedine nere dentro alle cittadelle non possono essere catturate
				if(this.citadels.contains(s.getBox(riga,  colonna))){
					return false;
				}
				//Controlla se a destra c'è una pedina bianca, il re, il trono o una cittadella
				if(s.getPawn(riga, colonna-1).equalsPawn("W") || s.getPawn(riga, colonna-1).equalsPawn("K")
						|| s.getPawn(riga, colonna-1).equalsPawn("T") || this.citadels.contains(s.getBox(riga,  colonna-1))){
					return true;
				}
			}
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

	/**
	 * Controlla se una pedina è isolata vedendo se c'è almeno un nero nelle vicinanze della pedina passata come valore
	 * 
	 * @param riga Riga inerenente la pedina da controllare
	 * @param colonna Colonna inerente la pedina da controlare
	 * @param s StateTablut ovvero lo stato che si vuole valutare
	 * @return True se la pedina non ha nessun nero nelle 9 celle accanto ad essa
	 */
	public boolean blackIsIsolated(int riga, int colonna, StateTablut s) {
		if(this.checkNeighbourBottom(riga, colonna, s).equals("B") || this.checkNeighbourBottomLeft(riga, colonna, s).equals("B") ||
				this.checkNeighbourBottomRight(riga, colonna, s).equals("B") || this.checkNeighbourTop(riga, colonna, s).equals("B") ||
				this.checkNeighbourTopLeft(riga, colonna, s).equals("B") || this.checkNeighbourTopRight(riga, colonna, s).equals("B") || 
				this.checkNeighbourRight(riga, colonna, s).equals("B") || this.checkNeighbourLeft(riga, colonna, s).equals("B"))
			return false;
		return true;
	}
	
	
}