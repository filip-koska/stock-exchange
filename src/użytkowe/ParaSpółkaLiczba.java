package użytkowe;

public class ParaSpółkaLiczba {
    private String spółka;
    private int liczbaAkcji;

    public ParaSpółkaLiczba(String spółka, int liczbaAkcji) {
        this.spółka = spółka;
        this.liczbaAkcji = liczbaAkcji;
    }

    public String spółka() {
        return this.spółka;
    }

    public int liczbaAkcji() {
        return this.liczbaAkcji;
    }
}
