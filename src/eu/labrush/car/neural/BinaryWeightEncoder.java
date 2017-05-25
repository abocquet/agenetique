package eu.labrush.car.neural;

import eu.labrush.agenetic.AbstractFellow;
import eu.labrush.neural.AbstractWeightEncoder;

public class BinaryWeightEncoder extends AbstractWeightEncoder {

    public BinaryWeightEncoder() {
        hiddenLayers = 1;
        nodePerLayers = 100;
        bitsPerWeight = 2 ;

        outputs = 6 ; // Left center right / faster steady slower
        inputs = 8 ;
    }

    @Override
    public int requiredDNASize() {
        return nodePerLayers * (inputs + nodePerLayers * hiddenLayers + outputs) * bitsPerWeight ;
    }

    @Override
    public double readWeight(int pos, AbstractFellow f) {
        return f.getDNA(pos) + f.getDNA(pos + 1) - 1 ;
    }

}
