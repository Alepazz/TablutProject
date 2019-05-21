package IAPlayer;

import java.io.IOException;
import java.net.UnknownHostException;

import it.unibo.ai.didattica.competition.tablut.client.TablutClient;
import it.unibo.ai.didattica.competition.tablut.domain.Action;
import it.unibo.ai.didattica.competition.tablut.domain.StateTablut;

//NON TOCCHIAMO PI� QUESTA CLASSE, LE STAMPE METTIAMOLE NEI METODI
public class PartecipareEImportante extends TablutClient {
	
	private static int secondi = 0;
	
	public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException {

		if (args.length == 0) {
			System.out.println("You must specify which player you are (WHITE or BLACK)!");
			System.exit(-1);
		}
		if (args.length == 1) {
			System.out.println("You must specify the time for the move!");
			System.exit(-1);
		}
		System.out.println("Selected this: " + args[0]);
		System.out.println("secondi inseriti: " + args[1]);
		secondi = Integer.parseInt(args[1]);
		secondi = secondi - 5;
		
		TablutClient client = new PartecipareEImportante(args[0], "Partecipare_e_importante");

		client.run();
	}
	
	private IA intelligenza;
	
	//INIZIALIZZO IL GIOCATORE (BIANCO O NERO) GIA' NEL COSTRUTTORE
	public PartecipareEImportante(String player, String name)
			throws UnknownHostException, IOException {
		super(player, name);
		if(this.getPlayer().equalsTurn("W"))
		{
			this.intelligenza = new IntelligenzaBianca(secondi);
		}
		else
		{
			this.intelligenza = new IntelligenzaNera(secondi);
		}
		
		try 
		{
			this.declareName();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	//METODO CHE INIZIA A GIOCARE, CONNESSIONE GIA' EFFETTUATA E NOME GIA' DATO
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
			System.exit(1);
		}
		
		//LOOP FINO A FINE PARTITA
		while(!this.getCurrentState().getTurn().equalsTurn("D") && !this.getCurrentState().getTurn().equalsTurn("BW") && !this.getCurrentState().getTurn().equalsTurn("WW"))
		{
			//SE E' IL MIO TURNO CALCOLO
			if(this.getCurrentState().getTurn().equalsTurn(this.getPlayer().toString()))
			{
				//CALCOLO LA MOSSA MIGLIORE
				betterAction = this.intelligenza.getBetterMove((StateTablut) this.getCurrentState());
				
				//SCRIVO AL SERVER LA MOSSA
				try 
				{
					this.write(betterAction);
					System.gc();
				}
				catch (Exception e1) 
				{
					e1.printStackTrace();
					System.exit(1);
				} 
				
				//LEGGO IL RISULTATO DEL MIO STATO
				try 
				{
					this.read();
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
					System.exit(1);
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
				System.exit(1);
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
