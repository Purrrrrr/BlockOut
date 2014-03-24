package peli.logiikka;

import peli.Koordinaatti;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Deque;
import java.util.LinkedList;

public class Palikka {
	private int koko;
	private int alapisteet, ylapisteet, palojenMaara;

	/** Palikan kaikki pyöräytetyt versiot hashcoden mukaan talletettuna */
	private HashMap<Integer, Pala[][][]> pyoritetytVersiot;
	/** Muunnos hashcodesta ja pyöräytyksestä pyöräytetyn palan hashcodeen */
	private HashMap<Integer, HashMap<PalikkaPyorayttaja.Pyoraytys, Integer>> pyoraytykset;
	/** Palikan särmäkoordinaatin hashcoden mukaan talletettuna */
	private HashMap<Integer, HashMap<Koordinaatti, ArrayList<Koordinaatti>>> pyoraytetytSarmat;
	
	/** Tämänhetkinen palikka */
	private Pala[][][] palikka;
	/** Tämänhetkiset särmäkoordinaatit */
	private HashMap<Koordinaatti, ArrayList<Koordinaatti>> sarmat;
	int hashcode = 0;
	
	/**
	* Hallinnoi yhden palikan sisaista rakennetta ja palikkaan liittyvia peruspisteita. Voidaan luoda 3x3x3 palikoita.
	* 
	* @param alapisteet Alemmat peruspisteet
	* @param ylapisteet Ylemmat peruspisteet
	* @param koordinaatit Palojen koordinaatit taulukkona, jossa on kolmella jaollinen lukumäärä koordinaatteja
	*/
	public Palikka(int alapisteet, int ylapisteet, int koordinaatit[]) {
		this(5, alapisteet, ylapisteet, koordinaatit);
	}
	
	/**
	* Hallinnoi yhden palikan sisaista rakennetta ja palikkaan liittyvia peruspisteita.
	* 
	* @param koko Palikan leveys/korkeus/syvyys
	* @param alapisteet Alemmat peruspisteet
	* @param ylapisteet Ylemmat peruspisteet
	* @param koordinaatit Palojen koordinaatit taulukkona, jossa on kolmella jaollinen lukumäärä koordinaatteja
	*/
	public Palikka(int koko, int alapisteet, int ylapisteet, int koordinaatit[]) {
		this(koko, alapisteet, ylapisteet);
		if (koordinaatit.length%3 != 0) {
			throw new IllegalArgumentException();
		}
		for(int i = 0; i < koordinaatit.length; i+=3) {
			lisaaPala(koordinaatit[i], koordinaatit[i+1], koordinaatit[i+2]);
		}
		
		alustaPyoraytykset();
		hashcode = laskeHashCode(this.palikka);
		this.sarmat = pyoraytetytSarmat.get(hashcode);
		
	}
	private Palikka(int koko, int alapisteet, int ylapisteet) {
		if (koko%2 == 0) {
			koko++;
		}
		this.koko = koko;
		this.palikka = new Pala[this.koko][this.koko][this.koko];
		palikanTyhjaksiAlustus(this.palikka);
		
		this.alapisteet = alapisteet;
		this.ylapisteet = ylapisteet;
		this.palojenMaara = 0;
		
		this.sarmat = new HashMap<Koordinaatti, ArrayList<Koordinaatti>>();
	}
	
	private void palikanTyhjaksiAlustus(Pala[][][] palikka) {
		for (int i=0; i<koko; i++) {
			for (int j=0; j<koko; j++) {
				for (int k=0; k<koko; k++) {
					palikka[i][j][k] = Pala.TYHJA;
				}
			}
		}
	}
	
	/**
	* Lisaa palikkaan palan.
	* 
	* @param x Palan x-koordinaatti
	* @param y Palan y-koordinaatti
	* @param z Palan z-koordinaatti
	* @return Tieto siita onnistuiko palikan lisaaminen vai ei
	*/
	private void lisaaPala(int x, int y, int z) {
		if (this.palikka[x-1][y-1][z-1] == Pala.TIPPUVA) {
			return;
		}
		
		this.palikka[x-1][y-1][z-1] = Pala.TIPPUVA;
		this.palojenMaara++;
	}

