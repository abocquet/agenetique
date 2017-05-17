package eu.labrush.NEAT;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Fellow {

    private HashMap<Integer, NodeType> nodes = new HashMap<>() ;
    private HashMap<Integer, Connection> connections = new HashMap<>();

    private double fitness = 0 ;

    static private int globalEvolutionNumber = 0 ;

    public Fellow() {}
    public Fellow(int sensors, int outputs){
        for (int i = 0; i < sensors; i++) {
            this.getNodes().put(Fellow.nextEvolutionnaryNumber(), NodeType.SENSOR);
        }

        for (int i = 0; i < outputs; i++) {
            this.getNodes().put(Fellow.nextEvolutionnaryNumber(), NodeType.OUTPUT);

            for (int j = 0; j < sensors; j++) {
                int n = Fellow.nextEvolutionnaryNumber();
                this.connections.put(n, new Connection(j, i, n));
            }
        }
    }


    /*************************
        Topology management
     *************************/
    void addNode(NodeType t, int number){
        nodes.put(number, t);
    }

    public HashMap<Integer, NodeType> getNodes() {
        return nodes;
    }

    public HashMap<Integer, Connection> getConnections() {
        return connections;
    }

    public void addConnection(Connection c) {
        this.connections.put(c.evolutionNumber, c);
    }

    boolean hasConnection(Connection c0){

        Iterator it = connections.entrySet().iterator();
        while (it.hasNext()) {
            Connection c = (Connection) ((Map.Entry)it.next()).getValue();
            if( (c.from == c0.from && c.to == c0.to) || (c.from == c0.to && c.to == c0.from)){
                return true ;
            }
        }

        return false ;
    }

    /*************************
        Evolution monitoring
     *************************/
    public static int getEvolutionNumber() {
        return globalEvolutionNumber;
    }

    public static void increaseEvolutionNumber(){
        globalEvolutionNumber++ ;
    }

    public static int nextEvolutionnaryNumber() {
        increaseEvolutionNumber();
        return getEvolutionNumber();
    }

    /*************************
        GA stuffs
     *************************/

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    // Cf eq 3.1 in NEAT PhD
    public double distanceTo(Fellow f){

        double D = 0, E = 0, W = 0 ;

        int max = this.getConnections().keySet().stream().reduce(0, Integer::max);
        int c = 0 ;

        for (Integer key: f.connections.keySet()) {

            if(key > max) {
                E++ ;
            } else if(this.getConnections().containsKey(key)){
                c++ ;
                W += Math.abs(f.connections.get(key).weight - this.connections.get(key).weight);
            } else {
                D++ ;
            }

        }

        for (Integer key: this.connections.keySet()){
            if(!f.connections.containsKey(key)){
                D++ ;
            }
        }

        return D + E + 0.3 * W ;

    }

    /*************************
        Neural network
     *************************/

    // On procède récursivement sur les noeuds en utilisant la programmation dynamique
    // ex:
    //         7        8
    //        / \     /
    //       6   \   /
    //      /  \<- 5
    //     4      /  \
    //       \ -> 3   2
    //
    // 7
    //  => 6
    //      => 4
    //          => 3 OK
    //      => 5
    //          => 3 OK
    //          => 2 OK
    //  => 5 OK (mémoisé)
    //  8
    //  => 5 OK (mémoisé)
    //
    public double[] thinkAbout(double[] input){ // Activates neural network

    }

    /*************************
        MISC
     *************************/

    @Override
    protected Fellow clone()  {
        Fellow f = new Fellow();

        f.nodes = (HashMap<Integer, NodeType>) this.nodes.clone();
        f.connections = (HashMap<Integer, Connection>) this.connections.clone();

        return f;
    }

}
