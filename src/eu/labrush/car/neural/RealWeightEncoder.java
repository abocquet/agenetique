package eu.labrush.car.neural;

import eu.labrush.agenetic.AbstractFellow;
import eu.labrush.neural.AbstractWeightEncoder;

public class RealWeightEncoder extends AbstractWeightEncoder {

    public RealWeightEncoder() {
        detectors = 8;
        outputs = 6;

        hidden_layers = 1;
        node_per_layer = 100;
        bits_per_weight = 8;
    }

    private double weight_amplitude = 2 ;
    private double node_amplitude = 3 ;

    @Override
    public int requiredDNASize() {
        return (int)Math.ceil(((double) node_per_layer * (detectors + node_per_layer * (hidden_layers - 1) + outputs) * bits_per_weight));
    }

    @Override
    public  double readWeight(int pos, AbstractFellow f){
        return 2 * weight_amplitude * readIntFromDNA(f, pos, bits_per_weight) / Math.pow(2, bits_per_weight) - weight_amplitude;
    }


    public double readBias(int pos, AbstractFellow f){
        return 2 * node_amplitude * readIntFromDNA(f, pos, bits_per_weight) / Math.pow(2, bits_per_weight) - node_amplitude;
    }

    private int readIntFromDNA(AbstractFellow f, int start, int length) {
        int x = 0 ;
        int tmp = 1 ;
        for(int i = start ; i < start + length ; i++){
            if(f.getDNA(i) == 1) {
                x += tmp ;
            }
            tmp *= 2 ;
        }

        return x;
    }
}
