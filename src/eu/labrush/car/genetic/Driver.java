package eu.labrush.car.genetic;

import eu.labrush.agenetic.AbstractFellow;
import eu.labrush.race_simulation.DriverInterface;
import eu.labrush.neural.AbstractWeightEncoder;
import eu.labrush.neural.NeuralNetwork;

public class Driver extends AbstractFellow implements DriverInterface {

    private double distance = -1 ;
    private AbstractWeightEncoder we ;

    private NeuralNetwork brain ;


    public Driver(int DNASIZE, int DNACARD, AbstractWeightEncoder we) {
        super(DNASIZE, DNACARD);
        this.we = we ;
    }

    protected Driver(int[] dna, AbstractWeightEncoder we) {
        super(dna, 2);
        this.we = we ;
    }

    /**
     * AG
     */

    public long getFitness(){
        return this.calcFitness();
    }

    @Override
    protected long calcFitness() {
        return (long) this.distance;

    }


    /**
     * Neural Network / Driver Interface
     */

    public void initBrain(){
        double weights[][][] = getWeights() ;

        this.brain = new NeuralNetwork(weights[0].length);
        for(int i = 0 ; i < weights.length ; i++){
            this.brain.addLayer(weights[i]);
        }
    }

    public double[] thinkAbout(double[] inputs){
        return brain.compute(inputs);
    }

    @Override
    public int nbInputs() {
        return we.getInputs();
    }


    public double[][][] getWeights() {
        return this.we.getWeights(this);
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