	private void alustaPyoraytykset() {
		pyoritetytVersiot = new HashMap<Integer, Pala[][][]>();
		pyoraytykset = new HashMap<Integer, HashMap<PalikkaPyorayttaja.Pyoraytys, Integer>>();
		pyoraytetytSarmat = new HashMap<Integer, HashMap<Koordinaatti, ArrayList<Koordinaatti>>>();

		PalikkaPyorayttaja.Pyoraytys[] pyoritykset = PalikkaPyorayttaja.Pyoraytys.values();

		Pala[][][] juuri = palikka;
		
		Deque<Pala[][][]> tutkittavat = new LinkedList<Pala[][][]>();
		tutkittavat.add(juuri);
		
		/*
		deb(juuri);
		/**/

		while(!tutkittavat.isEmpty()) {
			Pala[][][] tutkittava = tutkittavat.pop();

			int tutkittavanHashcode = laskeHashCode(tutkittava);
			pyoritetytVersiot.put(tutkittavanHashcode, tutkittava);

			//System.out.println("Tutkittava: "+tutkittavanHashcode);
			HashMap<PalikkaPyorayttaja.Pyoraytys, Integer> palikanPyoraytykset;
			palikanPyoraytykset = new HashMap<PalikkaPyorayttaja.Pyoraytys, Integer>();
			pyoraytykset.put(tutkittavanHashcode, palikanPyoraytykset);

			for(PalikkaPyorayttaja.Pyoraytys p :  pyoritykset) {
				PalikkaPyorayttaja pyorayttaja = new PalikkaPyorayttaja(tutkittava);
				Pala[][][] uusi = pyorayttaja.pyorita(p);
				int pyoraytetynHashcode = laskeHashCode(uusi);

				if (!pyoritetytVersiot.containsKey(pyoraytetynHashcode)) {
					pyoritetytVersiot.put(pyoraytetynHashcode, uusi);
					tutkittavat.add(uusi);
				}
				palikanPyoraytykset.put(p, pyoraytetynHashcode);

			}
		}
		for (Map.Entry<Integer, Pala[][][]> entry : pyoritetytVersiot.entrySet()) {
			Integer hc = entry.getKey();
			Pala[][][] pyoritettypalikka = entry.getValue();

			Kulmahaku kulmahaku = new Kulmahaku(pyoritettypalikka);
			pyoraytetytSarmat.put(hc, kulmahaku.haeSarmat());

			/*
			System.out.println("Pyöräytys! "+hc);
			HashMap<PalikkaPyorayttaja.Pyoraytys, Integer> p = pyoraytykset.get(hc);
			if (p == null) {
				System.out.println("!!");
			} else {
				for (Integer i : p.values()) {
					System.out.print(i+",");
				}
				System.out.println();
			}
			*/
		}
	}

	private int laskeHashCode(Pala[][][] laskettava) {
		int code = 0;
		int exp = 1;

		for (int k=0; k<laskettava[0][0].length; k++) {
			for (int j=0; j<laskettava[0].length; j++) {
				for (int i=0; i<laskettava.length; i++) {
					if (laskettava[i][j][k] == Pala.TIPPUVA) {
						int subcode = i<<8 + j<<4 + k;
						code += exp * subcode;
					}
					exp *= 31;
				}
			}
		}
		return code;
	}
	
	/**
	* Kopioi alkuperaisen palikan niin, etta alkuperaista palikkaa ei pyoriteta pelin aikana.
	* 
	* @return Kopioitu palikka
	*/
	public Palikka kopioi() {
		Palikka kopioituPalikka = new Palikka( koko, this.alapisteet, this.ylapisteet );
		
		kopioituPalikka.palikka = this.palikka;
		kopioituPalikka.pyoritetytVersiot = this.pyoritetytVersiot;
		kopioituPalikka.pyoraytykset = this.pyoraytykset;
		kopioituPalikka.pyoraytetytSarmat = this.pyoraytetytSarmat;
		kopioituPalikka.sarmat = this.sarmat;
		kopioituPalikka.hashcode = this.hashcode;
		
		return kopioituPalikka;
	}
	
	/**
	* Antaa Palikan taulukon.
	* 
	* @return Palikka taulukkomuodossa
	*/
	public Pala[][][] annaPalikka() {
		return this.palikka;
	}
	
	/**
	* Antaa palikan keskipisteen koordinaatin. Keskipisteen koordinaatti on kaikista suunnista samassa kohdassa.
	* 
	* @return Palikan keskipiste
	*/
	public int annaKeskipiste() {
		return (koko-1)/2;
	}
	
	/**
	* Antaa palikan leveyden.
	* 
	* @return Leveys ruutuina
	*/
	public int annaLeveys() {
		int leveys = 0;
		for (int i=0; i<koko; i++) {
			if (onkoTasossaPalikoita(i, i, 0, koko-1, 0, koko-1)) {
				leveys++;
			}
		}
		
		return leveys;
	}
	
	/**
	* Antaa palikan korkeuden.
	* 
	* @return Korkeus ruutuina
	*/
	public int annaKorkeus() {
		int korkeus = 0;
		for (int j=0; j<koko; j++) {
			if (onkoTasossaPalikoita(0, koko-1, j, j, 0, koko-1)) {
				korkeus++;
			}
		}
		
		return korkeus;
	}
	
	private boolean onkoTasossaPalikoita(int iAlku, int iLoppu, int jAlku, int jLoppu, int kAlku, int kLoppu) {
		for (int i = iAlku; i <= iLoppu; i++) {
			for (int j = jAlku; j <= jLoppu; j++) {
				for (int k = kAlku; k <= kLoppu; k++) {
					
					if (palikka[i][j][k] == Pala.TIPPUVA) {
						return true;
					}
					
				}
			}
		}
		
		return false;
	}
	
	/**
	* Antaa Palikkaan liittyvan minimitiputuksen pistelaskukertoimen.
	* 
	* @return pistekerroin
	*/
	public int annaAlapisteet() {
		return this.alapisteet;
	}
	
