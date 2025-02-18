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

// klasa służąca jako API symulacji dla inwestorów
public class DaneSymulacji {

    // Dane
    private int liczbaDni;
    private HashMap<String, Integer> cenyAkcji;
    private int obecnaTura;
    private int obecnaKolejność;
    private final ArrayList<String> spółki;

    // Techniczne

    public DaneSymulacji(ArrayList<String> spółki, HashMap<String, Integer> cenyAkcji, int liczbaDni) {
        this.spółki = spółki;
        this.obecnaTura = 0;
        this.liczbaDni = liczbaDni;
        this.cenyAkcji = cenyAkcji;
        this.obecnaKolejność = 0;
    }

    // Operacje

    public int liczbaDni() {
        return this.liczbaDni;
    }
    public int liczbaSpółek() {
        return this.spółki.size();
    }
    public int obecnaTura() {
        return this.obecnaTura;
    }

    // getter do spółki o odpowiednim indeksie
    public String dajSpółkę(int i) {
        try {
            return this.spółki.get(i);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Niepoprawny indeks spółki");
            e.printStackTrace();
            return "";
        }
    }

    // getter do ceny zadanej spółki
    public int cenaSpółki(String spółka) throws NiepoprawneZlecenie {
        Integer wynik = this.cenyAkcji.get(spółka);
        if (wynik == null)
            throw new NiepoprawneZlecenie();
        return wynik;
    }



    public int kolejnaTura() {
        this.obecnaKolejność = 0;
        return ++this.obecnaTura;
    }

    public int zwiększKolejność() {
        return this.obecnaKolejność++;
    }
    public void zaktualizujCenęAkcji(String spółka, int nowaCena) {
        this.cenyAkcji.put(spółka, nowaCena);
    }
}
