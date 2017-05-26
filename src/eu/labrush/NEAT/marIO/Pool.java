package eu.labrush.NEAT.marIO;

import java.util.ArrayList;

class Pool {

    ArrayList<Species> species = new ArrayList();
    int generation = 0;
    int innovation ;
    int currentSpecies = 1;
    int currentGenome  = 1;
    int currentFrame   = 0;
    double maxFitness  = 0;

    public Pool(int innovation) {
        this.innovation = innovation;
    }
}
