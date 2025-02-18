package użytkowe;

import zlecenia.Zlecenie;

import java.util.ArrayList;

public interface StrukturaKolejekZleceń {
    void wstaw(Zlecenie z);
    ParaZleceń zdejmijParę();
    boolean kupnoPusta();
    boolean sprzedażPusta();
    void przerzućWszystkie(ArrayList<Zlecenie> niezrealizowane);
}
