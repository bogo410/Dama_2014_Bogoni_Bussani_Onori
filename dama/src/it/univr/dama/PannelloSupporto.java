package it.univr.dama;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class PannelloSupporto extends JPanel{
	
	private static final int WIDTH = 576;
	private static final int HEIGHT = 20;
	private String diff="";
	
	//etichette
	JLabel etichettaDiff = new JLabel("");
	JLabel etichettaMess = new JLabel("Inizia la Partita!"+"   ");
	
	public PannelloSupporto() {
		
		setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(WIDTH,HEIGHT));
		add(etichettaDiff, BorderLayout.WEST);
		add(etichettaMess, BorderLayout.EAST);
		this.setVisible(true);
	}
	
	//funzione utilizzata per aggiornare la difficoltà in base alla scelta nel menù cambia difficoltà
	public void aggiornaDifficolta(){
		if(ControlloreCPU.difficolta == 0)
			diff="Facile";
		else if(ControlloreCPU.difficolta == 1)
			diff="Normale";
		else
			diff="Difficile";
		etichettaDiff.setText("  Difficoltà: "+diff);
	}

	//cambia il testo del messaggio con quello passato
	public void cambiaMessaggio(String messaggio) {
		etichettaMess.setText(messaggio+"   ");
		
	}

}
