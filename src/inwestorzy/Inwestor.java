package inwestorzy;

import java.util.HashMap;

import main.Losowanie;
import symulacja.DaneSymulacji;
import symulacja.Symulacja;
import zlecenia.*;
import wyjątki.NiepoprawnaTransakcja;

public abstract class Inwestor {

    // Data

    // unique investor ID
    protected  int id;
    // investor account balance
    protected int balans;
    // information about numbers of controlled companies' shares
    protected HashMap<String, Integer> liczbaAkcji;
    // shared simulation data
    protected DaneSymulacji daneSymulacji;

    // Technicalities
    
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

    // Operations

    public int balans() {
        return this.balans;
    }

    // Reads the number of shares of the company
    public int liczbaAkcji(String spółka) {
        return this.liczbaAkcji.get(spółka);
    }

    // Queries the investor about their investment decisions
    public abstract Zlecenie złóżZlecenie();

    // Updates the investor's account balance
    public void zmieńBalans(int zmiana) throws NiepoprawnaTransakcja {
        this.balans += zmiana;
        if (this.balans < 0)
            throw new NiepoprawnaTransakcja();
    }

    // Updates the number of shares of a given company
    public void zmieńAkcje(String akcja, int zmiana) throws NiepoprawnaTransakcja {
        if (!this.liczbaAkcji.containsKey(akcja))
            throw new NiepoprawnaTransakcja();
        this.liczbaAkcji.put(akcja, this.liczbaAkcji.get(akcja) + zmiana);
        if (this.liczbaAkcji.get(akcja) < 0)
            throw new NiepoprawnaTransakcja();
    }

    // Creates a stock order of the appropriate type
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

    // Returns a random order end date
    protected TerminZlecenia losujTermin() {
        int termin = Losowanie.losuj(0, Symulacja.LICZBA_TERMINÓW_ZLECEŃ - 1);
        return TerminZlecenia.values()[termin];
    }

    // Returns a random sale price of a company's shares
    protected int losujCenę(String spółka) {
        int ostatniaCena = this.daneSymulacji.cenaSpółki(spółka);
        return Losowanie.losuj(Math.max(1, ostatniaCena - 10), ostatniaCena + 10);
    }

    // Randomly selects the number of a company's shares the investor will be willing to buy
    protected int losujLiczbęAkcji(int limitCeny, String spółka, TypZlecenia typZlecenia) {
        int maxLiczbaAkcji = this.balans / limitCeny;
        return Losowanie.losuj(0, Math.min(maxLiczbaAkcji, this.liczbaAkcji(spółka)));
    }

    // Updates investor data at the end of a stock session
    public abstract void koniecTury();

}
