package eu.labrush.car.genetic;

import eu.labrush.agenetic.AbstractFellow;

import java.util.Arrays;

public class Driver extends AbstractFellow{

    private static final int hiddenLayers = 1;
    private static final int nodePerLayers = 16;
    private static final int amplitude = 2; //The weight of each links goes from -amplitude to +amplitude
    private static final int detectors = 8;
    private static final int outputs = 2 ; //Left or right
    private static final int bitsPerWeight = 3;

    private double distance = 0 ;

    public Driver(){
        super(
                (int)Math.ceil(
                        ((double)
                                nodePerLayers * (detectors + nodePerLayers * (hiddenLayers - 1) + outputs) * bitsPerWeight
                        )
                ),
                2
        );
    }

    protected Driver(int[] dna) {
        super(dna, 2);
    }

    @Override
    protected int calcFitness() {
        return (int) distance ;
    }

    public void increaseDistance(double d){
        this.distance += d ;
    }

    public double[][][] getWeights() {
        double weights[][][] = new double[hiddenLayers + 1][][] ;

        int pos = 0 ;

        weights[0] = new double[detectors][nodePerLayers];
        for(int i = 0 ; i < detectors ; i++){
            for(int j = 0 ; j < nodePerLayers ; j++){
                weights[0][i][j] = readWeight(pos);

                pos += bitsPerWeight ;
            }
        }

        for(int k = 1 ; k < hiddenLayers ; k++) {
            weights[k] = new double[nodePerLayers][nodePerLayers];

            for (int i = 0; i < nodePerLayers; i++) {
                for (int j = 0; j < nodePerLayers; j++) {
                    weights[j][i][j] = readWeight(pos);

                    pos += bitsPerWeight;
                }
            }
        }

        weights[hiddenLayers] = new double[nodePerLayers][outputs];
        for(int i = 0 ; i < nodePerLayers ; i++){
            for(int j = 0 ; j < outputs ; j++){
                weights[hiddenLayers][i][j] = readWeight(pos);
                pos += bitsPerWeight ;
            }
        }

        return weights;
    }

    private double readWeight(int pos){
        return 2 * amplitude * readIntFromDNA(this, pos, bitsPerWeight) / Math.pow(2, bitsPerWeight) - (double)amplitude;
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
