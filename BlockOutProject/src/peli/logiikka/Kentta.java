package peli.logiikka;

import peli.Peli;
import peli.Koordinaatti;
import peli.asetukset.logiikka.Ulottuvuudet;
import java.util.List;

public class Kentta {

  private Peli peli;
  private Pistelaskija pistelaskija;

  private PalaMatriisi kentta;
  private int leveys, korkeus, syvyys;

  /**
   * Hallinnoi pelikentän jähmetettyjä paloja ja reunoja.
   *
   * @param peli Pelin hallinnoija, joka vastaa nakyman paivityksesta
   * @param pistelaskija Pelin pistelaskija
   * @param ulottuvuudet Pelin kuilun ulottuvuudet
   */
  public Kentta(Peli peli, Pistelaskija pistelaskija, Ulottuvuudet ulottuvuudet) {
    alustaKentta(ulottuvuudet);

    this.peli = peli;
    this.pistelaskija = pistelaskija;
  }

  private void alustaKentta(Ulottuvuudet ulottuvuudet) {
    this.leveys = ulottuvuudet.annaLeveys();
    this.korkeus = ulottuvuudet.annaKorkeus();
    this.syvyys = ulottuvuudet.annaSyvyys();

    this.kentta = new PalaMatriisi(leveys+2, korkeus+2, syvyys+1);

    for (int k = 0; k < syvyys + 1; k++) {
      for (int j = 0; j < korkeus + 2; j++) {
        for (int i = 0; i < leveys + 2; i++) {
          if (k == syvyys + 1 - 1 || j == 0 || i == 0 || j == korkeus + 2 - 1 || i == leveys + 2 - 1) {
            //Reunapala
            kentta.asetaKohdanTyhjyys(i, j, k, false);
          } else {
            kentta.asetaKohdanTyhjyys(i, j, k, true);
          }
        }
      }
    }
  }

  /**
   * Antaa kentan leveyden
   *
   * @return Leveys ruuduissa
   */
  public int annaLeveys() {
    return this.leveys;
  }

  /**
   * Antaa kentan korkeuden
   *
   * @return Korkeus ruuduissa
   */
  public int annaKorkeus() {
    return this.korkeus;
  }

  /**
   * Antaa kentan syvyyden
   *
   * @return Syvyys ruuduissa
   */
  public int annaSyvyys() {
    return this.syvyys;
  }

  /**
   * Selvittaa onko kentan etummaisin kohta vapaana.
   *
   * @return Tieto siita oliko tyhja vai ei
   */
  public boolean onkoKentanEdustaVapaana() {
    return kentta.onkoTyhja((leveys+2) / 2, (korkeus+2) / 2, 0);
  }

  /**
   * Selvittää onko kentän tietty kohta varattu
   */
  public boolean onkoKoordinaattiVarattu(int x, int y, int z) {
    return !kentta.onkoTyhja(x, y, z);
  }

  /**
   * Jahmettaa tippuneen palikan varatuiksi paloiksi pohjalle.
   *
   * @param annettuPalikka Palikka, joka jahmetetaan
   * @param x Palikan x-koordinaatti
   * @param y Palikan y-koordinaatti
   * @param z Palikan z-koordinaatti
   * @param tiputusKorkeus Matka mita Palikkaa tiputettiin tauotta
   */
  public void jahmetaPalikka(Palikka annettuPalikka, int x, int y, int z, int tiputusKorkeus) {
    muutaPalatTippuvistaVaratuiksi(annettuPalikka, x, y, z);

    int tuhottujaKerroksia = tuhoaValmiitKerrokset();
    boolean tyhja = onkoKenttaTyhja();

    pistelaskija.annaPisteitaTiputuksesta(annettuPalikka, tiputusKorkeus, tuhottujaKerroksia, tyhja);

    peli.lisaaPelattujenPalojenMaaraa(annettuPalikka.annaPalojenMaara());
  }

