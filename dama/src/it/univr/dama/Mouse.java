package it.univr.dama;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class Mouse implements MouseListener{

	private final Scacchiera s;
	private final ArrayList<PedinaGrafica> pedineGrafiche;
	private final Casella[][] caselle;
	private Pedina pSel;
	private Gioco game;
	private Giocatore g1;
	private Giocatore CPU;
	private PannelloSupporto pS;
	
    public Mouse(Scacchiera s, Gioco game, Giocatore CPU, Giocatore g1, PannelloSupporto pS){
    	
    	this.game = game;
    	this.CPU = CPU;
    	this.g1 = g1;
    	this.s = s;
    	this.pS = pS;
    	this.pedineGrafiche = s.getPedineGrafiche();
    	this.caselle = s.getCaselle();
    }
    
	//in base alla casella cliccata sulla scacchiera sceglie l'azione da fare
	public void mouseClicked(MouseEvent e) {
		
	}

	private void mangiaPedina(Pedina pSel, int x, int y) {
		
		rimuoviPedinaMangiata(pSel, x, y);
		
		if(muoviPedina(pSel, x, y)){
			
		//controlla se la pedina puo' mangiare ancora, se si lascia il turno al giocatore che stava muovendo altrimenti passa il turno all'avversario
			if(puoMangiare(pSel, x, y)){
				selezioneCasella(x, y);
				mostraMosseValide(pSel, x, y);
			}
			else
				game.passaTurno();
		}else
			game.passaTurno();
	}

	//funzione che rimuove la pedina appena mangiata calcolandone le coordinate tramite le coordinate della pedina selezionata e della casella blu cliccata
	private void rimuoviPedinaMangiata(Pedina pSel, int x, int y) {
		int xMangiata = (x + pSel.getX())/2;
		int yMangiata = (y + pSel.getY())/2;
		
		//elimina la pedina mangiata
		for(PedinaGrafica pg : pedineGrafiche){
			
			if(pg.getPedina().getX() == xMangiata && pg.getPedina().getY() == yMangiata){
				
				CPU.rimuoviPedina(xMangiata,yMangiata);
				pedineGrafiche.remove(pg);
				break;
			}
		}
		
	}

	//muove la pedina nella posizione passata
	private boolean muoviPedina(Pedina pSel, int x ,int y) {
		pSel.setX(x);
		pSel.setY(y);
		

		// controlla se la pedina è pronta a diventare una dama
		if(pSel.getY() == 0){
			
			//scorre sull'arrayList di pedinegrafiche ed elimina la pedina grafica corrispondente
			for(PedinaGrafica pg : pedineGrafiche){
				
				if(pg.getPedina().getX() == pSel.getX() && pg.getPedina().getY() == pSel.getY()){
					pedineGrafiche.remove(pg);
					break;
				}
			}
			
			//Crea la dama rimuovendo la pedina e con la dama ritornata ne crea la dama grafica e infine passa il turno
			pedineGrafiche.add(new DamaGrafica(g1.creaDama(pSel), s));
			return false;
		}
	return true;
	}

	//se la pedina può essere selezionata la seleziona
	private Pedina selezioneCasella(int x, int y) {
		Pedina p;
		
		//guarda se cè una pedina
		for(PedinaGrafica pg : pedineGrafiche){
			
			//salviamo la pedina per i prossimi utilizzi
			p = pg.getPedina();

				if(p.getX() == x && p.getY() == y){
				
					if(p instanceof Dama)
						//se è una dama
						//evidenzia casella cliccata e la ripristina al colore originale se si clicca un' altra pedina
						caselle[x][y].setColor(Color.CYAN);
					else
						//se è una semplice pedina
						//evidenzia casella cliccata e la ripristina al colore originale se si clicca un' altra pedina
						caselle[x][y].setColor(Color.RED);
					
					//controlla se la casella è selezionabile o se c'è un'altra casella con una pedina che può mangiare
					if(puoMangiare(p,x,y) || nessunaDeveMangiare()) // perche mi dai errore MERDACCIA ?!?!?!?!?!	
						//evidenzia caselle possibili scelte
						mostraMosseValide(p, x, y);
					else{
						pS.cambiaMessaggio("Devi prima mangiare!");
						break;
					}
					
					//ritorniamo la pedina selezionata in modo da poterla muovere
					return p;
				}
		}
		return null;
	}

	// verifica se la pedina selezionata può mangiare una pedina avversaria
	private boolean puoMangiare(Pedina p, int x, int y) {
		
		//se la pedina è bianca
		if(p.getColore() == Color.WHITE){
			
		//se la pedina è una dama
				if(p instanceof Dama){
					
					//scorre tutte le caselle e verifica se sono da evidenziare come possibili mosse selezionata una pedina
					for(int i = 0; i < 8; i++)
						for(int j = 0; j < 8; j++)	{
							
							//(se la casella è occupata da una pedina bianca e non da una dama, quindi sono nera o) se la pedina è una dama e la pedina da mangiare è nera
							if (casellaOccupata(i,j) != null && casellaOccupata(i,j).getColore() == Color.BLACK){
								//guardo se posso mangiare a destra a sinistra, cioè se non c'è una pedina nella casella successiva lungo le diagonali
								
								//lungo la diagonale di destra, controllando di non uscire dalla finestra
								if(caselle[i][j].getX() == x + 1 && caselle[i][j].getY() == y + 1 && casellaOccupata(i+1,j+1) == null && (i+1<8 && j+1<8 && i-1>=0 &&j-1>=0))
									return true;
								
								//lungo la diagonale di sinistra, controllando di non uscire dalla finestra
								if(caselle[i][j].getX() == x - 1 && caselle[i][j].getY() == y + 1 && casellaOccupata(i-1,j+1) == null && (i+1<8 && j+1<8 && i-1>=0 &&j-1>=0))
									return true;
							}
						}	
				}
				
					
					//scorre tutte le caselle e verifica se sono da evidenziare come possibili mosse selezionata una pedina
					for(int i = 0; i < 8; i++)
						for(int j = 0; j < 8; j++)	{
							
							//se la casella è occupata da una pedina nera
							if(casellaOccupata(i,j) != null && casellaOccupata(i,j).getColore() == Color.BLACK && (!(casellaOccupata(i,j) instanceof Dama) || (p instanceof Dama))){
								//guardo se posso mangiare a destra a sinistra, cioè se non c'è una pedina nella casella successiva lungo le diagonali
								
								//lungo la diagonale di destra, controllando di non uscire dalla finestra
								if(caselle[i][j].getX() == x + 1 && caselle[i][j].getY() == y - 1 && casellaOccupata(i+1,j-1) == null && (i+1<8 && j+1<8 && i-1>=0 &&j-1>=0))
									return true;
								
								//lungo la diagonale di sinistra, controllando di non uscire dalla finestra
								if(caselle[i][j].getX() == x - 1 && caselle[i][j].getY() == y - 1 && casellaOccupata(i-1,j-1) == null && (i+1<8 && j+1<8 && i-1>=0 &&j-1>=0))
									return true;
							
						}	
				}
		}
		return false;
	}

	// controlla che nessuna pedina possa mangiare e ritorna false se invece esistono pedine in condizione di mangiarne una avversaria
	private boolean nessunaDeveMangiare() {
		Pedina p;
		
		//guarda se cè una pedina che puo mangiare sulla scacchiera
		for(PedinaGrafica pg : pedineGrafiche){
			p = pg.getPedina();
			if((p.getColore() == Color.WHITE) && puoMangiare(p, p.getX(), p.getY() ))
				return false;
		}
		
		return true;
	}
	
	// controlla che nessuna pedina possa mangiare e ritorna false se invece esistono pedine in condizione di mangiarne una avversaria
	private boolean nessunaDeveMuovere() {
			Pedina p;
			
			//guarda se cè una pedina che puo muovere sulla scacchiera
			for(PedinaGrafica pg : pedineGrafiche){
				p = pg.getPedina();
				if((p.getColore() == Color.WHITE) && puoMuovere(p, p.getX(), p.getY()))
					return false;
			}
			
			return true;
		}

	private boolean puoMuovere(Pedina p, int x, int y) {
		if(p.getColore() == Color.BLACK || (p instanceof Dama)){
			
			//scorre tutte le caselle e verifica se sono da evidenziare come possibili mosse selezionata una pedina
			for(int i = 0; i < 8; i++)
				for(int j = 0; j < 8; j++)	{
					
					//se la casella non è occupata da alcuna pedina
					if(casellaOccupata(i,j) == null)
						if((caselle[i][j].getX() == x + 1 || caselle[i][j].getX() == x - 1) && caselle[i][j].getY() == y + 1)
							return true;
			}
		}
		if(p.getColore() == Color.WHITE){
			
			//scorre tutte le caselle e verifica se sono da evidenziare come possibili mosse selezionata una pedina
			for(int i = 0; i < 8; i++)
				for(int j = 0; j < 8; j++)	{
					
					//se la casella non è occupata da alcuna pedina
					if(casellaOccupata(i,j) == null)
						if((caselle[i][j].getX() == x + 1 || caselle[i][j].getX() == x - 1) && caselle[i][j].getY() == y - 1)
							return true;
			}
		}
		return false;
	}

	// ripristina la scacchiera ai colori originali
	private void ripristinaScacchiera(Casella[][] caselle) {
		for (int i = 0; i < 8; i++)
			for(int j = 0; j < 8; j++)
				caselle[i][j].resetColor();
	}

	// mostra le mosse possibili della pedina selezionata
	private void mostraMosseValide(Pedina p, int x, int y) {
		
		//se la pedina è bianca
		if(p.getColore() == Color.WHITE){
		
			//se la pedina è una dama
			if(p instanceof Dama){
			
			//scorre tutte le caselle e verifica se sono da evidenziare come possibili mosse selezionata una pedina
			for(int i = 0; i < 8; i++)
				for(int j = 0; j < 8; j++)	{
					
					//se la casella non è occupata da alcuna pedina
					if(casellaOccupata(i,j) == null)
						if((caselle[i][j].getX() == x + 1 || caselle[i][j].getX() == x - 1) && caselle[i][j].getY() == y + 1)
							caselle[i][j].setColor(Color.GREEN);
				
					//(se la casella è occupata da una pedina bianca e non da una dama, quindi da una pedina nera o) se la pedina è una dama e la pedina da mangiare è nera
					if(casellaOccupata(i,j) != null && casellaOccupata(i,j).getColore() == Color.BLACK){
						//guardo se posso mangiare a destra a sinistra, cioè se non c'è una pedina nella casella successiva lungo le diagonali
						
						//lungo la diagonale di destra, controllando di non uscire dalla finestra
						if(caselle[i][j].getX() == x + 1 && caselle[i][j].getY() == y + 1 && casellaOccupata(i+1,j+1) == null && (i+1<8 && j+1<8 && i-1>=0 &&j-1>=0))
							caselle[i+1][j+1].setColor(Color.BLUE);
						
						//lungo la diagonale di sinistra, controllando di non uscire dalla finestra
						if(caselle[i][j].getX() == x - 1 && caselle[i][j].getY() == y + 1 && casellaOccupata(i-1,j+1) == null && (i+1<8 && j+1<8 && i-1>=0 &&j-1>=0))
							caselle[i-1][j+1].setColor(Color.BLUE);
					}
				}	
		}
			
			//scorre tutte le caselle e verifica se sono da evidenziare come possibili mosse selezionata una pedina
			for(int i = 0; i < 8; i++)
				for(int j = 0; j < 8; j++)	{
					
					//se la casella non è occupata da alcuna pedina
					if(casellaOccupata(i,j) == null)
						if((caselle[i][j].getX() == x + 1 || caselle[i][j].getX() == x - 1) && caselle[i][j].getY() == y - 1)
							caselle[i][j].setColor(Color.GREEN);
					
					//se la casella è occupata da una pedina nera
					if(casellaOccupata(i,j) != null && casellaOccupata(i,j).getColore() == Color.BLACK && (!(casellaOccupata(i,j) instanceof Dama) || (p instanceof Dama))){
						//guardo se posso mangiare a destra a sinistra, cioè se non c'è una pedina nella casella successiva lungo le diagonali
						
						//lungo la diagonale di destra, controllando di non uscire dalla finestra
						if(caselle[i][j].getX() == x + 1 && caselle[i][j].getY() == y - 1 && casellaOccupata(i+1,j-1) == null && (i+1<8 && j+1<8 && i-1>=0 &&j-1>=0))
							caselle[i+1][j-1].setColor(Color.BLUE);
						
						//lungo la diagonale di sinistra, controllando di non uscire dalla finestra
						if(caselle[i][j].getX() == x - 1 && caselle[i][j].getY() == y - 1 && casellaOccupata(i-1,j-1) == null && (i+1<8 && j+1<8 && i-1>=0 &&j-1>=0))
							caselle[i-1][j-1].setColor(Color.BLUE);
					}
				}	
		}
		
		//infine se una pedina può mangiare non deve poter muovere senza mangiare
		for(int i = 0; i < 8; i++)
			for(int j = 0; j < 8; j++)
				if( caselle[i][j].getColor() == Color.BLUE )
					for(int h = 0; h < 8; h++)
						for(int k = 0; k < 8; k++)
							if( caselle[h][k].getColor() == Color.GREEN )
								caselle[h][k].resetColor();
							
			
	}

	private Pedina casellaOccupata(int x, int y) {
		
		//fa un ciclo sull'arrayList di pedine grafiche per vedere se c'è una occupa la casella in questione
		ArrayList<PedinaGrafica> pedineGrafiche = s.getPedineGrafiche();
		for(PedinaGrafica pg : pedineGrafiche)
			if(x == pg.getPedina().getX() && y == pg.getPedina().getY())
				return pg.getPedina();
		return null;
	}
	
	public boolean nonHaMosseDisponibili(){
		if(nessunaDeveMangiare() && nessunaDeveMuovere())
			return true;
		return false;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	//rileva la pressione del mouse in una determinata zona della scacchiera o del menu'
	@Override
	public void mousePressed(MouseEvent e) {
		
		//se sulla schermata c'è l'immagine di informazione o di istruzioni la toglie
		if( s.immagineUgualeInfoIstru() ){
			s.immagine = null;
			s.repaint();
		}

		//se è il turno del giocatore1
		if(game.getTurno() == Color.WHITE && game.getFinito() != true){
			
			pS.cambiaMessaggio("Gioca");
			int x = e.getX()/(s.getWidth()/8);
			int y = e.getY()/(s.getHeight()/8);
			System.out.println(x + "," + y); //(coordinate in pixel)/dim in pixel della casella
	
		
			if(caselle[x][y].getColor() != Color.GREEN && caselle[x][y].getColor() != Color.BLUE){
			
				ripristinaScacchiera(caselle); // ricolora tutte le caselle con i colori originali
			
				pSel = selezioneCasella(x, y); //evidenzia in rosso la casella cliccata dal mouse e in verde le possibili scelte per le mosse
		
			}
			else if(caselle[x][y].getColor() != Color.BLUE){
			
				ripristinaScacchiera(caselle); // ricolora tutte le caselle con i colori originali
			
				muoviPedina(pSel, x, y); // muove la pedina nella casella selezionata
				game.passaTurno();
			}
			
			else{
			
				ripristinaScacchiera(caselle); // ricolora tutte le caselle con i colori originali
			
				mangiaPedina(pSel, x, y); // mangia la pedina e si sposta
			
			}
		
			s.repaint(); //ridipinge la scacchiera
		}
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


}
