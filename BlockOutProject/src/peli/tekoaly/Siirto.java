package peli.tekoaly;

import peli.logiikka.TippuvaPalikka;

public enum Siirto {

	VASEN(-1,0,0,0,0),
	OIKEA(1,0,0,0,0),
	YLOS(0,-1,0,0,0),
	ALAS(0,1,0,0,0),
	MYOTAPAIVA(0,0,1,0,0),
	VASTAPAIVA(0,0,-1,0,0),
	VASENESILLE(0,0,0,-1,0),
	OIKEAESILLE(0,0,0,1,0),
	ALAESILLE(0,0,0,0,-1),
	YLAESILLE(0,0,0,0,1);

	private final int dx,dy,r,px,py;

	/**
	* Tekee joko siirron tai pyörityksen
	* 
	* @param dx Siirtomäärä x-akselilla
	* @param dy Siirtomäärä y-akselilla
	* @param r  Kiertomäärä myötäpäivään. Joko 1 tai -1
	* @param px 1 pyorittaa oikean tahkon esille, -1 vasemman
	* @param py 1 pyorittaa alatahkon esille, -1 ylatahkon
	*/
	Siirto(int dx, int dy, int r, int px, int py) {
		this.dx = dx;
		this.dy = dy;
		this.px = px;
		this.py = py;
		this.r = r;
	}
	public void suorita(TippuvaPalikka palikka) {
		if (px != 0 || py != 0) {
			palikka.pyoritaSuuntaEsille(px,py);
			return;
		}
		if (dx != 0 || dy != 0) {
			palikka.siirra(dx,dy);
			return;
		}
		if (r != 0) {
			palikka.pyoritaMyotapaivaan(r == 1);
		}
	}

}
