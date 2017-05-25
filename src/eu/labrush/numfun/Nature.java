package eu.labrush.numfun;

import eu.labrush.agenetic.AbstractNature;
import eu.labrush.agenetic.operators.crossover.OnePointCrossover;
import eu.labrush.agenetic.operators.mutation.DefaultMutationOperator;
import eu.labrush.agenetic.operators.selection.BiasedWheelSelector;

class Nature extends AbstractNature {

    public Nature(int POPSIZE, double PCROSSOVER, double PMUTATION, NumFunction fun) {
        super(POPSIZE, 1, PCROSSOVER, PMUTATION, 0, new NumFunctionFactory(fun), new OnePointCrossover(), new DefaultMutationOperator(), new BiasedWheelSelector());
    }
}
