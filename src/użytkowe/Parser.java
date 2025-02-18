package użytkowe;

import wyjątki.ZłyPlik;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Parser {

    // Techniczne

    public Parser() {}

    // Operacje

    // pierwsza linia to typy inwestorów
    public String[] przetwórzPierwsząLinię(String linia) {
        try {
            String[] wynik = linia.split(" ");
            for (String s : wynik) {
                // przyjmujemy tylko słowa jednoliterowe nad alfabetem {R, S}
                if (s.length() != 1 || !s.matches("[RS]"))
                    throw new ZłyPlik();
            }
            return wynik;

        } catch (ZłyPlik e) {
            e.printStackTrace();
            return new String[0];
        }
    }

    // druga linia to opis początkowych cen akcji
    public HashMap<String, Integer> przetwórzDrugąLinię(String linia) {
        try {
            String[] spółki = linia.split(" ");
            if (spółki.length == 0)
                throw new ZłyPlik();
            HashMap<String, Integer> wynik = new HashMap<>();
            for (String s : spółki) {
                String[] dane = s.split(":");
                if (dane.length != 2 || !this.nowaSpółka(dane[0], wynik))
                    throw new ZłyPlik();
                wynik.put(dane[0], Integer.parseInt(dane[1]));
            }
            return wynik;
        } catch (ZłyPlik | NumberFormatException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    // trzecia linia to opis początkowego stanu portfela inwestorów
    public ArrayList<ParaSpółkaLiczba> przetwórzTrzeciąLinię(String linia) {
        try {
            String[] dane = linia.split(" ");
            int balans = Integer.parseInt(dane[0]);
            ArrayList<ParaSpółkaLiczba> wynik = new ArrayList<>();
            for (int i = 1; i < dane.length; ++i) {
                String[] daneSpółki = dane[i].split(":");
                if (daneSpółki.length != 2 || !this.poprawnaSpółka(daneSpółki[0]))
                    throw new ZłyPlik();
                ParaSpółkaLiczba p = new ParaSpółkaLiczba(daneSpółki[0], Integer.parseInt(daneSpółki[1]));
                wynik.add(p);
            }
            // na końcu portfolio dodajemy informację o początkowym stanie kont inwestorów
            wynik.add(new ParaSpółkaLiczba("BALANS", balans));
            return wynik;
        } catch (ZłyPlik | NumberFormatException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private boolean nowaSpółka(String spółka, HashMap<String, Integer> mapa) {
        return this.poprawnaSpółka(spółka) && !mapa.containsKey(spółka);
    }

    // przyjmujemy tylko niepuste co najwyżej 5-literowe nazwy spółek nad alfabetem wielkich liter angielskich
    private boolean poprawnaSpółka(String spółka) {
        return !spółka.isEmpty() && spółka.length() < 6 && spółka.matches("[A-Z]+");
    }
}
