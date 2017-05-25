package eu.labrush.car.neural;

import eu.labrush.agenetic.AbstractFellow;
import eu.labrush.neural.AbstractWeightEncoder;

public class BinaryWeightEncoder extends AbstractWeightEncoder {

    public BinaryWeightEncoder() {
        hidden_layers = 1;
        node_per_layer = 100;

        bits_per_weight = 2 ;

        outputs = 6 ; // Left center right / faster steady slower
        detectors = 8 ;
    }

    @Override
    public int requiredDNASize() {
        return node_per_layer * (detectors + node_per_layer * hidden_layers + outputs) * bits_per_weight + numberOfBiasedNodes();
    }

    @Override
    public double readWeight(int pos, AbstractFellow f) {
        return f.getDNA(pos) + f.getDNA(pos + 1) - 1 ;
    }

    @Override
    public double readBias(int pos, AbstractFellow f) {
        return (1 + f.getDNA(pos)) * (f.getDNA(pos + 1) == 1 ? 1 : -1) ; // bias is -2, -1, 1 or 2
    }

}
