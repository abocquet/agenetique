package eu.labrush.agenetic.numfun;

import eu.labrush.agenetic.AbstractFellow;

public class Fellow extends AbstractFellow {

    static private NumFunction fun ;

    protected Fellow(int DNASIZE, int DNACARD) {
        super(DNASIZE, DNACARD);
    }

    protected Fellow(int[] dna, int DNACARD){
        super(dna, DNACARD);
    }

    public long calcFitness() {
        return fun.getY(this.cloneDNA());
    }

    public String getX(){
        return fun.getArguments(this.cloneDNA()).toString();
    }

    public static NumFunction getFun() {
        return fun;
    }

    public static void setFun(NumFunction fun) {
        Fellow.fun = fun;
    }

}
