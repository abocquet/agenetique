package eu.labrush.numfun;

import eu.labrush.agenetic.AbstractFellow;

public class Fellow extends AbstractFellow {

    static private NumFunInterface fun ;

    protected Fellow(int DNASIZE, int DNACARD) {
        super(DNASIZE, DNACARD);
    }

    protected Fellow(int[] dna, int DNACARD){
        super(dna, DNACARD);
    }

    public long calcFitness() {
        return fun.f(this.cloneDNA());
    }

    public static void setFun(NumFunInterface fun) {
        Fellow.fun = fun;
    }

}
