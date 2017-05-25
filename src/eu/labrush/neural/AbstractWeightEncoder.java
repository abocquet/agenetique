package eu.labrush.neural;

import eu.labrush.agenetic.AbstractFellow;

public abstract class AbstractWeightEncoder {

    protected int detectors ;
    protected int outputs ;

    protected int hidden_layers;
    protected int node_per_layer;

    protected int bits_per_weight;
    protected int bits_per_node ;

    public abstract int requiredDNASize();

    public abstract double readWeight(int pos, AbstractFellow f);
    public abstract double readBias(int pos, AbstractFellow f);

    public double[][][] getWeights(AbstractFellow f) {
        double weights[][][] = new double[hidden_layers + 1][][];

        int pos = 0;

        weights[0] = new double[detectors][node_per_layer];
        for (int i = 0; i < detectors; i++) {
            for (int j = 0; j < node_per_layer; j++) {
                weights[0][i][j] = readWeight(pos, f);
                pos += bits_per_weight;
            }
        }

        for (int k = 1; k < hidden_layers; k++) {
            weights[k] = new double[node_per_layer][node_per_layer];

            for (int i = 0; i < node_per_layer; i++) {
                for (int j = 0; j < node_per_layer; j++) {
                    weights[k][i][j] = readWeight(pos, f);

                    pos += bits_per_weight;
                }
            }
        }

        weights[hidden_layers] = new double[node_per_layer][outputs];
        for (int i = 0; i < node_per_layer; i++) {
            for (int j = 0; j < outputs; j++) {
                weights[hidden_layers][i][j] = readWeight(pos, f);
                pos += bits_per_weight;
            }
        }

        return weights;
    }

    public double[][] getBias(AbstractFellow f) {
        double bias[][] = new double[hidden_layers + 1][] ;
        int pos = detectors * bits_per_weight + hidden_layers * node_per_layer * bits_per_weight ;

        for (int i = 0; i < bias.length ; i++) {
            bias[i] = new double[node_per_layer];
            for (int j = 0; j < node_per_layer; j++) {
                bias[i][j] = readBias(pos, f);
                pos += bits_per_node ;
            }
        }

        bias[hidden_layers] = new double[outputs];
        for (int j = 0; j < outputs; j++) {
            bias[hidden_layers][j] = readBias(pos, f);
            pos += bits_per_node ;
        }

        return bias ;
    }


    protected int numberOfBiasedNodes(){
        return outputs + hidden_layers * node_per_layer;
    }
}
