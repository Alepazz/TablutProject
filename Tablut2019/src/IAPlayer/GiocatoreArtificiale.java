package IAPlayer;

import java.io.IOException;
import java.net.UnknownHostException;

import it.unibo.ai.didattica.competition.tablut.client.TablutClient;
import it.unibo.ai.didattica.competition.tablut.domain.Action;
import it.unibo.ai.didattica.competition.tablut.domain.StateTablut;

public class GiocatoreArtificiale extends TablutClient {

	public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException {

		if (args.length == 0) {
			System.out.println("You must specify which player you are (WHITE or BLACK)!");
			System.exit(-1);
		}
		System.out.println("Selected this: " + args[0]);

		//NOME DA CAMBIARE
		TablutClient client = new GiocatoreArtificiale(args[0], "JUVEMERDA");
		client.run();
	}
	
	private IA intelligenza;
	
	//INIZIALIZZO IL GIOCATORE (BIANCO O NERO) GIà NEL COSTRUTTORE, DICHIARO ANCHE IL NOME AL SERVER QUI
	public GiocatoreArtificiale(String player, String name)
			throws UnknownHostException, IOException {
		super(player, name);
		if(this.getPlayer().equalsTurn("B"))
		{
			this.intelligenza = new IntelligenzaBianca();
		}
		else
		{
			this.intelligenza = new IntelligenzaNera();
		}
		
		try 
		{
			this.declareName();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	//METODO CHE INIZIA A GIOCARE, CONNESSIONE GIà EFFETTUATA E NOME GIà DATO
	@Override
	public void run() {
		
		Action betterAction;
		
		//LEGGO PER LA PRIMA VOLTA LO STATO
		try 
		{
			this.read();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		//LOOP FINO A FINE PARTITA
		while(!this.getCurrentState().getTurn().equalsTurn("D") || !this.getCurrentState().getTurn().equalsTurn("BW") || !this.getCurrentState().getTurn().equalsTurn("WW"))
		{
			//SE è IL MIO TURNO CALCOLO
			if(this.getCurrentState().getTurn().equals(this.getPlayer()))
			{
				//CALCOLO LA MOSSA MIGLIORE
				betterAction = this.intelligenza.getBetterMove((StateTablut) this.getCurrentState());
				
				//SCRIVO AL SERVER LA MOSSA
				try 
				{
					this.write(betterAction);
				}
				catch (Exception e1) 
				{
					e1.printStackTrace();
				} 
				
				//LEGGO IL RISULTATO DEL MIO STATO
				try 
				{
					this.read();
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
			
			//LEGGO IL RISULTATO DELLA MOSSA DELL'AVVERSARIO
			try 
			{
				this.read();
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		
		//LEGGO IL RISULTATO DELLA PARTITA
		if(this.getCurrentState().getTurn().equalsTurn("WW"))
		{
			if(this.getPlayer().equalsTurn("W"))
			{
				System.out.println("DAJE ABBIAMO VINTOOOOO");
			}
			else
			{
				System.out.println("Mannaggia abbiamo perso....");
			}
		}
		if(this.getCurrentState().getTurn().equalsTurn("BW"))
		{
			if(this.getPlayer().equalsTurn("B"))
			{
				System.out.println("DAJE ABBIAMO VINTOOOOO");
			}
			else
			{
				System.out.println("Mannaggia abbiamo perso....");
			}
		}
		if(this.getCurrentState().getTurn().equalsTurn("D"))
		{
			System.out.println("Beh, poteva andare meglio ma anche peggio");
		}
		
	}

}
