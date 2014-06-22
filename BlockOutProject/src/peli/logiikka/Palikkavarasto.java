package peli.logiikka;

import java.util.ArrayList;
import java.util.Random;

public class Palikkavarasto {

  private ArrayList<Palikka> palikat;
  private Palikkasetti setti;

  /**
   * Sisaltaa pelin kaikki palikat. Antaa tarvittaessa Palikkasetin perusteella
   * satunnaisen palikan.
   *
   * @param setti Palikkasetti
   */
  public Palikkavarasto(Palikkasetti setti) {
    this.palikat = new ArrayList<Palikka>();
    this.setti = setti;

    luoPalikat();
  }

  private void luoPalikat() {
    /*
     Palikka p = new Palikka(1, 2, 10, new int[]{
     1,1,1,
     palikat.add( p );
		
     p = new Palikka(7, 5, 80, new int[]{
     3,4,4,
     1,4,4,
     2,4,4,
     5,4,4,
     6,4,4,
     7,4,4,
     palikat.add( p );
     */
    if (setti == Palikkasetti.FLAT) {
      luoFlatExtendedPalikat();
      luoFlatBasicExtendedPalikat();
    }
    if (setti == Palikkasetti.BASIC) {
      luoFlatBasicExtendedPalikat();
      luoBasicExtendedPalikat();
    }
    if (setti == Palikkasetti.EXTENDED) {
      luoFlatExtendedPalikat();
      luoFlatBasicExtendedPalikat();
      luoBasicExtendedPalikat();
      luoExtendedPalikat();
    }

  }

  private void luoFlatExtendedPalikat() {
    Palikka p = new Palikka(14, 156, new int[]{
      3, 3, 3});
    palikat.add(p);

    p = new Palikka(14, 156, new int[]{
      3, 3, 3,
      3, 4, 3});
    palikat.add(p);

    p = new Palikka(27, 307, new int[]{
      3, 3, 3,
      3, 4, 3,
      3, 2, 3});
    palikat.add(p);

    p = new Palikka(14, 156, new int[]{
      3, 3, 3,
      3, 4, 3,
      4, 3, 3,
      4, 4, 3});
    palikat.add(p);
  }

  private void luoFlatBasicExtendedPalikat() {
    Palikka p = new Palikka(27, 307, new int[]{
      3, 3, 3,
      3, 4, 3,
      4, 3, 3});
    palikat.add(p);

    p = new Palikka(40, 461, new int[]{
      3, 3, 3,
      3, 4, 3,
      3, 2, 3,
      4, 3, 3});
    palikat.add(p);

    p = new Palikka(40, 461, new int[]{
      3, 3, 3,
      3, 4, 3,
      4, 3, 3,
      4, 2, 3});
    palikat.add(p);

    p = new Palikka(27, 307, new int[]{
      3, 3, 3,
      3, 4, 3,
      3, 2, 3,
      4, 2, 3});
    palikat.add(p);

  }

  private void luoBasicExtendedPalikat() {
    Palikka p = new Palikka(53, 552, new int[]{
      3, 3, 3,
      3, 2, 3,
      3, 3, 2,
      2, 3, 2});
    palikat.add(p);

    p = new Palikka(53, 552, new int[]{
      3, 3, 3,
      3, 2, 3,
      3, 3, 2,
      4, 3, 2});
    palikat.add(p);

    p = new Palikka(53, 552, new int[]{
      3, 3, 3,
      3, 2, 3,
      3, 3, 2,
      4, 3, 3});
    palikat.add(p);
  }

