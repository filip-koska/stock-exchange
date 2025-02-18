package zlecenia;

import inwestorzy.Inwestor;
import symulacja.DaneSymulacji;

public class ZlecenieBezterminowe extends Zlecenie{
    public ZlecenieBezterminowe(TypZlecenia typZlecenia, String spółka, int liczbaAkcji, int limitCeny,
                                int turaZłożenia, int kolejnośćZłożenia, int ostatniaTura, Inwestor inwestor) {
        super(typZlecenia, spółka, liczbaAkcji, limitCeny, turaZłożenia,
                kolejnośćZłożenia, ostatniaTura, inwestor);
        this.terminZlecenia = TerminZlecenia.valueOf("BT");
    }
}
