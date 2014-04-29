package it.univr.dama;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import javax.swing.JPanel;
import java.awt.Color;
import java.util.ArrayList;

public class Scacchiera extends JPanel{

	private final Casella[][] caselle = new Casella[8][8];
	private ArrayList<PedinaGrafica> pedineGrafiche;
	public final Color coloreChiaro = new Color(240, 240, 215);
	private final Color coloreScuro = new Color(70, 60, 48);
	
	//toolkit di default per recuperare immagini
	Toolkit tk = Toolkit.getDefaultToolkit(); 
	//oggetto dedicato ad occuparsi del caricamento dell'immagine
	MediaTracker mt = new MediaTracker(this); 
	Image immagine = null; 
	Image immagineInfo = tk.getImage("img/info.png");
	Image immagineIstru = tk.getImage("img/istruzioni.png");
	
	//Costruttore Scacchiera
	public Scacchiera(){
		
		setPedineGrafiche(new ArrayList<PedinaGrafica>());

		//inizializzo le caselle
		for(int i = 0; i<8; i++){
			
			//se il numero di riga  pari
			if(i%2==0)
				for(int j = 0; j<4; j++){
					getCaselle()[i][j*2] = new Casella(coloreScuro, this, i, j*2);
					getCaselle()[i][j*2+1] = new Casella(coloreChiaro, this, i, j*2+1);
				}
			
			//se il numero di riga  dispari
			else
				for(int j = 0; j<4; j++){
					getCaselle()[i][j*2] = new Casella(coloreChiaro, this, i, j*2);
					getCaselle()[i][j*2+1] = new Casella(coloreScuro, this, i, j*2+1);
				}
		}
		
		//recupera l'immagine iniziale
		immagine = tk.getImage("img/inizio.jpg"); 
		mt.addImage(immagine,1);
		// aggiunta di eventuali altre immagini 
		 try { mt.waitForAll(); } 
		 catch (InterruptedException e){} 
	
	}
	
	//Disegna scacchiera con un paint component, ogni casella e ogni pedinaGrafica ha il proprio metodo di disegno che viene richiamato da
	//	paint component, ogni volta che faccio una modifica alla scacchiera chiama il metodo repaint.	
	
	public void inizializzaPedineGrafiche(ArrayList<Pedina> pedine){
		
		for(Pedina p : pedine)
			getPedineGrafiche().add(new PedinaGrafica( p, this));
	}
	
	public Casella getCasella(Pedina p){
		return getCaselle()[p.getX()][p.getY()];
	}
	
	//metodo utilizzato per gestire le componenti grafiche
	public void paintComponent(Graphics g){
		
		super.paintComponent(g);
		
		//disegna le caselle
		for(int i = 0; i < 8; i++)
			for(int j = 0; j < 8; j++)
				getCaselle()[i][j].paintCasella(g);
		
		//disegna le pedine
		for( PedinaGrafica pg : getPedineGrafiche())
			pg.paintPedina(g);
		
		//disegna un immagine passatagli
		g.drawImage( immagine, 0, 0, null);
				
		}
	
	//fa il repaint di tutte le caselle
	public void resetScacchiera() {
		
		for(int h = 0; h < 8; h++)
						for(int k = 0; k < 8; k++)
								caselle[h][k].resetColor();
		immagine = null;
		repaint();	
	}

	//cambia l'immagine per la partita persa
	public void setImmaginePerdita() {
		immagine = tk.getImage("img/perdita.png");
		mt.addImage(immagine,1);
		// aggiunta di eventuali altre immagini 
		try { mt.waitForAll(); } 
		catch (InterruptedException e){} 
	}
	
	//cambia l'immagine per la partita vinta
	public void setImmagineVincita() {
		immagine = tk.getImage("img/vincita.png"); 
		mt.addImage(immagine,1);
		// aggiunta di eventuali altre immagini 
		try { mt.waitForAll(); } 
		catch (InterruptedException e){} 
	}
	
	//cambia l'immagine per le informazioni
	public void setImmagineInfo() {
		immagine = tk.getImage("img/info.png"); 
		mt.addImage(immagine,1);
		// aggiunta di eventuali altre immagini 
		try { mt.waitForAll(); } 
		catch (InterruptedException e){} 
	}

	//cambia l'immagine per le istruzioni
	public void setImmagineIstru() {
		immagine = tk.getImage("img/istruzioni.png"); 
		mt.addImage(immagine,1);
		// aggiunta di eventuali altre immagini 
		try { mt.waitForAll(); } 
		catch (InterruptedException e){} 
	}
	
		public boolean immagineUgualeInfoIstru() {
			return ( (immagine == immagineInfo) || (immagine == immagineIstru));
		}

		public ArrayList<PedinaGrafica> getPedineGrafiche() {
			return pedineGrafiche;
		}

		public void setPedineGrafiche(ArrayList<PedinaGrafica> pedineGrafiche) {
			this.pedineGrafiche = pedineGrafiche;
		}

		public Casella[][] getCaselle() {
			return caselle;
		}
}
