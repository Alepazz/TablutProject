package IAPlayer;

import java.io.IOException;
import java.net.UnknownHostException;

import it.unibo.ai.didattica.competition.tablut.client.TablutClient;
import it.unibo.ai.didattica.competition.tablut.client.TablutHumanClient;
import it.unibo.ai.didattica.competition.tablut.domain.State;

public class GiocatoreArtificiale extends TablutClient {

	private IA intelligenza;
	
	public GiocatoreArtificiale(String player, String name)
			throws UnknownHostException, IOException {
		super(player, name);
		// TODO Auto-generated constructor stub
	}
	
	public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException {

		if (args.length == 0) {
			System.out.println("You must specify which player you are (WHITE or BLACK)!");
			System.exit(-1);
		}
		System.out.println("Selected this: " + args[0]);

		TablutClient client = new GiocatoreArtificiale(args[0], "GalassiFaCagare");
		client.run();
	}

	@Override
	public void run() {
		if(this.getPlayer().equalsTurn("B"))
		{
			this.intelligenza = new IntelligenzaBianca();
		}
		else
		{
			this.intelligenza = new IntelligenzaNera();
		}
		
		
	}

}
