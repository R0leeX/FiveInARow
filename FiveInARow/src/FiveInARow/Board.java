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
 * Megjeleníti a táblát ami 168*168 pixel méretû négyzetrácsos tábla, 2pixel méretû választékkal.
 * Azt feltételezve, hogy az ablak 172*172 pixel méretû. Ez az osztály végzi a fõ munkát amikor a játékosok játszanak és megjeleníti a táblát.
 * Ebben a programban a tábla amin a játék zajlik 13 sorból és 13 oszlopból álló négyzetrács.
 * 
 * @author Roland
 *
 */

public class Board extends Game implements ActionListener, MouseListener{
		
	/**
	 * Konstruktor. Létrehozza a gombokat és a címkét. Figyeli az egér kattintást és a gombokra kattintást. Elindítja a játékot.
	 * @throws IOException Hiba esetén IOExceptiont dob.
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
	 * Reagál a felhasználó kattintáására a 3 gomb szerint.
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
	 * Ez a metódus egér kattintásra hívódik. A mousePressed() metódus által, amikor a játékos a táblán lévõ négyzetekre kattint.
	 * A metódus hívása csak akkor történik meg ha a játék folyamatban van.
	 * A row paraméter a sorokat.
	 * A col paraméter az oszlopokat jelöli.
	 * @param row A row a tábla sorait jelöli.
	 * @param col A col a tábla oszlopait jelöli.
	 * @throws IOException Hiba esetln IOExceptiont dob.
	 */
	public void doClickSquare(int row, int col)throws IOException {
         
         /* Megnézi, hogy egy játékos üres négyzetre kattintott-e ha nem akkor error üzenetet ír ki a képernyõre */
         
         if ( board[row][col] != EMPTY ) {
            if (currentPlayer == BLACK)
               message.setText("BLACK:  Please click an empty square.");
            else
               message.setText("WHITE:  Please click an empty square.");
            return;
         }
         
         /* Elvégzi a lépést.  Megnézi, hogy a tábla tele van, és hogy aki lépett nyert-e.
          *  Ha igen akkor a játék véget ér.  Ha nem akkor folyatódik. */
         
         board[row][col] = currentPlayer; 
         writeMatrix("LastGame.txt", board);// Elvégzi a lépést és kiírja egy fájlba azt.
         repaint();
         
         if (winner(row,col)) {  // Elõszõr megnézi, hogy nyertes lépés volt-e.
            if (currentPlayer == WHITE)
               gameOver("WHITE wins the game!");
            else
               gameOver("BLACK wins the game!");
            return;
         }
         
         boolean emptySpace = false;     // Megnézi, hogy tele van-e a tábla
         for (int i = 0; i < 13; i++)
            for (int j = 0; j < 13; j++)
               if (board[i][j] == EMPTY)
                  emptySpace = true;
         if (emptySpace == false) {
            gameOver("The game ends in a draw.");
            return;
         }
         
         /* Folytatódik a játék. A másik játékos lép */
         
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
	 * Ez a metódus mindig meghívódik amikor egy új bábut raktak le a táblára.
	 * Ez határozza meg, hogy az a lépés egy nyerõ lépés volt-e vagy sem.
	 * Tehát 5 azonos bábú van-e egymás mellett vagy sem.
	 * Mind a 4 lehetséges irányból megnézi, hogy van e 5 ugyanolyan bábú egymás mellett a négyzetekben.
	 * Ha van 5 olyan négyzet ahol azonos bábú van (vagy több) akkor a játék véget ér és a jelenlegi játékos nyer.
	 * A row paraméter a sorokat, a col paraméter pedig az oszlopokat jelöli.
	 * @param row A row a tábla sorait jelöli.
	 * @param col A col a tábla oszlopait jelöli.
	 * @return Igazzal tér vissza ha 5 ugyanolyan bábút talál egymás mellett, különben hamissal.
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
        

        /*Amikor ehhez a ponthoz érkezünk, tudjuk hogy a játékot nem nyerte meg senki.
         * Ezért a win_r1 értékét -1-re állítjuk  ami meg volt változtatv a count() metódusban.
         * Ezért újra -1-re kell állítani, hogy elkerüljük a piros vonalt*/
        
        win_r1 = -1;
        return false;
        
     }  
	
