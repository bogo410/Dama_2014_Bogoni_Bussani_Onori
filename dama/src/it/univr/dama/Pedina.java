package it.univr.dama;

import java.awt.Color;


public class Pedina {
	
		//attributi pedina: coordinate sulla scacchiera e colore
		private int x;
		private int y;
		private Color colore;
		
		//array per gestire il peso di ogni mossa, usato dal controllore CPU per decidere che mossa fare
		private int[] pesiMosse = new int[8];
		private int  indiceMax= 0;
		
		//costruttore pedina
		public Pedina(int i, int j, Color colore){
			this.x = i;
			this.y = j;
			this.colore = colore;
		}
		
		//secondo costruttore per creare una nuova pedina partendo da un'altra pedina(utilizzata in Dama)
		public Pedina(Pedina p){
			this(p.x,p.y,p.colore);
			for(int i = 0; i <8 ; i++)
				this.pesiMosse[i] = p.pesiMosse[i];
		}
		
		//aumenta la mossa indice con il peso passato
		public void aumentaMossa(int indice, int peso) {
			pesiMosse[indice] += peso;
			if (pesiMosse[indice] < 0)
				pesiMosse[indice] = 0;
		}

		//cambia le coordinate per il movimento
		public void muovi(int indice) {
			
			switch (indice){
			case 0: x++; y++; break;
			case 1: x--; y++; break;
			case 2: x += 2; y += 2; break;
			case 3: x -= 2; y += 2; break;
			case 4: x++; y--; break;
			case 5: x--; y--; break;
			case 6: x+=2; y-=2; break;
			case 7: x-=2; y-=2; break;
			}
		}

		//inizializza i pesi delle mosse
		public void inizializzaArray() {
			for(int i = 0; i< pesiMosse.length; i++)
				pesiMosse[i] = 0;
		}

		//muove la pedina con indice massimo
		public void muoviMax() {
			muovi(indiceMax);
			return;
		}

		//setta una mossa tramite l'indice ad un valore passatogli
		public void setMossa(int indice, int peso) {
			pesiMosse[indice] = peso;
			if (pesiMosse[indice] < 0)
				pesiMosse[indice] = 0;
			
		}
		
		//bilancia il peso di una singola mossa in base al peso passatogli
		public void bilanciaMossa( int indice, int peso ) {
			if(pesiMosse[indice] != 0)
				aumentaMossa(indice, peso);
		}

		//metodo per ottenere la coordinata x dall'esterno
		public int getX() {
			return x;
		}

		//metodo per settare la coordinata x dall'esterno
		public void setX(int x) {
			this.x = x;
		}

		//metodo per ottenere la coordinata y dall'esterno
		public int getY() {
			return y;
		}

		//metodo per settare la coordinata y dall'esterno
		public void setY(int y) {
			this.y = y;
		}

		//metodo per ottenere il colore dall'esterno
		public Color getColore() {
			return colore;
		}

		//metodo per settare il colore dall'esterno
		public void setColore(Color colore) {
			this.colore = colore;
		}
		public Pedina getPedina(){
			return this;
		}

		//ritorna l'indice massimo
		public int getIndiceMax(){
			return indiceMax;
		}

		//ritorna l'indice massimo tra le mosse della pedina
		public int getMossaMax() {
			int max = -1;
			for(int i = 0; i < pesiMosse.length; i++){
				if(max < pesiMosse[i]){
					max = pesiMosse[i];
					indiceMax = i;
				}
			}
			return max;
		}
		
		//ritorna l'array di pesi delle mosse
		public int[] getPesiMosse() {
			return pesiMosse;
		}
}
