package eu.labrush.NEAT.operators;

import eu.labrush.NEAT.fellow.Connection;
import eu.labrush.NEAT.fellow.Fellow;
import eu.labrush.NEAT.fellow.Node;

import java.util.Map;

public class Crossover {

    /**
     * We assume that f1 and f2 have the same sensors and outputs
     */
    public static Fellow crossover(Fellow f1, Fellow f2){

        if(f2.getFitness() > f1.getFitness()){
            Fellow tmp = f2 ;
            f2 = f1 ;
            f1 = tmp ;
        }

        Fellow child = new Fellow();
        for (Map.Entry<Integer, Node> e: f1.getNodes().entrySet()) {
            int k = e.getKey() ;
            Node n1 = e.getValue();

            if(f2.getNodes().containsKey(k)){
                child.addNode(crossover(n1, f2.getNodes().get(k)));
            } else {
                child.addNode(n1.clone());
            }
        }

        for(Map.Entry<Integer, Connection> e: f1.getConnections().entrySet()){
            int k = e.getKey();
            Connection c1 = e.getValue();

            if(f2.getConnections().containsKey(k)){
                child.addConnection(crossover(c1, f2.getConnections().get(k)));
            } else {
                child.addConnection(c1.clone());
            }

        }

        return child ;
    }

    static Node crossover(Node n1, Node n2){
        Node n = n1.clone();

        assert n1.type == n2.type;
        assert n1.getId() == n2.getId();
        assert n1.numerator == n2.numerator;
        assert n1.denominator == n2.denominator;

        n.bias = Math.random() <= .5 ? n1.bias : n2.bias ;

        return n ;
    }

    static Connection crossover(Connection c1, Connection c2){
        Connection c = c1.clone() ;

        assert c1.id == c2.id ;

        c.weight  = Math.random() <= .5 ? c1.weight  : c2.weight  ;
        c.enabled = Math.random() <= .5 ? c1.enabled : c2.enabled ;

        return c ;
    }

}
