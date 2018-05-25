package FiveInARow;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.*;

/**
 * Ezzel a programmal két játékos játszhat amõba játékot egymás ellen.
 * Mindig a fekete játékos fogja kezdeni a játékot. Amikor egy játékos ki rak 5 bábut egymás mellé a sajátjai közül, akkor nyer.
 * A játék döntetlennel ér véget ha a tábla megtelik azelõtt, hogy valamelyik játékos nyer.
 * 
 * Az osztálynak van egy main() függvénye, amely segítségével tud a program önállóan futni.
 * A program megnyit egy ablakot (window) ami a FiveInARow objektumot (object) használja.
 * 
 * @author Roland
 *
 */
public class FiveInARow extends Game{
	ArrayList<Integer> list;

/**
 * A main függvény teszi lehetõvé, hogy a program önállóan tudjon futni.
 * Megnyit egy ablakot amin látható az amõba tábla, a program bezáródik amikor a felhasználó bezárja azt.
 * 
 * @param args sztringek tömbje
 * @throws IOException hiba eseten IOExceptiont dob.
 */
	
	public static void main(String[] args)throws IOException {
	
	JFrame window = new JFrame("Five in a row");
	FiveInARow content = new FiveInARow();
	window.setContentPane(content);
	window.pack();
	Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
	window.setLocation( (screensize.width - window.getWidth())/2,
            (screensize.height - window.getHeight())/2 );
	window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	window.setResizable(false);
	window.setVisible(true);
	
	}

/**
 * A konstruktorban kerül megtervezésre a panel. A játék fõrésze a Game és a Board osztályban fut le.	
 * A méretek és a pozíciók beállítása történik itt és a layout null-ra állítása.
 * @throws IOException Hiba esetén IOExceptiont dob.
 */
	
	public FiveInARow() throws IOException{
		setLayout(null); //saját layoutot készítek
		setPreferredSize(new Dimension(350,250));
		setBackground(new Color(0,0,0)); // fekete háttér
		
		/*A komponensek létrehozása és hozzáadása a panelhez*/
		
		Board board = new Board(); //a Board konstruktora hozza létre a gombokat
		
		add(board);
		add(newGameButton);
		add(replayButton);
		add(resignButton);
		add(message);
		
		/*A méret és a pozíció beállítása minden komponensre a setBounds() metódus által*/
		
		board.setBounds(16,16,172,172);
		newGameButton.setBounds(210,  30,  120,  30);
		replayButton.setBounds(210, 90, 120, 30);
		resignButton.setBounds(210, 150, 120, 30);
		message.setBounds(0, 200, 350,30);
		list = new ArrayList<>();
				
	}
    public void list() {
    	for(int i = 0; i<13; ++i) {
    		list.add(i);
    	}
    }
	
	
}