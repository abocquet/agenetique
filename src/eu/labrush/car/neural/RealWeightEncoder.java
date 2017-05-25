package eu.labrush.car.neural;

import eu.labrush.agenetic.AbstractFellow;
import eu.labrush.neural.AbstractWeightEncoder;

public class RealWeightEncoder extends AbstractWeightEncoder {

    public RealWeightEncoder() {
        inputs = 8;
        outputs = 6;

        hiddenLayers = 1;
        nodePerLayers = 100;
        bitsPerWeight = 8;
    }

    private double amplitude = 2 ;

    @Override
    public int requiredDNASize() {
        return (int)Math.ceil(((double) nodePerLayers * (inputs + nodePerLayers * (hiddenLayers - 1) + outputs) * bitsPerWeight));
    }

    @Override
    public  double readWeight(int pos, AbstractFellow f){
        return 2 * amplitude * readIntFromDNA(f, pos, bitsPerWeight) / Math.pow(2, bitsPerWeight) - amplitude;
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
