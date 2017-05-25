package eu.labrush.NEAT.operators;

import eu.labrush.NEAT.Config;
import eu.labrush.NEAT.fellow.Connection;
import eu.labrush.NEAT.fellow.Fellow;
import eu.labrush.NEAT.fellow.Node;
import eu.labrush.NEAT.utils.Random;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

<<<<<<< HEAD:src/eu/labrush/NEAT/operators/Mutation.java
import static eu.labrush.NEAT.utils.Random.random;

=======
>>>>>>> parent of f4a8b74... amélioration de NEAT:src/eu/labrush/NEAT/Mutation.java
public class Mutation {

    public static void addConnectionMutation(Fellow f){

        Node from = null, to = null ;
        List<Node> nodes = new ArrayList<>(f.getNodes().values());

        int trials = 0 ; // We don't try for too long, especially at the begginng, new connections cannot be added
        int trial_limit = 10 ;

        do {
            from = (Node) random(nodes);
            to   = (Node) random(nodes);
            trials++ ;
        } while(
            from.frontOf(to) && trials < trial_limit
        );

        if(from.frontOf(to)){
            return ;
        }

        if(from.above(to) ){ // Pour éviter les cycles, (Démonstration par l'absurde: a -> b -> c -> a => a < b < c < a => a < a => absurde)
            Node tmp = from ;
            from = to ;
            to = tmp ;
        }

        f.addConnection(new Connection(from, to, Fellow.nextInnovationNumber()));
    }

    public static void addNodeMutation(Fellow f){

        List<Integer> keys = new ArrayList<>(f.getConnections().keySet());

        int n = keys.get(random(f.getConnections().size() - 1)); //The index of the connection on which we are going to addFellow the node
        Connection c = f.getConnections().get(n);

        c.enabled = false ;

<<<<<<< HEAD:src/eu/labrush/NEAT/operators/Mutation.java
        Node newNode = new Node(c.getFrom(), c.getTo());
=======
        Node newNode = Node.avg(c.from, c.to, Fellow.nextInnovationNumber());
>>>>>>> parent of f4a8b74... amélioration de NEAT:src/eu/labrush/NEAT/Mutation.java
        f.addNode(newNode);
        f.addConnection(new Connection(c.from,  newNode, Fellow.nextInnovationNumber()));
        f.addConnection(new Connection(newNode, c.to, Fellow.nextInnovationNumber()));
    }

    public static void delConnectionMutation(Fellow f) {
        f.removeConnection((Connection) random(f.getConnections().values()));
    }


    public static void delNodeMutation(Fellow f) {
        f.removeNode((Node) random(f.getNodes().values()));
    }

    /**
     * @param max
     * @return a number in [0, max - 1]
     */
    static int random(int max){
        if(max == 0){
            return 0 ;
        }

        return ((int) (Math.random() * (double) (max + 1))) % max ;
    }

    static Object random(List u){
        return u.get(random(u.size()));
    }

    static Object random(Collection u){
        return random(new ArrayList(u));
    }

}