	/**
	 * Megszámolja az egyik játékos bábúit. a megadott row col sorú és oszlopú cellától kezdve,
	 * és egészen végig a dirX és dirY irányba megszámolja õket. Azt feltételezi, hogy a megadott
	 * sorba és oszlopban van a játékosnak bábúja. Ez a metódus megnézi a négyzeteket row + dirX, col + dirY),
       * (row + 2*dirX, col + 2*dirY), ... és így tovább, addig amíg egy olyan négyzethez ér ami nincs a táblán vagy foglalt (van rajta már bábú).
       * Tehát megszámolja a cellákat amiken az adott játékosnak vannak bábúi.
       * Továbbá, beállítja a (win_r1, winr_c1) értékeket, úgy hogy az utolsó pozíciójára mutasson ahol volt a játékosnak bábúja.
       * Utána ugyan ez megtörténik, csak a másik irányból nézve. Ahol a négyzetetek a
       * (row - dirX, col-dirY), (row - 2*dirX, col - 2*dirY), ... és így tovább.
       * Ugyan az valósul meg mint az elõbb azzal a kivétellel, hogy most a (win_r2, win_c2) értékeket állítja be, úgy, hogy
       * ezek legyenek az utolsó bábú négyzetét meghatározó koordináták.
       * dirX és dirY 0,1 vagy -1 de sohase lehet mindkettõ 0;
       * 
	 * @param player a jelenlegi játékos.
	 * @param row A row a tábla sorait jelöli.
	 * @param col A col a tábla oszlopait jelöli.
	 * @param dirX x szerinti irány.
	 * @param dirY y szerinti irány.
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
	 * Kirajzolja a táblát és a bábukat a táblán. Ha a játékot valaki megynerte,
	 * akkor az 5 bábút tartalmazó négyzetet áthúzza egy piros vonallal.
	 */
    public void paintComponent(Graphics g) {
        
        super.paintComponent(g); // A képernyõt kiszínezi világos szürkére
        

        /*Rajzol két pixel szélles fekete határt a szélek körül és sötét szürkével vonalakat rajzol a táblán*/
        
        g.setColor(Color.DARK_GRAY);
        for (int i = 1; i < 13; i++) {
           g.drawLine(1 + 13*i, 0, 1 + 13*i, getSize().height);
           g.drawLine(0, 1 + 13*i, getSize().width, 1 + 13*i);
        }
        g.setColor(Color.BLACK);
        g.drawRect(0,0,getSize().width-1,getSize().height-1);
        g.drawRect(1,1,getSize().width-3,getSize().height-3);
        
        /* Kirajzolja  bábúkat amik a táblán vannak */
        
        for (int row = 0; row < 13; row++)
           for (int col = 0; col < 13; col++)
              if (board[row][col] != EMPTY)
                 drawPiece(g, board[row][col], row, col);
        

        /*Ha a játékot megnyerte valaki, akkor win_r1>=0.
         * rajzol egy vonalat hogy megmutassa az 5 (vagy több) nyerõ bábút*/
        
        if (win_r1 >= 0)
           drawWinLine(g);
        
     }
    /**
	 *Berajzol egy bábút a row sorú és col oszlopú négyzetbe. A bábú színe a piece paraméter által van meghatározva,
	 *ami lehet fekte (BLACK) vagy fehér (WHITE).
     * 
     * @param g Graphics.
     * @param piece bábú.
     * @param row A row a tábla sorait jelöli.
     * @param col A col a tábla oszlopait jelöli.
     */
    private void drawPiece(Graphics g, int piece, int row, int col) {
        if (piece == WHITE)
           g.setColor(Color.WHITE);
        else
           g.setColor(Color.BLACK);
        g.fillOval(3 + 13*col, 3 + 13*row, 10, 10);
     }
    /**
     * 2 pixel széles piros vonalat rajzol a négyzeteket keresztbe áthúzva a (win_r1,win_c1)tõl a (win_r2, win_c2)ig.
     * Ez akkor hívod meg amikor valamelyik játékos megnyerte a játékot. Tehát kirakta az 5 azonos bábút egymás mellé.
     * A változók értékei a count() metódusban kerülnek beállításra.
     * @param g A g a Graphics osztály objektuma.
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
     * Reagál az felhasználó általi egérkattintásra. Ha nincs játék folyamatban akkor error üzenetet mutat.
     * Egyébként megadja, hogy a felhasználó melyik sorba és oszlopba kattintott és meghívja a doClickSquare() metódust, ami kezeli ezt.
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
