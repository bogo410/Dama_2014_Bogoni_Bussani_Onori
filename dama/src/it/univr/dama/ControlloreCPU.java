package it.univr.dama;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;


public class ControlloreCPU {
	
	final int MOV = 10;
	final int MAN = 15;
	final int MANIND= 20;
	
	//booleano di controllo per creare la dama
	private boolean damaCreata = false;
	
	//livello di difficolt�
	public static int difficolta;
	
	private final Scacchiera s;
	private PannelloSupporto pS;
	private final Casella[][] caselle;
	private Giocatore g1;
	private Giocatore CPU;
	private Gioco game;
	private Set<Pedina> pedineCheMuovono = new HashSet<Pedina>();
	private int max;
	
	public ControlloreCPU(Finestra finestra, Gioco game, Giocatore g1, Giocatore CPU, PannelloSupporto pS){
		
		this.pS = pS;
		this.game = game;
		this.g1 = g1;
		this.CPU = CPU;
		this.s = finestra.getScacchiera();
		this.caselle = s.getCaselle();
		difficolta=2;
		
	}

	//funzione che da il via a tutti i metodi del controlloreCPU
	public void attiva() {
		damaCreata = false;
		
		//mette max a 0
		azzeraMax();
		
		//ripulisce l'hashset delle pedine che possono muovere
		pedineCheMuovono.clear();
		inizializzaArrayPedine();
		
		//crea l'elenco delle pedine che possono muovere
		elencoPedine();
		
		//valuta che mosse incrementare e come
		valutaMosse();
		
		//esegue la mossa
		eseguiMossa();
		
		//passa il turno al giocatore bianco
		game.passaTurno();
		
	}
	
	//inizializza l'array di mosse di tutte le pedine della CPU
	private void inizializzaArrayPedine() {
		for(Pedina p: CPU.getSetPedine())
			p.inizializzaArray();
		
	}

	//esegue la mossa in base al massimo peso tra le mosse delle pedine della CPU
	private void eseguiMossa() {
		Pedina pSel = null;
		Dama dSel = null;
		max = -1;
		
		for(Pedina p: pedineCheMuovono){
			if(max < p.getMossaMax()){
				max = p.getMossaMax();
				pSel = p;
			}
		}
		
		if(max >= 0){
			int indice = pSel.getIndiceMax();
			if( indice == 2 || indice == 3 || indice == 6 || indice == 7 ){
				Pedina prec = new Pedina(pSel);
				
				//evidenzia la casella appena liberata
				selezionaCasellaTraccia(pSel,Color.YELLOW);
				pSel.muoviMax();
				//evidenzia la casella appena occupata
				selezionaCasellaTraccia(pSel,Color.YELLOW);
				
				rimuoviPedinaAvversario(prec,pSel);
				inizializzaArrayPedine();
				
				if(pSel != null && puoMangiare(pSel)){
					pedineCheMuovono.removeAll(pedineCheMuovono);
					pedineCheMuovono.add(pSel);
					
					//evidenzia la casella appena liberata
					selezionaCasellaTraccia(pSel,Color.YELLOW);
					eseguiMossa();
					//evidenzia la casella appena occupata
					selezionaCasellaTraccia(pSel,Color.YELLOW);
					
					pSel.inizializzaArray();
					max = 0;
				}
				
			}else{
				//evidenzia la casella appena liberata
				selezionaCasellaTraccia(pSel,Color.YELLOW);
				pSel.muoviMax();
				//evidenzia la casella appena occupata
				selezionaCasellaTraccia(pSel,Color.YELLOW);
			}
			
			//se la pedina ha mangiato e pu� ancora mangiare rimangia
			pSel.inizializzaArray();
			
		}
		
		if(pSel!=null && pSel.getY() == 7){
				
				//scorre sull'arrayList di pedinegrafiche ed elimina la pedina grafica corrispondente
				for(PedinaGrafica pg : s.getPedineGrafiche()){
					
					if(pg.getPedina().getX() == pSel.getX() && pg.getPedina().getY() == pSel.getY() && damaCreata==false){
						s.getPedineGrafiche().remove(pg);
						break;
					}
				}
				
				//Crea la dama rimuovendo la pedina poi crea anche la dama grafica se non � appena stata creata
				if(damaCreata==false){
					dSel = CPU.creaDama(pSel);
					s.getPedineGrafiche().add(new DamaGrafica(dSel, s));
					damaCreata=true; //per sapere che � appena stata creata
				}
			}
	}
	
