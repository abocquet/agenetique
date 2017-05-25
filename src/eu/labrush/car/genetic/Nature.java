package eu.labrush.car.genetic;

import eu.labrush.agenetic.AbstractFellow;
import eu.labrush.agenetic.AbstractFellowFactory;
import eu.labrush.agenetic.AbstractNature;
import eu.labrush.agenetic.operators.CrossoverInterface;
import eu.labrush.agenetic.operators.MutationInterface;
import eu.labrush.agenetic.operators.SelectorInterface;
import eu.labrush.race_simulation.DriverInterface;
import eu.labrush.race_simulation.DriverManagerInterface;

public class Nature extends AbstractNature implements DriverManagerInterface {

    public Nature(int POPSIZE, int ELITISM, double PCROSSOVER, double PMUTATION, double PINSERTION, AbstractFellowFactory factory, CrossoverInterface ro, MutationInterface mo, SelectorInterface so) {
        super(POPSIZE, ELITISM, PCROSSOVER, PMUTATION, PINSERTION, factory, ro, mo, so);
    }


    @Override
    public DriverInterface newDriver() {
        return (DriverInterface) getFactory().newInstance();
    }

    public DriverInterface[] getDrivers(){
        Driver[] drivers = new Driver[getPOPSIZE()];
        AbstractFellow[] pop = getPopulation();

        for (int i = 0; i < getPOPSIZE(); i++) {
            drivers[i] = (Driver) pop[i] ;
        }

        return drivers ;
    }
}
