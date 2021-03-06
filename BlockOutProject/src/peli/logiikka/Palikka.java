package peli.logiikka;

import peli.Koordinaatti;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Deque;
import java.util.List;
import java.util.LinkedList;

public class Palikka {

  private int koko;
  private int alapisteet, ylapisteet, palojenMaara;

  /**
   * Luku, joka kertoo miten päin palikka on. Käytetään indeksinä hashcode-,
   * pala-, pyöräytys-, palakoordinaatti-, ja särmätaulukoihin
   */
  int suunta = 0;
  private int[] hashcodes;
  /**
   * Palikan kaikki pyöräytetyt versiot suunnan mukaan talletettuna
   */
  private PalaMatriisi[] pyoritetytVersiot;
  /**
   * Muunnos suunnasta ja pyöräytyksestä pyöräytetyn palan suuntaan
   */
  private int[][] pyoraytyksetSuunnittain;
  /**
   * Palikan särmäkoordinaatit suunnittain talletettuna
   */
  private HashMap<Koordinaatti, ArrayList<Koordinaatti>>[] pyoraytetytSarmat;

  /**
   * Tämänhetkinen palikka
   */
  private PalaMatriisi palikka;
  /**
   * Tämänhetkiset särmäkoordinaatit
   */
  private HashMap<Koordinaatti, ArrayList<Koordinaatti>> sarmat;
  int hashcode = 0;

  /**
   * Hallinnoi yhden palikan sisaista rakennetta ja palikkaan liittyvia
   * peruspisteita. Voidaan luoda 3x3x3 palikoita.
   *
   * @param alapisteet Alemmat peruspisteet
   * @param ylapisteet Ylemmat peruspisteet
   * @param koordinaatit Palojen koordinaatit taulukkona, jossa on kolmella
   * jaollinen lukumäärä koordinaatteja
   */
  public Palikka(int alapisteet, int ylapisteet, int koordinaatit[]) {
    this(5, alapisteet, ylapisteet, koordinaatit);
  }

  /**
   * Hallinnoi yhden palikan sisaista rakennetta ja palikkaan liittyvia
   * peruspisteita.
   *
   * @param koko Palikan leveys/korkeus/syvyys
   * @param alapisteet Alemmat peruspisteet
   * @param ylapisteet Ylemmat peruspisteet
   * @param koordinaatit Palojen koordinaatit taulukkona, jossa on kolmella
   * jaollinen lukumäärä koordinaatteja
   */
  public Palikka(int koko, int alapisteet, int ylapisteet, int koordinaatit[]) {
    this(koko, alapisteet, ylapisteet);
    if (koordinaatit.length % 3 != 0) {
      throw new IllegalArgumentException();
    }
    for (int i = 0; i < koordinaatit.length; i += 3) {
      lisaaPala(koordinaatit[i], koordinaatit[i + 1], koordinaatit[i + 2]);
    }

    alustaPyoraytykset();
    this.sarmat = pyoraytetytSarmat[suunta];

  }

  private Palikka(int koko, int alapisteet, int ylapisteet) {
    if (koko % 2 == 0) {
      koko++;
    }
    this.koko = koko;
    this.palikka = new PalaMatriisi(this.koko);

    this.alapisteet = alapisteet;
    this.ylapisteet = ylapisteet;
    this.palojenMaara = 0;
  }

  public Palikka(Palikka toinen) {
    this.koko = toinen.koko;
    this.alapisteet = toinen.alapisteet;
    this.ylapisteet = toinen.ylapisteet;
    this.palojenMaara = toinen.palojenMaara;

    this.palikka = toinen.palikka;
    this.pyoritetytVersiot = toinen.pyoritetytVersiot;
    this.pyoraytyksetSuunnittain = toinen.pyoraytyksetSuunnittain;
    this.pyoraytetytSarmat = toinen.pyoraytetytSarmat;
    this.sarmat = toinen.sarmat;
    this.hashcode = toinen.hashcode;
    this.suunta = toinen.suunta;
    this.hashcodes = toinen.hashcodes;
  }

