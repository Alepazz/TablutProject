package IAPlayer;

import it.unibo.ai.didattica.competition.tablut.domain.*;
import it.unibo.ai.didattica.competition.tablut.exceptions.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class TestingSimulator {

	public static void main(String[] args) throws IOException, BoardException, ActionException, StopException, PawnException, DiagonalException, ClimbingException, ThroneException, OccupitedException, ClimbingCitadelException, CitadelException {
			
		
        StateTablut state = new StateTablut();
		state.setTurn(State.Turn.BLACK);
		Simulator sim = new Simulator();
		int cont = 0;
		int c = 1;
		long t1 = System.currentTimeMillis();
		state.getBoard()[0][3] = State.Pawn.EMPTY;
		state.getBoard()[1][3] = State.Pawn.BLACK;
		state.getBoard()[2][4] = State.Pawn.EMPTY;
		state.getBoard()[3][3] = State.Pawn.WHITE;
		System.out.println(state.toString());
		//sim.mossePossibiliComplete(new Nodo(state));
		/*for(Nodo n : sim.mossePossibiliComplete(new Nodo(state)))
		{
			System.out.println(c);	
			c++;
			//System.out.println(n.getStato().toString());
			System.out.println(n.getAzione());			
		}*/
		
		/*System.out.println("E' simmetrico orrizontalmente? "+ sim.statoSimmetricoOrizontalmente(state));
		System.out.println("E' simmetrico verticalmente? "+ sim.statoSimmetricoVerticalmente(state));
		*/
		
		//IntelligenzaBianca ia = new IntelligenzaBianca();
		for(Nodo n : sim.mossePossibiliComplete(new Nodo(state)))
		{
			System.out.println(n.getAzione().toString());
		}
		//Action a = ia.getBetterMove(state);
		/*System.out.println(a.toString());
		long t2 = System.currentTimeMillis();
        //System.out.println(t2);
        System.out.println("Tempo trascorso: "+(t2-t1)+" millisecondi");*/
		System.out.println(state.toString());
    
        
        
		
	}
	
}
