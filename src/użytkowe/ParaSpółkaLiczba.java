package użytkowe;

// Simple <company, number of shares> pair structure
public class ParaSpółkaLiczba {
    // Data

    // company
    private String spółka;
    // number of shares
    private int liczbaAkcji;

    // Technicalities

    public ParaSpółkaLiczba(String spółka, int liczbaAkcji) {
        this.spółka = spółka;
        this.liczbaAkcji = liczbaAkcji;
    }

    // Operations

    public String spółka() {
        return this.spółka;
    }

    public int liczbaAkcji() {
        return this.liczbaAkcji;
    }
}
