package main;

import java.util.Random;
import wyjątki.NiepoprawneLosowanie;

// Simple Singleton wrapper for a random number generator
public final class Losowanie {
    private static final Random r = new Random();

    private Losowanie() {}

    public static int losuj(int dolna, int górna) throws NiepoprawneLosowanie {
        if (dolna > górna)
            throw new NiepoprawneLosowanie();
        return r.nextInt(górna - dolna + 1) + dolna;
    }
}
