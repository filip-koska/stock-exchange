package wyjątki;

// Invalid Random Number Exception
public class NiepoprawneLosowanie extends RuntimeException {
    public NiepoprawneLosowanie() {
        super("Niepoprawne granice losowania");
    }
}
