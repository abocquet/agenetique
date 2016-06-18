package eu.labrush;

public abstract class AbstractFellow implements Comparable<AbstractFellow> {

    private byte[] dna ;
    private int DNASIZE ;
    private int DNACARD ; //the number of symbols that can be used in the DNA from 0 to n - 1

    public AbstractFellow(int DNASIZE, int DNACARD){
        this.DNASIZE = DNASIZE ;
        this.DNACARD = DNACARD ;

        this.dna = new byte[DNASIZE] ;

        for(int i = 0 ; i < this.dna.length ; i++) this.dna[i] = (byte) ((int) (Math.random() * 10000) % DNACARD);

    }

    public AbstractFellow(byte[] dna, int DNACARD) {
        this.dna = dna;
        this.DNASIZE = dna.length;
        this.DNACARD = DNACARD;
    }

    /**
     * @return the fitness of the fellow ie its adaptation
     * higher is better
     */
    abstract public int getFitness();

    @Override
    public String toString() {
        return "Fellow{" +
                "fitness= " + this.getFitness() +
                '}';
    }

    public byte[] getDna() {
        return dna;
    }

    public void setDna(byte[] dna) {
        this.dna = dna;
    }

    @Override
    public int compareTo(AbstractFellow mate) {
        return this.getFitness() - mate.getFitness();
    }

    public int getDNASIZE() {
        return DNASIZE;
    }

    public int getDNACARD() {
        return DNACARD;
    }
}