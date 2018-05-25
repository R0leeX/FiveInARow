package FiveInARow;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;

import javax.swing.*;

/**
 * Megjelen�ti a t�bl�t ami 168*168 pixel m�ret� n�gyzetr�csos t�bla, 2pixel m�ret� v�laszt�kkal.
 * Azt felt�telezve, hogy az ablak 172*172 pixel m�ret�. Ez az oszt�ly v�gzi a f� munk�t amikor a j�t�kosok j�tszanak �s megjelen�ti a t�bl�t.
 * Ebben a programban a t�bla amin a j�t�k zajlik 13 sorb�l �s 13 oszlopb�l �ll� n�gyzetr�cs.
 * 
 * @author Roland
 *
 */

public class Board extends Game implements ActionListener, MouseListener{
		
	/**
	 * Konstruktor. L�trehozza a gombokat �s a c�mk�t. Figyeli az eg�r kattint�st �s a gombokra kattint�st. Elind�tja a j�t�kot.
	 * @throws IOException Hiba eset�n IOExceptiont dob.
	 */
	public Board() throws IOException{
		
        setBackground(Color.LIGHT_GRAY);
        addMouseListener(this);
        resignButton = new JButton("Resign");
        resignButton.addActionListener(this);
        newGameButton = new JButton("New Game");
        newGameButton.addActionListener(this);
        replayButton = new JButton("Replay");
        replayButton.addActionListener(this);
        message = new JLabel("",JLabel.CENTER);
        message.setFont(new  Font("Serif", Font.BOLD, 14));
        message.setForeground(Color.WHITE);
        board = new int[13][13];
        doNewGame();
		
	}
	
	/**
	 * Reag�l a felhaszn�l� kattint��s�ra a 3 gomb szerint.
	 */
	public void actionPerformed(ActionEvent evt) {
        Object src = evt.getSource();
        if (src == newGameButton)
           doNewGame();
        else if (src == resignButton)
           doResign();
        else if (src == replayButton)
        	doReplay();
     }
	/**
	 * Ez a met�dus eg�r kattint�sra h�v�dik. A mousePressed() met�dus �ltal, amikor a j�t�kos a t�bl�n l�v� n�gyzetekre kattint.
	 * A met�dus h�v�sa csak akkor t�rt�nik meg ha a j�t�k folyamatban van.
	 * A row param�ter a sorokat.
	 * A col param�ter az oszlopokat jel�li.
	 * @param row A row a t�bla sorait jel�li.
	 * @param col A col a t�bla oszlopait jel�li.
	 * @throws IOException Hiba esetln IOExceptiont dob.
	 */
	public void doClickSquare(int row, int col)throws IOException {
         
         /* Megn�zi, hogy egy j�t�kos �res n�gyzetre kattintott-e ha nem akkor error �zenetet �r ki a k�perny�re */
         
         if ( board[row][col] != EMPTY ) {
            if (currentPlayer == BLACK)
               message.setText("BLACK:  Please click an empty square.");
            else
               message.setText("WHITE:  Please click an empty square.");
            return;
         }
         
         /* Elv�gzi a l�p�st.  Megn�zi, hogy a t�bla tele van, �s hogy aki l�pett nyert-e.
          *  Ha igen akkor a j�t�k v�get �r.  Ha nem akkor folyat�dik. */
         
         board[row][col] = currentPlayer; 
         writeMatrix("LastGame.txt", board);// Elv�gzi a l�p�st �s ki�rja egy f�jlba azt.
         repaint();
         
         if (winner(row,col)) {  // El�sz�r megn�zi, hogy nyertes l�p�s volt-e.
            if (currentPlayer == WHITE)
               gameOver("WHITE wins the game!");
            else
               gameOver("BLACK wins the game!");
            return;
         }
         
         boolean emptySpace = false;     // Megn�zi, hogy tele van-e a t�bla
         for (int i = 0; i < 13; i++)
            for (int j = 0; j < 13; j++)
               if (board[i][j] == EMPTY)
                  emptySpace = true;
         if (emptySpace == false) {
            gameOver("The game ends in a draw.");
            return;
         }
         
         /* Folytat�dik a j�t�k. A m�sik j�t�kos l�p */
         
         if (currentPlayer == BLACK) {
            currentPlayer = WHITE;
            message.setText("WHITE:  Make your move.");
         }
         else {  
            currentPlayer = BLACK;
            message.setText("BLACK:  Make your move.");
         }
         
      }
	/**
	 * Ez a met�dus mindig megh�v�dik amikor egy �j b�but raktak le a t�bl�ra.
	 * Ez hat�rozza meg, hogy az a l�p�s egy nyer� l�p�s volt-e vagy sem.
	 * Teh�t 5 azonos b�b� van-e egym�s mellett vagy sem.
	 * Mind a 4 lehets�ges ir�nyb�l megn�zi, hogy van e 5 ugyanolyan b�b� egym�s mellett a n�gyzetekben.
	 * Ha van 5 olyan n�gyzet ahol azonos b�b� van (vagy t�bb) akkor a j�t�k v�get �r �s a jelenlegi j�t�kos nyer.
	 * A row param�ter a sorokat, a col param�ter pedig az oszlopokat jel�li.
	 * @param row A row a t�bla sorait jel�li.
	 * @param col A col a t�bla oszlopait jel�li.
	 * @return Igazzal t�r vissza ha 5 ugyanolyan b�b�t tal�l egym�s mellett, k�l�nben hamissal.
	 */
	
