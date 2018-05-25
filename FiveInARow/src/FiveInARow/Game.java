package FiveInARow;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Ebben az oszt�lyban a j�t�kkal kapcsolatos met�dusok ker�ltem mint p�ld�ul, �j j�t�k l�trehoz�sa, j�t�k felad�sa,
 * j�t�k visszaj�tsz�sa, j�t�k v�ge �llapot, illetve a visszaj�tsz�sn�l haszn�lt f�jlkezel�s is itt val�sul meg.
 * L�trehozza a t�bl�t a doNewGame met�dusban.
 * @author Roland
 *
 */

public class Game extends JPanel{
	public static JButton newGameButton; // �j j�t�kot hoz l�tre ez a gomb
	public static JButton resignButton; //Egy j�t�kos fel tudja adni a m�rk�z�st ezzel a gombbal
	public static JButton replayButton; // A j�t�k visszaj�tsz�s�ra szolg�l� gomb
	public static JLabel message; // Label a j�t�kosokhoz sz�l� �zenetek megjelen�t�s�re
	public BufferedWriter bw;
	public BufferedReader br;
	
	public Game ()throws IOException {
		bw = new BufferedWriter(new FileWriter("LastGame.txt"));
		br = new BufferedReader(new FileReader("LastGame.txt"));
		
	}

	
	int[][] board;  //A t�bla adatai itt t�rol�dnak, �s az elemeik a k�vetkez� konstansokb�l ker�lenk ki.
	final int EMPTY = 0, // Empty jel�li az �res n�gyzetr�csot a t�bl�n
			WHITE = 1,	// White jel�li azt a n�gyzetr�csot ahova a feh�r j�t�kos rakott b�but.
			BLACK = 2; // Black jel�li azt a n�gyzetr�csot ahova a fekete j�t�kos rakott b�but.
	
