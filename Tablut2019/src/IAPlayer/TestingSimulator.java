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
		SimulatorWhite sim = new SimulatorWhite();
		int cont = 0;
		int c = 0;
		long t1 = System.currentTimeMillis();
        //System.out.println(t1);
        List<Livello> albero = new ArrayList<Livello>();
        Livello livello = new Livello();
        livello.add(new Nodo(state));
        albero.add(livello);
  
        for(cont=0; cont<3; cont++)
        {
        	livello = new Livello();
        	for(Nodo nod: albero.get(cont).getNodi())
            {
            	livello.add(nod.generaFigli(sim, false));
            }
        	albero.add(livello);
        }
                
        for(int i=0; i<cont+1; i++)
        {
        	System.out.println("Livello "+i);
        	 for(Nodo nod: albero.get(i).getNodi())
             {
        		System.out.println(c);
        		c++;
             	//System.out.println(nod.getStato().toString());
             	System.out.println(nod.getAzione());
             	if(i==3)
             	{
             		String s=nod.getPadre().getAzione().toString() + " " + nod.getAzione().toString();
             		System.out.println(s);
             	}
             }
        }
        /*System.out.println(albero.size());
        System.out.println(albero.get(0).getNodi().size());
        System.out.println(albero.get(1).getNodi().size());
        System.out.println(albero.get(2).getNodi().size());
        System.out.println(albero.get(3).getNodi().size());*/
        
        
        
		/*for(Nodo nod: sim.mossePossibiliComplete(state, State.Turn.BLACK))
		{
			System.out.println(nod.getAzione().toString());
			System.out.println(nod.getStato().getTurn());
			System.out.println(cont);
			cont++;
			for(Nodo nod1: sim.mossePossibiliComplete(nod.getStato(), State.Turn.BLACK))
			{
				System.out.println(nod1.getAzione().toString());
				System.out.println(nod1.getStato().getTurn());
				System.out.println(cont);
				cont++;
				for(Nodo nod2: sim.mossePossibiliComplete(nod.getStato(), State.Turn.BLACK))
				{
					nod2.setProf(2);
					System.out.println(nod2.getAzione().toString());
					System.out.println(nod2.getStato().getTurn());
					System.out.println(cont);
					cont++;
				}
			}
		}
		for(Nodo nod: sim.mossePossibiliComplete(state, State.Turn.WHITE))
		{
			System.out.println(nod.getAzione().toString());
			System.out.println(nod.getStato().getTurn());
		}*/
		
		long t2 = System.currentTimeMillis();
        //System.out.println(t2);
        System.out.println("Tempo trascorso: "+(t2-t1)+" millisecondi");
        System.out.println("");
    
        
        
		
	}
	
}
