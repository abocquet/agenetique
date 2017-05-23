package eu.labrush.NEAT;

import eu.labrush.agenetic.Tuple;

import java.util.HashMap;

public class Connection implements Cloneable {

    private Node from  ;
    private Node to ;

    public int id = 0 ;

    public double weight ;
    public boolean enabled = true ;

    private static HashMap<Tuple<Integer, Integer>, Integer> connections = new HashMap<>();
    private static int current_id = 0 ;

    public Connection(Node from, Node to) {
        this(from, to, nextId());
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
     * ID Management
     */

    private static int nextId(){
        current_id++ ;
        return current_id ;
    }

    public static int getCurrentId(){
        return current_id ;
    }

    /**
     * NEAT
     */

    public void randomWeight() {
        this.weight = Random.gauss(Config.STDEV_CONNECTION_WEIGHT, Config.MIN_CONNECTION_WEIGHT, Config.MAX_CONNECTION_WEIGHT);
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
    protected Connection clone() {
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
        return "{ " + from.getId() + " -> " + to.getId() + "}\n" ;
    }
}