	//metodo che seleziona la casella del colore specificato per tenere traccia del movimento delle pedine nere
	private void selezionaCasellaTraccia(Pedina pSel, Color colore){

		//scorre tutte le caselle e evidenzia quella corrispondente alla pedina da muovere cos� da tener traccia del movimento
		for(int i = 0; i < 8; i++)
			for(int j = 0; j < 8; j++)
					if(caselle[i][j].getX() == pSel.getX() && caselle[i][j].getY() == pSel.getY())
						caselle[i][j].setColor(colore);	
	}

	//rimuove la pedina dell'avversario(giocatore1)
	private void rimuoviPedinaAvversario(Pedina prec, Pedina pSel) {
		int xMangiata = (prec.getX() + pSel.getX())/2;
		int yMangiata = (prec.getY() + pSel.getY())/2;
		
		//elimina la pedina e la pedina grafica avversarie mangiata ciclando sulle pedine e trovando quella da eliminare
		for(PedinaGrafica pg : s.getPedineGrafiche()){
			
			if(pg.getPedina().getX() == xMangiata && pg.getPedina().getY() == yMangiata){
				
				//evidenzia la casella in cui � appena stata mangiata una pedina
				selezionaCasellaTraccia(pg.getPedina(),Color.MAGENTA);
				
				//rimuove la pedina e la pedina grafica avversaria
				g1.rimuoviPedina(xMangiata,yMangiata);
				s.getPedineGrafiche().remove(pg);
				break;
			}
		}
		
		
	}

	//crea l'elenco delle pedine che possono muovere
	private void elencoPedine() {
		//carica nell'elenco le pedine che possono mangiare
		caricaPossonoMangiare();
		//se quell'elenco non � vuoto allora carica le mosse di movimento
		if(pedineCheMuovono.isEmpty())
			caricaPossonoMuovere();
		
	}

	//carica l'Hash set delle pedine che possono muovere
	private void caricaPossonoMuovere() {
		for(Pedina p: CPU.getSetPedine())
			if(puoMuovere(p)){
				pedineCheMuovono.add(p);
			}
		
	}

	//verifica se la pedina passata pu� muovere e aumenta i pesi corrispondenti al movimento
	private boolean puoMuovere(Pedina p) {
		boolean faQualcosa = false;
			
			if(casellaVuota(p.getX()+1,p.getY()-1))
				if(p instanceof Dama){
					faQualcosa = true;
					//aumenta il peso della mossa corrispondente nell'array pesiMosse della pedina in questione
					p.aumentaMossa(4,MOV);}
			if(casellaVuota(p.getX()-1,p.getY()-1))
				if(p instanceof Dama){
					faQualcosa = true;
					p.aumentaMossa(5,MOV);}	
			if(casellaVuota(p.getX()+1,p.getY()+1 )){
					faQualcosa = true;
					p.aumentaMossa(0,MOV);}
			if(casellaVuota(p.getX()-1,p.getY()+1 )){
					p.aumentaMossa(1,MOV);
					faQualcosa = true;}			
		return faQualcosa;

	}

	//carica sempre sullo stesso Hash set delle pedine che possono muovere le pedine che possono mangiare
	private void caricaPossonoMangiare() {
		//cicla l'arraylist di pedine e aggiunge le pedine che possono mangiare
		for(Pedina p: CPU.getSetPedine())
			//se p pu� mangiare la carica
			if(puoMangiare(p)){
				pedineCheMuovono.add(p);
			}
	}

