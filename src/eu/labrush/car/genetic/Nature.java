package eu.labrush.car.genetic;

import eu.labrush.agenetic.AbstractFellowFactory;
import eu.labrush.agenetic.AbstractNature;

public class Nature extends AbstractNature {

    public Nature(int POPSIZE, double PCROSSOVER, double PMUTATION, AbstractFellowFactory factory) {
        super(POPSIZE, PCROSSOVER, PMUTATION, factory);
    }

}
