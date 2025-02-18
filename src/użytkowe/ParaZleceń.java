package użytkowe;

import zlecenia.TypZlecenia;
import zlecenia.Zlecenie;

// Simple pair of orders
public class ParaZleceń {

    // buy order
    private Zlecenie kupno;
    // sell order
    private Zlecenie sprzedaż;

    // Technicalities
    
    public ParaZleceń(Zlecenie kupno, Zlecenie sprzedaż) {
        this.kupno = kupno;
        this.sprzedaż = sprzedaż;
    }

    // Operations

    public Zlecenie kupno() {
        return this.kupno;
    }

    public Zlecenie sprzedaż() {
        return this.sprzedaż;
    }

}