	boolean gameInProgress; //Figyeli, hogy a j�t�k folyamatban van-e
	static int gameState = 0;	//Figyeli, hogy a j�t�k melyik �llpotban van. Playing, Resigning, Replaying.
	int currentPlayer;	// Figyeli, hogy ki a jelenlegi j�t�kos aki l�pni fog. Black or white.
	int win_r1, win_c1, win_r2, win_c2; // Amikor egy j�t�kos nyer azzal, hogy 5 ugyanolyan b�but rak egym�s mell�. akkor az az els� �s utols� n�gyzetben l�v� b�buk a k�vetkez�k:(win_r1,win_c1) �s (win_r2,win_c2).
										//Ezeket a n�gyzeteket egy piros vonallal k�t�m �ssze. Amikor nincs kirakva 5 b�bu egym�s mellett, akkor win_r1 �rt�k�t -1-re �ll�tom.
										//a count() met�dusban �ll�tom be ezeket az �rt�kekt. A paintComponent() met�dusban ellen�rzi a program az �rt�ket.
	/**
	 * Elkezd�dik egy �j j�t�k, Ez az actionPerformed() met�dusban h�v�dik meg,
	 * amikor a felhaszn�l� az �j j�t�k gombra kattint valamint ind�t�skor a Board konstruktor�ban.
	 */
    public void doNewGame() {
    	gameState = 0;
    	if (gameInProgress == true) {
              // Ennek nem szabadna megt�rt�nnie mert a New Game gomb csak
              //  akkor m�k�dik amikor m�k�dnie kell.
              // De az�rt nem �rt ellen�rizni.
           message.setText("Finish the current game first!");
           return;
        }
        for (int row = 0; row < 13; row++)       // Felt�lti a t�bl�t EMPTY-kel.
           for (int col = 0; col < 13; col++)
              board[row][col] = EMPTY;
        currentPlayer = BLACK;   // BLACK l�p el�sz�r.
        message.setText("BLACK:  Make your move.");
        gameInProgress = true;
        newGameButton.setEnabled(false);
        replayButton.setEnabled(false);
        resignButton.setEnabled(true);
        win_r1 = -1;  // Ez az �rt�k jelzi, hogy ne legyen piros vonal a n�gyzetek k�z�tt, csak ha ki van rakva az 5 b�b� egym�s mell�.
        repaint();
     }
	/**
	 * A jelenlegi j�t�kos ( current player) feladja a j�t�kot.
	 * Ez a met�dus az actionPerformed() met�dusban ker�l megh�v�sra, amikor a felhaszn�l� r�kattint a Resign button-ra.
	 * A j�t�k v�get �r �s az ellenf�l nyer.
	 */
	public void doResign() {
		gameState = 2;
        if (gameInProgress == false) {
               // Ennek nem k�ne megt�rt�nnie
           message.setText("There is no game in progress!");
           return;
        }
        if (currentPlayer == WHITE)
           message.setText("WHITE resigns.  BLACK wins.");
        else
           message.setText("BLACK resigns.  WHITE wins.");
        newGameButton.setEnabled(true);
        replayButton.setEnabled(true);
        resignButton.setEnabled(false);
        gameInProgress = false;
     }
	/**
	 * A j�t�k visszaj�tsz�s��rt felel�s f�ggv�ny.
	 * Ez a met�dus az actionPerformed() met�dusban ker�l megh�v�sra, amikor a felhaszn�l� r�kattint a Replay button-ra.
	 * Az el�z� j�t�kot/j�t�kokat l�p�sr�l l�p�sre vissza lehet j�tszani a replay gomb lenyom�s�val. Egy gomb nyom�s egy l�p�st j�tszik vissza.
	 */
	public void doReplay() {
		gameState = 1;
		if(gameInProgress == true) {
				// Ennek nem k�ne megt�rt�nnie
			message.setText("Finish the current game first!");
		return;
		}
	
		String line;
		int i = 0;
			try {
				while(i<13)
				if((line = br.readLine())!= null) {
				readints(board, line, i);
				i++;
				}
				else { 
					break;
					}	 
			} catch (FileNotFoundException e) {
				
				e.printStackTrace();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
			gameInProgress = false;
			newGameButton.setEnabled(true);
			replayButton.setEnabled(true);
			resignButton.setEnabled(false);
			win_r1 = -1;  // Ez az �rt�k jelzi, hogy ne legyen piros vonal a n�gyzetek k�z�tt, csak ha ki van rakva az 5 b�b� egym�s mell�..
			repaint();
			
			}
	
		/**
		 * Ez a met�dus akkor h�v�dik meg amikor a j�t�k v�get �r. Az str param�ter a megjelen�tend� sz�veg.
		 * A gombok be �s kikapcsol�dnak, hogy l�tsz�djon a j�t�k m�r nincs folyamatban.
		 * @param str A megjelen�tend� sz�veg.
		 */
	
	public void gameOver(String str) {
		try {
			bw.flush();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		message.setText(str);
        newGameButton.setEnabled(true);
        replayButton.setEnabled(true);
        resignButton.setEnabled(false);
        gameInProgress = false;
        
     }
	/**
	 * A t�bla vagyis a m�trix f�jlba val� �r�sa itt t�rt�nik, az a visszaj�tsz�s miatt sz�ks�ges.
	 * A met�dus a doClickSquare() met�dusban h�v�dik meg. Teh�t minden l�p�s ut�n ki�rja egy f�jlba a jelenlegi �ll�st, amit majd k�s�bb vissza tudunk olvasni, a visszaj�tsz�shoz.
	 *  
	 * @param filename A filename param�ter a f�jl nev�t jelenti amibe �rni fogunk.
	 * @param matrix A matrix param�ter pedig a t�bl�t amit lement�nk a f�jlba.
	 */
    public void writeMatrix(String filename, int[][] matrix) {
        try {
        	
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix[i].length; j++) {
                	 bw.write(matrix[i][j] + ((j == matrix[i].length-1) ? "" : " "));
                }
                bw.newLine();
            }
            
        } catch (IOException e) {}
    }
    
/**
 * Az el�z� j�t�kok f�jlb�l val� beolvas�sa t�rt�nik a met�dusban. 
 * Ez a met�dus a doReplay() met�dusban h�v�dik meg. Amikor vissza szeretn�nk j�tszani egy j�t�kot a Replay gomb megnyom�s�val egyes�vel minden l�p�st beolvas. 
 * @param matrix   A matrix param�terhez a t�bl�t kell megadni amibe beolvassuk a l�p�st.
 * @param line A line param�terben egy beolvasott sort adunk meg amit be�runk a m�trix i. sor�ba.
 * @param i A line param�terben egy beolvasott sort adunk meg amit be�runk a m�trix i. sor�ba.
 * @throws FileNotFoundException Ha nem tal�lja a f�jlt akkor FileNotFountExceptiont dob.
 */
    public static void readints(int[][] matrix, String line, int i) throws FileNotFoundException {
		BufferedReader br = new BufferedReader(new FileReader("LastGame.txt"));
		int j = 0;
		
			Scanner sc = new Scanner(line);
			while(sc.hasNextInt()) {
				int a = sc.nextInt();
				matrix[i][j++] = a;
				}
			}
    

		
		
		
	}

