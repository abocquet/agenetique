package eu.labrush.traveller;

import eu.labrush.agenetic.AbstractFellow;
import eu.labrush.agenetic.AbstractFellowFactory;
import eu.labrush.traveller.data.Point;

public class TravelFactory extends AbstractFellowFactory {
    private final Point[] places;

    public TravelFactory(Point[] points) {
        this.places = points ;
    }

    @Override
    public AbstractFellow newInstance() {
        return new Travel(places);
    }

    @Override
    public AbstractFellow newInstance(int[] dna) {
        return newInstance(dna, false);
    }

    public AbstractFellow newInstance(int[] dna, boolean isSafe){
        return new Travel(dna, this.places, isSafe);
    }
}
