package wyjątki;

public class NiepoprawneZlecenie extends RuntimeException {
    public NiepoprawneZlecenie() {
        super("Niepoprawne dane zlecenia");
    }
}