  /**
   * Kopioi alkuperaisen palikan niin, etta alkuperaista palikkaa ei pyoriteta
   * pelin aikana.
   *
   * @return Kopioitu palikka
   */
  public Palikka kopioi() {
    return new Palikka(this);
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
    if (!this.palikka.onkoTyhja(x - 1, y - 1, z - 1)) {
      return;
    }

    this.palikka.asetaKohdanTyhjyys(x - 1, y - 1, z - 1, false);
    this.palojenMaara++;
  }

  private void alustaPyoraytykset() {

    //HashMap-versiot lopullisista pyörittelytaulukoista:
    HashMap<Integer, PalaMatriisi> pyoritetytVersiotHM = new HashMap<Integer, PalaMatriisi>();
    HashMap<Integer, Integer> versionumerot = new HashMap<>();
    HashMap<Integer, HashMap<PalikkaPyorayttaja.Pyoraytys, Integer>> pyoraytykset = new HashMap<Integer, HashMap<PalikkaPyorayttaja.Pyoraytys, Integer>>();
    PalikkaPyorayttaja.Pyoraytys[] pyoritykset = PalikkaPyorayttaja.Pyoraytys.values();

    int versioLaskuri = 0;

    PalaMatriisi juuri = palikka;

    Deque<PalaMatriisi> tutkittavat = new LinkedList<PalaMatriisi>();
    tutkittavat.add(juuri);

    while (!tutkittavat.isEmpty()) {
      PalaMatriisi tutkittava = tutkittavat.pop();

      int tutkittavanHashcode = tutkittava.hashCode();
      pyoritetytVersiotHM.put(tutkittavanHashcode, tutkittava);
      versionumerot.put(tutkittavanHashcode, versioLaskuri);
      versioLaskuri++;

      //System.out.println("Tutkittava: "+tutkittavanHashcode);
      HashMap<PalikkaPyorayttaja.Pyoraytys, Integer> palikanPyoraytykset;
      palikanPyoraytykset = new HashMap<PalikkaPyorayttaja.Pyoraytys, Integer>();
      pyoraytykset.put(tutkittavanHashcode, palikanPyoraytykset);

      for (PalikkaPyorayttaja.Pyoraytys p : pyoritykset) {
        PalikkaPyorayttaja pyorayttaja = new PalikkaPyorayttaja(tutkittava);
        PalaMatriisi uusi = pyorayttaja.pyorita(p);
        int pyoraytetynHashcode = uusi.hashCode();

        if (!pyoritetytVersiotHM.containsKey(pyoraytetynHashcode)) {
          pyoritetytVersiotHM.put(pyoraytetynHashcode, uusi);
          tutkittavat.add(uusi);
        }
        palikanPyoraytykset.put(p, pyoraytetynHashcode);

      }
    }

    pyoritetytVersiot = new PalaMatriisi[pyoritetytVersiotHM.size()];
    hashcodes = new int[pyoritetytVersiot.length];
    pyoraytyksetSuunnittain = new int[pyoritetytVersiot.length][pyoritykset.length];
    pyoraytetytSarmat = new HashMap[pyoritetytVersiot.length];

    for (Integer hc : pyoritetytVersiotHM.keySet()) {
      PalaMatriisi versio = pyoritetytVersiotHM.get(hc);
      int versioNumero = versionumerot.get(hc);
      pyoritetytVersiot[versioNumero] = versio;
      hashcodes[versioNumero] = hc;

      HashMap<PalikkaPyorayttaja.Pyoraytys, Integer> versionPyoraytykset = pyoraytykset.get(hc);
      for (PalikkaPyorayttaja.Pyoraytys p : pyoritykset) {
        Integer pyoraytyksenHc = versionPyoraytykset.get(p);
        pyoraytyksetSuunnittain[versioNumero][p.ordinal()] = versionumerot.get(pyoraytyksenHc);
      }

      Kulmahaku kulmahaku = new Kulmahaku(versio);
      pyoraytetytSarmat[versioNumero] = kulmahaku.haeSarmat();
    }

  }

  public PalaMatriisi annaPalat() {
    return palikka;
  }

  /**
   * Antaa palikan keskipisteen koordinaatin. Keskipisteen koordinaatti on
   * kaikista suunnista samassa kohdassa.
   *
   * @return Palikan keskipiste
   */
  public int annaKeskipiste() {
    return (koko - 1) / 2;
  }

