package peli.tekoaly;

import peli.logiikka.TippuvaPalikka;

import java.util.Collection;
import java.util.Deque;
import java.util.ArrayDeque;

public class SiirtoSuunnitelma {

  DemoPeli peli;
  TippuvaPalikka palikka;
  Deque<Siirto> siirrot;

  public SiirtoSuunnitelma(DemoPeli peli, Collection<Siirto> siirrot) {
    this.peli = peli;
    this.palikka = peli.annaTippuvaPalikka();
    this.siirrot = new ArrayDeque<Siirto>(siirrot);
  }

  public void toteutaSiirto() {
    if (peli.annaTippuvaPalikka() != palikka) {
      return;
    }
    if (siirrot.size() > 0) {
      siirrot.pop().suorita(palikka);
    } else {
      palikka.tiputaPohjalle();
    }
  }

}
