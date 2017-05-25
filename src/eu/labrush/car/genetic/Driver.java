package eu.labrush.car.genetic;

import eu.labrush.agenetic.AbstractFellow;
import eu.labrush.neural.AbstractWeightEncoder;

public class Driver extends AbstractFellow {

    private double distance = -1 ;
    private AbstractWeightEncoder we ;

    public Driver(int DNASIZE, int DNACARD, AbstractWeightEncoder we) {
        super(DNASIZE, DNACARD);
        this.we = we ;
    }

    protected Driver(int[] dna, AbstractWeightEncoder we) {
        super(dna, 2);
        this.we = we ;
    }

    public long getFitness(){
        return this.calcFitness();
    }

    @Override
    protected long calcFitness() {
        return (long) this.distance;

    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double[][][] getWeights() {
        return this.we.getWeights(this);
    }

    public double[][] getBias() {
        return this.we.getBias(this);
    }
}
