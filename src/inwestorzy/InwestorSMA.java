package inwestorzy;

import main.Losowanie;
import symulacja.DaneSymulacji;
import zlecenia.TerminZlecenia;
import zlecenia.TypZlecenia;
import zlecenia.Zlecenie;

import java.util.ArrayList;
import java.util.HashMap;

public class InwestorSMA extends Inwestor {

    // Dane
    private HashMap<String, ArrayList<Integer>> cenySMA5;
    private HashMap<String, ArrayList<Integer>> cenySMA10;
    private HashMap<String, Double> poprzednieSMA10;
    private HashMap<String, Double> poprzednieSMA5;
    private int ileTur;
    private int indeksSMA10;
    private int indeksSMA5;

    // Techniczne
    public InwestorSMA(int id, int balans, HashMap<String, Integer> liczbaAkcji, DaneSymulacji daneSymulacji) {
        super(id, balans, liczbaAkcji, daneSymulacji);
        this.cenySMA5 = new HashMap<>();
        this.cenySMA10 = new HashMap<>();
        this.poprzednieSMA10 = new HashMap<>();
        this.poprzednieSMA5 = new HashMap<>();
        for (int i = 0; i < this.daneSymulacji.liczbaSpółek(); ++i) {
            String spółka = this.daneSymulacji.dajSpółkę(i);
            this.cenySMA10.put(spółka, new ArrayList<>());
            this.cenySMA5.put(spółka, new ArrayList<>());
            this.poprzednieSMA5.put(spółka, 0.0);
            this.poprzednieSMA10.put(spółka, 0.0);
            for (int j = 0; j < 5; ++j) {
                this.cenySMA10.get(spółka).add(0);
                this.cenySMA5.get(spółka).add(0);
            }
            for (int j = 0; j < 5; ++j)
                this.cenySMA10.get(spółka).add(0);
        }
        this.ileTur = this.indeksSMA5 = this.indeksSMA10 = 0;
    }

    // Operacje
    private double obliczSMA5(String spółka) {
        ArrayList<Integer> ceny = this.cenySMA5.get(spółka);
        double suma = 0;
        for (int i : ceny)
            suma += i;
        return suma / 5.0;
    }

    private double obliczSMA10(String spółka) {
        ArrayList<Integer> ceny = this.cenySMA10.get(spółka);
        double suma = 0;
        for (int i : ceny)
            suma += i;
        return suma / 10.0;
    }

    // na koniec tury inwestor SMA aktualizuje swoje wskaźniki
    // (to tak samo, jakby odpytał się symulacji o cenę ostatniej transakcji na początku następnej tury)
    @Override
    public void koniecTury() {
        ++this.ileTur;
        for (int i = 0; i < this.daneSymulacji.liczbaSpółek(); ++i) {
            String spółka = this.daneSymulacji.dajSpółkę(i);
            int cenaSpółki = this.daneSymulacji.cenaSpółki(spółka);
            // dopiero od szóstej tury jesteśmy w stanie porównać poprzednie SMA5
            if (this.ileTur >= 6)
                this.poprzednieSMA5.put(spółka, this.obliczSMA5(spółka));
            this.cenySMA5.get(spółka).set(this.indeksSMA5, cenaSpółki);
            // dopiero od jedenastej tury jesteśmy w stanie porównać poprzednie SMA10
            if (this.ileTur >= 11)
                this.poprzednieSMA10.put(spółka, this.obliczSMA10(spółka));
            this.cenySMA10.get(spółka).set(this.indeksSMA10, cenaSpółki);
            this.indeksSMA5 = (this.indeksSMA5 + 1) % 5;
            this.indeksSMA10 = (this.indeksSMA10 + 1) % 10;
        }
    }

    // metoda obliczająca czy doszło do przecięcia SMA10 przez SMA5, a jeśli tak, to z której strony
    private Przecięcie przecięcie(String spółka) {
        Przecięcie wynik = null;
        // przecięcie z dołu
        if (this.poprzednieSMA5.get(spółka) < this.poprzednieSMA10.get(spółka)
                && this.obliczSMA5(spółka) > this.obliczSMA10(spółka))
            return Przecięcie.DÓŁ;
        // przecięcie z góry
        if (this.poprzednieSMA5.get(spółka) > this.poprzednieSMA10.get(spółka)
                && this.obliczSMA5(spółka) < this.obliczSMA10(spółka))
            return Przecięcie.GÓRA;
        // wpp brak przecięcia
        return Przecięcie.BRAK;
    }

    // znajduje spółkę, w którą będzie chciał zainwestować inwestor SMA
    // (przyjmujemy, że inwestuje w pierwszą spółkę z niepustym sygnałem wg arbitralnej kolejności)
    private String znajdźSpółkę() {
        for (String spółka : this.cenySMA10.keySet()) {
            Przecięcie przecięcie = this.przecięcie(spółka);
            switch (przecięcie) {
                case DÓŁ:
                    return spółka + ":" + "KUPNO";
                case GÓRA:
                    return spółka + ":" + "SPRZEDAŻ";
                default:
                    break;
            }
        }
        return "BRAK:BRAK";
    }

    @Override
    public Zlecenie złóżZlecenie() {
        // jeżeli jesteśmy co najwyżej w 10. turze, to nie mamy jak porównać dwóch poprzednich SMA10
        if (this.ileTur < 11)
            return null;

        String[] sygnał = this.znajdźSpółkę().split(":");
        String spółka = sygnał[0];
        // jeżeli brak sygnału, nie inwestujemy
        if (spółka.equals("BRAK"))
            return null;

        // składamy zlecenie dot. pierwszej znalezionej spółki
        TypZlecenia typZlecenia = TypZlecenia.valueOf(sygnał[1]);
        TerminZlecenia terminZlecenia = this.losujTermin();
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
}
