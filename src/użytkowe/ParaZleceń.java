package użytkowe;

import zlecenia.TypZlecenia;
import zlecenia.Zlecenie;

public class ParaZleceń {
    private Zlecenie kupno;
    private Zlecenie sprzedaż;


    public ParaZleceń(Zlecenie kupno, Zlecenie sprzedaż) {
        this.kupno = kupno;
        this.sprzedaż = sprzedaż;
    }

    public Zlecenie kupno() {
        return this.kupno;
    }

    public Zlecenie sprzedaż() {
        return this.sprzedaż;
    }

}
