package eu.labrush.NEAT.marIO;

import java.util.ArrayList;

class Species {

    public double topFitness = 0;
    public int staleness     = 0;
    public ArrayList<Genome> genomes = new ArrayList<>();
    public double averageFitness = 0 ;
}