	public boolean winner(int row, int col) {
        
        if (count( board[row][col], row, col, 1, 0 ) >= 5)
           return true;
        if (count( board[row][col], row, col, 0, 1 ) >= 5)
           return true;
        if (count( board[row][col], row, col, 1, -1 ) >= 5)
           return true;
        if (count( board[row][col], row, col, 1, 1 ) >= 5)
           return true;
        

        /*Amikor ehhez a ponthoz �rkez�nk, tudjuk hogy a j�t�kot nem nyerte meg senki.
         * Ez�rt a win_r1 �rt�k�t -1-re �ll�tjuk  ami meg volt v�ltoztatv a count() met�dusban.
         * Ez�rt �jra -1-re kell �ll�tani, hogy elker�lj�k a piros vonalt*/
        
        win_r1 = -1;
        return false;
        
     }  
	
	/**
	 * Megsz�molja az egyik j�t�kos b�b�it. a megadott row col sor� �s oszlop� cell�t�l kezdve,
	 * �s eg�szen v�gig a dirX �s dirY ir�nyba megsz�molja �ket. Azt felt�telezi, hogy a megadott
	 * sorba �s oszlopban van a j�t�kosnak b�b�ja. Ez a met�dus megn�zi a n�gyzeteket row + dirX, col + dirY),
       * (row + 2*dirX, col + 2*dirY), ... �s �gy tov�bb, addig am�g egy olyan n�gyzethez �r ami nincs a t�bl�n vagy foglalt (van rajta m�r b�b�).
       * Teh�t megsz�molja a cell�kat amiken az adott j�t�kosnak vannak b�b�i.
       * Tov�bb�, be�ll�tja a (win_r1, winr_c1) �rt�keket, �gy hogy az utols� poz�ci�j�ra mutasson ahol volt a j�t�kosnak b�b�ja.
       * Ut�na ugyan ez megt�rt�nik, csak a m�sik ir�nyb�l n�zve. Ahol a n�gyzetetek a
       * (row - dirX, col-dirY), (row - 2*dirX, col - 2*dirY), ... �s �gy tov�bb.
       * Ugyan az val�sul meg mint az el�bb azzal a kiv�tellel, hogy most a (win_r2, win_c2) �rt�keket �ll�tja be, �gy, hogy
       * ezek legyenek az utols� b�b� n�gyzet�t meghat�roz� koordin�t�k.
       * dirX �s dirY 0,1 vagy -1 de sohase lehet mindkett� 0;
       * 
	 * @param player a jelenlegi j�t�kos.
	 * @param row A row a t�bla sorait jel�li.
	 * @param col A col a t�bla oszlopait jel�li.
	 * @param dirX x szerinti ir�ny.
	 * @param dirY y szerinti ir�ny.
	 * @return
	 */
	
	private int count(int player, int row, int col, int dirX, int dirY) {
         
         int ct = 1;  // Number of pieces in a row belonging to the player.
         
         int r, c;    // A row and column to be examined
         
         r = row + dirX;  // Look at square in specified direction.
         c = col + dirY;
         while ( r >= 0 && r < 13 && c >= 0 && c < 13 && board[r][c] == player ) {
            // Square is on the board and contains one of the players's pieces.
            ct++;
            r += dirX;  // Go on to next square in this direction.
            c += dirY;
         }
         
         win_r1 = r - dirX;  // The next-to-last square looked at.
         win_c1 = c - dirY;  //    (The LAST one looked at was off the board or
         //    did not contain one of the player's pieces.
         
         r = row - dirX;  // Look in the opposite direction.
         c = col - dirY;
         while ( r >= 0 && r < 13 && c >= 0 && c < 13 && board[r][c] == player ) {
            // Square is on the board and contains one of the players's pieces.
            ct++;
            r -= dirX;   // Go on to next square in this direction.
            c -= dirY;
         }
         
         win_r2 = r + dirX;
         win_c2 = c + dirY;
         
         // At this point, (win_r1,win_c1) and (win_r2,win_c2) mark the endpoints
         // of the line of pieces belonging to the player.
         
         return ct;
         
      }  // end count()
	
