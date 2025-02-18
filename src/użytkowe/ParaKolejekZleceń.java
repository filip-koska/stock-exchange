package użytkowe;

import java.util.ArrayList;
import java.util.PriorityQueue;

import symulacja.Symulacja;
import zlecenia.TypZlecenia;
import zlecenia.Zlecenie;

// Pair of order queues
public class ParaKolejekZleceń implements StrukturaKolejekZleceń {

    // Data

    // array of size 2 of order queues
    private final ArrayList<PriorityQueue<Zlecenie>> zlecenia;

    // Technicalities

    public ParaKolejekZleceń() {
        this.zlecenia = new ArrayList<>();
        for (int i = 0; i < Symulacja.LICZBA_TYPÓW_ZLECEŃ; ++i)
            this.zlecenia.add(new PriorityQueue<>());
    }

    // Operations

    // Enqueue order
    public void wstaw(Zlecenie z) {
        this.zlecenia.get(z.typZlecenia().ordinal()).add(z);
    }
    
    // Try to match a pair of orders from the top of the order queues
    public ParaZleceń zdejmijParę() {
        return new ParaZleceń(this.zlecenia.get(0).poll(), this.zlecenia.get(1).poll());
    }

    // isEmpty for buy orders queue
    public boolean kupnoPusta() {
        return this.zlecenia.get(TypZlecenia.KUPNO.ordinal()).isEmpty();
    }

    // isEmpty for sell orders queue
    public boolean sprzedażPusta() {
        return this.zlecenia.get(TypZlecenia.SPRZEDAŻ.ordinal()).isEmpty();
    }

    // Moves all uncompleted orders to the uncompleted orders stack
    public void przerzućWszystkie(ArrayList<Zlecenie> niezrealizowane) {
        for (int i = 0; i < 2; ++i)
            while (!this.zlecenia.get(i).isEmpty())
                niezrealizowane.add(this.zlecenia.get(i).poll());
    }
}
