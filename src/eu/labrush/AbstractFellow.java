package eu.labrush;

public abstract class AbstractFellow implements Comparable<AbstractFellow> {

    private int[] dna ;

    /**
        If you want to adapt DNASIZE and DNACARD, change the static class
        value inside the Nature class before creating any new Fellow
     */
    static private int DNASIZE = 10 ;
    static private int DNACARD = 2; //the number of symbols that can be used in the DNA from 0 to n - 1

    static private boolean hasBeenInstanciated = false ;

    public AbstractFellow(){
        this.dna = new int[DNASIZE];
        for(int i = 0 ; i < DNASIZE ; i++) this.dna[i] = ((int) (Math.random() * 10000) % DNACARD);
        hasBeenInstanciated = true ;
    }

    public AbstractFellow(int[] dna) {
        this.setDna(dna);
        hasBeenInstanciated = true ;
    }

    /**
     * @return the fitness of the fellow ie its adaptation
     * higher is better
     *
     * Note: it must return a positive value, else it mess up with
     * the biased wheel
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

    public static int getDNASIZE() {
        return DNASIZE;
    }

    public static int getDNACARD() {
        return DNACARD;
    }

    public static void setDNASIZE(int DNASIZE) throws Exception {
        if(hasBeenInstanciated) throw new Exception("Cannot change DNA size after you have instanciated a Fellow");
        AbstractFellow.DNASIZE = DNASIZE;
    }

    public static void setDNACARD(int DNACARD) throws Exception {
        if(hasBeenInstanciated) throw new Exception("Cannot change DNA card after you have instanciated a Fellow");
        AbstractFellow.DNACARD = DNACARD;
    }

}