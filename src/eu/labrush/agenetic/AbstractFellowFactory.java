package eu.labrush.agenetic;

public abstract class AbstractFellowFactory {

    private int DNASize = 10 ;
    private int DNACard = 2 ;

    public AbstractFellowFactory() {}

    public AbstractFellowFactory(int DNACard, int DNASize) {
        this.setDNACard(DNACard);
        this.setDNASize(DNASize);
    }

    public abstract AbstractFellow newInstance();
    public abstract AbstractFellow newInstance(int[] dna);

    public int getDNASize() {
        return DNASize;
    }

    public void setDNASize(int DNASize) {
        this.DNASize = DNASize;
    }

    public int getDNACard() {
        return DNACard;
    }

    public void setDNACard(int DNACard) {
        this.DNACard = DNACard;
    }
}
