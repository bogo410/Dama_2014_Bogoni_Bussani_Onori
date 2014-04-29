package it.univr.dama;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;


public class DamaGrafica extends PedinaGrafica{
		private Dama d;
		private Scacchiera s;
		
	//costruttore di pedina grafica che si salva la pedina e la scacchiera passategli
	public DamaGrafica( final Dama d, final Scacchiera s){
		
		super(d, s);
		this.d = d;
		this.s = s;
		
	}
	
	//metodo paint della dama
	public void paintPedina(Graphics g){
		
		Graphics2D g2D = (Graphics2D)g;
		g2D.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		//setta il colore alla pdama grafica
		g2D.setColor(d.getColore());
		//crea un ovale pieno (in questo caso un cerchio) 
		g2D.fillOval((s.getWidth()/8*d.getX())+4,(s.getHeight()/8*d.getY())+4, (s.getWidth()/8)-8, (s.getHeight()/8)-8);
		
		if(d.getColore() == Color.WHITE)
			//setta il colore al bordo della dama grafica
			g2D.setColor(Color.BLUE);
		else
			//setta il colore al bordo della dama grafica
			g2D.setColor(Color.MAGENTA);
			
		//crea il bordo di un ovale (in questo caso un cerchio) 
		g2D.drawOval((s.getWidth()/8*d.getX())+4,(s.getHeight()/8*d.getY())+4, (s.getWidth()/8)-8, (s.getHeight()/8)-8);
		g2D.drawOval(1+(s.getWidth()/8*d.getX())+4, 1+(s.getHeight()/8*d.getY())+4, (s.getWidth()/8)-10, (s.getHeight()/8)-10);
		g2D.drawOval(2+(s.getWidth()/8*d.getX())+4, 2+(s.getHeight()/8*d.getY())+4, (s.getWidth()/8)-12, (s.getHeight()/8)-12);
	}
	
	//metodo che ritorna la dama grafica all'esterno
	public Dama getDama(){
		return this.d;
	}
	
}
