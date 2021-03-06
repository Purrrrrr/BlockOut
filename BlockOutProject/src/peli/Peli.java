package peli;

import kayttoliittyma.BlockOut;
import peli.asetukset.logiikka.Asetukset;
import peli.asetukset.logiikka.Ulottuvuudet;
import peli.ennatyslista.Ennatyslistaaja;
import peli.grafiikka.Piirturi;
import peli.logiikka.Kentta;
import peli.logiikka.Palikkasetti;
import peli.logiikka.Palikkavarasto;
import peli.logiikka.Pistelaskija;
import peli.logiikka.TippuvaPalikka;
import valmiskomponentit.Ikkuna;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;
import java.awt.event.ComponentListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
//import java.applet.AudioClip;
//import java.applet.Applet;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.FloatControl.Type;

public class Peli extends Ikkuna {

  private BlockOut kayttis;
  private Piirturi piirturi = null;
  private Ennatyslistaaja ennatyslistaaja;

	// Hallinnoitavat oliot:
  private Kentta kentta;
  private Palikkavarasto palikkavarasto;
  private TippuvaPalikka tippuvaPalikka;
  private double tiputustenVali;
  private long viimeAjastushetki;
  private double pohjaAika = 5.51, aikatasokerroin = 0.64;

	// Pelin statuksen, pisteiden ja tasojen hallinta
  private Pistelaskija pistelaskija;
  private int taso, pelattujaPalikoita;
  private Palikkasetti palikkasetti;
  private boolean tauolla, gameOver;
  private long gameOverHetki;

  private boolean aanetPaalla;
  private Clip tasoAani;

  /**
   * Alustaa ja hallinnoi pelin osasia.
   *
   * @param kayttis Kayttoliittyma
   * @param asetukset Kayttajan valitsemat asetukset
   * @param ennatyslistaaja Ennatyslistan hallinnoija
   */
  public Peli(BlockOut kayttis, Asetukset asetukset, Ennatyslistaaja ennatyslistaaja) {
    super(); //vai olisko, etta konstruktoritonta superia kutsutaan automaattisesti jos sita ei kirjoteta tahan?

    this.kayttis = kayttis;
    this.ennatyslistaaja = ennatyslistaaja;

    this.palikkasetti = asetukset.annaPalikkasetti();

    asetaPeliValmiiksi(asetukset);
  }

  private void asetaPeliValmiiksi(final Asetukset asetukset) {
    alustaTilanne(asetukset.annaAloitustaso());

    Ulottuvuudet ulottuvuudet = asetukset.annaUlottuvuudet();

    alustaLogiikka(asetukset, ulottuvuudet);

    // Lisätään kuuntelija, joka muuttaa asettaa piirturin, jos ikkunan kokoa muutetaan.
    addComponentListener(new ComponentAdapter() {
      public void componentResized(ComponentEvent e) {
        alustaGrafiikka(asetukset, asetukset.annaUlottuvuudet());
      }
    });

    NappainKuuntelija nappainKuuntelija = new NappainKuuntelija(this, this.kayttis, asetukset.annaNappainsetti());
    this.addKeyListener(nappainKuuntelija);

    haeAanet(asetukset.annaAanet());
  }

  private void alustaTilanne(int aloitustaso) {
    this.tauolla = true;
    this.gameOver = false;
    this.gameOverHetki = -1;

    this.taso = aloitustaso;
    this.pelattujaPalikoita = 0;

    this.tiputustenVali = pohjaAika * Math.pow(aikatasokerroin, taso);
  }

  private void alustaLogiikka(Asetukset asetukset, Ulottuvuudet ulottuvuudet) {
    this.pistelaskija = new Pistelaskija(this, ulottuvuudet.annaSyvyys(), asetukset.annaPalikkasetti());

    this.kentta = new Kentta(this, pistelaskija, ulottuvuudet);

    this.palikkavarasto = new Palikkavarasto(asetukset.annaPalikkasetti());
  }

  private void alustaGrafiikka(final Asetukset asetukset, final Ulottuvuudet ulottuvuudet) {
    int leveys = this.getWidth();
    int korkeus = this.getHeight();
    this.piirturi = new Piirturi(this, leveys, korkeus, ulottuvuudet, asetukset.annaPalikkasetti(), asetukset.annaVarit(), this.pistelaskija, this.ennatyslistaaja);
    this.repaint();

  }

