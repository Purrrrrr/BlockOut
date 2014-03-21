package peli.logiikka;

import peli.Peli;

public class TippuvaPalikka extends PalikkaKentalla {
	private int dAlfaXY, dAlfaYZ, dAlfaXZ; // vastapaivaan positiivinen, 0 astetta idassa
	private Peli peli;
	
	/**
	* Huolehtii palikan sijainnista ja siirtelysta.
	* 
	* @param palikka Palikka, jonka sijainnista ja siirtelysta huolehditaan
	* @param kentta Kentta, johon palikka on tippumassa ja johon sen tulee mahtua
	* @param peli Peli, joka paivittaa nakymaa ja joka sisaltaa palikan
	*/
	public TippuvaPalikka(Palikka palikka, Kentta kentta, Peli peli) {
		super(palikka, kentta);
		this.peli = peli;
		
		nollaaPyoritykset();
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
		if (!super.siirra(dx, dy, dz)) {
			return false;
		}
		
		this.peli.paivita();
		return true;
	}
	
	/**
	* Tiputtaa palikan VARATTUjen palojen paalle tai pohjalle.
	*/
	public void tiputaPohjalle() {
		int tiputusKorkeus = 0; //TODO vai oikeasta pohjasta eika siita mihin pystyy tippumaan
		
		while (siirra(0, 0, 1)) {
			tiputusKorkeus++;
		}
		
		annaKentta().jahmetaPalikka(annaPalikka(), annaX(), annaY(), annaZ(), tiputusKorkeus);
		this.peli.haeUusiPalikkaKenttaan();
	}
	
	//*******************************************
	//
	// Tippuvan palikan pyorittaminen
	//
	//*******************************************
	
	/**
	* Kayttajan pyoritettya palikkaa se siirtyy valittomasti minka takia piirtaja on toteutettu toimimaan viiveella. Kulmat dAlfaXY, dAlfaYZ ja dAlfaXZ kertovat kulman minka verran kuva on jaljessa todellisuutta missakin tasossa. Tama metodi oikaisee kuvaa hieman todellisuuden suuntaan aina kun sita kutsutaan.
	*/
	public void oikaisePyoraytysta() {
		//TODO jos lyhyempi matka toiseen suuntaan niin vaihda suunta
		
		if (this.dAlfaXY != 0) {
			this.dAlfaXY += -10*(dAlfaXY/Math.abs(dAlfaXY));
		}
		if (this.dAlfaYZ != 0) {
			this.dAlfaYZ += -10*(dAlfaYZ/Math.abs(dAlfaYZ));
		}
		if (this.dAlfaXZ != 0) {
			this.dAlfaXZ += -10*(dAlfaXZ/Math.abs(dAlfaXZ));
		}
		
		this.peli.paivita();
		if (this.dAlfaXY != 0 || this.dAlfaYZ != 0 || this.dAlfaXZ != 0) {
			new PyoritysAjastin(peli, this, 20);
		}
	}
	
	/**
	* Kertoo minka kulman verran todellisuutta jaljessa kuva on xy-tasossa.
	* 
	* @return Kulma asteina
	*/
	public int annaXYKulma() {
		return this.dAlfaXY;
	}
	
	/**
	* Kertoo minka kulman verran todellisuutta jaljessa kuva on yz-tasossa.
	* 
	* @return Kulma asteina
	*/
	public int annaYZKulma() {
		return this.dAlfaYZ;
	}
	
	/**
	* Kertoo minka kulman verran todellisuutta jaljessa kuva on xz-tasossa.
	* 
	* @return Kulma asteina
	*/
	public int annaXZKulma() {
		return this.dAlfaXZ;
	}
	
	/**
	* Nollaa tippuvan palikan kuvan viiveen.
	*/
	public void nollaaPyoritykset() {
		this.dAlfaXY = 0;
		this.dAlfaYZ = 0;
		this.dAlfaXZ = 0;
	}
	
	/**
	* Pyorittaa palikasta jonkun suunnan tahkon esille.
	* 
	* @param x 1 pyorittaa oikean tahkon esille, -1 vasemman
	* @param y 1 pyorittaa alatahkon esille, -1 ylatahkon
	*/
	public boolean pyoritaSuuntaEsille(int x, int y) {
		if (super.pyoritaSuuntaEsille(x,y)) {
			selvitaPyorityksenAnimointi(x, y);
			return true;
		} else {
			return false;
		}
	}
	
	private void selvitaPyorityksenAnimointi(int x, int y) {
		if (x != 0) {
			dAlfaXZ += 90*x;
		}
		else if (y != 0) {
			dAlfaYZ += -90*y;
		}
		this.peli.paivita();
		
		if (Math.abs(dAlfaXY) + Math.abs(dAlfaYZ) + Math.abs(dAlfaXZ) == 90) {
			new PyoritysAjastin(peli, this, 20);
		}
	}
	
	/**
	* Pyorittaa palikkaa myota-tai vastapaivaan.
	* 
	* @param myotapaivaan Tieto siita pyoritetaanko myotapaivaan vai vastakkaiseen suuntaan
	*/
	public boolean pyoritaMyotapaivaan(boolean myotapaivaan) {
		if (super.pyoritaMyotapaivaan(myotapaivaan)) {
			selvitaPyorityksenAnimointi(myotapaivaan);
			return true;
		} else {
			return false;
		}
	}
	
	private void selvitaPyorityksenAnimointi(boolean myotapaivaan) {
		if (myotapaivaan) {
			dAlfaXY += 90;
		}
		else {
			dAlfaXY += -90;
		}
		this.peli.paivita();
		
		if (Math.abs(dAlfaXY) + Math.abs(dAlfaYZ) + Math.abs(dAlfaXZ) == 90) {
			new PyoritysAjastin(peli, this, 20);
		}
	}
	
}
