package eu.labrush.moto.genetic;

import eu.labrush.agenetic.AbstractFellow;
import eu.labrush.agenetic.AbstractFellowFactory;

public class MotoFactory extends AbstractFellowFactory {
    public MotoFactory() {
        super(2, Moto.AskedDNASIZE);
    }

    @Override
    public AbstractFellow newInstance() {
        return new Moto();
    }

    @Override
    public AbstractFellow newInstance(int[] dna) {
        try {
            return new Moto(dna);
        } catch (Exception e) {
            e.printStackTrace();
            return null ;
        }
    }
}
