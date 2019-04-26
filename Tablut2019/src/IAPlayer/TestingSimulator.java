package IAPlayer;

import it.unibo.ai.didattica.competition.tablut.domain.*;
import it.unibo.ai.didattica.competition.tablut.exceptions.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class TestingSimulator {

	public static void main(String[] args) throws IOException, BoardException, ActionException, StopException, PawnException, DiagonalException, ClimbingException, ThroneException, OccupitedException, ClimbingCitadelException, CitadelException {
			
		
        StateTablut state = new StateTablut();
		state.setTurn(State.Turn.WHITE);
		Simulator sim = new Simulator();
		int cont = 0;
		int c = 1;
		long t1 = System.currentTimeMillis();
		/*state.getBoard()[4][3] = State.Pawn.BLACK;
		state.getBoard()[4][5] = State.Pawn.BLACK;
		/*state.getBoard()[5][3] = State.Pawn.BLACK;
		state.getBoard()[5][5] = State.Pawn.BLACK;*/
		
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
		
		IntelligenzaBianca ia = new IntelligenzaBianca();
		ia.getBetterMove(state);
		
		long t2 = System.currentTimeMillis();
        //System.out.println(t2);
        System.out.println("Tempo trascorso: "+(t2-t1)+" millisecondi");
        System.out.println("");
    
        
        
		
	}
	
}
