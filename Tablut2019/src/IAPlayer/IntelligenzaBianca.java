package IAPlayer;

import it.unibo.ai.didattica.competition.tablut.domain.Action;
import it.unibo.ai.didattica.competition.tablut.domain.State;
import it.unibo.ai.didattica.competition.tablut.domain.StateTablut;

import java.io.IOException;
import java.util.List;

public class IntelligenzaBianca implements IA {

	private float getHeuristicValue(StateTablut s) {
		// TODO Auto-generated method stub
		return 0;
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
		
		//QUESTA � SOLO UNA PROVA PER VEDERE SE EFFETTIVAMENTE IL NOSTRO GIOCATORE FUNZIONA
		//RISULTATO POSITIVO
		Action a = null;
		try {
		/*	if(this.isStart(s)) {
				System.out.println("FATTOOOOOOOOOOO");
			} else {
				System.out.println("La scacchiera non è riempita");

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

/*
 * ALLEGO DOMANDE
 * 
 * Che metodo va chiamato per sapere quando siamo nello stato iniziale (nessuna mossa ancora effettuata)?
 * Come ottengo qual'è l'ultima mossa effettuata dal giocatore avversario?
 */
