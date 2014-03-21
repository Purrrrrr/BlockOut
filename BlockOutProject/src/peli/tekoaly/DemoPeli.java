package peli.tekoaly;

import peli.ennatyslista.Ennatyslistaaja;
import peli.asetukset.logiikka.Asetukset;
import kayttoliittyma.BlockOut;
import kayttoliittyma.DemoIkkuna;
import peli.Peli;
import java.awt.event.KeyListener;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class DemoPeli extends Peli {

  private DemoIkkuna emoIkkuna;
	private TekoAly aly;

  public DemoPeli(DemoIkkuna ikkuna, Asetukset asetukset, Ennatyslistaaja ennatyslistaaja) {
    super(ikkuna.getKayttis(), asetukset, ennatyslistaaja);
    emoIkkuna = ikkuna;
		aly = TekoAly.annaTekoAly(this,400);
    
    //Poistetaan normaali näppäinkuuntelija
    for (KeyListener l : getKeyListeners()) {
      removeKeyListener(l);
    }
  }
  public void asetaUudetAsetukset(Asetukset asetukset) {
    super.asetaUudetAsetukset(asetukset);
    //Poistetaan normaali näppäinkuuntelija
    for (KeyListener l : getKeyListeners()) {
      removeKeyListener(l);
    }
  }

  public void haeUusiPalikkaKenttaan() {
    super.haeUusiPalikkaKenttaan();

    if (this.onkoGameOver()) {
			aly.stop();
      emoIkkuna.alustaUusiPeli();
    } else {
			aly.uusiPalikka();
		}
  }
	public void aloitaPeli() {
		super.aloitaPeli();
		aly.uusiPalikka();
		aly.start();
	}
	public void asetaPeliTauolle(boolean tauolla) {
		super.asetaPeliTauolle(tauolla);
		if (tauolla) aly.stop();
		if (!tauolla && !aly.isRunning()) aly.start();
	}


  /**
  * Piirtaa pelinakyman, jonka päällä on teksti DEMO
  * 
  * @param g Graphics
  */
  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);

    int ikkunanLeveys = this.getWidth();
    int ikkunanKorkeus = this.getHeight();
    
    Font fontti = new Font("futura", Font.PLAIN, 80);
    g.setFont(fontti);
    g.setColor(Color.BLACK);
    g.drawString("BlockOut", ikkunanLeveys/10*3, ikkunanKorkeus/5);
    g.setColor(Color.WHITE);
    g.drawString("BlockOut", ikkunanLeveys/10*3-3, ikkunanKorkeus/5-3);
  }

  /**
  * Kertoo piirretäänkö peliin statistiikat.
  * 
  * @return Aina false 
  */
  public boolean piirretaankoStatistiikka() {
    return false;
  }

}
