package peli.grafiikka;

import peli.asetukset.logiikka.Ulottuvuudet;
import peli.Peli;
import peli.Koordinaatti;
import peli.logiikka.Pala;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

public class Reunapiirturi {
	private Image valimuisti;
	private Piste3DHaku piste;
	int syvyys, korkeus, leveys;
	
	/**
	* Piirtaa pelattavan kentan kuilujen seinamat.
	* 
	* @param piste3DHaku 3D-pisteiden koordinaattien hakija
	*/
	public Reunapiirturi(Peli p, int ruudunLeveys, int ruudunKorkeus, Ulottuvuudet ulottuvuudet, Piste3DHaku piste3DHaku) {

		/* Alustetaan kentän leveys, korkeus ja syvyys. 
		 * Lisätään samalla arvoihin reunan vaatimat mitat */
		leveys = ulottuvuudet.annaLeveys() + 2;
		korkeus = ulottuvuudet.annaKorkeus() + 2;
		syvyys = ulottuvuudet.annaSyvyys() + 1;
		piste = piste3DHaku;

		valimuisti = p.createImage(ruudunLeveys,ruudunKorkeus);
		Graphics g = valimuisti.getGraphics();
		piirraValimuistiin(g);
		g.dispose();
	}
	
	public void piirra(Graphics g) {
		g.drawImage(valimuisti, 0,0, null);
	}

	/**
	* Piirtaa pelattavan kentan kuilujen seinamat.
	* 
	* @param g Graphics
	*/
	public void piirraValimuistiin(Graphics g) {
		g.setColor(Color.GREEN.darker());
		
		piirraPohja(g, leveys, korkeus, syvyys);
		
		piirraSivut(g, leveys, korkeus, syvyys);
	}
	
	private void piirraPohja(Graphics g, int leveys, int korkeus, int syvyys) {
		for (int j=0; j<korkeus; j++) {
			for (int i=0; i<leveys; i++) {
				//pystyviivat
				if (j>0 && j<korkeus-1) {
					if (i< leveys/2.0) {
						piirraSivu(g, i, j, syvyys-1, 1, 0, -1);
					}
					else if (i> leveys/2.0) {
						piirraSivu(g, i, j, syvyys-1, -1, 0, -1);
					}
				}
				
				//vaakaviivat
				if (i>0 && i<leveys-1) {
					if (j< korkeus/2.0) {
						piirraSivu(g, i, j, syvyys-1, 0, 1, -1);
					}
					else if (j> korkeus/2.0) {
						piirraSivu(g, i, j, syvyys-1, 0, -1, -1);
					}
				}
			}
		}
	}
	
	private void piirraSivut(Graphics g, int leveys, int korkeus, int syvyys) {
		piirraVasenSivu(g, korkeus, syvyys);
		piirraOikeaSivu(g, leveys, korkeus, syvyys);
		piirraAlasivu(g, leveys, korkeus, syvyys);
		piirraYlasivu(g, leveys, syvyys);
	}
	
	private void piirraVasenSivu(Graphics g, int korkeus, int syvyys) {
		for (int k=syvyys-2; k>=0; k--) {
			for (int j=0; j<korkeus; j++) {
				if (j< korkeus/2.0) {
					piirraSivu(g, 0, j, k, 1, 1, 0);
				}
				else {
					piirraSivu(g, 0, j, k, 1, -1, 0);
				}
				
				if (j>0 && j<korkeus-1) {
					piirraSivu(g, 0, j, k, 1, 0, -1);
				}
			}
		}
	}
	
	private void piirraOikeaSivu(Graphics g, int leveys, int korkeus, int syvyys) {
		for (int k=syvyys-2; k>=0; k--) {
			for (int j=0; j<korkeus; j++) {
				if (j< korkeus/2.0) {
					piirraSivu(g, leveys-1, j, k, -1, 1, 0);
				}
				else {
					piirraSivu(g, leveys-1, j, k, -1, -1, 0);
				}
				
				if (j>0 && j<korkeus-1) {
					piirraSivu(g, leveys-1, j, k, -1, 0, -1);
				}
			}
		}
	}
	
	private void piirraAlasivu(Graphics g, int leveys, int korkeus, int syvyys) {
		for (int k=syvyys-2; k>=0; k--) {
			for (int i=0; i<leveys; i++) {
				if (i< leveys/2.0) {
					piirraSivu(g, i, korkeus-1, k, 1, -1, 0);
				}
				else {
					piirraSivu(g, i, korkeus-1, k, -1, -1, 0);
				}
				
				if (i>0 && i<leveys-1) {
					piirraSivu(g, i, korkeus-1, k, 0, -1, -1);
				}
			}
		}
	}
	
	private void piirraYlasivu(Graphics g, int leveys, int syvyys) {
		for (int k=syvyys-2; k>=0; k--) {
			for (int i=0; i<leveys; i++) {
				if (i< leveys/2.0) {
					piirraSivu(g, i, 0, k, 1, 1, 0);
				}
				else {
					piirraSivu(g, i, 0, k, -1, 1, 0);
				}
				
				if (i>0 && i<leveys-1) {
					piirraSivu(g, i, 0, k, 0, 1, -1);
				}
			}
		}
	}
	
	private void piirraSivu(Graphics g, int i, int j, int k, int xSivu, int ySivu, int zSivu) {
		
		if (xSivu==0) {
			Koordinaatti vasenKoordinaatti = piste.koordinaatit(i, j, k, -1, ySivu, zSivu);
			Koordinaatti oikeaKoordinaatti = piste.koordinaatit(i, j, k, 1, ySivu, zSivu);
			
			g.drawLine(vasenKoordinaatti.annaX(), vasenKoordinaatti.annaY(), oikeaKoordinaatti.annaX(), oikeaKoordinaatti.annaY());
		}
		
		else if (ySivu==0) {
			Koordinaatti ylaKoordinaatti = piste.koordinaatit(i, j, k, xSivu, -1, zSivu);
			Koordinaatti alaKoordinaatti = piste.koordinaatit(i, j, k, xSivu, 1, zSivu);
			
			g.drawLine(ylaKoordinaatti.annaX(), ylaKoordinaatti.annaY(), alaKoordinaatti.annaX(), alaKoordinaatti.annaY());
		}
		
		else if (zSivu==0) {
			Koordinaatti ylaKoordinaatti = piste.koordinaatit(i, j, k, xSivu, ySivu, -1);
			Koordinaatti alaKoordinaatti = piste.koordinaatit(i, j, k, xSivu, ySivu, 1);
			
			g.drawLine(ylaKoordinaatti.annaX(), ylaKoordinaatti.annaY(), alaKoordinaatti.annaX(), alaKoordinaatti.annaY());
		}
	}
}
