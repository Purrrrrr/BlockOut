package kayttoliittyma;

import valmiskomponentit.Ikkuna;
import peli.asetukset.PelinAsetukset;
import peli.ennatyslista.Ennatyslistaaja;
import peli.tekoaly.DemoPeli;
import java.awt.BorderLayout;

public class DemoIkkuna extends Ikkuna {

  private DemoPeli peli;
  private PelinAsetukset asetukset;
  private Ennatyslistaaja ennatykset;
  private BlockOut kayttis;

  public DemoIkkuna(BlockOut kayttis, PelinAsetukset asetukset, Ennatyslistaaja ennatykset) {
    super(new BorderLayout());
    this.kayttis = kayttis;
    this.asetukset = asetukset;
    this.ennatykset = ennatykset;

    alustaUusiPeli();
  }
  public BlockOut getKayttis() {
    return kayttis;
  }
  public void pysaytaPeli() {
    peli.asetaPeliTauolle(true);
  }
  public void alustaUusiPeli() {
    if (kayttis.onkoPeliKaynnissa()) return;
    this.ennatykset.poistaEnnatyslistanKysely();

    this.peli = new DemoPeli(this, asetukset.annaValitutAsetukset(), ennatykset);

    this.peli.aloitaPeli();
    this.removeAll();
    this.add(peli, BorderLayout.CENTER);
    this.revalidate();
  }

}
