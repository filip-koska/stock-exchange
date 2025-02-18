package wyjątki;

// Invalid Order Exception
public class NiepoprawneZlecenie extends RuntimeException {
    public NiepoprawneZlecenie() {
        super("Niepoprawne dane zlecenia");
    }
}
