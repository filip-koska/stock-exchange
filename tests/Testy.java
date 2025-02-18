import main.Losowanie;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import symulacja.DaneSymulacji;
import symulacja.Symulacja;
import wyjątki.*;
import zlecenia.TypZlecenia;
import zlecenia.Zlecenie;
import zlecenia.ZlecenieCzasowe;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;



public class Testy {

    int sumaPieniędzy;
    String liczbaTur;
    HashMap<String, Integer> akcje;

    @BeforeEach
    void setUp() {
        this.sumaPieniędzy = 6000000;
        this.liczbaTur = "10000";
        this.akcje = new HashMap<>();
        this.akcje.put("APL", 150);
        this.akcje.put("MSFT", 240);
        this.akcje.put("GOOGL", 210);
    }

    @AfterEach
    void tearDown() {
        this.akcje = new HashMap<>();
    }

    @Test
    void testSamiLosowi() {
        Symulacja sym = new Symulacja("tests/input_losowi.txt", this.liczbaTur);
        sym.symuluj();
        assertEquals(this.sumaPieniędzy, sym.sumaPieniędzy(), "Niepoprawna suma pieniędzy");
        for (String s : this.akcje.keySet())
            assertEquals(this.akcje.get(s), sym.sumaAkcjiSpółki(s), "Niepoprawna suma akcji spółki " + s);
    }

    @Test
    void testSamiSMA() {
        Symulacja sym = new Symulacja("tests/input_SMA.txt", this.liczbaTur);
        sym.symuluj();
        assertEquals(this.sumaPieniędzy, sym.sumaPieniędzy(), "Niepoprawna suma pieniędzy");
        for (String s : this.akcje.keySet())
            assertEquals(this.akcje.get(s), sym.sumaAkcjiSpółki(s), "Niepoprawna suma akcji spółki " + s);
    }

    @Test
    void testPółNaPół() {
        Symulacja sym = new Symulacja("tests/input_pół_na_pół.txt", this.liczbaTur);
        sym.symuluj();
        for (String s : this.akcje.keySet())
            assertEquals(this.akcje.get(s), sym.sumaAkcjiSpółki(s), "Niepoprawna suma akcji spółki " + s);
    }

    @Test
    void testRóżneTypy() {
        assertThrows(RóżneTypy.class, () -> {new ZlecenieCzasowe(TypZlecenia.KUPNO, "APL",
                0, 0, 0, 0, 0, null).compareTo(new
                ZlecenieCzasowe(TypZlecenia.SPRZEDAŻ, "MSFT", 0, 0, 0,
                0, 0, null));});
    }

    @Test
    void testNiepoprawneZlecenie() {
        DaneSymulacji testDane = new DaneSymulacji(null, new HashMap<>(), 0);
        assertThrows(NiepoprawneZlecenie.class, () -> {testDane.cenaSpółki("APL");});
    }

    @Test
    void testNiepoprawneLosowanie() {
        assertThrows(NiepoprawneLosowanie.class, () -> {Losowanie.losuj(2, 1);});
    }

}
