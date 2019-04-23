package IAPlayer;

import it.unibo.ai.didattica.competition.tablut.domain.*;
import it.unibo.ai.didattica.competition.tablut.exceptions.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class SimulatorWhite {
	
	private List<String> citadels;
	private List<Nodo> nodiEsistenti;
	
	public SimulatorWhite()
	{
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
	
	public List<Nodo> mossePossibiliComplete(StateTablut stato, State.Turn turno) throws IOException, BoardException, ActionException, StopException, PawnException, DiagonalException, ClimbingException, ThroneException, OccupitedException, ClimbingCitadelException, CitadelException{
		List<Nodo> listaMossePossibili = new ArrayList<Nodo>();
		for(int i=0; i<stato.getBoard().length; i++)
		{
			for(int j=0; j<stato.getBoard().length; j++)
			{
				if(turno.equalsTurn(State.Turn.BLACK.toString()) && State.Pawn.BLACK.equalsPawn(stato.getBoard()[i][j].toString()))
				{
					for(Nodo nod: mossePossibiliPedina(stato, i, j, turno))
					{
						listaMossePossibili.add(nod);
					}
				}
				if(turno.equalsTurn(State.Turn.WHITE.toString()) && (State.Pawn.WHITE.equalsPawn(stato.getBoard()[i][j].toString())|| State.Pawn.KING.equalsPawn(stato.getBoard()[i][j].toString())))
				{
					for(Nodo nod: mossePossibiliPedina(stato, i, j, turno))
					{
						listaMossePossibili.add(nod);
					}
				}
			}
		}	
		return listaMossePossibili;
	}
	
	private List<Nodo> mossePossibiliPedina(StateTablut stato, int i, int j, State.Turn turno) throws IOException, BoardException, ActionException, StopException, PawnException, DiagonalException, ClimbingException, ThroneException, OccupitedException, ClimbingCitadelException, CitadelException
	{
		List<Nodo> listaMossePossibili = new ArrayList<Nodo>();
		if(canMoveUp(stato, i, j))
		{
			for(Nodo nod: mossePossibiliPedinaSopra(stato, i, j, turno))
			{
				listaMossePossibili.add(nod);
			}
		}
		if(canMoveDown(stato, i, j))
		{
			for(Nodo nod: mossePossibiliPedinaSotto(stato, i, j, turno))
			{
				listaMossePossibili.add(nod);
			}
		}
		if(canMoveLeft(stato, i, j))
		{
			for(Nodo nod: mossePossibiliPedinaSinistra(stato, i, j, turno))
			{
				listaMossePossibili.add(nod);
			}
		}
		if(canMoveRight(stato, i, j))
		{
			for(Nodo nod: mossePossibiliPedinaDestra(stato, i, j, turno))
			{
				listaMossePossibili.add(nod);
			}
		}
		return listaMossePossibili;
	}

	private List<Nodo> mossePossibiliPedinaSopra(StateTablut stato, int i, int j, State.Turn turno) throws IOException, BoardException, ActionException, StopException, PawnException, DiagonalException, ClimbingException, ThroneException, OccupitedException, ClimbingCitadelException, CitadelException
	{
		List<Nodo> listaMossePossibili = new ArrayList<Nodo>();
		int c = 0;
		stato.setTurn(turno);
		while(canMoveUp(stato, i-c, j))
		{
			c++;
			//System.out.println(stato.toString());
			Action ac = new Action(stato.getBox(i, j), stato.getBox(i-c, j), turno);
			StateTablut nuovoStato = (StateTablut) this.checkMove(stato, ac);
			//System.out.println(stato.toString());
			//System.out.println(nuovoStato.toString());
			//
			Nodo node = new Nodo(nuovoStato);
			node.setAzione(ac);
			listaMossePossibili.add(node);
		}		
		return listaMossePossibili;
	}
	
	private List<Nodo> mossePossibiliPedinaSotto(StateTablut stato, int i, int j, State.Turn turno) throws IOException, BoardException, ActionException, StopException, PawnException, DiagonalException, ClimbingException, ThroneException, OccupitedException, ClimbingCitadelException, CitadelException
	{
		List<Nodo> listaMossePossibili = new ArrayList<Nodo>();
		int c = 0;
		stato.setTurn(turno);
		while(canMoveDown(stato, i+c, j))
		{
			c++;
			//System.out.println(stato.toString());
			Action ac = new Action(stato.getBox(i, j), stato.getBox(i+c, j), turno);
			StateTablut nuovoStato = (StateTablut) this.checkMove(stato, ac);
			//System.out.println(stato.toString());
			//System.out.println(nuovoStato.toString());
			//stato.setTurn(turno);
			Nodo node = new Nodo(nuovoStato);
			node.setAzione(ac);
			listaMossePossibili.add(node);
		}		
		return listaMossePossibili;
	}
	
	private List<Nodo> mossePossibiliPedinaDestra(StateTablut stato, int i, int j, State.Turn turno) throws IOException, BoardException, ActionException, StopException, PawnException, DiagonalException, ClimbingException, ThroneException, OccupitedException, ClimbingCitadelException, CitadelException
	{
		List<Nodo> listaMossePossibili = new ArrayList<Nodo>();
		int c = 0;
		stato.setTurn(turno);
		while(canMoveRight(stato, i, j+c))
		{
			c++;
			//System.out.println(stato.toString());
			Action ac = new Action(stato.getBox(i, j), stato.getBox(i, j+c), turno);
			StateTablut nuovoStato = (StateTablut) this.checkMove(stato, ac);
			//System.out.println(stato.toString());
			//System.out.println(nuovoStato.toString());
			//stato.setTurn(turno);
			Nodo node = new Nodo(nuovoStato);
			node.setAzione(ac);
			listaMossePossibili.add(node);
		}		
		return listaMossePossibili;
	}
	
	private List<Nodo> mossePossibiliPedinaSinistra(StateTablut stato, int i, int j, State.Turn turno) throws IOException, BoardException, ActionException, StopException, PawnException, DiagonalException, ClimbingException, ThroneException, OccupitedException, ClimbingCitadelException, CitadelException
	{
		List<Nodo> listaMossePossibili = new ArrayList<Nodo>();
		int c = 0;
		stato.setTurn(turno);
		while(canMoveLeft(stato, i, j-c))
		{
			c++;
			//System.out.println(stato.toString());
			Action ac = new Action(stato.getBox(i, j), stato.getBox(i, j-c), turno);
			StateTablut nuovoStato = (StateTablut) this.checkMove(stato, ac);
			//System.out.println(stato.toString());
			//System.out.println(nuovoStato.toString());
			//stato.setTurn(turno);
			Nodo node = new Nodo(nuovoStato);
			node.setAzione(ac);
			listaMossePossibili.add(node);
		}			
		return listaMossePossibili;
	}
	
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
	
	private State checkMove(State stato, Action a) throws BoardException, ActionException, StopException, PawnException, DiagonalException, ClimbingException, ThroneException, OccupitedException, ClimbingCitadelException, CitadelException 
	{
		StateTablut nuovoStato = new StateTablut();
		
		//copia parametri
		State.Turn nuovoTurno;
		if(stato.getTurn().equalsTurn(State.Turn.BLACK.toString()))
		{
			nuovoTurno=State.Turn.BLACK;
		}
		else
		{
			nuovoTurno=State.Turn.WHITE;
		}
		State.Pawn[][] nuovoTavolo = new State.Pawn[9][9];
		for(int i=0; i<9; i++)
		{
			for(int j=0; j<9; j++)
			{
				nuovoTavolo[i][j] = stato.getPawn(i, j);
			}
		}
		
		nuovoStato.setBoard(nuovoTavolo);
		nuovoStato.setTurn(nuovoTurno);
		/*int columnFrom = a.getColumnFrom();
		int columnTo = a.getColumnTo();
		int rowFrom = a.getRowFrom();
		int rowTo = a.getRowTo();
		
		//controllo se sono fuori dal tabellone
		if(columnFrom>nuovoStato.getBoard().length-1 || rowFrom>nuovoStato.getBoard().length-1 || rowTo>nuovoStato.getBoard().length-1 || columnTo>nuovoStato.getBoard().length-1 || columnFrom<0 || rowFrom<0 || rowTo<0 || columnTo<0)
		{
			System.out.println("riga"+rowFrom+"  colonna"+columnFrom);
			throw new BoardException(a);			
		}
		
		//controllo che non vada sul trono
		if(nuovoStato.getPawn(rowTo, columnTo).equalsPawn(State.Pawn.THRONE.toString()))
		{
			throw new ThroneException(a);
		}
		
		//controllo la casella di arrivo
		if(!nuovoStato.getPawn(rowTo, columnTo).equalsPawn(State.Pawn.EMPTY.toString()))
		{
			throw new OccupitedException(a);
		}
		if(this.citadels.contains(nuovoStato.getBox(rowTo, columnTo)) && !this.citadels.contains(nuovoStato.getBox(rowFrom, columnFrom)))
		{
			throw new CitadelException(a);
		}
		if(this.citadels.contains(nuovoStato.getBox(rowTo, columnTo)) && this.citadels.contains(nuovoStato.getBox(rowFrom, columnFrom)))
		{
			if(rowFrom==rowTo)
			{
				if(columnFrom-columnTo>5 || columnFrom-columnTo<-5)
				{
					throw new CitadelException(a);
				}
			}
			else
			{
				if(rowFrom-rowTo>5 || rowFrom-rowTo<-5)
				{
					throw new CitadelException(a);
				}
			}
			
		}
		
		//controllo se cerco di stare fermo
		if(rowFrom==rowTo && columnFrom==columnTo)
		{
			throw new StopException(a);
		}
		
		//controllo se sto muovendo una pedina giusta
		if(nuovoStato.getTurn().equalsTurn(State.Turn.WHITE.toString()))
		{
			if(!nuovoStato.getPawn(rowFrom, columnFrom).equalsPawn("W") && !nuovoStato.getPawn(rowFrom, columnFrom).equalsPawn("K"))
			{
				System.out.println(rowFrom + " "+ columnFrom);
				throw new PawnException(a);
			}
		}
		if(nuovoStato.getTurn().equalsTurn(State.Turn.BLACK.toString()))
		{
			if(!nuovoStato.getPawn(rowFrom, columnFrom).equalsPawn("B"))
			{
				throw new PawnException(a);
			}
		}
		
		//controllo di non muovere in diagonale
		if(rowFrom != rowTo && columnFrom != columnTo)
		{
			throw new DiagonalException(a);
		}
		
		
		//controllo di non scavalcare pedine
		if(rowFrom==rowTo)
		{
			if(columnFrom>columnTo)
			{
				for(int i=columnTo; i<columnFrom; i++)
				{
					if(!nuovoStato.getPawn(rowFrom, i).equalsPawn(State.Pawn.EMPTY.toString()))
					{
						if(nuovoStato.getPawn(rowFrom, i).equalsPawn(State.Pawn.THRONE.toString()))
						{
							throw new ClimbingException(a);
						}
						else 
						{
							throw new ClimbingException(a);
						}
					}
					if(this.citadels.contains(nuovoStato.getBox(rowFrom, i)) && !this.citadels.contains(nuovoStato.getBox(a.getRowFrom(), a.getColumnFrom())))
					{
						throw new ClimbingCitadelException(a);
					}
				}
			}
			else
			{
				for(int i=columnFrom+1; i<=columnTo; i++)
				{
					if(!nuovoStato.getPawn(rowFrom, i).equalsPawn(State.Pawn.EMPTY.toString()))
					{
						if(nuovoStato.getPawn(rowFrom, i).equalsPawn(State.Pawn.THRONE.toString()))
						{
							throw new ClimbingException(a);
						}
						else 
						{
							throw new ClimbingException(a);
						}
					}
					if(this.citadels.contains(nuovoStato.getBox(rowFrom, i)) && !this.citadels.contains(nuovoStato.getBox(a.getRowFrom(), a.getColumnFrom())))
					{
						throw new ClimbingCitadelException(a);
					}
				}
			}
		}
		else
		{
			if(rowFrom>rowTo)
			{
				for(int i=rowTo; i<rowFrom; i++)
				{
					if(!nuovoStato.getPawn(i, columnFrom).equalsPawn(State.Pawn.EMPTY.toString()))
					{
						if(nuovoStato.getPawn(i, columnFrom).equalsPawn(State.Pawn.THRONE.toString()))
						{
							throw new ClimbingException(a);
						}
						else 
						{
							throw new ClimbingException(a);
						}
					}
					if(this.citadels.contains(nuovoStato.getBox(i, columnFrom)) && !this.citadels.contains(nuovoStato.getBox(a.getRowFrom(), a.getColumnFrom())))
					{
						throw new ClimbingCitadelException(a);
					}
				}
			}
			else
			{
				for(int i=rowFrom+1; i<=rowTo; i++)
				{
					if(!nuovoStato.getPawn(i, columnFrom).equalsPawn(State.Pawn.EMPTY.toString()))
					{
						if(nuovoStato.getPawn(i, columnFrom).equalsPawn(State.Pawn.THRONE.toString()))
						{
							throw new ClimbingException(a);
						}
						else 
						{
							throw new ClimbingException(a);
						}
					}
					if(this.citadels.contains(nuovoStato.getBox(i, columnFrom)) && !this.citadels.contains(nuovoStato.getBox(a.getRowFrom(), a.getColumnFrom())))
					{
						throw new ClimbingCitadelException(a);
					}
				}
			}
		}*/
		
		//se sono arrivato qui, muovo la pedina
		nuovoStato = (StateTablut) this.movePawn(nuovoStato, a);
		
		//a questo punto controllo lo stato per eventuali catture
		if(nuovoStato.getTurn().equalsTurn("W"))
		{
			nuovoStato = (StateTablut) this.checkCaptureBlack(nuovoStato, a);
		}
		if(nuovoStato.getTurn().equalsTurn("B"))
		{
			nuovoStato = (StateTablut) this.checkCaptureWhite(nuovoStato, a);
		}
		return nuovoStato;
	}
	
	private State movePawn(State state, Action a) {
		State.Pawn pawn = state.getPawn(a.getRowFrom(), a.getColumnFrom());
		State.Pawn[][] newBoard = state.getBoard();
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
		state.setBoard(newBoard);
		//cambio il turno
		if(state.getTurn().equalsTurn(State.Turn.WHITE.toString()))
		{
			state.setTurn(State.Turn.BLACK);
		}
		else
		{
			state.setTurn(State.Turn.WHITE);
		}
		
		
		return state;
	}

	private State checkCaptureWhite(State state, Action a) {
		// controllo se mangio a destra
		if (a.getColumnTo() < state.getBoard().length - 2
				&& state.getPawn(a.getRowTo(), a.getColumnTo() + 1).equalsPawn("B")
				&& (state.getPawn(a.getRowTo(), a.getColumnTo() + 2).equalsPawn("W")
						|| state.getPawn(a.getRowTo(), a.getColumnTo() + 2).equalsPawn("T")
						|| state.getPawn(a.getRowTo(), a.getColumnTo() + 2).equalsPawn("K")
						|| (this.citadels.contains(state.getBox(a.getRowTo(), a.getColumnTo() + 2)) &&(a.getColumnTo()+2!=8&&a.getRowTo()!=4)&&(a.getColumnTo()+2!=4&&a.getRowTo()!=0)&&(a.getColumnTo()+2!=4&&a.getRowTo()!=0)&&(a.getColumnTo()+2!=0&&a.getRowTo()!=4)))) {
			state.removePawn(a.getRowTo(), a.getColumnTo() + 1);
		}
		// controllo se mangio a sinistra
		if (a.getColumnTo() > 1 && state.getPawn(a.getRowTo(), a.getColumnTo() - 1).equalsPawn("B")
				&& (state.getPawn(a.getRowTo(), a.getColumnTo() - 2).equalsPawn("W")
						|| state.getPawn(a.getRowTo(), a.getColumnTo() - 2).equalsPawn("T")
						|| state.getPawn(a.getRowTo(), a.getColumnTo() - 2).equalsPawn("K")
						|| (this.citadels.contains(state.getBox(a.getRowTo(), a.getColumnTo() - 2)) &&(a.getColumnTo()-2!=8&&a.getRowTo()!=4)&&(a.getColumnTo()-2!=4&&a.getRowTo()!=0)&&(a.getColumnTo()-2!=4&&a.getRowTo()!=0)&&(a.getColumnTo()-2!=0&&a.getRowTo()!=4)))) {
			state.removePawn(a.getRowTo(), a.getColumnTo() - 1);
		}
		// controllo se mangio sopra
		if (a.getRowTo() > 1 && state.getPawn(a.getRowTo() - 1, a.getColumnTo()).equalsPawn("B")
				&& (state.getPawn(a.getRowTo() - 2, a.getColumnTo()).equalsPawn("W")
						|| state.getPawn(a.getRowTo() - 2, a.getColumnTo()).equalsPawn("T")
						|| state.getPawn(a.getRowTo() - 2, a.getColumnTo()).equalsPawn("K")
						|| (this.citadels.contains(state.getBox(a.getRowTo() - 2, a.getColumnTo()))&&(a.getColumnTo()!=8&&a.getRowTo()-2!=4)&&(a.getColumnTo()!=4&&a.getRowTo()-2!=0)&&(a.getColumnTo()!=4&&a.getRowTo()-2!=0)&&(a.getColumnTo()!=0&&a.getRowTo()-2!=4)) )) {
			state.removePawn(a.getRowTo() - 1, a.getColumnTo());

		}
		// controllo se mangio sotto
		if (a.getRowTo() < state.getBoard().length - 2
				&& state.getPawn(a.getRowTo() + 1, a.getColumnTo()).equalsPawn("B")
				&& (state.getPawn(a.getRowTo() + 2, a.getColumnTo()).equalsPawn("W")
						|| state.getPawn(a.getRowTo() + 2, a.getColumnTo()).equalsPawn("T")
						|| state.getPawn(a.getRowTo() + 2, a.getColumnTo()).equalsPawn("K")
						|| (this.citadels.contains(state.getBox(a.getRowTo() + 2, a.getColumnTo()))&&(a.getColumnTo()!=8&&a.getRowTo()+2!=4)&&(a.getColumnTo()!=4&&a.getRowTo()+2!=0)&&(a.getColumnTo()!=4&&a.getRowTo()+2!=0)&&(a.getColumnTo()!=0&&a.getRowTo()+2!=4)))) {
			state.removePawn(a.getRowTo() + 1, a.getColumnTo());

		}
		// controllo se ho vinto
		if (a.getRowTo() == 0 || a.getRowTo() == state.getBoard().length - 1 || a.getColumnTo() == 0
				|| a.getColumnTo() == state.getBoard().length - 1) {
			if (state.getPawn(a.getRowTo(), a.getColumnTo()).equalsPawn("K")) {
				state.setTurn(State.Turn.WHITEWIN);
			}
		}

		return state;
	}

	private State checkCaptureBlack(State state, Action a)
	{
		//controllo se mangio a destra
		if(a.getColumnTo()<state.getBoard().length-2 && (state.getPawn(a.getRowTo(), a.getColumnTo()+1).equalsPawn("W")||state.getPawn(a.getRowTo(), a.getColumnTo()+1).equalsPawn("K")) && (state.getPawn(a.getRowTo(), a.getColumnTo()+2).equalsPawn("B")||state.getPawn(a.getRowTo(), a.getColumnTo()+2).equalsPawn("T")||this.citadels.contains(state.getBox(a.getRowTo(), a.getColumnTo()+2))))
		{
			//nero-re-trono N.B. No indexOutOfBoundException perchè se il re si trovasse sul bordo il giocatore bianco avrebbe già vinto
			if(state.getPawn(a.getRowTo(), a.getColumnTo()+1).equalsPawn("K") && state.getPawn(a.getRowTo(), a.getColumnTo()+2).equalsPawn("T"))
			{
				//ho circondato su 3 lati il re?
				if((state.getPawn(a.getRowTo()+1, a.getColumnTo()+1).equalsPawn("B")||this.citadels.contains(state.getBox(a.getRowTo()+1, a.getColumnTo()+1))) && (state.getPawn(a.getRowTo()-1, a.getColumnTo()+1).equalsPawn("B")||this.citadels.contains(state.getBox(a.getRowTo()-1, a.getColumnTo()+1))))
				{
					state.setTurn(State.Turn.BLACKWIN);
				}
			}
			//nero-re-nero
			if(state.getPawn(a.getRowTo(), a.getColumnTo()+1).equalsPawn("K") && (state.getPawn(a.getRowTo(), a.getColumnTo()+2).equalsPawn("B")||this.citadels.contains(state.getBox(a.getRowTo(), a.getColumnTo()+2))))
			{
				//mangio il re?
				if(!state.getPawn(a.getRowTo()+1, a.getColumnTo()+1).equalsPawn("T") && !state.getPawn(a.getRowTo()-1, a.getColumnTo()+1).equalsPawn("T"))
				{
					if(!(a.getRowTo()*2 + 1==9 && state.getBoard().length==9) && !(a.getRowTo()*2 + 1==7 && state.getBoard().length==7))
					{
						state.setTurn(State.Turn.BLACKWIN);
					}	
				}						
				//ho circondato su 3 lati il re?
				if((state.getPawn(a.getRowTo()+1, a.getColumnTo()+1).equalsPawn("B")||this.citadels.contains(state.getBox(a.getRowTo()+1, a.getColumnTo()+1))) && state.getPawn(a.getRowTo()-1, a.getColumnTo()+1).equalsPawn("T"))
				{
					state.setTurn(State.Turn.BLACKWIN);
				}
				if(state.getPawn(a.getRowTo()+1, a.getColumnTo()+1).equalsPawn("T") && state.getPawn(a.getRowTo()-1, a.getColumnTo()+1).equalsPawn("B"))
				{
					state.setTurn(State.Turn.BLACKWIN);
				}
			}			
			//nero-bianco-trono/nero/citadel
			if(state.getPawn(a.getRowTo(), a.getColumnTo()+1).equalsPawn("W"))
			{
				state.removePawn(a.getRowTo(), a.getColumnTo()+1);
			}
			
		}
		//controllo se mangio a sinistra
		if(a.getColumnTo()>1 && (state.getPawn(a.getRowTo(), a.getColumnTo()-1).equalsPawn("W")||state.getPawn(a.getRowTo(), a.getColumnTo()-1).equalsPawn("K")) && (state.getPawn(a.getRowTo(), a.getColumnTo()-2).equalsPawn("B")||state.getPawn(a.getRowTo(), a.getColumnTo()-2).equalsPawn("T")||this.citadels.contains(state.getBox(a.getRowTo(), a.getColumnTo()-2))))
		{
			//trono-re-nero
			if(state.getPawn(a.getRowTo(), a.getColumnTo()-1).equalsPawn("K") && state.getPawn(a.getRowTo(), a.getColumnTo()-2).equalsPawn("T"))
			{
				//ho circondato su 3 lati il re?
				if((state.getPawn(a.getRowTo()+1, a.getColumnTo()-1).equalsPawn("B")||this.citadels.contains(state.getBox(a.getRowTo()+1, a.getColumnTo()-1))) && (state.getPawn(a.getRowTo()-1, a.getColumnTo()-1).equalsPawn("B")||this.citadels.contains(state.getBox(a.getRowTo()-1, a.getColumnTo()-1))))
				{
					state.setTurn(State.Turn.BLACKWIN);
				}
			}
			//nero-re-nero
			if(state.getPawn(a.getRowTo(), a.getColumnTo()-1).equalsPawn("K") && (state.getPawn(a.getRowTo(), a.getColumnTo()-2).equalsPawn("B")||this.citadels.contains(state.getBox(a.getRowTo(), a.getColumnTo()-2))))
			{
				//mangio il re?
				if(!state.getPawn(a.getRowTo()+1, a.getColumnTo()-1).equalsPawn("T") && !state.getPawn(a.getRowTo()-1, a.getColumnTo()-1).equalsPawn("T"))
				{
					if(!(a.getRowTo()*2 + 1==9 && state.getBoard().length==9) && !(a.getRowTo()*2 + 1==7 && state.getBoard().length==7))
					{
						state.setTurn(State.Turn.BLACKWIN);
					}
				}
				//ho circondato su 3 lati il re?
				if((state.getPawn(a.getRowTo()+1, a.getColumnTo()-1).equalsPawn("B")||this.citadels.contains(state.getBox(a.getRowTo()+1, a.getColumnTo()-1))) && state.getPawn(a.getRowTo()-1, a.getColumnTo()-1).equalsPawn("T"))
				{
					state.setTurn(State.Turn.BLACKWIN);
				}
				if(state.getPawn(a.getRowTo()+1, a.getColumnTo()-1).equalsPawn("T") && (state.getPawn(a.getRowTo()-1, a.getColumnTo()-1).equalsPawn("B")||this.citadels.contains(state.getBox(a.getRowTo()-1, a.getColumnTo()-1))))
				{
					state.setTurn(State.Turn.BLACKWIN);
				}
			}
			//trono/nero/citadel-bianco-nero
			if(state.getPawn(a.getRowTo(), a.getColumnTo()-1).equalsPawn("W"))
			{
				state.removePawn(a.getRowTo(), a.getColumnTo()-1);
			}
		}
		//controllo se mangio sopra
		if(a.getRowTo()>1 && (state.getPawn(a.getRowTo()-1, a.getColumnTo()).equalsPawn("W")||state.getPawn(a.getRowTo()-1, a.getColumnTo()).equalsPawn("K")) && (state.getPawn(a.getRowTo()-2, a.getColumnTo()).equalsPawn("B")||state.getPawn(a.getRowTo()-2, a.getColumnTo()).equalsPawn("T")||this.citadels.contains(state.getBox(a.getRowTo()-2, a.getColumnTo()))))
		{
			//nero-re-trono 
			if(state.getPawn(a.getRowTo()-1, a.getColumnTo()).equalsPawn("K") && state.getPawn(a.getRowTo()-2, a.getColumnTo()).equalsPawn("T"))
			{
				//ho circondato su 3 lati il re?
				if((state.getPawn(a.getRowTo()-1, a.getColumnTo()-1).equalsPawn("B")||this.citadels.contains(state.getBox(a.getRowTo()-1, a.getColumnTo()-1))) && (state.getPawn(a.getRowTo()-1, a.getColumnTo()+1).equalsPawn("B")||this.citadels.contains(state.getBox(a.getRowTo()-1, a.getColumnTo()+1))))
				{
					state.setTurn(State.Turn.BLACKWIN);
				}
			}			
			//nero-re-nero
			if(state.getPawn(a.getRowTo()-1, a.getColumnTo()).equalsPawn("K") && (state.getPawn(a.getRowTo()-2, a.getColumnTo()).equalsPawn("B")||this.citadels.contains(state.getBox(a.getRowTo()-2, a.getColumnTo()))))
			{
				//ho circondato su 3 lati il re?
				if((state.getPawn(a.getRowTo()-1, a.getColumnTo()-1).equalsPawn("B")||this.citadels.contains(state.getBox(a.getRowTo()-1, a.getColumnTo()-1))) && state.getPawn(a.getRowTo()-1, a.getColumnTo()+1).equalsPawn("T"))
				{
					state.setTurn(State.Turn.BLACKWIN);
				}
				if(state.getPawn(a.getRowTo()-1, a.getColumnTo()-1).equalsPawn("T") && (state.getPawn(a.getRowTo()-1, a.getColumnTo()+1).equalsPawn("B")||this.citadels.contains(state.getBox(a.getRowTo()-1, a.getColumnTo()+1))))
				{
					state.setTurn(State.Turn.BLACKWIN);
				}
				//mangio il re?
				if(!state.getPawn(a.getRowTo()-1, a.getColumnTo()-1).equalsPawn("T") && !state.getPawn(a.getRowTo()-1, a.getColumnTo()+1).equalsPawn("T"))
				{
					if(!(a.getRowTo()*2 + 1==9 && state.getBoard().length==9) && !(a.getRowTo()*2 + 1==7 && state.getBoard().length==7))
					{
						state.setTurn(State.Turn.BLACKWIN);
					}
				}
			}			
			//nero-bianco-trono/nero/citadel
			if(state.getPawn(a.getRowTo()-1, a.getColumnTo()).equalsPawn("W"))
			{
				state.removePawn(a.getRowTo()-1, a.getColumnTo());
			}
		}
		//controllo se mangio sotto
		if(a.getRowTo()<state.getBoard().length-2 && (state.getPawn(a.getRowTo()+1, a.getColumnTo()).equalsPawn("W")||state.getPawn(a.getRowTo()+1, a.getColumnTo()).equalsPawn("K")) && (state.getPawn(a.getRowTo()+2, a.getColumnTo()).equalsPawn("B")||state.getPawn(a.getRowTo()+2, a.getColumnTo()).equalsPawn("T")||this.citadels.contains(state.getBox(a.getRowTo()+2, a.getColumnTo()))))
		{
			//nero-re-trono
			if(state.getPawn(a.getRowTo()+1, a.getColumnTo()).equalsPawn("K") && state.getPawn(a.getRowTo()+2, a.getColumnTo()).equalsPawn("T"))
			{
				//ho circondato su 3 lati il re?
				if((state.getPawn(a.getRowTo()+1, a.getColumnTo()-1).equalsPawn("B")||this.citadels.contains(state.getBox(a.getRowTo()+1, a.getColumnTo()-1))) && (state.getPawn(a.getRowTo()+1, a.getColumnTo()+1).equalsPawn("B")||this.citadels.contains(state.getBox(a.getRowTo()+1, a.getColumnTo()+1))))
				{
					state.setTurn(State.Turn.BLACKWIN);
				}
			}			
			//nero-re-nero
			if(state.getPawn(a.getRowTo()+1, a.getColumnTo()).equalsPawn("K") && (state.getPawn(a.getRowTo()+2, a.getColumnTo()).equalsPawn("B")||this.citadels.contains(state.getBox(a.getRowTo()+2, a.getColumnTo()))))
			{
				//ho circondato su 3 lati il re?
				if((state.getPawn(a.getRowTo()+1, a.getColumnTo()-1).equalsPawn("B")||this.citadels.contains(state.getBox(a.getRowTo()+1, a.getColumnTo()-1))) && state.getPawn(a.getRowTo()+1, a.getColumnTo()+1).equalsPawn("T"))
				{
					state.setTurn(State.Turn.BLACKWIN);
				}
				if(state.getPawn(a.getRowTo()+1, a.getColumnTo()-1).equalsPawn("T") && (state.getPawn(a.getRowTo()+1, a.getColumnTo()+1).equalsPawn("B")||this.citadels.contains(state.getBox(a.getRowTo()+1, a.getColumnTo()+1))))
				{
					state.setTurn(State.Turn.BLACKWIN);
				}
				//mangio il re?
				if(!state.getPawn(a.getRowTo()+1, a.getColumnTo()+1).equalsPawn("T") && !state.getPawn(a.getRowTo()+1, a.getColumnTo()-1).equalsPawn("T"))
				{
					if(!(a.getRowTo()*2 + 1==9 && state.getBoard().length==9) && !(a.getRowTo()*2 + 1==7 && state.getBoard().length==7))
					{
						state.setTurn(State.Turn.BLACKWIN);
					}
				}
			}		
			//nero-bianco-trono/nero
			if(state.getPawn(a.getRowTo()+1, a.getColumnTo()).equalsPawn("W"))
			{
				state.removePawn(a.getRowTo()+1, a.getColumnTo());
			}			
		}
		//controllo il re completamente circondato
		if(state.getPawn(4, 4).equalsPawn(State.Pawn.KING.toString()) && state.getBoard().length==9)
		{
			if(state.getPawn(3, 4).equalsPawn("B") && state.getPawn(4, 3).equalsPawn("B") && state.getPawn(5, 4).equalsPawn("B") && state.getPawn(4, 5).equalsPawn("B"))
			{
				state.setTurn(State.Turn.BLACKWIN);
			}
		}
		if(state.getPawn(3, 3).equalsPawn(State.Pawn.KING.toString()) && state.getBoard().length==7)
		{
			if(state.getPawn(3, 4).equalsPawn("B") && state.getPawn(4, 3).equalsPawn("B") && state.getPawn(2, 3).equalsPawn("B") && state.getPawn(3, 2).equalsPawn("B"))
			{
				state.setTurn(State.Turn.BLACKWIN);
			}
		}
		//controllo regola 11
		if(state.getBoard().length==9)
		{
			if(a.getColumnTo()==4 && a.getRowTo()==2)
			{
				if(state.getPawn(3, 4).equalsPawn("W") && state.getPawn(4, 4).equalsPawn("K") && state.getPawn(4, 3).equalsPawn("B") && state.getPawn(4, 5).equalsPawn("B") && state.getPawn(5, 4).equalsPawn("B"))
				{
					state.removePawn(3, 4);
				}
			}
			if(a.getColumnTo()==4 && a.getRowTo()==6)
			{
				if(state.getPawn(5, 4).equalsPawn("W") && state.getPawn(4, 4).equalsPawn("K") && state.getPawn(4, 3).equalsPawn("B") && state.getPawn(4, 5).equalsPawn("B") && state.getPawn(3, 4).equalsPawn("B"))
				{
					state.removePawn(5, 4);
				}
			}
			if(a.getColumnTo()==2 && a.getRowTo()==4)
			{
				if(state.getPawn(4, 3).equalsPawn("W") && state.getPawn(4, 4).equalsPawn("K") && state.getPawn(3, 4).equalsPawn("B") && state.getPawn(5, 4).equalsPawn("B") && state.getPawn(4, 5).equalsPawn("B"))
				{
					state.removePawn(4, 3);
				}
			}
			if(a.getColumnTo()==6 && a.getRowTo()==4)
			{
				if(state.getPawn(4, 5).equalsPawn("W") && state.getPawn(4, 4).equalsPawn("K") && state.getPawn(4, 3).equalsPawn("B") && state.getPawn(5, 4).equalsPawn("B") && state.getPawn(3, 4).equalsPawn("B"))
				{
					state.removePawn(4, 5);
				}
			}
		}
		return state;
	}

	

}
