package wyjątki;

public class RóżneTypy extends RuntimeException {
    public RóżneTypy() {
        super("Próba porównania zleceń różnych typów");
    }
}
