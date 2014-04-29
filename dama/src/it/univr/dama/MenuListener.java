package it.univr.dama;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

public class MenuListener implements ActionListener{
	private JMenuItem menu;
	private JMenuItem facile;
	private JMenuItem normale;
	private JMenuItem difficile;
	private JMenuItem info;
	private JMenuItem istruzioni;
	private JMenuItem esci;
	private Gioco game;
	private Finestra finestra;
	private  ControlloreCPU controlloreCPU;
	
	//costruttore
	public MenuListener(JMenuItem menu, JMenuItem facile, JMenuItem normale, JMenuItem difficile, JMenuItem info, JMenuItem esci, JMenuItem istruzioni, Gioco game, Finestra finestra){
		
		this.menu = menu;
		this.facile = facile;
		this.normale = normale;
		this.difficile = difficile;
		this.info = info;
		this.istruzioni = istruzioni;
		this.esci = esci;

		this.game = game;
		this.finestra = finestra;
		this.controlloreCPU = game.getControlloreCPU();
	}
	
	public void actionPerformed(ActionEvent e) {
		
		//salva la sorgente in base alla pressione del mouse
		Object src = e.getSource();
		
		//se la sorgente è menu'
		if(src == menu){
			
			//mette l'immagine a null per mostrare la scacchiera
			finestra.getScacchiera().immagine = null;
			
			//ricolora tutte le caselle
			finestra.getScacchiera().resetScacchiera();

			//crea un nuovo gioco
			game.nuovoGioco();
			
			//reimposta la difficolta massima
			controlloreCPU.cambiaDifficolta(2);
			
			//ridipinge la finestra
			finestra.repaint();
		}	
		
		//se la sorgente è facile(difficoltà)
		if(src == facile){
			//imposta la difficolta minima
			controlloreCPU.cambiaDifficolta(0);
		}
		
		//se la sorgente è media(difficoltà)
		if(src == normale){
			//imposta la difficolta media
			controlloreCPU.cambiaDifficolta(1);
		}
				
		//se la sorgente è facile(difficoltà)
		if(src == difficile){
			//imposta la difficolta massima
			controlloreCPU.cambiaDifficolta(2);
		}
		
		//se la sorgente è info
		if(src == info){
					
			//mette l'immagine per le informazioni
			finestra.getScacchiera().setImmagineInfo();
			finestra.getScacchiera().repaint();
		}
		
		//se la sorgente è istruzioni
		if(src == istruzioni){
							
			//mette l'immagine per le istruzioni
			finestra.getScacchiera().setImmagineIstru();
			finestra.getScacchiera().repaint();
		}
		
		//se la sorgente è esci
		if(src == esci){
							
			//esce senza errori
			System.exit(0);
		}			
	}

}
