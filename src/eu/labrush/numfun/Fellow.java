package eu.labrush.numfun;

import eu.labrush.AbstractFellow;

public class Fellow extends AbstractFellow {

    static private NumFunction fun ;

    public Fellow() {
        super();
    }

    public Fellow(int[] dna){
        super(dna);
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
