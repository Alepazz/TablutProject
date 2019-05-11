package IAPlayerTest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import IAPlayer.CommonHeuristicFunction;
import IAPlayer.IntelligenzaBianca;
import it.unibo.ai.didattica.competition.tablut.domain.State.Pawn;
import it.unibo.ai.didattica.competition.tablut.domain.State.Turn;
import it.unibo.ai.didattica.competition.tablut.domain.StateTablut;

class CommonHeuristicFunctionTest {

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
	void kingOnTheThroneTest_1() {	
		
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
		Assert.assertTrue(c.kingOnTheThrone(4, 4));
	}
	
	@Test
	void kingOnTheThroneTest_2() {	
		
		//Varianti del particolare caso in osservazione
		board[4][3] = Pawn.KING;
		
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOKOOOOO
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
		Assert.assertFalse(c.kingOnTheThrone(4, 3));
	}
	
	@Test
	void kingAdjacentToTheThroneTest_1() {	
		
		//Varianti del particolare caso in osservazione
		board[4][3] = Pawn.KING;
		
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOKOOOOO
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
		Assert.assertTrue(c.kingAdjacentToTheThrone(4, 3));
	}
	
	@Test
	void kingAdjacentToTheThroneTest_2() {	
		
		//Varianti del particolare caso in osservazione
		board[3][3] = Pawn.KING;
		
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOKOOOOO
		 * OOOOOOOOO
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
		Assert.assertFalse(c.kingAdjacentToTheThrone(3, 3));
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
		
		//Esecuzione Test
		Assert.assertFalse(c.kingCanBeCaptured(4, 4, s));
	}
	
