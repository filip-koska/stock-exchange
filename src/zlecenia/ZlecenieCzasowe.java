package zlecenia;

import inwestorzy.Inwestor;
import symulacja.DaneSymulacji;

// Fixed-expiration order
public class ZlecenieCzasowe extends Zlecenie {
    public ZlecenieCzasowe(TypZlecenia typZlecenia, String spółka, int liczbaAkcji, int limitCeny,
                           int turaZłożenia, int kolejnośćZłożenia, int ostatniaTura, Inwestor inwestor) {
        super(typZlecenia, spółka, liczbaAkcji, limitCeny, turaZłożenia,
                kolejnośćZłożenia, ostatniaTura, inwestor);
        this.terminZlecenia = TerminZlecenia.valueOf("CZ");
    }
}
