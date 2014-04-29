package it.univr.dama;

import java.awt.Color;


public class Gioco {
	
	private final Finestra finestra;
	private Giocatore g1, CPU;
	private ControlloreCPU controlloreCPU;
	private Mouse giocatore1Controllore;
	private Color turno;
	private PannelloSupporto pS;
	private boolean finito = false;

	public Gioco(Finestra finestra, PannelloSupporto pS){
		this.finestra = finestra;
		this.pS = pS;
		
		g1 = new Giocatore (this, Color.WHITE);
		CPU = new Giocatore(this, Color.BLACK);
		
		giocatore1Controllore = new Mouse(finestra.getScacchiera(), this, CPU, g1, pS);
		
		//pulisce gli arraylist se sono già occupati
		g1.svuota();
		CPU.svuota();
		
		// setta il turno iniziale a WHITE, cioè per il giocatore 1
		turno = Color.WHITE;
		
		
		this.controlloreCPU = new ControlloreCPU( finestra, this, g1, CPU, pS);
		
		//aggiunge il mouse listener
		finestra.getScacchiera().addMouseListener(giocatore1Controllore);
		
	}

	//per creare un nuovo gioco
	public void nuovoGioco(){
		
		//pulisce gli arraylist se sono già occupati
		g1.svuota();
		CPU.svuota();
		
		finito = false;
		pS.cambiaMessaggio("Inizia la partita!");
		turno = Color.WHITE;
		
		//elimina anche le pedine grafiche
		finestra.getScacchiera().getPedineGrafiche().clear();
		finestra.getScacchiera().inizializzaPedineGrafiche(g1.inizializzaPedine());
		finestra.getScacchiera().inizializzaPedineGrafiche(CPU.inizializzaPedine());
		
	}
	
	//restituisce true se il gioco è finito, inoltre cambia immagini e messaggi in modo da avvisare che la partita è finita e comunicare chi ha vinto
	public boolean giocoFinito() {

		//se il setPedine di g1 è vuoto oppure g1 non ha più mosse disponibili la partita è finita ed è stata vinta dal giocatore CPU
		if( g1.setPedineVuoto() || giocatore1Controllore.nonHaMosseDisponibili()){
			System.out.println("CPU ha vinto");
			//imposta l'immagine per la vincita
			finestra.getScacchiera().setImmaginePerdita();
			//azzera max
			controlloreCPU.azzeraMax();
			finito = true;
			pS.cambiaMessaggio("Partita terminata!");
			return true;
		}
		
		//se il setPedine di CPU è vuoto oppure CPU non ha più mosse disponibili la partita è finita ed è stata vinta dal giocatore 1
		if( CPU.setPedineVuoto() || controlloreCPU.getMax() == -1){
			System.out.println("Giocatore1 ha vinto");
			//imposta l'immagine per la vincita
			finestra.getScacchiera().setImmagineVincita();
			//azzera max
			controlloreCPU.azzeraMax();
			finito=true;
			pS.cambiaMessaggio("Partita terminata!");
			return true;
		}
		
		
		return false;
	}
	
	//passa il turno all'avversario
	public void passaTurno(){
		
		if(!giocoFinito()){
			if(turno == Color.WHITE){
				turno = Color.BLACK;
				controlloreCPU.attiva();
			}
			else{
				turno = Color.WHITE;
			}
		}
	
	}

	public Color getTurno() {
		return turno;
	}
	
	public boolean getFinito() {
		return finito;
	}
	
	public Mouse getGiocatoreG1controllore(){
		return giocatore1Controllore;
		
	}

	public ControlloreCPU getControlloreCPU() {
		return controlloreCPU;
	}

}
