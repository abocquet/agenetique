package eu.labrush.neural;

import java.util.ArrayList;

public class NeuralNetwork {

    private Layer in ;
    private Layer out ;
    private ArrayList<Layer> layers = new ArrayList<>();


    public NeuralNetwork(int nbFirst) {
        this.in = new Layer(nbFirst);
        this.out = in ;
    }

    public double[] compute(double data[]){
        in.compute(data);
        return out.getValues();
    }

    /**
     * [ [from1 -> to1, from1 -> to2, ... from1 -> to(n)],
     *   [from2 -> to1, from2 -> to2, ... from2 -> to(n)],
     *   ...
     *   [from(n) -> to1, ..., from(n) -> to(n)]
     *
     */
    public void addLayer(double[][] weights){
        Layer layer = new Layer(weights[0].length);

        out.setWeights(weights);
        out.setNext(layer);

        layers.add(layer);
        out = layer ;
    }
}