	@Test
	void kingCanBeCapturedTest_7() {	
		
		//Varianti del particolare caso in osservazione
		board[4][4] = Pawn.KING;
		board[3][5] = Pawn.BLACK;
		board[5][4] = Pawn.BLACK;
		board[4][5] = Pawn.BLACK;
		board[4][3] = Pawn.BLACK;
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOBOOO
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
	void kingCanBeCapturedTest_8() {	
		
		//Varianti del particolare caso in osservazione
		board[4][3] = Pawn.KING;
		board[4][0] = Pawn.BLACK;
		board[3][3] = Pawn.BLACK;
		board[5][3] = Pawn.BLACK;
		board[4][4] = Pawn.THRONE;
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOBOOOOO
		 * BOOKTOOOO
		 * OOOBOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * 
		 * */
		
		
		//Creazione dello stato con la precedente disposizione delle pedine
		s.setBoard(board);
		s.setTurn(Turn.BLACK);
		
		//Esecuzione Test
		Assert.assertTrue(c.kingCanBeCaptured(4, 3, s));
	}
	
	@Test
	void kingCanBeCapturedTest_9() {	
		
		//Varianti del particolare caso in osservazione
		board[4][3] = Pawn.KING;
		board[6][2] = Pawn.BLACK;
		board[3][3] = Pawn.BLACK;
		board[5][3] = Pawn.BLACK;
		board[4][4] = Pawn.THRONE;
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOBOOOOO
		 * OOOKTOOOO
		 * OOOBOOOOO
		 * OOBOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * 
		 * */
		
		
		//Creazione dello stato con la precedente disposizione delle pedine
		s.setBoard(board);
		s.setTurn(Turn.BLACK);
		
		//Esecuzione Test
		Assert.assertTrue(c.kingCanBeCaptured(4, 3, s));
	}
	
	@Test
	void kingCanBeCapturedTest_10() {	
		
		//Varianti del particolare caso in osservazione
		board[4][3] = Pawn.KING;
		board[6][2] = Pawn.BLACK;
		board[3][3] = Pawn.BLACK;
		board[5][3] = Pawn.WHITE;
		board[4][4] = Pawn.THRONE;
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOBOOOOO
		 * OOOKTOOOO
		 * OOOWOOOOO
		 * OOBOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * 
		 * */
		
		
		//Creazione dello stato con la precedente disposizione delle pedine
		s.setBoard(board);
		s.setTurn(Turn.BLACK);
		
		//Esecuzione Test
		Assert.assertFalse(c.kingCanBeCaptured(4, 3, s));
	}
	
	@Test
	void kingCanBeCapturedTest_11() {	
		
		//Varianti del particolare caso in osservazione
		board[3][2] = Pawn.KING;
		board[3][3] = Pawn.BLACK;
		board[6][1] = Pawn.BLACK;
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOKBOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OBOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * 
		 * */
		
		
		//Creazione dello stato con la precedente disposizione delle pedine
		s.setBoard(board);
		s.setTurn(Turn.BLACK);
		
		//Esecuzione Test
		Assert.assertFalse(c.kingCanBeCaptured(3, 2, s));
	}
	
	@Test
	void kingCanBeCapturedTest_12() {	
		
		//Varianti del particolare caso in osservazione
		board[2][1] = Pawn.KING;
		board[2][2] = Pawn.BLACK;
		board[5][0] = Pawn.BLACK;
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OKBOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * BOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * 
		 * */
		
		
		//Creazione dello stato con la precedente disposizione delle pedine
		s.setBoard(board);
		s.setTurn(Turn.BLACK);
		
		//Esecuzione Test
		Assert.assertTrue(c.kingCanBeCaptured(2, 1, s));
	}
	
	@Test
	void kingCanBeCapturedTest_13() {	
		
		//Varianti del particolare caso in osservazione
		board[3][1] = Pawn.KING;
		board[5][2] = Pawn.BLACK;
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OKOOOOOOO
		 * OOOOOOOOO
		 * OOBOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * 
		 * */
		
		
		//Creazione dello stato con la precedente disposizione delle pedine
		s.setBoard(board);
		s.setTurn(Turn.BLACK);
		
		//Esecuzione Test
		Assert.assertTrue(c.kingCanBeCaptured(3, 1, s));
	}
	
	@Test
	void checkNeighbourTopLeftAndRight_1() {
		//Varianti del particolare caso in osservazione
		board[1][2] = Pawn.BLACK;
		board[0][1] = Pawn.BLACK;
		board[0][3] = Pawn.BLACK;
		
		
		/* Rappresentazione tavola
		 * 
		 * OBOBOOOOO
		 * OOBOOOOOO
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
		s.setTurn(Turn.BLACK);
		
		//Esecuzione Test
		Assert.assertSame("B", c.checkNeighbourTopLeft(1, 2, s));
		Assert.assertSame("B", c.checkNeighbourTopRight(1, 2, s));
		
	}
		
	@Test
	void checkNeighbourTopLeftandRight_2() {
		//Varianti del particolare caso in osservazione
		board[0][2] = Pawn.BLACK;
		
		
		/* Rappresentazione tavola
		 * 
		 * OOBOOOOOO
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
		s.setTurn(Turn.BLACK);
		
		//Esecuzione Test
		Assert.assertSame("X", c.checkNeighbourTopLeft(0, 2, s));
		Assert.assertSame("X", c.checkNeighbourTopRight(0, 2, s));
		
	}
	
	@Test
	void checkNeighbourBottomLeftAndRight_1() {
		//Varianti del particolare caso in osservazione
		board[1][2] = Pawn.BLACK;
		board[2][1] = Pawn.BLACK;
		board[2][3] = Pawn.BLACK;
		
		
		/* Rappresentazione tavola
		 * 
		 * OBOBOOOOO
		 * OOBOOOOOO
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
		s.setTurn(Turn.BLACK);
		
		//Esecuzione Test
		Assert.assertSame("B", c.checkNeighbourBottomLeft(1, 2, s));
		Assert.assertSame("B", c.checkNeighbourBottomRight(1, 2, s));
		
	}
		
	@Test
	void checkNeighbourBottomLeftandRight_2() {
		//Varianti del particolare caso in osservazione
		board[8][2] = Pawn.BLACK;
		
		
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
		 * OOBOOOOOO
		 * 
		 * */
		
		
		//Creazione dello stato con la precedente disposizione delle pedine
		s.setBoard(board);
		s.setTurn(Turn.BLACK);
		
		//Esecuzione Test
		Assert.assertSame("X", c.checkNeighbourBottomLeft(8, 2, s));
		Assert.assertSame("X", c.checkNeighbourBottomRight(8, 2, s));
		
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
	void enemyOnTheTopTest_4() {	
		
		//Varianti del particolare caso in osservazione
		board[0][2] = Pawn.WHITE;
		
		
		/* Rappresentazione tavola
		 * 
		 * OOWOOOOOO
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
		Assert.assertFalse(c.enemyOnTheTop(0, 2, s));
			
	}
	
	@Test
	void enemyOnTheTopTest_5() {	
		
		//Varianti del particolare caso in osservazione
		board[4][0] = Pawn.BLACK;
		
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * BOOOOOOOO     
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
		Assert.assertFalse(c.enemyOnTheTop(4, 0, s));
			
	}
	
	@Test
	void enemyOnTheTopTest_6() {	
		
		//Varianti del particolare caso in osservazione
		board[5][4] = Pawn.WHITE;
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO     
		 * OOOOWOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * 
		 * */
		
		
		//Creazione dello stato con la precedente disposizione delle pedine
		s.setBoard(board);
		s.setTurn(Turn.WHITE);
		
		//Esecuzione Test
		Assert.assertTrue(c.enemyOnTheTop(5, 4, s));
			
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
	void enemyOnTheRightTest_4() {	
		
		//Varianti del particolare caso in osservazione
		board[1][8] = Pawn.BLACK;
		
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOB
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
		Assert.assertFalse(c.enemyOnTheRight(1, 8, s));
			
	}
	
	@Test
	void enemyOnTheRightTest_5() {	
		
		//Varianti del particolare caso in osservazione
		board[4][0] = Pawn.BLACK;
		
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * BOOOOOOOO     
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
		Assert.assertFalse(c.enemyOnTheRight(4, 0, s));
			
	}
	
	@Test
	void enemyOnTheRightTest_6() {	
		
		//Varianti del particolare caso in osservazione
		board[4][3] = Pawn.WHITE;
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOWOOOOO     
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
		Assert.assertTrue(c.enemyOnTheRight(4, 3, s));
			
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
	void enemyOnTheBottomTest_4() {	
		
		//Varianti del particolare caso in osservazione
		board[8][2] = Pawn.WHITE;
		
		
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
		 * OOWOOOOOO
		 * 
		 * */
		
		
		//Creazione dello stato con la precedente disposizione delle pedine
		s.setBoard(board);
		s.setTurn(Turn.WHITE);
		
		//Esecuzione Test
		Assert.assertFalse(c.enemyOnTheBottom(8, 2, s));
			
	}
	
	@Test
	void enemyOnTheBottomTest_5() {	
		
		//Varianti del particolare caso in osservazione
		board[4][0] = Pawn.BLACK;
		
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * BOOOOOOOO     
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
		Assert.assertFalse(c.enemyOnTheBottom(4, 0, s));
			
	}
	
	@Test
	void enemyOnTheBottomTest_6() {	
		
		//Varianti del particolare caso in osservazione
		board[3][4] = Pawn.WHITE;
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOWOOOO
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
		Assert.assertTrue(c.enemyOnTheBottom(3, 4, s));
			
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
	void enemyOnTheLeftTest_4() {	
		
		//Varianti del particolare caso in osservazione
		board[1][0] = Pawn.BLACK;
		
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * BOOOOOOOO
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
		Assert.assertFalse(c.enemyOnTheLeft(1, 0, s));
			
	}
	
	@Test
	void enemyOnTheLeftTest_5() {	
		
		//Varianti del particolare caso in osservazione
		board[4][8] = Pawn.BLACK;
		
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOB     
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
		Assert.assertFalse(c.enemyOnTheLeft(4, 8, s));
			
	}
	
	@Test
	void enemyOnTheLeftTest_6() {	
		
		//Varianti del particolare caso in osservazione
		board[4][5] = Pawn.WHITE;
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOWOOO     
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
		Assert.assertTrue(c.enemyOnTheLeft(4, 5, s));
			
	}
	
	@Test
	void checkBlackCanBeCapturedTest_1() {
		//Varianti del particolare caso in osservazione
		board[3][4] = Pawn.WHITE;
		board[2][2] = Pawn.WHITE;
		board[2][3] = Pawn.BLACK;
				
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOWBOOOOO
		 * OOOOWOOOO
		 * OOOOOOOOO
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
		Assert.assertTrue(c.checkBlackCanBeCaptured(2, 3, s));
	}
	
	@Test
	void checkBlackCanBeCapturedTest_2() {
		//Varianti del particolare caso in osservazione
		board[3][1] = Pawn.WHITE;
		board[2][4] = Pawn.BLACK;
				
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOBOOOO
		 * OWOOOOOOO
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
		Assert.assertTrue(c.checkBlackCanBeCaptured(2, 4, s));
	}
	
	@Test
	void checkBlackCanBeCapturedTest_3() {
		//Varianti del particolare caso in osservazione
		board[2][2] = Pawn.WHITE;
		board[1][4] = Pawn.BLACK;
				
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOBOOOO
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
		Assert.assertFalse(c.checkBlackCanBeCaptured(1, 4, s));
	}
	
	@Test
	void checkBlackCanBeCapturedTest_4() {
		//Varianti del particolare caso in osservazione
		board[2][6] = Pawn.WHITE;
		board[4][5] = Pawn.BLACK;
				
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOWOO
		 * OOOOOOOOO
		 * OOOOOBOOO
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
		Assert.assertTrue(c.checkBlackCanBeCaptured(4, 5, s));
	}
	
	@Test
	void checkWhiteCanBeCapturedTest_1() {
		//Varianti del particolare caso in osservazione
		board[1][5] = Pawn.WHITE;
		board[0][5] = Pawn.BLACK;
		board[5][5] = Pawn.BLACK;
				
		/* Rappresentazione tavola
		 * 
		 * OOOOOBOOO
		 * OOOOOWOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOBOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * 
		 * */
		
		//IMPORTANTE: In teoria questa non dovrebbe essere catturabile visto che si trova gi� in mezzo a due neri
		//Creazione dello stato con la precedente disposizione delle pedine
		s.setBoard(board);
		s.setTurn(Turn.WHITE);
		
		//Esecuzione Test
		Assert.assertTrue(c.checkWhiteCanBeCaptured(1, 5, s));
	}
	
	@Test
	void checkWhiteCanBeCapturedTest_2() {
		//Varianti del particolare caso in osservazione
		board[1][5] = Pawn.WHITE;
		board[0][5] = Pawn.BLACK;
		board[1][4] = Pawn.BLACK;
				
		/* Rappresentazione tavola
		 * 
		 * OOOOOBOOO
		 * OOOOBWOOO
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
		Assert.assertFalse(c.checkWhiteCanBeCaptured(1, 5, s));
	}
	
	
	@Test
	void checkWhiteCanBeCapturedTest_4() {
		//Varianti del particolare caso in osservazione
		board[1][5] = Pawn.WHITE;
		board[1][6] = Pawn.BLACK;
		board[1][3] = Pawn.BLACK;
				
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOBOOWBOO
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
		s.setTurn(Turn.BLACK);
		
		//Esecuzione Test
		Assert.assertFalse(c.checkWhiteCanBeCaptured(1, 5, s));
	}
	
	@Test
	void checkWhiteCanBeCapturedTest_5() {
		//Varianti del particolare caso in osservazione
		board[3][4] = Pawn.WHITE;
		board[4][4] = Pawn.THRONE;
		board[2][6] = Pawn.BLACK;
				
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOBOO
		 * OOOOWOOOO
		 * OOOOTOOOO
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
		Assert.assertTrue(c.checkWhiteCanBeCaptured(3, 4, s));
	}
	

	@Test
	void checkPedinaIsolataTest_1() {	
		
		//Varianti del particolare caso in osservazione
		
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO     <----- X = Pedina passata alla funzione
		 * OOOOOOOOO
		 * OOXOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * 
		 * */
		
		
		//Creazione dello stato con la precedente disposizione delle pedine
		s.setBoard(board);
		s.setTurn(Turn.BLACK);
		
		//Esecuzione Test 
		Assert.assertTrue(c.checkPedinaIsolata(6, 3, s));
			
	}
	
	
	@Test
	void checkBlackCanArriveAdjacentInTopPositionTest_1() {	
		
		//Varianti del particolare caso in osservazione
		board[3][7] = Pawn.BLACK;
		
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOBO
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
		Assert.assertTrue(c.checkBlackCanArriveAdjacentInTopPosition(4, 3, s));
			
	}
	
	@Test
	void checkBlackCanArriveAdjacentInTopPositionTest_2() {	
		
		//Varianti del particolare caso in osservazione
		board[3][7] = Pawn.BLACK;
		board[3][6] = Pawn.WHITE;
		
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOWBO
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
		Assert.assertFalse(c.checkBlackCanArriveAdjacentInTopPosition(4, 3, s));
			
	}
	
	@Test
	void checkBlackCanArriveAdjacentInTopPositionTest_3() {	
		
		//Varianti del particolare caso in osservazione
		board[1][3] = Pawn.BLACK;
		
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOBOOOOO
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
		Assert.assertTrue(c.checkBlackCanArriveAdjacentInTopPosition(4, 3, s));
			
	}
	
	@Test
	void checkBlackCanArriveAdjacentInRightPositionTest_1() {	
		
		//Varianti del particolare caso in osservazione
		board[3][7] = Pawn.BLACK;
		
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOXOOOOBO
		 * OOOOOOOOO     <----- X = Pedina passata alla funzione
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
		Assert.assertTrue(c.checkBlackCanArriveAdjacentInRightPosition(3, 2, s));
			
	}
	
	@Test
	void checkBlackCanArriveAdjacentInRightPositionTest_2() {	
		
		//Varianti del particolare caso in osservazione
		board[3][7] = Pawn.BLACK;
		board[3][6] = Pawn.WHITE;
		
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOXOOOWBO
		 * OOOOOOOOO     <----- X = Pedina passata alla funzione
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
		Assert.assertFalse(c.checkBlackCanArriveAdjacentInRightPosition(3, 2, s));
			
	}
	
	@Test
	void checkBlackCanArriveAdjacentInRightPositionTest_3() {	
		
		//Varianti del particolare caso in osservazione
		board[6][3] = Pawn.BLACK;
		
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOXOOOOOO
		 * OOOOOOOOO     <----- X = Pedina passata alla funzione
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
		Assert.assertTrue(c.checkBlackCanArriveAdjacentInRightPosition(3, 2, s));
			
	}

	@Test
	void checkBlackCanArriveAdjacentInBottomPositionTest_1() {	
		
		//Varianti del particolare caso in osservazione
		board[5][7] = Pawn.BLACK;
		
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOXOOOOO     <----- X = Pedina passata alla funzione
		 * OOOOOOOBO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * 
		 * */
		
		
		//Creazione dello stato con la precedente disposizione delle pedine
		s.setBoard(board);
		s.setTurn(Turn.BLACK);
		
		//Esecuzione Test
		Assert.assertTrue(c.checkBlackCanArriveAdjacentInBottomPosition(4, 3, s));
			
	}
	
	@Test
	void checkBlackCanArriveAdjacentInBottomPositionTest_2() {	
		
		//Varianti del particolare caso in osservazione
		board[5][7] = Pawn.BLACK;
		board[5][6] = Pawn.WHITE;
		
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOXOOOOO     <----- X = Pedina passata alla funzione
		 * OOOOOOWBO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * 
		 * */
		
		
		//Creazione dello stato con la precedente disposizione delle pedine
		s.setBoard(board);
		s.setTurn(Turn.BLACK);
		
		//Esecuzione Test
		Assert.assertFalse(c.checkBlackCanArriveAdjacentInBottomPosition(4, 3, s));
			
	}
	
	@Test
	void checkBlackCanArriveAdjacentInBottomPositionTest_3() {	
		
		//Varianti del particolare caso in osservazione
		board[7][3] = Pawn.BLACK;
		board[5][3] = Pawn.WHITE;
		
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOXOOOOO     <----- X = Pedina passata alla funzione
		 * OOOWOOOOO
		 * OOOOOOOOO
		 * OOOBOOOOO
		 * OOOOOOOOO
		 * 
		 * */
		
		
		//Creazione dello stato con la precedente disposizione delle pedine
		s.setBoard(board);
		s.setTurn(Turn.BLACK);
		
		//Esecuzione Test
		Assert.assertFalse(c.checkBlackCanArriveAdjacentInBottomPosition(4, 3, s));
			
	}

	@Test
	void checkBlackCanArriveAdjacentInLeftPositionTest_1() {	
		
		//Varianti del particolare caso in osservazione
		board[3][2] = Pawn.BLACK;
		
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOBOOOOXO
		 * OOOOOOOOO     <----- X = Pedina passata alla funzione
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
		Assert.assertTrue(c.checkBlackCanArriveAdjacentInLeftPosition(3, 7, s));
			
	}
	
	@Test
	void checkBlackCanArriveAdjacentInLeftPositionTest_2() {	
		
		//Varianti del particolare caso in osservazione
		board[3][2] = Pawn.BLACK;
		board[3][3] = Pawn.WHITE;
		
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOBWOOOXO
		 * OOOOOOOOO     <----- X = Pedina passata alla funzione
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
		Assert.assertFalse(c.checkBlackCanArriveAdjacentInLeftPosition(3, 7, s));
			
	}
	
	@Test
	void checkBlackCanArriveAdjacentInLeftPositionTest_3() {	
		
		//Varianti del particolare caso in osservazione
		board[6][3] = Pawn.BLACK;
		
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOXOOOO
		 * OOOOOOOOO     <----- X = Pedina passata alla funzione
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
		Assert.assertTrue(c.checkBlackCanArriveAdjacentInLeftPosition(3, 4, s));
			
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
		 * BOOOOOOOO     <-----  X = Pedina passata alla funzione
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
	void checkBlackCanArriveFromTopTest_5() {	
		
		//Varianti del particolare caso in osservazione
		board[2][1] = Pawn.BLACK;
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OBOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO     <-----  X = Pedina passata alla funzione
		 * OOOOOOOOO
		 * OXOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * 
		 * */
		
		
		//Creazione dello stato con la precedente disposizione delle pedine
		s.setBoard(board);
		s.setTurn(Turn.BLACK);
		
		//Esecuzione Test
		Assert.assertFalse(c.checkBlackCanArriveFromTop(6, 1, s));
			
	}
	
	@Test
	void checkBlackCanArriveFromTopTest_6() {	
		
		//Varianti del particolare caso in osservazione
		board[3][0] = Pawn.BLACK;
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * BOOOOOOOO
		 * OOOOOOOOO     <-----  X = Pedina passata alla funzione
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
		 * OOOOOOOOO     <----- X = Pedina passata alla funzione
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
		 * OOOOOOOOO     <----- X = Pedina passata alla funzione
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
		 * OOOXOBOOO     <----- X = Pedina passata alla funzione
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
		 * OOOOOOXOB     <-----  X = Pedina passata alla funzione
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
	void checkBlackCanArriveFromRightTest_5() {	
		
		//Varianti del particolare caso in osservazione
		board[1][6] = Pawn.BLACK;
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOXOOOBOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO     <-----  X = Pedina passata alla funzione
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
		Assert.assertFalse(c.checkBlackCanArriveFromRight(1, 2, s));
			
	}
	
	@Test
	void checkBlackCanArriveFromRightTest_6() {	
		
		//Varianti del particolare caso in osservazione
		board[0][5] = Pawn.BLACK;
		
		/* Rappresentazione tavola
		 * 
		 * OXOOOBOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO     <-----  X = Pedina passata alla funzione
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
		 * OOOXOOOOO     <----- X = Pedina passata alla funzione
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
		 * OOOXOOOOO     <----- X = Pedina passata alla funzione
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
		 * OOOOOOOOO     <----- X = Pedina passata alla funzione
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
		 * BOOOOOOOO     <-----  X = Pedina passata alla funzione
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
	
	@Test
	void checkBlackCanArriveFromBottomTest_5() {	
		
		//Varianti del particolare caso in osservazione
		board[6][1] = Pawn.BLACK;
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OXOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO     <-----  X = Pedina passata alla funzione
		 * OOOOOOOOO
		 * OBOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * 
		 * */
		
		
		//Creazione dello stato con la precedente disposizione delle pedine
		s.setBoard(board);
		s.setTurn(Turn.BLACK);
		
		//Esecuzione Test
		Assert.assertFalse(c.checkBlackCanArriveFromBottom(2, 1, s));
			
	}
	
	@Test
	void checkBlackCanArriveFromBottomTest_6() {	
		
		//Varianti del particolare caso in osservazione
		board[5][0] = Pawn.BLACK;
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * XOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO     <-----  X = Pedina passata alla funzione
		 * BOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * 
		 * */
		
		
		//Creazione dello stato con la precedente disposizione delle pedine
		s.setBoard(board);
		s.setTurn(Turn.BLACK);
		
		//Esecuzione Test
		Assert.assertTrue(c.checkBlackCanArriveFromBottom(1, 0, s));
			
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
		 * OOOOOOOOO     <----- X = Pedina passata alla funzione
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
		 * OOOOOOOOO     <----- X = Pedina passata alla funzione
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
		 * OOOBOXOOO     <----- X = Pedina passata alla funzione
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
		 * BOXOOOOOO     <-----  X = Pedina passata alla funzione
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
	void checkBlackCanArriveFromLeftTest_5() {	
		
		//Varianti del particolare caso in osservazione
		board[1][2] = Pawn.BLACK;
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOBOOOXOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO     <-----  X = Pedina passata alla funzione
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
		Assert.assertFalse(c.checkBlackCanArriveFromLeft(1, 6, s));
			
	}
	
	@Test
	void checkBlackCanArriveFromLeftTest_6() {	
		
		//Varianti del particolare caso in osservazione
		board[0][3] = Pawn.BLACK;
		
		/* Rappresentazione tavola
		 * 
		 * OOOBOOOXO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO     <-----  X = Pedina passata alla funzione
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
			
	}

	@Test
	void checkWhiteCanArriveFromTopTest_1() {	
		
		//Varianti del particolare caso in osservazione
		board[1][3] = Pawn.WHITE;
		
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
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
		s.setTurn(Turn.WHITE);
		
		//Esecuzione Test
		Assert.assertTrue(c.checkWhiteCanArriveFromTop(4, 3, s));
			
	}
	
	@Test
	void checkWhiteCanArriveFromTopTest_2() {	
		
		//Varianti del particolare caso in osservazione
		board[2][3] = Pawn.BLACK;
		board[1][3] = Pawn.WHITE;
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOWOOOOO
		 * OOOBOOOOO
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
		s.setTurn(Turn.WHITE);
		
		//Esecuzione Test
		Assert.assertFalse(c.checkWhiteCanArriveFromTop(4, 3, s));
			
	}
	
	@Test
	void checkWhiteCanArriveFromTopTest_3() {	
		
		//Varianti del particolare caso in osservazione
		board[2][4] = Pawn.WHITE;
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOWOOOO
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
		s.setTurn(Turn.WHITE);
		
		//Esecuzione Test
		Assert.assertFalse(c.checkWhiteCanArriveFromTop(5, 4, s));
			
	}
		
	@Test
	void checkWhiteCanArriveFromTopTest_4() {	
		
		//Varianti del particolare caso in osservazione
		board[1][1] = Pawn.WHITE;

		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OWOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO     <-----  X = Pedina passata alla funzione
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OXOOOOOOO
		 * OOOOOOOOO
		 * 
		 * */
		
		
		//Creazione dello stato con la precedente disposizione delle pedine
		s.setBoard(board);
		s.setTurn(Turn.WHITE);
		
		//Esecuzione Test
		Assert.assertFalse(c.checkWhiteCanArriveFromTop(7, 1, s));
			
	}
	
	@Test
	void checkWhiteCanArriveFromTopTest_5() {	
		
		//Varianti del particolare caso in osservazione
		board[2][1] = Pawn.WHITE;
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OWOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO     <-----  X = Pedina passata alla funzione
		 * OOOOOOOOO
		 * OXOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * 
		 * */
		
		
		//Creazione dello stato con la precedente disposizione delle pedine
		s.setBoard(board);
		s.setTurn(Turn.WHITE);
		
		//Esecuzione Test
		Assert.assertFalse(c.checkWhiteCanArriveFromTop(6, 1, s));
			
	}

	@Test
	void checkWhiteCanArriveFromRightTest_1() {	
		
		//Varianti del particolare caso in osservazione
		board[2][6] = Pawn.WHITE;
		
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOXOOOWOO
		 * OOOOOOOOO
		 * OOOOOOOOO     <----- X = Pedina passata alla funzione
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
		Assert.assertTrue(c.checkWhiteCanArriveFromRight(2, 2, s));
			
	}
	
	@Test
	void checkWhiteCanArriveFromRightTest_2() {	
		
		//Varianti del particolare caso in osservazione
		board[2][6] = Pawn.WHITE;
		board[2][5] = Pawn.BLACK;
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOXOOBWOO
		 * OOOOOOOOO
		 * OOOOOOOOO     <----- X = Pedina passata alla funzione
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
		Assert.assertFalse(c.checkWhiteCanArriveFromRight(2, 2, s));
			
	}
	
	@Test
	void checkWhiteCanArriveFromRightTest_3() {	
		
		//Varianti del particolare caso in osservazione
		board[4][5] = Pawn.WHITE;
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOXOWOOO     <----- X = Pedina passata alla funzione
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
		Assert.assertFalse(c.checkWhiteCanArriveFromRight(4, 3, s));
			
	}

	@Test
	void checkWhiteCanArriveFromRightTest_4() {	
		
		//Varianti del particolare caso in osservazione
		board[1][7] = Pawn.WHITE;
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OXOOOOOWO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO     <-----  X = Pedina passata alla funzione
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
		Assert.assertFalse(c.checkWhiteCanArriveFromRight(1, 1, s));
			
	}
	
	@Test
	void checkWhiteCanArriveFromRightTest_5() {	
		
		//Varianti del particolare caso in osservazione
		board[1][6] = Pawn.WHITE;
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOXOOOWOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO     <-----  X = Pedina passata alla funzione
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
		Assert.assertFalse(c.checkWhiteCanArriveFromRight(1, 2, s));
			
	}
	
	@Test
	void checkWhiteCanArriveFromBottomTest_1() {	
		
		//Varianti del particolare caso in osservazione
		board[7][3] = Pawn.WHITE;
		
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOXOOOOO     <----- X = Pedina passata alla funzione
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOWOOOOO
		 * OOOOOOOOO
		 * 
		 * */
		
		
		//Creazione dello stato con la precedente disposizione delle pedine
		s.setBoard(board);
		s.setTurn(Turn.WHITE);
		
		//Esecuzione Test
		Assert.assertTrue(c.checkWhiteCanArriveFromBottom(4, 3, s));
			
	}
	
	@Test
	void checkWhiteCanArriveFromBottomTest_2() {	
		
		//Varianti del particolare caso in osservazione
		board[6][3] = Pawn.BLACK;
		board[7][3] = Pawn.WHITE;
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOXOOOOO     <----- X = Pedina passata alla funzione
		 * OOOOOOOOO
		 * OOOBOOOOO
		 * OOOWOOOOO
		 * OOOOOOOOO
		 * 
		 * */
		
		
		//Creazione dello stato con la precedente disposizione delle pedine
		s.setBoard(board);
		s.setTurn(Turn.WHITE);
		
		//Esecuzione Test
		Assert.assertFalse(c.checkWhiteCanArriveFromBottom(4, 3, s));
			
	}
	
	@Test
	void checkWhiteCanArriveFromBottomTest_3() {	
		
		//Varianti del particolare caso in osservazione
		board[6][4] = Pawn.WHITE;
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOXOOOO
		 * OOOOOOOOO     <----- X = Pedina passata alla funzione
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
		Assert.assertFalse(c.checkWhiteCanArriveFromBottom(3, 4, s));
			
	}
	
	@Test
	void checkWhiteCanArriveFromBottomTest_4() {	
		
		//Varianti del particolare caso in osservazione
		board[7][1] = Pawn.WHITE;
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OXOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO     <-----  X = Pedina passata alla funzione
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OWOOOOOOO
		 * OOOOOOOOO
		 * 
		 * */
		
		
		//Creazione dello stato con la precedente disposizione delle pedine
		s.setBoard(board);
		s.setTurn(Turn.WHITE);
		
		//Esecuzione Test
		Assert.assertFalse(c.checkWhiteCanArriveFromBottom(1, 1, s));
			
	}
	
	@Test
	void checkWhiteCanArriveFromBottomTest_5() {	
		
		//Varianti del particolare caso in osservazione
		board[6][1] = Pawn.WHITE;
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OXOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO     <-----  X = Pedina passata alla funzione
		 * OOOOOOOOO
		 * OWOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * 
		 * */
		
		
		//Creazione dello stato con la precedente disposizione delle pedine
		s.setBoard(board);
		s.setTurn(Turn.WHITE);
		
		//Esecuzione Test
		Assert.assertFalse(c.checkWhiteCanArriveFromBottom(2, 1, s));
			
	}
	
	@Test
	void checkWhiteCanArriveFromLeftTest_1() {	
		
		//Varianti del particolare caso in osservazione
		board[2][2] = Pawn.WHITE;
		
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOWOOOXOO
		 * OOOOOOOOO
		 * OOOOOOOOO     <----- X = Pedina passata alla funzione
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
		Assert.assertTrue(c.checkWhiteCanArriveFromLeft(2, 6, s));
			
	}
	
	@Test
	void checkWhiteCanArriveFromLeftTest_2() {	
		
		//Varianti del particolare caso in osservazione
		board[2][2] = Pawn.WHITE;
		board[2][3] = Pawn.BLACK;
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOWBOOXOO
		 * OOOOOOOOO
		 * OOOOOOOOO     <----- X = Pedina passata alla funzione
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
		Assert.assertFalse(c.checkWhiteCanArriveFromLeft(2, 6, s));
			
	}
	
	@Test
	void checkWhiteCanArriveFromLeftTest_3() {	
		
		//Varianti del particolare caso in osservazione
		board[4][3] = Pawn.WHITE;
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOWOXOOO     <----- X = Pedina passata alla funzione
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
		Assert.assertFalse(c.checkWhiteCanArriveFromLeft(4, 5, s));
			
	}
	
	@Test
	void checkWhiteCanArriveFromLeftTest_4() {	
		
		//Varianti del particolare caso in osservazione
		board[1][1] = Pawn.WHITE;
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OWOOOOOXO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO     <-----  X = Pedina passata alla funzione
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
		Assert.assertFalse(c.checkWhiteCanArriveFromLeft(1, 7, s));
			
	}
	
	@Test
	void checkWhiteCanArriveFromLeftTest_5() {	
		
		//Varianti del particolare caso in osservazione
		board[1][2] = Pawn.WHITE;
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOWOOOXOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO     <-----  X = Pedina passata alla funzione
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
		Assert.assertFalse(c.checkWhiteCanArriveFromLeft(1, 6, s));
			
	}
	
	@Test
	void checkWhiteCanArriveAdjacentInTopPositionTest_1() {	
		
		//Varianti del particolare caso in osservazione
		board[3][7] = Pawn.WHITE;
		
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOWO
		 * OOOXOOOOO     <----- X = Pedina passata alla funzione
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
		Assert.assertTrue(c.checkWhiteCanArriveAdjacentInTopPosition(4, 3, s));
			
	}
	
	@Test
	void checkWhiteCanArriveAdjacentInTopPositionTest_2() {	
		
		//Varianti del particolare caso in osservazione
		board[3][7] = Pawn.WHITE;
		board[3][6] = Pawn.BLACK;
		
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOBWO
		 * OOOXOOOOO     <----- X = Pedina passata alla funzione
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
		Assert.assertFalse(c.checkWhiteCanArriveAdjacentInTopPosition(4, 3, s));
			
	}
	
	@Test
	void checkWhiteCanArriveAdjacentInTopPositionTest_3() {	
		
		//Varianti del particolare caso in osservazione
		board[1][3] = Pawn.WHITE;
		
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
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
		s.setTurn(Turn.WHITE);
		
		//Esecuzione Test
		Assert.assertTrue(c.checkWhiteCanArriveAdjacentInTopPosition(4, 3, s));
			
	}
	
	@Test
	void checkWhiteCanArriveAdjacentInRightPositionTest_1() {	
		
		//Varianti del particolare caso in osservazione
		board[3][7] = Pawn.WHITE;
		
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOXOOOOWO
		 * OOOOOOOOO     <----- X = Pedina passata alla funzione
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
		Assert.assertTrue(c.checkWhiteCanArriveAdjacentInRightPosition(3, 2, s));
			
	}
	
	@Test
	void checkWhiteCanArriveAdjacentInRightPositionTest_2() {	
		
		//Varianti del particolare caso in osservazione
		board[3][7] = Pawn.WHITE;
		board[3][6] = Pawn.BLACK;
		
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOXOOOBWO
		 * OOOOOOOOO     <----- X = Pedina passata alla funzione
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
		Assert.assertFalse(c.checkWhiteCanArriveAdjacentInRightPosition(3, 2, s));
			
	}
	
	@Test
	void checkWhiteCanArriveAdjacentInRightPositionTest_3() {	
		
		//Varianti del particolare caso in osservazione
		board[6][3] = Pawn.WHITE;
		
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOXOOOOOO
		 * OOOOOOOOO     <----- X = Pedina passata alla funzione
		 * OOOOOOOOO
		 * OOOWOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * 
		 * */
		
		
		//Creazione dello stato con la precedente disposizione delle pedine
		s.setBoard(board);
		s.setTurn(Turn.WHITE);
		
		//Esecuzione Test
		Assert.assertTrue(c.checkWhiteCanArriveAdjacentInRightPosition(3, 2, s));
			
	}

	@Test
	void checkWhiteCanArriveAdjacentInBottomPositionTest_1() {	
		
		//Varianti del particolare caso in osservazione
		board[5][7] = Pawn.WHITE;
		
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOXOOOOO     <----- X = Pedina passata alla funzione
		 * OOOOOOOWO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * 
		 * */
		
		
		//Creazione dello stato con la precedente disposizione delle pedine
		s.setBoard(board);
		s.setTurn(Turn.WHITE);
		
		//Esecuzione Test
		Assert.assertTrue(c.checkWhiteCanArriveAdjacentInBottomPosition(4, 3, s));
			
	}
	
	@Test
	void checkWhiteCanArriveAdjacentInBottomPositionTest_2() {	
		
		//Varianti del particolare caso in osservazione
		board[5][7] = Pawn.WHITE;
		board[5][6] = Pawn.BLACK;
		
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOXOOOOO     <----- X = Pedina passata alla funzione
		 * OOOOOOBWO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * 
		 * */
		
		
		//Creazione dello stato con la precedente disposizione delle pedine
		s.setBoard(board);
		s.setTurn(Turn.WHITE);
		
		//Esecuzione Test
		Assert.assertFalse(c.checkWhiteCanArriveAdjacentInBottomPosition(4, 3, s));
			
	}
	
	@Test
	void checkWhiteCanArriveAdjacentInBottomPositionTest_3() {	
		
		//Varianti del particolare caso in osservazione
		board[7][3] = Pawn.WHITE;
		
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOXOOOOO     <----- X = Pedina passata alla funzione
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOWOOOOO
		 * OOOOOOOOO
		 * 
		 * */
		
		
		//Creazione dello stato con la precedente disposizione delle pedine
		s.setBoard(board);
		s.setTurn(Turn.WHITE);
		
		//Esecuzione Test
		Assert.assertTrue(c.checkWhiteCanArriveAdjacentInBottomPosition(4, 3, s));
			
	}

	@Test
	void checkWhiteCanArriveAdjacentInLeftPositionTest_1() {	
		
		//Varianti del particolare caso in osservazione
		board[3][2] = Pawn.WHITE;
		
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOWOOOOXO
		 * OOOOOOOOO     <----- X = Pedina passata alla funzione
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
		Assert.assertTrue(c.checkWhiteCanArriveAdjacentInLeftPosition(3, 7, s));
			
	}
	
	@Test
	void checkWhiteCanArriveAdjacentInLeftPositionTest_2() {	
		
		//Varianti del particolare caso in osservazione
		board[3][2] = Pawn.WHITE;
		board[3][3] = Pawn.BLACK;
		
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOWBOOOXO
		 * OOOOOOOOO     <----- X = Pedina passata alla funzione
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
		Assert.assertFalse(c.checkWhiteCanArriveAdjacentInLeftPosition(3, 7, s));
			
	}
	
	@Test
	void checkWhiteCanArriveAdjacentInLeftPositionTest_3() {	
		
		//Varianti del particolare caso in osservazione
		board[6][3] = Pawn.WHITE;
		
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOXOOOO
		 * OOOOOOOOO     <----- X = Pedina passata alla funzione
		 * OOOOOOOOO
		 * OOOWOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * 
		 * */
		
		
		//Creazione dello stato con la precedente disposizione delle pedine
		s.setBoard(board);
		s.setTurn(Turn.WHITE);
		
		//Esecuzione Test
		Assert.assertTrue(c.checkWhiteCanArriveAdjacentInLeftPosition(3, 4, s));
			
	}
	
	@Test
	void checkPawnBlockedTest_1() {	
		
		//Varianti del particolare caso in osservazione
		board[6][3] = Pawn.WHITE;
		board[6][2] = Pawn.BLACK;
		board[6][4] = Pawn.BLACK;
		board[5][3] = Pawn.BLACK;
		board[7][3] = Pawn.BLACK;
		
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO     
		 * OOOBOOOOO
		 * OOBWBOOOO
		 * OOOBOOOOO
		 * OOOOOOOOO
		 * 
		 * */
		
		
		//Creazione dello stato con la precedente disposizione delle pedine
		s.setBoard(board);
		s.setTurn(Turn.WHITE);
		
		//Esecuzione Test
		Assert.assertTrue(c.checkPawnBlocked(6, 3, s));
		
	}
	
	@Test
	void checkPawnBlockedTest_2() {	
		
		//Varianti del particolare caso in osservazione
		board[7][3] = Pawn.WHITE;
		board[7][2] = Pawn.BLACK;
		board[6][3] = Pawn.BLACK;
		
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO     
		 * OOOOOOOOO
		 * OOOBOOOOO
		 * OOBWOOOOO
		 * OOOOOOOOO
		 * 
		 * */
		
		
		//Creazione dello stato con la precedente disposizione delle pedine
		s.setBoard(board);
		s.setTurn(Turn.WHITE);
		
		//Esecuzione Test
		Assert.assertTrue(c.checkPawnBlocked(7, 3, s));
		
	}
	
	@Test
	void checkPawnBlockedTest_3() {	
		
		//Varianti del particolare caso in osservazione
		board[4][2] = Pawn.WHITE;
		board[3][3] = Pawn.BLACK;
		board[5][3] = Pawn.BLACK;
		board[4][3] = Pawn.KING;
		board[4][4] = Pawn.THRONE;
		
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOBOOOOO
		 * OOWKTOOOO     
		 * OOOBOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * 
		 * */
		
		
		//Creazione dello stato con la precedente disposizione delle pedine
		s.setBoard(board);
		s.setTurn(Turn.WHITE);
		
		//Esecuzione Test
		Assert.assertTrue(c.checkPawnBlocked(4, 3, s));
		
	}
	
	@Test
	void checkPawnBlockedTest_4() {	
		
		//Varianti del particolare caso in osservazione
		board[3][1] = Pawn.WHITE;
		board[4][1] = Pawn.BLACK;
		board[4][2] = Pawn.BLACK;
		board[5][1] = Pawn.WHITE;
		
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OWOOOOOOO
		 * OBBOOOOOO     
		 * OWOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * 
		 * */
		
		
		//Creazione dello stato con la precedente disposizione delle pedine
		s.setBoard(board);
		s.setTurn(Turn.BLACK);
		
		//Esecuzione Test
		Assert.assertFalse(c.checkPawnBlocked(4, 1, s));
		
	}
	
	@Test
	void checkPawnBlockedTest_5() {	
		
		//Varianti del particolare caso in osservazione
		board[3][2] = Pawn.WHITE;
		board[4][3] = Pawn.KING;
		board[4][2] = Pawn.BLACK;
		board[5][2] = Pawn.WHITE;
		
		
		/* Rappresentazione tavola
		 * 
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOWOOOOOO
		 * OOBKOOOOO     
		 * OOWOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * OOOOOOOOO
		 * 
		 * */
		
		
		//Creazione dello stato con la precedente disposizione delle pedine
		s.setBoard(board);
		s.setTurn(Turn.BLACK);
		
		//Esecuzione Test
		Assert.assertTrue(c.checkPawnBlocked(4, 2, s));
		
	}
	
	@Test
	void blackIsIsolatedTest_1() {
	
	
		board[1][1] = Pawn.BLACK;
			
			/* Rappresentazione tavola
			 * 
			 * OOOOOOOOO
			 * OBOOOOOOO
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
		s.setTurn(Turn.BLACK);
		
		//Esecuzione Test
		Assert.assertTrue(c.blackIsIsolated(1, 1, s));
					
		
	}
	
	@Test
	void blackIsIsolatedTest_2() {
	
	
		board[1][1] = Pawn.BLACK;
		board[2][1] = Pawn.BLACK;
			
			/* Rappresentazione tavola
			 * 
			 * OOOOOOOOO
			 * OBOOOOOOO
			 * OBOOOOOOO
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
		s.setTurn(Turn.BLACK);
		
		//Esecuzione Test
		Assert.assertFalse(c.blackIsIsolated(1, 1, s));
					
		
	}
	
	@Test
	void checkDraw() {
		
		IntelligenzaBianca ia = new IntelligenzaBianca();
		
		ia.setState(s);
		
		Pawn[][] board1 = {{Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY},
				{Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY},
				{Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY},
				{Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY},
				{Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.THRONE,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY},
				{Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY},
				{Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY},
				{Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY},
				{Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY}	
	};
				
		board1[0][3] = Pawn.BLACK;
		board1[0][4] = Pawn.BLACK;
		board1[0][5] = Pawn.BLACK;
		board1[1][4] = Pawn.BLACK;
		board1[3][0] = Pawn.BLACK;
		board1[3][8] = Pawn.BLACK;
		board1[4][0] = Pawn.BLACK;
		board1[4][1] = Pawn.BLACK;
		board1[4][7] = Pawn.BLACK;
		board1[4][8] = Pawn.BLACK;
		board1[5][0] = Pawn.BLACK;
		board1[5][8] = Pawn.BLACK;
		board1[7][4] = Pawn.BLACK;
		board1[8][3] = Pawn.BLACK;
		board1[8][4] = Pawn.BLACK;
		board1[8][5] = Pawn.BLACK;
		board1[2][4] = Pawn.WHITE;
		board1[3][4] = Pawn.WHITE;
		board1[4][2] = Pawn.WHITE;
		board1[4][5] = Pawn.WHITE;
		board1[4][6] = Pawn.WHITE;
		board1[5][4] = Pawn.WHITE;
		board1[6][4] = Pawn.WHITE;
		board1[3][3] = Pawn.WHITE;
		board1[4][3] = Pawn.EMPTY;
		board1[4][4] = Pawn.KING;
		
		
		StateTablut nuovoStato = new StateTablut();
		nuovoStato.setBoard(board1);
		System.out.println(nuovoStato.toString());
		
		ia.setState(nuovoStato);
		
		Pawn[][] board2 = {{Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY},
				{Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY},
				{Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY},
				{Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY},
				{Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.THRONE,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY},
				{Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY},
				{Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY},
				{Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY},
				{Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY}	
	};
		
		board2[0][3] = Pawn.BLACK;
		board2[0][4] = Pawn.BLACK;
		board2[0][5] = Pawn.BLACK;
		board2[1][4] = Pawn.BLACK;
		board2[3][0] = Pawn.BLACK;
		board2[3][8] = Pawn.BLACK;
		board2[4][0] = Pawn.BLACK;
		board2[4][1] = Pawn.BLACK;
		board2[4][7] = Pawn.BLACK;
		board2[4][8] = Pawn.BLACK;
		board2[5][0] = Pawn.BLACK;
		board2[5][8] = Pawn.BLACK;
		board2[7][4] = Pawn.BLACK;
		board2[8][3] = Pawn.BLACK;
		board2[8][4] = Pawn.BLACK;
		board2[8][5] = Pawn.BLACK;
		board2[2][4] = Pawn.WHITE;
		board2[3][4] = Pawn.WHITE;
		board2[4][2] = Pawn.WHITE;
		board2[4][3] = Pawn.WHITE;
		//board2[3][3] = Pawn.EMPTY;
		board2[4][5] = Pawn.WHITE;
		board2[4][6] = Pawn.WHITE;
		board2[5][4] = Pawn.WHITE;
		board2[6][4] = Pawn.WHITE;
		board2[4][4] = Pawn.KING; //commentare una di queste righe per ottenere false
		
		nuovoStato = new StateTablut();
		nuovoStato.setBoard(board2);
		System.out.println(nuovoStato.toString());
						
		Assert.assertTrue(ia.checkDraw(nuovoStato));
				
	}
	
	@Test
	void checkMoveWhiteT() {
		
		Assert.assertEquals(3, c.canMoveWhite(4, 5, "T", s));
	}
	
	@Test
	void checkMoveWhiteB() {
		
		Assert.assertEquals(3, c.canMoveWhite(4, 5, "B", s));
	}
	
	@Test
	void checkMoveWhiteL() {
		
		Assert.assertEquals(-1, c.canMoveWhite(4, 5, "L", s));
	}
	
	@Test
	void checkMoveWhiteR() {
		
		Assert.assertEquals(-1, c.canMoveWhite(4, 5, "R", s));
	}
	
	@Test
	void checkMoveKingT() {
		
		Assert.assertEquals(-1, c.canMoveWhite(4, 4, "T", s));
		
	}
	
	@Test
	void checkMoveKingB() {
		
		Assert.assertEquals(-1, c.canMoveWhite(4, 4, "B", s));
		
	}
	
	@Test
	void checkMoveKingL() {
		
		Assert.assertEquals(-1, c.canMoveWhite(4, 4, "L", s));
		
	}
	
	@Test
	void checkMoveKingR() {
		
		Assert.assertEquals(-1, c.canMoveWhite(4, 4, "R", s));
		
	}
	
	@Test
	void checkMoveKingL_1() {
		
		Pawn[][] board2 = {{Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY},
				{Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY},
				{Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY},
				{Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY},
				{Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.THRONE,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY},
				{Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY},
				{Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY},
				{Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY},
				{Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY}	
	};
		
		board2[2][3] = Pawn.BLACK;
		
		StateTablut nuovoStato = new StateTablut();
		nuovoStato.setBoard(board2);
		
		Assert.assertEquals(1, c.canMoveWhite(2, 5, "L", nuovoStato));
	}
	
	@Test
	void checkMoveKingR_1() {
		
		Pawn[][] board2 = {{Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY},
				{Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY},
				{Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY},
				{Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY},
				{Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.THRONE,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY},
				{Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY},
				{Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY},
				{Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY},
				{Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY}	
	};
		
		board2[2][5] = Pawn.BLACK;
		
		StateTablut nuovoStato = new StateTablut();
		nuovoStato.setBoard(board2);
		
		Assert.assertEquals(1, c.canMoveWhite(2, 3, "R", nuovoStato));
	}
	
	@Test
	void checkMoveKingT_1() {
		
		Pawn[][] board2 = {{Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY},
				{Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY},
				{Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY},
				{Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY},
				{Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.THRONE,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY},
				{Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY},
				{Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY},
				{Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY},
				{Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY}	
	};
		
		board2[2][2] = Pawn.BLACK;
		
		StateTablut nuovoStato = new StateTablut();
		nuovoStato.setBoard(board2);
		
		Assert.assertEquals(2, c.canMoveWhite(5, 2, "T", nuovoStato));
	}
	
	@Test
	void checkMoveKingB_1() {
		
		Pawn[][] board2 = {{Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY},
				{Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY},
				{Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY},
				{Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY},
				{Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.THRONE,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY},
				{Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY},
				{Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY},
				{Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY},
				{Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY,Pawn.EMPTY}	
	};
		
		board2[5][2] = Pawn.BLACK;
		
		StateTablut nuovoStato = new StateTablut();
		nuovoStato.setBoard(board2);
		
		Assert.assertEquals(2, c.canMoveWhite(2, 2, "B", nuovoStato));
	}
	
}
