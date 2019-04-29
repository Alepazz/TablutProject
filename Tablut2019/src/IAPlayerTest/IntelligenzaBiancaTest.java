package IAPlayerTest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Assert;

import it.unibo.ai.didattica.competition.tablut.domain.State;
import it.unibo.ai.didattica.competition.tablut.domain.State.Pawn;
import it.unibo.ai.didattica.competition.tablut.domain.State.Turn;
import it.unibo.ai.didattica.competition.tablut.domain.StateTablut;
import org.junit.jupiter.api.Test;

import IAPlayer.IntelligenzaBianca;

class IntelligenzaBiancaTest {

	private IntelligenzaBianca b = new IntelligenzaBianca();
	private StateTablut s = new StateTablut();
	private Pawn[][] board =   {{Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY},
			{Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY},
			{Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY},
			{Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY},
			{Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.THRONE,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY},
			{Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY},
			{Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY},
			{Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY},
			{Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY}	
};
	
	@Test
	void isColumnFreeTest_1() {	
		
		//Varianti del particolare caso in osservazione
		
		
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * 
		 * */
		
		
		//Creazione dello stato con la precedente disposizione delle pedine
		s.setBoard(board);
		s.setTurn(Turn.WHITE);
		
		//Esecuzione Test
		Assert.assertFalse(b.isColumnFree(0, s));
		Assert.assertFalse(b.isColumnFree(1, s));
		Assert.assertTrue(b.isColumnFree(2, s));
		Assert.assertFalse(b.isColumnFree(3, s));
		Assert.assertFalse(b.isColumnFree(4, s));
		Assert.assertFalse(b.isColumnFree(5, s));
		Assert.assertTrue(b.isColumnFree(6, s));
		Assert.assertFalse(b.isColumnFree(7, s));
		Assert.assertFalse(b.isColumnFree(8, s));			
	}
	
	@Test
	void isColumnFreeTest_2() {	
		
		//Varianti del particolare caso in osservazione
		board[4][2] = Pawn.BLACK;
		board[4][6] = Pawn.BLACK;
		
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOBOOOBOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * 
		 * */
		
		
		//Creazione dello stato con la precedente disposizione delle pedine
		s.setBoard(board);
		s.setTurn(Turn.WHITE);
		
		//Esecuzione Test
		Assert.assertFalse(b.isColumnFree(0, s));
		Assert.assertFalse(b.isColumnFree(1, s));
		Assert.assertFalse(b.isColumnFree(2, s));
		Assert.assertFalse(b.isColumnFree(3, s));
		Assert.assertFalse(b.isColumnFree(4, s));
		Assert.assertFalse(b.isColumnFree(5, s));
		Assert.assertFalse(b.isColumnFree(6, s));
		Assert.assertFalse(b.isColumnFree(7, s));
		Assert.assertFalse(b.isColumnFree(8, s));			
	}
	
	@Test
	void isRowFreeTest_1() {
	
		//Varianti del particolare caso in osservazione
		
		
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * 
		 * */
		
		
		//Creazione dello stato con la precedente disposizione delle pedine
		s.setBoard(board);
		s.setTurn(Turn.WHITE);
		
		//Esecuzione Test
		Assert.assertFalse(b.isRowFree(0, s));
		Assert.assertFalse(b.isRowFree(1, s));
		Assert.assertTrue(b.isRowFree(2, s));
		Assert.assertFalse(b.isRowFree(3, s));
		Assert.assertFalse(b.isRowFree(4, s));
		Assert.assertFalse(b.isRowFree(5, s));
		Assert.assertTrue(b.isRowFree(6, s));
		Assert.assertFalse(b.isRowFree(7, s));
		Assert.assertFalse(b.isRowFree(8, s));			
	}
	
