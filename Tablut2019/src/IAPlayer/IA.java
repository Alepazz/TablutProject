package IAPlayer;

import it.unibo.ai.didattica.competition.tablut.domain.Action;
import it.unibo.ai.didattica.competition.tablut.domain.StateTablut;

public interface IA {
	
	//IL METODO CHE A SECONDA DEL GIOCATORE CHE IMPLEMENTA QUESTA INTERFACCIA DAR� LA MOSSA MIGLIORE
	public Action getBetterMove(StateTablut s);
	
	public int getHeuristicValueOfState(StateTablut s);

}
