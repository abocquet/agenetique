package eu.labrush.neural;

import eu.labrush.agenetic.AbstractFellow;

public abstract class AbstractWeightEncoder {

    protected int inputs;
    protected int outputs ;

    protected int hiddenLayers;
    protected int nodePerLayers ;
    protected int bitsPerWeight ;

    public abstract int requiredDNASize();

    public abstract double readWeight(int pos, AbstractFellow f);

    public double[][][] getWeights(AbstractFellow f) {
        double weights[][][] = new double[hiddenLayers + 1][][];

        int pos = 0;

        weights[0] = new double[inputs][nodePerLayers];
        for (int i = 0; i < inputs; i++) {
            for (int j = 0; j < nodePerLayers; j++) {
                weights[0][i][j] = readWeight(pos, f);
                pos += bitsPerWeight;
            }
        }

        for (int k = 1; k < hiddenLayers; k++) {
            weights[k] = new double[nodePerLayers][nodePerLayers];

            for (int i = 0; i < nodePerLayers; i++) {
                for (int j = 0; j < nodePerLayers; j++) {
                    weights[k][i][j] = readWeight(pos, f);

                    pos += bitsPerWeight;
                }
            }
        }

        weights[hiddenLayers] = new double[nodePerLayers][outputs];
        for (int i = 0; i < nodePerLayers; i++) {
            for (int j = 0; j < outputs; j++) {
                weights[hiddenLayers][i][j] = readWeight(pos, f);
                pos += bitsPerWeight;
            }
        }

        return weights;
    }

    public int getInputs() {
        return inputs;
    }

    public int getOutputs() {
        return outputs;
    }
}
