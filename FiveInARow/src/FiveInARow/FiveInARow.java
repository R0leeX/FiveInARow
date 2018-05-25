package FiveInARow;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.*;

/**
 * Ezzel a programmal k�t j�t�kos j�tszhat am�ba j�t�kot egym�s ellen.
 * Mindig a fekete j�t�kos fogja kezdeni a j�t�kot. Amikor egy j�t�kos ki rak 5 b�but egym�s mell� a saj�tjai k�z�l, akkor nyer.
 * A j�t�k d�ntetlennel �r v�get ha a t�bla megtelik azel�tt, hogy valamelyik j�t�kos nyer.
 * 
 * Az oszt�lynak van egy main() f�ggv�nye, amely seg�ts�g�vel tud a program �n�ll�an futni.
 * A program megnyit egy ablakot (window) ami a FiveInARow objektumot (object) haszn�lja.
 * 
 * @author Roland
 *
 */
public class FiveInARow extends Game{
	ArrayList<Integer> list;

/**
 * A main f�ggv�ny teszi lehet�v�, hogy a program �n�ll�an tudjon futni.
 * Megnyit egy ablakot amin l�that� az am�ba t�bla, a program bez�r�dik amikor a felhaszn�l� bez�rja azt.
 * 
 * @param args sztringek t�mbje
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
 * A konstruktorban ker�l megtervez�sre a panel. A j�t�k f�r�sze a Game �s a Board oszt�lyban fut le.	
 * A m�retek �s a poz�ci�k be�ll�t�sa t�rt�nik itt �s a layout null-ra �ll�t�sa.
 * @throws IOException Hiba eset�n IOExceptiont dob.
 */
	
	public FiveInARow() throws IOException{
		setLayout(null); //saj�t layoutot k�sz�tek
		setPreferredSize(new Dimension(350,250));
		setBackground(new Color(0,0,0)); // fekete h�tt�r
		
		/*A komponensek l�trehoz�sa �s hozz�ad�sa a panelhez*/
		
		Board board = new Board(); //a Board konstruktora hozza l�tre a gombokat
		
		add(board);
		add(newGameButton);
		add(replayButton);
		add(resignButton);
		add(message);
		
		/*A m�ret �s a poz�ci� be�ll�t�sa minden komponensre a setBounds() met�dus �ltal*/
		
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