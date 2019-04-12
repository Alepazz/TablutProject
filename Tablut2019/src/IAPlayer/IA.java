package IAPlayer;

import it.unibo.ai.didattica.competition.tablut.domain.Action;
import it.unibo.ai.didattica.competition.tablut.domain.StateTablut;

public interface IA {
	
	//IL METODO CHE A SECONDA DEL GIOCATORE CHE IMPLEMENTA QUESTA INTERFACCIA DARà LA MOSSA MIGLIORE
	public Action getBetterMove(StateTablut s);

}
