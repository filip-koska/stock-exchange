package inwestorzy;

import main.Losowanie;
import symulacja.DaneSymulacji;
import symulacja.Symulacja;
import wyjątki.NiepoprawnaTransakcja;
import wyjątki.NiepoprawneZlecenie;
import zlecenia.*;

import java.util.HashMap;

// Investor who makes random investment decisions
public class InwestorLosowy extends Inwestor {

    // Data

    // Technicalities
    
    public InwestorLosowy(int id, int balans, HashMap<String, Integer> liczbaAkcji, DaneSymulacji daneSymulacji) {
        super(id, balans, liczbaAkcji, daneSymulacji);
    }

    // Operations

    // Randomly selects the type of order the investor is going to submit
    private TypZlecenia losujTyp() {
        int typ = Losowanie.losuj(0, Symulacja.LICZBA_TYPÓW_ZLECEŃ - 1);
        return TypZlecenia.values()[typ];
    }

    // Randomly selects the company whose shares the order will concern
    private String losujSpółkę() {
        int indeksSpółki = Losowanie.losuj(0, this.daneSymulacji.liczbaSpółek() - 1);
        return this.daneSymulacji.dajSpółkę(indeksSpółki);
    }


    @Override
    public Zlecenie złóżZlecenie() {
        TypZlecenia typZlecenia = this.losujTyp();
        TerminZlecenia terminZlecenia = this.losujTermin();
        String spółka = this.losujSpółkę();
        int limitCeny = this.losujCenę(spółka);
        int turaZłożenia = this.daneSymulacji.obecnaTura();
        int ostatniaTura = Losowanie.losuj(turaZłożenia, this.daneSymulacji.liczbaDni() - 1);
        int liczbaAkcji = this.losujLiczbęAkcji(limitCeny, spółka, typZlecenia);
        if (liczbaAkcji == 0)
            return null;
        Zlecenie z = this.stwórzZlecenie(terminZlecenia, typZlecenia, spółka, liczbaAkcji,
                                    limitCeny, turaZłożenia, ostatniaTura);
        return z;
    }

    // Nothing happens for the random investor at the end of the session
    @Override
    public void koniecTury() {}

}
