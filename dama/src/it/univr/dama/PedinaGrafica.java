package it.univr.dama;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

public class PedinaGrafica extends JPanel{
	
	private final Pedina p;
	private final Scacchiera s;
	
	//costruttore di pedina grafica che si salva la pedina e la scacchiera passategli
	public PedinaGrafica( final Pedina p, final Scacchiera s){
		this.p = p;
		this.s = s;
		
	}
	
	//metodo paint della pedina
	public void paintPedina(Graphics g){
		
		Graphics2D g2D = (Graphics2D)g;
		g2D.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		//setta il colore alla pedina grafica
		g2D.setColor(p.getColore());
		//crea un ovale pieno (in questo caso un cerchio) 
		g2D.fillOval((s.getWidth()/8*p.getX())+4,(s.getHeight()/8*p.getY())+4, (s.getWidth()/8)-8, (s.getHeight()/8)-8);
		
		if(p.getColore() == Color.WHITE)
			//setta il colore al bordo della pedina grafica
			g2D.setColor(Color.BLACK);
		else
			//setta il colore al bordo della pedina grafica
			g2D.setColor(Color.WHITE);
			
		//crea il bordo di un ovale (in questo caso un cerchio) 
		g2D.drawOval((s.getWidth()/8*p.getX())+4,(s.getHeight()/8*p.getY())+4, (s.getWidth()/8)-8, (s.getHeight()/8)-8);
		g2D.drawOval(1+(s.getWidth()/8*p.getX())+4, 1+(s.getHeight()/8*p.getY())+4, (s.getWidth()/8)-10, (s.getHeight()/8)-10);
		g2D.drawOval(2+(s.getWidth()/8*p.getX())+4, 2+(s.getHeight()/8*p.getY())+4, (s.getWidth()/8)-12, (s.getHeight()/8)-12);
		g2D.drawOval(16+(s.getWidth()/8*p.getX())+4, 16+(s.getHeight()/8*p.getY())+4, (s.getWidth()/8)-40, (s.getHeight()/8)-40);
	}
	
	//metodo che ritorna la pedina grafica all'esterno
	public Pedina getPedina(){
		return this.p;
	}
	
}
