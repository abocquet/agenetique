package eu.labrush.car.genetic;

import eu.labrush.agenetic.AbstractFellowFactory;
import eu.labrush.agenetic.AbstractNature;
import eu.labrush.agenetic.operators.CrossoverInterface;
import eu.labrush.agenetic.operators.MutationInterface;
import eu.labrush.agenetic.operators.SelectorInterface;

public class Nature extends AbstractNature {

    public Nature(int POPSIZE, int ELITISM, double PCROSSOVER, double PMUTATION, double PINSERTION, AbstractFellowFactory factory, CrossoverInterface ro, MutationInterface mo, SelectorInterface so) {
        super(POPSIZE, ELITISM, PCROSSOVER, PMUTATION, PINSERTION, factory, ro, mo, so);
    }
}
