package eu.labrush.neural;

import java.util.ArrayList;

public class Network {

    private Layer in ;
    private Layer out ;
    private ArrayList<Layer> layers = new ArrayList<>();


    public Network(int nbFirst) {
        this.in = new Layer(nbFirst);
        this.out = in ;
    }

    public double[] compute(double data[]){
        in.compute(data);
        return out.getValues();
    }

    public void addLayer(double[][] weights){
        Layer layer = new Layer(weights[0].length);

        out.setWeights(weights);
        out.setNext(layer);

        layers.add(layer);
        out = layer ;
    }
}
