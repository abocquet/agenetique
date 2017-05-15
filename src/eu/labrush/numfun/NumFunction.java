package eu.labrush.numfun;

public abstract class NumFunction implements NumFunInterface {

    public Object getArguments(int[] dna) {
        int DNASIZE = dna.length ;

        int x = 0 ;
        int tmp = 1 ;
        for(int i = 0 ; i < DNASIZE ; i++){
            if(dna[i] == 1) {
                x += tmp ;
            }
            tmp *= 2 ;
        }

        return x;
    }

    public int getY(int[] dna) {
        int x = (int) getArguments(dna);
        return f(x);
    }

    public abstract int f(int x);
    public abstract int getDNASIZE();
}
