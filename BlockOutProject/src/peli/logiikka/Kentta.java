package peli.logiikka;

import peli.Peli;
import peli.Koordinaatti;
import peli.asetukset.logiikka.Ulottuvuudet;
import java.util.List;

public class Kentta {

  private Peli peli;
  private Pistelaskija pistelaskija;

  private Pala[][][] kentta;
  private int leveys, korkeus, syvyys;

  /**
   * Hallinnoi peli kentan jahmetettyja paloja ja reunoja.
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

    this.kentta = new Pala[leveys + 2][korkeus + 2][syvyys + 1];

    for (int k = 0; k < syvyys + 1; k++) {
      for (int j = 0; j < korkeus + 2; j++) {
        for (int i = 0; i < leveys + 2; i++) {
          if (k == syvyys + 1 - 1 || j == 0 || i == 0 || j == korkeus + 2 - 1 || i == leveys + 2 - 1) {
            kentta[i][j][k] = Pala.REUNA;
          } else {
            kentta[i][j][k] = Pala.TYHJA;
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
    return kentta[(leveys + 2) / 2][(korkeus + 2) / 2][0] == Pala.TYHJA;
  }

  /**
   * Selvittää onko kentän tietty kohta varattu
   */
  public boolean onkoKoordinaattiVarattu(int x, int y, int z) {
    return kentta[x][y][z] != Pala.TYHJA;
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

    for (int k = 0; k < palikka.annaSyvyys(); k++) {
      for (int j = 0; j < palikka.annaKorkeus(); j++) {
        for (int i = 0; i < palikka.annaLeveys(); i++) {

          if (!palikka.onkoTyhja(i, j, k) && k - keskipiste + z >= 0) {
            kentta[ i - keskipiste + x][ j - keskipiste + y][ k - keskipiste + z] = Pala.VARATTU;
          }

        }
      }
    }

  }

  public String toString() {
    String result = "Palikka!";

    for (int i = 0; i < kentta.length; i++) {
      for (int k = 0; k < kentta[0][0].length; k++) {
        for (int j = 0; j < kentta[0].length; j++) {
          if (kentta[i][j][k] != Pala.TYHJA) {
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
        if (kentta[i][j][kerros] != Pala.VARATTU) {
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
            kentta[i][j][k] = Pala.TYHJA;
          } else {
            kentta[i][j][k] = kentta[i][j][k - 1];
          }

        }
      }
    }
  }

  private boolean onkoKenttaTyhja() {
    for (int k = 0; k < this.syvyys; k++) {
      for (int j = 1; j < this.korkeus + 1; j++) {
        for (int i = 1; i < this.leveys + 1; i++) {
          if (kentta[i][j][k] != Pala.TYHJA) {
            return false;
          }
        }
      }
    }

    return true;
  }

  /**
   * Selvittaako mahtuuko lista paloja kenttaan. Jos pala on kuilun sivujen
   * ulkopuolella palauttaa false. Jos pala on kuilun edessa, mutta sivujen
   * sisapuolella palauttaa true.
   *
   * @param palaKoordinaatit Palojen x,y,z-koordinaatit suhteessa palaan
   * @param dx Palan x-koordinaatin säätö
   * @param dy Palan y-koordinaatin säätö
   * @param dz Palan z-koordinaatin säätö
   * @return Tieto siita mahtuuko pala kenttaan vai ei
   */
  public boolean mahtuvatkoPalatKenttaan(List<Koordinaatti> palat, int dx, int dy, int dz) {
    for (Koordinaatti k : palat) {
      int x = k.annaX() + dx;
      int y = k.annaY() + dy;
      int z = k.annaZ() + dz;
      try {
        if (kentta[x][y][z] != Pala.TYHJA) {
          return false;
        }
      } catch (IndexOutOfBoundsException e) {
        //kentan reunojen sisalla olevat palaset
        if (x < 1 || y < 1 || x > this.leveys || y > this.korkeus) {
          return false;
        }
        //kentan edessa olevat palaset paastetaan lapi.
      }
    }
    return true;
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
        if (kentta[i][j][kerros] == Pala.VARATTU) {
          return false;
        }
      }
    }

    return true;
  }
}
