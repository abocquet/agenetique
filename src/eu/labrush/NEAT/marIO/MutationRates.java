package eu.labrush.NEAT.marIO;

public class MutationRates implements Cloneable{
    public double connections;
    public double link;
    public double bias;
    public double node;
    public double enable;
    public double disable;
    public double step;

    @Override
    protected MutationRates clone() {
        MutationRates m = new MutationRates() ;
        m.connections = this.connections ;
        m.link = this.link ;
        m.bias = this.bias ;
        m.node = this.node ;
        m.disable = this.disable ;
        m.step = this.step ;

        return m ;
    }
}
