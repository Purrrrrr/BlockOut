package peli.logiikka;

import peli.Koordinaatti;

import java.util.ArrayList;
import java.util.HashMap;

public class Kulmahaku {

  private PalaMatriisi palikka;
  private HashMap<Koordinaatti, ArrayList<Koordinaatti>> sarmat;
  private Koordinaatti viimeisinAlkupiste;

  /**
   * Osaa hakea kulmat annetulle 3D-taulukossa esiteltylle palikalle ja yhdistaa
   * kulmat, jotka muodostavat sarman.
   *
   * @param palikka Palikka, jolle kulmat ja sarmat halutaan etsia.
   */
  public Kulmahaku(PalaMatriisi palikka) {
    this.palikka = palikka;
    this.sarmat = new HashMap<Koordinaatti, ArrayList<Koordinaatti>>();
  }

  /**
   * Hakee palikan sarmat.
   *
   * @return Lista alkupiste-loppupiste pareista, jotka muodostavat palikan
   * sarmat
   */
  public HashMap<Koordinaatti, ArrayList<Koordinaatti>> haeSarmat() {
    haeXSuuntaisetSarmat();
    haeYSuuntaisetSarmat();
    haeZSuuntaisetSarmat();

    return this.sarmat;
  }

  private void haeXSuuntaisetSarmat() {
    for (int k = -1; k < palikka.annaSyvyys(); k++) {
      for (int j = -1; j < palikka.annaKorkeus(); j++) {

        haeXViivanSarmat(k, j);

      }
    }
  }

  private void haeXViivanSarmat(int k, int j) {
    viimeisinAlkupiste = null;
    String edellinenNelio = "0000";

    for (int i = 0; i <= palikka.annaSyvyys(); i++) {
      String nelio = nelionSisalto(i, j, k, 1, 2, 2);

      if (onkoTahko(nelio)) {
        kasitteleTahko(edellinenNelio, i, j + 1, k + 1);
        nelio = "1111";
      } else if (liittyykoPaatepisteeseen(edellinenNelio, nelio)) {
        kasitteleMuuttuvaSarma(edellinenNelio, nelio, i, j + 1, k + 1);
      }

      edellinenNelio = nelio;
    }
  }

  private void haeYSuuntaisetSarmat() {
    for (int k = -1; k < palikka.annaSyvyys(); k++) {
      for (int i = -1; i < palikka.annaLeveys(); i++) {

        haeYViivanSarmat(k, i);

      }
    }
  }

  private void haeYViivanSarmat(int k, int i) {
    viimeisinAlkupiste = null;
    String edellinenNelio = "0000";

    for (int j = 0; j <= palikka.annaKorkeus(); j++) {
      String nelio = nelionSisalto(i, j, k, 2, 1, 2);

      if (onkoTahko(nelio)) {
        kasitteleTahko(edellinenNelio, i + 1, j, k + 1);
        nelio = "1111";
      } else if (liittyykoPaatepisteeseen(edellinenNelio, nelio)) {
        kasitteleMuuttuvaSarma(edellinenNelio, nelio, i + 1, j, k + 1);
      }

      edellinenNelio = nelio;
    }
  }

  private void haeZSuuntaisetSarmat() {
    for (int j = -1; j < palikka.annaKorkeus(); j++) {
      for (int i = -1; i < palikka.annaLeveys(); i++) {

        haeZViivanSarmat(j, i);

      }
    }
  }

  private void haeZViivanSarmat(int j, int i) {
    viimeisinAlkupiste = null;
    String edellinenNelio = "0000";

    for (int k = 0; k <= palikka.annaSyvyys(); k++) {
      String nelio = nelionSisalto(i, j, k, 2, 2, 1);

      if (onkoTahko(nelio)) {
        kasitteleTahko(edellinenNelio, i + 1, j + 1, k);
        nelio = "1111";
      } else if (liittyykoPaatepisteeseen(edellinenNelio, nelio)) {
        kasitteleMuuttuvaSarma(edellinenNelio, nelio, i + 1, j + 1, k);
      }

      edellinenNelio = nelio;
    }
  }

	//kaikkiin suuntiin patevat metodit:
  private String nelionSisalto(int indI, int indJ, int indK, int leveys, int korkeus, int syvyys) {
		// jarjestys: 12
    // 			  34
    // merkinnat: 0 = tyhja, 1 = tippuva

    String nelio = "";

    for (int i = 0; i < leveys; i++) {
      for (int j = 0; j < korkeus; j++) {
        for (int k = 0; k < syvyys; k++) {
          if (onkoRuudussaTippuva(indI + i, indJ + j, indK + k)) {
            nelio += "1";
          } else {
            nelio += "0";
          }
        }
      }
    }

    return nelio;
  }

  private boolean onkoRuudussaTippuva(int i, int j, int k) {
    if (i < 0 || i > palikka.annaLeveys() - 1 || j < 0 || j > palikka.annaKorkeus() - 1 || k < 0 || k > palikka.annaSyvyys() - 1) {
      return false;
    }

    if (!palikka.onkoTyhja(i, j, k)) {
      return true;
    }
    return false;
  }

  private boolean onkoTahko(String nelio) {
    if (nelio.equals("1010") || nelio.equals("0101") || nelio.equals("1100") || nelio.equals("0011")) {
      return true;
    }
    return false;
  }

  private boolean liittyykoPaatepisteeseen(String edellinenNelio, String nelio) {
    if (nelio.equals(edellinenNelio)) {
      return false;
    }
    if (nelio.equals("0000") || nelio.equals("1111")) {
      if (edellinenNelio.equals("0000") || edellinenNelio.equals("1111")) {
        return false;
      }
    }

    return true;
  }

  private void kasitteleTahko(String edellinenNelio, int loppupisteenX, int loppupisteenY, int loppupisteenZ) {
    if (edellinenNelio.equals("0000") || edellinenNelio.equals("1111")) {
      //jos edellinen oli tahko niin uudelleen nimetty 1111:ksi
      return;
    }

    Koordinaatti loppupiste = new Koordinaatti(loppupisteenX, loppupisteenY, loppupisteenZ);
    lisaaSarma(viimeisinAlkupiste, loppupiste);
  }

  private void kasitteleMuuttuvaSarma(String edellinenNelio, String nelio, int alkuX, int alkuY, int alkuZ) {
    //alkaa uusi suora
    if (edellinenNelio.equals("0000") || edellinenNelio.equals("1111")) {
      viimeisinAlkupiste = new Koordinaatti(alkuX, alkuY, alkuZ);
    } //suora loppuu
    else if (nelio.equals("0000") || nelio.equals("1111")) {
      Koordinaatti loppupiste = new Koordinaatti(alkuX, alkuY, alkuZ);
      lisaaSarma(viimeisinAlkupiste, loppupiste);
    } //edellinen suora loppuu, uusi suora alkaa
    else {
      Koordinaatti loppupiste = new Koordinaatti(alkuX, alkuY, alkuZ);
      lisaaSarma(viimeisinAlkupiste, loppupiste);

      viimeisinAlkupiste = new Koordinaatti(alkuX, alkuY, alkuZ);
    }
  }

  private void lisaaSarma(Koordinaatti alku, Koordinaatti loppu) {
    if (sarmat.containsKey(alku)) {
      sarmat.get(alku).add(loppu);
    } else if (sarmat.containsKey(loppu)) {
      sarmat.get(loppu).add(alku);
    } else {
      ArrayList<Koordinaatti> loppupaat = new ArrayList<Koordinaatti>();
      loppupaat.add(loppu);

      sarmat.put(alku, loppupaat);
    }
  }
}
