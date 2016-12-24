package eu.labrush.agenetic;

public abstract class AbstractFellow implements Comparable<AbstractFellow> {

    private int[] dna ;

    protected long fitness = Long.MIN_VALUE;
    private boolean DNAEdited = false ;

    /**
        If you want to adapt DNASIZE and DNACARD, change the static class
        value inside the Nature class before creating any new Fellow
     */
    private int DNASIZE = -1;
    private int DNACARD = 2; //the number of symbols that can be used in the DNA from 0 to n - 1

    protected AbstractFellow(int DNASIZE, int DNACARD){
        this.DNACARD = DNACARD ;
        this.DNASIZE = DNASIZE ;

        this.dna = new int[DNASIZE];
        for(int i = 0 ; i < DNASIZE ; i++) this.dna[i] = ((int) (Math.random() * 10000) % DNACARD);
    }

    protected AbstractFellow(int[] dna, int DNACARD) {
        this.dna = dna ;
        this.DNASIZE = dna.length ;
        this.DNACARD = DNACARD ;
    }

    /**
     * @return the fitness of the fellow ie its adaptation
     * higher is better
     */
    public final long getFitness(){
        if(this.fitness == Long.MIN_VALUE || DNAEdited){
            this.fitness = this.calcFitness();
        }

        return this.fitness ;
    }

    protected abstract long calcFitness();

    @Override
    public String toString() {
        return "Fellow{" +
                "fitness= " + this.getFitness() +
                '}';
    }


    public int getDNA(int index){
        return this.dna[index] ;
    }

    public void setDNA(int index, int value){
        this.dna[index] = value ;
        this.DNAEdited = true ;
    }

    protected void setDna(int[] dna) {
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
        return Long.compare(this.getFitness(), mate.getFitness());
    }

    public int getDNASIZE() { return DNASIZE; }
    protected int getDNACARD() {
        return DNACARD;
    }


    public int[] cloneDNA(){ return  this.dna.clone(); }

}