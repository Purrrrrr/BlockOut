package peli.tekoaly;

import peli.logiikka.PalikkaKentalla;
import java.util.Random;

public enum Siirto {

	MYOTAPAIVA(0,0,0,1,0,0),
	VASTAPAIVA(0,0,0,-1,0,0),
	VASENESILLE(0,0,0,0,-1,0),
	OIKEAESILLE(0,0,0,0,1,0),
	ALAESILLE(0,0,0,0,0,-1),
	YLAESILLE(0,0,0,0,0,1), 
	VASEN(-1,0,0,0,0,0),
	OIKEA(1,0,0,0,0,0),
	YLOS(0,-1,0,0,0,0),
	ALAS(0,1,0,0,0,0);
	/*
	 */

	private static final Random RND = new Random();

	private final int dx,dy,dz,r,px,py;

	/**
	* Tekee joko siirron tai pyörityksen
	* 
	* @param dx Siirtomäärä x-akselilla
	* @param dy Siirtomäärä y-akselilla
	* @param dz Siirtomäärä z-akselilla
	* @param r  Kiertomäärä myötäpäivään. Joko 1 tai -1
	* @param px 1 pyorittaa oikean tahkon esille, -1 vasemman
	* @param py 1 pyorittaa alatahkon esille, -1 ylatahkon
	*/
	Siirto(int dx, int dy, int dz, int r, int px, int py) {
		this.dx = dx;
		this.dy = dy;
		this.dz = dz;
		this.px = px;
		this.py = py;
		this.r = r;
	}
	public boolean suorita(PalikkaKentalla palikka) {
		if (px != 0 || py != 0) {
			return palikka.pyoritaSuuntaEsille(px,py);
		}
		if (dx != 0 || dy != 0 || dz != 0) {
			return palikka.siirra(dx,dy,dz);
		}
		if (r != 0) {
			return palikka.pyoritaMyotapaivaan(r == 1);
		}
		return false;
	}

	public static Siirto satunnainenSiirto() {
		int pick = RND.nextInt(Siirto.values().length);
		return Siirto.values()[pick];
	}
}