  private void haeAanet(boolean aanetPaalla) {
    this.aanetPaalla = aanetPaalla;

    URL tasoUrl = Peli.class.getResource("Taso.wav");

    try {
      AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(tasoUrl);
      this.tasoAani = AudioSystem.getClip();
      this.tasoAani.open(audioInputStream);

      FloatControl gainControl = (FloatControl) tasoAani.getControl(FloatControl.Type.MASTER_GAIN);
      gainControl.setValue(-10.0f);
    } catch (Exception e) {
      System.out.println("Äänitiedoston avaus ei onnistunut.");
      this.aanetPaalla = false;
    }
  }

  /**
   * Asettaa peliin uudet asetukset. Kayttajan muuttamista asetuksista vain
   * grafiikkaan ja nappaimiin vaikuttavat asetukset otetaan voimaan.
   */
  public void asetaUudetAsetukset(Asetukset asetukset) {
    if (this.piirturi != null) {
      this.piirturi.asetaUudetAsetukset(asetukset.annaUlottuvuudet().annaLeikkauspiste(), asetukset.annaVarit());
    }

    this.removeKeyListener(this.getKeyListeners()[0]);
    this.addKeyListener(new NappainKuuntelija(this, this.kayttis, asetukset.annaNappainsetti()));

    this.aanetPaalla = asetukset.annaAanet();
  }

	//*******************************************
  //
  // Hallinnoi pelin statusta
  //
  //*******************************************
  /**
   * Aloittaa pelin. Peli on valmisteltu kaynnistettavaksi kun Peli-luokka on
   * luotu.
   */
  public void aloitaPeli() {
    haeTippuvaPalikka();

    this.tauolla = false;
    ajastaSeuraavaTiputus();
  }

  /**
   * Asettaa pelin tauolle tai tauolta pois.
   *
   * @param tauolla Tieto siita halutaanko peli tauolle vai tauolta pois
   */
  public void asetaPeliTauolle(boolean tauolla) {
    if (this.tauolla == true && tauolla == false) {
      ajastaSeuraavaTiputus();
    }
    if (this.tauolla == false && tauolla == true) {
      paivita(); //palikka piiloon
    }

    this.tauolla = tauolla;
  }

  /**
   * Kertoo onko peli tauolla.
   *
   * @return Tieto siita onko peli tauolla vai ei
   */
  public boolean onkoTauolla() {
    return this.tauolla;
  }

  /**
   * Kertoo piirretäänkö peliin statistiikat
   *
   * @return Aina true
   */
  public boolean piirretaankoStatistiikka() {
    return true;
  }

  /**
   * Lopettaa pelin.
   *
   * @return Tieto siita paasiko pelin pistetulos ennatyslistalle
   */
  public boolean lopetaPeli() {
    this.gameOver = true;
    asetaPeliTauolle(true);

    return selvitaEnnatyslistallePaasy();
  }

  private boolean selvitaEnnatyslistallePaasy() {
    int pisteet = pistelaskija.annaPisteet();
    int leveys = kentta.annaLeveys();
    int korkeus = kentta.annaKorkeus();
    int syvyys = kentta.annaSyvyys();

    if (!this.ennatyslistaaja.paaseekoListalle(pisteet, leveys, korkeus, syvyys, this.palikkasetti)) {
      return false;
    }

    this.ennatyslistaaja.annaEnnatyslistalleKasiteltavaksi(pisteet, leveys, korkeus, syvyys, this.palikkasetti);

    return true;
  }

  /**
   * Kertoo onko peli paattynyt Game Overiin.
   *
   * @return Tieto siita onko game over vai ei
   */
  public boolean onkoGameOver() {
    return this.gameOver;
  }

  /**
   * Kertoo onko pelin paattymisesta sekunti. Kaytannollinen kun jarjestelma
   * game over ilmoituksen jalkeen seuraavaksi odottaa kayttajaa painamaan
   * jotain nappainta, etta pelisessio lopetetaan, niin kayttaja ehtii lopettaa
   * palikan kuumeisen kaantelyn.
   *
   * @return Tieto siita onko game overista sekunti
   */
  public boolean onkoGameOveristaSekunti() {
    if (gameOverHetki != -1) {
      if (System.currentTimeMillis() - 1000 >= gameOverHetki) {
        return true;
      }
    }
    return false;
  }

  /**
   * Antaa silla hetkella pelattavan tason.
   *
   * @return Taso, jota pelataan
   */
  public int annaTaso() {
    return this.taso;
  }

