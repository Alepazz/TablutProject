package IAPlayerTest;


import it.unibo.ai.didattica.competition.tablut.domain.State;
import it.unibo.ai.didattica.competition.tablut.domain.State.Pawn;
import it.unibo.ai.didattica.competition.tablut.domain.State.Turn;
import it.unibo.ai.didattica.competition.tablut.domain.StateTablut;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import IAPlayer.CommonHeuristicFunction;
import IAPlayer.IntelligenzaBianca;

class IntelligenzaBiancaTest {

	private CommonHeuristicFunction c = new CommonHeuristicFunction();
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
		Assert.assertFalse(c.isColumnFree(0, s));
		Assert.assertFalse(c.isColumnFree(1, s));
		Assert.assertTrue(c.isColumnFree(2, s));
		Assert.assertFalse(c.isColumnFree(3, s));
		Assert.assertFalse(c.isColumnFree(4, s));
		Assert.assertFalse(c.isColumnFree(5, s));
		Assert.assertTrue(c.isColumnFree(6, s));
		Assert.assertFalse(c.isColumnFree(7, s));
		Assert.assertFalse(c.isColumnFree(8, s));			
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
		Assert.assertFalse(c.isColumnFree(0, s));
		Assert.assertFalse(c.isColumnFree(1, s));
		Assert.assertFalse(c.isColumnFree(2, s));
		Assert.assertFalse(c.isColumnFree(3, s));
		Assert.assertFalse(c.isColumnFree(4, s));
		Assert.assertFalse(c.isColumnFree(5, s));
		Assert.assertFalse(c.isColumnFree(6, s));
		Assert.assertFalse(c.isColumnFree(7, s));
		Assert.assertFalse(c.isColumnFree(8, s));			
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
		Assert.assertFalse(c.isRowFree(0, s));
		Assert.assertFalse(c.isRowFree(1, s));
		Assert.assertTrue(c.isRowFree(2, s));
		Assert.assertFalse(c.isRowFree(3, s));
		Assert.assertFalse(c.isRowFree(4, s));
		Assert.assertFalse(c.isRowFree(5, s));
		Assert.assertTrue(c.isRowFree(6, s));
		Assert.assertFalse(c.isRowFree(7, s));
		Assert.assertFalse(c.isRowFree(8, s));			
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
		Assert.assertFalse(c.isRowFree(0, s));
		Assert.assertFalse(c.isRowFree(1, s));
		Assert.assertFalse(c.isRowFree(2, s));
		Assert.assertFalse(c.isRowFree(3, s));
		Assert.assertFalse(c.isRowFree(4, s));
		Assert.assertFalse(c.isRowFree(5, s));
		Assert.assertFalse(c.isRowFree(6, s));
		Assert.assertFalse(c.isRowFree(7, s));
		Assert.assertFalse(c.isRowFree(8, s));			
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
		Assert.assertTrue(c.isSemicolumnFree(1, s));
		Assert.assertTrue(c.isSemicolumnFree(7, s));
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
		Assert.assertFalse(c.isSemirowFree(1, s));
		Assert.assertFalse(c.isSemirowFree(7, s));
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
		Assert.assertTrue(c.isSemicolumnFree(1, s));
		Assert.assertTrue(c.isSemicolumnFree(7, s));
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
		Assert.assertFalse(c.isSemirowFree(1, s));
		Assert.assertFalse(c.isSemirowFree(7, s));
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
		Assert.assertFalse(c.kingCanBeCaptured(4, 4, s));
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
		Assert.assertTrue(c.kingCanBeCaptured(4, 4, s));
	}
	
	@Test
	void kingCanBeCapturedTest_3() {	
		
		//Varianti del particolare caso in osservazione
		board[4][4] = Pawn.KING;
		board[3][4] = Pawn.BLACK;
		board[5][4] = Pawn.BLACK;
		board[4][3] = Pawn.BLACK;
		board[4][8] = Pawn.BLACK;
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOBOOOO
		 * OOOBKOOOB
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
		Assert.assertTrue(c.kingCanBeCaptured(4, 4, s));
	}
	
	@Test
	void kingCanBeCapturedTest_4() {	
		
		//Varianti del particolare caso in osservazione
		board[4][4] = Pawn.KING;
		board[3][4] = Pawn.BLACK;
		board[8][4] = Pawn.BLACK;
		board[4][5] = Pawn.BLACK;
		board[4][3] = Pawn.BLACK;
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOBOOOO
		 * OOOBKBOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOBOOOO
		 * 
		 * */
		
		
		//Creazione dello stato con la precedente disposizione delle pedine
		s.setBoard(board);
		s.setTurn(Turn.BLACK);
		
		//Esecuzione Test
		Assert.assertTrue(c.kingCanBeCaptured(4, 4, s));
	}
	
	@Test
	void kingCanBeCapturedTest_5() {	
		
		//Varianti del particolare caso in osservazione
		board[4][4] = Pawn.KING;
		board[0][4] = Pawn.BLACK;
		board[5][4] = Pawn.BLACK;
		board[4][5] = Pawn.BLACK;
		board[4][3] = Pawn.BLACK;
		
		/* Rappresentazione tavola
		 * 
		 * OOOOBOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOBKBOOO
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
		Assert.assertTrue(c.kingCanBeCaptured(4, 4, s));
	}
	
	@Test
	void kingCanBeCapturedTest_6() {	
		
		//Varianti del particolare caso in osservazione
		board[4][4] = Pawn.KING;
		board[3][4] = Pawn.BLACK;
		board[5][4] = Pawn.BLACK;
		board[4][5] = Pawn.BLACK;
		board[4][0] = Pawn.BLACK;
		board[4][3] = Pawn.WHITE;
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOBOOOO
		 * BOOWKBOOO
		 * OOOOBOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * 
		 * */
		
		
		//Creazione dello stato con la precedente disposizione delle pedine
		s.setBoard(board);
		s.setTurn(Turn.BLACK);
		System.out.println("qui");
		//Esecuzione Test
		Assert.assertFalse(c.kingCanBeCaptured(4, 4, s));
	}
	
	@Test
	void enemyOnTheTopTest_1() {	
		
		//Varianti del particolare caso in osservazione
		board[1][2] = Pawn.BLACK;
		board[2][2] = Pawn.WHITE;
		
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOBOOOOOO
		 * OOWOOOOOO
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
		Assert.assertTrue(c.enemyOnTheTop(2, 2, s));
			
	}
	
	@Test
	void enemyOnTheTopTest_2() {	
		
		//Varianti del particolare caso in osservazione
		board[1][2] = Pawn.WHITE;
		board[2][2] = Pawn.WHITE;
		
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOWOOOOOO
		 * OOWOOOOOO
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
		Assert.assertFalse(c.enemyOnTheTop(2, 2, s));
			
	}
	
	@Test
	void enemyOnTheTopTest_3() {	
		
		//Varianti del particolare caso in osservazione
		board[2][4] = Pawn.WHITE;
		
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOWOOOO
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
		Assert.assertTrue(c.enemyOnTheTop(2, 4, s));
			
	}
	
	@Test
	void enemyOnTheRightTest_1() {	
		
		//Varianti del particolare caso in osservazione
		board[2][3] = Pawn.BLACK;
		board[2][2] = Pawn.WHITE;
		
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOWBOOOOO
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
		Assert.assertTrue(c.enemyOnTheRight(2, 2, s));
			
	}
	
	@Test
	void enemyOnTheRightTest_2() {	
		
		//Varianti del particolare caso in osservazione
		board[2][3] = Pawn.WHITE;
		board[2][2] = Pawn.WHITE;
		
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOWWOOOOO
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
		Assert.assertFalse(c.enemyOnTheRight(2, 2, s));
			
	}
	
	@Test
	void enemyOnTheRightTest_3() {	
		
		//Varianti del particolare caso in osservazione
		board[1][3] = Pawn.WHITE;
		
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOWOOOOO
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
		Assert.assertTrue(c.enemyOnTheRight(1, 3, s));
			
	}
	
	@Test
	void enemyOnTheBottomTest_1() {	
		
		//Varianti del particolare caso in osservazione
		board[3][2] = Pawn.BLACK;
		board[2][2] = Pawn.WHITE;
		
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOWOOOOOO
		 * OOBOOOOOO
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
		Assert.assertTrue(c.enemyOnTheBottom(2, 2, s));
			
	}
	
	@Test
	void enemyOnTheBottomTest_2() {	
		
		//Varianti del particolare caso in osservazione
		board[3][2] = Pawn.WHITE;
		board[2][2] = Pawn.WHITE;
		
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOWOOOOOO
		 * OOWOOOOOO
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
		Assert.assertFalse(c.enemyOnTheBottom(2, 2, s));
			
	}
	
	@Test
	void enemyOnTheBottomTest_3() {	
		
		//Varianti del particolare caso in osservazione
		board[6][4] = Pawn.WHITE;
		
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOWOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * 
		 * */
		
		
		//Creazione dello stato con la precedente disposizione delle pedine
		s.setBoard(board);
		s.setTurn(Turn.WHITE);
		
		//Esecuzione Test
		Assert.assertTrue(c.enemyOnTheBottom(6, 4, s));
			
	}
	
	@Test
	void enemyOnTheLeftTest_1() {	
		
		//Varianti del particolare caso in osservazione
		board[2][1] = Pawn.BLACK;
		board[2][2] = Pawn.WHITE;
		
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OBWOOOOOO
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
		Assert.assertTrue(c.enemyOnTheLeft(2, 2, s));
			
	}
	
	@Test
	void enemyOnTheLeftTest_2() {	
		
		//Varianti del particolare caso in osservazione
		board[2][1] = Pawn.WHITE;
		board[2][2] = Pawn.WHITE;
		
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OWWOOOOOO
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
		Assert.assertFalse(c.enemyOnTheLeft(2, 2, s));
			
	}
	
	@Test
	void enemyOnTheLeftTest_3() {	
		
		//Varianti del particolare caso in osservazione
		board[1][5] = Pawn.WHITE;
		
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOWOOO
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
		Assert.assertTrue(c.enemyOnTheLeft(1, 5, s));
			
	}
	
	@Test
	void checkBlackCanArriveFromTopTest_1() {	
		
		//Varianti del particolare caso in osservazione
		board[0][3] = Pawn.BLACK;
		
		
		/* Rappresentazione tavola
		 * 
		 * OOOBOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOXOOOOO     <----- X = Pedina passata alla funzione
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
		Assert.assertTrue(c.checkBlackCanArriveFromTop(4, 3, s));
			
	}
	
	@Test
	void checkBlackCanArriveFromTopTest_2() {	
		
		//Varianti del particolare caso in osservazione
		board[0][3] = Pawn.BLACK;
		board[1][3] = Pawn.WHITE;
		
		/* Rappresentazione tavola
		 * 
		 * OOOBOOOOO
		 * OOOWOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOXOOOOO     <----- X = Pedina passata alla funzione
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
		Assert.assertFalse(c.checkBlackCanArriveFromTop(4, 3, s));
			
	}
	
	@Test
	void checkBlackCanArriveFromTopTest_3() {	
		
		//Varianti del particolare caso in osservazione
		board[1][4] = Pawn.BLACK;
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOBOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO     <----- X = Pedina passata alla funzione
		 * OOOOXOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * 
		 * */
		
		
		//Creazione dello stato con la precedente disposizione delle pedine
		s.setBoard(board);
		s.setTurn(Turn.BLACK);
		
		//Esecuzione Test
		Assert.assertFalse(c.checkBlackCanArriveFromTop(5, 4, s));
			
	}
		
	@Test
	void checkBlackCanArriveFromTopTest_4() {	
		
		//Varianti del particolare caso in osservazione
		board[0][4] = Pawn.BLACK;
		board[4][0] = Pawn.BLACK;
		
		/* Rappresentazione tavola
		 * 
		 * OOOOBOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOXOOOO
		 * BOOOOOOOO     <-----  X = casella passata alla funzione
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * XOOOOOOOO
		 * OOOOOOOOO
		 * 
		 * */
		
		
		//Creazione dello stato con la precedente disposizione delle pedine
		s.setBoard(board);
		s.setTurn(Turn.BLACK);
		
		//Esecuzione Test
		Assert.assertTrue(c.checkBlackCanArriveFromTop(3, 4, s));
		Assert.assertTrue(c.checkBlackCanArriveFromTop(7, 0, s));
			
	}
	
	@Test
	void checkBlackCanArriveFromRightTest_1() {	
		
		//Varianti del particolare caso in osservazione
		board[2][6] = Pawn.BLACK;
		
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOXOOOBOO
		 * OOOOOOOOO
		 * OOOOOOOOO     <----- X = casella passata alla funzione
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
		Assert.assertTrue(c.checkBlackCanArriveFromRight(2, 2, s));
			
	}
	
	@Test
	void checkBlackCanArriveFromRightTest_2() {	
		
		//Varianti del particolare caso in osservazione
		board[2][6] = Pawn.BLACK;
		board[2][5] = Pawn.WHITE;
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOXOOWBOO
		 * OOOOOOOOO
		 * OOOOOOOOO     <----- X = casella passata alla funzione
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
		Assert.assertFalse(c.checkBlackCanArriveFromRight(2, 2, s));
			
	}
	
	@Test
	void checkBlackCanArriveFromRightTest_3() {	
		
		//Varianti del particolare caso in osservazione
		board[4][5] = Pawn.BLACK;
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOXOBOOO     <----- X = casella passata alla funzione
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
		Assert.assertFalse(c.checkBlackCanArriveFromRight(4, 3, s));
			
	}

	@Test
	void checkBlackCanArriveFromRightTest_4() {	
		
		//Varianti del particolare caso in osservazione
		board[0][4] = Pawn.BLACK;
		board[4][8] = Pawn.BLACK;
		
		/* Rappresentazione tavola
		 * 
		 * OXOOBOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOXOB     <-----  X = casella passata alla funzione
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
		Assert.assertTrue(c.checkBlackCanArriveFromRight(0, 1, s));
		Assert.assertTrue(c.checkBlackCanArriveFromRight(4, 6, s));
			
	}
	
	@Test
	void checkBlackCanArriveFromLeftTest_1() {	
		
		//Varianti del particolare caso in osservazione
		board[2][2] = Pawn.BLACK;
		
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOBOOOXOO
		 * OOOOOOOOO
		 * OOOOOOOOO     <----- X = casella passata alla funzione
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
		Assert.assertTrue(c.checkBlackCanArriveFromLeft(2, 6, s));
			
	}
	
	@Test
	void checkBlackCanArriveFromLeftTest_2() {	
		
		//Varianti del particolare caso in osservazione
		board[2][2] = Pawn.BLACK;
		board[2][3] = Pawn.WHITE;
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOBWOOXOO
		 * OOOOOOOOO
		 * OOOOOOOOO     <----- X = casella passata alla funzione
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
		Assert.assertFalse(c.checkBlackCanArriveFromLeft(2, 6, s));
			
	}
	
	@Test
	void checkBlackCanArriveFromLeftTest_3() {	
		
		//Varianti del particolare caso in osservazione
		board[4][3] = Pawn.BLACK;
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOBOXOOO     <----- X = casella passata alla funzione
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
		Assert.assertFalse(c.checkBlackCanArriveFromLeft(4, 5, s));
			
	}
	
	@Test
	void checkBlackCanArriveFromLeftTest_4() {	
		
		//Varianti del particolare caso in osservazione
		board[0][4] = Pawn.BLACK;
		board[4][0] = Pawn.BLACK;
		
		/* Rappresentazione tavola
		 * 
		 * OOOOBOOXO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * BOXOOOOOO     <-----  X = casella passata alla funzione
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
		Assert.assertTrue(c.checkBlackCanArriveFromLeft(0, 7, s));
		Assert.assertTrue(c.checkBlackCanArriveFromLeft(4, 2, s));
			
	}
	
	@Test
	void checkBlackCanArriveFromBottomTest_1() {	
		
		//Varianti del particolare caso in osservazione
		board[8][3] = Pawn.BLACK;
		
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOXOOOOO     <----- X = casella passata alla funzione
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOBOOOOO
		 * 
		 * */
		
		
		//Creazione dello stato con la precedente disposizione delle pedine
		s.setBoard(board);
		s.setTurn(Turn.BLACK);
		
		//Esecuzione Test
		Assert.assertTrue(c.checkBlackCanArriveFromBottom(4, 3, s));
			
	}
	
	@Test
	void checkBlackCanArriveFromBottomTest_2() {	
		
		//Varianti del particolare caso in osservazione
		board[8][3] = Pawn.BLACK;
		board[7][3] = Pawn.WHITE;
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOXOOOOO     <----- X = casella passata alla funzione
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOWOOOOO
		 * OOOBOOOOO
		 * 
		 * */
		
		
		//Creazione dello stato con la precedente disposizione delle pedine
		s.setBoard(board);
		s.setTurn(Turn.BLACK);
		
		//Esecuzione Test
		Assert.assertFalse(c.checkBlackCanArriveFromBottom(4, 3, s));
			
	}
	
	@Test
	void checkBlackCanArriveFromBottomTest_3() {	
		
		//Varianti del particolare caso in osservazione
		board[7][4] = Pawn.BLACK;
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOXOOOO
		 * OOOOOOOOO     <----- X = casella passata alla funzione
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOBOOOO
		 * OOOOOOOOO
		 * 
		 * */
		
		
		//Creazione dello stato con la precedente disposizione delle pedine
		s.setBoard(board);
		s.setTurn(Turn.BLACK);
		
		//Esecuzione Test
		Assert.assertFalse(c.checkBlackCanArriveFromBottom(3, 4, s));
			
	}
	
	@Test
	void checkBlackCanArriveFromBottomTest_4() {	
		
		//Varianti del particolare caso in osservazione
		board[8][4] = Pawn.BLACK;
		board[4][0] = Pawn.BLACK;
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * XOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * BOOOOOOOO     <-----  X = casella passata alla funzione
		 * OOOOXOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOBOOOO
		 * 
		 * */
		
		
		//Creazione dello stato con la precedente disposizione delle pedine
		s.setBoard(board);
		s.setTurn(Turn.BLACK);
		
		//Esecuzione Test
		Assert.assertTrue(c.checkBlackCanArriveFromBottom(5, 4, s));
		Assert.assertTrue(c.checkBlackCanArriveFromBottom(1, 0, s));
			
	}

}

