package wyjątki;

public class NiepoprawnaTransakcja extends RuntimeException {
    public NiepoprawnaTransakcja() {
        super("Niepoprawna transakcja - nielegalny stan inwestora");
    }
}