  private void luoExtendedPalikat() {
    Palikka p = new Palikka(27, 307, new int[]{
      3, 3, 3,
      3, 2, 3,
      3, 4, 3,
      3, 5, 3});
    palikat.add(p);

    p = new Palikka(27, 307, new int[]{
      3, 3, 3,
      3, 1, 3,
      3, 2, 3,
      3, 4, 3,
      3, 5, 3});
    palikat.add(p);

    p = new Palikka(79, 921, new int[]{
      3, 3, 3,
      3, 2, 3,
      3, 1, 3,
      4, 3, 3,
      5, 3, 3});
    palikat.add(p);

    p = new Palikka(79, 921, new int[]{
      3, 3, 3,
      3, 2, 3,
      3, 4, 3,
      4, 3, 3,
      5, 3, 3});
    palikat.add(p);

    p = new Palikka(79, 921, new int[]{
      3, 3, 3,
      3, 2, 3,
      3, 4, 3,
      2, 3, 3,
      4, 4, 3});
    palikat.add(p);

    p = new Palikka(79, 921, new int[]{
      3, 3, 3,
      3, 2, 3,
      3, 4, 3,
      2, 2, 3,
      4, 4, 3});
    palikat.add(p);

    p = new Palikka(66, 780, new int[]{
      3, 3, 3,
      2, 3, 3,
      2, 2, 3,
      3, 4, 3,
      4, 4, 3});
    palikat.add(p);

    p = new Palikka(66, 780, new int[]{
      3, 3, 3,
      3, 2, 3,
      3, 1, 3,
      3, 4, 3,
      4, 4, 3});
    palikat.add(p);

    p = new Palikka(79, 921, new int[]{
      3, 3, 3,
      3, 2, 3,
      3, 1, 3,
      3, 4, 3,
      4, 3, 3});
    palikat.add(p);

    p = new Palikka(105, 1248, new int[]{
      3, 3, 3,
      3, 2, 3,
      3, 4, 3,
      4, 2, 3,
      4, 4, 3});
    palikat.add(p);

    p = new Palikka(40, 461, new int[]{
      3, 3, 3,
      3, 2, 3,
      3, 4, 3,
      4, 3, 3,
      4, 4, 3});
    palikat.add(p);

    p = new Palikka(79, 921, new int[]{
      3, 3, 3,
      3, 2, 3,
      3, 1, 3,
      4, 3, 3,
      4, 4, 3});
    palikat.add(p);

    p = new Palikka(118, 1402, new int[]{
      3, 3, 3,
      3, 4, 3,
      3, 2, 3,
      2, 3, 3,
      4, 3, 3});
    palikat.add(p);

    p = new Palikka(131, 1379, new int[]{
      3, 3, 3,
      3, 2, 3,
      3, 4, 3,
      4, 4, 3,
      4, 4, 2});
    palikat.add(p);

    p = new Palikka(131, 1379, new int[]{
      3, 3, 3,
      3, 2, 3,
      3, 4, 3,
      2, 4, 3,
      2, 4, 2});
    palikat.add(p);

    p = new Palikka(92, 965, new int[]{
      3, 3, 3,
      3, 2, 3,
      3, 4, 3,
      4, 3, 3,
      4, 3, 2});
    palikat.add(p);

    p = new Palikka(92, 965, new int[]{
      3, 3, 3,
      3, 4, 3,
      3, 2, 3,
      4, 2, 3,
      3, 3, 2});
    palikat.add(p);

    p = new Palikka(92, 965, new int[]{
      3, 3, 3,
      3, 4, 3,
      3, 2, 3,
      2, 2, 3,
      3, 3, 2});
    palikat.add(p);

    p = new Palikka(131, 1379, new int[]{
      3, 3, 3,
      3, 2, 3,
      2, 3, 3,
      2, 4, 3,
      2, 4, 2});
    palikat.add(p);

    p = new Palikka(131, 1379, new int[]{
      3, 3, 3,
      3, 2, 3,
      4, 3, 3,
      4, 4, 3,
      4, 4, 2});
    palikat.add(p);

    p = new Palikka(105, 1103, new int[]{
      3, 3, 3,
      3, 2, 3,
      4, 3, 3,
      4, 3, 2,
      4, 4, 2});
    palikat.add(p);

    p = new Palikka(105, 1103, new int[]{
      3, 3, 3,
      3, 2, 3,
      2, 3, 3,
      2, 3, 2,
      2, 4, 2});
    palikat.add(p);

    p = new Palikka(131, 1379, new int[]{
      3, 3, 3,
      3, 2, 3,
      4, 2, 3,
      3, 4, 3,
      3, 4, 2});
    palikat.add(p);

    p = new Palikka(131, 1379, new int[]{
      3, 3, 3,
      3, 2, 3,
      2, 2, 3,
      3, 4, 3,
      3, 4, 2});
    palikat.add(p);

    p = new Palikka(79, 827, new int[]{
      3, 3, 3,
      2, 3, 3,
      4, 3, 3,
      3, 4, 3,
      3, 3, 2});
    palikat.add(p);

    p = new Palikka(40, 461, new int[]{
      3, 3, 3,
      4, 3, 3,
      3, 4, 3,
      4, 4, 3,
      3, 3, 2});
    palikat.add(p);

    p = new Palikka(105, 1103, new int[]{
      3, 3, 3,
      3, 2, 3,
      3, 4, 3,
      4, 4, 3,
      3, 4, 2});
    palikat.add(p);

    p = new Palikka(105, 1103, new int[]{
      3, 3, 3,
      3, 2, 3,
      4, 3, 3,
      3, 3, 2,
      3, 4, 2});
    palikat.add(p);

    p = new Palikka(105, 1103, new int[]{
      3, 3, 3,
      3, 2, 3,
      2, 3, 3,
      3, 3, 2,
      3, 4, 2});
    palikat.add(p);

    p = new Palikka(131, 1379, new int[]{
      3, 3, 3,
      3, 2, 3,
      4, 3, 3,
      3, 2, 2,
      4, 3, 2});
    palikat.add(p);
  }

  /**
   * Antaa satunnaisen palikan kayttajan valitsemasta palikkasetista.
   *
   * @return Satunnainen palikka
   */
  public Palikka annaPalikka() {
    Random arpoja = new Random();

    return palikat.get(arpoja.nextInt(palikat.size())).kopioi();
  }

  /**
   * Antaa kayttajan maaritteleman palikkasetin.
   *
   * @return Palikkasetti
   */
  public Palikkasetti annaPalikkasetti() {
    return this.setti;
  }
}
