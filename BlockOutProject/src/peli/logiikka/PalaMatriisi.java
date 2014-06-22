package peli.logiikka;

public class PalaMatriisi {

  /**
   * 8x8 bittikartta, joka kuvastaa yhtä kerrosta matriisissa *
   */
  static class PalaKerros {

    private long kerros;

    public static final char TAYSIRIVI = 255;
    public static final long TAYSIKARTTA = -1L;

    public PalaKerros() {
      this.kerros = 0L;
    }

    public PalaKerros(long kerros) {
      this.kerros = kerros;
    }

    public PalaKerros(PalaKerros toinen) {
      this.kerros = toinen.kerros;
    }

    public boolean onkoTyhja(int x, int y) {
      long maski = 1L << annaSijaintiShiftaus(x, y);
      return (this.kerros & maski) == 0L;
    }

    public void asetaKohta(int x, int y, boolean onkoAsetettu) {

      long maski = 1L << annaSijaintiShiftaus(x, y);
			//System.out.println(annaSijaintiShiftaus(x,y));
      ////System.out.println(maski);
      if (onkoAsetettu) {
        this.kerros = this.kerros | maski;
      } else {
        this.kerros = this.kerros & (~maski);
      }
    }

    public void asetaPalat(PalaKerros toinen) {
      this.kerros = toinen.kerros;
    }
    /*
     public void asetaPalat(long uusiKerros) {
     this.kerros = uusiKerros;
     }
     public void asetaPalat(boolean[][] uusiKerros) {
     this.kerros = kerros;
     }
     */

    public boolean onkoPaallekkain(PalaKerros toinen, int x, int y) {
      long siirretty = siirrettyKerros(toinen.kerros, x, y);
      siirretty = siirretty & ~annaSiirtoMaski(x, y);
      return (this.kerros & siirretty) != 0L;
    }

    public boolean meneekoSiirtoReunanYli(int x, int y) {
      return (annaSiirtoMaski(x, y) & kerros) != 0L;
    }
    /* Palauttaa maskin, jossa kaikki siirron syrjäyttämät alueet ovat nollia */

    private static long annaSiirtoMaski(int x, int y) {
			//Maski, joka on aluksi pelkkiä ykkösiä, siirretään siten, että
      //siirron vuoksi yli menevät rivit ovat nollia
      long rivimaski = siirrettyKerros(TAYSIKARTTA, 0, y);

			//Samanlainen sarakkeiden siirto. 
      //Otetaan ensiksi ensimmäinen rivi:
      long sarakemaski = siirrettyKerros(TAYSIRIVI, x, 0) & TAYSIRIVI;
      //Muutetaan rivi kaksinkertaistamalla koko kerrokseksi
      sarakemaski = (sarakemaski + (sarakemaski << 8));
      sarakemaski = (sarakemaski + (sarakemaski << 16));
      sarakemaski = (sarakemaski + (sarakemaski << 32));
      //System.out.println(new PalaKerros(maski));

      return ~(sarakemaski & rivimaski);
    }

    public void lisaaPalat(PalaKerros toinen, int x, int y) {
      long siirretty = siirrettyKerros(toinen.kerros, x, y);
      siirretty = siirretty & ~annaSiirtoMaski(x, y);
      this.kerros = this.kerros | siirretty;
    }

    private static int annaSijaintiShiftaus(int x, int y) {
      return x + (y << 3);
    }

    private static long siirrettyKerros(long siirrettava, int dx, int dy) {
      int siirto = annaSijaintiShiftaus(dx, dy);
      if (siirto > 0) {
        return siirrettava >>> siirto;
      } else {
        return siirrettava << (-siirto);
      }
    }

    @Override
    public String toString() {
      String tulos = "";
      long rivit = this.kerros;

      for (int y = 0; y < 8; y++) {
        long rivi = rivit & 255;
        for (int mask = 1; mask < 256; mask = mask << 1) {
          tulos += (rivi & mask) != 0 ? 1 : 0;
        }
        tulos += "\n";
        rivit = rivit >>> 8;
      }
      return tulos;
    }
  }

  private Pala[][][] matriisi;

