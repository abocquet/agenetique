package eu.labrush.agenetic.numfun;

import eu.labrush.agenetic.AbstractFellow;

public class Fellow extends AbstractFellow {

    static private NumFunction fun ;

    public Fellow(int DNASIZE, int DNACARD) {
        super(DNASIZE, DNACARD);
    }

    public Fellow(int[] dna, int DNACARD){
        super(dna, DNACARD);
    }

    public int getFitness() {
        return fun.getY(this.getDna());
    }

    public String getX(){
        return this.fun.getArguments(this.getDna()).toString();
    }

    public static NumFunction getFun() {
        return fun;
    }

    public static void setFun(NumFunction fun) {
        Fellow.fun = fun;
    }

}