	//verifica quando la pedina passata pu� mangiare e aumenta i pesi corrispondenti alla mangiata
	private boolean puoMangiare(Pedina p) {		
		
		boolean faQualcosa = false;
		for(Pedina pAvv: g1.getSetPedine()){
			
			if(pAvv.getX() == p.getX() + 1 && pAvv.getY() == p.getY() - 1 && casellaVuota(p.getX()+2,p.getY()-2 ))
				if(p instanceof Dama){
					faQualcosa = true;
					//aumenta il peso della mossa corrispondente nell'array pesiMosse della pedina in questione
					p.aumentaMossa(6,MAN);}
			if(pAvv.getX() == p.getX() - 1 && pAvv.getY() == p.getY() - 1 && casellaVuota(p.getX()-2,p.getY()-2 ))
				if(p instanceof Dama){
					faQualcosa = true;
					p.aumentaMossa(7,MAN);}	
			if(pAvv.getX() == p.getX() + 1 && pAvv.getY() == p.getY() + 1 && casellaVuota(p.getX()+2,p.getY()+2 ) && ((p instanceof Dama) || !(pAvv instanceof Dama))){
					faQualcosa = true;
					p.aumentaMossa(2,MAN);}
			if(pAvv.getX() == p.getX() - 1 && pAvv.getY() == p.getY() + 1 && casellaVuota(p.getX()-2,p.getY()+2 ) && ((p instanceof Dama) || !(pAvv instanceof Dama))){
					p.aumentaMossa(3,MAN);
					faQualcosa = true;}			
		}
		return faQualcosa;

	}
	
	//verifica che la casella non sia occupata da una pedina
	private boolean casellaVuota(int x, int y) {
		//verifica che si controlli in una casella all'interno della scacchiera
		if(x>7 || x< 0 || y < 0 || y> 7)
			return false;
		
		for(Pedina pAvv: g1.getSetPedine())
			if(pAvv.getX() == x && pAvv.getY() == y)
				return false;
			
		for(Pedina pAvv: CPU.getSetPedine())
			if(pAvv.getX() == x && pAvv.getY() == y)
				return false;
		
		return true;
	}
	
	//richiama le funzioni per aggiornare i pesi delle mosse delle pedine della CPU
	private void valutaMosse(){
		valutaPosizionamentoPedine();
		valutaPosizionamentoPedineAvversarie();
		valutaControMosse();
	}

	//valuta il posizionamento delle pedine nere nella scacchiera
	private void valutaPosizionamentoPedine() {
			
			//cicla sulle pedine che possono muovere
			for(Pedina p: pedineCheMuovono){
				
				//se la pedina � vicina al bordo sinistro della scacchiera
				if( p.getX() >= 5 ){
					
					p.bilanciaMossa(0,1);	//aumentiamo di 1 il movimento a sinistra
					p.bilanciaMossa(2,1);	//aumentiamo di 1 la mangiata a sinistra
					p.bilanciaMossa(4,1);	//aumentiamo di 1 il movimento opposto a sinistra
					p.bilanciaMossa(6,1);	//aumentiamo di 1 la mangiata opposta a sinistra
				}
				
				//se la pedina � vicina al bordo destro della scacchiera
				if( p.getX() <= 2 ){
					
					p.bilanciaMossa(1,1);	//aumentiamo di 1 il movimento a destra
					p.bilanciaMossa(3,1);	//aumentiamo di 1 la mangiata a destra
					p.bilanciaMossa(5,1);	//aumentiamo di 1 il movimento opposto a destra
					p.bilanciaMossa(7,1);	//aumentiamo di 1 la mangiata opposta a destra
				}
				
				//se la pedina � vicina al bordo avversario della scacchiera
				if( p.getY() >= 5 && !(p instanceof Dama)){
					
					//aumenta le mosse in modo da raggiungere la sponda avversaria per creare la dama
					p.bilanciaMossa(0,5);	//aumentiamo di 5 il movimento a sinistra
					p.bilanciaMossa(1,5);	//aumentiamo di 5 il movimento a destra
					p.bilanciaMossa(2,5);	//aumentiamo di 5 la mangiata a sinistra
					p.bilanciaMossa(3,5);	//aumentiamo di 5 la mangiata a destra
					
					//aumenta le mosse in modo da bilanciare i pesi per la dama, per i movimenti opposti
					p.bilanciaMossa(4,2);	//aumentiamo di 2 il movimento opposto a sinistra
					p.bilanciaMossa(5,2);	//aumentiamo di 2 il movimento opposto a destra
					p.bilanciaMossa(6,2);	//aumentiamo di 2 la mangiata opposta a sinistra
					p.bilanciaMossa(7,2);	//aumentiamo di 2 la mangiata opposta a destra
				}
			
				//se la pedina nera � una dama e pu� essere mangiata vengono aumentati i pesi delle sue mosse in modo che si sposti
				for(Pedina pAvv : g1.getSetPedine()){
					if(puoEssereMangiataDama(pAvv)){
						for(int i=0; i<8; i++)
							p.bilanciaMossa(i,4);	//aumentiamo di 4 tutti i suoi pesi
					}
				}
			}	
		}

