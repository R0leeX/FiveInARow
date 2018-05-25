package FiveInARow;

import static org.junit.Assert.*;

import java.awt.Graphics;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;



public class FiveInARowTest {

	Board b;
	
	@Before
    public void setUp() {

        try {
			b = new Board();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        for (int row = 0; row < 13; row++)       // Fill the board with EMPTYs
            for (int col = 0; col < 13; col++)
               b.board[row][col] = 0;
    }
	
	
    @Test
	public void testwinner() {
		boolean expResult = true;
		boolean result = b.winner(0, 0);
		assertEquals(expResult, result);
		
	}
    
    @Test
    public void testResign() {
    	b.doResign();
    }
    @Test
    public void testReplay() {
    	b.gameInProgress = false;
    	b.doReplay();
    }
    @Test
    public void testgameOver() {
    	b.gameOver("WHITE wins the game!");
    }
    @Test 
    public void testWriteMatrix() {
    	b.writeMatrix("LastGame.txt", b.board);
    }
    @Test 
    public void testReadints() {
    	try {
			b.readints(b.board, "1 2 3 4 5", 0);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    @Test 
    public void testDoClickSquare() {
    	try {
			b.doClickSquare(0,0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    @Test
    public void testFiveInARow() {
    	try {
			FiveInARow f = new FiveInARow();
			f.main(null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
    }
	@Test
    public void testNotWin() {
		b.board[0][0] = 1;
		boolean expResult = false;
		boolean result = b.winner(0, 0);
		assertEquals(expResult, result);
		
	}
	@Test
    public void testwinner2() {
		b.board[0][0] = 1;
		b.board[0][1]= 1;
		b.board[0][2]= 1;
		b.board[0][3]= 1;
		b.board[0][4]= 1;
		boolean expResult = true;
		boolean result = b.winner(0, 0);
		assertEquals(expResult, result);
	}


}
