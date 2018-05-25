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
 * Ebben az osztályban a játékkal kapcsolatos metódusok kerültem mint például, új játék létrehozása, játék feladása,
 * játék visszajátszása, játék vége állapot, illetve a visszajátszásnál használt fájlkezelés is itt valósul meg.
 * Létrehozza a táblát a doNewGame metódusban.
 * @author Roland
 *
 */

public class Game extends JPanel{
	public static JButton newGameButton; // új játékot hoz létre ez a gomb
	public static JButton resignButton; //Egy játékos fel tudja adni a mérkõzést ezzel a gombbal
	public static JButton replayButton; // A játék visszajátszására szolgáló gomb
	public static JLabel message; // Label a játékosokhoz szóló üzenetek megjelenítésére
	public BufferedWriter bw;
	public BufferedReader br;
	
	public Game ()throws IOException {
		bw = new BufferedWriter(new FileWriter("LastGame.txt"));
		br = new BufferedReader(new FileReader("LastGame.txt"));
		
	}

	
	int[][] board;  //A tábla adatai itt tárolódnak, és az elemeik a következõ konstansokból kerülenk ki.
	final int EMPTY = 0, // Empty jelöli az üres négyzetrácsot a táblán
			WHITE = 1,	// White jelöli azt a négyzetrácsot ahova a fehér játékos rakott bábut.
			BLACK = 2; // Black jelöli azt a négyzetrácsot ahova a fekete játékos rakott bábut.
	
	boolean gameInProgress; //Figyeli, hogy a játék folyamatban van-e
	static int gameState = 0;	//Figyeli, hogy a játék melyik állpotban van. Playing, Resigning, Replaying.
	int currentPlayer;	// Figyeli, hogy ki a jelenlegi játékos aki lépni fog. Black or white.
	int win_r1, win_c1, win_r2, win_c2; // Amikor egy játékos nyer azzal, hogy 5 ugyanolyan bábut rak egymás mellé. akkor az az elsõ és utolsó négyzetben lévõ bábuk a következõk:(win_r1,win_c1) és (win_r2,win_c2).
										//Ezeket a négyzeteket egy piros vonallal kötöm össze. Amikor nincs kirakva 5 bábu egymás mellett, akkor win_r1 értékét -1-re állítom.
										//a count() metódusban állítom be ezeket az értékekt. A paintComponent() metódusban ellenörzi a program az értéket.
	/**
	 * Elkezdõdik egy új játék, Ez az actionPerformed() metódusban hívódik meg,
	 * amikor a felhasználó az új játék gombra kattint valamint indításkor a Board konstruktorában.
	 */
    public void doNewGame() {
    	gameState = 0;
    	if (gameInProgress == true) {
              // Ennek nem szabadna megtörténnie mert a New Game gomb csak
              //  akkor mûködik amikor mûködnie kell.
              // De azért nem árt ellenõrizni.
           message.setText("Finish the current game first!");
           return;
        }
        for (int row = 0; row < 13; row++)       // Feltölti a táblát EMPTY-kel.
           for (int col = 0; col < 13; col++)
              board[row][col] = EMPTY;
        currentPlayer = BLACK;   // BLACK lép elõszõr.
        message.setText("BLACK:  Make your move.");
        gameInProgress = true;
        newGameButton.setEnabled(false);
        replayButton.setEnabled(false);
        resignButton.setEnabled(true);
        win_r1 = -1;  // Ez az érték jelzi, hogy ne legyen piros vonal a négyzetek között, csak ha ki van rakva az 5 bábú egymás mellé.
        repaint();
     }
	/**
	 * A jelenlegi játékos ( current player) feladja a játékot.
	 * Ez a metódus az actionPerformed() metódusban kerül meghívásra, amikor a felhasználó rákattint a Resign button-ra.
	 * A játék véget ér és az ellenfél nyer.
	 */
	public void doResign() {
		gameState = 2;
        if (gameInProgress == false) {
               // Ennek nem kéne megtörténnie
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
	 * A játék visszajátszásáért felelõs függvény.
	 * Ez a metódus az actionPerformed() metódusban kerül meghívásra, amikor a felhasználó rákattint a Replay button-ra.
	 * Az elõzõ játékot/játékokat lépésrõl lépésre vissza lehet játszani a replay gomb lenyomásával. Egy gomb nyomás egy lépést játszik vissza.
	 */
	public void doReplay() {
		gameState = 1;
		if(gameInProgress == true) {
				// Ennek nem kéne megtörténnie
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
			win_r1 = -1;  // Ez az érték jelzi, hogy ne legyen piros vonal a négyzetek között, csak ha ki van rakva az 5 bábú egymás mellé..
			repaint();
			
			}
	
		/**
		 * Ez a metódus akkor hívódik meg amikor a játék véget ér. Az str paraméter a megjelenítendõ szöveg.
		 * A gombok be és kikapcsolódnak, hogy látszódjon a játék már nincs folyamatban.
		 * @param str A megjelenítendõ szöveg.
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
	 * A tábla vagyis a mátrix fájlba való írása itt történik, az a visszajátszás miatt szükséges.
	 * A metódus a doClickSquare() metódusban hívódik meg. Tehát minden lépés után kiírja egy fájlba a jelenlegi állást, amit majd késõbb vissza tudunk olvasni, a visszajátszáshoz.
	 *  
	 * @param filename A filename paraméter a fájl nevét jelenti amibe írni fogunk.
	 * @param matrix A matrix paraméter pedig a táblát amit lementünk a fájlba.
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
 * Az elõzõ játékok fájlból való beolvasása történik a metódusban. 
 * Ez a metódus a doReplay() metódusban hívódik meg. Amikor vissza szeretnénk játszani egy játékot a Replay gomb megnyomásával egyesével minden lépést beolvas. 
 * @param matrix   A matrix paraméterhez a táblát kell megadni amibe beolvassuk a lépést.
 * @param line A line paraméterben egy beolvasott sort adunk meg amit beírunk a mátrix i. sorába.
 * @param i A line paraméterben egy beolvasott sort adunk meg amit beírunk a mátrix i. sorába.
 * @throws FileNotFoundException Ha nem találja a fájlt akkor FileNotFountExceptiont dob.
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

