package eu.labrush.car.genetic;

import eu.labrush.agenetic.AbstractFellowFactory;
import eu.labrush.agenetic.AbstractNature;
import eu.labrush.agenetic.operators.MutationInterface;
import eu.labrush.car.genetic.operators.ReproductionOperator;

public class Nature extends AbstractNature {

    public Nature(int POPSIZE, double PCROSSOVER, double PMUTATION, AbstractFellowFactory factory, ReproductionOperator ro, MutationInterface mo) {
        super(POPSIZE, PCROSSOVER, PMUTATION, factory, ro, mo);
    }

}
