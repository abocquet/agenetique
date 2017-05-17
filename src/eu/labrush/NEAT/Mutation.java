package eu.labrush.NEAT;

import java.util.ArrayList;
import java.util.List;

public class Mutation {

    static void connectionMutation(Fellow f){

        Connection c ;
        int n = f.getNodes().size() - 1;

        do { c = new Connection(random(n), random(n), Fellow.getInnovationNumber()); }
        while(!(c.from == c.to) && !f.hasConnection(c));

        Fellow.increaseInnovationNumber();
        f.addConnection(c);
    }

    static void nodeMutation(Fellow f){

        List<Integer> keys = new ArrayList<>(f.getConnections().keySet());

        int n = keys.get(random(f.getConnections().size() - 1)); //The index of the connection on which we are going to add the node
        Connection c = f.getConnections().get(n);

        c.enabled = false ;

        n = Fellow.nextInnovationNumber(); // The number of the node we are inserting
        f.addNode(NodeType.HIDDEN, n);
        f.addConnection(new Connection(c.from,  n, Fellow.nextInnovationNumber()));
        f.addConnection(new Connection(n, c.to, Fellow.nextInnovationNumber()));
    }

    static int random(int max){ // return random number between 0 and max
        return ((int) (Math.random() * (double) (max + 1))) % max ;
    }

}
