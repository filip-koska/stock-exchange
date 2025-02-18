package wyjątki;

// Invalid Transaction Exception
public class NiepoprawnaTransakcja extends RuntimeException {
    public NiepoprawnaTransakcja() {
        super("Niepoprawna transakcja - nielegalny stan inwestora");
    }
}
