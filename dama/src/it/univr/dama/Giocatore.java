package it.univr.dama;

import java.awt.Color;
import java.util.ArrayList;

public class Giocatore {
	
	private Color colore;
	private ArrayList<Pedina> setPedine;
	
	public Giocatore( Gioco g, Color colore){
		this.colore = colore;
		setPedine = new ArrayList<Pedina>();
		
	}
	
	// inizializza l'arrayList di Pedine del giocatore
	public ArrayList<Pedina> inizializzaPedine(){
		if(colore == Color.BLACK) //se le pedine sono nere le inizializza nella parte alta della scacchiera
			for(int i=0; i<12; i++)
				if(i/ 4 % 2 == 0)
					setPedine.add(new Pedina(((i % 4) * 2), i / 4, colore));
				else
					setPedine.add(new Pedina(((i % 4) * 2 + 1), i / 4, colore));
		
		else //altrimenti le crea nella parte bassa
			for(int i=0; i<12; i++)
				if(i/ 4 % 2 == 0)
					setPedine.add(new Pedina(((i % 4) * 2 + 1), 7-(i / 4), colore));
				else
					setPedine.add(new Pedina(((i % 4) * 2),7-( i / 4), colore));
	
		return setPedine;

	}
	
	//svuota il set di pedine del giocatore
	public void svuota(){
		setPedine.clear();
	}
	
	//rimuove la pedina selezionata
	public void rimuoviPedina(int x, int y){

		for(Pedina p : setPedine){
			if(p.getX() == x && p.getY() == y){
				setPedine.remove(p);
				break;
			}
			
		}
	}

	
	//crea la dama a partire dalla pedina pronta ad essere trasformata
	public Dama creaDama(Pedina pSel) {
			
		//elimina la pedina per sostituirla
		setPedine.remove(pSel);
			
		//crea la nuova dama e la aggiunge all'arraylist di pedine
		Dama d = new Dama(pSel);
		setPedine.add(d);
			
		return d;
	}

	//ritorna true se il setPedine del giocatore  vuoto
	public boolean setPedineVuoto() {
		if(setPedine.isEmpty())
			return true;
		return false;
	}

	//ritorna l'arraylist di pedine
	public ArrayList<Pedina> getSetPedine() {
		return setPedine;
	}

}
