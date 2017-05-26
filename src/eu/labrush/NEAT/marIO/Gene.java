package eu.labrush.NEAT.marIO;

public class Gene implements Cloneable {
    public int into = 0;
    public int out  = 0;
    public double weight = 0.;
    public boolean enabled = true;
    public int innovation  = 0;

    public Gene clone() {
        Gene g = new Gene();
        g.into = this.into;
        g.out = this.out;
        g.weight = this.weight;
        g.enabled = this.enabled;
        g.innovation = this.innovation;

        return g;
    }

}