  private void muutaPalatTippuvistaVaratuiksi(Palikka annettuPalikka, int x, int y, int z) {
    PalaMatriisi palikka = annettuPalikka.annaPalat();
    int keskipiste = (palikka.annaLeveys() - 1) / 2;
    x -= keskipiste;
    y -= keskipiste;
    z -= keskipiste;
    kentta.lisaaMatriisinPalatKohtaan(palikka, x,y,z);
  }

  public String toString() {
    String result = "Palikka!";

    for (int i = 0; i < kentta.annaLeveys(); i++) {
      for (int k = 0; k < kentta.annaSyvyys(); k++) {
        for (int j = 0; j < kentta.annaKorkeus(); j++) {
          if (!kentta.onkoTyhja(i, j, k)) {
            result += "*";
          } else {
            result += " ";
          }
        }

        result += "|";
      }
      result += "\n";
    }
    return result;
  }

  private int tuhoaValmiitKerrokset() {
    int kerrostenMaara = 0;

    for (int k = 0; k <= this.syvyys - 1; k++) {
      if (kokoKerrosTaytetty(k)) {
        kerrostenMaara++;
        tiputaKerroksia(k);
      }
    }

    return kerrostenMaara;
  }

  private boolean kokoKerrosTaytetty(int kerros) {
    for (int i = 1; i < this.leveys + 1; i++) {
      for (int j = 1; j < this.korkeus + 1; j++) {
        if (kentta.onkoTyhja(i, j, kerros)) {
          return false;
        }
      }
    }

    return true;
  }

  private void tiputaKerroksia(int tuhottuKerros) {
    for (int k = tuhottuKerros; k >= 0; k--) {
      for (int i = 1; i < this.leveys + 1; i++) {
        for (int j = 1; j < this.korkeus + 1; j++) {

          if (k == 0) {
            kentta.asetaKohdanTyhjyys(i, j, k, true);
          } else {
            kentta.asetaKohdanTyhjyys(i, j, k, kentta.onkoTyhja(i, j, k-1));
          }

        }
      }
    }
  }

  private boolean onkoKenttaTyhja() {
    for (int k = 0; k < this.syvyys; k++) {
      for (int j = 1; j < this.korkeus + 1; j++) {
        for (int i = 1; i < this.leveys + 1; i++) {
          if (!kentta.onkoTyhja(i, j, k)) {
            return false;
          }
        }
      }
    }

    return true;
  }

  /**
   * Selvittaako mahtuuko palikka kenttaan. Jos pala on kuilun sivujen
   * ulkopuolella palauttaa false. Jos pala on kuilun edessa, mutta sivujen
   * sisapuolella palauttaa true.
   *
   * @param palikka Palojen x,y,z-koordinaatit suhteessa palaan
   * @param x Palan x-koordinaatin säätö
   * @param y Palan y-koordinaatin säätö
   * @param z Palan z-koordinaatin säätö
   * @return Tieto siita mahtuuko pala kenttaan vai ei
   */
  public boolean mahtuukoPalikkaKenttaan(Palikka palikka, int x, int y, int z) {
    int keskipiste = (palikka.annaPalat().annaLeveys() - 1) / 2;
    return kentta.mahtuukoPalikka(palikka.annaPalat(), x-keskipiste, y-keskipiste, z-keskipiste);
  }

  /**
   * Kertoo kuinka monessa kerroksessa on paloja.
   *
   * @return Kerrosten maara
   */
  public int annaPalojaSisaltavienKerrostenMaara() {
    int kerrostenMaara = 0;

    for (int k = this.syvyys - 1; k >= 0; k--) {
      if (kokoKerrosTyhja(k)) {
        break;
      }
      kerrostenMaara++;
    }

    return kerrostenMaara;
  }

  private boolean kokoKerrosTyhja(int kerros) {
    for (int i = 1; i < this.leveys + 1; i++) {
      for (int j = 1; j < this.korkeus + 1; j++) {
        if (!kentta.onkoTyhja(i, j, kerros)) {
          return false;
        }
      }
    }

    return true;
  }
}