	//valuta il posizionamento delle pedine bianche nella scacchiera	
	private void valutaPosizionamentoPedineAvversarie() {
		
		//Verifica se una delle caselle dell'ultima riga � vuota se � vuota cicla su tutte le pedine che possono muovere e incrementa il movimento verso la casella vuota
		for(Pedina p: pedineCheMuovono){
			if(!(p instanceof Dama)){
				if(casellaVuota(1,7))
						if((p.getX() - 1) > 0){
							p.bilanciaMossa(1,1);	//aumentiamo di 1 il movimento a destra
							p.bilanciaMossa(3,1);	//aumentiamo di 1 la mangiata a destra
						}
						else{
							p.bilanciaMossa(0,1);	//aumentiamo di 1 il movimento a sinistra
							p.bilanciaMossa(2,1);	//aumentiamo di 1 la mangiata a sinistra
						}
				if(casellaVuota(3,7)){
					if((p.getX() - 3) > 0){
						p.bilanciaMossa(1,1);	//aumentiamo di 1 il movimento a destra
						p.bilanciaMossa(3,1);	//aumentiamo di 1 la mangiata a destra
					}
					else{
						p.bilanciaMossa(0,1);	//aumentiamo di 1 il movimento a sinistra
						p.bilanciaMossa(2,1);	//aumentiamo di 1 la mangiata a sinistra
					}}
				if(casellaVuota(5,7)){
					if((p.getX() - 5) > 0){
						p.bilanciaMossa(1,1);	//aumentiamo di 1 il movimento a destra
						p.bilanciaMossa(3,1);	//aumentiamo di 1 la mangiata a destra
					}
					else{
						p.bilanciaMossa(0,1);	//aumentiamo di 1 il movimento a sinistra
						p.bilanciaMossa(2,1);	//aumentiamo di 1 la mangiata a sinistra
					}}
				if(casellaVuota(7,7)){
						p.bilanciaMossa(0,1);	//aumentiamo di 1 il movimento a sinistra
						p.bilanciaMossa(2,1);	//aumentiamo di 1 la mangiata a sinistra
					}
			}
			//se la pedina selezionata � una dama e le pedine del giocatore1 sono meno di 10 incrementa le mosse verso le altre pedine
			if (p instanceof Dama && (g1.getSetPedine().size() < 9)){
				for(Pedina pAvv : g1.getSetPedine()){
					if(p.getX() - pAvv.getX() > 0){
						p.bilanciaMossa(1,1);	//aumentiamo di 1 il movimento a destra
						p.bilanciaMossa(3,1);	//aumentiamo di 1 la mangiata a destra
						p.bilanciaMossa(5,1);	//aumentiamo di 1 il movimento opposto a destra
						p.bilanciaMossa(7,1);	//aumentiamo di 1 la mangiata opposta a destra
					}	
					else{
						p.bilanciaMossa(0,1);	//aumentiamo di 1 il movimento a sinistra
						p.bilanciaMossa(2,1);	//aumentiamo di 1 la mangiata a sinistra
						p.bilanciaMossa(4,1);	//aumentiamo di 1 il movimento opposto a sinistra
						p.bilanciaMossa(6,1);	//aumentiamo di 1 la mangiata opposta a sinistra
					}
					if(p.getY() >= pAvv.getY()){
						p.bilanciaMossa(4,1);	//aumentiamo di 1 il movimento opposto a sinistra
						p.bilanciaMossa(5,1);	//aumentiamo di 1 il movimento opposto a destra
						p.bilanciaMossa(6,1);	//aumentiamo di 1 la mangiata opposta a sinistra
						p.bilanciaMossa(7,1);	//aumentiamo di 1 la mangiata opposta a destra
					}
					else{
						p.bilanciaMossa(0,1);	//aumentiamo di 1 il movimento a sinistra
						p.bilanciaMossa(1,1);	//aumentiamo di 1 il movimento a destra
						p.bilanciaMossa(2,1);	//aumentiamo di 1 la mangiata a sinistra
						p.bilanciaMossa(3,1);	//aumentiamo di 1 la mangiata a destra
					}
					
				}
			}
		
		
		//se la pedina selezionata � una dama e le pedine del giocatore1 sono meno di 3 incrementa le mosse verso le altre pedine
		if (p instanceof Dama && (g1.getSetPedine().size() < 3)){
			for(Pedina pAvv : g1.getSetPedine()){
				if(p.getX() - pAvv.getX() > 0){
					p.bilanciaMossa(1,2);	//aumentiamo di 2 il movimento a destra
					p.bilanciaMossa(3,2);	//aumentiamo di 2 la mangiata a destra
					p.bilanciaMossa(5,2);	//aumentiamo di 2 il movimento opposto a destra
					p.bilanciaMossa(7,2);	//aumentiamo di 2 la mangiata opposta a destra
				}	
				else{
					p.bilanciaMossa(0,2);	//aumentiamo di 2 il movimento a sinistra
					p.bilanciaMossa(2,2);	//aumentiamo di 2 la mangiata a sinistra
					p.bilanciaMossa(4,2);	//aumentiamo di 2 il movimento opposto a sinistra
					p.bilanciaMossa(6,2);	//aumentiamo di 2 la mangiata opposta a sinistra
				}
				if(p.getY() >= pAvv.getY()){
					p.bilanciaMossa(4,2);	//aumentiamo di 2 il movimento opposto a sinistra
					p.bilanciaMossa(5,2);	//aumentiamo di 2 il movimento opposto a destra
					p.bilanciaMossa(6,2);	//aumentiamo di 2 la mangiata opposta a sinistra
					p.bilanciaMossa(7,2);	//aumentiamo di 2 la mangiata opposta a destra
				}
				else{
					p.bilanciaMossa(0,2);	//aumentiamo di 2 il movimento a sinistra
					p.bilanciaMossa(1,2);	//aumentiamo di 2 il movimento a destra
					p.bilanciaMossa(2,2);	//aumentiamo di 2 la mangiata a sinistra
					p.bilanciaMossa(3,2);	//aumentiamo di 2 la mangiata a destra
				}
				
			}
		}
	}

	}

