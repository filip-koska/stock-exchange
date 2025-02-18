package zlecenia;

import java.util.HashMap;

import inwestorzy.Inwestor;
import symulacja.DaneSymulacji;
import użytkowe.ParaKolejekZleceń;
import wyjątki.*;

public abstract class Zlecenie implements Comparable<Zlecenie> {

    // Dane
    protected Inwestor inwestor;
    protected String spółka;
    protected TypZlecenia typZlecenia;
    protected TerminZlecenia terminZlecenia;
    protected int liczbaAkcji;
    protected int limitCeny;
    protected int turaZłożenia;
    protected int kolejnośćZłożenia;
    protected int ostatniaTura;

    // Techniczne

    public Zlecenie(TypZlecenia typZlecenia, String spółka, int liczbaAkcji,
                    int limitCeny, int turaZłożenia, int kolejnośćZłożenia,
                    int ostatniaTura, Inwestor inwestor) {
        this.typZlecenia = typZlecenia;
        this.spółka = spółka;
        this.liczbaAkcji = liczbaAkcji;
        this.limitCeny = limitCeny;
        this.turaZłożenia = turaZłożenia;
        this.kolejnośćZłożenia = kolejnośćZłożenia;
        this.ostatniaTura = ostatniaTura;
        this.inwestor = inwestor;
    }

    // Operacje

    // porównujemy w pierwszej kolejności po limicie ceny, potem po czasie złożenia
    @Override
    public int compareTo(Zlecenie z)  throws RóżneTypy {
        // nie możemy porównywać zleceń różnych typów
        if (this.typZlecenia != z.typZlecenia)
            throw new RóżneTypy();
        if (this.limitCeny == z.limitCeny
                && this.turaZłożenia == z.turaZłożenia
                && this.kolejnośćZłożenia == z.kolejnośćZłożenia)
            return 0;
        if (this.limitCeny != z.limitCeny) {
            return switch (this.typZlecenia) {
                case KUPNO -> this.limitCeny > z.limitCeny ? 1 : -1;
                case SPRZEDAŻ -> this.limitCeny < z.limitCeny ? 1 : -1;
            };
        }
        if (this.turaZłożenia != z.turaZłożenia)
            return this.turaZłożenia < z.turaZłożenia ? 1 : -1;
        return this.kolejnośćZłożenia < z.kolejnośćZłożenia ? 1 : -1;
    }


    public int balansInwestora() {
        return this.inwestor.balans();
    }

    public int liczbaAkcjiInwestora() {
        return this.inwestor.liczbaAkcji(this.spółka);
    }

    public String spółka() {
        return this.spółka;
    }

    public TypZlecenia typZlecenia() {
        return this.typZlecenia;
    }

    public TerminZlecenia terminZlecenia() {
        return this.terminZlecenia;
    }

    public int turaZłożenia() {
        return this.turaZłożenia;
    }

    public int kolejnośćZłożenia() {
        return this.kolejnośćZłożenia;
    }
    public int ostatniaTura() {
        return this.ostatniaTura;
    }

    public int liczbaAkcji() {
        return this.liczbaAkcji;
    }

    public int limitCeny() {
        return this.limitCeny;
    }

    // sprawdzamy, czy transakcja z danymi parametrami jest możliwa do zrealizowania
    // przy obecnym stanie portfela inwestora
    public boolean czyMożliwe(int cena, int liczbaAkcji) {
        return switch (this.typZlecenia) {
            case KUPNO -> this.balansInwestora() >= cena * liczbaAkcji;
            case SPRZEDAŻ -> this.liczbaAkcjiInwestora() >= liczbaAkcji;
        };
    }

    // znajdujemy chronologicznie pierwsze z dwóch zleceń
    public static Zlecenie wcześniejsze(Zlecenie a, Zlecenie b) {
        if (a.turaZłożenia != b.turaZłożenia)
            return a.turaZłożenia < b.turaZłożenia ? a : b;
        return a.kolejnośćZłożenia < b.turaZłożenia ? a : b;
    }

    // sprawdzamy, czy limit zlecenia kupna jest >= limitowi zlecenia sprzedaży
    public boolean zgodne(Zlecenie z) {
        return switch (this.typZlecenia) {
            case KUPNO -> this.limitCeny >= z.limitCeny;
            case SPRZEDAŻ -> z.zgodne(this);
        };
    }

    // realizujemy częściowo zlecenie, aktualizując pozostałą przypisaną mu liczbę akcji
    public boolean zrealizujCzęściowo(int zmiana) {
        this.liczbaAkcji -= zmiana;
        return this.liczbaAkcji > 0;
    }

    public void zmieńBalansInwestora(int zmiana) {
        this.inwestor.zmieńBalans(zmiana);
    }

    public void zmieńLiczbęAkcjiInwestora(String spółka, int zmiana) {
        this.inwestor.zmieńAkcje(spółka, zmiana);
    }
}
