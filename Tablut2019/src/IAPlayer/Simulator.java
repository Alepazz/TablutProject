package IAPlayer;

import it.unibo.ai.didattica.competition.tablut.domain.*;
import it.unibo.ai.didattica.competition.tablut.exceptions.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Simulator {
	
	private int cache_size;
	private int repeated_moves_allowed;
	private List<String> citadels;
	private int movesWithutCapturing;
	private List<State> drawConditions;
	
	public Simulator()
	{
		this.cache_size = 20;
		this.repeated_moves_allowed = 2;
		this.drawConditions = new ArrayList<State>();
		this.movesWithutCapturing=0;
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
	
	public boolean assiSimmetrici(StateTablut s)
	{
		for(int i=0; i<8; i++)
		{
			if(!s.getPawn(i, 4).equalsPawn(s.getPawn(4, i).toString()))
			{
				return false;
			}
		}
		return true;
	}
	
	//controllo che lo stato sia simmetrico Verticalmente(asse di simmetrica colonna e)
	public boolean statoSimmetricoVerticalmente(StateTablut s)
	{
		//ciclo ogni riga
		for(int i=0; i<9; i++)
		{
			for(int j=0; j<4; j++)
			{
				if(!s.getPawn(i, j).equalsPawn(s.getPawn(i, 8-j).toString()))
				{
					return false;
				}
			}
		}
		return true;
	}
	
	//controllo che lo stato sia simmetrico Orizontalmente(asse di simmetrica riga 5)
	public boolean statoSimmetricoOrizontalmente(StateTablut s)
		{
			//ciclo righe
			for(int i=0; i<4; i++)
			{
				for(int j=0; j<9; j++)
				{
					if(!s.getPawn(i, j).equalsPawn(s.getPawn(8-i, j).toString()))
					{
						return false;
					}
				}
			}
			return true;
		}
	
	//restituisce tutti i nodi a cui è possibile arrivare a partire dal nodo passato
	public List<Nodo> mossePossibiliComplete(Nodo node) throws IOException, BoardException, ActionException, StopException, PawnException, DiagonalException, ClimbingException, ThroneException, OccupitedException, ClimbingCitadelException, CitadelException{
		
		List<Nodo> listaMossePossibili = new ArrayList<Nodo>();
		boolean simmV = this.statoSimmetricoVerticalmente(node.getStato());
		boolean simmO = this.statoSimmetricoOrizontalmente(node.getStato());
		boolean statiSimm = this.assiSimmetrici(node.getStato());
		//itero su tutta la scacchiera
		int righeDaControllare = 9;
		int colonneDaControllare = 9;
		if(simmV)
		{
			colonneDaControllare = 5;
		}
		if(simmO)
		{
			righeDaControllare = 5;
		}
		if(simmV && simmO && statiSimm)
		{
			righeDaControllare = 4;
		}
		
		
		//prima le righe
		for(int i=0; i<righeDaControllare; i++)
		{
			//poi le colonne
			for(int j=0; j<colonneDaControllare; j++)
			{
				//se è il turno nero conto le mosse delle pedine nere
				if(node.getTurn().equalsTurn(State.Turn.BLACK.toString()) && State.Pawn.BLACK.equalsPawn(node.getBoard()[i][j].toString()))
				{
					if(statiSimm && j==4)
					{
						for(Nodo nod: mossePossibiliPedinaCS(node, i, j))
						{
							listaMossePossibili.add(nod);
						}
					}
					else
					{
						if(simmV && simmO && j==4)
						{
							for(Nodo nod: mossePossibiliPedinaCCS(node, i, j))
							{
								listaMossePossibili.add(nod);
							}
						}
						else
						{
							if(simmV && j==4)
							{
								for(Nodo nod: mossePossibiliPedinaSV(node, i, j))
								{
									listaMossePossibili.add(nod);
								}
							}
							else
							{
								if(simmO && i==4)
								{
									for(Nodo nod: mossePossibiliPedinaSO(node, i, j))
									{
										listaMossePossibili.add(nod);
									}
								}
								else
								{
									for(Nodo nod: mossePossibiliPedina(node, i, j))
									{
										listaMossePossibili.add(nod);
									}
								}
							}	
						}	
					}
							
				}
				
				//se è il turno bianco conto le mosse delle pedine bianche
				if(node.getTurn().equalsTurn(State.Turn.WHITE.toString())) 
				{
					if(node.getStato().getPawn(i, j).equalsPawn("W") || node.getStato().getPawn(i, j).equalsPawn("K"))
					{
						if(statiSimm && j==4)
						{
							for(Nodo nod: mossePossibiliPedinaCS(node, i, j))
							{
								listaMossePossibili.add(nod);
							}
						}
						else
						{
							if(simmV && simmO && j==4)
							{
								for(Nodo nod: mossePossibiliPedinaCCS(node, i, j))
								{
									listaMossePossibili.add(nod);
								}
							}
							else
							{
								if(simmV && j==4)
								{
									for(Nodo nod: mossePossibiliPedinaSV(node, i, j))
									{
										listaMossePossibili.add(nod);
									}
								}
								else
								{
									if(simmO && i==4)
									{
										for(Nodo nod: mossePossibiliPedinaSO(node, i, j))
										{
											listaMossePossibili.add(nod);
										}
									}
									else
									{
										for(Nodo nod: mossePossibiliPedina(node, i, j))
										{
											listaMossePossibili.add(nod);
										}
									}
								}	
							}		
						}	
					}
				}
			}
		}
		for(Nodo n : listaMossePossibili)
		{
			n.setPadre(node);
		}
		return listaMossePossibili;
	}
	
	
	//ritorna i nodi nei quali è possibile trovarsi col movimento della pedina bianca indicata
	private List<Nodo> mossePossibiliPedina(Nodo node, int riga, int colonna) throws IOException, BoardException, ActionException, StopException, PawnException, DiagonalException, ClimbingException, ThroneException, OccupitedException, ClimbingCitadelException, CitadelException
		{
			List<Nodo> listaMossePossibili = new ArrayList<Nodo>();
			if(canMoveUp(node.getStato(), riga, colonna))
			{
				for(Nodo nod: mossePossibiliPedinaSopra(node, riga, colonna))
				{
					listaMossePossibili.add(nod);
				}
			}
			if(canMoveDown(node.getStato(), riga, colonna))
			{
				
				for(Nodo nod: mossePossibiliPedinaSotto(node, riga, colonna))
				{
					listaMossePossibili.add(nod);
				}
			}
			if(canMoveLeft(node.getStato(), riga, colonna))
			{
				for(Nodo nod: mossePossibiliPedinaSinistra(node, riga, colonna))
				{
					listaMossePossibili.add(nod);
				}
			}
			if(canMoveRight(node.getStato(), riga, colonna))
			{
				for(Nodo nod: mossePossibiliPedinaDestra(node, riga, colonna))
				{
					listaMossePossibili.add(nod);
				}
			}
			return listaMossePossibili;
		}
	
	private List<Nodo> mossePossibiliPedinaCCS(Nodo node, int riga, int colonna) throws IOException, BoardException, ActionException, StopException, PawnException, DiagonalException, ClimbingException, ThroneException, OccupitedException, ClimbingCitadelException, CitadelException
	{
		List<Nodo> listaMossePossibili = new ArrayList<Nodo>();
		if(canMoveUp(node.getStato(), riga, colonna))
		{
			for(Nodo nod: mossePossibiliPedinaSopra(node, riga, colonna))
			{
				listaMossePossibili.add(nod);
			}
		}
		if(canMoveLeft(node.getStato(), riga, colonna))
		{
			for(Nodo nod: mossePossibiliPedinaSinistra(node, riga, colonna))
			{
				listaMossePossibili.add(nod);
			}
		}
		return listaMossePossibili;
	}
	
	private List<Nodo> mossePossibiliPedinaCS(Nodo node, int riga, int colonna) throws IOException, BoardException, ActionException, StopException, PawnException, DiagonalException, ClimbingException, ThroneException, OccupitedException, ClimbingCitadelException, CitadelException
	{
		List<Nodo> listaMossePossibili = new ArrayList<Nodo>();
		if(canMoveLeft(node.getStato(), riga, colonna))
		{
			for(Nodo nod: mossePossibiliPedinaSinistra(node, riga, colonna))
			{
				listaMossePossibili.add(nod);
			}
		}
		return listaMossePossibili;
	}
	
	private List<Nodo> mossePossibiliPedinaSV(Nodo node, int riga, int colonna) throws IOException, BoardException, ActionException, StopException, PawnException, DiagonalException, ClimbingException, ThroneException, OccupitedException, ClimbingCitadelException, CitadelException
	{
		List<Nodo> listaMossePossibili = new ArrayList<Nodo>();
		if(canMoveUp(node.getStato(), riga, colonna))
		{
			for(Nodo nod: mossePossibiliPedinaSopra(node, riga, colonna))
			{
				listaMossePossibili.add(nod);
			}
		}
		if(canMoveDown(node.getStato(), riga, colonna))
		{
			
			for(Nodo nod: mossePossibiliPedinaSotto(node, riga, colonna))
			{
				listaMossePossibili.add(nod);
			}
		}
		if(canMoveLeft(node.getStato(), riga, colonna))
		{
			for(Nodo nod: mossePossibiliPedinaSinistra(node, riga, colonna))
			{
				listaMossePossibili.add(nod);
			}
		}
		return listaMossePossibili;
	}
	
	private List<Nodo> mossePossibiliPedinaSO(Nodo node, int riga, int colonna) throws IOException, BoardException, ActionException, StopException, PawnException, DiagonalException, ClimbingException, ThroneException, OccupitedException, ClimbingCitadelException, CitadelException
	{
		List<Nodo> listaMossePossibili = new ArrayList<Nodo>();
		if(canMoveUp(node.getStato(), riga, colonna))
		{
			for(Nodo nod: mossePossibiliPedinaSopra(node, riga, colonna))
			{
				listaMossePossibili.add(nod);
			}
		}
		if(canMoveLeft(node.getStato(), riga, colonna))
		{
			for(Nodo nod: mossePossibiliPedinaSinistra(node, riga, colonna))
			{
				listaMossePossibili.add(nod);
			}
		}
		if(canMoveRight(node.getStato(), riga, colonna))
		{
			for(Nodo nod: mossePossibiliPedinaDestra(node, riga, colonna))
			{
				listaMossePossibili.add(nod);
			}
		}
		return listaMossePossibili;
	}
	

	//ritorna i nodi nei quali è possibile trovarsi col movimento verso l'alto della pedina indicata
	private List<Nodo> mossePossibiliPedinaSopra(Nodo node, int riga, int colonna) throws IOException, BoardException, ActionException, StopException, PawnException, DiagonalException, ClimbingException, ThroneException, OccupitedException, ClimbingCitadelException, CitadelException
	{
		List<Nodo> listaMossePossibili = new ArrayList<Nodo>();
		int c = 0;
		//stato.setTurn(turno);
		while(canMoveUp(node.getStato(), riga-c, colonna))
		{
			c++;
			Action ac = new Action(node.getStato().getBox(riga, colonna), node.getStato().getBox(riga-c, colonna), node.getTurn());
			StateTablut nuovoStato = (StateTablut) this.checkMove(node.getStato(), ac);
			Nodo nodo2 = new Nodo(nuovoStato);
			nodo2.setAzione(ac);
			listaMossePossibili.add(nodo2);
			//System.out.println(ac);
		}
		return listaMossePossibili;
	}
	
	//ritorna i nodi nei quali è possibile trovarsi col movimento verso il basso della pedina indicata
	private List<Nodo> mossePossibiliPedinaSotto(Nodo node, int riga, int colonna) throws IOException, BoardException, ActionException, StopException, PawnException, DiagonalException, ClimbingException, ThroneException, OccupitedException, ClimbingCitadelException, CitadelException
	{
		List<Nodo> listaMossePossibili = new ArrayList<Nodo>();
		int c = 0;
		//stato.setTurn(turno);
		while(canMoveDown(node.getStato(), riga+c, colonna))
		{
			c++;
			Action ac = new Action(node.getStato().getBox(riga, colonna), node.getStato().getBox(riga+c, colonna), node.getTurn());
			StateTablut nuovoStato = (StateTablut) this.checkMove(node.getStato(), ac);
			Nodo nodo2 = new Nodo(nuovoStato);
			nodo2.setAzione(ac);
			listaMossePossibili.add(nodo2);
			//System.out.println(ac);
		}		
		return listaMossePossibili;
	}
	
	//ritorna i nodi nei quali è possibile trovarsi col movimento verso destra della pedina indicata
	private List<Nodo> mossePossibiliPedinaDestra(Nodo node, int riga, int colonna) throws IOException, BoardException, ActionException, StopException, PawnException, DiagonalException, ClimbingException, ThroneException, OccupitedException, ClimbingCitadelException, CitadelException
	{
		List<Nodo> listaMossePossibili = new ArrayList<Nodo>();
		int c = 0;
		while(canMoveRight(node.getStato(), riga, colonna+c))
		{
			c++;
			Action ac = new Action(node.getStato().getBox(riga, colonna), node.getStato().getBox(riga, colonna+c), node.getTurn());
			StateTablut nuovoStato = (StateTablut) this.checkMove(node.getStato(), ac);
			Nodo nodo2 = new Nodo(nuovoStato);
			nodo2.setAzione(ac);
			listaMossePossibili.add(nodo2);
			//System.out.println(ac);
		}		
		return listaMossePossibili;
	}
	
	//ritorna i nodi nei quali è possibile trovarsi col movimento verso destra della pedina indicata
	private List<Nodo> mossePossibiliPedinaSinistra(Nodo node, int riga, int colonna) throws IOException, BoardException, ActionException, StopException, PawnException, DiagonalException, ClimbingException, ThroneException, OccupitedException, ClimbingCitadelException, CitadelException
	{
		List<Nodo> listaMossePossibili = new ArrayList<Nodo>();
		int c = 0;
		while(canMoveLeft(node.getStato(), riga, colonna-c))
		{
			c++;
			Action ac = new Action(node.getStato().getBox(riga, colonna), node.getStato().getBox(riga, colonna-c), node.getTurn());
			StateTablut nuovoStato =  (StateTablut) this.checkMove(node.getStato(), ac);
			/*System.out.println("AAAAA");
			System.out.println(nuovoStato);*/
			Nodo nodo2 = new Nodo(nuovoStato);
			nodo2.setAzione(ac);
			listaMossePossibili.add(nodo2);
			//System.out.println(ac);
		}			
		return listaMossePossibili;
	}

	//dice se una data pedina può muoversi verso l'alto
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
	
	//dice se una data pedina può muoversi verso il basso
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
	
	//dice se una data pedina può muoversi verso sinistra
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
	
	//dice se una data pedina può muoversi verso destra
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
	
	//applico la mossa
	private State checkMove(State state, Action a) throws BoardException, ActionException, StopException, PawnException, DiagonalException, ClimbingException, ThroneException, OccupitedException, ClimbingCitadelException, CitadelException {
		
		// se sono arrivato qui, muovo la pedina
		StateTablut newState = (StateTablut) this.movePawn(state, a);

		// a questo punto controllo lo stato per eventuali catture
		if (newState.getTurn().equalsTurn("W")) {
			newState = this.checkCaptureBlack(newState, a);
		} else if (newState.getTurn().equalsTurn("B")) {
			newState = this.checkCaptureWhite(newState, a);
		}

		// if something has been captured, clear cache for draws
		if (this.movesWithutCapturing == 0) {
			this.drawConditions.clear();
		}

		// controllo pareggio
		int trovati = 0;
		for (State s : drawConditions) {

			//System.out.println(s.toString());

			if (s.equals(state)) {
					trovati++;
				if (trovati > repeated_moves_allowed) {
					//state.setTurn(State.Turn.DRAW);
					break;
				}
			}
		}
		if (cache_size >= 0 && this.drawConditions.size() > cache_size) {
			this.drawConditions.remove(0);
		}
		this.drawConditions.add(state.clone());


		return newState;
	}
	
	private State movePawn(State state, Action a) {
		StateTablut newState = new StateTablut();
		
		State.Pawn pawn = state.getPawn(a.getRowFrom(), a.getColumnFrom());
		State.Pawn[][] newBoard = new State.Pawn[9][9];
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				newBoard[i][j] = state.getPawn(i, j);
			}
		}
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
		newState.setBoard(newBoard);
		
		//cambio il turno
		if(state.getTurn().equalsTurn(State.Turn.WHITE.toString()))
		{
			newState.setTurn(State.Turn.BLACK);
		}
		else
		{
			newState.setTurn(State.Turn.WHITE);
		}
		
		
		return newState;
	}

	private StateTablut checkCaptureWhite(StateTablut state, Action a) {
		// controllo se mangio a destra
		if (a.getColumnTo() < state.getBoard().length - 2
				&& state.getPawn(a.getRowTo(), a.getColumnTo() + 1).equalsPawn("B")
				&& (state.getPawn(a.getRowTo(), a.getColumnTo() + 2).equalsPawn("W")
						|| state.getPawn(a.getRowTo(), a.getColumnTo() + 2).equalsPawn("T")
						|| state.getPawn(a.getRowTo(), a.getColumnTo() + 2).equalsPawn("K")
						|| (this.citadels.contains(state.getBox(a.getRowTo(), a.getColumnTo() + 2)) &&!(a.getColumnTo()+2==8&&a.getRowTo()==4)&&!(a.getColumnTo()+2==4&&a.getRowTo()==0)&&!(a.getColumnTo()+2==4&&a.getRowTo()==8)&&!(a.getColumnTo()+2==0&&a.getRowTo()==4)))) {
			state.removePawn(a.getRowTo(), a.getColumnTo() + 1);
			this.movesWithutCapturing = -1;
		}
		// controllo se mangio a sinistra
		if (a.getColumnTo() > 1 && state.getPawn(a.getRowTo(), a.getColumnTo() - 1).equalsPawn("B")
				&& (state.getPawn(a.getRowTo(), a.getColumnTo() - 2).equalsPawn("W")
						|| state.getPawn(a.getRowTo(), a.getColumnTo() - 2).equalsPawn("T")
						|| state.getPawn(a.getRowTo(), a.getColumnTo() - 2).equalsPawn("K")
						|| (this.citadels.contains(state.getBox(a.getRowTo(), a.getColumnTo() - 2)) &&!(a.getColumnTo()-2==8&&a.getRowTo()==4)&&!(a.getColumnTo()-2==4&&a.getRowTo()==0)&&!(a.getColumnTo()-2==4&&a.getRowTo()==8)&&!(a.getColumnTo()-2==0&&a.getRowTo()==4)))) {
			state.removePawn(a.getRowTo(), a.getColumnTo() - 1);
			this.movesWithutCapturing = -1;
		}
		// controllo se mangio sopra
		if (a.getRowTo() > 1 && state.getPawn(a.getRowTo() - 1, a.getColumnTo()).equalsPawn("B")
				&& (state.getPawn(a.getRowTo() - 2, a.getColumnTo()).equalsPawn("W")
						|| state.getPawn(a.getRowTo() - 2, a.getColumnTo()).equalsPawn("T")
						|| state.getPawn(a.getRowTo() - 2, a.getColumnTo()).equalsPawn("K")
						|| (this.citadels.contains(state.getBox(a.getRowTo() - 2, a.getColumnTo()))&&!(a.getColumnTo()==8&&a.getRowTo()-2==4)&&!(a.getColumnTo()==4&&a.getRowTo()-2==0)&&!(a.getColumnTo()==4&&a.getRowTo()-2==8)&&!(a.getColumnTo()==0&&a.getRowTo()-2==4)) )) {
			state.removePawn(a.getRowTo() - 1, a.getColumnTo());
			this.movesWithutCapturing = -1;
		}
		// controllo se mangio sotto
		if (a.getRowTo() < state.getBoard().length - 2
				&& state.getPawn(a.getRowTo() + 1, a.getColumnTo()).equalsPawn("B")
				&& (state.getPawn(a.getRowTo() + 2, a.getColumnTo()).equalsPawn("W")
						|| state.getPawn(a.getRowTo() + 2, a.getColumnTo()).equalsPawn("T")
						|| state.getPawn(a.getRowTo() + 2, a.getColumnTo()).equalsPawn("K")
						|| (this.citadels.contains(state.getBox(a.getRowTo() + 2, a.getColumnTo()))&&!(a.getColumnTo()==8&&a.getRowTo()+2==4)&&!(a.getColumnTo()==4&&a.getRowTo()+2==0)&&!(a.getColumnTo()==4&&a.getRowTo()+2==8)&&!(a.getColumnTo()==0&&a.getRowTo()+2==4)))) {
			state.removePawn(a.getRowTo() + 1, a.getColumnTo());
			this.movesWithutCapturing = -1;
		}
		// controllo se ho vinto
		if (a.getRowTo() == 0 || a.getRowTo() == state.getBoard().length - 1 || a.getColumnTo() == 0
				|| a.getColumnTo() == state.getBoard().length - 1) {
			if (state.getPawn(a.getRowTo(), a.getColumnTo()).equalsPawn("K")) {
				state.setTurn(State.Turn.WHITEWIN);
			}
		}

		this.movesWithutCapturing++;
		return state;
	}

	private StateTablut checkCaptureBlack(StateTablut state, Action a) {
		
		this.checkCaptureBlackPawnRight(state, a);
		this.checkCaptureBlackPawnLeft(state, a);
		this.checkCaptureBlackPawnUp(state, a);
		this.checkCaptureBlackPawnDown(state, a);
		this.checkCaptureBlackKingRight(state, a);
		this.checkCaptureBlackKingLeft(state, a);
		this.checkCaptureBlackKingDown(state, a);
		this.checkCaptureBlackKingUp(state, a);

		this.movesWithutCapturing++;
		return state;
	}

	private State checkCaptureBlackKingLeft(State state, Action a){
		//ho il re sulla sinistra
		if (a.getColumnTo()>1&&state.getPawn(a.getRowTo(), a.getColumnTo()-1).equalsPawn("K"))
		{
			//re sul trono
			if(state.getBox(a.getRowTo(), a.getColumnTo()-1).equals("e5"))
			{
				if(state.getPawn(3, 4).equalsPawn("B")
						&& state.getPawn(4, 3).equalsPawn("B")
						&& state.getPawn(5, 4).equalsPawn("B"))
				{
					state.setTurn(State.Turn.BLACKWIN);
				}
			}
			//re adiacente al trono
			if(state.getBox(a.getRowTo(), a.getColumnTo()-1).equals("e4"))
			{		
				if(state.getPawn(2, 4).equalsPawn("B")
						&& state.getPawn(3, 3).equalsPawn("B"))
				{
					state.setTurn(State.Turn.BLACKWIN);
				}
			}
			if(state.getBox(a.getRowTo(), a.getColumnTo()-1).equals("f5"))
			{
				if(state.getPawn(5, 5).equalsPawn("B")
						&& state.getPawn(3, 5).equalsPawn("B"))
				{
					state.setTurn(State.Turn.BLACKWIN);
				}
			}
			if(state.getBox(a.getRowTo(), a.getColumnTo()-1).equals("e6"))
			{
				if(state.getPawn(6, 4).equalsPawn("B")
						&& state.getPawn(5, 3).equalsPawn("B"))
				{
					state.setTurn(State.Turn.BLACKWIN);
				}
			}
			//sono fuori dalle zone del trono
			if(!state.getBox(a.getRowTo(), a.getColumnTo()-1).equals("e5")
					&& !state.getBox(a.getRowTo(), a.getColumnTo()-1).equals("e6")
					&& !state.getBox(a.getRowTo(), a.getColumnTo()-1).equals("e4")
					&& !state.getBox(a.getRowTo(), a.getColumnTo()-1).equals("f5"))
			{
				if(state.getPawn(a.getRowTo(), a.getColumnTo() - 2).equalsPawn("B")
						|| this.citadels.contains(state.getBox(a.getRowTo(), a.getColumnTo()-2)))
				{
					state.setTurn(State.Turn.BLACKWIN);
				}					
			}
		}		
		return state;
	}
	
	private State checkCaptureBlackKingRight(State state, Action a){
		//ho il re sulla destra
		if (a.getColumnTo()<state.getBoard().length-2&&(state.getPawn(a.getRowTo(),a.getColumnTo()+1).equalsPawn("K")))				
		{
			//re sul trono
			if(state.getBox(a.getRowTo(), a.getColumnTo()+1).equals("e5"))
			{
				if(state.getPawn(3, 4).equalsPawn("B")
						&& state.getPawn(4, 5).equalsPawn("B")
						&& state.getPawn(5, 4).equalsPawn("B"))
				{
					state.setTurn(State.Turn.BLACKWIN);
				}
			}
			//re adiacente al trono
			if(state.getBox(a.getRowTo(), a.getColumnTo()+1).equals("e4"))
			{
				if(state.getPawn(2, 4).equalsPawn("B")
						&& state.getPawn(3, 5).equalsPawn("B"))
				{
					state.setTurn(State.Turn.BLACKWIN);
				}
			}
			if(state.getBox(a.getRowTo(), a.getColumnTo()+1).equals("e6"))
			{
				if(state.getPawn(5, 5).equalsPawn("B")
						&& state.getPawn(6, 4).equalsPawn("B"))
				{
					state.setTurn(State.Turn.BLACKWIN);
				}
			}
			if(state.getBox(a.getRowTo(), a.getColumnTo()+1).equals("d5"))
			{
				if(state.getPawn(3, 3).equalsPawn("B")
						&& state.getPawn(5, 3).equalsPawn("B"))
				{
					state.setTurn(State.Turn.BLACKWIN);
				}
			}
			//sono fuori dalle zone del trono
			if(!state.getBox(a.getRowTo(), a.getColumnTo()+1).equals("d5")
					&& !state.getBox(a.getRowTo(), a.getColumnTo()+1).equals("e6")
					&& !state.getBox(a.getRowTo(), a.getColumnTo()+1).equals("e4")
					&& !state.getBox(a.getRowTo(), a.getColumnTo()+1).equals("e5"))
			{
				if(state.getPawn(a.getRowTo(), a.getColumnTo() + 2).equalsPawn("B")
						|| this.citadels.contains(state.getBox(a.getRowTo(), a.getColumnTo()+2)))
				{
					state.setTurn(State.Turn.BLACKWIN);
				}					
			}
		}
		return state;
	}
	
	private State checkCaptureBlackKingDown(State state, Action a){
		//ho il re sotto
		if (a.getRowTo()<state.getBoard().length-2&&state.getPawn(a.getRowTo()+1,a.getColumnTo()).equalsPawn("K"))
		{
			//re sul trono
			if(state.getBox(a.getRowTo()+1, a.getColumnTo()).equals("e5"))
			{
				if(state.getPawn(5, 4).equalsPawn("B")
						&& state.getPawn(4, 5).equalsPawn("B")
						&& state.getPawn(4, 3).equalsPawn("B"))
				{
					state.setTurn(State.Turn.BLACKWIN);
				}
			}
			//re adiacente al trono
			if(state.getBox(a.getRowTo()+1, a.getColumnTo()).equals("e4"))
			{
				if(state.getPawn(3, 3).equalsPawn("B")
						&& state.getPawn(3, 5).equalsPawn("B"))
				{
					state.setTurn(State.Turn.BLACKWIN);
				}
			}
			if(state.getBox(a.getRowTo()+1, a.getColumnTo()).equals("d5"))
			{
				if(state.getPawn(4, 2).equalsPawn("B")
						&& state.getPawn(5, 3).equalsPawn("B"))
				{
					state.setTurn(State.Turn.BLACKWIN);
				}
			}
			if(state.getBox(a.getRowTo()+1, a.getColumnTo()).equals("f5"))
			{
				if(state.getPawn(4, 6).equalsPawn("B")
						&& state.getPawn(5, 5).equalsPawn("B"))
				{
					state.setTurn(State.Turn.BLACKWIN);
				}
			}
			//sono fuori dalle zone del trono
			if(!state.getBox(a.getRowTo()+1, a.getColumnTo()).equals("d5")
					&& !state.getBox(a.getRowTo()+1, a.getColumnTo()).equals("e4")
					&& !state.getBox(a.getRowTo()+1, a.getColumnTo()).equals("f5")
					&& !state.getBox(a.getRowTo()+1, a.getColumnTo()).equals("e5"))
			{
				if(state.getPawn(a.getRowTo()+2, a.getColumnTo()).equalsPawn("B")
						|| this.citadels.contains(state.getBox(a.getRowTo()+2, a.getColumnTo())))
				{
					state.setTurn(State.Turn.BLACKWIN);
				}					
			}			
		}		
		return state;
	}
	
	private State checkCaptureBlackKingUp(State state, Action a){
		//ho il re sopra
		if (a.getRowTo()>1&&state.getPawn(a.getRowTo()-1, a.getColumnTo()).equalsPawn("K"))
		{
			//re sul trono
			if(state.getBox(a.getRowTo()-1, a.getColumnTo()).equals("e5"))
			{
				if(state.getPawn(3, 4).equalsPawn("B")
						&& state.getPawn(4, 5).equalsPawn("B")
						&& state.getPawn(4, 3).equalsPawn("B"))
				{
					state.setTurn(State.Turn.BLACKWIN);
				}
			}
			//re adiacente al trono
			if(state.getBox(a.getRowTo()-1, a.getColumnTo()).equals("e6"))
			{
				if(state.getPawn(5, 3).equalsPawn("B")
						&& state.getPawn(5, 5).equalsPawn("B"))
				{
					state.setTurn(State.Turn.BLACKWIN);
				}
			}
			if(state.getBox(a.getRowTo()-1, a.getColumnTo()).equals("d5"))
			{
				if(state.getPawn(4, 2).equalsPawn("B")
						&& state.getPawn(3, 3).equalsPawn("B"))
				{
					state.setTurn(State.Turn.BLACKWIN);
				}
			}
			if(state.getBox(a.getRowTo()-1, a.getColumnTo()).equals("f5"))
			{
				if(state.getPawn(4, 6).equalsPawn("B")
						&& state.getPawn(3, 5).equalsPawn("B"))
				{
					state.setTurn(State.Turn.BLACKWIN);
				}
			}
			//sono fuori dalle zone del trono
			if(!state.getBox(a.getRowTo()-1, a.getColumnTo()).equals("d5")
					&& !state.getBox(a.getRowTo()-1, a.getColumnTo()).equals("e4")
					&& !state.getBox(a.getRowTo()-1, a.getColumnTo()).equals("f5")
					&& !state.getBox(a.getRowTo()-1, a.getColumnTo()).equals("e5"))
			{
				if(state.getPawn(a.getRowTo()-2, a.getColumnTo()).equalsPawn("B")
						|| this.citadels.contains(state.getBox(a.getRowTo()-2, a.getColumnTo())))
				{
					state.setTurn(State.Turn.BLACKWIN);
				}					
			}	
		}
		return state;
	}
	
	private void checkCaptureBlackPawnRight(State state, Action a)	{
		//mangio a destra
		if (a.getColumnTo() < state.getBoard().length - 2 && state.getPawn(a.getRowTo(), a.getColumnTo() + 1).equalsPawn("W"))
		{
			if(state.getPawn(a.getRowTo(), a.getColumnTo() + 2).equalsPawn("B"))
			{
				state.removePawn(a.getRowTo(), a.getColumnTo() + 1);
				this.movesWithutCapturing = -1;
			}
			if(state.getPawn(a.getRowTo(), a.getColumnTo() + 2).equalsPawn("T"))
			{
				state.removePawn(a.getRowTo(), a.getColumnTo() + 1);
				this.movesWithutCapturing = -1;
			}
			if(this.citadels.contains(state.getBox(a.getRowTo(), a.getColumnTo() + 2)))
			{
				state.removePawn(a.getRowTo(), a.getColumnTo() + 1);
				this.movesWithutCapturing = -1;
			}
			if(state.getBox(a.getRowTo(), a.getColumnTo()+2).equals("e5"))
			{
				state.removePawn(a.getRowTo(), a.getColumnTo() + 1);
				this.movesWithutCapturing = -1;
			}
			
		}
	}
	
	private void checkCaptureBlackPawnLeft(State state, Action a){
		//mangio a sinistra
		if (a.getColumnTo() > 1
				&& state.getPawn(a.getRowTo(), a.getColumnTo() - 1).equalsPawn("W")
				&& (state.getPawn(a.getRowTo(), a.getColumnTo() - 2).equalsPawn("B")
						|| state.getPawn(a.getRowTo(), a.getColumnTo() - 2).equalsPawn("T")
						|| this.citadels.contains(state.getBox(a.getRowTo(), a.getColumnTo() - 2))
						|| (state.getBox(a.getRowTo(), a.getColumnTo()-2).equals("e5"))))
		{
			state.removePawn(a.getRowTo(), a.getColumnTo() - 1);
			this.movesWithutCapturing = -1;
		}
	}
	
	private void checkCaptureBlackPawnUp(State state, Action a){
		// controllo se mangio sopra
		if (a.getRowTo() > 1
				&& state.getPawn(a.getRowTo() - 1, a.getColumnTo()).equalsPawn("W")
				&& (state.getPawn(a.getRowTo() - 2, a.getColumnTo()).equalsPawn("B")
						|| state.getPawn(a.getRowTo() - 2, a.getColumnTo()).equalsPawn("T")
						|| this.citadels.contains(state.getBox(a.getRowTo() - 2, a.getColumnTo()))
						|| (state.getBox(a.getRowTo()-2, a.getColumnTo()).equals("e5"))))
		{
			state.removePawn(a.getRowTo()-1, a.getColumnTo());
			this.movesWithutCapturing = -1;
		}
	}
	
	private void checkCaptureBlackPawnDown(State state, Action a){
		// controllo se mangio sotto
		if (a.getRowTo() < state.getBoard().length - 2
				&& state.getPawn(a.getRowTo() + 1, a.getColumnTo()).equalsPawn("W")
				&& (state.getPawn(a.getRowTo() + 2, a.getColumnTo()).equalsPawn("B")
						|| state.getPawn(a.getRowTo() + 2, a.getColumnTo()).equalsPawn("T")
						|| this.citadels.contains(state.getBox(a.getRowTo() + 2, a.getColumnTo()))
						|| (state.getBox(a.getRowTo()+2, a.getColumnTo()).equals("e5"))))
		{
			state.removePawn(a.getRowTo()+1, a.getColumnTo());
			this.movesWithutCapturing = -1;
		}
	}
	
	
}