	private void valutaControMosse() {
		
		//entra solo se il livello di difficolt� � maggiore di 0, altrimenti la difficolt� si basa solo sulle valutazioni sulle posizioni attuali delle pedine
		if(difficolta > 0){
		
			//cicla sulle pedine nere che possono muovere e verifica se facendo una determinata mossa vengono mangiate evitando cos� di farsi mangiare
			for(Pedina p : pedineCheMuovono){
				
				//se si trova una pedina bianca sulla stessa diagonale in cui muove oppure sulla diagonale opposta rispetto a cui muove e la pu� mangiare setta a 5 il movimento a sinistra
				if(casellaVuota(p.getX()+1,p.getY()+1))
					if( casellaOccupataDaPedinaBianca(p.getX()+2,p.getY()+2) || (casellaOccupataDaPedinaBianca(p.getX(),p.getY()+2) && casellaVuota(p.getX()+2,p.getY())) )
						p.setMossa(0, 5); //setta a 5(valore basso) il movimento in quella direzione
				//se si trova una pedina bianca sulla stessa diagonale in cui muove oppure sulla diagonale opposta rispetto a cui muove e la pu� mangiare setta a 5 il movimento a destra
				if(casellaVuota(p.getX()-1,p.getY()+1))	
					if( casellaOccupataDaPedinaBianca(p.getX()-2,p.getY()+2) || (casellaOccupataDaPedinaBianca(p.getX(),p.getY()+2) && casellaVuota(p.getX()-2,p.getY())) )
						p.setMossa(1, 5); //setta a 5(valore basso) il movimento in quella direzione
			
				//entra solo se il livello di difficolt� � massimo, altrimenti la difficolt� si basa sulle valutazioni sulle posizioni e solo in parte sull'evitare di farsi mangiare 
				if(difficolta == 2){
					
					if(casellaVuota(p.getX(),p.getY()+2)){
						//se si trova una dama bianca sopra a sinistra e la pu� mangiare setta a 5 il movimento a sinistra
						if( casellaOccupataDaDamaBianca(p.getX()+2,p.getY()) && casellaVuota(p.getX()+1,p.getY()+1) )
								p.setMossa(0, 5); //setta a 5(valore basso) il movimento in quella direzione
						//se si trova una dama bianca sopra a destra e la pu� mangiare setta a 5 il movimento a destra
						if( casellaOccupataDaDamaBianca(p.getX()-2,p.getY()) && casellaVuota(p.getX()-1,p.getY()+1) )
								p.setMossa(1, 5); //setta a 5(valore basso) il movimento in quella direzione
					}
					
					//se la pedina nera in questione � una dama deve evitare di farsi mangiare anche risalendo la scacchiera in direzione opposta
					if( p instanceof Dama){
						
						//se si trova una dama bianca vicino che la pu� mangiare muovendo indietro a sinistra setta a 5 il movimento indietro a sinistra
						if(casellaVuota(p.getX()+1,p.getY()-1))
							if( casellaOccupataDaDamaBianca(p.getX()+2,p.getY()-2) || (casellaOccupataDaDamaBianca(p.getX(),p.getY()-2) && casellaVuota(p.getX()+2,p.getY())) || (casellaOccupataDaDamaBianca(p.getX()+2,p.getY()) && casellaVuota(p.getX(),p.getY()-2)) )
								p.setMossa(4, 5); //setta a 5(valore basso) il movimento in quella direzione;
						//se si trova una dama bianca vicino che la pu� mangiare muovendo indietro a destra setta a 5 il movimento indietro a destra
						if(casellaVuota(p.getX()-1,p.getY()-1))
							if( casellaOccupataDaDamaBianca(p.getX()-2,p.getY()-2) || (casellaOccupataDaDamaBianca(p.getX(),p.getY()-2) && casellaVuota(p.getX()-2,p.getY())) || (casellaOccupataDaDamaBianca(p.getX()-2,p.getY()) && casellaVuota(p.getX(),p.getY()-2)) )
								p.setMossa(5, 5); //setta a 5(valore basso) il movimento in quella direzione;
					}
				}
			}
		}
	}
	
