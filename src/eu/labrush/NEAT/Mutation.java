package eu.labrush.NEAT;

import java.util.ArrayList;
import java.util.List;

import static eu.labrush.NEAT.Random.random;

public class Mutation {

    static void addConnectionMutation(Fellow f){

        Node from, to;
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

        f.addConnection(new Connection(from, to));
    }

    static void addNodeMutation(Fellow f){

        List<Integer> keys = new ArrayList<>(f.getConnections().keySet());

        if(keys.size() == 0){
            addConnectionMutation(f);
            return;
        }

        int n = keys.get(random(f.getConnections().size() - 1)); //The index of the connection on which we are going to addFellow the node
        Connection c = f.getConnections().get(n);

        c.enabled = false ;

        Node newNode = Node.avg(c.getFrom(), c.getTo());
        f.addNode(newNode);
        f.addConnection(new Connection(c.getFrom(),  newNode));
        f.addConnection(new Connection(newNode, c.getTo()));
    }

    public static void delConnectionMutation(Fellow f) {
        if(f.getConnections().size() == 0){
            return;
        }

        f.removeConnection((Connection) random(f.getConnections().values()));
    }


    public static void delNodeMutation(Fellow f) {
        Node n = (Node) random(f.getNodes().values());

        if((n.numerator == 1 && n.denominator == 1) || (n.numerator == 0 && n.denominator == 1)){
            return ;
        }

        f.removeNode(n);
    }

    public static void changeNodeBias(Fellow f){
        Node n = (Node) Random.random(f.getNodes().values());
        n.bias = Math.random() * (Config.MAX_NODE_BIAS - Config.MIN_NODE_BIAS) + Config.MIN_NODE_BIAS ;
    }

}