	/**
	 * Kirajzolja a t�bl�t �s a b�bukat a t�bl�n. Ha a j�t�kot valaki megynerte,
	 * akkor az 5 b�b�t tartalmaz� n�gyzetet �th�zza egy piros vonallal.
	 */
    public void paintComponent(Graphics g) {
        
        super.paintComponent(g); // A k�perny�t kisz�nezi vil�gos sz�rk�re
        

        /*Rajzol k�t pixel sz�lles fekete hat�rt a sz�lek k�r�l �s s�t�t sz�rk�vel vonalakat rajzol a t�bl�n*/
        
        g.setColor(Color.DARK_GRAY);
        for (int i = 1; i < 13; i++) {
           g.drawLine(1 + 13*i, 0, 1 + 13*i, getSize().height);
           g.drawLine(0, 1 + 13*i, getSize().width, 1 + 13*i);
        }
        g.setColor(Color.BLACK);
        g.drawRect(0,0,getSize().width-1,getSize().height-1);
        g.drawRect(1,1,getSize().width-3,getSize().height-3);
        
        /* Kirajzolja  b�b�kat amik a t�bl�n vannak */
        
        for (int row = 0; row < 13; row++)
           for (int col = 0; col < 13; col++)
              if (board[row][col] != EMPTY)
                 drawPiece(g, board[row][col], row, col);
        

        /*Ha a j�t�kot megnyerte valaki, akkor win_r1>=0.
         * rajzol egy vonalat hogy megmutassa az 5 (vagy t�bb) nyer� b�b�t*/
        
        if (win_r1 >= 0)
           drawWinLine(g);
        
     }
    /**
	 *Berajzol egy b�b�t a row sor� �s col oszlop� n�gyzetbe. A b�b� sz�ne a piece param�ter �ltal van meghat�rozva,
	 *ami lehet fekte (BLACK) vagy feh�r (WHITE).
     * 
     * @param g Graphics.
     * @param piece b�b�.
     * @param row A row a t�bla sorait jel�li.
     * @param col A col a t�bla oszlopait jel�li.
     */
    private void drawPiece(Graphics g, int piece, int row, int col) {
        if (piece == WHITE)
           g.setColor(Color.WHITE);
        else
           g.setColor(Color.BLACK);
        g.fillOval(3 + 13*col, 3 + 13*row, 10, 10);
     }
    /**
     * 2 pixel sz�les piros vonalat rajzol a n�gyzeteket keresztbe �th�zva a (win_r1,win_c1)t�l a (win_r2, win_c2)ig.
     * Ez akkor h�vod meg amikor valamelyik j�t�kos megnyerte a j�t�kot. Teh�t kirakta az 5 azonos b�b�t egym�s mell�.
     * A v�ltoz�k �rt�kei a count() met�dusban ker�lnek be�ll�t�sra.
     * @param g A g a Graphics oszt�ly objektuma.
     */
    public void drawWinLine(Graphics g) {
        g.setColor(Color.RED);
        g.drawLine( 8 + 13*win_c1, 8 + 13*win_r1, 8 + 13*win_c2, 8 + 13*win_r2 );
        if (win_r1 == win_r2)
           g.drawLine( 8 + 13*win_c1, 7 + 13*win_r1, 8 + 13*win_c2, 7 + 13*win_r2 );
        else
           g.drawLine( 7 + 13*win_c1, 8 + 13*win_r1, 7 + 13*win_c2, 8 + 13*win_r2 );
     }
    /**
     * Reag�l az felhaszn�l� �ltali eg�rkattint�sra. Ha nincs j�t�k folyamatban akkor error �zenetet mutat.
     * Egy�bk�nt megadja, hogy a felhaszn�l� melyik sorba �s oszlopba kattintott �s megh�vja a doClickSquare() met�dust, ami kezeli ezt.
     */
    public void mousePressed(MouseEvent evt) {
        if (gameInProgress == false)
           message.setText("Click \"New Game\" to start a new game.");
        else {
           int col = (evt.getX() - 2) / 13;
           int row = (evt.getY() - 2) / 13;
        if(gameState == 0) {
           if (col >= 0 && col < 13 && row >= 0 && row < 13)
			try {
				doClickSquare(row,col);
			} catch (IOException e) {
				
				e.printStackTrace();
			}
          }

        }
     }
    

     
     
     public void mouseReleased(MouseEvent evt) { }
     public void mouseClicked(MouseEvent evt) { }
     public void mouseEntered(MouseEvent evt) { }
     public void mouseExited(MouseEvent evt) { }
	
	
}
