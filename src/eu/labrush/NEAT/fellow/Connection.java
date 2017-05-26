package eu.labrush.NEAT.fellow;

import eu.labrush.NEAT.Config;
import eu.labrush.NEAT.utils.Indexer;
import eu.labrush.NEAT.utils.Random;

public class Connection implements Cloneable {

    private Node from  ;
    private Node to ;

    public int id = 0 ;

    public double weight ;
    public boolean enabled = true ;

    public static Indexer indexer = new Indexer(0);

    public Connection(Node from, Node to) {
        this(from, to, indexer.next());
    }

    private Connection(Node from, Node to, int id){
        if(from.above(to)){
            this.to = from;
            this.from = to;
        } else {
            this.from = from;
            this.to = to;
        }

        this.id = id ;
        randomWeight();
    }

    /**
     * NEAT
     */

    public void randomWeight() {
        this.weight = Random.gauss(Config.STDEV_CONNECTION_WEIGHT, Config.MAX_CONNECTION_WEIGHT, Config.MIN_CONNECTION_WEIGHT);
    }

    public Node getFrom() {
        return from;
    }

    public Node getTo() {
        return to;
    }

    /**
     * Override
     */

    @Override
    public Connection clone() {
        Connection c = new Connection(from, to, id);
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
        return "{ " + from.getId() + " -> " + to.getId() + "} " + weight +  "\n" ;
    }
}
