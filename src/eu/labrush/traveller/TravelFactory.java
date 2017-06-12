package eu.labrush.traveller;

import eu.labrush.agenetic.AbstractFellow;
import eu.labrush.agenetic.AbstractFellowFactory;
import eu.labrush.traveller.data.PointSet;

public class TravelFactory extends AbstractFellowFactory {
    private final PointSet set;

    public TravelFactory(PointSet set) {
        this.set = set ;
        this.setDNACard(set.getPoints().length);
        this.setDNASize(set.getPoints().length);
    }

    @Override
    public AbstractFellow newInstance() {
        return new Travel(set);
    }

    @Override
    public AbstractFellow newInstance(int[] dna) {
        return newInstance(dna, false);
    }

    public AbstractFellow newInstance(int[] dna, boolean isSafe){
        return new Travel(dna, set, isSafe);
    }
}
