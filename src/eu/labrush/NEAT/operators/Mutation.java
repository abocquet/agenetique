package eu.labrush.NEAT.operators;

import eu.labrush.NEAT.Config;
import eu.labrush.NEAT.fellow.Connection;
import eu.labrush.NEAT.fellow.Fellow;
import eu.labrush.NEAT.fellow.Node;
import eu.labrush.NEAT.utils.Random;

import java.util.ArrayList;
import java.util.List;

import static eu.labrush.NEAT.utils.Random.random;

public class Mutation {

    public static void addConnectionMutation(Fellow f){

        Node from, to;
        List<Node> nodes = new ArrayList<>(f.getNodes().values());

        int trials = 0 ; // We don't try for too long, especially at the begginng, new connections cannot be added
        int trial_limit = 30 ;

        Connection c = new Connection(nodes.get(0), nodes.get(1));
        f.addConnection(c);

        do {
            from = (Node) random(nodes);
            to   = (Node) random(nodes);

            trials++;
        } while(
            trials < trial_limit && f.detectCycle(c)
        );

        if(trials >= trial_limit){
            f.removeConnection(c);
        }
    }

    public static void addNodeMutation(Fellow f){

        List<Integer> keys = new ArrayList<>(f.getConnections().keySet());

        if(keys.size() == 0){
            addConnectionMutation(f);
            return;
        }

        int n = keys.get(Random.randInt(f.getConnections().size() - 1)); //The index of the connection on which we are going to addFellow the node
        Connection c = f.getConnections().get(n);

        c.enabled = false ;

        Node newNode = new Node(c.getFrom(), c.getTo());
        f.addNode(newNode);
        f.addConnection(new Connection(c.getFrom(),  newNode));
        f.addConnection(new Connection(newNode, c.getTo()));
    }

    public static void delConnectionMutation(Fellow f) {
        if(f.getConnections().size() == 0){
            return;
        }

        //f.removeConnection((Connection) random(f.getConnections().values()));
        ((Connection) random(f.getConnections().values())).enabled = false ;
    }

    public static void changeConnectionWeightMutation(Fellow f){
        if(f.getConnections().size() == 0){
            return;
        }

        Connection c = (Connection) Random.random(f.getConnections().values());
        c.randomWeight();
    }

    public static void delNodeMutation(Fellow f) {
        Node n = (Node) random(f.getNodes().values());

        if(n.denominator != 1){
            f.removeNode(n);
        }
    }

    public static void changeNodeBias(Fellow f){
        Node n = (Node) Random.random(f.getNodes().values());
        n.bias = Random.gauss(Config.STDEV_NODE_BIAS, Config.MAX_NODE_BIAS, Config.MIN_NODE_BIAS) ;
    }



}
