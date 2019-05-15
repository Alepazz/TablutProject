package IAPlayer;

import it.unibo.ai.didattica.competition.tablut.domain.*;
import it.unibo.ai.didattica.competition.tablut.domain.State.Pawn;
import it.unibo.ai.didattica.competition.tablut.exceptions.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class TestingSimulator {

	public static void main(String[] args) throws IOException, BoardException, ActionException, StopException, PawnException, DiagonalException, ClimbingException, ThroneException, OccupitedException, ClimbingCitadelException, CitadelException {
			
		
        StateTablut state = new StateTablut();
		state.setTurn(State.Turn.WHITE);
		Simulator sim = new Simulator();
		IntelligenzaBianca ia = new IntelligenzaBianca(30);
		int cont = 0;
		int c = 1;
		//long t1 = System.currentTimeMillis();
		for(int x=0; x<9; x++)
		{
			for(int y=0; y<9; y++)
			{
				state.removePawn(x, y);
			}
		}
		state.getBoard()[2][4] = Pawn.KING;
		state.getBoard()[2][8] = Pawn.BLACK;
		state.getBoard()[3][7] = Pawn.BLACK;  
		state.getBoard()[4][4] = Pawn.THRONE;
  		System.out.println(state.toString());
  		System.out.println();
  		System.out.println();
  		System.out.println();
  		System.out.println(ia.getBetterMove(state).toString());
  		
	}
	
}
