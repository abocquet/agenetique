package eu.labrush.agenetic;

public abstract class AbstractFellowFactory {

    private int DNASize = 10 ;
    private int DNACard = 2 ;

    protected AbstractFellowFactory() {}

    protected AbstractFellowFactory(int DNACard, int DNASize) {
        this.setDNACard(DNACard);
        this.setDNASize(DNASize);
    }

    public abstract AbstractFellow newInstance();
    public abstract AbstractFellow newInstance(int[] dna);

    public int getDNASize() {
        return DNASize;
    }

    protected void setDNASize(int DNASize) {
        this.DNASize = DNASize;
    }

    public int getDNACard() {
        return DNACard;
    }

    protected void setDNACard(int DNACard) {
        this.DNACard = DNACard;
    }
}
