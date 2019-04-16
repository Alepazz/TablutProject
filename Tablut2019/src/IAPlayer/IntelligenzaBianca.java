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
	
	private float getHeuristicValue(StateTablut s) {
		
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
		
		
		float value =0;
		
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
		
		//controllo se re viene mangiato
		if(this.kingCanBeCaptured(s, rigaRe, colonnaRe))
		{
			return this.MIN_VALUE+1;
		}
		
		//controllo vie di fuga re
		int viedifuga=this.checkVieDiFugaRe(s, rigaRe, colonnaRe);
				
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
		
		//controllo re sul trono
		if(rigaRe==4 && colonnaRe==4 && s.getTurn().equalsTurn("B"))
		{
			//bloccato sopra, destra e sinistra
			if(this.getKingNearBlackUp(rigaRe, colonnaRe, s) && this.getKingNearBlackRight(rigaRe, colonnaRe, s) && this.getKingNearBlackLeft(rigaRe, colonnaRe, s))
			{
				if(this.checkBlackCanArrive(5, 4, s))
				{
					return this.MIN_VALUE+1;
				}
			}
			//bloccato sotto, destra, sinistra
			if(this.getKingNearBlackDown(rigaRe, colonnaRe, s) && this.getKingNearBlackRight(rigaRe, colonnaRe, s) && this.getKingNearBlackLeft(rigaRe, colonnaRe, s))
			{
				if(this.checkBlackCanArrive(3, 4, s))
				{
					return this.MIN_VALUE+1;
				}
			}
			//bloccato sopra, sotto, destra
			if(this.getKingNearBlackUp(rigaRe, colonnaRe, s) && this.getKingNearBlackDown(rigaRe, colonnaRe, s) && this.getKingNearBlackRight(rigaRe, colonnaRe, s))
			{
				if(this.checkBlackCanArrive(4, 3, s))
				{
					return this.MIN_VALUE+1;
				}
			}
			//bloccato sopra, sotto, sinistra
			if(this.getKingNearBlackUp(rigaRe, colonnaRe, s) && this.getKingNearBlackDown(rigaRe, colonnaRe, s) && this.getKingNearBlackLeft(rigaRe, colonnaRe, s))
			{
				if(this.checkBlackCanArrive(4, 5, s))
				{
					return this.MIN_VALUE+1;
				}
			}
		}
		//controllo casella adiacente sopra
		if(rigaRe==3 && colonnaRe==4 && s.getTurn().equalsTurn("B"))
		{
			//bloccato sopra e a destra
			if(this.getKingNearBlackUp(rigaRe, colonnaRe, s) && this.getKingNearBlackRight(rigaRe, colonnaRe, s))
			{
				if(this.checkBlackCanArrive(3, 3, s))
				{
					return this.MIN_VALUE+1;
				}
			}
			//bloccato sinistra e destra
			if(this.getKingNearBlackLeft(rigaRe, colonnaRe, s) && this.getKingNearBlackRight(rigaRe, colonnaRe, s))
			{
				if(this.checkBlackCanArrive(2, 4, s))
				{
					return this.MIN_VALUE+1;
				}
			}
			//bloccato sopra e a sinistra
			if(this.getKingNearBlackUp(rigaRe, colonnaRe, s) && this.getKingNearBlackLeft(rigaRe, colonnaRe, s))
			{
				if(this.checkBlackCanArrive(3, 5, s))
				{
					return this.MIN_VALUE+1;
				}
			}
		}
		//controllo casella adiacente sotto
		if(rigaRe==5 && colonnaRe==4 && s.getTurn().equalsTurn("B"))
		{
			//bloccato destra e sinistra
			if(this.getKingNearBlackLeft(rigaRe, colonnaRe, s) && this.getKingNearBlackRight(rigaRe, colonnaRe, s))
			{
				if(this.checkBlackCanArrive(6, 4, s))
				{
					return this.MIN_VALUE+1;
				}
			}
			//bloccato sotto e a destra
			if(this.getKingNearBlackDown(rigaRe, colonnaRe, s) && this.getKingNearBlackRight(rigaRe, colonnaRe, s))
			{
				if(this.checkBlackCanArrive(5, 3, s))
				{
					return this.MIN_VALUE+1;
				}
			}
			//bloccato sotto e a sinistra
			if(this.getKingNearBlackDown(rigaRe, colonnaRe, s) && this.getKingNearBlackLeft(rigaRe, colonnaRe, s))
			{
				if(this.checkBlackCanArrive(5, 5, s))
				{
					return this.MIN_VALUE+1;
				}
			}
		}
		//controllo casella adiacente destra
		if(rigaRe==4 && colonnaRe==5 && s.getTurn().equalsTurn("B"))
		{
			//bloccato sotto e a destra
			if(this.getKingNearBlackDown(rigaRe, colonnaRe, s) && this.getKingNearBlackRight(rigaRe, colonnaRe, s))
			{
				if(this.checkBlackCanArrive(3, 5, s))
				{
					return this.MIN_VALUE+1;
				}
			}
			//bloccato sopra e a destra
			if(this.getKingNearBlackUp(rigaRe, colonnaRe, s) && this.getKingNearBlackRight(rigaRe, colonnaRe, s))
			{
				if(this.checkBlackCanArrive(5, 5, s))
				{
					return this.MIN_VALUE+1;
				}
			}
			//bloccato sopra e sotto
			if(this.getKingNearBlackUp(rigaRe, colonnaRe, s) && this.getKingNearBlackDown(rigaRe, colonnaRe, s))
			{
				if(this.checkBlackCanArrive(4, 6, s))
				{
					return this.MIN_VALUE+1;
				}
			}			
		}
		//controllo casella adiacente sinistra
		if(rigaRe==4 && colonnaRe==3 && s.getTurn().equalsTurn("B"))
		{
			//bloccato sopra e sotto
			if(this.getKingNearBlackUp(rigaRe, colonnaRe, s) && this.getKingNearBlackDown(rigaRe, colonnaRe, s))
			{
				if(this.checkBlackCanArrive(4, 2, s))
				{
					return this.MIN_VALUE+1;
				}
			}	
			//bloccato sotto e a sinistra
			if(this.getKingNearBlackDown(rigaRe, colonnaRe, s) && this.getKingNearBlackLeft(rigaRe, colonnaRe, s))
			{
				if(this.checkBlackCanArrive(3, 3, s))
				{
					return this.MIN_VALUE+1;
				}
			}//bloccato sopra e a sinistra
			if(this.getKingNearBlackUp(rigaRe, colonnaRe, s) && this.getKingNearBlackLeft(rigaRe, colonnaRe, s))
			{
				if(this.checkBlackCanArrive(5, 3, s))
				{
					return this.MIN_VALUE+1;
				}
			}			
		}
		
		/*
		 * Funzione che controlla se, eseguita una mossa del re, esso ha liberato un'intera colonna, in cui vincere (al 100%) il turno successivo
		 */
		if (this.checkFreeColComingFromLeft(rigaRe, colonnaRe, s) || this.checkFreeColComingFromRight(rigaRe, colonnaRe, s)) {
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

	private boolean kingCanBeCaptured(StateTablut s, int rigaRe, int colonnaRe)
	{
		if(s.getTurn().equalsTurn("B"))
		{
			if(getKingNearBlackRight(rigaRe, colonnaRe, s))
			{
				if(checkBlackCanArrive(rigaRe, colonnaRe-1, s))
				{
					return true;
				}
			}
			if(getKingNearBlackLeft(rigaRe, colonnaRe, s))
			{
				if(checkBlackCanArrive(rigaRe, colonnaRe+1, s))
				{
					return true;
				}
			}
			if(getKingNearBlackUp(rigaRe, colonnaRe, s))
			{
				if(checkBlackCanArrive(rigaRe+1, colonnaRe, s))
				{
					return true;
				}
			}
			if(getKingNearBlackDown(rigaRe, colonnaRe, s))
			{
				if(checkBlackCanArrive(rigaRe-1, colonnaRe, s))
				{
					return true;
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
	private int checkVieDiFugaRe(StateTablut s, int rigaRe, int colonnaRe)
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

	private boolean getKingNearBlackRight(int rigaRe, int colonnaRe, StateTablut s)
	{
		if(s.getPawn(rigaRe, colonnaRe+1).equalsPawn("B") || this.citadels.contains(s.getBox(rigaRe,  colonnaRe+1)))
		{
			return true;
		}
		return false;
	}
	
	
	private boolean getKingNearBlackLeft(int rigaRe, int colonnaRe, StateTablut s)
	{
		if(s.getPawn(rigaRe, colonnaRe-1).equalsPawn("B") || this.citadels.contains(s.getBox(rigaRe,  colonnaRe-1)))
		{
			return true;
		}
		return false;
	}
	
	private boolean getKingNearBlackUp(int rigaRe, int colonnaRe, StateTablut s)
	{
		if(s.getPawn(rigaRe-1, colonnaRe).equalsPawn("B") || this.citadels.contains(s.getBox(rigaRe-1,  colonnaRe)))
		{
			return true;
		}
		return false;
	}
	
	private boolean getKingNearBlackDown(int rigaRe, int colonnaRe, StateTablut s)
	{
		if(s.getPawn(rigaRe+1, colonnaRe).equalsPawn("B") || this.citadels.contains(s.getBox(rigaRe+1,  colonnaRe)))
		{
			return true;
		}
		return false;
	}
	
	/*
	 * Funzione che controlla se il re, muovendosi di una o più mosse da destra a sinistra (orizzontale), arriva ad avere un'intera colonna libera, in cui vincere
	 */
	private boolean checkFreeColComingFromRight(int rigaRe, int colonnaRe, StateTablut s) {
		for(int i=colonnaRe+1; i==6;i++)
		{
			if(s.getPawn(rigaRe, colonnaRe+i).equalsPawn("B") || s.getPawn(rigaRe, colonnaRe+i).equalsPawn("W") || s.getPawn(rigaRe, colonnaRe+i).equalsPawn("T")){
				return false; //c'e' una pedina nera/bianca che intralcia la mossa o c'e' il trono
			}		
		}
		
		colonnaRe = 2; //colonna libera con possibilita' di vittoria
		
		if(!s.getPawn(rigaRe, 0).equalsPawn("B") && 
				!this.citadels.contains(s.getBox(rigaRe, 0)) && 
				checkFreeColTop(rigaRe, colonnaRe, s) && 
				checkFreeColBottom(rigaRe, colonnaRe, s)){
			return true;
		}
		
		return checkFreeColTop(rigaRe, colonnaRe, s) && checkFreeColBottom(rigaRe, colonnaRe, s) && !checkBlackCanArriveFromTop(rigaRe, colonnaRe+1, s); //checkBlack deve essere falso per far ritornare true il return
	}
	
	/*
	 * Funzione che controlla se il re, muovendosi di una o più mosse da sinistra a destra (orizzontale), arriva ad avere un'intera colonna libera, in cui vincere
	 */
	private boolean checkFreeColComingFromLeft(int rigaRe, int colonnaRe, StateTablut s) {
		for(int i=colonnaRe-1; i==2;i--)
		{
			if(s.getPawn(rigaRe, colonnaRe-i).equalsPawn("B") || s.getPawn(rigaRe, colonnaRe-i).equalsPawn("W") || s.getPawn(rigaRe, colonnaRe-i).equalsPawn("T")){
				return false; //c'e' una pedina nera/bianca che intralcia la mossa o c'e' il trono
			}		
		}
		
		colonnaRe = 6; //colonna libera con possibilita' di vittoria
		
		if(!s.getPawn(rigaRe, 8).equalsPawn("B") && 
				!this.citadels.contains(s.getBox(rigaRe, 8)) && 
				checkFreeColTop(rigaRe, colonnaRe, s) && 
				checkFreeColBottom(rigaRe, colonnaRe, s)){
			return true;
		}
		
		return checkFreeColTop(rigaRe, colonnaRe, s) && checkFreeColBottom(rigaRe, colonnaRe, s) && !checkBlackCanArriveFromTop(rigaRe, colonnaRe-1, s); //checkBlack deve essere falso per far ritornare true il return;
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

