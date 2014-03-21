package peli.tekoaly;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.Timer;
import java.util.Random;
import java.util.Collection;

public abstract class TekoAly extends Timer implements ActionListener {
  
  private DemoPeli peli;
	private SiirtoSuunnitelma siirtoSuunnitelma;
	private static final Random RND = new Random();

	public static TekoAly annaTekoAly(DemoPeli peli, int viive) {
		return new AlykasTekoAly(peli, viive);
		
		/*
		int pick = RND.nextInt(1);
		switch(pick) {
			case 2:
				return new AlykasTekoAly(peli, viive);
			case 1:
			default:
				return new SatunnainenTekoAly(peli, viive);
		}
		*/
	}

	/* Tehty:
	 * battle plan -> palikan vaatimat ohjeet
	 * battle planin toteuttaja, satunnainen/deterministinen
	 * battle plan enum: kierrot, siirrot
	 * pelin käynnistäminen vaikuttaa tekoälyyn
	 * toteuttaja pudottaa aina lopuksi
	 * Tarvitaan:
	 * battle planin suunnittelija
	 * mahdollisuusavaruuden läpikäyjä
	 * mahdollisuuden arvioija/rating function
	 */

  public TekoAly(DemoPeli peli, int viive) {
		super(viive, null);
		super.addActionListener(this);

    this.peli = peli;

		this.setDelay(viive);
		this.setRepeats(true);
  }
	
	/* Metodi, jota kutsutaan aina kun peliin on tullut uusi palikka */
	public void uusiPalikka() {
	}

	/**
	* Vastaanottaa ajastimen tapahtuman. 
	* Suorittaa tekoälyn seuraavan siirron.
	* @param ae Ajastimen tapahtuma
	*/
	@Override
	public void actionPerformed(ActionEvent ae) {
		if (siirtoSuunnitelma != null) {
			siirtoSuunnitelma.toteutaSiirto();
		}
	}
	public DemoPeli annaPeli() {
		return peli;
	}
	public SiirtoSuunnitelma annaSiirtoSuunnitelma() {
		return siirtoSuunnitelma;
	}
	public void asetaSiirtoSuunnitelma(SiirtoSuunnitelma uusi) {
		siirtoSuunnitelma = uusi;
	}
	public void asetaSiirtoSuunnitelma(Collection<Siirto> siirrot) {
		asetaSiirtoSuunnitelma(new SiirtoSuunnitelma(annaPeli(), siirrot));
	}


}
