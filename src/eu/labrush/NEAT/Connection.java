package eu.labrush.NEAT;

public class Connection implements Cloneable {

    public Node from  ;
    public Node to ;

    public int evolutionNumber = 0 ;

    public double weight ;
    public boolean enabled = true ;

    public Connection(Node from, Node to, int evolutionNumber) {
        if(from.above(to)){
            this.to = from;
            this.from = to;
        } else {
            this.from = from;
            this.to = to;
        }

        this.evolutionNumber = evolutionNumber ;

        this.weight = Math.random() * (Config.MAX_CONNECTION_VALUE - Config.MIN_CONNECTION_VALUE) + Config.MIN_CONNECTION_VALUE ;
    }

    @Override
    protected Connection clone() {
        Connection c = new Connection(from, to, evolutionNumber);
        c.weight = weight ;
        c.enabled = enabled ;
        return c;
    }

    /*@Override
    public String toString() {
        return "Connection{" +
                "from=" + from +
                ", to=" + to +
                ", #=" + evolutionNumber +
                ", weight=" + weight +
                ", enabled=" + enabled +
                '}';
    }*/

    @Override
    public String toString() {
        return "{ " + from.id + " -> " + to.id + "}\n" ;
    }
}
