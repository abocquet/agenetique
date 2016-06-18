package eu.labrush;

public abstract class AbstractFellow implements Comparable<AbstractFellow> {

    protected byte[] dna ;
    static protected int DNASIZE = 10 ;
    static protected int DNACARD = 2; //the number of symbols that can be used in the DNA from 0 to n - 1

    public AbstractFellow(){
        for(int i = 0 ; i < this.dna.length ; i++) this.dna[i] = (byte) ((int) (Math.random() * 10000) % DNACARD);
    }

    public AbstractFellow(byte[] dna) {
        if(dna.length != this.DNASIZE){
            try {
                throw new Exception("DNA size does not match");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        this.dna = dna;
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