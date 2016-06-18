package eu.labrush.Polynome;

public class Fellow implements Comparable<Fellow> {

    private boolean[] dna ;
    private int DNASIZE ;

    public Fellow(int DNASIZE){
        this.DNASIZE = DNASIZE ;
        this.dna = new boolean[DNASIZE] ;

        for(int i = 0 ; i < this.dna.length ; i++){
            this.dna[i] = ((int)(Math.random() * 10000) % 2) == 1;
        }
    }

    public Fellow() {
        this(8);
    }

    public Fellow(boolean[] dna){
        this.dna = dna ;
        this.DNASIZE = dna.length ;
    }

    /**
     * On convertit simplement l'ADN binaire en entier
     */
    public int getFitness() {
        int x = 0 ;
        int tmp = 1 ;

        for(int i = 0 ; i < this.dna.length ; i++){
            if(this.dna[i]) {
                x += tmp ;
            }
            tmp *= 2 ;
        }

        return x*(256 - x) ;

    }

    @Override
    public String toString() {
        return "Travel{" +
                //"dna=" + Arrays.toString(dna).replace("true", "1").replace("false", "0") +
                ", fitness= " + this.getFitness() +
                '}';
    }

    public boolean[] getDna() {
        return dna;
    }

    public void setDna(boolean[] dna) {
        this.dna = dna;
    }

    @Override
    public int compareTo(Fellow mate) {
        return this.getFitness() - mate.getFitness();
    }

    public int getDNASIZE() {
        return DNASIZE;
    }
}
