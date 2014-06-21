package peli.logiikka;

import peli.Koordinaatti;

public class PalikkaKentalla {
	private Palikka palikka;
	private int x, y, z;
	private Kentta kentta;

	/**
	* Huolehtii palikan sijainnista ja siirtelysta.
	* 
	* @param palikka Palikka, jonka sijainnista ja siirtelysta huolehditaan
	* @param kentta Kentta, johon palikka on tippumassa ja johon sen tulee mahtua
	*/
	public PalikkaKentalla(Palikka palikka, Kentta kentta) {
		this.palikka = palikka;
		this.kentta = kentta;
		
		this.x = (kentta.annaLeveys()+2) / 2;
		this.y = (kentta.annaKorkeus()+2) / 2;
		this.z = 0;
		
		mahdutaPalikkaKenttaan();
	}
	private PalikkaKentalla(Palikka palikka, Kentta kentta, int x, int y, int z) {
		this.palikka = palikka;
		this.kentta = kentta;
		
		this.x = x;
		this.y = y;
		this.z = z;
	}
	public PalikkaKentalla(PalikkaKentalla toinen) {
		this.palikka = new Palikka(toinen.palikka);
		this.kentta = toinen.kentta;
		
		this.x = toinen.x;
		this.y = toinen.y;
		this.z = toinen.z;
	}
	
	private void mahdutaPalikkaKenttaan() {
		mahdutaPyorayttamallaKenttaan();
		mahdutaSiirtamallaKenttaan();
	}
	
	private void mahdutaPyorayttamallaKenttaan() {
		if (palikka.annaLeveys() > kentta.annaLeveys() || palikka.annaKorkeus() > kentta.annaKorkeus()) {
			pyoritaSuuntaEsille(0, 1);
			if (mahtuukoPalikkaKenttaan(0, 0, 0)) {
				return;
			}
			
			pyoritaSuuntaEsille(1, 0);
			if (mahtuukoPalikkaKenttaan(0, 0, 0)) {
				return;
			}
		}
	}
	
	private void mahdutaSiirtamallaKenttaan() {
		if (!mahtuukoPalikkaKenttaan(0, 0, 0)) {
			if (siirra(-1, 0, 0)) {
				return;
			}
			if (siirra(-1, 1, 0)) {
				return;
			}
			
			System.out.println("Palikkaa ei saatu mahtumaan kenttaan. Ei pitaisi tapahtua jos kyseessa ei ole itse tehty palikka.");
		}
	}
	
	/**
	* Antaa tippuvan palikan keskipisteen x-koordinaatin
	* 
	* @return X-koordinaatti ruuduissa
	*/
	public int annaX() {
		return this.x;
	}
	
	/**
	* Antaa tippuvan palikan keskipisteen y-koordinaatin
	* 
	* @return Y-koordinaatti ruuduissa
	*/
	public int annaY() {
		return this.y;
	}
	
	/**
	* Antaa tippuvan palikan keskipisteen z-koordinaatin
	* 
	* @return Z-koordinaatti ruuduissa
	*/
	public int annaZ() {
		return this.z;
	}
	
	/**
	* Antaa tippuvan palikan ilman tippumisen tai pyorimisen tietoja.
	* 
	* @return Palikka
	*/
	public Palikka annaPalikka() {
		return this.palikka;
	}

	/**
	* Antaa kentän jossa palikka on.
	* 
	* @return Kentta
	*/
	public Kentta annaKentta() {
		return this.kentta;
	}
	
	//*******************************************
	//
	// Tippuvan palikan siirtaminen
	//
	//*******************************************

	/**
	* Siirtaa kayttajan toimien perusteella tippuvaa palikkaa kun se on mahdollista.
	* 
	* @param dx Siirrettava matka x-suunnassa
	* @param dy Siirrettava matka y-suunnassa
	*/
	//vain siirrot, kentta hoitaa tippuvien palikoiden jahmettamisen
	public void siirra(int dx, int dy) {
		siirra(dx, dy, 0);
	}

	/**
	* Siirtaa kayttajan tai pelin perusteella tippuvaa palikkaa kun se on mahdollista.
	* 
	* @param dx Siirrettava matka x-suunnassa
	* @param dy Siirrettava matka y-suunnassa
	* @param dz Siirrettava matka z-suunnassa
	* 
	* @return Tieto siita pystyttiinko palikkaa siirtamaan.
	*/
	public boolean siirra(int dx, int dy, int dz) {
		if (!mahtuukoPalikkaKenttaan(dx, dy, dz)) {
			return false;
		}
		
		this.x += dx;
		this.y += dy;
		this.z += dz;
		
		return true;
	}

