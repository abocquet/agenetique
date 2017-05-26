package eu.labrush.NEAT.marIO;

import java.util.ArrayList;

@SuppressWarnings("WeakerAccess")
public class Genome implements Cloneable {
    public ArrayList<Gene> genes;
    public double fitness;
    public int adjustedFitness;
    public NN network;
    public int maxneuron;
    public int globalRank;
    public MutationRates mutationRates;

    public Genome clone(){
        Genome g = new Genome();
        for (int i = 0; i < this.genes.size(); i++) {
            g.genes.add(this.genes.get(i).clone());
        }
        g.maxneuron = this.maxneuron;
        g.mutationRates.connections = this.mutationRates.connections;
        g.mutationRates.link = this.mutationRates.link;
        g.mutationRates.bias = this.mutationRates.bias;
        g.mutationRates.node = this.mutationRates.node;
        g.mutationRates.enable = this.mutationRates.enable;
        g.mutationRates.disable = this.mutationRates.disable;

        return g;
    }
}
