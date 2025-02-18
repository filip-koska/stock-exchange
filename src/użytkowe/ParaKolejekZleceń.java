package użytkowe;

import java.util.ArrayList;
import java.util.PriorityQueue;

import symulacja.Symulacja;
import zlecenia.TypZlecenia;
import zlecenia.Zlecenie;

public class ParaKolejekZleceń implements StrukturaKolejekZleceń {
    // konwencja: trzymamy tablicę dwuelementową kolejek priorytetowych
    // pierwsza odpowiada za zlecenia kupna, druga za zlecenia sprzedaży

    // Dane
    private final ArrayList<PriorityQueue<Zlecenie>> zlecenia;

    // Techniczne

    public ParaKolejekZleceń() {
        this.zlecenia = new ArrayList<>();
        for (int i = 0; i < Symulacja.LICZBA_TYPÓW_ZLECEŃ; ++i)
            this.zlecenia.add(new PriorityQueue<>());
    }

    // Operacje

    // wstaw zlecenie do odpowiadającej mu kolejki
    public void wstaw(Zlecenie z) {
        this.zlecenia.get(z.typZlecenia().ordinal()).add(z);
    }

    // spróbuj obsłużyć parę zleceń z wierzchu pary kolejek
    public ParaZleceń zdejmijParę() {
        return new ParaZleceń(this.zlecenia.get(0).poll(), this.zlecenia.get(1).poll());
    }

    public boolean kupnoPusta() {
        return this.zlecenia.get(TypZlecenia.KUPNO.ordinal()).isEmpty();
    }

    public boolean sprzedażPusta() {
        return this.zlecenia.get(TypZlecenia.SPRZEDAŻ.ordinal()).isEmpty();
    }

    // przerzuca wszystkie elementy pary kolejek na stos zleceń niezrealizowanych w całości
    public void przerzućWszystkie(ArrayList<Zlecenie> niezrealizowane) {
        for (int i = 0; i < 2; ++i)
            while (!this.zlecenia.get(i).isEmpty())
                niezrealizowane.add(this.zlecenia.get(i).poll());
    }
}
