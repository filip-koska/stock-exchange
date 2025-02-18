package symulacja;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import inwestorzy.Inwestor;
import inwestorzy.InwestorLosowy;
import inwestorzy.InwestorSMA;
import użytkowe.*;

import wyjątki.*;
import zlecenia.TerminZlecenia;
import zlecenia.TypZlecenia;
import zlecenia.Zlecenie;

public class Symulacja {

    // Dane
    private int liczbaTur;
    private ArrayList<String> spółki;
    private ArrayList<Inwestor> inwestorzy;
    private HashMap<String, StrukturaKolejekZleceń> arkusze;
    private static final int OCZEKIWANA_LICZBA_LINII = 3;
    public static final int LICZBA_TYPÓW_ZLECEŃ = 2;
    public static final int LICZBA_TERMINÓW_ZLECEŃ = 3;
    private DaneSymulacji daneSymulacji;

    // Techniczne

    public Symulacja(String ścieżka, String liczbaTur) {
        try {
            int ileDni = Integer.parseInt(liczbaTur);
            this.liczbaTur = ileDni;
            File plik = new File(ścieżka);
            Scanner czytnik = new Scanner(plik);
            Parser parser = new Parser();
            this.inwestorzy = new ArrayList<>();
            this.spółki = new ArrayList<>();
            this.arkusze = new HashMap<>();
            String[] typyInwestorów = null;
            HashMap<String, Integer> początkoweCeny = null;
            ArrayList<ParaSpółkaLiczba> początkowePortfolio = null;
            int liczbaLinii = 0;
            while (czytnik.hasNextLine()) {
                String dane = czytnik.nextLine();
                if (dane.charAt(0) == '#')
                    continue;
                switch (liczbaLinii) {
                    case 0:
                        typyInwestorów = parser.przetwórzPierwsząLinię(dane);
                        break;
                    case 1:
                        początkoweCeny = parser.przetwórzDrugąLinię(dane);
                        break;
                    case 2:
                        początkowePortfolio = parser.przetwórzTrzeciąLinię(dane);
                        break;
                    default:
                        break;
                }
                ++liczbaLinii;
            }
            if (liczbaLinii != OCZEKIWANA_LICZBA_LINII
                    || początkoweCeny.keySet().size() != początkowePortfolio.size() - 1)
                throw new ZłyPlik();
            this.spółki.addAll(początkoweCeny.keySet());
            int początkowyBalans = początkowePortfolio.getLast().liczbaAkcji();
            początkowePortfolio.removeLast();
            HashMap<String, Integer> portfolio = new HashMap<>();
            for (ParaSpółkaLiczba p : początkowePortfolio) {
                if (portfolio.containsKey(p.spółka()))
                    throw new ZłyPlik();
                portfolio.put(p.spółka(), p.liczbaAkcji());
            }

            this.daneSymulacji = new DaneSymulacji(this.spółki, początkoweCeny, ileDni);
            for (int i = 0; i < typyInwestorów.length; ++i) {
                switch (typyInwestorów[i].charAt(0)) {
                    case 'R':
                        this.inwestorzy.add(new InwestorLosowy(i, początkowyBalans, portfolio, this.daneSymulacji));
                        break;
                    case 'S':
                        this.inwestorzy.add(new InwestorSMA(i, początkowyBalans, portfolio, this.daneSymulacji));
                }
            }
            for (String s : this.spółki)
                this.arkusze.put(s, new ParaKolejekZleceń());
            czytnik.close();

        } catch (NumberFormatException | ZłyPlik | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Operacje

    private void dodajZlecenieKupna(Zlecenie z) {
        if (z.balansInwestora() >= z.limitCeny() * z.liczbaAkcji())
            this.arkusze.get(z.spółka()).wstaw(z);
    }

    private void dodajZlecenieSprzedaży(Zlecenie z) {
        if (z.liczbaAkcjiInwestora() >= z.liczbaAkcji())
            this.arkusze.get(z.spółka()).wstaw(z);
    }

    public void dodajZlecenie(Zlecenie z) {
        if (z.typZlecenia() == TypZlecenia.KUPNO)
            this.dodajZlecenieKupna(z);
        else
            this.dodajZlecenieSprzedaży(z);
    }


    // Obsługa transakcji wynikającej ze zgodności dwóch zleceń
    private void transakcja(Zlecenie kupno, Zlecenie sprzedaż, int cenaAkcji, int liczbaAkcji) {
        kupno.zmieńBalansInwestora(-cenaAkcji * liczbaAkcji);
        kupno.zmieńLiczbęAkcjiInwestora(kupno.spółka(), liczbaAkcji);
        sprzedaż.zmieńBalansInwestora(cenaAkcji * liczbaAkcji);
        sprzedaż.zmieńLiczbęAkcjiInwestora(sprzedaż.spółka(), -liczbaAkcji);
        this.daneSymulacji.zaktualizujCenęAkcji(sprzedaż.spółka(), cenaAkcji);
    }

    // obsługuje parę zleceń najwyżej postawionych w arkuszu zleceń danej spółki
    private void obsłużParęZleceń(ParaZleceń p, ArrayList<Zlecenie> niezrealizowane) {
        Zlecenie wcześniejsze = Zlecenie.wcześniejsze(p.kupno(), p.sprzedaż());
        Zlecenie późniejsze = wcześniejsze == p.kupno() ? p.sprzedaż() : p.kupno();

        // ustalamy cenę akcji jako limit ceny wcześniej postawionego zlecenia
        int cenaAkcji = wcześniejsze.limitCeny();

        // jeżeli zlecenia nie są zgodne cenowo, to przyjmujemy, że usuwamy wcześniejsze
        if (!wcześniejsze.zgodne(późniejsze)) {
            niezrealizowane.add(wcześniejsze);
            this.dodajZlecenie(późniejsze);
            return;
        }

        // nie dopuszczamy częściowej realizacji obu zleceń
        int liczbaAkcji = Math.min(wcześniejsze.liczbaAkcji(), późniejsze.liczbaAkcji());

        // sprawdzamy, czy obaj inwestorzy mają niezbędne do wykonania transakcji środki (fundusze/akcje)
        if (!wcześniejsze.czyMożliwe(cenaAkcji, liczbaAkcji)) {
            this.dodajZlecenie(późniejsze);
            return;
        }
        if (!późniejsze.czyMożliwe(cenaAkcji, liczbaAkcji)) {
            this.dodajZlecenie(wcześniejsze);
            return;
        }

        // dochodzi do transakcji
        this.transakcja(p.kupno(), p.sprzedaż(), cenaAkcji, liczbaAkcji);

        // przywracamy częściowo zrealizowane zlecenie do arkusza zleceń spółki
        boolean czyPrzywrócić = wcześniejsze.zrealizujCzęściowo(liczbaAkcji);
        if (czyPrzywrócić)
            this.dodajZlecenie(wcześniejsze);
        czyPrzywrócić = późniejsze.zrealizujCzęściowo(liczbaAkcji);
        if (czyPrzywrócić)
            this.dodajZlecenie(późniejsze);
    }

    // obsługuje arkusz zleceń danej spółki w obecnej turze
    private void obsłużZleceniaSpółki(String spółka) {
        StrukturaKolejekZleceń zleceniaSpółki = this.arkusze.get(spółka);
        ArrayList<Zlecenie> niezrealizowane = new ArrayList<>();

        // dopóki dalej istnieją nierozpatrzone zlecenia kupna i sprzedaży
        while (!zleceniaSpółki.sprzedażPusta() && !zleceniaSpółki.kupnoPusta()) {
            ParaZleceń p = zleceniaSpółki.zdejmijParę();
            this.obsłużParęZleceń(p, niezrealizowane);
        }

        // dalej nie da się zrealizować żadnego zlecenia; wrzucamy wszystko z powrotem na kolejkę
        zleceniaSpółki.przerzućWszystkie(niezrealizowane);
        for (Zlecenie z : niezrealizowane)
            if (z.ostatniaTura() > this.daneSymulacji.obecnaTura() && z.terminZlecenia() != TerminZlecenia.BT)
                this.dodajZlecenie(z);
    }

    // powiadamia inwestorów o zakończeniu tury
    private void zakończTurę() {
        for (Inwestor i : this.inwestorzy)
            i.koniecTury();
        this.daneSymulacji.kolejnaTura();
    }

    public void obsłużTurę() {
        // odpytujemy inwestorów o decyzje inwestycyjne
        for (Inwestor i : this.inwestorzy) {
            Zlecenie z = i.złóżZlecenie();
            if (z != null)
                this.dodajZlecenie(z);
        }

        // obsługujemy arkusz zleceń
        for (String spółka : this.arkusze.keySet())
            this.obsłużZleceniaSpółki(spółka);
        this.zakończTurę();
    }

    // główna metoda klasy Symulacja
    public void symuluj() {
        for (int i = 0; i < this.liczbaTur; ++i)
            this.obsłużTurę();
        for (Inwestor i : this.inwestorzy) {
            System.out.println(i);
        }
    }

    // oblicza sumę pieniędzy w obiegu, przydatna do testów
    public int sumaPieniędzy() {
        int suma = 0;
        for (Inwestor i : this.inwestorzy)
            suma += i.balans();
        return suma;
    }

    // oblicza dla spółki liczbę jej akcji w obiegu, przydatna do testów
    public int sumaAkcjiSpółki(String spółka) {
        int suma = 0;
        for (Inwestor i : this.inwestorzy)
            suma += i.liczbaAkcji(spółka);
        return suma;
    }
}