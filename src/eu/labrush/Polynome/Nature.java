package eu.labrush.Polynome;

import eu.labrush.AbstractNature;

public class Nature extends AbstractNature {

    public Nature(int POPSIZE, double PCROSSOVER, double PMUTATION) {
        super();

        this.PMUTATION = PMUTATION;
        this.PCROSSOVER = PCROSSOVER;
        setPOPSIZE(POPSIZE);

        try {
            Fellow.setDNASIZE(8);
        } catch (Exception e) {
            e.printStackTrace();
        }

        setFellowType(Fellow.class);
        initPopulation();
    }
}
