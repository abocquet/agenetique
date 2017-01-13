package eu.labrush.car.neural;

import eu.labrush.agenetic.AbstractFellow;

public class BinaryWeightEncoder extends AbstractWeightEncoder {

    public BinaryWeightEncoder() {
        hiddenLayers = 1;
        nodePerLayers = 4;
        bitsPerWeight = 2 ;
    }

    @Override
    public int requiredDNASize() {
        return nodePerLayers * (detectors + nodePerLayers * hiddenLayers + outputs) * bitsPerWeight ;
    }

    @Override
    double readWeight(int pos, AbstractFellow f) {
        return f.getDNA(pos) + f.getDNA(pos + 1) - 1 ;
    }

}
