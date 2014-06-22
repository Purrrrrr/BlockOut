package peli.tekoaly;

import java.util.List;
import java.util.Deque;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.HashSet;
import java.util.Set;
import peli.logiikka.PalikkaKentalla;
import peli.logiikka.TippuvaPalikka;

public class AlykasTekoAly extends TekoAly {

  public AlykasTekoAly(DemoPeli peli, int viive) {
    super(peli, viive);
  }

  public void uusiPalikka() {
    ArrayList<Siirto> siirrot = new ArrayList<Siirto>(7);
    for (int i = 0; i < 7; i++) {
      siirrot.add(Siirto.satunnainenSiirto());
    }
    annaPyoraytykset();
    asetaSiirtoSuunnitelma(siirrot);
  }

  public List<PalikkaKentalla> annaPyoraytykset() {
    //Siirto[] esiinkierrot = {Siirto.VASENESILLE, Siirto.OIKEAESILLE, Siirto.ALAESILLE, Siirto.YLAESILLE};
    Siirto[] siirrot = Siirto.values();
    TippuvaPalikka tippuva = annaPeli().annaTippuvaPalikka();
    PalikkaKentalla juuri = new PalikkaKentalla(tippuva.annaPalikka(), tippuva.annaKentta());

    Set<PalikkaKentalla> tutkitut = new HashSet<PalikkaKentalla>();
    List<PalikkaKentalla> tiputetut = new LinkedList<PalikkaKentalla>();
    Deque<PalikkaKentalla> tutkittavat = new LinkedList<PalikkaKentalla>();

    tutkittavat.add(juuri);

    while (!tutkittavat.isEmpty()) {
      PalikkaKentalla tutkittava = tutkittavat.pop();
      tutkitut.add(tutkittava);
      //System.out.println("Tutkittava: "+tutkittava.hashCode());

      for (Siirto s : siirrot) {
        PalikkaKentalla uusi = new PalikkaKentalla(tutkittava);
        if (!s.suorita(uusi)) {
          continue;
        }

				//System.out.println("Naapuri: "+uusi.hashCode());
        if (!tutkitut.contains(uusi)) {
          tutkittavat.add(uusi);
          //System.out.println("Lisätty: "+uusi.hashCode());
        }
      }
      /*for(PalikkaKentalla p : tutkitut) {
       //System.out.println("Jo tutkittu: "+p.hashCode());
       }*/

    }

    return null;
  }
}
