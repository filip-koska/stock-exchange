package wyjątki;

public class NiepoprawneLosowanie extends RuntimeException {
    public NiepoprawneLosowanie() {
        super("Niepoprawne granice losowania");
    }
}
