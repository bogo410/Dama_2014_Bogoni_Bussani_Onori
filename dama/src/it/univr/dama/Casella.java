package it.univr.dama;
 
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;

public class Casella extends JPanel{

	private Color colore;
	private final Color coloreBase;
	private final Scacchiera s;

	private int x, y;
	
	//costruttore Casella 
	public Casella( Color colore, Scacchiera s, int x, int y){
		
		this.colore = colore;
		this.coloreBase = colore;
		this.s = s;
		this.x = x;
		this.y = y;
		
	}
	
	//metodo paint della casella
	public void paintCasella(Graphics g){
		g.setColor(colore);
		//disegna un rettangolo(in questo caso un quadrato)
		g.fillRect(s.getWidth()/8*x,s.getHeight()/8*y, s.getWidth()/8, s.getHeight()/8);
			
	}

	public void resetColor(){
		setColor(coloreBase);
	}

	public Color getColor(){
		return colore;
	}
	
	public void setColor(Color colore){
			this.colore = colore;
		repaint();
	}
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	
}
