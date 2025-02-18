package symulacja;

import java.util.ArrayList;

import inwestorzy.InwestorLosowy;
import inwestorzy.InwestorSMA;
import użytkowe.ParaZleceń;
import użytkowe.StrukturaKolejekZleceń;
import wyjątki.*;
import inwestorzy.Inwestor;
import użytkowe.ParaKolejekZleceń;
import użytkowe.ParaSpółkaLiczba;
import zlecenia.TypZlecenia;
import zlecenia.Zlecenie;

import java.util.HashMap;

// Simulation data and API for investors
public class DaneSymulacji {

    // Data

    // number of days
    private int liczbaDni;
    // shares prices
    private HashMap<String, Integer> cenyAkcji;
    // current session number
    private int obecnaTura;
    // current turn
    private int obecnaKolejność;
    // array of companies
    private final ArrayList<String> spółki;

    // Technicalities

    public DaneSymulacji(ArrayList<String> spółki, HashMap<String, Integer> cenyAkcji, int liczbaDni) {
        this.spółki = spółki;
        this.obecnaTura = 0;
        this.liczbaDni = liczbaDni;
        this.cenyAkcji = cenyAkcji;
        this.obecnaKolejność = 0;
    }

    // Operations

    public int liczbaDni() {
        return this.liczbaDni;
    }
    public int liczbaSpółek() {
        return this.spółki.size();
    }
    public int obecnaTura() {
        return this.obecnaTura;
    }

    // Returns the name of the company with given index
    public String dajSpółkę(int i) {
        try {
            return this.spółki.get(i);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Niepoprawny indeks spółki");
            e.printStackTrace();
            return "";
        }
    }

    // Returns the price of given company's shares
    public int cenaSpółki(String spółka) throws NiepoprawneZlecenie {
        Integer wynik = this.cenyAkcji.get(spółka);
        if (wynik == null)
            throw new NiepoprawneZlecenie();
        return wynik;
    }



    // prepares the next session
    public int kolejnaTura() {
        this.obecnaKolejność = 0;
        return ++this.obecnaTura;
    }

    // updates the turn
    public int zwiększKolejność() {
        return this.obecnaKolejność++;
    }

    // updates a company's share price
    public void zaktualizujCenęAkcji(String spółka, int nowaCena) {
        this.cenyAkcji.put(spółka, nowaCena);
    }
}
