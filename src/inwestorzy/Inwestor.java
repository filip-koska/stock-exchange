package inwestorzy;

import java.util.HashMap;

import main.Losowanie;
import symulacja.DaneSymulacji;
import symulacja.Symulacja;
import zlecenia.*;
import wyjątki.NiepoprawnaTransakcja;

public abstract class Inwestor {

    // Dane
    protected  int id;
    protected int balans;
    protected HashMap<String, Integer> liczbaAkcji;
    protected DaneSymulacji daneSymulacji;

    // Techniczne
    public Inwestor(int id, int balans, HashMap<String, Integer> liczbaAkcji, DaneSymulacji daneSymulacji) {
        this.id = id;
        this.balans = balans;
        this.liczbaAkcji = new HashMap<>(liczbaAkcji);
        this.daneSymulacji = daneSymulacji;
    }

    @Override
    public String toString() {
        String wyn = "Inwestor " + this.id + ": końcowy balans: " + this.balans;
        wyn += "\nAkcje: ";
        for (String s : this.liczbaAkcji.keySet())
            wyn += s + ":" + this.liczbaAkcji.get(s) + " ";
        return wyn;
    }

    // Operacje
    public int balans() {
        return this.balans;
    }

    public int liczbaAkcji(String spółka) {
        return this.liczbaAkcji.get(spółka);
    }

    // metoda służąca do odpytania inwestora o decyzję inwestycyjną
    public abstract Zlecenie złóżZlecenie();

    public void zmieńBalans(int zmiana) throws NiepoprawnaTransakcja {
        this.balans += zmiana;
        if (this.balans < 0)
            throw new NiepoprawnaTransakcja();
    }

    public void zmieńAkcje(String akcja, int zmiana) throws NiepoprawnaTransakcja {
        if (!this.liczbaAkcji.containsKey(akcja))
            throw new NiepoprawnaTransakcja();
        this.liczbaAkcji.put(akcja, this.liczbaAkcji.get(akcja) + zmiana);
        if (this.liczbaAkcji.get(akcja) < 0)
            throw new NiepoprawnaTransakcja();
    }

    // tworzy odpowiedni typ zlecenia z podanymi parametrami
    protected Zlecenie stwórzZlecenie(TerminZlecenia terminZlecenia, TypZlecenia typZlecenia, String spółka,
                                    int liczbaAkcji, int limitCeny, int turaZłożenia, int ostatniaTura) {
        return switch (terminZlecenia) {
            case BT -> new ZlecenieBezterminowe(typZlecenia, spółka, liczbaAkcji, limitCeny,
                    turaZłożenia, this.daneSymulacji.zwiększKolejność(), ostatniaTura, this);
            case NA -> new ZlecenieNatychmiastowe(typZlecenia, spółka, liczbaAkcji, limitCeny,
                    turaZłożenia, this.daneSymulacji.zwiększKolejność(), ostatniaTura, this);
            case CZ -> new ZlecenieCzasowe(typZlecenia, spółka, liczbaAkcji, limitCeny,
                    turaZłożenia, this.daneSymulacji.zwiększKolejność(), ostatniaTura, this);
        };
    }

    protected TerminZlecenia losujTermin() {
        int termin = Losowanie.losuj(0, Symulacja.LICZBA_TERMINÓW_ZLECEŃ - 1);
        return TerminZlecenia.values()[termin];
    }

    protected int losujCenę(String spółka) {
        int ostatniaCena = this.daneSymulacji.cenaSpółki(spółka);
        return Losowanie.losuj(Math.max(1, ostatniaCena - 10), ostatniaCena + 10);
    }

    // zakładamy, że żaden inwestor nie chce za daną cenę sprzedać więcej akcji, niż mógłby ich za nią kupić
    protected int losujLiczbęAkcji(int limitCeny, String spółka, TypZlecenia typZlecenia) {
        int maxLiczbaAkcji = this.balans / limitCeny;
        return Losowanie.losuj(0, Math.min(maxLiczbaAkcji, this.liczbaAkcji(spółka)));
    }

    // metoda aktualizująca dane inwestora na koniec tury giełdowej
    public abstract void koniecTury();

}