	@Test
	void isRowFreeTest_2() {	
		
		//Varianti del particolare caso in osservazione
		board[2][3] = Pawn.BLACK;
		board[6][3] = Pawn.BLACK;
		
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOBOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOBOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * 
		 * */
				
		//Creazione dello stato con la precedente disposizione delle pedine
		s.setBoard(board);
		s.setTurn(Turn.BLACK);
		
		//Esecuzione Test
		Assert.assertFalse(b.isRowFree(0, s));
		Assert.assertFalse(b.isRowFree(1, s));
		Assert.assertFalse(b.isRowFree(2, s));
		Assert.assertFalse(b.isRowFree(3, s));
		Assert.assertFalse(b.isRowFree(4, s));
		Assert.assertFalse(b.isRowFree(5, s));
		Assert.assertFalse(b.isRowFree(6, s));
		Assert.assertFalse(b.isRowFree(7, s));
		Assert.assertFalse(b.isRowFree(8, s));			
	}
	
	@Test
	void isSemirowFreeTest_1() {	
		
		//Varianti del particolare caso in osservazione
		
		
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * 
		 * */
		
		
		//Creazione dello stato con la precedente disposizione delle pedine
		s.setBoard(board);
		s.setTurn(Turn.WHITE);
		
		//Esecuzione Test
		Assert.assertTrue(b.isSemicolumnFree(1, s));
		Assert.assertTrue(b.isSemicolumnFree(7, s));
	}
	
	@Test
	void isSemirowFreeTest_2() {	
		
		//Varianti del particolare caso in osservazione
		board[1][2] = Pawn.BLACK;
		board[7][2] = Pawn.BLACK;
		board[7][6] = Pawn.BLACK;
		
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOBOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOBOOOBOO
		 * OOOOOOOOO
		 * 
		 * */
		
		
		//Creazione dello stato con la precedente disposizione delle pedine
		s.setBoard(board);
		s.setTurn(Turn.WHITE);
		
		//Esecuzione Test
		Assert.assertFalse(b.isSemirowFree(1, s));
		Assert.assertFalse(b.isSemirowFree(7, s));
	}
	
	@Test
	void isSemicolumnFreeTest_1() {	
		
		//Varianti del particolare caso in osservazione
		
		
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * 
		 * */
		
		
		//Creazione dello stato con la precedente disposizione delle pedine
		s.setBoard(board);
		s.setTurn(Turn.WHITE);
		
		//Esecuzione Test
		Assert.assertTrue(b.isSemicolumnFree(1, s));
		Assert.assertTrue(b.isSemicolumnFree(7, s));
	}
	
	@Test
	void isSemicolumnFreeTest_2() {	
		
		//Varianti del particolare caso in osservazione
		board[1][2] = Pawn.BLACK;
		board[7][2] = Pawn.BLACK;
		board[7][6] = Pawn.BLACK;
		
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOBOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOBOOOBOO
		 * OOOOOOOOO
		 * 
		 * */
		
		
		//Creazione dello stato con la precedente disposizione delle pedine
		s.setBoard(board);
		s.setTurn(Turn.WHITE);
		
		//Esecuzione Test
		Assert.assertFalse(b.isSemirowFree(1, s));
		Assert.assertFalse(b.isSemirowFree(7, s));
	}
		
	@Test
	void kingCanBeCapturedTest_1() {	
		
		//Varianti del particolare caso in osservazione
		board[4][4] = Pawn.KING;
		
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOKOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * 
		 * */
		
		
		//Creazione dello stato con la precedente disposizione delle pedine
		s.setBoard(board);
		s.setTurn(Turn.BLACK);
		
		//Esecuzione Test
		Assert.assertFalse(b.kingCanBeCaptured(4, 4, s));
	}
	
	@Test
	void kingCanBeCapturedTest_2() {	
		
		//Varianti del particolare caso in osservazione
		board[4][4] = Pawn.KING;
		board[3][4] = Pawn.BLACK;
		board[5][4] = Pawn.BLACK;
		board[4][5] = Pawn.BLACK;
		board[4][0] = Pawn.BLACK;
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOBOOOO
		 * BOOOKBOOO
		 * OOOOBOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * 
		 * */
		
		
		//Creazione dello stato con la precedente disposizione delle pedine
		s.setBoard(board);
		s.setTurn(Turn.BLACK);
		
		//Esecuzione Test
		Assert.assertTrue(b.kingCanBeCaptured(4, 4, s));
	}

}

