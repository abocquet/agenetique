package eu.labrush.NEAT;

import java.util.ArrayList;
import java.util.List;

public class Mutation {

    static void connectionMutation(Fellow f){

        Node from = null, to = null ;
        List<Node> nodes = new ArrayList<>(f.getNodes().values());

        int trials = 0 ; // We don't try for too long, especially at the begginng, new connections cannot be added
        int trial_limit = 10 ;

        do {
            from = nodes.get(random(f.getNodes().size() - 1));
            to = nodes.get(random(f.getNodes().size() - 1));
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

    static void nodeMutation(Fellow f){

        List<Integer> keys = new ArrayList<>(f.getConnections().keySet());

        int n = keys.get(random(f.getConnections().size() - 1)); //The index of the connection on which we are going to addFellow the node
        Connection c = f.getConnections().get(n);

        c.enabled = false ;

        Node newNode = Node.avg(c.from, c.to, Fellow.nextInnovationNumber());
        f.addNode(newNode);
        f.addConnection(new Connection(c.from,  newNode, Fellow.nextInnovationNumber()));
        f.addConnection(new Connection(newNode, c.to, Fellow.nextInnovationNumber()));
    }

    static int random(int max){ // return random number between 0 and max
        return ((int) (Math.random() * (double) (max + 1))) % max ;
    }

}
