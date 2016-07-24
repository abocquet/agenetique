package eu.labrush.walker.genetic;

import eu.labrush.agenetic.AbstractFellow;
import eu.labrush.agenetic.AbstractFellowFactory;

public class WalkerFactory extends AbstractFellowFactory {
    public WalkerFactory() {
        super(2, Walker.AskedDNASIZE);
    }

    @Override
    public AbstractFellow newInstance() {
        return new Walker();
    }

    @Override
    public AbstractFellow newInstance(int[] dna) {
        try {
            return new Walker(dna);
        } catch (Exception e) {
            e.printStackTrace();
            return null ;
        }
    }
}