  /**
   * Antaa palikan leveyden.
   *
   * @return Leveys ruutuina
   */
  public int annaLeveys() {
    int leveys = 0;
    for (int i = 0; i < koko; i++) {
      if (onkoTasossaPalikoita(i, i, 0, koko - 1, 0, koko - 1)) {
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
    for (int j = 0; j < koko; j++) {
      if (onkoTasossaPalikoita(0, koko - 1, j, j, 0, koko - 1)) {
        korkeus++;
      }
    }

    return korkeus;
  }

  private boolean onkoTasossaPalikoita(int iAlku, int iLoppu, int jAlku, int jLoppu, int kAlku, int kLoppu) {
    for (int i = iAlku; i <= iLoppu; i++) {
      for (int j = jAlku; j <= jLoppu; j++) {
        for (int k = kAlku; k <= kLoppu; k++) {

          if (!palikka.onkoTyhja(i, j, k)) {
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
   * @param x Haluttu tahko on vasen (x=-1) tai oikea (x=1), jos muu tahko niin
   * 0
   * @param y Haluttu tahko on ylapuoli (y=-1) tai alapuoli (y=1), jos muu tahko
   * niin 0
   */
  public void pyoritaSuuntaEsille(int x, int y) {
    PalikkaPyorayttaja.Pyoraytys pyoraytys;
    if (y == 1) {
      pyoraytys = PalikkaPyorayttaja.Pyoraytys.ALAESILLE;
    } else if (y == -1) {
      pyoraytys = PalikkaPyorayttaja.Pyoraytys.YLAESILLE;
    } else if (x == -1) {
      pyoraytys = PalikkaPyorayttaja.Pyoraytys.VASENESILLE;
    } else if (x == 1) {
      pyoraytys = PalikkaPyorayttaja.Pyoraytys.OIKEAESILLE;
    } else {
      return;
    }

    suunta = pyoraytyksetSuunnittain[suunta][pyoraytys.ordinal()];
    hashcode = hashcodes[suunta];

    /*
     PalikkaPyorayttaja pyorayttaja = new PalikkaPyorayttaja( this.palikka );
     PalaMatriisi verrokki = new Pala[koko][koko][koko];
     verrokki = pyorayttaja.pyoritaSuuntaEsille(verrokki, x, y);
     /**/
    this.sarmat = pyoraytetytSarmat[suunta];
    this.palikka = pyoritetytVersiot[suunta];

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
   * @param myotapaivaan Tieto siita pyoritetaanko myotapaivaan vai
   * vastakkaiseen suuntaan
   */
  public void pyoritaMyotapaivaan(boolean myotapaivaan) {
    PalikkaPyorayttaja.Pyoraytys pyoraytys;
    if (myotapaivaan) {
      pyoraytys = PalikkaPyorayttaja.Pyoraytys.MYOTAPAIVA;
    } else {
      pyoraytys = PalikkaPyorayttaja.Pyoraytys.VASTAPAIVA;
    }

    suunta = pyoraytyksetSuunnittain[suunta][pyoraytys.ordinal()];
    hashcode = hashcodes[suunta];

    /*
     PalikkaPyorayttaja pyorayttaja = new PalikkaPyorayttaja( this.palikka );
     PalaMatriisi verrokki = new Pala[koko][koko][koko];
     verrokki = pyorayttaja.pyoritaMyotapaivaan(verrokki, myotapaivaan);
     */
    this.sarmat = pyoraytetytSarmat[suunta];
    this.palikka = pyoritetytVersiot[suunta];

    /*
     if (!cmp(palikka, verrokki)) {
     System.out.println("Vääryys! "+(myotapaivaan?"myota":"vasta")+"paivaan");
     deb(palikka);
     deb(verrokki);
     }
     /**/
  }

  /*
   private boolean cmp(PalaMatriisi a, PalaMatriisi b) {
   PalaMatriisi palikka = a;
   PalaMatriisi palikka2 = b;
		
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
   private void deb(PalaMatriisi palikka) {
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
  @Override
  public int hashCode() {
    return hashcode;
  }

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof Palikka)) {
      return false;
    }

    return this.equals((Palikka) other);
  }

  public boolean equals(Palikka that) {
    return this.palikka.equals(that.palikka);
  }

}
