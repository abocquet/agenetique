package eu.labrush.g2048;


import eu.labrush.agenetic.AbstractFellow;
import eu.labrush.agenetic.AbstractFellowFactory;


public class PlayerFactory extends AbstractFellowFactory {

    PlayerWeightEncoder we = new PlayerWeightEncoder();

    @Override
    public AbstractFellow newInstance() {
        return new Player(we.requiredDNASize(), 2, we);
    }

    @Override
    public AbstractFellow newInstance(int[] dna) {
        return new Player(dna, we.requiredDNASize(), we) ;
    }
}