	/**
	* Antaa Palikkaan liittyvan maksimitiputuksen pistelaskukertoimen.
	* 
	* @return pistekerroin
	*/
	public int annaYlapisteet() {
		return this.ylapisteet;
	}
	
	/**
	* Antaa palikan sisaltamien palojen maaran.
	* 
	* @return Palojen maara
	*/
	public int annaPalojenMaara() {
		return palojenMaara;
	}
	
	/**
	* Antaa palikan piirtoon tarvittavat suorat.
	* 
	* @return Alkupiste-loppupiste parit
	*/
	public HashMap<Koordinaatti, ArrayList<Koordinaatti>> annaSuorat() {
		return this.sarmat;
	}
	
	// Pyorittelyt
	
	/**
	* Pyorittaa jonkun sivutahkoista esille.
	* 
	* @param x Haluttu tahko on vasen (x=-1) tai oikea (x=1), jos muu tahko niin 0
	* @param y Haluttu tahko on ylapuoli (y=-1) tai alapuoli (y=1), jos muu tahko niin 0
	*/
	public void pyoritaSuuntaEsille(int x, int y) {
		PalikkaPyorayttaja.Pyoraytys pyoraytys;
		if (y == 1) {
			pyoraytys = PalikkaPyorayttaja.Pyoraytys.ALAESILLE;
		}
		else if (y == -1) {
			pyoraytys = PalikkaPyorayttaja.Pyoraytys.YLAESILLE;
		}
		else if (x == -1) {
			pyoraytys = PalikkaPyorayttaja.Pyoraytys.VASENESILLE;
		}
		else if (x == 1) {
			pyoraytys = PalikkaPyorayttaja.Pyoraytys.OIKEAESILLE;
		} else {
			return;
		}

    HashMap<PalikkaPyorayttaja.Pyoraytys, Integer> p = pyoraytykset.get(hashcode);
		hashcode = p.get(pyoraytys);
		
		/*
		PalikkaPyorayttaja pyorayttaja = new PalikkaPyorayttaja( this.palikka );
		Pala[][][] verrokki = new Pala[koko][koko][koko];
		verrokki = pyorayttaja.pyoritaSuuntaEsille(verrokki, x, y);
		/**/

		this.sarmat = pyoraytetytSarmat.get(hashcode);
		this.palikka = pyoritetytVersiot.get(hashcode);
		
		/*
		if (!cmp(palikka, verrokki)) {
			System.out.println("Vääryys!"+x+","+y);
			deb(palikka);
			deb(verrokki);
		}
		/**/

	}
	
	/**
	* Pyorittaa palikkaa myotapaivaan tai vastapaivaan.
	* 
	* @param myotapaivaan Tieto siita pyoritetaanko myotapaivaan vai vastakkaiseen suuntaan
	*/
	public void pyoritaMyotapaivaan(boolean myotapaivaan) {
		PalikkaPyorayttaja.Pyoraytys pyoraytys;
		if (myotapaivaan) {
			pyoraytys = PalikkaPyorayttaja.Pyoraytys.MYOTAPAIVA;
		} else {
			pyoraytys = PalikkaPyorayttaja.Pyoraytys.VASTAPAIVA;
		}

    HashMap<PalikkaPyorayttaja.Pyoraytys, Integer> p = pyoraytykset.get(hashcode);
		hashcode = p.get(pyoraytys);
		
		/*
		PalikkaPyorayttaja pyorayttaja = new PalikkaPyorayttaja( this.palikka );
		Pala[][][] verrokki = new Pala[koko][koko][koko];
		verrokki = pyorayttaja.pyoritaMyotapaivaan(verrokki, myotapaivaan);
		*/

		this.sarmat = pyoraytetytSarmat.get(hashcode);
		this.palikka = pyoritetytVersiot.get(hashcode);
		
		/*
		if (!cmp(palikka, verrokki)) {
			System.out.println("Vääryys! "+(myotapaivaan?"myota":"vasta")+"paivaan");
			deb(palikka);
			deb(verrokki);
		}
	  /**/
	}
	
	/*
	private boolean cmp(Pala[][][] a, Pala[][][] b) {
		Pala[][][] palikka = a;
		Pala[][][] palikka2 = b;
		
		if (palikka.length != palikka2.length) return false;
		if (palikka[0].length != palikka2[0].length) return false;
		if (palikka[0][0].length != palikka2[0][0].length) return false;

		for (int k=0; k<palikka[0][0].length; k++) {
			for (int j=0; j<palikka[0].length; j++) {
				for (int i=0; i<palikka.length; i++) {
					if (palikka[i][j][k] != palikka2[i][j][k]) {
						return false;
					}
				}
			}
		}
		return true;
	}
	private void deb(Pala[][][] palikka) {
		System.out.println("Palikka!");

		for (int i=0; i<koko; i++) {
			for (int j=0; j<koko; j++) {
				for (int k=0; k<koko; k++) {
					if (palikka[i][j][k] == Pala.TIPPUVA) {
						System.out.print("*");
					} else {
						System.out.print(" ");
					}
				}
				System.out.print("|");
			}
			System.out.println();
		}

	}
	*/

	public int hashCode() {
		return hashcode;
	}

}
