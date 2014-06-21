package peli.logiikka;

public class PalikkaPyorayttaja {
	private PalaMatriisi palikka;
	private int koko;

	public enum Pyoraytys {
		MYOTAPAIVA(),
		VASTAPAIVA(),
		VASENESILLE(),
		OIKEAESILLE(),
		ALAESILLE(),
		YLAESILLE();
	}
	
	/**
	* Osaa pyoraytella 3D-taulukoita, jotka on rakennettu Paloista.
	* 
	* @param palikka Pyoraytettava palikka
	*/
	public PalikkaPyorayttaja(PalaMatriisi palikka) {
		this.palikka = palikka;
		this.koko = palikka.annaSyvyys(); //kaikkiin suuntiin saman kokoinen
	}
	
	public PalaMatriisi pyorita(Pyoraytys suunta) {
		PalaMatriisi uusi = new PalaMatriisi(koko);

		switch(suunta) {
			case MYOTAPAIVA:
				return pyoritaMyotapaivaan(uusi);
			case VASTAPAIVA:
				return pyoritaVastapaivaan(uusi);
			case VASENESILLE:
				return pyoritaVasenPuoliEsille(uusi);
			case OIKEAESILLE:
				return pyoritaOikeaPuoliEsille(uusi);
			case ALAESILLE:
				return pyoritaAlapuoliEsille(uusi);
			case YLAESILLE:
				return pyoritaYlapuoliEsille(uusi);
		}
		return uusi;
	}
	
	/**
	* Pyorittaa palikaa niin, etta jokin sivutahkoista tulee esille.
	* 
	* @param uusi Uusi tyhja 3D-taulukko, johon pyoraytetty palikka muodostetaan
	* @param x Jos annettu arvo on -1 pyoritetaan vasen puoli esille, jos se on 1 pyoritetaan oikea puoli esille, jos nolla niin ei pyoriteta y-akselin ympari
	* @param y Jos annettu arvo on -1 pyoritetaan ylapuoli esille, jos jos se on 1 pyoritetaan alapuoli esille, jos nolla niin ei pyoriteta x-akselin ympari
	* 
	* @return Luotu uusi 3D-taulukko
	*/
	public PalaMatriisi pyoritaSuuntaEsille(PalaMatriisi uusi, int x, int y) {
		if (y == 1) {
			return pyoritaAlapuoliEsille(uusi);
		}
		else if (y == -1) {
			return pyoritaYlapuoliEsille(uusi);
		}
		
		else if (x == -1) {
			return pyoritaVasenPuoliEsille(uusi);
		}
		else if (x == 1) {
			return pyoritaOikeaPuoliEsille(uusi);
		}
		
		return this.palikka;
	}
	
	private PalaMatriisi pyoritaYlapuoliEsille(PalaMatriisi uusi) {
		//System.out.println("alapuoli");
		for (int k=0; k<koko; k++) {
			
			for (int i=0; i<koko; i++) {
				for (int j=0; j<koko; j++) {
					uusi.asetaKohdanTyhjyys(i,j,k,palikka.onkoTyhja(i,koko-1-k,j));
				}
			}
			
		}
		
		return uusi;
	}
	
	private PalaMatriisi pyoritaAlapuoliEsille(PalaMatriisi uusi) {
		//System.out.println("ylapuoli");
		for (int k=0; k<koko; k++) {
			
			for (int i=0; i<koko; i++) {
				for (int j=0; j<koko; j++) {
					uusi.asetaKohdanTyhjyys(i,j,k,palikka.onkoTyhja(i,k,koko-1-j));
				}
			}
			
		}
		
		return uusi;
	}
	
	private PalaMatriisi pyoritaOikeaPuoliEsille(PalaMatriisi uusi) {
		//System.out.println("vasen puoli");
		for (int k=0; k<koko; k++) {
			
			for (int i=0; i<koko; i++) {
				for (int j=0; j<koko; j++) {
					uusi.asetaKohdanTyhjyys(i,j,k,palikka.onkoTyhja(koko-1-k,j,i));
				}
			}
			
		}
		
		return uusi;
	}
	
	private PalaMatriisi pyoritaVasenPuoliEsille(PalaMatriisi uusi) {
		//System.out.println("oikea puoli");
		for (int k=0; k<koko; k++) {
			
			for (int i=0; i<koko; i++) {
				for (int j=0; j<koko; j++) {
					uusi.asetaKohdanTyhjyys(i,j,k,palikka.onkoTyhja(k,j,koko-1-i));
				}
			}
			
		}
		
		return uusi;
	}
	
	/**
	* Pyorittaa palikkaa myota-ja vastapaivaan.
	* 
	* @param uusi Uusi 3D-taulukko, johon pyoraytetty palikka muodostetaan
	* @param myotapaivaan Tieto siita pyoritetaanko myotapaivaan vai vastakkaiseen suuntaan
	* 
	* @return Luotu uusi 3D-taulukko
	*/
	public PalaMatriisi pyoritaMyotapaivaan(PalaMatriisi uusi, boolean myotapaivaan) {
		if (myotapaivaan) {
			return pyoritaMyotapaivaan(uusi);
		}
		else {
			return pyoritaVastapaivaan(uusi);
		}
	}
	
	private PalaMatriisi pyoritaVastapaivaan(PalaMatriisi uusi) {
		//System.out.println("myotapaivaan");
		for (int k=0; k<koko; k++) {
			
			for (int i=0; i<koko; i++) {
				for (int j=0; j<koko; j++) {
					uusi.asetaKohdanTyhjyys(i,j,k,palikka.onkoTyhja(koko-1-j,i,k));
				}
			}
			
		}
		
		return uusi;
	}
	
	private PalaMatriisi pyoritaMyotapaivaan(PalaMatriisi uusi) {
		//System.out.println("vastapaivaan");
		for (int k=0; k<koko; k++) {
			
			for (int i=0; i<koko; i++) {
				for (int j=0; j<koko; j++) {
					uusi.asetaKohdanTyhjyys(i,j,k,palikka.onkoTyhja(j,koko-1-i,k));
				}
			}
			
		}
		
		return uusi;
	}
}
