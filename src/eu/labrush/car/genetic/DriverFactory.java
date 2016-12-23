package eu.labrush.car.genetic;

import eu.labrush.agenetic.AbstractFellow;
import eu.labrush.agenetic.AbstractFellowFactory;

public class DriverFactory extends AbstractFellowFactory {

    public DriverFactory() {
        super(
                2,
                (int)Math.ceil(
                ((double) Driver.getNodePerLayers() * (Driver.getDetectors() + Driver.getNodePerLayers() * (Driver.getHiddenLayers() - 1) + Driver.getOutputs()) * Driver.getBitsPerWeight()))
        );
    }

    @Override
    public AbstractFellow newInstance() {
        return new Driver(this.getDNASize(), this.getDNACard());
    }

    @Override
    public AbstractFellow newInstance(int[] dna) {
        return new Driver(dna);
    }
}
