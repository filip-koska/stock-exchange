package wyjątki;

public class ZłyPlik extends Exception {
    public ZłyPlik() {
        super("Niepoprawna struktura pliku wejściowego");
    }
}
