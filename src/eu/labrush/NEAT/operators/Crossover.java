package eu.labrush.NEAT.operators;

import eu.labrush.NEAT.Config;
import eu.labrush.NEAT.fellow.Connection;
import eu.labrush.NEAT.fellow.Fellow;
import eu.labrush.NEAT.fellow.Node;

public class Crossover {

    /**
     * We assume that f1 and f2 have the same sensors and outputs
     */
    public static Fellow crossover(Fellow f1, Fellow f2){

        Fellow child = new Fellow();
        for (int i = 0; i <= Node.indexer.current(); i++) {
            if(f1.getNodes().keySet().contains(i)){
                child.addNode(f1.getNodes().get(i));
            } else if(f2.getNodes().keySet().contains(i)){
                child.addNode(f2.getNodes().get(i));
            }
        }

        for (int i = 0; i < Connection.indexer.current(); i++) {

            if(f1.getConnections().keySet().contains(i) && f2.getConnections().keySet().contains(i)) {

                Connection c = f1.getConnections().get(i).clone();

                /*
                    f1 f2 rand res
                     0  0    0   0
                     0  1    0   0
                     1  1    0   1
                     0  0    1   0
                     0  1    1   1
                     1  1    1   1
                 */
                if(!f2.getConnections().get(i).enabled && Math.random() <= Config.P_NODE_DISABLE){
                    c.enabled = false ;
                }

                c.weight = Math.random() <= 0.5 ? f2.getConnections().get(i).weight : c.weight ;
                child.addConnection(c);

            } else if(f1.getConnections().keySet().contains(i)){
                child.addConnection(f1.getConnections().get(i).clone());
            } else if(f2.getConnections().keySet().contains(i)){
                child.addConnection(f2.getConnections().get(i).clone());
            }

        }

        return child ;
    }

}
