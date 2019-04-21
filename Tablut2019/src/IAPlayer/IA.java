package IAPlayer;

import it.unibo.ai.didattica.competition.tablut.domain.Action;
import it.unibo.ai.didattica.competition.tablut.domain.StateTablut;

//INTERFACCIA MOLTO SEMPLICE GIUSTO PER CHIAMARE IL METODO CHE CI SERVE, NON AGGIUNGIAMO COSE INUTILI CHE POI CI CONFONDIAMO
public interface IA {
		
	//IL METODO CHE A SECONDA DEL GIOCATORE CHE IMPLEMENTA QUESTA INTERFACCIA DARE LA MOSSA MIGLIORE
	public Action getBetterMove(StateTablut s);
	
}