	/**
	* Pyorittaa palikasta jonkun suunnan tahkon esille.
	* 
	* @param x 1 pyorittaa oikean tahkon esille, -1 vasemman
	* @param y 1 pyorittaa alatahkon esille, -1 ylatahkon
	*/
	public boolean pyoritaSuuntaEsille(int x, int y) {
		this.palikka.pyoritaSuuntaEsille(x, y);
		
		if (mahtuukoPalikkaKenttaan(0, 0, 0)) {
			return true;
		}
		
		if (siirtelyMahdollistaaPyorityksen()) {
			return true;
		}
		else {
			this.palikka.pyoritaSuuntaEsille(-x, -y);
			return false;
		}
	}

	/**
	* Pyorittaa palikkaa myota-tai vastapaivaan.
	* 
	* @param myotapaivaan Tieto siita pyoritetaanko myotapaivaan vai vastakkaiseen suuntaan
	*/
	public boolean pyoritaMyotapaivaan(boolean myotapaivaan) {
		this.palikka.pyoritaMyotapaivaan(myotapaivaan);
		
		if (mahtuukoPalikkaKenttaan(0, 0, 0)) {
			return true;
		}
		
		if (siirtelyMahdollistaaPyorityksen()) {
			return true;
		}
		else {
			this.palikka.pyoritaMyotapaivaan(!myotapaivaan);
			return false;
		}
	}

	//*******************************************
	//
	// siirtelyita pyorittamisen mahdollistamiseksi
	//
	//*******************************************
	
	private boolean siirtelyMahdollistaaPyorityksen() {
		if (yksinkertainenSiirtelyMahdollistaaPyorityksen()) {
			return true;
		}
		if (lyhytKulmasiirtelyMahdollistaaPyorityksen()) {
			return true;
		}
		if (kahdenVerranSiirtelyMahdollistaaPyorityksen()) {
			return true;
		}
		return false;
	}
	
	private boolean yksinkertainenSiirtelyMahdollistaaPyorityksen() {
		for (int dx = -1; dx <= 1; dx += 2) {
			if (mahtuukoPalikkaKenttaan(dx, 0, 0)) {
				this.x += dx;
				return true;
			}
		}
		for (int dy = -1; dy <= 1; dy += 2) {
			if (mahtuukoPalikkaKenttaan(0, dy, 0)) {
				this.y += dy;
				return true;
			}
		}
		
		return false;
	}
	
	private boolean lyhytKulmasiirtelyMahdollistaaPyorityksen() {
		for (int dx = -1; dx <= 1; dx += 2) {
			for (int dy = -1; dy <= 1; dy += 2) {
				
				if (mahtuukoPalikkaKenttaan(dx, dy, 0)) {
					this.x += dx;
					this.y += dy;
					return true;
				}
			
			}
		}
		
		return false;
	}
	
	private boolean kahdenVerranSiirtelyMahdollistaaPyorityksen() {
		for (int dx = -2; dx <= 2; dx += 4) {
			if (mahtuukoPalikkaKenttaan(dx, 0, 0)) {
				this.x += dx;
				return true;
			}
		}
		for (int dy = -2; dy <= 2; dy += 4) {
			if (mahtuukoPalikkaKenttaan(0, dy, 0)) {
				this.y += dy;
				return true;
			}
		}
		
		return false;
	}

	public boolean equals(Object other) {
		if (!(other instanceof PalikkaKentalla)) return false;

		PalikkaKentalla that = (PalikkaKentalla)other;

		if (that.y != this.y)  return false;
	  if (that.x != this.x) return false;
		if (that.z != this.z) return false;

		return this.palikka.equals(that.palikka);
		/**/
	}

	public int hashCode() {
		int code = new Koordinaatti(x,y,z).hashCode() + this.palikka.hashCode();

		return code;
	}
	
	// kenttaan mahtuminen
	
	private boolean mahtuukoPalikkaKenttaan(int dx, int dy, int dz) {
		int keskipiste = this.palikka.annaKeskipiste();

		return kentta.mahtuvatkoPalatKenttaan(this.palikka.annaPalaKoordinaatit(), x+dx-keskipiste, y+dy-keskipiste, z+dz-keskipiste);
	}
}
