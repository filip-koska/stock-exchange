package zlecenia;

import inwestorzy.Inwestor;
import symulacja.DaneSymulacji;

// Immediate order
public class ZlecenieNatychmiastowe extends ZlecenieCzasowe {
    public ZlecenieNatychmiastowe(TypZlecenia typZlecenia, String spółka, int liczbaAkcji, int limitCeny,
                                  int turaZłożenia, int kolejnośćZłożenia, int ostatniaTura, Inwestor inwestor) {
        super(typZlecenia, spółka, liczbaAkcji, limitCeny, turaZłożenia,
                kolejnośćZłożenia, ostatniaTura, inwestor);
        this.ostatniaTura = turaZłożenia;
        this.terminZlecenia = TerminZlecenia.valueOf("NA");
    }
}
