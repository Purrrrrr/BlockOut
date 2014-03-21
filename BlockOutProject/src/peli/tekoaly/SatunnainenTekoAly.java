package peli.tekoaly;
import java.util.ArrayList;

public class SatunnainenTekoAly extends TekoAly {

  public SatunnainenTekoAly(DemoPeli peli, int viive) {
		super(peli,viive);
	}

	public void uusiPalikka() {
		ArrayList<Siirto> siirrot = new ArrayList<Siirto>(7);
		for(int i = 0; i < 7; i++) {
			siirrot.add(Siirto.satunnainenSiirto());
		}
		asetaSiirtoSuunnitelma(siirrot);
	}
}
