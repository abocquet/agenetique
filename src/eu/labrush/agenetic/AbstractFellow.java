package eu.labrush.agenetic;

public abstract class AbstractFellow implements Comparable<AbstractFellow> {

    private int[] dna ;

    /**
        If you want to adapt DNASIZE and DNACARD, change the static class
        value inside the Nature class before creating any new Fellow
     */
    private int DNASIZE = 10 ;
    private int DNACARD = 2; //the number of symbols that can be used in the DNA from 0 to n - 1

    public AbstractFellow(int DNASIZE, int DNACARD){
        this.DNACARD = DNACARD ;
        this.DNASIZE = DNASIZE ;

        this.dna = new int[DNASIZE];
        for(int i = 0 ; i < DNASIZE ; i++) this.dna[i] = ((int) (Math.random() * 10000) % DNACARD);
    }

    public AbstractFellow(int[] dna, int DNACARD) {
        this.dna = dna ;
        this.DNASIZE = dna.length ;
        this.DNACARD = DNACARD ;
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

    public int[] getDna() {
        return dna;
    }

    public void setDna(int[] dna) {
        if(dna.length != DNASIZE){
            try {
                throw new Exception("DNA size does not match");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

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