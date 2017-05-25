package eu.labrush.NEAT.fellow;

<<<<<<< HEAD:src/eu/labrush/NEAT/fellow/Connection.java
import eu.labrush.NEAT.Config;
import eu.labrush.NEAT.utils.Indexer;
import eu.labrush.NEAT.utils.Random;

=======
>>>>>>> parent of f4a8b74... amélioration de NEAT:src/eu/labrush/NEAT/Connection.java
public class Connection implements Cloneable {

    public Node from  ;
    public Node to ;

    public int evolutionNumber = 0 ;

    public double weight ;
    public boolean enabled = true ;

<<<<<<< HEAD:src/eu/labrush/NEAT/fellow/Connection.java
    public static Indexer indexer = new Indexer(0);

    public Connection(Node from, Node to) {
        this(from, to, indexer.next());
    }

    private Connection(Node from, Node to, int id){
=======
    public Connection(Node from, Node to, int evolutionNumber) {
>>>>>>> parent of f4a8b74... amélioration de NEAT:src/eu/labrush/NEAT/Connection.java
        if(from.above(to)){
            this.to = from;
            this.from = to;
        } else {
            this.from = from;
            this.to = to;
        }

        this.evolutionNumber = evolutionNumber ;

<<<<<<< HEAD:src/eu/labrush/NEAT/fellow/Connection.java
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
    public Connection clone() {
        Connection c = new Connection(from, to, id);
=======
       randomWeight();
    }

    @Override
    protected Connection clone() {
        Connection c = new Connection(from, to, evolutionNumber);
>>>>>>> parent of f4a8b74... amélioration de NEAT:src/eu/labrush/NEAT/Connection.java
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

    public void randomWeight() {
        this.weight = Math.random() * (Config.MAX_CONNECTION_VALUE - Config.MIN_CONNECTION_VALUE) + Config.MIN_CONNECTION_VALUE ;
    }
}
