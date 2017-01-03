package eu.labrush.car.genetic;

import eu.labrush.agenetic.AbstractFellow;
import eu.labrush.agenetic.AbstractFellowFactory;
import eu.labrush.car.neural.AbstractWeightEncoder;

public class DriverFactory extends AbstractFellowFactory {

    AbstractWeightEncoder we ;

    public DriverFactory(AbstractWeightEncoder we) {
        super(
                2,
                we.requiredDNASize()
        );

        this.we = we ;
    }

    @Override
    public AbstractFellow newInstance() {
        return new Driver(this.getDNASize(), this.getDNACard(), we);
    }

    @Override
    public AbstractFellow newInstance(int[] dna) {
        return new Driver(dna, we);

    }
}
