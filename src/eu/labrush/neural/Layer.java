package eu.labrush.neural;


import java.util.Arrays;

public class Layer {

    private int neurons  ;
    private double[] values ;
    private Layer next ;

    private double[][] weights ;
    private double[] bias;

    public Layer(int neurons) {
        this.neurons = neurons;
        this.values = new double[neurons] ;
        this.bias = new double[neurons];

        Arrays.fill(bias, 0);
    }

    public void setNext(Layer next){
        this.next = next ;
    }

    public Layer getNext() {
        return next;
    }

    public void setWeights(double[][] weights) {
        this.weights = weights;
    }


    public void setBias(double[] bias) {
        if(bias.length != neurons){
            try {
                throw new Exception("Wrong size for bias : expected " + neurons + " got " + bias.length);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        this.bias = bias;
    }

    private void compute(double[][] weights, double values[]){

        if(weights.length != values.length || weights[0].length != this.neurons){
            System.err.println("Links between this layer and the previous one are not compatible");
        }

        for(int i = 0 ; i < neurons ; i ++){
            for(int j = 0 ; j < values.length ; j++){
                this.values[i] += values[j] * weights[j][i] ;
            }

            this.values[i] = sigma(this.values[i] + this.bias[i]);
        }

        if(next != null){
            next.compute(this.weights, this.values);
        }
    }

    public void compute(double[] values) {
        if(values.length != neurons) {
            System.err.println("Wrong number args passed");
        }

        this.values = values;
        if(next != null){
            next.compute(this.weights, this.values);
        }
    }

    public double[] getValues() {
        return values;
    }

    private double sigma(double x){
        return 1 / (1 + Math.exp(-x));
    }

}
