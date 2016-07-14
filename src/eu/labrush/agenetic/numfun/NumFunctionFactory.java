package eu.labrush.agenetic.numfun;

import eu.labrush.agenetic.AbstractFellow;
import eu.labrush.agenetic.AbstractFellowFactory;

public class NumFunctionFactory extends AbstractFellowFactory {

    private NumFunction fun ;

    public NumFunctionFactory(NumFunction fun) {
        this.setDNACard(2);
        this.setDNASize(fun.getDNASIZE());
        this.fun = fun ;
    }

    @Override
    public AbstractFellow newInstance() {
        Fellow newFellow = new Fellow(this.getDNASize(), this.getDNACard());
        newFellow.setFun(fun);

        return newFellow ;
    }

    @Override
    public AbstractFellow newInstance(int[] dna) {
        Fellow newFellow = new Fellow(dna, this.getDNACard());
        newFellow.setFun(fun);

        return newFellow ;
    }
}