	//controlla se la casella indicata � occupata da una pedina bianca
	private boolean casellaOccupataDaPedinaBianca(int x, int y){
		//controlla che si guardi in una casella all'interno della scacchiera
				if(x>7 || x< 0 || y < 0 || y> 7)
					return false;
				
				for(Pedina p: g1.getSetPedine())
					if(p.getX() == x && p.getY() == y)
						return true;
				
				return false;
	}
	
	//controlla se la casella indicata � occupata da una dama bianca
	private boolean casellaOccupataDaDamaBianca(int x, int y){
		//controlla che si guardi in una casella all'interno della scacchiera
				if(x>7 || x< 0 || y < 0 || y> 7)
					return false;
				
				for(Pedina p: g1.getSetPedine())
					if( (p.getX() == x && p.getY() == y) && (p instanceof Dama) )
						return true;
				
				return false;
	}
	
	//ritorna true se la dama passata pu� essere mangiata
	private boolean puoEssereMangiataDama(Pedina pBianca) {
		boolean faQualcosa = false;
		if(pBianca instanceof Dama){
			for(Pedina pSel: pedineCheMuovono){
				if(pSel instanceof Dama){
					if( pBianca.getX() == pSel.getX() + 1 && pBianca.getY() == pSel.getY() - 1 && casellaVuota(pSel.getX()-1,pSel.getY()+1 ) )
						faQualcosa = true;
					if( pBianca.getX() == pSel.getX() - 1 && pBianca.getY() == pSel.getY() - 1 && casellaVuota(pSel.getX()+1,pSel.getY()+1 ) )
						faQualcosa = true;
					if( pBianca.getX() == pSel.getX() - 1 && pBianca.getY() == pSel.getY() + 1 && casellaVuota(pSel.getX()+1,pSel.getY()-1 ) )
						faQualcosa = true;
					if( pBianca.getX() == pSel.getX() + 1 && pBianca.getY() == pSel.getY() + 1 && casellaVuota(pSel.getX()-1,pSel.getY()-1 ) )
						faQualcosa = true;
				}
			}
		}
		return faQualcosa;
	}
	
	//ritorna max
	public int getMax(){
	return max;
	}
	
	//azzera max
	public void azzeraMax(){
		max=0;
	}
	
	//cambia la difficolt� in base alla scelta del giocatore
	public void cambiaDifficolta(int nuovaDifficolta){
		
		if(nuovaDifficolta == 0)
			difficolta = 0;
		else if(nuovaDifficolta == 1)
			difficolta = 1;
		else
			difficolta = 2;
		pS.aggiornaDifficolta();
	}
}
