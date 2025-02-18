package wyjątki;

// Bad File Exception
public class ZłyPlik extends Exception {
    public ZłyPlik() {
        super("Niepoprawna struktura pliku wejściowego");
    }
}
