package zlecenia;

import java.util.HashMap;

import inwestorzy.Inwestor;
import symulacja.DaneSymulacji;
import użytkowe.ParaKolejekZleceń;
import wyjątki.*;

// Stock order class
public abstract class Zlecenie implements Comparable<Zlecenie> {

    // Data

    // investor that issued the order
    protected Inwestor inwestor;
    // company whose shares the order concerns
    protected String spółka;
    // transaction type
    protected TypZlecenia typZlecenia;
    // expiration type
    protected TerminZlecenia terminZlecenia;
    // number of shares
    protected int liczbaAkcji;
    // price limit (upper for buy, lower for sell)
    protected int limitCeny;
    // issue session
    protected int turaZłożenia;
    // issue turn
    protected int kolejnośćZłożenia;
    // last session when the order can be completed
    protected int ostatniaTura;

    // Technicalities

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

    // Operations

    // When matching, prioritise price limits, then issue time
    @Override
    public int compareTo(Zlecenie z)  throws RóżneTypy {
        // Comparison of different transaction types is illegal
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

    // Check if specified transaction is possible given the investor's current balance
    public boolean czyMożliwe(int cena, int liczbaAkcji) {
        return switch (this.typZlecenia) {
            case KUPNO -> this.balansInwestora() >= cena * liczbaAkcji;
            case SPRZEDAŻ -> this.liczbaAkcjiInwestora() >= liczbaAkcji;
        };
    }

    // Find the earlier of two orders
    public static Zlecenie wcześniejsze(Zlecenie a, Zlecenie b) {
        if (a.turaZłożenia != b.turaZłożenia)
            return a.turaZłożenia < b.turaZłożenia ? a : b;
        return a.kolejnośćZłożenia < b.turaZłożenia ? a : b;
    }
    
    // Check if the price limits of two orders are compatible
    // (buy price limit >= sell price limit)
    public boolean zgodne(Zlecenie z) {
        return switch (this.typZlecenia) {
            case KUPNO -> this.limitCeny >= z.limitCeny;
            case SPRZEDAŻ -> z.zgodne(this);
        };
    }
    
    // Partially complete the order and update the remaining associated number of shares
    public boolean zrealizujCzęściowo(int zmiana) {
        this.liczbaAkcji -= zmiana;
        return this.liczbaAkcji > 0;
    }

    // Update investor's account balance
    public void zmieńBalansInwestora(int zmiana) {
        this.inwestor.zmieńBalans(zmiana);
    }

    // Update investor's number of given company's shares
    public void zmieńLiczbęAkcjiInwestora(String spółka, int zmiana) {
        this.inwestor.zmieńAkcje(spółka, zmiana);
    }
}
