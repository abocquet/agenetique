package eu.labrush.car.genetic;

import eu.labrush.agenetic.AbstractFellow;
import eu.labrush.agenetic.AbstractFellowFactory;

public class DriverFactory extends AbstractFellowFactory {

    @Override
    public AbstractFellow newInstance() {
        return new Driver();
    }

    @Override
    public AbstractFellow newInstance(int[] dna) {
        return new Driver(dna);
    }
}