  /**
   * Kertoo pelattujen palojen maaran. Tippuva palikka koostuu usein useasta
   * palasta.
   *
   * @return Palojen maara
   */
  public int annaPelattujenPalojenMaara() {
    return this.pelattujaPalikoita;
  }

  /**
   * Lisaa pelattujen palojen maaraa ja kun pelattuja paloja tulee tarpeeksi
   * nostaa tasoa.
   *
   * @param maara Uusien pelattujen palojen maara
   */
  public void lisaaPelattujenPalojenMaaraa(int maara) {
    this.pelattujaPalikoita += maara;

    if (taso < 10 && this.pelattujaPalikoita >= 15 * (kentta.annaLeveys() + kentta.annaKorkeus()) * (this.taso + 1)) {
      this.taso++;
      this.tiputustenVali *= aikatasokerroin;
      if (aanetPaalla) {
        try {
          tasoAani.start();
          System.out.println("Äänen olisi pitänyt soida.");
        } catch (Exception e) {
          System.out.println("Ongelma");
          e.printStackTrace();
        }
      }
    }
  }

  /**
   * Antaa pelattavan palikkasetin.
   *
   * @return Pelattava palikkasetti
   */
  public Palikkasetti annaPalikkasetti() {
    return this.palikkasetti;
  }

	//*******************************************
  //
  // Hallinnoi pelin etenemista
  //
  //*******************************************
  /**
   * Hakee uuden tippuvan palikan kenttaan edellisen tilalle ja lisaa sille
   * ajastimen huolehtimaan tippumisesta.
   */
  public void haeUusiPalikkaKenttaan() {
    haeTippuvaPalikka();
    this.piirturi.nakymaaMuutettu();
    paivita();

    ajastaSeuraavaTiputus();
  }

  private void haeTippuvaPalikka() {
    if (!kentta.onkoKentanEdustaVapaana()) {
      this.tauolla = true;
      this.gameOver = true;
      this.gameOverHetki = System.currentTimeMillis();
      return;
    }

    this.tippuvaPalikka = new TippuvaPalikka(palikkavarasto.annaPalikka(), kentta, this);
  }

  /**
   * Antaa silla hetkella tippuvan palikan.
   *
   * @return Tippuva palikka
   */
  public TippuvaPalikka annaTippuvaPalikka() {
    return this.tippuvaPalikka;
  }

  /**
   * Tiputtaa palikkaa yhden verran.
   */
  public void tiputaPalikkaa(long ajastushetki) {
    if (ajastushetki > 0 && ajastushetki != this.viimeAjastushetki) {
      return;
    }
    this.viimeAjastushetki = System.currentTimeMillis(); //kuuluisi ennemminkin ajastaSeuraavaTiputus-metodille, mutta voisi johtaa hyvin huonolla tuurilla siihen, etta ajastin tulisi tiputtamaan palikkaa kun pelaaja on tiputtamassa sita

    if (!tippuvaPalikka.siirra(0, 0, 1)) {
      tippuvaPalikka.tiputaPohjalle();
    } else {

      ajastaSeuraavaTiputus();
    }
  }

  private void ajastaSeuraavaTiputus() {
    new Ajastin(this, tippuvaPalikka, (int) (tiputustenVali * 1000) + 160, this.viimeAjastushetki); //160 drop time
  }

	//*******************************************
  //
  // Hallinnoi pelin piirtoa
  //
  //*******************************************
  /**
   * Paivittaa nakyman.
   */
  public void paivita() {
    this.repaint();
  }

  /**
   * Piirtaa pelinakyman.
   *
   * @param g Graphics
   */
  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);

    if (this.piirturi != null) {
      this.piirturi.piirra(g, kentta, tippuvaPalikka, kentta.annaPalojaSisaltavienKerrostenMaara());

      if (gameOver) {
        boolean paaseekoListalle = ennatyslistaaja.paaseekoListalle(pistelaskija.annaPisteet(), kentta.annaLeveys(), kentta.annaKorkeus(), kentta.annaSyvyys(), this.palikkasetti);
        this.piirturi.piirraGameOver(g, this, paaseekoListalle);
      }
    }
  }

  /**
   * Varmistaa, että pelinäkymä päivittyy kun kuvat latautuvat
   *
   */
  @Override
  public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
    this.piirturi.nakymaaMuutettu();

    return super.imageUpdate(img, infoflags, x, y, width, height);
  }
}
