package eu.labrush.Polynome;

import eu.labrush.AbstractNature;

public class Nature extends AbstractNature {

    public Nature(int POPSIZE, double PCROSSOVER, double PMUTATION) {
        super(POPSIZE, PCROSSOVER, PMUTATION);
        setFellowType(Fellow.class);
    }
}
