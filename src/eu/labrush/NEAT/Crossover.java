package eu.labrush.NEAT;

class Crossover {

    private static final double PDISABLE = 0.7 ; //Probabilty that a connection is disbaled on the child if it is on one and only one of the two parents

    /**
     * We assume that f1 and f2 have the same sensors and outputs
     */
    static Fellow crossover(Fellow f1, Fellow f2){

        Fellow child = new Fellow();
        for (int i = 0; i < Fellow.getEvolutionNumber(); i++) {
            if(f1.getNodes().keySet().contains(i)){
                child.addNode(f1.getNodes().get(i), i);
            } else if(f2.getNodes().keySet().contains(i)){
                child.addNode(f2.getNodes().get(i), i);
            }
        }

        for (int i = 0; i < Fellow.getEvolutionNumber(); i++) {

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
                if(!f2.getConnections().get(i).enabled && Math.random() <= PDISABLE){
                    c.enabled = false ;
                }

                c.weight = Math.random() <= 0.5 ? f2.getConnections().get(i).weight : c.weight ;

            } else if(f1.getConnections().keySet().contains(i)){
                child.addConnection(f1.getConnections().get(i).clone());
            } else if(f2.getConnections().keySet().contains(i)){
                child.addConnection(f2.getConnections().get(i).clone());
            }

        }

        return child ;
    }

}
