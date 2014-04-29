package it.univr.dama;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class Finestra extends JFrame{
	
	private static final int WIDTH = 576;
	private static final int HEIGHT = 640;
	private Scacchiera s;
	private Gioco game;
	private MenuListener menuListener;
	
	//Menu'
	private JMenuBar barraMenu;
	private JMenu menu;
	private JMenuItem nuovaPartita;
	private JMenu cambiaDifficolta;
	private JMenuItem esci;
	private JMenuItem facile;
	private JMenuItem normale;
	private JMenuItem difficile;
	private JMenu altro;
	private JMenuItem info;
	private JMenuItem istruzioni;
	
	//pannello di supporto
	private PannelloSupporto pS = new PannelloSupporto();
	
	//Costruttore Finestra
	public Finestra(String titolo){
		
		//setta il titolo
		this.setTitle(titolo);
		
		//si salva la scacchiera
		s = new Scacchiera();
		
		//crea il Menu'
		barraMenu = new JMenuBar();
		menu = new JMenu("Menù");
		nuovaPartita = new JMenuItem("Nuova Partita");
		cambiaDifficolta = new JMenu("Cambia Difficoltà");
		esci = new JMenuItem("Esci");
		facile = new JMenuItem("Facile");
		normale = new JMenuItem("Normale");
		difficile = new JMenuItem("Difficile");
		altro = new JMenu("Altro");
		info = new JMenuItem("Informazioni");
		istruzioni = new JMenuItem("Istruzioni");
		
		//crea il nuovo gioco
		game = new Gioco( this, pS);
		
		//aggiuta dell'actionListener del menù ai vari componenti
		nuovaPartita.addActionListener(menuListener = new MenuListener(nuovaPartita, facile, normale, difficile, info, esci, istruzioni, game, this));
		facile.addActionListener(menuListener);
		normale.addActionListener(menuListener);
		difficile.addActionListener(menuListener);
		esci.addActionListener(menuListener);
		info.addActionListener(menuListener);
		istruzioni.addActionListener(menuListener);
		
		setPreferredSize(new Dimension( WIDTH, HEIGHT ));
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(s, BorderLayout.CENTER);
		
		//aggiunge il Menu'
		barraMenu.add(menu);
		barraMenu.add(altro);
		menu.add(nuovaPartita);
		menu.add(cambiaDifficolta);
		menu.add(esci);
		cambiaDifficolta.add(facile);
		cambiaDifficolta.add(normale);
		cambiaDifficolta.add(difficile);
		altro.add(info);
		altro.add(istruzioni);
		add(barraMenu, BorderLayout.NORTH);
		
		//aggiunge il pannello di supporto
		add(pS, BorderLayout.SOUTH);
		
		//setta le dimensioni in modo corretto(adatta i pannelli nella finestra)
		pack();
		this.setResizable(false);
		
	}
	
	//metodo che ritorna la scacchiera creata
	public Scacchiera getScacchiera(){
		return s;
	}

}