  public PalaMatriisi(int leveys, int korkeus, int syvyys) {
    matriisi = new Pala[leveys][korkeus][syvyys];

    for (int i = 0; i < leveys; i++) {
      for (int j = 0; j < korkeus; j++) {
        for (int k = 0; k < syvyys; k++) {
          matriisi[i][j][k] = Pala.TYHJA;
        }
      }
    }
  }

  public PalaMatriisi(int koko) {
    matriisi = new Pala[koko][koko][koko];

    for (int i = 0; i < koko; i++) {
      for (int j = 0; j < koko; j++) {
        for (int k = 0; k < koko; k++) {
          matriisi[i][j][k] = Pala.TYHJA;
        }
      }
    }
  }

  public PalaMatriisi(Pala[][][] m) {
    matriisi = m;
  }

  public Pala[][][] annaPalat() {
    return matriisi;
  }

  public boolean onkoTyhja(int x, int y, int z) {
    return matriisi[x][y][z] == Pala.TYHJA;
  }

  public void asetaKohdanTyhjyys(int x, int y, int z, boolean tyhjako) {
    matriisi[x][y][z] = tyhjako ? Pala.TYHJA : Pala.TIPPUVA;
  }

  public int annaLeveys() {
    return matriisi.length;
  }

  public int annaKorkeus() {
    return matriisi[0].length;
  }

  public int annaSyvyys() {
    return matriisi[0][0].length;
  }

  /* Placeholder methods */
  public boolean mahtuukoPalikka(PalaMatriisi palikka, int x, int y, int z) {
    return true;
  }

  public void lisaaPalikka(PalaMatriisi palikka, int x, int y, int z) {

  }

  public static void main(String[] args) {
    long[] testb = {
      0b00000000,
      0b00000000,
      0b00000000,
      0b00000000,
      0b00000000,
      0b00000000,
      0b10000000,
      0b00000000
    };
    long[] test = {
      0b00000000,
      0b00000000,
      0b00000000,
      0b00000000,
      0b00000000,
      0b00000001,
      0b00000000,
      0b00000000
    };
    //System.out.println(m);
    PalaMatriisi.PalaKerros k = new PalaKerros(compose(testb));
    PalaMatriisi.PalaKerros k2 = new PalaKerros(compose(test));
    System.out.println(k);
    System.out.println(k2);
		//System.out.println(k.meneekoSiirtoReunanYli(3,2));
    //k.lisaaPalat(k, 3,2);
    boolean b = true;
    for (int i = 0; i < 10000; i++) {
      b = !b;
      k.asetaKohta(3, 3, b);
      k2.onkoPaallekkain(k, -1, 0);
      k.onkoPaallekkain(k2, -1, 0);
    }
  }

  private static long compose(long[] ms) {
    long c = 0;
    for (int l = 0; l < ms.length; l++) {
      c = (c << 8) + ms[l];
    }
    return c;
  }

  public boolean equals(Object other) {
    if (!(other instanceof PalaMatriisi)) {
      return false;
    }

    return this.equals((PalaMatriisi) other);
  }

  public boolean equals(PalaMatriisi that) {
    if (this.annaLeveys() != that.annaLeveys()) {
      return false;
    }
    if (this.annaKorkeus() != that.annaKorkeus()) {
      return false;
    }
    if (this.annaSyvyys() != that.annaSyvyys()) {
      return false;
    }

    for (int k = 0; k < matriisi[0][0].length; k++) {
      for (int j = 0; j < matriisi[0].length; j++) {
        for (int i = 0; i < matriisi.length; i++) {
          if (matriisi[i][j][k] != that.matriisi[i][j][k]) {
            return false;
          }
        }
      }
    }

    return true;
  }

  public int hashCode() {
    int code = 0;
    int exp = 1;

    for (int k = 0; k < matriisi[0][0].length; k++) {
      for (int j = 0; j < matriisi[0].length; j++) {
        for (int i = 0; i < matriisi.length; i++) {
          if (matriisi[i][j][k] == Pala.TIPPUVA) {
            int subcode = i << 8 + j << 4 + k;
            code += exp * subcode;
          }
          exp *= 31;
        }
      }
    }
    return code;
  }

}
