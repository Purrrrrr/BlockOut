package peli.tekoaly;

import peli.logiikka.TippuvaPalikka;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.Timer;

import java.util.Random;

public class TekoAly extends Timer implements ActionListener {
  
  DemoPeli peli;

	/* Tehty:
	 * battle plan -> palikan vaatimat ohjeet
	 * battle planin toteuttaja, satunnainen/deterministinen
	 * Tarvitaan:
	 * toteuttaja pudottaa aina lopuksi
	 * battle planin suunnittelija
	 * mahdollisuusavaruuden läpikäyjä
	 * mahdollisuuden arvioija/rating function
	 * battle plan enum: kierrot, siirrot
	 * pelin käynnistäminen vaikuttaa tekoälyyn
	 */

  public TekoAly(DemoPeli peli, int viive) {
		super(viive, null);
		super.addActionListener(this);

    this.peli = peli;

		this.setDelay(viive);
		this.setRepeats(true);
		//this.start();
  }

	public void uusiPalikka() {

	}

	/**
	* Vastaanottaa ajastimen tapahtuman. Pyorayttaa tippuvaa palikkaa oikeampaa suuntaan jos tippuva palikka on yha sama eika se ole tippunut pohjalle. Nollaa kuvan viiveen jos peli on tauolla.
	* 
	* @param ae Ajastimen tapahtuma
	*/
	@Override
	public void actionPerformed(ActionEvent ae) {
		/* if (peli.annaTippuvaPalikka() != tippuvaPalikka) {
			return;
		} */

		int pick = new Random().nextInt(Siirto.values().length);
		Siirto seuraava = Siirto.values()[pick];

		seuraava.suorita(peli.annaTippuvaPalikka());
		
		//peli.annaTippuvaPalikka().tiputaPohjalle();
	}




}
